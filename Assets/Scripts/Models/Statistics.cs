using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using H5Tracker;
using Manager;
using Newtonsoft.Json;
using Other;
using Platform;
using UnityEngine;

namespace Models
{
    public static class Statistics
    {
        public static void InitData()
        {
            
        }
        
        public static void Send(string eventName, Hashtable data = null)
        {
            return;
            PlatformBridge.logEvent(eventName, data == null ? "" : JsonConvert.SerializeObject(data));
        }
    
        public static void SendAF(string eventName, Hashtable data = null)
        {
            return;
            if (!H5TrackManager.Instance.AppsFlyerInit)
            {
                Helper.LogError("AF 未初始化");
                return;
            }
            
            PlatformBridge.logAFEvent(eventName, data == null ? "" : JsonConvert.SerializeObject(data));
        }

        public static void SendES(string eventName, Hashtable data = null)
        {
            return;
            PlatformBridge.logESEvent(eventName, data == null ? "" : JsonConvert.SerializeObject(data));
        }

        public static void SendFirebase(string eventName)
        {
            return;
//             if (!GameObject.FindObjectOfType<CrashlyticsInit>() || !GameObject.FindObjectOfType<CrashlyticsInit>().InitState)
//             {
//                 Helper.Log("Firebase 未初始化");
//                 return;
//             }
//             
//             
//             Helper.Log( "=======LogFirebaseEvent " + eventName);
//
//             try
//             {
//                 
// #if !UNITY_EDITOR
//             FirebaseAnalytics.LogEvent(eventName);
// #endif
//             }
//             catch (Exception e)
//             {
//                 Helper.LogError("========= " + e.Message);
//                 Console.WriteLine(e);
//                 throw;
//             }

        }
        
        public static void LogFirebaseEvent(string eventName, Dictionary<string, string> paras = null)
        {
            return;
//             if (!GameObject.FindObjectOfType<CrashlyticsInit>() || !GameObject.FindObjectOfType<CrashlyticsInit>().InitState)
//             {
//                 Helper.Log("Firebase 未初始化");
//                 return;
//             }
//             
//             Helper.Log("LogFirebaseEvent", eventName, paras != null);
//
//         
// #if !UNITY_EDITOR
//         if (paras == null)
//         {
//             FirebaseAnalytics.LogEvent(eventName);
//             return;
//         }
//         
//         var data = new List<Parameter>();
//         foreach (var t in paras)
//         {
//             data.Add(new Parameter(t.Key, t.Value));
//         }
//         FirebaseAnalytics.LogEvent(eventName, data.ToArray());
// #endif
        
        }
        

