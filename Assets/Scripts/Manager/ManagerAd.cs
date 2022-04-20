using System.Collections;
using H5Ads;
using Models;
using Platform;
using UI;

namespace Manager
{
    public static class ManagerAd
    {
        public enum RewardVideoPlayType
        {
            SecondChance = 1,
            B421,
            B43221,
            B43221And1Hang,
            Prop1,
            Prop2,
            Prop3,
            Prop4,
            SpecialGold,
        }
        
        public enum InterstitialPlayType
        {
            Default = 1,
            SpecialGold,
            Restart
        }
        
        private static int _rewardVideoPlayType = 0;
        private static int _interstitialPlayType = 0;
        
        public static void InitData()
        {
            
        }

        public static bool HaveRewardAd()
        {
            if (PlatformBridge.getConfigBoolean("Application.RewardVideo.ESLog", false))
            {
                Statistics.Send("Should_Show_Reward_Video");
            }
            
            if (PlatformBridge.getConfigBoolean("Application.RewardVideo.ESLogShow", false))
            {
                Statistics.SendES("Should_Show_Reward_Video");
            }

            return PlatformBridge.isRewardVideoReady();
        }

        public static void PlayRewardAd(int playType = (int)RewardVideoPlayType.SecondChance)
        {
            ManagerAudio.PauseBgm();
            
            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdShown();
            _rewardVideoPlayType = playType;
            PlatformBridge.showRewardVideoAd();
        }

        public static void OnRewardAdShown()
        {
//            ManagerAudio.PauseBgm();
        }

        public static void OnRewardAdClose(string result = "success")
        {
            switch (result)
            {
                case "success":
                    if (_rewardVideoPlayType == (int) RewardVideoPlayType.SecondChance)
                    {
                        Constant.GamePlayScript.SecondChanceResult(new Hashtable(){{"success", true}});
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.B421)
                    {
                        Constant.GamePlayScript.B421Result(new Hashtable(){{"success", true}});
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.B43221)
                    {
                        Constant.GamePlayScript.B43221Result(new Hashtable(){{"success", true}});
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.B43221And1Hang)
                    {
                        Constant.GamePlayScript.B43221And1HangResult(new Hashtable(){{"success", true}});
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.Prop1)
                    {
                        Player.UpdatePropNumById(Constant.Prop1, Player.GetPropNumById(Constant.Prop1) + 1);
                        Constant.GamePlayScript.UpdatePropInfo();
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.Prop2)
                    {
                        Player.UpdatePropNumById(Constant.Prop2, Player.GetPropNumById(Constant.Prop2) + 1);
                        Constant.GamePlayScript.UpdatePropInfo();
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.Prop3)
                    {
                        Player.UpdatePropNumById(Constant.Prop3, Player.GetPropNumById(Constant.Prop3) + 1);
                        Constant.GamePlayScript.UpdatePropInfo();
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.Prop4)
                    {
                        Player.UpdatePropNumById(Constant.Prop4, Player.GetPropNumById(Constant.Prop4) + 1);
                        Constant.GamePlayScript.UpdatePropInfo();
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.SpecialGold)
                    {
                        Constant.GamePlayScript.MoveEnd(null, Constant.PreviousBlocks, true);
                    }
                    break;
                case "fail":
                    if (_rewardVideoPlayType == (int) RewardVideoPlayType.SecondChance || _rewardVideoPlayType == (int) RewardVideoPlayType.B43221And1Hang)
                    {
                        Constant.GamePlayScript.SecondChanceResult();
                    } else if (_rewardVideoPlayType == (int) RewardVideoPlayType.SpecialGold)
                    {
                        Constant.GamePlayScript.MoveEnd(null, Constant.PreviousBlocks);
                    }
                    break;
            }
            
            ManagerAudio.ResumeBgm();

            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdClosed();
        }

        public static bool Have5sInterstitialAd(bool isRestart = false)
        {
            if (PlatformBridge.getConfigBoolean("Application.Interstitial.ESLog", false))
            {
                var eventName = "Should_Show_5sInterstitial_Ad";
                if (isRestart)
                {
                    eventName = "Should_Show_5sRestart_Ad";
                }
                Statistics.Send(eventName);
            }

            return PlatformBridge.FiveSecInterstitialCount();
        }

        public static bool HaveInterstitialAd(bool isRestart = false)
        {
            if (PlatformBridge.getConfigBoolean("Application.Interstitial.ESLog", false))
            {
                var eventName = "Should_Show_Interstitial_Ad";
                if (isRestart)
                {
                    eventName = "Should_Show_Restart_Ad";
                }
                Statistics.Send(eventName);
            }

            if (PlatformBridge.getConfigBoolean("Application.Interstitial.ESLogShow", false))
            {
                var eventName = "Should_Show_Interstitial_Ad";
                if (isRestart)
                {
                    eventName = "Should_Show_Restart_Ad";
                }
                Statistics.SendES(eventName);
            }

            return PlatformBridge.isInterstitialReady();
        }

        public static void PlayInterstitialAd(int playType = (int) InterstitialPlayType.Default)
        {
            ManagerAudio.PauseBgm();
            
            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdShown();
            
            _interstitialPlayType = playType;
            PlatformBridge.showInterstitialAd();
        }

        public static void OnInterstitialAdShown()
        {
//            ManagerAudio.PauseBgm();
        }
        
        public static void OnInterstitialAdClose()
        {
            ManagerAudio.ResumeBgm();
            
            switch (_interstitialPlayType)
            {
                case (int) InterstitialPlayType.Default:
                    Constant.GamePlayScript.ShowGameOverDialog(false);
                    break;
                case (int) InterstitialPlayType.SpecialGold:
                    Constant.GamePlayScript.MoveEnd(null, Constant.PreviousBlocks, true);
                    break;
            }

            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdClosed();
        }

        public static void PlayRestartInterstitialAd()
        {
            ManagerAudio.PauseBgm();
            
            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdShown();
            
            if (PlatformBridge.getConfigBoolean("Application.Interstitial.ESLog", false))
            {
                Statistics.Send("Show_Restart_Ad");
            }
            
            _interstitialPlayType = (int) InterstitialPlayType.Restart;
            PlatformBridge.show5sInterstitialAd(true);
        }

        public static void OnMainSceneRestart()
        {
            Constant.GamePlayScript.GetComponent<RestartAd>().OnMainSceneRestart();
        }

        public static void OnAdClk(int adType = 0)
        {
            Constant.GamePlayScript.GetComponent<RestartAd>().OnAdClk();
        }
    }
}
