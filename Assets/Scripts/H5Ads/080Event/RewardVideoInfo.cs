using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Other;
using Platform;
using UnityEngine;

namespace Plugins
{
    public class RewardVideoInfo
    {
        private readonly string TAG = "RewardVideoInfo------------ ";
        
        private Dictionary<string, Hashtable> mRewardData = new Dictionary<string, Hashtable>();
        private Dictionary<string, Hashtable> mRewardMetadata = new Dictionary<string, Hashtable>();
        private Dictionary<string, long> mRewardShowTime = new Dictionary<string, long>();
        private Dictionary<string, long> mRewardIDClickTime = new Dictionary<string, long>();
        private Dictionary<string, long> mRewardClickTime = new Dictionary<string, long>();
//         public void OnRewardedAdLoadedEvent(string adUnit, MaxSdkBase.AdInfo adInfo) 
//         {
//             
//             Hashtable meta;
//             if (this.mRewardMetadata.ContainsKey(adUnit)) {
//                 ((Hashtable)this.mRewardMetadata[adUnit]).Clear();
//             } else {
//                 meta = new Hashtable();
//                 this.mRewardMetadata.Add(adUnit, meta);
//             }
//
//             string networkName = adInfo.NetworkName;
//             string networkPlacement = adInfo.NetworkPlacement;
//            
//             
//             try 
//             {
// //                if (networkName.Contains("AppLovin")) {
// //                    meta.Add("vendor", "AppLovin");
// //                    meta.Add("id", networkPlacement);
// //                } else if (networkName.Contains("Facebook")) {
// //                    meta.Add("vendor", "Facebook");
// //                    meta.Add("id", serverExtras.get("placement_id"));
// //                } else if (networkName.Contains("IronSource")) {
// //                    meta.Add("vendor", "IronSource");
// //                    meta.Add("id", serverExtras.get("instanceId"));
// //                } else if (networkName.Contains("Unity")) {
// //                    meta.Add("vendor", "Unity");
// //                    meta.Add("id", (String)serverExtras.get("zoneId") + "_" + (String)serverExtras.get("gameId"));
// //                } else if (networkName.Contains("Vungle")) {
// //                    meta.Add("vendor", "Vungle");
// //                    meta.Add("id", serverExtras.get("pid"));
// //                } else if (networkName.Contains("Chartboost")) {
// //                    meta.Add("vendor", "Chartboost");
// //                    meta.Add("id", serverExtras.get("location"));
// //                } else if (networkName.Contains("GooglePlay")) {
// //                    meta.Add("id", serverExtras.get("adunit"));
// //                    String id = (String)serverExtras.get("adUnitID");
// //                    if (TextUtils.isEmpty(id)) {
// //                        meta.Add("vendor", "Admob");
// //                    } else if (id.toLowerCase().startsWith("ca-app-pub")) {
// //                        meta.Add("vendor", "Admob");
// //                    } else if (id.toLowerCase().startsWith("ca-mb-app-pub")) {
// //                        meta.Add("vendor", "Adx");
// //                    } else {
// //                        meta.Add("vendor", "Dfp");
// //                    }
// //                } else if (networkName.Contains("AdColony")) {
// //                    meta.Add("vendor", "AdColony");
// //                    meta.Add("id", serverExtras.get("zoneId"));
// //                } else if (networkName.Contains("InMobi")) {
// //                    meta.Add("vendor", "InMobi");
// //                    meta.Add("id", serverExtras.get("placementid"));
// //                } else {
// //                    meta.Add("vendor", "MoPub");
// //                    meta.Add("id", adUnit + "_" + adResponse.getAdGroupId());
// //                }
//                 
//                 meta = (Hashtable)this.mRewardMetadata[adUnit];
//                 meta.Add("vendor", networkName);
//                 meta.Add("id", networkPlacement);
//                 meta.Add("ad_format", "rewardedvideo");
//                 this.mRewardMetadata.Add(adUnit, meta);
//             } catch (Exception var7) {
//                 
//             }
//     
//             if (this.mRewardData.ContainsKey(adUnit)) {
//                 ((Hashtable)this.mRewardData[adUnit]).Clear();
//             } else {
//                 this.mRewardData.Add(adUnit, new Hashtable());
//             }
//     
//             meta = (Hashtable)this.mRewardData[adUnit];
// //            meta.Add("request_id", adResponse.getRequestId());
//             meta.Add("placement_name", adUnit);
//             meta.Add("event_meta", this.mRewardMetadata[adUnit]);
//             this.mRewardData.Add(adUnit, meta);
//             
//         }
        
