using System;
using System.Runtime.InteropServices;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using BFF;
using Mono.Data.Sqlite;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using Manager;
using Models;
using Other;
using Plugins;
using UnityEngine;

namespace Platform
{
    public static class PlatformBridge
    {
        private static readonly string TAG = "PlatformBridge-------";
        // static readonly AndroidJavaClass androidProxy = new AndroidJavaClass("h5adapter.CCNativeAPIProxy");

        static AppLovinMediationAdapter adapter = null;

        public static string notifyInited()
        {

            return "";
// #if UNITY_IOS && !UNITY_EDITOR
//         return mNotifyInited();
//
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("onJSInited");
// #else
//             return "";
// #endif
        }

        public static void InitAppLovinAdapter()
        {

            if (adapter == null)
            {
                adapter = AppLovinMediationAdapter.Instance;


                //#if UNITY_IOS
                //
                //#elif UNITY_ANDROID
                //                        var bfMoPubDataManager = new GameObject();
                //                        bfMoPubDataManager.AddComponent<BFMoPubDataManager>();
                //#endif
            }

        }

        public static void OnMainSceneOnRestart()
        {
            adapter?.OnMainSceneOnRestart();
        }

        public static string getCustomUserID()
        {
            return "";
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetCustomUserID();
//
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("getCustomUserId");
// #else
//             return "";
// #endif
        }

        public static string getDeviceId()
        {
            return "";
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetDeviceId();
//
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("getDeviceId");
// #else
//             return "";
// #endif
        }

        public static void gotoMarket()
        {

            return;
// #if UNITY_IOS  && !UNITY_EDITOR
//         mGotoMarket();
// #elif UNITY_ANDROID  && !UNITY_EDITOR
//         CallAndoridMethod("gotoMarket");
// #endif
        }

        public static void onFinish()
        {
            
// #if UNITY_IOS  && !UNITY_EDITOR
//         mOnFinish();
// #elif UNITY_ANDROID  && !UNITY_EDITOR
//         CallAndoridMethod("onFinish");
// #endif
        }

        public static void showToast(string toast)
        {
// #if UNITY_IOS  && !UNITY_EDITOR
//         mShowToast(toast);
// #elif UNITY_ANDROID  && !UNITY_EDITOR
//         CallAndoridMethod("showToast", toast);
// #endif
        }

        public static void isBGMMuted(bool mute)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         mSetBGMMuted(mute);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("isBGMMuted", mute.ToString());
// #endif
        }

        public static void shareOverFacebook()
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         mShareOverFacebook();
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("shareOverFacebook");
// #endif
        }

        //config

        public static string getConfigList(string keyPath)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetConfigList(keyPath);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("getConfigList", keyPath);
// #else
            return "";
// #endif
        }

        public static string getConfigMap(string keyPath)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetConfigMap(keyPath);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("getConfigMap", keyPath);
// #else
            return "";
// #endif
        }

        public static int getConfigInt(string keyPath, int defaultValue)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         string result = mGetConfigInt(keyPath, defaultValue.ToString());
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         string result = CallAndoridMethod<string>("getConfigInt", keyPath, defaultValue.ToString());
// #else
//             string result = null;
// #endif
//             if (string.IsNullOrEmpty(result))
//             {
//                 return defaultValue;
//             }
//
//             int.TryParse(result, out defaultValue);
            return defaultValue;
        }

        public static float getConfigFloat(string keyPath, float defaultValue)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         string result = mGetConfigFloat(keyPath, defaultValue.ToString());
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         string result = CallAndoridMethod<string>("getConfigFloat", keyPath, defaultValue.ToString());
// #else
//             string result = null;
// #endif
//             if (string.IsNullOrEmpty(result))
//             {
//                 return defaultValue;
//             }
//
//             float.TryParse(result, out defaultValue);
            return defaultValue;
        }

        public static string getConfigString(string keyPath, string defaultValue)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         string result = mGetConfigString(keyPath, defaultValue);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         string result = CallAndoridMethod<string>("getConfigString", keyPath, defaultValue);
