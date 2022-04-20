using System.Collections;
using System.Collections.Generic;
using Plugins;
using UnityEngine;

namespace Platform
{
    public static class BFAppLovinDataManager
    {
        private const string TAG = "BFMoPubDataManager";
        
        public static void EmitBFRewardedVideoShownEvent(string json)
        {
            Helper.Log(TAG, "EmitBFRewardedVideoShownEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
        }
    
        public static void EmitBFRewardedVideoClickedEvent(string json)
        {
            Helper.Log(TAG, "EmitBFRewardedVideoClickedEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
        }
    
        public static void EmitBFRewardedVideoReceivedRewardEvent(string json)
        {
            Helper.Log(TAG, "EmitBFRewardedVideoReceivedRewardEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
        }
    
        public static void EmitBFRewardedVideoReturnEvent(string json)
        {
            Helper.Log(TAG, "EmitBFRewardedVideoReturnEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
        }
    
        public static void EmitBFInterstitialShownEvent(string json)
        {
            Helper.Log(TAG, "EmitBFInterstitialShownEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetInterstitialData());
        }
    
        public static void EmitBFInterstitialClickedEvent(string json)
        {
            Helper.Log(TAG, "EmitBFInterstitialClickedEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetInterstitialData());
        }
    
        public static void EmitBFInterstitialReturnEvent(string json)
        {
            Helper.Log(TAG, "EmitBFInterstitialReturnEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, GetInterstitialData());
        }
    
        public static void EmitBFBannerShownEvent(string json)
        {
            Helper.Log(TAG, "EmitBFBannerShownEvent", json);
            
            Helper.Log(TAG + json);
            if (PlatformBridge.getConfigBoolean("Application.Banner.GEB", false))
            {
                PlatformBridge.submitAdData(json, "bottom");
            }
        }
    
        public static void EmitBFBannerClickedEvent(string json)
        {
            Helper.Log(TAG, "EmitBFBannerClickedEvent");
            
            Helper.Log(TAG + json);
            PlatformBridge.submitAdData(json, "bottom");
            
            Helper.Log(TAG + "onBFBannerClicked");
            AppLovinMediationAdapter.BannerClickedData = json;
        }
        
        private static string GetRewardVideoAdChanceData()
        {
            string adtype = AppLovinMediationAdapter.Instance.GetRewardType();
            Helper.Log(TAG + adtype);
            return adtype;
        }

        private static string GetInterstitialData()
        {
            string adtype = AppLovinMediationAdapter.Instance.GetInterstitialType();
            Helper.Log(TAG + adtype);
            return adtype;
        }
        
        
    }

}


