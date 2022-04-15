package bitchat.android.com.bitstore.bean;

import com.android.base.net.AppConst;

public class CategaryBean {

    private int categoryId;
    private String categoryName;
    private String categoryIcon;

    public String getCategoryIcon() {
        return  AppConst.BASE_URL +categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }



    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