        public static void SendGameOverData()
        {
            return;
//             Player.AddTotalScore();
//             Player.AddTotalRound();
//             Player.AddDayRound();
//             Player.AddDayStep(Constant.GameStatusData.SlideNumber);
//             Player.UpdateDayBestScore(Player.GetCurScore());
//             Player.UpdateLeftInningTime( Constant.GameStatusData.InningTime);
//             Player.UpNewBsetRound();
//
//             if (Constant.GameStatusData.SlideNumber > 0 || Player.GetCurScore() > 0)
//             {
//                 if (!ManagerLocalData.HaveData(ManagerLocalData.AF_ROUND_OVER_SCORE) &&
//                     Player.GetCurScore() >= Constant.AF_ROUND_OVER_SCORE)
//                 {
//                     SendAF("round_over_score");
//                     ManagerLocalData.SetStringData(ManagerLocalData.AF_ROUND_OVER_SCORE, "true");
//                 }
//                 
//                 if (!ManagerLocalData.HaveData(ManagerLocalData.AF_PLAY_ROUND) && Player.GetTotalRound() >= Constant.AF_PLAY_ROUND)
//                 {
//                     SendAF("play_round");
//                     ManagerLocalData.SetStringData(ManagerLocalData.AF_PLAY_ROUND, "true");
//                 }
//
//                 if (!Constant.AFData.round_over_score_2000 && Player.GetCurScore() >= 2000)
//                 {
//                     SendAF("round_over_score_2000");
//                     Constant.AFData.round_over_score_2000 = true;
//                     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
//                 }
//                 string[] eSIDandSwitchFlag = PlatformBridge.getESIDandSwitchFlag().Split('|');
//
//                 SendES("RoundOver", new Hashtable()
//                 {
//                     {"GameLife", Player.GetLoginDay()},
//                     {"CurrentScore", Player.GetCurScore()},
//                     {"BestScore", Player.GetBestScore()},
//                     {"NewHighScore", (Player.IsNewBestStatus() + "").ToLower()},
//                     
//                     {"SlideNumber", Constant.GameStatusData.SlideNumber},
// //                    {"RemoveNumber", Constant.GameStatusData.RemoveNumber},
//                     {"ComboNumber", Constant.GameStatusData.ComboNumber},
// //                    {"HighComboNumber", Constant.GameStatusData.HighComboNumber},
//                     {"InningTime", Tools.ChinaRound(Constant.GameStatusData.InningTime)},
// //                    {"GuideTime", Constant.GameStatusData.GuideTime},
// //                    {"ContinueSlide", Constant.GameStatusData.ContinueSlide},
// //                    {"ContinueRemove", Constant.GameStatusData.ContinueRemove},
//                     
// //                    {"IsRestart", Constant.GameStatusData.IsRestart + ""},
// //                    {"SwitchSound", ManagerAudio.GetMusicEnabled() + ""},
//                     
// //                    {"videoReady", Constant.GameStatusData.videoReady + ""},
// //                    {"videoShow", Constant.GameStatusData.videoShow + ""},
// //                    {"videoReward", Constant.GameStatusData.videoReward + ""},
// //                    {"interstitialReady", Constant.GameStatusData.interstitialReady + ""},
// //                    {"interstitialShow", Constant.GameStatusData.interstitialShow + ""},
// //                    {"interstitialShould", Constant.GameStatusData.interstitialShould + ""},
//
// //                    {"continueUse", Player.IsSecondChanceUsed() + ""},
// //                    {"continueShow", Constant.GameStatusData.continueShow + ""},
// //                    {"continueClk", Constant.GameStatusData.continueClk + ""},
// //                    {"continueShould", Constant.GameStatusData.continueShould + ""},
//                     
// //                    {"dayRound", Player.GetDayRound()},
// //                    {"dayBestScore", Player.GetDayBestScore()},
// //                    {"totalScore", Player.GetTotalScore()},
// //                    {"totalRound", Player.GetTotalRound()},
//                     
//                     {"CurrentLevel", Constant.GameStatusData.CurrentLevel},
//                     {"RemoveFrozen", Constant.GameStatusData.RemoveFrozen},
//                     
//                     {"DifficultyLevel", Blocks.GetDifficulty()},
// //                    {"AverageSteps", Player.GetStepAverage()},
//                     
//                     //道具相关
// //                    {"propVideoShould", "false"},
// //                    {"propVideo", "false"},
// //                    {"propRemain_bomb", 0},
// //                    {"propUsed_bomb", 0},
//                     
//                     //消除类型相关
//                     {"clearTypeA", Constant.GameStatusData.clearTypeA},
//                     {"clearTypeA2", Constant.GameStatusData.clearTypeA2},
//                     {"clearTypeB", Constant.GameStatusData.clearTypeB},
//                     {"clearTypeB2", Constant.GameStatusData.clearTypeB2},
//                     {"clearTypeC", Constant.GameStatusData.clearTypeC},
//                     
//                     //切割相关
// //                    {"dwadClick", Constant.GameStatusData.DWAD_CLICK + ""},
// //                    {"dwadUse", Constant.GameStatusData.DWAD_USE + ""},
// //                    {"dwadClickSlideNumber", Constant.GameStatusData.DWAD_CLICK_SLIDENUMBER},
// //                    {"dwadSlideNumber", Constant.GameStatusData.DWAD_USE ? Constant.GameStatusData.SlideNumber - Constant.GameStatusData.DWAD_USE_SLIDENUMBER : 0},
//                     
//                     //b421相关
// //                    {"b421Click", Constant.GameStatusData.b421_Clk + ""},
// //                    {"b421Use", Constant.GameStatusData.b421_Use + ""},
// //                    {"b421Count", Constant.GameStatusData.b421_Count},
// //                    {"b421SlideNumber", Constant.GameStatusData.b421_Use ? Constant.GameStatusData.SlideNumber - Constant.GameStatusData.b421_SlideNumber : 0},
//                     
//                     //成就相关
//                     {"dieToLifeCount", Constant.GameStatusData.DieToLifeCount},
//                     {"stepScore", Constant.GameStatusData.StepScore},
//                     {"allClearCount", Constant.GameStatusData.AllClearCount},
//                     
//                     //特殊块相关
//                     {"specialBronze", Constant.GameStatusData.SpecialBronze},
//                     {"specialGold", Constant.GameStatusData.SpecialGold},
//                     {"specialGoldDialog", Constant.GameStatusData.SpecialGoldDialog},
//                     {"specialGoldVideo", Constant.GameStatusData.SpecialGoldVideo},
//                     {"specialGoldVideoShow", Constant.GameStatusData.SpecialGoldVideoShow},
//                     {"scoreWhenGoldClear", Constant.GameStatusData.ScoreWhenSpecialGoldClear},
//                     {"specialGoldInsShow", Constant.GameStatusData.SpecialGoldInsShow},
//                     
//                     //石头块
//                     {"stoneCountGenerate", Constant.GameStatusData.StoneCountGenerate},
//                     {"stoneCountClear", Constant.GameStatusData.StoneCountClear},
//                     
//                     //统一发送数据
//                     {"ShouldShowInterstitial", (Constant.GameStatusData.interstitialShould + "").ToLower()},
//                     {"ShowInterstitial", (Constant.GameStatusData.interstitialShow + "").ToLower()},
//                     {"ShouldShowRewardViedo", (Constant.GameStatusData.continueShould + "").ToLower()},
//                     {"ShowRewardViedo", (Constant.GameStatusData.videoShow + "").ToLower()},
//                     {"RewardVideoResult", ((Constant.GameStatusData.videoReward == "success") + "").ToLower()},
//                     {"ShowSecondChance", (Constant.GameStatusData.continueShow + "").ToLower()},
//                     {"ClickSecondChance", (Constant.GameStatusData.continueClk + "").ToLower()},
//                     {"sumScore", Player.GetTotalScore()},
//                     {"sumRoundCount", Player.GetTotalRound()},
//                     {"PlaceBlockNumber", Constant.GameStatusData.SlideNumber},
//                     {"EliminateNumber", Constant.GameStatusData.RemoveNumber},
//                     
//                     //震动相关
//                     {"shakeSwitch", eSIDandSwitchFlag[1]},
//                     {"shakeTestID",eSIDandSwitchFlag[0]},
//                     {"removeShake", Constant.GameStatusData.RemoveShake},
//                     {"comboShake", Constant.GameStatusData.ComboShake},
//                     {"specialGoldShake", Constant.GameStatusData.SpecialGoldShake},
//                     {"specialBlueSelectShake", Constant.GameStatusData.SpecialBlueSelectShake},
//                     {"specialBlueSplitShake", Constant.GameStatusData.SpecialBlueSplitShake},
//                 });
//                 
//                 Send("user_score", new Hashtable()
//                 {
//                     {"score", Player.GetCurScore() / 100}
//                 });
//                 
//                 Send("inning_time", new Hashtable()
//                 {
//                     {"time", Tools.ChinaRound(Constant.GameStatusData.InningTime / 15)}
//                 });
//                 
//                 Send("combo_time", new Hashtable()
//                 {
//                     {"times", Constant.GameStatusData.ComboNumber}
//                 });
//                 
//                 Send("game_start", new Hashtable()
//                 {
//                     {"gameStatus", 1}
//                 });
//                 
//                 
//                 SendLtRoundCount();
//                 SendLtInningTime();
//                 SendLtBesetScore();
//

            // }
        }


