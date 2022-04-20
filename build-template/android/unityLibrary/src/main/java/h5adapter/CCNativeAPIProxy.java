package h5adapter;

import android.content.Context;
import android.widget.Toast;

import com.blowfire.app.framework.BFGdprConsent;
import com.blowfire.common.analytics.BFAnalytics;
import com.blowfire.common.utils.BFLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sportbrain.jewelpuzzle.BuildConfig;
import com.sportbrain.jewelpuzzle.H5Application;
import com.sportbrain.jewelpuzzle.autopilot.Configs;

import net.appcloudbox.autopilot.AutopilotSDK;
import net.appcloudbox.autopilot.AutopilotTopic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import h5adapter.utils.Markets;
import h5adapter.utils.Toasts;
import h5adapter.utils.Utils;

public class CCNativeAPIProxy {

    private static final String TAG = CCNativeAPIProxy.class.getSimpleName();

    private static final String SCREEN_GAME = "H5GameScene";


    public static void loadInterstitialAd(String adPlacement) {
        // TODO: 2019/3/16 remind js remove interstitial load function
    }

    public static void loadRewardVideoAd(String adPlacement) {
        // TODO: 2019/3/16  remind js remove reward load function
    }

    public static String getPackageName() {
        return Utils.getPackageName(H5Application.getContext());
    }

    public static void onAppsFlyerCallback(String conversion) {
        NativeAPI.sharedInstance().onAppsFlyerCallback(conversion);
    }

    public static void reportJSError(String error) {
        NativeAPI.sharedInstance().reportJSError(error);
    }

    public static String getDeviceId() {
        return NativeAPI.sharedInstance().getDeviceId();
    }

    public static String getCustomUserId() {
        return NativeAPI.sharedInstance().getAdId();
    }

    public static void gotoMarket() {
        Markets.browseAPP();
    }

    public static void showToast(String content) {
        Toasts.showToast(content, Toast.LENGTH_LONG);
    }

    public static void declineGpdr() {
        BFGdprConsent.setGranted(false);
    }

    public static void acceptGpdr() {
        BFGdprConsent.setGranted(true);
    }

    public static boolean isGpdrUser() {
        return BFGdprConsent.isGdprUser();
    }

    public static boolean isFirstDay() {
        return Utils.isInDay(1);
    }

    public static String getConfigMap(String path) {
        return NativeAPI.sharedInstance().getConfigMap(path);
    }

    public static String getConfigList(String path) {
        return NativeAPI.sharedInstance().getConfigList(path);
    }

    public static String getConfigInt(String path, String defaultValue) {
        return NativeAPI.sharedInstance().getConfigInt(path, defaultValue);
    }

    public static String getConfigFloat(String path, String defaultValue) {
        return NativeAPI.sharedInstance().getConfigFloat(path, defaultValue);
    }

    public static String getConfigString(String path, String defaultValue) {
        return NativeAPI.sharedInstance().getConfigString(path, defaultValue);
    }

    public static String getConfigBoolean(String path, String defaultValue) {
        return NativeAPI.sharedInstance().getConfigBoolean(path, defaultValue);
    }

    public static void onFinish() {
        NativeAPI.sharedInstance().onFinish();
    }

    public static String onJSInited() {
        return NativeAPI.sharedInstance().onJSInited();
    }

    public static void isBGMMuted(String muted) {
        BFLog.d(TAG, muted);
        NativeAPI.sharedInstance().setBGMMuted(Boolean.parseBoolean(muted));
    }

    public static String getSystemApiLevel() {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    public static void logEventWithJSON(String eventName, String enableES, String fabric, String json) {
        Map<String, Object> event = new HashMap<>();
        if (json != null && json.length() > 0) {
            try {
                JSONObject jsonEvent = new JSONObject(json);
                Iterator<String> keys = jsonEvent.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonEvent.get(key);
                    if (value != null) {
                        event.put(key, value);
                    }
                }
            } catch (JSONException ex) {
                BFLog.e(ex.toString());
            }
        }
        logEvent(eventName, Boolean.valueOf(enableES), false, event);
    }

