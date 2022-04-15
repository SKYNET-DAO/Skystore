package bitchat.android.com.bitstore.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import bitchat.android.com.bitstore.MainActivity;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.wallet.BitUtil;
import bitchat.android.com.bitstore.wallet.JsonUtil;
import bitchat.android.com.bitstore.wallet.data.WalletLiveData;
import bitchat.android.com.bitstore.wallet.manager.ListenerManager;
import bitchat.android.com.bitstore.wallet.services.WalletService;
import bitchat.android.com.bitstore.wallet.viewmodel.WalletViewModel;
import com.android.base.cache.ACacheUtil;
import com.android.base.net.AppConst;
import com.android.base.utils.UIUtils;
import com.android.wallet.constants.Constants;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.base.net.AppConst.TESTADDRESS;

public class SplashActivity extends AppCompatActivity {

    private static String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_CODE_DRAW_OVERLAY = 101;

    private SharedPreferences sharedPreferences;
    private String id;
    private String token;
    private WalletViewModel walletViewModel;

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel= ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.getWalletLoadLiveData().observe(this,wallet -> {
            String root= BitUtil.INSTANCE.getMasterAddress1(wallet, Constants.NETWORK_PARAMETERS);
            Logger.e("-----root----->"+root);
            new Handler().postDelayed(() -> showMain(),5000);
        });

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_splash);
        hideStatusBar();

        if (checkPermission()) {
            if (checkOverlayPermission()) {
                new Handler().postDelayed(this::showNextScreen, 1000);
            }
        } else {
            requestPermissions(permissions, 100);
        }
    }

    private boolean checkPermission() {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                granted = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    break;
                }
            }
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.str_need_float_prom, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        if (checkOverlayPermission()) {
            showNextScreen();
        }
    }

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, R.string.str_need_float_prom, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (infos == null || infos.isEmpty()) {
                    return true;
                }
                startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DRAW_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, UIUtils.getString(R.string.str_auth_fail), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    showNextScreen();
                }
            }
        }
    }

    private void showNextScreen() {
        walletViewModel.getWalletLoadLiveData().loadWallet();
    }

    private void showMain() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
