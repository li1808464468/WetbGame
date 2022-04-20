using Platform;
using Plugins;
using UnityEngine;

public class BFMoPubDataManager : MonoBehaviour
{
    private const string TAG = "BFMoPubDataManager";

    private void Awake()
    {
        gameObject.name = "BFMoPubDataManager";
        DontDestroyOnLoad(this); 
    }

    public void EmitBFRewardedVideoShownEvent(string json)
    {
        Helper.Log(TAG, "EmitBFRewardedVideoShownEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
    }

    public void EmitBFRewardedVideoClickedEvent(string json)
    {
        Helper.Log(TAG, "EmitBFRewardedVideoClickedEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
    }

    public void EmitBFRewardedVideoReceivedRewardEvent(string json)
    {
        Helper.Log(TAG, "EmitBFRewardedVideoReceivedRewardEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
    }

    public void EmitBFRewardedVideoReturnEvent(string json)
    {
        Helper.Log(TAG, "EmitBFRewardedVideoReturnEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetRewardVideoAdChanceData());
    }

    public void EmitBFInterstitialShownEvent(string json)
    {
        Helper.Log(TAG, "EmitBFInterstitialShownEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetInterstitialData());
    }

    public void EmitBFInterstitialClickedEvent(string json)
    {
        Helper.Log(TAG, "EmitBFInterstitialClickedEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetInterstitialData());
    }

    public void EmitBFInterstitialReturnEvent(string json)
    {
        Helper.Log(TAG, "EmitBFInterstitialReturnEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, GetInterstitialData());
    }

    public void EmitBFBannerShownEvent(string json)
    {
        Helper.Log(TAG, "EmitBFBannerShownEvent", json);
        
        Helper.Log(TAG + json);
        if (PlatformBridge.getConfigBoolean("Application.Banner.GEB", false))
        {
            PlatformBridge.submitAdData(json, "bottom");
        }
    }

    public void EmitBFBannerClickedEvent(string json)
    {
        Helper.Log(TAG, "EmitBFBannerClickedEvent");
        
        Helper.Log(TAG + json);
        PlatformBridge.submitAdData(json, "bottom");
        
        Helper.Log(TAG + "onBFBannerClicked");
        AppLovinMediationAdapter.BannerClickedData = json;
    }
    
    
    private string GetRewardVideoAdChanceData()
    {
        string adtype = AppLovinMediationAdapter.Instance.GetRewardType();
        Helper.Log(TAG + adtype);
        return adtype;
    }

    private string GetInterstitialData()
    {
        string adtype = AppLovinMediationAdapter.Instance.GetInterstitialType();
        Helper.Log(TAG + adtype);
        return adtype;
    }
}
