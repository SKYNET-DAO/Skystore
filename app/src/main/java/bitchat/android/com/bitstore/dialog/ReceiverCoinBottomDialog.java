package bitchat.android.com.bitstore.dialog;

import android.view.View;
import android.widget.TextView;
import bitchat.android.com.bitstore.R;
import com.android.base.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxClipboardTool;
import me.shaohui.bottomdialog.BaseBottomDialog;

public class ReceiverCoinBottomDialog extends BaseBottomDialog {

    private String mAddress;
    public ReceiverCoinBottomDialog(String address) {
        this.mAddress=address;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_receiver_layout;
    }

    @Override
    public void bindView(View v) {
        Logger.e("----v--->"+v.getId());
        initView(v);
    }

    private void initView(View root){
        TextView address=root.findViewById(R.id.address);
        root.findViewById(R.id.copy).setOnClickListener(v->{
            ToastUtils.show("Copied");
            RxClipboardTool.copyText(getContext(),mAddress);

        });
        address.setText(mAddress);


    }
}
