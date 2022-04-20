package h5adapter;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blowfire.app.framework.BFApplication;
import com.blowfire.app.framework.BFGdprConsent;
import com.blowfire.app.framework.BFSessionMgr;
import com.blowfire.common.config.BFConfig;
import com.blowfire.common.connection.BFHttpConnection;
import com.blowfire.common.connection.BFServerAPIConnection;
import com.blowfire.common.connection.httplib.HttpRequest;
import com.blowfire.common.countrycode.BFCountryCodeManager;
import com.blowfire.common.utils.BFAdUtils;
import com.blowfire.common.utils.BFError;
import com.blowfire.common.utils.BFLog;
import com.blowfire.common.utils.BFMapUtils;
import com.blowfire.common.utils.BFPreferenceHelper;
import com.blowfire.common.utils.BFVersionControlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sportbrain.jewelpuzzle.BuildConfig;
import com.sportbrain.jewelpuzzle.H5Application;
import com.sportbrain.jewelpuzzle.UnityPlayerActivity;
import com.sportbrain.jewelpuzzle.autopilot.Configs;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import net.appcloudbox.autopilot.AutopilotSDK;
import net.appcloudbox.autopilot.AutopilotTopic;

import h5adapter.utils.Dates;
import h5adapter.utils.Threads;
import h5adapter.utils.Utils;

public class NativeAPI {

    private static final String TAG = NativeAPI.class.getSimpleName();
    public static final String PREFER_FILE_NAME = "h5_game_prefer";
    public static final String PREFER_KEY_INSTALL_TYPE = "key_istall_type";
    public static final String PREFER_KEY_MEDIA_SOURCE = "key_media_source";
    public static final String PREFER_KEY_CAMPAIGN = "key_campaign";
    public static final String PREFER_KEY_CAMPAIGN_ID = "key_campaign_id";
    public static final String PREFER_KEY_AGENCY = "key_agency";
    public static final String PREFER_KEY_ADSET = "key_adset";
    public static final String PREFER_KEY_ADSETID = "key_adsetID";
    public static final String PREFER_KEY_SITE_ID = "key_siteID";
    public static final String PREFER_KEY_GAME_DAY = "key_game_day";
    public static final String PREFER_KEY_LAST_GAME = "key_last_game";
    public static final String PREFER_KEY_ADID = "custom_adid";
    private static final String PREFER_KEY_ES_INDEX = "h5_es_index";
    private static final String PREFER_KEY_ES_LOCAL_STORAGE = "h5_es_storage";
    private static final String PREFER_KEY_ES_LOG_ENABLE = "h5_es_log_enable";
    private static final String PREFER_KEY_NEED_UPGRADE = "need_upgrade";
    private static final String PREFER_KEY_BASE_DATA = "custom_base_app_data";

    private static final int MAX_LOCAL_STORAGE_REQUEST = 4;
    private static final int MAX_LOCAL_STORAGE_ITEM_NUMBER = 10;

    private static volatile NativeAPI _instance;

    private UnityPlayerActivity mainActivity;
    private String adId = "";
    private String ip = "";
    private boolean isJSReady = false;

    private BFPreferenceHelper h5Game;
    private boolean enableES;
    private boolean bgmMuted = false;
    private boolean isIPQueryFinish = true;

    private final List<JSONObject> adDataArray = new ArrayList<JSONObject>(20);

    private long appOpenTime = System.currentTimeMillis();


    public static NativeAPI sharedInstance() {
        if (_instance == null) {
            synchronized (NativeAPI.class) {
                if (_instance == null) {
                    _instance = new NativeAPI();

                }
            }
        }
        return _instance;
    }

    private NativeAPI() {
    }