        // public void OnRewardedVideoShownEvent(string adUnitId, MaxSdkBase.AdInfo adInfo) 
        // {
        //     updateImpressionTime(adUnitId);
        //     Hashtable data = mRewardData[adUnitId];
        //     long now = Tools.GetCurrentTimeMillis();
        //     data.Add("event_time", now);
        //     data.Add("event_type", 2);
        //     data.Add("event_name", "ad_impression");
        //     Hashtable metadata = mRewardMetadata[adUnitId];
        //     long delta = now - getIDClickTime(adUnitId, now);
        //     
        //     metadata.Add("duration", delta);
        //     if (metadata.ContainsKey("ad_finish"))
        //     {
        //         metadata.Remove("ad_finish");
        //     }
        //     updateClickTime(adUnitId, -1);
        //     BFAppLovinDataManager.EmitBFRewardedVideoShownEvent(data.ToString());
        // }
        //
        // public void OnRewardedVideoClickedEvent(string adUnitId, MaxSdkBase.AdInfo adInfo)
        // {
        //     long now = Tools.GetCurrentTimeMillis();
        //     Hashtable data = mRewardData[adUnitId];
        //     data.Add("event_type", 3);
        //     data.Add("event_name", "ad_click");
        //     data.Add("event_time", now);
        //     Hashtable metadata = mRewardMetadata[adUnitId];
        //     long delta = now - getImpressionTime(adUnitId);
        //     if (delta < 0L) {
        //         delta = -1L;
        //     }
        //
        //     metadata.Add("duration", delta);
        //     if (metadata.ContainsKey("ad_finish"))
        //     {
        //         metadata.Remove("ad_finish");
        //     }
        //     
        //     BFAppLovinDataManager.EmitBFRewardedVideoClickedEvent(data.ToString());
        //     updateClickTime(adUnitId, now);
        //     updateIDClickTime(adUnitId, now);
        // }
        //
        
        void updateIDClickTime(string requestID, long time) 
        {
            if (this.mRewardIDClickTime.ContainsKey(requestID)) 
            {
                this.mRewardIDClickTime[requestID] = time;
            }
            else
            {
                this.mRewardIDClickTime.Add(requestID, time);
            }
        }

        long getIDClickTime(string requestID, long defaultTime) 
        {
            return !this.mRewardIDClickTime.ContainsKey(requestID) ? defaultTime : (long)this.mRewardIDClickTime[requestID];
        }
        
        
        
        void updateImpressionTime(string adUnit) 
        {
            if (this.mRewardShowTime.ContainsKey(adUnit))
            {
                this.mRewardShowTime[adUnit] = Tools.GetCurrentTimeMillis();
            }
            else
            {
                this.mRewardShowTime.Add(adUnit, Tools.GetCurrentTimeMillis());
            }
            
        }

        long getImpressionTime(string adUnit) 
        {
            return this.mRewardShowTime.ContainsKey(adUnit) ? (long)this.mRewardShowTime[adUnit] :  Tools.GetCurrentTimeMillis();
        }
        
        
        void updateClickTime(string adUnit, long time) 
        {
            if (this.mRewardClickTime.ContainsKey(adUnit))
            {
                this.mRewardClickTime[adUnit] = time;
            }
            else
            {
                this.mRewardClickTime.Add(adUnit, time);
            }
            
        }

        long getClickTime(string adUnit) 
        {
            return this.mRewardClickTime.ContainsKey(adUnit) ? (long)this.mRewardClickTime[adUnit] : -1;
        }
        
    }


}
