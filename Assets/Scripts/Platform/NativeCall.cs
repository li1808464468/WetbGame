using Plugins;
using UnityEngine;

namespace Platform
{
    public class NativeCall : MonoBehaviour
    {
        private static readonly string TAG = "NativeCall-------";

        // Start is called before the first frame update
        public static NativeCall Instance;

        private void Awake()
        {
            Instance = this;
            this.gameObject.name = "PlatformBridge";
            DontDestroyOnLoad(this);//相当于cocos的常驻节点
        }

        void Start()
        {

        }

        // 登录Google Play成功回调
        public void accreditSuccessGooglePlay()
        {
//        EventManager.Instance.DispatchEvent("accreditSuccessFB");
//        EventManager.Instance.DispatchEvent("accreditSuccessFBOver");
//        EventManager.Instance.DispatchEvent("upFBData");
        }

        public void OnSplashFinish()

        {
            Helper.Log(TAG + "OnSplashFinish");
            AppLovinMediationAdapter.Instance.OnSplashFinish();
        }

        public void OnMainSceneOnRestart()
        {
            Helper.Log(TAG + "OnMainSceneOnRestart");
//            MoPubMediationAdapter.Instance.OnMainSceneOnRestart();

            PlatformBridge.OnMainSceneOnRestart();
        }

        public void OnGDPRGranted()
        {
            Helper.Log(TAG + "OnGDPRGranted");
            H5Tracker.H5TrackManager.Instance.InitAppsFlyer();
            H5Tracker.H5TrackManager.Instance.InitFlurry();
        }

    }
}