    public void init(UnityPlayerActivity mainActivity) {
        this.mainActivity = mainActivity;

        h5Game = BFPreferenceHelper.create(BFApplication.getContext(), PREFER_FILE_NAME);
        if (h5Game.contains(PREFER_KEY_ES_LOG_ENABLE)) {
            enableES = h5Game.getBoolean(PREFER_KEY_ES_LOG_ENABLE, true);
        } else {
            float value = new Random().nextFloat();
            BFLog.d(TAG, "current es rate generation value is " + value);
            enableES = value <= BFConfig.optFloat(1.0f, "Application", "ESSamplingRate");
        }

        adId = h5Game.getString(PREFER_KEY_ADID, "");
        queryIP();
        queryUserID();
    }

    Context getContext() {
        return mainActivity;
    }

    public void logSnapshot(final JSONObject json) {
        if (!enableES) {
            BFLog.d(TAG, "es not enable by ESSamplingRate");
            BFLog.d(TAG, "es debug log is " + json);
            return;
        }

        if (json == null) {
            return;
        }

        try {
            long current = System.currentTimeMillis();
            JSONObject device = new JSONObject();
            device.put("os_version", Build.VERSION.SDK_INT);
            Locale locale = getLocale();
            String code = BFCountryCodeManager.getInstance().getSimCountryCode();
            if (TextUtils.isEmpty(code)) {
                code = locale.getCountry();
            }
            device.put("country", code);
            device.put("language", locale.getLanguage());
            device.put("wifi", Utils.isWifiEnabled());
            device.put("time-zone", (TimeZone.getDefault().getRawOffset() > 0 ? "+" : "") +
                    TimeZone.getDefault().getRawOffset() / Dates.TIME_HOUR);
            device.put("timestamp", current);
            json.put("device", device);

            JSONObject app = new JSONObject();
            app.put("name", BFConfig.optString("not_configured", "Application", "App", "AppName"));
            app.put("code", BFVersionControlUtils.getAppVersionCode());
            app.put("version", BFVersionControlUtils.getAppVersionName());
            long firstSession = BFSessionMgr.getFirstSessionStartTime();
            app.put("time", firstSession);
            app.put("first_version", BFApplication.getFirstLaunchInfo().appVersionName);
            app.put("first_code", BFApplication.getFirstLaunchInfo().appVersionCode);
            app.put("network", Utils.getNetworkStatus(getContext()));
            app.put("type", "android");
            json.put("app", app);

            JSONObject user = new JSONObject();
            user.put("install_type", h5Game.getString(PREFER_KEY_INSTALL_TYPE, ""));
            user.put("media_source", h5Game.getString(PREFER_KEY_MEDIA_SOURCE, ""));
            user.put("campaign", h5Game.getString(PREFER_KEY_CAMPAIGN, ""));
            user.put("campaign_id", h5Game.getString(PREFER_KEY_CAMPAIGN_ID, ""));
            user.put("agency", h5Game.getString(PREFER_KEY_AGENCY, ""));
            user.put("adset", h5Game.getString(PREFER_KEY_ADSET, ""));
            user.put("adsetId", h5Game.getString(PREFER_KEY_ADSETID, ""));
            user.put("segment", BFConfig.optString("default", "SegmentName"));
            user.put("config_version", BFConfig.optString("default", "Application", "App", "ConfigVersion"));
            user.put("user_id", BFApplication.getInstallationUUID());
            user.put("ad_id", adId);
            user.put("site_id", h5Game.getString(PREFER_KEY_SITE_ID, ""));
            user.put("debug", BuildConfig.DEBUG);

            long day = h5Game.getLong(PREFER_KEY_GAME_DAY, 0);
            if (!Dates.isSameDay(current, h5Game.getLong(PREFER_KEY_LAST_GAME, firstSession))) {
                h5Game.putLong(PREFER_KEY_GAME_DAY, ++day);
                h5Game.putLong(PREFER_KEY_LAST_GAME, current);
            }
            user.put("day", day);
            json.put("user", user);

            long index = h5Game.getLong(PREFER_KEY_ES_INDEX, 0);
            h5Game.putLong(PREFER_KEY_ES_INDEX, ++index);
            json.put("es_index", index);

            BFLog.d(TAG, json.toString());

            final JSONArray jsonArray = new JSONArray();
            jsonArray.put(json);
            loadESData(jsonArray);

            submitData2ES(jsonArray, new BFHttpConnection.OnConnectionFinishedListener() {
                @Override
                public void onConnectionFinished(BFHttpConnection BFHttpConnection) {
                    try {
                        if (BFHttpConnection.isSucceeded()
                                && BFHttpConnection.getBodyJSON() != null
                                && BFHttpConnection.getBodyJSON().has("meta")
                                && BFHttpConnection.getBodyJSON().getJSONObject("meta").getInt("code") == 200) {
                            BFLog.d(TAG, "log es log success " + BFHttpConnection.getResponseMessage());
                        } else {
                            BFLog.d(TAG, "log es failed in finish " + BFHttpConnection.getResponseMessage());
                            storeESData(jsonArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        storeESData(jsonArray);
                    }
                }

                @Override
                public void onConnectionFailed(BFHttpConnection BFHttpConnection, BFError hsError) {
                    BFLog.d(TAG, "log es connection failed " + hsError.getMessage());
                    storeESData(jsonArray);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeESData(JSONArray jsonArray) {
        String origin = h5Game.getString(PREFER_KEY_ES_LOCAL_STORAGE, "");

        StringBuilder content = new StringBuilder(origin);
        if (!TextUtils.isEmpty(origin)) {
            content.append("$$");
        }

        int index = 0;
        for (index = 0; index < jsonArray.length(); index++) {
            try {
                content.append(jsonArray.get(index));
                if (index < jsonArray.length() - 1) {
                    content.append("$$");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                BFLog.d(TAG, "store es data at index:" + index + ", json array is " + jsonArray);
            }
        }

        h5Game.putString(PREFER_KEY_ES_LOCAL_STORAGE, content.toString());
    }

    private void loadESData(JSONArray jsonArray) {
        String storage = h5Game.getString(PREFER_KEY_ES_LOCAL_STORAGE, "");
        if (!TextUtils.isEmpty(storage)) {
            String[] local = storage.split("\\$\\$");
            if (local.length <= MAX_LOCAL_STORAGE_REQUEST) {
                for (int i = 0; i < local.length; i++) {
                    try {
                        jsonArray.put(new JSONObject(local[i]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        BFLog.d(TAG, "loadESData at " + local[i]);
                    }
                }
                h5Game.putString(PREFER_KEY_ES_LOCAL_STORAGE, "");
            } else {
                int start = 0;
                int end = storage.length();

                for (int i = 0; i < MAX_LOCAL_STORAGE_REQUEST; i++) {
                    start += local[i].length() + 2;
                    try {
                        jsonArray.put(new JSONObject(local[i]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        BFLog.d(TAG, "loadESData at " + local[i]);
                    }
                }


                for (int i = MAX_LOCAL_STORAGE_ITEM_NUMBER; i < local.length; i++) {
                    end -= (2 + local[i].length());
                }

                h5Game.putString(PREFER_KEY_ES_LOCAL_STORAGE, storage.substring(start, end));
            }
        }
    }

    private Locale getLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = BFApplication.getContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = BFApplication.getContext().getResources().getConfiguration().locale;
        }

        return locale;
    }

    public void onFinish() {
        if (!BFConfig.optBoolean(true, "Application", "backEnable")) {
            return;
        }

        if (mainActivity != null) {
            mainActivity.finish();
        }
    }

    public boolean shouldNewPlacement() {
        if (!BFConfig.optBoolean(false, "Application", "Ad", "NewEnable")) {
            BFLog.d(TAG, "new placement disable as config not enable");
            return false;
        }

        if (!Utils.isNewUser()) {
            BFLog.d(TAG, "new placement disable as not new user");
            return false;
        }

        if (!Utils.isInDay(BFConfig.optInteger(7, "Application", "Ad", "NewDuration"))) {
            BFLog.d(TAG, "new placement disable as not in config day");
            return false;
        }

        return true;
    }

    public void reportJSError(String error) {
    }

    public void setBGMMuted(boolean muted) {
        this.bgmMuted = muted;
    }

    public boolean isMuted() {
        return this.bgmMuted;
    }

    public void onGuideComplete() {
        BFLog.d(TAG, "onGuideComplete");
    }

    public void reportJSEvent(String error) {
    }

    public void onMainSceneOnRestart() {
        callPlatformJS("OnMainSceneOnRestart", "", "");
    }

    public void onSplashFinish() {
        callPlatformJS("OnSplashFinish", "", "");
    }

    public void onGDPRGranted() {
        callPlatformJS("OnGDPRGranted", "", "");
    }

    public void onAppsFlyerCallback(String conversion) {
        if (TextUtils.isEmpty(conversion)) {
            return;
        }

        try {
            JSONObject userData = new JSONObject(conversion);

            BFPreferenceHelper helper = BFPreferenceHelper.create(BFApplication.getContext(), NativeAPI.PREFER_FILE_NAME);

            if (userData.has("af_status")) {
                helper.putString(NativeAPI.PREFER_KEY_INSTALL_TYPE, userData.getString("af_status"));
            }

            if (userData.has("media_source")) {
                helper.putString(NativeAPI.PREFER_KEY_MEDIA_SOURCE, userData.getString("media_source"));
            }

            if (userData.has("campaign_id")) {
                helper.putString(NativeAPI.PREFER_KEY_CAMPAIGN_ID, userData.getString("campaign_id"));
            }

            if (userData.has("campaign")) {
                helper.putString(NativeAPI.PREFER_KEY_CAMPAIGN, userData.getString("campaign"));
            }

            if (userData.has("agency")) {
                helper.putString(NativeAPI.PREFER_KEY_AGENCY, userData.getString("agency"));
            }

            if (userData.has("af_adset")) {
                helper.putString(NativeAPI.PREFER_KEY_ADSET, userData.getString("af_adset"));
            }

            if (userData.has("adset_id")) {
                helper.putString(NativeAPI.PREFER_KEY_ADSETID, userData.getString("adset_id"));
            }

            if (userData.has("af_siteid")) {
                helper.putString(NativeAPI.PREFER_KEY_SITE_ID, userData.getString("af_siteid"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callPlatformJS(String methodName, String argName, Object argValue) {
        Map<String, Object> event = new HashMap<>();
        event.put(argName, argValue);
        callPlatformJS(methodName, event);
    }

    public String onJSInited() {
        isJSReady = true;
        mainActivity.dismissSplashView();
        Threads.postOnThreadPoolExecutor(new Runnable() {
            @Override
            public void run() {
                NativeAPI.sharedInstance().getAdId();
                addSpecificPushOnCondition();
            }
        });


        return mainActivity.getApplication().getPackageName();
    }

    public String getDeviceId() {
        return BFApplication.getInstallationUUID();
    }

    public String getGDPRStatus() {
        if (BFGdprConsent.getConsentState() == BFGdprConsent.ConsentState.ACCEPTED) {
            return "0";
        } else if (BFGdprConsent.getConsentState() == BFGdprConsent.ConsentState.DECLINED) {
            return "1";
        }
        return "2";
    }

    public String getAdId() {
        queryUserID();
        return adId;
    }

    public String getIp() {
        queryIP();
        return ip;
    }

    public String getNetworkStatus() {
        return Utils.getNetworkStatus(getContext());
    }

    public String getCountryCode() {
        Locale locale = getLocale();
        String code = BFCountryCodeManager.getInstance().getSimCountryCode();
        if (TextUtils.isEmpty(code)) {
            code = locale.getCountry();
        }
        return code;
    }

    public String getCountrySource() {
        String code = BFCountryCodeManager.getInstance().getSimCountryCode();
        if (!TextUtils.isEmpty(code)) {
            return "sim";
        }

        return "setting";
    }

    public String getLocalLanguage() {
        return getLocale().getLanguage();
    }

    public int getVersionCode() {
        return BFVersionControlUtils.getAppVersionCode();
    }

    public String getVersionName() {
        return BFVersionControlUtils.getAppVersionName();
    }

    public int getTimeZone() {
        return TimeZone.getDefault().getRawOffset();
    }

    public String getSdkInt() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getBrand() {
        return Build.MANUFACTURER;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getFingerprint() {
        return Build.FINGERPRINT;
    }

    public void submitBaseData() {
        long last = h5Game.getLong(PREFER_KEY_BASE_DATA, -1);
        long now = System.currentTimeMillis();
        if (Dates.isSameDay(now, last)) {
            return;
        }

        try {
            final JSONObject base = new JSONObject();
            base.put("gdpr_status", getGDPRStatus());
            base.put("gaid", getAdId());
            base.put("network_type", Utils.getNetworkStatus(getContext()));
            base.put("app_bundle", getContext().getPackageName());
            base.put("app_version_name", getVersionName());
            base.put("app_version_code", getVersionCode());
            base.put("fingerprint", getFingerprint());
            base.put("os_version", getOSVersion());
            base.put("country", getCountryCode());
            base.put("country_source", getCountrySource());
            base.put("platform", "android");
            base.put("device_band", getBrand());
            base.put("device_model", getModel());
            base.put("language", getLocalLanguage());
            base.put("sdk_int", getSdkInt());
            base.put("uuid", getDeviceId());
            base.put("time_zone", getTimeZone());
            base.put("event_time", now);
            base.put("ip", ip);
            base.put("apiy", 1);
            base.put("install_time", BFSessionMgr.getFirstSessionStartTime());

            final JSONArray jsonArray = new JSONArray();
            jsonArray.put(base);

            submitData2ES(jsonArray, null);

            h5Game.putLong(PREFER_KEY_BASE_DATA, now);

        } catch (JSONException e) {
        }
    }

    public void submitData2ES(@NonNull final JSONArray jsonArray, @Nullable BFHttpConnection.OnConnectionFinishedListener listener) {
        try {
            final JSONObject request = new JSONObject();
            request.put("data", jsonArray);

            final BFServerAPIConnection serverAPIConnection = new BFServerAPIConnection(BFConfig.getString("Application", "esUrl"),
                    HttpRequest.Method.POST, request, false);

            serverAPIConnection.setConnectionFinishedListener(listener);

            Threads.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    serverAPIConnection.startAsync();
                }
            });
        } catch (JSONException e) {
        }
    }

    public void submitAdData(String json, String adChance) {
        try {
            final JSONObject base = new JSONObject(json);
            doSubmitAdJson(base, adChance);
        } catch (JSONException e) {

        }
    }

    public void submitBannerReturnData(String json, String adChance) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        try {
            final JSONObject base = new JSONObject(json);
            long now = System.currentTimeMillis();
            long duration = 0;
            if (base.has("event_time")) {
                duration = now - base.getLong("event_time");
                base.put("event_time", now);
            }
            base.put("event_type", 4);
            base.put("event_name", "ad_return");
            if (base.optJSONObject("event_meta") != null) {
                base.getJSONObject("event_meta").put("duration", duration);
            }
            doSubmitAdJson(base, adChance);
        } catch (JSONException e) {
        }
    }

    private void doSubmitAdJson(JSONObject base, String adChance) {
        if (!BFConfig.optBoolean(true, "Application", "GED", "Enable")) {
            return;
        }

        queryIP();

        try {
            base.put("gdpr_status", getGDPRStatus());
            base.put("uuid", getDeviceId());
            base.put("ip", ip);
            base.put("gaid", getAdId());
            base.put("network_type", Utils.getNetworkStatus(getContext()));
            base.put("time_zone", getTimeZone());
            base.put("app_bundle", getContext().getPackageName());
            base.put("app_version_name", getVersionName());
            base.put("app_version_code", getVersionCode());
            base.put("country", getCountryCode());
            base.put("platform", "android");
            base.put("apiy", 1);
            base.put("install_time", BFSessionMgr.getFirstSessionStartTime());
            if (base.optJSONObject("event_meta") != null) {
                base.getJSONObject("event_meta").put("ad_chance_name", adChance);
            }

            adDataArray.add(base);
            if (adDataArray.size() >= BFConfig.optInteger(20, "Application", "GED", "PackageSize")) {
                final JSONArray jsonArray = new JSONArray(adDataArray);
                submitData2ES(jsonArray, null);
                adDataArray.clear();
            }
        } catch (JSONException e) {

        }
    }

    private void mergeMap(Map<String, ?> map) {
        if (map == null) {
            return;
        }

        BFMapUtils.mergeMaps((Map<String, Object>) BFConfig.getConfigMap(), map);
    }

    public String getConfigMap(String path) {
        Map value = BFConfig.getMap(path.split("\\."));
        Gson map = new GsonBuilder().create();
        return map.toJson(value);
    }

    public String getConfigList(String path) {
        List value = BFConfig.getList(path.split("\\."));
        Gson list = new GsonBuilder().create();
        return list.toJson(value);
    }

    public String getConfigInt(String path, String defaultValue) {
        return String.valueOf(BFConfig.optInteger(Integer.parseInt(defaultValue), path.split("\\.")));
    }

    public String getConfigFloat(String path, String defaultValue) {
        return String.valueOf(BFConfig.optFloat(Float.parseFloat(defaultValue), path.split("\\.")));
    }

    public String getConfigString(String path, String defaultValue) {
        return BFConfig.optString(defaultValue, path.split("\\."));
    }

    public String getConfigBoolean(String path, String defaultValue) {
        return BFConfig.optBoolean(Boolean.valueOf(defaultValue), path.split("\\.")) ? "true" : "false";
    }

    private void addSpecificPushOnCondition() {
        if (!BFConfig.optBoolean(false, "Application", "Push", "Enable")) {
            WorkManager.getInstance(H5Application.getContext()).cancelAllWorkByTag("SpecificLocalPush");
            return;
        }

        List<Map<String, String>> push = (List<Map<String, String>>) BFConfig.getList("Application", "Push", "Contents");
        if (push == null || push.isEmpty()) {
            return;
        }

        WorkManager.getInstance(H5Application.getContext()).cancelAllWorkByTag("SpecificLocalPush");
        for (int index = 0; index < push.size(); index++) {
            OneTimeWorkRequest specificTime = new OneTimeWorkRequest.Builder(LocalPush.class)
                    .setInitialDelay(Dates.getNextDayTime(
                            Integer.valueOf(push.get(index).get("Hour")),
                            Integer.valueOf(push.get(index).get("Minute")))
                                    - System.currentTimeMillis(),
                            TimeUnit.MILLISECONDS)
                    .setInputData(new Data.Builder()
                            .putInt("Type", 2001 + index)
                            .putString("Title", push.get(index).get("Title"))
                            .putString("Des", push.get(index).get("Des"))
                            .putString("Duration", push.get(index).get("Hour") + ":" + push.get(index).get("Minute"))
                            .build())
                    .addTag("SpecificLocalPush")
                    .build();

            WorkManager.getInstance(H5Application.getContext()).enqueue(specificTime);
        }
    }

    private void queryUserID() {
        if (TextUtils.isEmpty(adId)) {
            try {
                BFAdUtils.getAdID(BFApplication.getContext(), new BFAdUtils.GetAdIdListener() {
                    public void onGetAdIdSuccess(String var1) {
                        BFLog.d(TAG, "get custom user id successfully, user id = " + var1);
                        adId = var1;
                        h5Game.putString(PREFER_KEY_ADID, adId);
                    }

                    public void onGetAdIdFailed() {
                        BFLog.d(TAG, "get custom user id failed");
                    }
                });
            } catch (Exception ignored) {
                BFLog.d(TAG, "query user id failed by " + ignored);
            }
        }
    }

    public void queryIP() {
        if (!isIPQueryFinish) {
            return;
        }

        if (!TextUtils.isEmpty(ip)) {
            return;
        }

        isIPQueryFinish = false;
        Utils.queryIP(new BFHttpConnection.OnConnectionFinishedListener() {
            @Override
            public void onConnectionFinished(BFHttpConnection bfHttpConnection) {
                BFLog.d(TAG, bfHttpConnection.toString());
                isIPQueryFinish = true;
            }

            @Override
            public void onConnectionFailed(BFHttpConnection bfHttpConnection, BFError bfError) {
                BFLog.d(TAG, bfHttpConnection.toString());
                isIPQueryFinish = true;
            }
        }, new BFHttpConnection.OnDataReceivedListener() {
            @Override
            public void onDataReceived(BFHttpConnection bfHttpConnection, byte[] bytes, long l, long l1) {
                String original = new String(bytes);
                BFLog.d(TAG, bfHttpConnection.toString() + ", data  = " + original);

                try {
                    JSONObject json = new JSONObject(original);
                    ip = json.optString("query");
                } catch (JSONException e) {
                    ip = original;
                }

                Pattern pattern = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
                Matcher matcher = pattern.matcher(ip);
                if (!matcher.matches()) {
                    ip = "";
                }
            }
        });
    }

//    public boolean shouldUpgrade() {
//        return h5Game.getBoolean(PREFER_KEY_NEED_UPGRADE, false);
//    }

//    public void enableUpdagrade() {
//        h5Game.putBoolean(PREFER_KEY_NEED_UPGRADE, true);
//    }

//    public void onUpgradeFinish() {
//        h5Game.putBoolean(PREFER_KEY_NEED_UPGRADE, false);
//    }

    @SuppressLint("MissingPermission")
    public void doVibrator(long time, int amplitude) {
        if (mainActivity == null || mainActivity.isFinishing()) {
            return;
        }

        Vibrator vib = null;

        try {
            vib = (Vibrator) BFApplication.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        } catch (Exception ignored) {
            vib = null;
        }

        if (vib == null) {
            return;
        }

        if (!vib.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(time, amplitude));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vib.vibrate(time, new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
        } else {
            vib.vibrate(time);
        }
    }

    public void vibratorStart(List<Integer> patternList, int repeat) {
        long[] pattern = new long[patternList.size()];
        for (int i = 0; i < patternList.size(); i++) {
            pattern[i] = Long.parseLong(patternList.get(i) + "");
        }

        Vibrator vib = (Vibrator) BFApplication.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, repeat);
    }

    public String getESIDandSwitch(String topicId) {
        String result = "";
        try {
            AutopilotTopic topic = AutopilotSDK.getInstance().getTopic(topicId);
            String configs_s = topic.getString("configs", "{\"ESID\":\"none_shake\",\"switchflag\":\"off\",\"remove\":{\"mode\":[0,25,0,0],\"repeat\":-1},\"combo\":{\"mode\":[0,25,0,0],\"repeat\":-1},\"special_gold\":{\"mode\":[0,1000,0,0],\"repeat\":-1},\"special_blue_select\":{\"mode\":[0,300,0,0],\"repeat\":-1},\"special_blue_split\":{\"mode\":[0,75,0,0],\"repeat\":-1},\"bestscore_ingame\":{\"mode\":[0,500,0,0],\"repeat\":-1},\"bestscore_roundover\":{\"mode\":[0,500,0,0],\"repeat\":-1}}");
            Gson gson = new GsonBuilder().create();
            Configs config = gson.fromJson(configs_s, Configs.class);
            String esid = config.getESID();
            String switchflag = config.getSwitchflag();
            result = esid + "|" + switchflag;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void vibratorStop() {
        Vibrator vib = (Vibrator) BFApplication.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }

    public void callPlatformJS(String methodName, Map event) {
        try {
            String jsonStr = new JSONObject(event).toString();
            jsonStr = jsonStr.replace('"', '\'');

            UnityPlayer.UnitySendMessage("PlatformBridge", methodName, jsonStr);

        } catch (NullPointerException ex) {
            BFLog.e(TAG, "fail to call PlatformAPI:" + methodName + ". error:" + ex);
        }
    }

    public int getBannerHeight() {
        if (mainActivity == null || mainActivity.getResources() == null || mainActivity.getResources().getDisplayMetrics() == null) {
            return 200;
        }
        DisplayMetrics displayMetrics = mainActivity.getResources().getDisplayMetrics();
        float height = displayMetrics.heightPixels / displayMetrics.density;
        height = Math.min(90, Math.round(height * 0.15F));
        float height2 = Math.round((displayMetrics.widthPixels / displayMetrics.density / 320.0F * 50.0F));
        return Math.round(Math.max(Math.min(height, height2), 50) * displayMetrics.density);
    }

    public void recordAppOpenTime() {
        appOpenTime = System.currentTimeMillis();
    }

    public long getAppOpenTime() {
        return System.currentTimeMillis() - appOpenTime;
    }

    public String getNativeData() {
        long current = System.currentTimeMillis();
        long firstSession = BFSessionMgr.getFirstSessionStartTime();
        long day = h5Game.getLong(PREFER_KEY_GAME_DAY, 0);
        if (!Dates.isSameDay(current, h5Game.getLong(PREFER_KEY_LAST_GAME, firstSession))) {
            h5Game.putLong(PREFER_KEY_GAME_DAY, ++day);
            h5Game.putLong(PREFER_KEY_LAST_GAME, current);
        }

        JSONObject data = new JSONObject();
        try {
            //game
            data.put("language", getLocalLanguage());
            data.put("country", getCountryCode());
            data.put("country_source", getCountrySource());
            data.put("os_version", getOSVersion());
            data.put("debug", BuildConfig.DEBUG);
            data.put("segment", BFConfig.optString("default", "SegmentName"));
            data.put("user_id", BFApplication.getInstallationUUID());
            data.put("install_type", h5Game.getString(PREFER_KEY_INSTALL_TYPE, ""));
            data.put("day", day);
            data.put("first_version", BFApplication.getFirstLaunchInfo().appVersionName);
            data.put("time", firstSession);
            data.put("first_code", BFApplication.getFirstLaunchInfo().appVersionCode);
            data.put("code", BFVersionControlUtils.getAppVersionCode());
            data.put("name", BFConfig.optString("not_configured", "Application", "App", "AppName"));
            data.put("config_version", BFConfig.optString("default", "Application", "App", "ConfigVersion"));
            data.put("version", BFVersionControlUtils.getAppVersionName());

            //ad
            data.put("gdpr_status", getGDPRStatus());
            data.put("uuid", getDeviceId());
            data.put("time_zone", getTimeZone());
            data.put("app_bundle", getContext().getPackageName());
            data.put("app_version_name", getVersionName());
            data.put("app_version_code", getVersionCode());
            data.put("install_time", firstSession);
            data.put("fingerprint", getFingerprint());
            data.put("device_band", getBrand());
            data.put("device_model", getModel());
            data.put("sdk_int", getSdkInt());

            //appsflyer
            data.put("media_source", h5Game.getString(PREFER_KEY_MEDIA_SOURCE, ""));
            data.put("campaign_id", h5Game.getString(PREFER_KEY_CAMPAIGN_ID, ""));
            data.put("campaign", h5Game.getString(PREFER_KEY_CAMPAIGN, ""));
            data.put("agency", h5Game.getString(PREFER_KEY_AGENCY, ""));
            data.put("af_adset", h5Game.getString(PREFER_KEY_ADSET, ""));
            data.put("adset_id", h5Game.getString(PREFER_KEY_ADSETID, ""));
            data.put("af_siteid", h5Game.getString(PREFER_KEY_SITE_ID, ""));
            data.put("af_status", h5Game.getString(PREFER_KEY_INSTALL_TYPE, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
