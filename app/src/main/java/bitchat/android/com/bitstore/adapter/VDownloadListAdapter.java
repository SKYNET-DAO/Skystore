
package bitchat.android.com.bitstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.activitys.AppDetailActivity;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.bean.HomeDataBean.DatasBean.AppsBean;
import bitchat.android.com.bitstore.listener.LogDownloadListener;
import bitchat.android.com.bitstore.utils.AppUtils;
import bitchat.android.com.bitstore.utils.SnackbarUtil;
import bitchat.android.com.bitstore.widget.ProgressButton;
import com.android.base.glide.GlideApp;
import com.android.base.net.AppConst;
import com.android.base.utils.FileSizeUtil;
import com.android.base.utils.JsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxFileTool;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;


public class VDownloadListAdapter extends RecyclerView.Adapter<VDownloadListAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<AppsBean> datas;
    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type = 1;

    public VDownloadListAdapter(Context context, List<AppsBean> model) {
        this.context = context;
        this.datas = model;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        updateData(1);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_loadmore, parent, false);
        return new ViewHolder(view);
    }

    public void addMoreData(List<AppsBean> data) {
        if (datas != null) datas.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppsBean bean = datas.get(position);
        if (holder.getTag() == null) {


            DownloadTask task = OkDownload.getInstance().getTask(bean.getAppid());
            if (task == null) {
                String tag= bean.getAppid();
                holder.title.setText(datas.get(position).getAppname());
                long size=Long.parseLong(datas.get(position).getAppsize());
                holder.subtitle2.setText(FileSizeUtil.FormetFileSize(size));
                holder.download.setText(R.string.str_install);
                GlideApp.with(context).load(datas.get(position).getAppicon()).into(holder.icon);
                holder.download.setOnClickListener(v -> {

                    StartDownload(holder, bean, position,tag);
                });
//                holder.bindDefault(datas.get(position));

            } else {
                String tag = createTag(task);
                task.register(new ListDownloadListener(tag, holder))//
                        .register(new LogDownloadListener());
                holder.setTag(tag);
                holder.setTask(task);
                holder.bind();
                holder.refresh(task.progress);
            }

        } else {
            holder.refresh(holder.getTask().progress);

        }

        holder.icon.setOnClickListener(v -> {
            Intent i = new Intent(context, AppDetailActivity.class);
            i.putExtra("appsbean",bean);
            context.startActivity(i);

        });

    }


    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(DownloadTask task) {
        return  task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView subtitle2;
        ProgressButton download;
        private DownloadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle2 = itemView.findViewById(R.id.subtitle2);
            download = itemView.findViewById(R.id.download);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public DownloadTask getTask() {
            return task;
        }

        public void bindDefault(HomeDataBean.DatasBean item) {

//            GlideApp.with(context).load("xxxx").error(R.mipmap.icon_plug).into(icon);
            //ipfs model
//           IPFSManager.getInstance().getIpfsByte(item.getIconUrl())
//                   .subscribe(new RxSubscriber<byte[]>() {
//                       @Override
//                       public void onSuccess(byte[] bytes) {
//                           GlideApp.with(context).load(bytes).error(R.mipmap.icon_plug).into(icon);
//                       }
//                       @Override
//                       protected void onError(ResponseThrowable ex) { }
//                   });


//            name.setText(item.getTitle());
        }


        public void bind() {
            Progress progress = task.progress;
            AppsBean model = (AppsBean) progress.extra1;
            GlideApp.with(context).load(model.getAppicon()).into(icon);
            title.setText(model.getAppname());
            subtitle2.setText(Formatter.formatFileSize(context, progress.currentSize));
        }


        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, progress.totalSize);

            switch (progress.status) {
                case Progress.NONE:
                    download.setText(R.string.str_download);
                    download.setProgress((int) (progress.fraction * 100));
                    break;
                case Progress.PAUSE:
                    download.setText(R.string.str_continue);
                    download.setProgress((int) (progress.fraction * 100));
                    break;
                case Progress.ERROR:

                    download.setText(R.string.str_download_error);
                    break;
                case Progress.WAITING:


                    download.setText(R.string.str_download_wait);
                    download.setEnabled(false);
                    download.setProgress((int) (progress.fraction * 100));
                    break;
                case Progress.FINISH:
                    download.setEnabled(true);

                    if (!RxFileTool.isFileExists(task.progress.filePath)) {
                        Toast.makeText(context, R.string.str_app_del_reinstall, Toast.LENGTH_SHORT).show();
                        download.setText(R.string.str_open_app);
                        download.setOnClickListener(v -> {
                            task.restart();
                        });

                    } else {

                        AppUtils.AppInfo appInfo = AppUtils.apkInfo(progress.filePath, context);
                        download.setText(R.string.str_open_app);
                        download.setOnClickListener(v -> {



                            if (!RxAppTool.isInstallApp(context, appInfo.getPackagename())) {

                                AppUtils.installApk(context, progress.filePath);

                            } else {
                                RxAppTool.launchApp(context, appInfo.getPackagename());
                            }

                        });
                    }

                    break;
                case Progress.LOADING:
                    download.setText(R.string.str_pause);
                    download.setEnabled(false);
                    download.setProgress((int) (progress.fraction * 100));
                    download.setText((int) (progress.fraction * 100) + "%");
                    break;
            }

        }

        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.NONE:
                case Progress.ERROR:
                    task.start();
                    break;
                case Progress.LOADING:
                    task.pause();
                    break;
                case Progress.FINISH:

                    break;
            }
            refresh(progress);
        }

//        @OnClick(R.id.remove)
//        public void remove() {
//            task.remove(true);
//            updateData(type);
//        }

//        @OnClick(R.id.restart)
//        public void restart() {
//            task.restart();
//        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Toast.makeText(context, R.string.str_download_finish + progress.filePath, Toast.LENGTH_SHORT).show();
            updateData(1);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }


    public void StartDownload(ViewHolder holder, AppsBean model, int position, String tag) {



        GetRequest<File> request = OkGo.<File>get(AppConst.BASE_URL+model.getDownloadurl());//
//                .headers("aaa", "111")//
//                .params("bbb", "222");


        DownloadTask task = OkDownload.request(tag, request)//
//                .priority(ipfsModel.getPriority())//
                .extra1(model)//
                .save()//
                .register(new ListDownloadListener(tag, holder));//

        task.start();
        holder.setTag(tag);
        holder.setTask(task);


        notifyItemChanged(position);
    }


    public void updateData(int type) {
        
        this.type = type;
        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH) values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING) values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        notifyDataSetChanged();

    }

}
