using System;
using System.Collections;
using Manager;
using Other;
using Platform;
using UnityEngine;

namespace BFF
{
    public class AsyncThread
    {
        private const string TAG = "AsyncThread----------";
        private static FileRecordQueue _recFile;
        private static ESDataSender _eventSender;
        private static string _savePath = Application.persistentDataPath;
        // private static bool _submitAdData2ESEnabled = PlatformBridge.getConfigBoolean("Application.GED.Enable", false); 
        private static bool _submitAdData2ESEnabled = false;

        //游戏里我们自己发的事件
        public static void SubmitData2ES(string h5Action, Hashtable content = null)
        {
            return;
            ESDataBase.UpdateBaseData();
            Helper.Log(TAG, "SubmitData2ES", h5Action);
            ESDataLoopThread.Instance.WriteGameRecord(_savePath, h5Action, content);
        }

        //080广告事件
        public static void SubmitAdData2ES(string content, string adChance, bool isBannerReturn = false)
        {
            if (!_submitAdData2ESEnabled) return;
            
            return;
            
            ESDataBase.UpdateBaseData();
            Helper.Log(TAG, "SubmitAdData2ES", adChance);
            ESDataLoopThread.Instance.WriteAdRecord(_savePath, content, adChance, isBannerReturn);
        }

        //一天只发一次
        public static void SubmitBaseData2ES()
        {
            if (!_submitAdData2ESEnabled) return;

#if UNITY_EDITOR || UNITY_IOS
            return;
#endif
            
            long lastTime = 0;
            if (PlayerPrefs.HasKey("SubmitBaseDataTime"))
            {
                lastTime = long.Parse(PlayerPrefs.GetString("SubmitBaseDataTime"));
            }

            if (Tools.IsToday(lastTime))
            {
                return;
            }
            
            var startTime = TimeZone.CurrentTimeZone.ToLocalTime(new DateTime(1970, 1, 1, 0, 0, 0)); // 当地时区
            var nowTimeStamp = (long)(DateTime.Now - startTime).TotalMilliseconds;
            PlayerPrefs.SetString("SubmitBaseDataTime", nowTimeStamp.ToString());


            ESDataBase.UpdateBaseData();
            Helper.Log(TAG, "SubmitBaseData2ES");
            ESDataLoopThread.Instance.WriteBaseData(_savePath);
        }

        //存储游戏数据
        public static void AddTask(Action act)
        {
            GameDataLoopThread.Instance.AddTask(act);
        }
    }
}