// #else
//             string result = null;
// #endif
//
//             if (string.IsNullOrEmpty(result))
//             {
//                 return defaultValue;
//             }

            return defaultValue;

        }


        public static bool getConfigBoolean(string keyPath, bool defaultValue)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         string result = mGetConfigBoolean(keyPath, defaultValue);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         string result = CallAndoridMethod<string>("getConfigBoolean", keyPath, defaultValue.ToString());
// #else
//             string result = null;
// #endif
//             if (string.IsNullOrEmpty(result))
//             {
//                 return defaultValue;
//             }
//
//             bool.TryParse(result, out defaultValue);
            return defaultValue;

        }

        public static string getPackageName(string name = "")
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         string result = mGetPackageName();
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         string result = CallAndoridMethod<string>("getPackageName");
// #else
//             string result = null;
// #endif
//
//             if (string.IsNullOrEmpty(result))
//             {
//                 return name;
//             }

            return name;
        }

        public static long GetEnterGameTime()
        {
//             long enterGameTime = 0;
// #if UNITY_ANDROID && !UNITY_EDITOR
//                 enterGameTime = CallAndoridMethod<long>("getAppOpenTime");
// #elif UNITY_IOS && !UNITY_EDITOR
//                 enterGameTime = long.Parse(mGetAppOpenTime());
// #endif
            return 0;
        }

        public static int GetBannerHeight()
        {
            var result = 150;
// #if UNITY_IOS && !UNITY_EDITOR
//             result = 150;
// #elif UNITY_ANDROID && !UNITY_EDITOR
//             result = CallAndoridMethod<int>("getBannerHeight");
// #endif
//             Helper.Log(TAG + "getBannerHeight" + result);
            return result;
        }

        //广告

        public static void showInterstitialAd(bool restart = false)
        {
            adapter?.ShowInterstitialAd(restart);
        }

        public static void show5sInterstitialAd(bool restart = false)
        {
            adapter?.Show5sInterstitialAd(restart);
        }

        public static void loadInterstitialAd()
        {
            adapter?.LoadInterstitial();
        }

        public static bool FiveSecInterstitialCount()
        {
            if (adapter == null) return false;
            return adapter.FiveSecInterstitialCount() > 0;
        }

        public static bool isInterstitialReady()
        {
            if (adapter == null) return false;
            return adapter.InterstitialAdAvailableCount() > 0;
        }

        public static void showRewardVideoAd()
        {
            adapter?.ShowRewardVideo();
        }

        public static void loadRewardVideoAd()
        {
            adapter?.LoadRewardVideo();
        }

        public static bool isRewardVideoReady()
        {
            if (adapter == null) return false;
            return adapter.RewardVideoAvailableCount() > 0;
        }


        //广告结束
        public static void OnAppsFlyerTrackerCallback(string conversion)
        {
            Helper.Log(TAG + "OnAppsFlyerTrackerCallback " + conversion);

// #if UNITY_IOS && !UNITY_EDITOR
//         mOnAppsFlyerTrackerCallback(conversion);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("onAppsFlyerCallback", conversion);
// #endif
        }

        //记录事件
        public static void logEvent(string eventName, string arg1 = "")
        {
            return;
            // Helper.Log(TAG + "logEvent " + eventName);
            // if (H5Tracker.H5TrackManager.Instance.IsGranted())
            // {
            //     Helper.Log(TAG + "logFlurry " + eventName);
            //     // Flurry.LogEvent(eventName);
            // }

            //#if UNITY_IOS && !UNITY_EDITOR
            //        mLogEventWithJSON(eventName, "false", "true", arg1);
            //#elif UNITY_ANDROID && !UNITY_EDITOR
            //        CallAndoridMethod("logEventWithJSON", eventName, "false", "true", arg1);
            //#endif
        }

        public static void logESEvent(string eventName, string arg1 = "")
        {
            return;
//             Helper.Log(TAG + "logESEvent " + eventName);
//
//             if (Constant.ESLogUseNative)
//             {
// #if UNITY_IOS && !UNITY_EDITOR
//         mLogEventWithJSON(eventName, "true", "false", arg1);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("logEventWithJSON", eventName, "true", "false", arg1);
// #endif
//             }
//             else
//             {
//                 AsyncThread.SubmitData2ES(eventName, arg1 == "" ? null : JsonConvert.DeserializeObject<Hashtable>(arg1));
//             }
        }

        public static void logESEvent(string eventName, bool enableFabric, string arg1 = "")
        {
            return;
//             Helper.Log(TAG + "logESEvent " + eventName);
//
//             if (Constant.ESLogUseNative)
//             {
// #if UNITY_IOS && !UNITY_EDITOR
//         mLogEventWithJSON(eventName, "true", "" + enableFabric, arg1);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("logEventWithJSON", eventName, "true", "" + enableFabric, arg1);
// #endif
//             }
//             else
//             {
//                 AsyncThread.SubmitData2ES(eventName, arg1 == "" ? null : JsonConvert.DeserializeObject<Hashtable>(arg1));
//             }
        }

        public static void logAFEvent(string eventName, string arg1 = "")
        {
            return;
            // Helper.Log(TAG + "logAFEvent " + eventName);
            // Dictionary<string, string> paras = new Dictionary<string, string>();
            // AppsFlyer.sendEvent(eventName, paras);
        }

        //震动 时间：毫秒单位  ， 震动幅度 0 - 255
        public static void doRibrator(int time, int amplitude)
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         mDoRibrator(time, amplitude);
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         CallAndoridMethod("doVibrator", time.ToString(), amplitude.ToString());
// #endif
        }

        /**
     * @param patternName "remove"                  消除一行
     *                    "combo"                   combo_N行(同时消除N行) 暂时不用。可以用多次消除一行替代
     *                    "special_gold"            special_gold（黄块消除）
     *                    "special_blue_select"     special_blue_选定方块
     *                    "special_blue_split"      special_blue_分裂方块
     */
        public enum eVibratorMode
        {
            remove = 0,
            combo = 1,
            special_gold = 2,
            special_blue_select = 3,
            special_blue_split = 4,
            bestscore_ingame = 5,
            bestscore_roundover = 6,
            
        }
        
        /// <summary>
        /// 开始震动
        /// 
        /// </summary>
        /// <param name="patternString"></param>
        /// <param name="repeat"></param>
        public static void vibratorStart(eVibratorMode mode)
        {
//             if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on")
//             {
//                 
//                 string patternString = mode.ToString();
//                 string topicID = Constant.TopicID;
// #if UNITY_ANDROID && !UNITY_EDITOR
//                 CallAndoridMethod("vibratorStart", patternString, topicID);
// #endif
//             }
        }

        /// <summary>
        /// 停止震动
        /// </summary>
        public static void vibratorStop()
        {
//             if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on")
//             {
// #if UNITY_ANDROID && !UNITY_EDITOR
//             CallAndoridMethod("vibratorStop");
// #endif
//             }
        }
        /// <summary>
        /// 获取EsID和SwitchFlag
        /// </summary>
        /// <returns>"EsID|SwitchFlag"</returns>
        public static string getESIDandSwitchFlag()
        {
//             string topicID = Constant.TopicID;
// #if UNITY_ANDROID && !UNITY_EDITOR
//             return CallAndoridMethod<string>("getESIDandSwitchFlag", topicID);
// #endif
            return "|off";
        }

        public static string getSystemApiLevel()
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetSystemApiLevel();
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("getSystemApiLevel");
// #else
            return "";
