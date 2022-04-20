using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
//using Blowfires.AssetManager;
//using System.Runtime.InteropServices;
using DG.Tweening;
using Manager;
using Models;
using Platform;
using TMPro;
using UnityEngine;
using UnityEngine.AddressableAssets;
using UnityEngine.ResourceManagement.AsyncOperations;
using UnityEngine.UI;
using Random = System.Random;

namespace Other
{
    public static class Tools
    {
        private static readonly Random Ran = new Random();

        public static double OneDayMilliseconds = 24 * 3600 * 1000;

        /// <summary>
        /// 四舍五入, 在x.5时取离0远的那个数
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static int ChinaRound(float d)
        {
            return (int)Math.Round(d, MidpointRounding.AwayFromZero);
        }

        // 获取引用类型的内存地址方法
        //        public static string GetMemory(object o)
        //        {
        //            var h = GCHandle.Alloc(o, GCHandleType.Pinned);
        //            var adar = h.AddrOfPinnedObject();
        //            return "0x" + adar.ToString("X");
        //        }

        public static int GetNumFromRange(int min, int max)
        {
            return Ran.Next(min, max + 1);
        }


        public static long GetCurrentTimeMillis()
        {
            return (DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000;
        }

        public static List<T> RandomSortList<T>(List<T> listT)
        {
            var random = new Random();
            var newList = new List<T>();
            foreach (var item in listT)
            {
                newList.Insert(random.Next(newList.Count + 1), item);
            }
            return newList;
        }


        /// <summary>
        /// 相差天数
        /// </summary>
        /// <param name="startTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public static int DiffDays(DateTime startTime, DateTime endTime)
        {
            TimeSpan daysSpan = new TimeSpan(endTime.Ticks - startTime.Ticks);
            return (int)daysSpan.TotalDays;
        }


        public static bool IsToday(long timeStamp)
        {
            var today = DateTime.Now.ToString("yyyy/MM/dd");

            var startTime = TimeZone.CurrentTimeZone.ToLocalTime(new DateTime(1970, 1, 1)); // 当地时区

            var dt = startTime.AddMilliseconds(timeStamp);
            var day = dt.ToString("yyyy/MM/dd");

            var todayArr = today.Split('/');
            var dayArr = day.Split('/');

            for (var i = 0; i < todayArr.Length; ++i)
            {
                if (todayArr[i] != dayArr[i])
                {
                    return false;
                }
            }
            return true;
        }

        public static void NumChange(TextMeshProUGUI label, int endNum, float time = 0.5f, Action changeCallback = null, bool isToStandard = false, Action endCallback = null)
        {
            var startNum = int.Parse(label.text.Replace(",", ""));
            var num = endNum - startNum;

            if (Constant.LevelProgressVersion == "1")
                if (num < 0)
                {
                    num = endNum;
                }
            if (num != 0)
            {
                var seq = DOTween.Sequence();
                seq.Append(DOTween.To(delegate (float value)
                {
                    value = (float)Math.Floor(value);
                    if (isToStandard)
                    {
                        label.text = NumToStandard((int)value);
                    }
                    else
                    {
                        label.text = ((int)value).ToString();
                    }
                    changeCallback?.Invoke();
                }, startNum, endNum, time).SetEase(Ease.Linear).OnComplete(() =>
                {
                    endCallback?.Invoke();
                }));
                seq.SetUpdate(true);
            }
        }

        public static string NumToStandard(int num)
        {
            return num.ToString("N0");
        }

        public static void DoVibrator(int time, int amplitude)
        {
            if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on")
            {
                PlatformBridge.doRibrator(time, amplitude);
            }
        }

        public static void SetImgOpacity(Image img, int opacityNum = 0)
        {
            var tmpColor = img.color;
            tmpColor.a = opacityNum;
            img.color = tmpColor;
        }

        //        public static Vector3[] GetPointsFromBezier(Vector3 p0, Vector3 p1, Vector3 p2, int pointNum)
        //        {
        //            var points = new Vector3[pointNum + 1];
        //
        //            var deltaT = 1f / pointNum;
        //            var count = 0;
        //            for (var t = 0f; t <= 1f; t = t + deltaT)
        //            {
        //                //二次bezier
        //                points[count] = (1 - t) * (1 - t) * p0 + 2 * t * (1 - t) * p1 + t * t * p2;
        //                ++count;
        //            }
        //            return points;
        //        }

        public static async Task<T> LoadAssetAsync<T>(string assetPath)
        {
            //            if (Constant.SceneVersion == "2")
            //            {
            //                assetPath = "Prefabs_Scene2/" + assetPath;
            //            } else if (Constant.SceneVersion == "3")
            //            {
            //                assetPath = "Prefabs_Scene3/" + assetPath;
            //            }

            //            var tmpTime = PlatformBridge.GetEnterGameTime();
            //            DebugEx.Log("LoadAssetAsync", assetPath, "start");
            //            var result = await AssetManagerFacade.LoadAssetAsync<T>(assetPath);
            //            DebugEx.Log("LoadAssetAsync", assetPath, (PlatformBridge.GetEnterGameTime() - tmpTime) / 1000f);

            var result = await Addressables.LoadAssetAsync<T>(assetPath).Task;
            return result;
        }

        //        public static async Task<GameObject> InstantiateAsync(string assetPath)
        //        {
        //            var tmpTime = PlatformBridge.GetEnterGameTime();
        //            DebugEx.Log("InstantiateAsync", assetPath, "start");
        //            var result = await AssetManagerFacade.InstantiateAsync(assetPath);
        //            DebugEx.Log("InstantiateAsync", assetPath, (PlatformBridge.GetEnterGameTime() - tmpTime) / 1000f);
        //            return result; 
        //        }
    }

    public static class Delay
    {
        public static IEnumerator Run(Action action, float delaySeconds)
        {
            yield return new WaitForSeconds(delaySeconds);
            action();
        }
    }
}
