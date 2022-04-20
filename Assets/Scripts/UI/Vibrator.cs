using System.Collections;
using System.Collections.Generic;
using Manager;
using Platform;
using UnityEngine;
using UnityEngine.UI;

public class Vibrator : MonoBehaviour
{
    private void StartVibrator(string pattern, int repeat = -1)
    {
        /*if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on")
        {
            PlatformBridge.vibratorStart(pattern, repeat + "");
        }*/
    }

    private void StopVibrator()
    {
        /*if (ManagerLocalData.GetStringData(ManagerLocalData.VIBRATE_SWITCH) == "on")
        {
            PlatformBridge.vibratorStop();
        }*/
    }
}