// #endif
        }

        public static string networkStatus()
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         return mGetNetworkStatus();
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<string>("networkStatus");
// #else
            return "";
// #endif
        }

        public static bool isFirstDay()
        {
// #if UNITY_IOS && !UNITY_EDITOR
//         return mIsFirstDay();
// #elif UNITY_ANDROID && !UNITY_EDITOR
//         return CallAndoridMethod<bool>("isFirstDay");
// #else
            return true;
// #endif
        }
        
        //4configATT开关关闭
        //3用户允许获取
        //2用户未选择
        //1受限
        //0用户拒绝获取
        //只有状态值是2的时候需要弹ATT系统弹窗
        public static int trackingTransparencyStatus()
        {

// #if UNITY_IOS && !UNITY_EDITOR
//                 return mTrackingTransparencyStatus();
// #else
                return 0;
// #endif
   
        }

        
        public static void showTrackingTransparency()
        {
// #if UNITY_IOS && !UNITY_EDITOR
//                  mShowTrackingTransparency();
// #else
//
// #endif
        }


        //        public static bool shouldUpgrade()
        //        {
        //#if UNITY_IOS && !UNITY_EDITOR
        //        return mShouldUpgrade();
        //#elif UNITY_ANDROID && !UNITY_EDITOR
        //        return CallAndoridMethod<bool>("shouldUpgrade");
        //#else
        //            return false;
        //#endif
        //        }

        //        public static void onUpgradeFinish()
        //        {
        //#if UNITY_IOS && !UNITY_EDITOR
        //        mOnUpgradeFinish();
        //#elif UNITY_ANDROID && !UNITY_EDITOR
        //        CallAndoridMethod("onUpgradeFinish");
        //#endif
        //        }

        //******* FB 相关  start *******//

        // 获取用户是否绑定GooglePlay
        public static bool isGooglePlayBind()
        {
            return false;
            // return CallAndoridMethod<bool>("isGooglePlayBind");
        }

        // 调用GooglePlay绑定接口
        public static void loginGooglePlay()
        {
            CallAndoridMethod("loginGooglePlay");
        }

        // 上报排行榜数据
        public static void updateLeaderBoardData(int score)
        {
            CallAndoridMethod("updateLeaderBoardData", score.ToString());
        }

        // 展示排行榜数据
        public static void showLeaderboard()
        {
            CallAndoridMethod("showLeaderboard");
        }

        public static void submitAdData(string json, string adChance)
        {
//             if (Constant.ESLogUseNative)
//             {
// #if UNITY_ANDROID && !UNITY_EDITOR
//             CallAndoridMethod("submitAdData", json, adChance);
// #endif
//             }
//             else
//             {
//                 AsyncThread.SubmitAdData2ES(json, adChance);
//             }
        }

        public static void submitBannerReturnData(string json, string adChance)
        {
//             if (Constant.ESLogUseNative)
//             {
// #if UNITY_ANDROID && !UNITY_EDITOR
//             CallAndoridMethod("submitBannerReturnData", json, adChance);
// #endif
//             }
//             else
//             {
//                 AsyncThread.SubmitAdData2ES(json, adChance, true);
//             }
        }

        public static void submitBaseData()
        {
//             if (Constant.ESLogUseNative)
//             {
// #if UNITY_ANDROID && !UNITY_EDITOR
//             CallAndoridMethod("submitBaseData");
// #endif
//             }
//             else
//             {
//                 AsyncThread.SubmitBaseData2ES();
//             }
        }

        //打开隐私政策网页
        public static void ShowPrivacy()
        {
            // var privacyUrl = getConfigString("Application.App.Privacy", "");
            // if (privacyUrl != "")
            // {
            //     Application.OpenURL(privacyUrl);
            // }
            // else
            // {
            //     Helper.Log("未在config中配置Privacy字段");
            // }
        }

        //调用原生安卓函数
        private static void CallAndoridMethod(string MethodName)
        {
            // androidProxy.CallStatic(MethodName);

        }

        private static void CallAndoridMethod(string MethodName, string args)
        {
            // androidProxy.CallStatic(MethodName, args);

        }

        private static void CallAndoridMethod(string MethodName, string args, string arg1)
        {


            // androidProxy.CallStatic(MethodName, args, arg1);

        }

        private static void CallAndoridMethod(string MethodName, string args, string args1, string args2)
        {

            // androidProxy.CallStatic(MethodName, args, args1, args2);

        }

        private static void CallAndoridMethod(string MethodName, string args, string args1, string args2, string args3)
        {

            // androidProxy.CallStatic(MethodName, args, args1, args2, args3);

        }

        // private static T CallAndoridMethod<T>(string MethodName)
        // {
        //
        //     return androidProxy.CallStatic<T>(MethodName);
        //
        // }


        // private static T CallAndoridMethod<T>(string MethodName, string args)
        // {
        //
        //     // return androidProxy.CallStatic<T>(MethodName, args);
        //
        // }

        // private static T CallAndoridMethod<T>(string MethodName, string args, string args1)
        // {
        //
        //     return androidProxy.CallStatic<T>(MethodName, args, args1);
        //
        // }
        //
        // private static T CallAndoridMethod<T>(string MethodName, string args, string args1, string args2)
        // {
        //
        //     return androidProxy.CallStatic<T>(MethodName, args, args1, args2);
        //
        // }


        //    public class chessDataCCC
        //    {
        //        /// <summary>
        //        /// 继承cocos棋盘数据的
        //        /// </summary>
        //        public string[,] blockArray { get; set; }
        //    }

        //    public static void deleteSqlite()
        //    {
        //
        //
        //#if UNITY_EDITOR
        //
        //        if (!File.Exists(Application.dataPath + "/jsb.sqlite")) return;
        //        File.Delete(Application.dataPath + "/jsb.sqlite");
        //
        //#else
        //        string sqlitePath = "/data/user/0/com.timetoriseup.blockpuzzle/databases/jsb.sqlite";
        //
        //        if (!File.Exists(sqlitePath))
        //        {
        //            return;
        //        }
        //
        //        File.Delete(sqlitePath);
        //#endif
        //
        //    }

        //    public static void dataLevelUp()
        //    {
        //
        //        /// data / user / 0 / com.spear.blockpuzzle / databases/jsb.sqlite   data数据库 
        //
        //#if UNITY_EDITOR
        //        if (!File.Exists(Application.dataPath + "/jsb.sqlite")) return;
        //        string conn = "URI=" + Application.dataPath + "/jsb.sqlite";
        //#else
        //        if (!File.Exists("/data/user/0/com.timetoriseup.blockpuzzle/databases/jsb.sqlite")) {
        //
        //            Debug.Log("### sqlite 不存在");
        //            return;
        //        }
        //        string conn = "URI=file:/data/user/0/com.timetoriseup.blockpuzzle/databases/jsb.sqlite";
        //#endif
        //        SqliteConnection dbconn = new SqliteConnection(conn);
        //        dbconn.Open();
        //
        //        SqliteCommand command = dbconn.CreateCommand();
        //        command.CommandText = "SELECT key,value " + "FROM data where key = \"Guidance_Key\"";
        //        SqliteDataReader reader = command.ExecuteReader();
        //
        //        if (reader.Read())
        //        {
        //
        //            string name1 = reader.GetString(0);
        //            string value1 = reader.GetString(1);
        //            Debug.Log(" ##### name = " + name1 + " value = " + value1);
        //
        //            if (value1 == "0")
        //            {
        //
        ////                RecordData.lData.gameState = 0;
        //
        //                SqliteCommand command1 = dbconn.CreateCommand();
        //                command1.CommandText = "SELECT key,value " + "FROM data where key = \"Guidance_Level_Key\"";
        //                SqliteDataReader reader1 = command1.ExecuteReader();
        //                if (reader1.Read())
        //                {
        //                    Debug.Log("value = " + reader1.GetString(1));
        //                    int.TryParse(reader1.GetString(1), out int guideLevel);
        ////                    RecordData.lData.guideLevel = guideLevel;
        //
        //                }
        //
        //                dbconn.Close();
        //                return;
        //
        //            }
        //            else
        //            {
        //                reader.Close();
        //
        //                //游戏进行中
        //                SqliteCommand command3 = dbconn.CreateCommand();
        //                command3.CommandText = "SELECT key,value " + "FROM data where key = \"Game_Data_Key\"";
        //                SqliteDataReader reader3 = command3.ExecuteReader();
        //
        //                if (reader3.Read())
        //                {
        //                    Debug.Log(" ##### gameState 1 ");
        ////                    RecordData.lData.gameState = 1;
        //                    string allData = reader3.GetString(1);
        //                    Debug.Log("AllData: " + allData);
        //                    if (string.IsNullOrEmpty(allData))
        //                    {
        //                        dbconn.Close();
        //                        return;
        //                    }
        //                    JObject json = JObject.Parse(allData);
        //
        //                    //继承累计数据
        //                    
        //
        //                }
        //                else
        //                {
        //                    dbconn.Close();
        //                    return;
        //                }
        //
        //            }
        //
        //        }
        //        else
        //        {
        //
        //            dbconn.Close();
        //            return;
        //
        //        }
        //
        //    }


