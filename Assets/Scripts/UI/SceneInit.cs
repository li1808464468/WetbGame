using System;
using System.Threading.Tasks;
using BFF;
using Manager;
using Models;
using Newtonsoft.Json.Linq;
using Other;
using Platform;
using UnityEngine;
using UnityEngine.AddressableAssets;
using UnityEngine.UI;

namespace UI
{
    public class SceneInit : MonoBehaviour
    {
        //出现空格权重
        public int emptyBlock1 = 6;
        public int emptyBlock2 = 3;
        public int emptyBlock3 = 2;
        
        //场景版本
        public string sceneVersion;
        
        // Start is called before the first frame update
        void Awake()
        {
            Application.targetFrameRate = Constant.FrameRate;
            Constant.InitData();
            Constant.SceneVersion = sceneVersion;
            Constant.EffCtrlScript.LoadResAsync_manRes();
            
            if (!Blocks.IsTesting())
            {
                UpdateServerData();
            }
            
            ManagerLocalData.InitData();
            Player.InitData();
            ManagerAudio.InitData(Constant.GamePlayScript.gameObject);
            ManagerDialog.InitData(Constant.SceneVersion == "3" ? GameObject.Find("CanvasDialogGroup") : gameObject);
            Statistics.InitData();
            ManagerAd.InitData();
            
            SetBlockGroup();
            UpdateDragDelta();
            SetDifficulty();
            SetLevelText();

            if (Constant.SceneVersion == "3")
            {
//                Constant.ClearWaitTime = 0.3f;
//                Constant.DownAnimTime = 0.3f;
                
                Constant.AchievementSwitch = false;
                Constant.SpecialRainbowSwitch = false;
                Constant.StoneSwitch = false;
                Constant.LevelUpEffVersion = "3";
                Constant.SpecialGoldClearTime = 0f;
            } else if (Constant.SceneVersion == "2")
            {
                Constant.AchievementSwitch = true;
                Constant.LevelUpEffVersion = "1";
            }

#if UNITY_EDITOR
            Constant.IceBlockSwitch = true;
            Constant.SpecialGoldSwitch = true;
            Constant.SpecialBronzeSwitch = true;
            Constant.SpecialGoldEffWeight = "1:0:0";
            Constant.SecondChanceEnabled = true;
            Constant.LevelProgressVersion = "1";
            Constant.ShowRemoveAd = true;
            Constant.ShowSpecialGoldDialogAnim = true;
#endif
        }

        void SetLevelText()
        {
            if (Constant.LevelToStageSwitch)
            {
                Constant.LevelText = "Stage ";
                Constant.LevelUpText = "STAGE ";
            }
        }

        void SetBlockGroup()
        {
            if (Constant.SceneVersion == "1" || Constant.SceneVersion == "2" || Constant.SceneVersion == "3")
            {
                Constant.UpdateBlockSize(116);

                if (!Constant.LevelToStageSwitch)
                {
                    Constant.LevelText = "Lv.";

                    if (Constant.SceneVersion == "3")
                    {
                        Constant.LevelText = "Lv. ";
                    }
                }
            }
        }

        void SetDifficulty()
        {
            //空格概率初始化
            var sum = emptyBlock1 + emptyBlock2 + emptyBlock3;
            Constant.EmptyBlockRate[0] = new[] {0, Tools.ChinaRound(emptyBlock1 * 1.0f / sum * 100)};
            Constant.EmptyBlockRate[1] = new[] {Tools.ChinaRound(emptyBlock1 * 1.0f / sum * 100), Tools.ChinaRound((emptyBlock1 + emptyBlock2) * 1.0f / sum * 100)};
            Constant.EmptyBlockRate[2] = new[] {Tools.ChinaRound((emptyBlock1 + emptyBlock2) * 1.0f / sum * 100), 100};
        }