        private static void SendLtInningTime()
        {
            return;
            float leftInningTime = ManagerLocalData.GetFloatData(ManagerLocalData.LEFT_INNING_TIME);
                
            if (!Constant.AFData.inningtime_lt_600 && leftInningTime >= 600)
            {
                SendAF("roundover_inningtime_lt_600");
                SendFirebase("roundover_inningtime_lt_600");
                Constant.AFData.inningtime_lt_600 = true;
                ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            }
                
            if (!Constant.AFData.inningtime_lt_1800 && leftInningTime >= 1800)
            {
                SendAF("roundover_inningtime_lt_1800");
                SendFirebase("roundover_inningtime_lt_1800");
                Constant.AFData.inningtime_lt_1800 = true;
                ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            }
                
            if (!Constant.AFData.inningtime_lt_3000 && leftInningTime >= 3000)
            {
                SendAF("roundover_inningtime_lt_3000");
                SendFirebase("roundover_inningtime_lt_3000");
                Constant.AFData.inningtime_lt_3000 = true;
                ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            }
        }

        public static void SendRetention()
        {
            return;
            // if (!H5TrackManager.Instance.AppsFlyerInit || !GameObject.FindObjectOfType<CrashlyticsInit>().InitState)
            // { 
            //     return;
            // }
            //
            //
            // var beginTime = TimeZone.CurrentTimeZone.ToLocalTime(new DateTime(1970, 1, 1)); // 当地时区
            // //和第一次登陆时的时间差的毫秒值
            // long timeDif = (long)((DateTime.Now - beginTime).TotalMilliseconds - Player.GetFirstOpenAppTime() );
            // var firstOpenTime = beginTime.AddMilliseconds(Player.GetFirstOpenAppTime());
            // //和第一次登陆时的天数差
            // int dayDif =  Tools.DiffDays(firstOpenTime,new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, 23,59,59));
            //
            //
            // if (!Constant.AFData.day1_retention_24h && timeDif >= Tools.OneDayMilliseconds && timeDif <= Tools.OneDayMilliseconds*2)
            // {
            //     SendAF("day1_retention_24h");
            //     SendFirebase("day1_retention_24h");
            //     Constant.AFData.day1_retention_24h = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            //
            // }
            //
            // if (!Constant.AFData.day3_retention_72h && timeDif >= Tools.OneDayMilliseconds*3 && timeDif <= Tools.OneDayMilliseconds*4)
            // {
            //     SendAF("day3_retention_72h");
            //     SendFirebase("day3_retention_72h");
            //     Constant.AFData.day3_retention_72h = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            //
            // if (!Constant.AFData.day1_retention && dayDif == 1)
            // {
            //     SendAF("day1_retention");
            //     SendFirebase("day1_retention");
            //     Constant.AFData.day1_retention = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            //
            // if (!Constant.AFData.day3_retention && dayDif == 3)
            // {
            //     SendAF("day3_retention");
            //     SendFirebase("day3_retention");
            //     Constant.AFData.day3_retention = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
        }