    public static void logEvent(String eventName, boolean enableES, boolean enableFabric, Map<String, Object> event) {
        if (enableES) {
            JSONObject para = new JSONObject(event);
            JSONObject result = new JSONObject();
            parseJson(para, result);
            JSONObject es = new JSONObject();
            try {
                es.put("h5Action", eventName);
                es.put("h5Para", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NativeAPI.sharedInstance().logSnapshot(es);
        }
    }

    public static void onGuideComplete() {
        NativeAPI.sharedInstance().onGuideComplete();
    }

    public static void parseJson(JSONObject para, JSONObject result) {
        Iterator<String> keys = para.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = null;
            try {
                value = para.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (value == null) {
                continue;
            }

            try {
                JSONObject next = new JSONObject(value.toString());
                JSONObject keyJson = new JSONObject();
                parseJson(next, keyJson);
                result.put(key, next);
            } catch (JSONException e) {
                try {
                    result.put(key, value.toString());
                } catch (JSONException e1) {
                }
            }
        }
    }

    public static void logEvent(String eventName, String... vars) {
        logEvent(eventName, false, true, vars);
    }

    public static void logEvent(String eventName, boolean enableES, boolean enableFabric, String... vars) {
        Map<String, Object> item = new HashMap();
        if (null != vars) {
            int length = vars.length;
            if (length % 2 != 0) {
                --length;
            }
            String key = null;
            String value = null;
            int i = 0;
            while (i < length) {
                key = vars[i++];
                value = vars[i++];
                item.put(key, value);
            }
        }
        logEvent(eventName, enableES, false, item);
    }

    public static void logGAEvent(String category, String action, String label) {
        BFLog.d(TAG, "category = " + category + ", action = " + action + ", label = " + label);
        BFAnalytics.logEvent(category, "action", action, "label", label);
    }

    public static String networkStatus() {
        Context ctx = NativeAPI.sharedInstance().getContext();
        return Utils.getNetworkStatus(ctx);
    }

    public static void doVibrator(String time, String amplitude) {
        NativeAPI.sharedInstance().doVibrator(Long.valueOf(time), Integer.valueOf(amplitude));
    }

    public static String getESIDandSwitchFlag(String topicId) {
        return NativeAPI.sharedInstance().getESIDandSwitch(topicId);
    }

    /**
     * @param patternName "remove"                  消除一行
     *                    "combo"                   combo_N行(同时消除N行) 暂时不用。可以用多次消除一行替代
     *                    "special_gold"            special_gold（黄块消除）
     *                    "special_blue_select"     special_blue_选定方块
     *                    "special_blue_split"      special_blue_分裂方块
     */
    public static void vibratorStart(String patternName, String topicId) {
        AutopilotTopic topic = AutopilotSDK.getInstance().getTopic(topicId);
        String configs_s = topic.getString("configs", "{\"ESID\":\"none_shake\",\"switchflag\":\"off\",\"remove\":{\"mode\":[0,25,0,0],\"repeat\":-1},\"combo\":{\"mode\":[0,25,0,0],\"repeat\":-1},\"special_gold\":{\"mode\":[0,1000,0,0],\"repeat\":-1},\"special_blue_select\":{\"mode\":[0,300,0,0],\"repeat\":-1},\"special_blue_split\":{\"mode\":[0,75,0,0],\"repeat\":-1},\"bestscore_ingame\":{\"mode\":[0,500,0,0],\"repeat\":-1},\"bestscore_roundover\":{\"mode\":[0,500,0,0],\"repeat\":-1}}");
        Gson gson = new GsonBuilder().create();
        Configs config = gson.fromJson(configs_s, Configs.class);
        List<Integer> patternList = new ArrayList<>();
        int repeat = -1;
        switch (patternName) {
            case "remove":
                patternList = config.getRemove().getMode();
                repeat = config.getRemove().getRepeat();
                break;
            case "combo":
                patternList = config.getCombo().getMode();
                repeat = config.getCombo().getRepeat();
                break;
            case "special_gold":
                patternList = config.getSpecial_gold().getMode();
                repeat = config.getSpecial_gold().getRepeat();
                break;
            case "special_blue_select":
                patternList = config.getSpecial_blue_select().getMode();
                repeat = config.getSpecial_blue_select().getRepeat();
                break;
            case "special_blue_split":
                patternList = config.getSpecial_blue_split().getMode();
                repeat = config.getSpecial_blue_split().getRepeat();
                break;
            case "bestscore_ingame":
                patternList = config.getBestscore_ingame().getMode();
                repeat = config.getBestscore_ingame().getRepeat();
                break;
            case "bestscore_roundover":
                patternList = config.getBestscore_roundover().getMode();
                repeat = config.getBestscore_roundover().getRepeat();
                break;
            default:
                break;
        }
        NativeAPI.sharedInstance().vibratorStart(patternList, repeat);
    }

    public static void vibratorStop() {
        NativeAPI.sharedInstance().vibratorStop();
    }

    public static void submitAdData(String json, String adChance) {
        NativeAPI.sharedInstance().submitAdData(json, adChance);
    }

    public static void submitBaseData() {
        NativeAPI.sharedInstance().submitBaseData();
    }

    public static void submitBannerReturnData(String json, String adChance) {
        NativeAPI.sharedInstance().submitBannerReturnData(json, adChance);
    }


    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static int getBannerHeight() {
        return NativeAPI.sharedInstance().getBannerHeight();
    }

    public static long getAppOpenTime() {
        return NativeAPI.sharedInstance().getAppOpenTime();
    }

    public static String getNativeData() {
        return NativeAPI.sharedInstance().getNativeData();
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static String getAdId() {
        return NativeAPI.sharedInstance().getAdId();
    }

    public static String getIp() {
        return NativeAPI.sharedInstance().getIp();
    }

    public static String getNetworkStatus() {
        return NativeAPI.sharedInstance().getNetworkStatus();
    }
}