        void UpdateDragDelta()
        {
            var designWidth = (int) GetComponent<CanvasScaler>().referenceResolution.x;
            var designHeight = (int) GetComponent<CanvasScaler>().referenceResolution.y;

            //修改分辨率
            var canvasScaler = GetComponent<CanvasScaler>();
            if (Screen.width * 1f / Screen.height > designWidth * 1f / designHeight)
            {
                DebugEx.Log("修改适配方案，改为高适配！");
                canvasScaler.matchWidthOrHeight = 1;
            }
            else
            {
                var design = designHeight * 1f / designWidth;
                var fact = Screen.height * 1f / Screen.width;
                if (fact > (design + 0.1f))
                {
                    Constant.IsDeviceSoHeight = true;
                }
            }
            
            //宽适配
            var matchMode = canvasScaler.matchWidthOrHeight;
            if (matchMode < 0.5f)
            {
                Constant.DragDelta = designWidth * 1.0f / Screen.width;
            }
            //高适配
            if (matchMode >= 0.5f)
            {
                Constant.DragDelta = designHeight * 1.0f / Screen.height;
            }

            Constant.ScreenWidth = Screen.width * Constant.DragDelta;
            Constant.ScreenHeight = Screen.height * Constant.DragDelta;

#if !UNITY_EDITOR && UNITY_IOS
            var splashImg = GameObject.Find("SplashImg");
            if (splashImg != null)
            {
                splashImg.GetComponent<Image>().color = Color.white;
            }
#endif
        }