        private static void SendLtRoundCount()
        {
            return;
            // int roundCount = Player.GetTotalRound();
            // if (roundCount == 1 && !Constant.AFData.round_lt_1)
            // {
            //     SendAF("round_1st_over_lifetime");
            //     SendFirebase("round_1st_over_lifetime");
            //     Constant.AFData.round_lt_1 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (roundCount == 5 && !Constant.AFData.round_lt_5)
            // {
            //     SendAF("round_5st_over_lifetime");
            //     SendFirebase("round_5st_over_lifetime");
            //     Constant.AFData.round_lt_5 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (roundCount == 10 && !Constant.AFData.round_lt_10)
            // {
            //     SendAF("round_10st_over_lifetime");
            //     SendFirebase("round_10st_over_lifetime");
            //     Constant.AFData.round_lt_10 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (roundCount == 15 && !Constant.AFData.round_lt_15)
            // {
            //     SendAF("round_15st_over_lifetime");
            //     SendFirebase("round_15st_over_lifetime");
            //     Constant.AFData.round_lt_15 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (roundCount == 20 && !Constant.AFData.round_lt_20)
            // {
            //     SendAF("round_20st_over_lifetime");
            //     SendFirebase("round_20st_over_lifetime");
            //     Constant.AFData.round_lt_20 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            //
            // }
            //
            
        }

        private static void SendLtBesetScore()
        {
            return;
            // bool status = Player.IsNewBestStatus();
            // if (!status)
            // {
            //     return;
            // }
            // int bestRoundCount = ManagerLocalData.GetIntData(ManagerLocalData.NEW_BEST_ROUND);
            // if (bestRoundCount == 1 && !Constant.AFData.bestscore_lt_1)
            // {
            //     SendAF("roundover_bestscore_1th");
            //     SendFirebase("roundover_bestscore_1th");
            //     Constant.AFData.bestscore_lt_1 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (bestRoundCount == 2 && !Constant.AFData.bestscore_lt_2)
            // {
            //     SendAF("roundover_bestscore_2th");
            //     SendFirebase("roundover_bestscore_2th");
            //     Constant.AFData.bestscore_lt_2 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (bestRoundCount == 5 && !Constant.AFData.bestscore_lt_5)
            // {
            //     SendAF("roundover_bestscore_5th");
            //     SendFirebase("roundover_bestscore_5th");
            //     Constant.AFData.bestscore_lt_5 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            // else if (bestRoundCount == 10 && !Constant.AFData.bestscore_lt_10)
            // {
            //     SendAF("roundover_bestscore_10th");
            //     SendFirebase("roundover_bestscore_10th");
            //     Constant.AFData.bestscore_lt_10 = true;
            //     ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
            // }
            //
            //
            
        }
    }
}
