using System.Collections;
using System.Collections.Generic;
using Models;
using Newtonsoft.Json;
using Platform;
using UnityEngine;

namespace Plugins
{
    public class AppLovinMediationAdapter
    {
        private static readonly string TAG = "AppLovinMediationAdapter-------";
        public static string BannerClickedData = ""; //上报的banner点击数据

        private static AppLovinMediationAdapter instance = null;
        private static readonly object padlock = new object();

        private readonly AppLovinMediationRewardVideo _rewardVideo = new AppLovinMediationRewardVideo();
        private readonly AppLovinMediationInterstital _interstitial = new AppLovinMediationInterstital();
        private readonly AppLovinMediationBanner _banner = new AppLovinMediationBanner();
        private readonly Dictionary<string, string> _impressionData = new Dictionary<string, string>();
        private readonly Dictionary<string, string> _adType = new Dictionary<string, string>();

        AppLovinMediationAdapter()
        {

            var sdkKey = "BBBcJn2C0NcLCxiqER8XdzgiXBPBG5dRHq3ba6obTOxgKdUT4c5hZmP6xpHBvwdBkhnJoTwGyrxGTQqX1vEbed";

            Helper.Log(TAG + "AppLovinMediationAdapter.InitializeSdk");


            // MaxSdkCallbacks.OnSdkInitializedEvent += OnSdkInitializedEvent;
            //
            // MaxSdk.SetSdkKey(sdkKey);
            // MaxSdk.SetUserId(PlatformBridge.getCustomUserID());
            // MaxSdk.InitializeSdk();

            Helper.Log(TAG + "AppLovin.InitializeSdk End");

            
        }

        public static AppLovinMediationAdapter Instance
        {
            get
            {
                lock (padlock)
                {
                    if (instance == null)
                    {
                        instance = new AppLovinMediationAdapter();
                    }
                    return instance;
                }
            }

        }
        
        
//         private void OnSdkInitializedEvent(MaxSdkBase.SdkConfiguration sdkConfiguration) {
//
//             Helper.Log(TAG + "OnSdkInitializedEvent");
//             
//             if (sdkConfiguration.ConsentDialogState == MaxSdkBase.ConsentDialogState.Applies)
//             {
//                 // Show user consent dialog
//                 Helper.Log(TAG  + " MaxSdkBase.ConsentDialogState.Applies");
//                 
//             }
//             else if (sdkConfiguration.ConsentDialogState == MaxSdkBase.ConsentDialogState.DoesNotApply)
//             {
//                 // No need to show consent dialog, proceed with initialization
// //                onSdkInitialized();
//                 Helper.Log(TAG  + " MaxSdkBase.ConsentDialogState.DoesNotApply");
//
//             }
//             else
//             {
//                 // Consent dialog state is unknown. Proceed with initialization, but check if the consent
//                 // dialog should be shown on the next application initialization
//                 Helper.Log(TAG + " dialog should be shown on the next application initialization");
//             }
// //            
//             onSdkInitialized();
//
//
// //#if LOG
// //           MaxSdk.ShowMediationDebugger();
// //#endif
//
//
//         }

        private void onSdkInitialized()
        {
            _rewardVideo.OnSdkInitializedEvent();
            _interstitial.OnSdkInitializedEvent();
            _banner.OnSdkInitializedEvent();
        }
        
        
         //Banner
        public void ShowBanner()
        {
            _banner.LoadBanner();
        }
        

        public void LoadInterstitial(string placementName = "", int loadCount = 1) {
            _interstitial.LoadInterstitial();
        }

        public void ShowInterstitialAd(bool restart = false) {
            _interstitial.ShowInterstitialAd(restart);
        }

        public void Show5sInterstitialAd(bool restart = false)
        {
            _interstitial.Show5sInterstitialAd(restart);
        }
        
        public int InterstitialAdAvailableCount() {
            return _interstitial.InterstitialAdAvailableCount();
        }

        public int FiveSecInterstitialCount()
        {
            return _interstitial.FiveSecInterstitialCount();
        }
        

        public void LoadRewardVideo(string placementName = "", int loadCount = 1) {
            _rewardVideo.LoadRewardVideo();
        }

        public void ShowRewardVideo() {
            // _rewardVideo.ShowRewardVideo();
        }
        

        public int RewardVideoAvailableCount() {
            return _rewardVideo.RewardVideoAvailableCount();
        }