        void UpdateServerData()
        {
#if (UNITY_IOS || UNITY_ANDROID) && !UNITY_EDITOR
            var remoteConfig = JObject.Parse(PlatformBridge.getConfigMap("Application.gameConfig"));
            DebugEx.Log("remoteConfig");
            DebugEx.Log(remoteConfig);
            if (remoteConfig != null) {
                //评星配置
                if (remoteConfig.ContainsKey("rateRound"))
                {
                    Constant.RateRound = (int) remoteConfig["rateRound"];
                }
    
                //新手引导版本配置
                Constant.GuideVersion = remoteConfig.ContainsKey("guideVersion")
                    ? (string) remoteConfig["guideVersion"]
                    : Constant.GuideVersion;
                
                //b421功能配置
                Constant.B421Switch = remoteConfig.ContainsKey("b421Switch")
                    ? (bool) remoteConfig["b421Switch"]
                    : Constant.B421Switch;
                Constant.B421NotAddBlockStep = remoteConfig.ContainsKey("b421NotAddBlockStep")
                    ? (int) remoteConfig["b421NotAddBlockStep"]
                    : Constant.B421NotAddBlockStep;
                
                //b43221功能配置
                Constant.B43221Switch = remoteConfig.ContainsKey("b43221Switch")
                    ? (bool) remoteConfig["b43221Switch"]
                    : Constant.B43221Switch;
                Constant.B43221BlockPro = remoteConfig.ContainsKey("b43221BlockPro")
                    ? (string) remoteConfig["b43221BlockPro"]
                    : Constant.B43221BlockPro;
                Constant.B43221RemoveSlideNumber = remoteConfig.ContainsKey("b43221RemoveSlideNumber")
                    ? (int) remoteConfig["b43221RemoveSlideNumber"]
                    : Constant.B43221RemoveSlideNumber;
                Constant.B43221NotAddBlockStep = remoteConfig.ContainsKey("b43221NotAddBlockStep")
                    ? (int) remoteConfig["b43221NotAddBlockStep"]
                    : Constant.B43221NotAddBlockStep;

                //b43221And1Hang功能配置
                Constant.B43221And1HangSwitch = remoteConfig.ContainsKey("b43221And1HangSwitch")
                    ? (bool) remoteConfig["b43221And1HangSwitch"]
                    : Constant.B43221And1HangSwitch;
                
                //激励视频开关
                Constant.SecondChanceEnabled = remoteConfig.ContainsKey("secondChanceEnabled")
                    ? (bool) remoteConfig["secondChanceEnabled"]
                    : Constant.SecondChanceEnabled;

                //成就开关
                Constant.AchievementSwitch = remoteConfig.ContainsKey("achievementSwitch")
                    ? (bool) remoteConfig["achievementSwitch"]
                    : Constant.AchievementSwitch;
                
                //分数评星
                Constant.ScoreToStar = remoteConfig.ContainsKey("scoreToStar")
                    ? (string) remoteConfig["scoreToStar"]
                    : Constant.ScoreToStar;
                
                //二次机会出现分数条件
                Constant.SecondChanceScore = remoteConfig.ContainsKey("secondChanceScore")
                    ? (int) remoteConfig["secondChanceScore"]
                    : Constant.SecondChanceScore;
                
                //道具开关
                Constant.PropSwitch = remoteConfig.ContainsKey("propSwitch")
                    ? (bool) remoteConfig["propSwitch"]
                    : Constant.PropSwitch;
                
                //单局道具使用次数
                Constant.PropUsedCount = remoteConfig.ContainsKey("propUsedCount")
                    ? (int) remoteConfig["propUsedCount"]
                    : Constant.PropUsedCount;

                //彩色开关
                Constant.SpecialRainbowSwitch = remoteConfig.ContainsKey("specialRainbowSwitch")
                    ? (bool) remoteConfig["specialRainbowSwitch"]
                    : Constant.SpecialRainbowSwitch;

                //青铜开关
                Constant.SpecialBronzeSwitch = remoteConfig.ContainsKey("specialBronzeSwitch")
                    ? (bool) remoteConfig["specialBronzeSwitch"]
                    : Constant.SpecialBronzeSwitch;

                //金色开关
                Constant.SpecialGoldSwitch = remoteConfig.ContainsKey("specialGoldSwitch")
                    ? (bool) remoteConfig["specialGoldSwitch"]
                    : Constant.SpecialGoldSwitch;
                
                //青铜随机概率
                Constant.SpecialBronzeRandom = remoteConfig.ContainsKey("specialBronzeRandom")
                    ? (string) remoteConfig["specialBronzeRandom"]
                    : Constant.SpecialBronzeRandom;
                
                //金随机概率
                Constant.SpecialGoldRandom = remoteConfig.ContainsKey("specialGoldRandom")
                    ? (string) remoteConfig["specialGoldRandom"]
                    : Constant.SpecialGoldRandom;

                //金色功能权重
                Constant.SpecialGoldEffWeight = remoteConfig.ContainsKey("specialGoldEffWeight")
                    ? (string) remoteConfig["specialGoldEffWeight"]
                    : Constant.SpecialGoldEffWeight;
                
                //金色块是否展示插屏广告
                Constant.SpecialGoldAdInterstitialSwitch = remoteConfig.ContainsKey("specialGoldAdInterstitialSwitch")
                    ? (bool) remoteConfig["specialGoldAdInterstitialSwitch"]
                    : Constant.SpecialGoldAdInterstitialSwitch;

                //消除金色块后，如果没有激励视频就用插屏
                Constant.SpecialGoldAdRvAndInterSwitch = remoteConfig.ContainsKey("specialGoldAdRvAndInterSwitch")
                    ? (bool) remoteConfig["specialGoldAdRvAndInterSwitch"]
                    : Constant.SpecialGoldAdRvAndInterSwitch;
                
                //金色块倒计时开关
                Constant.SpecialGoldCountDownSwitch = remoteConfig.ContainsKey("specialGoldCountDownSwitch")
                    ? (bool) remoteConfig["specialGoldCountDownSwitch"]
                    : Constant.SpecialGoldCountDownSwitch;

                //金色块倒计时时间
                Constant.SpecialGoldCountDownNum = remoteConfig.ContainsKey("specialGoldCountDownNum")
                    ? (int) remoteConfig["specialGoldCountDownNum"]
                    : Constant.SpecialGoldCountDownNum;
                
                //游戏难度
                Constant.DifficultyLevel = remoteConfig.ContainsKey("difficultyLevel")
                    ? (int) remoteConfig["difficultyLevel"]
                    : Constant.DifficultyLevel;

                //震动开关
                Constant.VibratorSwitch = remoteConfig.ContainsKey("vibratorSwitch")
                    ? (bool) remoteConfig["vibratorSwitch"]
                    : Constant.VibratorSwitch;

                //震动时间
                Constant.VibratorTime = remoteConfig.ContainsKey("vibratorTime")
                    ? (int) remoteConfig["vibratorTime"]
                    : Constant.VibratorTime;
                
                //震动幅度
                Constant.VibratorAmplitude = remoteConfig.ContainsKey("vibratorAmplitude")
                    ? (int) remoteConfig["vibratorAmplitude"]
                    : Constant.VibratorAmplitude;

                //石头块功能开关
                Constant.StoneSwitch = remoteConfig.ContainsKey("stoneSwitch")
                    ? (bool) remoteConfig["stoneSwitch"]
                    : Constant.StoneSwitch;
                
                //基础分数倍数
                Constant.StoneScoreTimes = remoteConfig.ContainsKey("stoneScoreTimes")
                    ? (int) remoteConfig["stoneScoreTimes"]
                    : Constant.StoneScoreTimes;
                
                //石头开始出现的分数
                Constant.StoneScoreGenerate = remoteConfig.ContainsKey("stoneScoreGenerate")
                    ? (int) remoteConfig["stoneScoreGenerate"]
                    : Constant.StoneScoreGenerate;
                
                //几行出一个石头
                Constant.StoneGenerateHang = remoteConfig.ContainsKey("stoneGenerateHang")
                    ? (int) remoteConfig["stoneGenerateHang"]
                    : Constant.StoneGenerateHang;
                
                //场上最多的石头数
                Constant.StoneMaxCount = remoteConfig.ContainsKey("stoneMaxCount")
                    ? (int) remoteConfig["stoneMaxCount"]
                    : Constant.StoneMaxCount;
                
                //冰块开关
                Constant.IceBlockSwitch = remoteConfig.ContainsKey("iceBlockSwitch")
                    ? (bool) remoteConfig["iceBlockSwitch"]
                    : Constant.IceBlockSwitch;
                
                //level文案改为stage开关
                Constant.LevelToStageSwitch = remoteConfig.ContainsKey("levelToStageSwitch")
                    ? (bool) remoteConfig["levelToStageSwitch"]
                    : Constant.LevelToStageSwitch;
                
                //levelReward开关
                Constant.LevelRewardSwitch = remoteConfig.ContainsKey("levelRewardSwitch")
                    ? (bool) remoteConfig["levelRewardSwitch"]
                    : Constant.LevelRewardSwitch;
                
                //升级时的其他特效
                Constant.LevelUpOtherEffSwitch = remoteConfig.ContainsKey("levelUpOtherEffSwitch")
                    ? (bool) remoteConfig["levelUpOtherEffSwitch"]
                    : Constant.LevelUpOtherEffSwitch;
                
                //升级特效版本
                Constant.LevelUpEffVersion = remoteConfig.ContainsKey("levelUpEffVersion")
                    ? (string) remoteConfig["levelUpEffVersion"]
                    : Constant.LevelUpEffVersion;
                
                //levelProgress显示版本
                Constant.LevelProgressVersion = remoteConfig.ContainsKey("levelProgressVersion")
                    ? (string) remoteConfig["levelProgressVersion"]
                    : Constant.LevelProgressVersion;
                
                //金色块消除需要时间
                Constant.SpecialGoldClearTime = remoteConfig.ContainsKey("specialGoldClearTime")
                    ? (float) remoteConfig["specialGoldClearTime"]
                    : Constant.SpecialGoldClearTime;

                //金色块消除时功能的出发是否需要展示广告
                Constant.SpecialGoldAdClear = remoteConfig.ContainsKey("specialGoldAdClear")
                    ? (bool) remoteConfig["specialGoldAdClear"]
                    : Constant.SpecialGoldAdClear;

                //结算界面是否展示去广告按钮
                Constant.ShowRemoveAd = remoteConfig.ContainsKey("showRemoveAd")
                    ? (bool) remoteConfig["showRemoveAd"]
                    : Constant.ShowRemoveAd;

                //金色块弹窗里展示动态效果
                Constant.ShowSpecialGoldDialogAnim = remoteConfig.ContainsKey("showSpecialGoldDialogAnim")
                    ? (bool) remoteConfig["showSpecialGoldDialogAnim"]
                    : Constant.ShowSpecialGoldDialogAnim;
            }
#endif
        }
    }
}
