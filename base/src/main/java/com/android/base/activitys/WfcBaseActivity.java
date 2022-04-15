package com.android.base.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.base.R;
import com.android.base.language.LanguageUtil;
import com.android.base.mvvm.viewmodel.ErrorEnvelope;
import com.android.base.utils.UIUtils;
import com.orhanobut.logger.Logger;



public abstract class WfcBaseActivity extends AppCompatActivity {
    public Toolbar toolbar;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        beforeViews();
        setContentView(contentLayout());
        toolbar=findViewById(R.id.toolbar);

        if(toolbar!=null){
            if(isCenterTitle()){
                toolbar.setTitle("");
                TextView cusTitle= toolbar.findViewById(R.id.custitle);
                cusTitle.setText(getTitle());
            }

            setSupportActionBar(toolbar);

            if (showHomeMenuItem()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        afterViews();
        if(isAcceptAppManager())AppManager.getInstance().addActivity(this);
    }

    protected boolean isCenterTitle(){
        return true;
    }

    public abstract boolean isAcceptAppManager();

    @Override
    protected void attachBaseContext(Context newBase) {
        Context newContext = LanguageUtil.initAppLanguage(newBase);
        super.attachBaseContext(newContext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu() != 0) {
            getMenuInflater().inflate(menu(), menu);
        }
        afterMenus(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideInputMethod();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected abstract @LayoutRes
    int contentLayout();

    protected @MenuRes
    int menu() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void beforeViews() {
    }

    protected void afterViews() {
    }

    protected void afterMenus(Menu menu) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }

    protected boolean showHomeMenuItem() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }

    protected MaterialDialog dialog=null;
    public void showLoading(boolean ishow ){
        if(ishow){
            if(dialog==null)
            dialog= new MaterialDialog.Builder(this).theme(Theme.LIGHT)
                    .content(R.string.str_loading)
                    .progress(true, 2000)
                    .build();

            if(!dialog.isShowing())dialog.show();

//            }
        } else{
            if(dialog!=null){
                dialog.dismiss();
                dialog=null;
            }

        }

    }

    public void showLoading(boolean ishow,String message){
//        Logger.e("-----showLoading---->"+ishow);
        if(ishow){
            dialog= new MaterialDialog.Builder(this).theme(Theme.LIGHT)
                    .content(message)
                    .progress(true, 2000)
                    .build();
            dialog.show();
//            }
        } else{
            if(dialog!=null){
                dialog.dismiss();
                dialog=null;
            }

        }

    }

    public void showError(ErrorEnvelope errorEnvelope){
        Toast.makeText(this,errorEnvelope.message,Toast.LENGTH_SHORT).show();
    }

}