#if UNITY_IOS
    [DllImport("__Internal")]
    private static extern string mNotifyInited();

    [DllImport("__Internal")]
    private static extern string mGetCustomUserID();

    [DllImport("__Internal")]
    private static extern string mGetDeviceId();

    [DllImport("__Internal")]
    private static extern void mGotoMarket();

    [DllImport("__Internal")]
    private static extern void mOnFinish();

    [DllImport("__Internal")]
    private static extern void mShowToast(string toast);

    [DllImport("__Internal")]
    private static extern void mSetBGMMuted(bool mute);

    [DllImport("__Internal")]
    private static extern void mShareOverFacebook();

    [DllImport("__Internal")]
    private static extern string mGetConfigList(string keyPath);

    [DllImport("__Internal")]
    private static extern string mGetConfigMap(string keyPath);

    [DllImport("__Internal")]
    private static extern string mGetConfigInt(string keyPath, string defaultValue);

    [DllImport("__Internal")]
    private static extern string mGetConfigFloat(string keyPath, string defaultValue);

    [DllImport("__Internal")]
    private static extern string mGetConfigString(string keyPath, string defaultValue);

    [DllImport("__Internal")]
    private static extern string mGetConfigBoolean(string keyPath, bool defaultValue);

    [DllImport("__Internal")]
    private static extern string mGetPackageName();

    [DllImport("__Internal")]
    private static extern void mOnAppsFlyerTrackerCallback(string conversion);

//    [DllImport("__Internal")]
//    private static extern void mOnUpgradeFinish();
//
//    [DllImport("__Internal")]
//    private static extern bool mShouldUpgrade();

    [DllImport("__Internal")]
    private static extern bool mIsFirstDay();

    [DllImport("__Internal")]
    private static extern string mGetNetworkStatus();

    [DllImport("__Internal")]
    private static extern string mGetSystemApiLevel();

    [DllImport("__Internal")]
    private static extern void mDoRibrator(int time, int amplitude);

    [DllImport("__Internal")]
    private static extern void mLogEventWithJSON(string eventName, string enbaleES, string enableFabtic, string para);

    [DllImport("__Internal")]
    private static extern string mGetAppOpenTime();
    
    [DllImport("__Internal")]
    private static extern int mTrackingTransparencyStatus();

    [DllImport("__Internal")]
    private static extern void mShowTrackingTransparency();

#endif

    }
}

//UNITY_IOS UNITY_ANDROID UNITY_EDITOR