        public int RewardVideoAvailableCount(string placementName) {
            return RewardVideoAvailableCount();
        }
        
        
        
//         public void OnImpressionTrackedEvent(string adUnitId, MaxSdkBase.AdInfo impressionData) {
//             Helper.Log(TAG + 
//                 "AppLovinMediationAdapter OnImpressionTrackedEvent adUnitId:" + adUnitId + " impressionData.JsonRepresentation:" +
//                 impressionData.ToString());
//
//             _impressionData.Clear();
//
// //            if (impressionData.Currency != null)
// //            {
// //                _impressionData.Add(AFInAppEvents.CURRENCY, impressionData.Currency);
// //            }
//             if (impressionData.AdUnitIdentifier != null)
//             {
//                 _impressionData.Add("BF_AdUnitId", impressionData.AdUnitIdentifier);
//             }
// //            if (impressionData.AdUnitName != null)
// //            {
// //                _impressionData.Add("BF_AdUnitName", impressionData.AdUnitName);
// //            }
// //            if (impressionData.AdUnitFormat != null)
// //            {
// //                _impressionData.Add("BF_AdUnitFormat", impressionData.AdUnitFormat);
// //            }
// //            if (impressionData.AdGroupId != null)
// //            {
// //                _impressionData.Add("BF_AdGroupId", impressionData.AdGroupId);
// //            }
// //            if (impressionData.AdGroupName != null)
// //            {
// //                _impressionData.Add("BF_AdGroupName", impressionData.AdGroupName);
// //            }
// //            if (impressionData.Country != null) 
// //            {
// //                _impressionData.Add("BF_Country", impressionData.Country);
// //            }
//
//             _impressionData.Add("BF_Country", MaxSdk.GetSdkConfiguration().CountryCode);
//
//             if (impressionData.NetworkName != null)
//             {
//                 _impressionData.Add("BF_NetworkName", impressionData.NetworkName);
//             }
// //            if (impressionData.NetworkPlacementId != null)
// //            {
// //                _impressionData.Add("BF_NetworkPlacementId", impressionData.NetworkPlacementId);
// //            }
// //            if (impressionData.AdGroupType != null)
// //            {
// //                _impressionData.Add("BF_AdGroupType", impressionData.AdGroupType);
// //            }
//
//
//             string adEvent = "unknown";
//             if (_adType.ContainsKey(adUnitId))
//             {
//                 adEvent = _adType[adUnitId];
//             }
//             else if (_banner.IsBannerId(adUnitId))
//             {
//                 adEvent = "BF_Banner_Revenue";
//                 _adType[adUnitId] = adEvent;
//                 _impressionData.Add("BF_AdUnitFormat", "Banner");
//             }
//             else if (_interstitial.IsInterstitialId(adUnitId))
//             {
//                 adEvent = "BF_Interstitial_Revenue";
//                 _adType[adUnitId] = adEvent;
//                 _impressionData.Add("BF_AdUnitFormat", "Fullscreen");
//
//             }
//             else if (_rewardVideo.IsRewardId(adUnitId))
//             {
//                 adEvent = "BF_Reward_Revenue";
//                 _adType[adUnitId] = adEvent;
//                 _impressionData.Add("BF_AdUnitFormat", "Rewarded Video");
//
//             }
//
//             decimal revenue = (decimal)impressionData.Revenue;
//             
//
//             if (revenue<= 0)
//             {
//                 revenue = 0;
//             }
//             
// //            if (impressionData.Revenue != 0)
// //            {
//                 Player.UpdateADRevenue((float)revenue);
//                 _impressionData.Add(AFInAppEvents.REVENUE, (revenue).ToString());
//                 
//                 
// //            }
// //            else
// //            {
// //                if (_impressionData.ContainsKey("BF_NetworkName")
// //                    && (_impressionData["BF_NetworkName"].ToLower().Contains("facebook") || _impressionData["BF_NetworkName"].ToLower().Contains("undisclosed")))
// //                {
// //                    if (adEvent.Contains("Reward"))
// //                    {
// //                        _impressionData.Add(AFInAppEvents.REVENUE, PlatformBridge.getConfigString("Application.Bidding.Facebook.Reward", "0"));
// //                    } else if (adEvent.Contains("Interstitial"))
// //                    {
// //                        _impressionData.Add(AFInAppEvents.REVENUE, PlatformBridge.getConfigString("Application.Bidding.Facebook.Interstitial", "0"));
// //                    }
// //                }
// //            }
//
//
//
//             AppsFlyer.sendEvent(adEvent, _impressionData);
//             PlatformBridge.logESEvent(adEvent, false, JsonConvert.SerializeObject(_impressionData, Formatting.Indented));
//
//         }

        private void OnConsentDialogLoadedEvent() {
            Helper.Log(TAG + "MoPubMediationAdapter OnConsentDialogLoadedEvent");
        }


        private void OnConsentDialogFailedEvent(string err) {
            Helper.Log(TAG + "MoPubMediationAdapter OnConsentDialogFailedEvent:" + err);
        }


        private void OnConsentDialogShownEvent() {
            Helper.Log(TAG + "MoPubMediationAdapter OnConsentDialogShownEvent");
        }

        public void AssignRewardType(string type)
        {
            _rewardVideo.AssignRewardType(type);
        }

        public void OnMainSceneOnRestart()
        {
            _interstitial.OnMainSceneOnRestart();
            _rewardVideo.OnMainSceneOnRestart();
            _banner.OnMainSceneOnRestart();
            BannerClickedData = "";
        }

        public void OnSplashFinish()
        {
            _banner.OnSplashFinish();
        }

        public void OnGuideComplete()
        {
            _banner.OnGuideComplete();
        }

        public void DestroyBanner()
        {
            _banner.DestroyBanner();
        }
        
        public string GetRewardType( )
        {
            return _rewardVideo.rewardType;
        }
        
        public string GetInterstitialType( )
        {
            return _interstitial.interstitialType;
        }


        
    }

}

