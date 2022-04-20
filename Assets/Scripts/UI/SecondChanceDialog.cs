using System.Collections;
using DG.Tweening;
using Manager;
using Models;
using Other;
using Spine;
using Spine.Unity;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

namespace UI
{
    public class SecondChanceDialog : MonoBehaviour
    {
        public GameObject countDown;
        public GameObject content;
        public GameObject btnNoThx;
        public Sprite[] countDownSpriteFrames;
        public TextMeshProUGUI curScore;

//        public GameObject countDownShadow;
//        public Sprite[] countDownShadowSpriteFrames;
//        public GameObject countDownBgLight;
        
        private int _remainTime = 5;

        private void OnDestroy()
        {
            if (Constant.SceneVersion == "3")
            {
                var topMask = GameObject.Find("topMask");
                if (topMask != null)
                {
                    topMask.GetComponent<TopMask>().ShowLight();
                }
            }
        }

        private void PlayCountDownSound()
        {
            ManagerAudio.PlaySound("countDown"); 
            StartCoroutine(Delay.Run(() =>
                {
                    PlayCountDownSound();
                }, 80 / 60f));
        }

        // Start is called before the first frame update
        void Start()
        {
            curScore.text = Player.GetCurScore().ToString();
            Constant.GameStatusData.continueShow = true;
            
            if (Constant.SceneVersion == "3")
            {
                GameObject.Find("topMask").GetComponent<TopMask>().HideLight();
                
                var scoreGroup = curScore.transform.parent;
                var oriPos = scoreGroup.transform.localPosition;
                scoreGroup.transform.localPosition =
                    new Vector2(Player.GetCurScore().ToString().Length * -129 / 6f, oriPos.y);

//                countDown.SetActive(false);
//                countDownShadow.SetActive(false);
//                countDownBgLight.SetActive(false);
                
                StartCoroutine(Delay.Run(() =>
                {
                    var countDownObj = content.transform.Find("countDownShadow2");
                    countDownObj.gameObject.SetActive(true);
                    countDownObj.GetComponent<SkeletonGraphic>().AnimationState.Complete += delegate(TrackEntry entry)
                    {
                        if (entry.ToString() == "animation_2")
                        {
                            OnBtnClk("noThx");
                        }
                    };
                    countDownObj.GetComponent<SkeletonGraphic>().AnimationState.SetAnimation(0, "animation_2", false);
                    StartCoroutine(Delay.Run(() => { PlayCountDownSound(); }, 25 / 60f));
                    
//                    StartCountDown();
                }, 1.2f));
            }
            else
            {
                btnNoThx.GetComponent<CanvasGroup>().alpha = 0;
                countDown.transform.localScale = Vector2.zero;
                var localPosition = content.transform.localPosition;
                var originalY = localPosition.y;
                localPosition = new Vector2(localPosition.x,
                    localPosition.y + Constant.ScreenHeight);
                content.transform.localPosition = localPosition;
                content.transform.DOLocalMoveY(originalY, 0.5f).SetEase(Ease.OutBack).OnComplete(() =>
                {
                    btnNoThx.GetComponent<CanvasGroup>().DOFade(1, 1);
                    StartCountDown();
                });
            }
        }

        void StartCountDown()
        {
            countDown.SetActive(true);
            
            if (Constant.SceneVersion == "3")
            {
//                if (countDownShadow != null)
//                {
//                    countDownShadow.SetActive(true);
//                }
//
//                if (countDownBgLight != null)
//                {
//                    countDownBgLight.SetActive(true);
//                }
            }

            if (_remainTime <= 0)
            {
                if (Constant.SceneVersion == "3")
                {
                    if (_remainTime < 0)
                    {
                        OnBtnClk("noThx");
                        return;
                    }
                }
                else
                {
                    OnBtnClk("noThx");
                    return;
                }
            }
            
            countDown.GetComponent<Image>().sprite = countDownSpriteFrames[Constant.SceneVersion == "3" ? _remainTime : _remainTime - 1];

            if (Constant.SceneVersion == "3")
            {
//                if (countDownShadow != null)
//                {
//                    countDownShadow.GetComponent<Image>().sprite = countDownShadowSpriteFrames[_remainTime];
//                }
                
                GetComponent<Animator>().Play("SecondChanceCountDown", 0, 0);

                StartCoroutine(Delay.Run(() =>
                {
                    ManagerAudio.PlaySound("countDown");
                }, 0.2f));
                
                StartCoroutine(Delay.Run(() =>
                {
                    --_remainTime;
                    StartCountDown();
                }, 1.5f));
            }
            else
            {
                countDown.transform.DOScale(new Vector2(1, 1), 0.2f).SetEase(Ease.OutBack).OnComplete(() =>
                {
                    --_remainTime;
                    countDown.transform.DOScale(new Vector2(0, 0), 0.2f).SetDelay(0.6f).OnComplete(StartCountDown);
                
                    ManagerAudio.PlaySound("countDown");
                });
            }
        }

        public void OnBtnClk(string btnType)
        {
            DebugEx.Log(btnType);
            switch (btnType)
            {
                case "playOn":
                    Constant.GameStatusData.continueClk = true;
                    Constant.GameStatusData.videoShow = true;
                    Constant.RewardVideoType = "secondChance";

#if UNITY_EDITOR
                    Player.UseSecondChance();
                    var result = new Hashtable();
                    result.Add("success", true);
                    Constant.GamePlayScript.SecondChanceResult(result);
//                    Constant.GamePlayScript.B43221And1HangResult(result);
                    
                    ManagerDialog.DestroyDialog("SecondChanceDialog");
                    return;
#endif
                    
                    if (!Constant.AFData.video_show)
                    {
                        Statistics.SendAF("video_show");
                        Constant.AFData.video_show = true;
                        ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);
                    }

                    if (Constant.B43221And1HangSwitch)
                    {
                        ManagerAd.PlayRewardAd((int)ManagerAd.RewardVideoPlayType.B43221And1Hang);
                    }
                    else
                    {
                        ManagerAd.PlayRewardAd();
                    }
                    
                    ManagerDialog.DestroyDialog("SecondChanceDialog");
                    

                    break;
                case "noThx":
                    ManagerDialog.DestroyDialog("SecondChanceDialog");
                    Constant.GamePlayScript.ShowGameOverEff();
                    break;
            }
        }
    }
}
