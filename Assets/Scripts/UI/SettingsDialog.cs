using System.Collections;
using Manager;
using Models;
using Other;
using Platform;
using UnityEngine;

namespace UI
{
    public class SettingsDialog : MonoBehaviour
    {
        public GameObject btnMusicOpen;
        public GameObject btnMusicClose;
        public GameObject btnSoundOpen;
        public GameObject btnSoundClose;
        public GameObject btnVibrateOpen;
        public GameObject btnVibrateClose;
        public GameObject vibratorLine;
        public GameObject btnReplay;
        public GameObject btnPrivacy;
        public GameObject bG;
        
        private void OnDestroy()
        {
            var topMask = GameObject.Find("topMask");
            if (topMask != null)
            {
                topMask.GetComponent<TopMask>().ShowLight();
            }
        }
        
        // Start is called before the first frame update
        void Start()
        {
            GameObject.Find("topMask").GetComponent<TopMask>().HideLight();
            
            string temSwitchFlag =PlatformBridge.getESIDandSwitchFlag();
            if (temSwitchFlag.Split('|')[1] == "off")
            {
                ManagerLocalData.SetStringData(ManagerLocalData.VIBRATE_SWITCH,"dontShow");
                Debug.Log("SettingDialog" + ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH));
            }
            else if(ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "dontShow")
            {
                ManagerLocalData.SetStringData(ManagerLocalData.VIBRATE_SWITCH,"on");
                Debug.Log("SettingDialog" + ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH));
            }

            if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "dontShow")
            {
                Debug.Log("SettingDialog1 " + ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH));
                vibratorLine.SetActive(false);
                Vector3 tem = new Vector3();
                tem = btnReplay.transform.localPosition;
                tem.y = -41.2f;
                btnReplay.transform.localPosition = tem;
                tem = btnPrivacy.transform.localPosition;
                tem.y = -203.2f;
                btnPrivacy.transform.localPosition = tem;
                bG.GetComponent<RectTransform>().sizeDelta = new Vector2(890, 1000);
            }
            else
            {
                Debug.Log("SettingDialog1 " + ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH));
                vibratorLine.SetActive(true);
                Vector3 tem = new Vector3();
                tem = btnReplay.transform.localPosition;
                tem.y = -162.2f;
                btnReplay.transform.localPosition = tem;
                tem = btnPrivacy.transform.localPosition;
                tem.y = -307.9f;
                btnPrivacy.transform.localPosition = tem;
                bG.GetComponent<RectTransform>().sizeDelta = new Vector2(890, 1099);
            }
            //震动默认开关
            if (!ManagerLocalData.HaveData(ManagerLocalData.VIBRATE_SWITCH))
            {
                ManagerLocalData.SetStringData(ManagerLocalData.VIBRATE_SWITCH, "on");
            }
            ResetBtnStatus();
        }

        private void ResetBtnStatus()
        {
            btnMusicOpen.SetActive(!ManagerAudio.GetMusicEnabled());
            btnMusicClose.SetActive(ManagerAudio.GetMusicEnabled());
            
            btnSoundOpen.SetActive(!ManagerAudio.GetSoundEnabled());
            btnSoundClose.SetActive(ManagerAudio.GetSoundEnabled());
            
            btnVibrateOpen.SetActive(ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "off");
            btnVibrateClose.SetActive(ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on");
        }
        
        /// <summary>
        /// MenuGroup 中也有一个方法
        /// </summary>
        private void sendReplayClickAF()
        {
            if (Constant.AFData.setting_replay_click)
            {
                return;
            }
            
            Statistics.SendAF("setting_replay_click");
            Constant.AFData.setting_replay_click = true;
            ManagerLocalData.SetTableData(ManagerLocalData.AF, Constant.AFData);


        }
        

        public void OnBtnClk(string param)
        {
            DebugEx.Log(param);
            switch (param)
            {
                case "music_open":
                    ManagerAudio.SetMusicOn();
                    ResetBtnStatus();
                    Statistics.Send("click_music", new Hashtable()
                    {
                        {"status", 0}
                    });
                    break;
                case "music_close":
                    ManagerAudio.SetMusicOff();
                    ResetBtnStatus();
                    Statistics.Send("click_music", new Hashtable()
                    {
                        {"status", 1}
                    });
                    break;
                case "sound_open":
                    ManagerAudio.SetSoundOn();
                    ResetBtnStatus();
                    Statistics.Send("click_sound", new Hashtable()
                    {
                        {"status", 0}
                    });
                    break;
                case "sound_close":
                    ManagerAudio.SetSoundOff();
                    ResetBtnStatus();
                    Statistics.Send("click_sound", new Hashtable()
                    {
                        {"status", 1}
                    });
                    break;
                case "vibrate_open":
                    ManagerLocalData.SetStringData(ManagerLocalData.VIBRATE_SWITCH, "on");
                    ResetBtnStatus();
                    break;
                case "vibrate_close":
                    ManagerLocalData.SetStringData(ManagerLocalData.VIBRATE_SWITCH, "off");
                    ResetBtnStatus();
                    break;
                case "restart":
                    if (!Player.UserCanMove) return;
                    Constant.GamePlayScript.RestartGame(true);
                    
                    Statistics.Send("game_start", new Hashtable()
                    {
                        {"gameStatus", 2}
                    });
                    sendReplayClickAF();
                    OnBtnClk("close");
                    break;
                case "privacy":
                    PlatformBridge.ShowPrivacy();
                    break;
                case "close":
                    ManagerDialog.DestroyDialog("SettingsDialog");
                    break;
            }
        }
    }
}
