package bitchat.android.com.bitstore.bean;

import com.android.base.mvvm.viewmodel.Constants;
import com.android.base.net.AppConst;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class HomeDataBean {

    private List<BannerBean> banners;
    private List<DatasBean> datas;

    public List<BannerBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerBean> banners) {
        this.banners = banners;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {


        private int showtype;
        private String label;


        private int columnId;
        private List<AppsBean> apps;

        public int getShowtype() {
            return showtype;
        }

        public void setShowtype(int showtype) {
            this.showtype = showtype;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<AppsBean> getApps() {
            return apps;
        }

        public void setApps(List<AppsBean> apps) {
            this.apps = apps;
        }

        public static class AppsBean implements Serializable {


            private String id;
            private String appsize;
            private String version;
            private String appicon;
            private String appname;
            private int downcount;
            private String downloadurl;

            public String getAppid() {
                return id;
            }

            public void setAppid(String id) {
                this.id = id;
            }

            public String getAppsize() {
                return appsize;
            }

            public void setAppsize(String appsize) {
                this.appsize = appsize;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getAppicon() {
                return AppConst.BASE_URL+appicon;
            }

            public void setAppicon(String appicon) {
                this.appicon = appicon;
            }

            public String getAppname() {
                return appname;
            }

            public void setAppname(String appname) {
                this.appname = appname;
            }

            public int getDowncount() {
                return downcount;
            }

            public void setDowncount(int downcount) {
                this.downcount = downcount;
            }

            public String getDownloadurl() {
                return downloadurl;
            }

            public void setDownloadurl(String downloadurl) {
                this.downloadurl = downloadurl;
            }
        }

        public int getColumnId() {
            return columnId;
        }

        public void setColumnId(int columnId) {
            this.columnId = columnId;
        }
    }



    public class BannerBean  {

        public String getUrl() {
            return AppConst.BASE_URL +url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg() {
            return AppConst.BASE_URL+img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        private String url;

        private String img;
    }










}
