package com.sportbrain.jewelpuzzle;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.blowfire.app.framework.BFGdprConsent;
import com.blowfire.app.framework.activity.BFActivity;
import com.blowfire.common.utils.BFLog;
import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;

import androidx.work.WorkManager;
import h5adapter.CCNativeAPIProxy;
import h5adapter.GDPRActivity;
import h5adapter.LocalPush;
import h5adapter.NativeAPI;
import h5adapter.utils.Badges;
import h5adapter.utils.Threads;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class UnityPlayerActivity extends BFActivity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private static final String TAG = "UnityPlayerActivity-------";

    private InstallReferrerClient referrerClient;
    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | SYSTEM_UI_FLAG_FULLSCREEN
                    | SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();

        ((ViewGroup) findViewById(android.R.id.content)).addView(createSplashView(),
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        NativeAPI.sharedInstance().init(this);

        NativeAPI.sharedInstance().recordAppOpenTime();
        this.initInstallReferrer();

        logPushClick(getIntent());


        BFLog.d(TAG, "onCreate");

    }

    private void initInstallReferrer()
    {
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {
                            // Connection established.
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                        }
                        catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }

                referrerClient.endConnection();

            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }


    private void logPushClick(Intent intent) {
        String duration = LocalPush.getPushType(intent);
        if (!TextUtils.isEmpty(duration)) {
            Map<String, Object> event = new HashMap<>();
            event.put("type", duration);
            CCNativeAPIProxy.logEvent("Local_Push_Clicked", true, true, event);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        BFLog.d(TAG, "onNewIntent");
        setIntent(intent);
        logPushClick(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();
        BFLog.d(TAG, "onDestroy");
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
        BFLog.d(TAG, "onPause");
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
        BFLog.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        NativeAPI.sharedInstance().onMainSceneOnRestart();
        BFLog.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUnityPlayer.resume();
        

        Badges.clearBadge(this);

        WorkManager.getInstance(H5Application.getContext()).cancelAllWorkByTag("LocalPush");

        BFLog.d(TAG, "onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        mUnityPlayer.pause();
        BFLog.d(TAG, "onStop");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
        BFLog.d(TAG, "onConfigurationChanged " + newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        BFLog.d(TAG, "onWindowFocusChanged1 " + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
        BFLog.d(TAG, "onWindowFocusChanged2 " + hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    private ImageView createSplashImage() {
        ImageView splashImageView = new ImageView(this);
        splashImageView.setImageResource(R.drawable.logo1);
        splashImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        splashImageView.setLayoutParams(param);
        return splashImageView;
    }
    private RelativeLayout splashView;

    private RelativeLayout createSplashView() {
        splashView = new RelativeLayout(this);
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashView.setLayoutParams(param);
        splashView.setBackgroundColor(Color.BLACK);
        splashView.addView(createSplashImage());
        return splashView;
    }

    public void dismissSplashView() {
        if (splashView == null) {
            return;
        }

        Threads.postOnMainThreadDelayed(new Runnable() {
            @Override
            public void run() {

                ((ViewGroup) splashView.getParent()).removeView(splashView);
                splashView.removeAllViews();
                splashView = null;

                if (BFGdprConsent.getConsentState() == BFGdprConsent.ConsentState.TO_BE_CONFIRMED
                        || BFGdprConsent.getConsentState() == BFGdprConsent.ConsentState.UNKNOWN) {
                    UnityPlayerActivity.this.startActivity(new Intent(UnityPlayerActivity.this, GDPRActivity.class));
                    overridePendingTransition(R.anim.anim_popup, 0);
                } else {
                    NativeAPI.sharedInstance().onGDPRGranted();
                }

                NativeAPI.sharedInstance().onSplashFinish();

            }
        }, 30);
    }

}
