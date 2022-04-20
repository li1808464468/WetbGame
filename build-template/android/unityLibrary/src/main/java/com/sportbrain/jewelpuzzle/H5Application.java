package com.sportbrain.jewelpuzzle;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.webkit.WebView;

import com.blowfire.app.framework.BFApplication;
import com.blowfire.app.framework.BFGdprConsent;
import com.blowfire.common.utils.BFLog;
import com.blowfire.common.utils.BFPreferenceHelper;
import com.google.android.exoplayer2.util.Log;

import androidx.multidex.MultiDex;

import net.appcloudbox.autopilot.AutopilotInitOption;
import net.appcloudbox.autopilot.AutopilotSDK;
import net.appcloudbox.autopilot.AutopilotSessionController;

import java.util.ArrayList;
import java.util.List;

import h5adapter.NativeAPI;

import static h5adapter.NativeAPI.PREFER_FILE_NAME;
import static h5adapter.NativeAPI.PREFER_KEY_ADID;

public class H5Application extends BFApplication {

    public static final String TAG = H5Application.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAutopilot();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String name = getPackageName();
            if (TextUtils.isEmpty(name)) {
                name = "pre_game";
            }
            WebView.setDataDirectorySuffix(name + Process.myPid());
        }

        if (BFGdprConsent.getConsentState() == BFGdprConsent.ConsentState.ACCEPTED) {
            onGdprAccepted();
        } else {
            BFGdprConsent.addListener(new BFGdprConsent.GDPRConsentListener() {
                @Override
                public void onGDPRStateChanged(BFGdprConsent.ConsentState oldState, BFGdprConsent.ConsentState newState) {
                    if (newState == BFGdprConsent.ConsentState.ACCEPTED) {
                        onGdprAccepted();
                    }
                }
            });
        }

        //        onMigration();
//        String a = AutopilotSDK.getInstance().getTopic("topic-8a21ixizu").getString("configs", "default");
//        NativeAPI.sharedInstance().getESIDandSwitch("topic-8a21ixizu");
//        Log.i("12333", a);
    }

    private void initAutopilot() {
        List<String> activityNameBlackList = new ArrayList<>();  // Session 外 Activity 列表 非必要添加
        AutopilotInitOption option = new AutopilotInitOption.Builder(this, activityNameBlackList)
                .build();
        AutopilotSDK.getInstance().initialize(option);
        setCustomerUserID();
        upgradeUserApply(true);
    }

    private void setCustomerUserID() {
        BFPreferenceHelper helper = BFPreferenceHelper.create(BFApplication.getContext(), PREFER_FILE_NAME);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String adId = helper.getString(PREFER_KEY_ADID, "");
                if (!TextUtils.isEmpty(adId)) {
                    AutopilotSDK.getInstance().getUserProperty().edit().setCustomerUserId(adId).apply();
                } else {
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.post(runnable);
    }

    private void upgradeUserApply(boolean isUpgradeUser) {
        if (isUpgradeUser) { // 是该 App 的升级用户
            AutopilotSDK.getInstance().getUserProperty().edit().setIsUpgradeUser().apply();
        }
    }

    private void onGdprAccepted() {
        //在得到 GDPR 状态后设置给 Autopilot SDK
        AutopilotSDK.getInstance().getUserProperty().edit().setIsGdprConsentGranted(true).apply();
        NativeAPI.sharedInstance().onGDPRGranted();
    }


    @Override
    protected String getConfigFileName() {
        return BuildConfig.DEBUG ? "config-d.ya" : "config-r.ya";
    }

    //    private void onMigration() {
    //        BFLaunchInfo lastLaunch = BFApplication.getLastLaunchInfo();
    //        int newVersion = BFApplication.getCurrentLaunchInfo().appVersionCode;
    //        int oldVersion = lastLaunch == null ? newVersion : lastLaunch.appVersionCode;
    //
    //        BFLog.i(TAG, "lastLaunch is null ? " + (lastLaunch == null));
    //
    //        int targetCode = ((oldVersion / 3000) == 1) ? 3021 : 2021;
    //
    //        BFLog.i(TAG, "Old version: " + oldVersion + ", new version: " + newVersion + ", target version = " + targetCode);
    //        if (oldVersion < targetCode) {
    //            BFLog.i(TAG, "onUpgrade, try data migration");
    //            NativeAPI.sharedInstance().enableUpdagrade();
    //        }
    //    }
}
