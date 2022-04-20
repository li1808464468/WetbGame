using System;
using System.Collections;
using System.IO;
using System.Threading.Tasks;
using BFF;
//using Blowfire.Utility.Runtime.Message;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Other;
using Platform;
using UnityEngine;
using UnityEngine.AddressableAssets;
using UnityEngine.SceneManagement;

public class LoadingScene : MonoBehaviour
{
    // Start is called before the first frame update
    void Awake()
    {

        DebugEx.Log("LoadTime", "LoadingAwake", PlatformBridge.GetEnterGameTime() / 1000f);
        // PlatformBridge.InitAppLovinAdapter();
        EnterGame();
        // AsyncThread.SubmitData2ES("App_Open");

//        GlobalMessage.Instance.AddListener(UILoadingView.DOWNLOAD_OVER, EnterGame);
//        RecTest();
    }

    private void EnterGame()
    {
        var sceneVersion = "3";
//#if (UNITY_IOS || UNITY_ANDROID) && !UNITY_EDITOR
//        var remoteConfig = JObject.Parse(PlatformBridge.getConfigMap("Application.gameConfig"));
//        DebugEx.Log("remoteConfig");
//        DebugEx.Log(remoteConfig);
//        if (remoteConfig != null)
//        {
//            if (remoteConfig.ContainsKey("sceneVersion"))
//            {
//                sceneVersion = (string) remoteConfig["sceneVersion"];
//            }
//        }
//#endif
        SceneManager.LoadScene("GameScene" + sceneVersion);
        
        
    }

//    private async void RecTest()
//    {
//        var path = Application.persistentDataPath;
//        var finalPath = Path.Combine(path, "recTest.dat");
//        
//        await Task.Delay(2000);
//        var recTest = new FileRecordQueue(finalPath, 2048 * 10, 100);
//        await Task.Delay(100);
//        DebugEx.Log("LoadingSceneRecordTest", "打开文件", "当前条数", recTest.RecordCount());
//
//        var data1 = new Hashtable();
//        data1.Add("record1", 1);
//        
//        recTest.WriteRecord(JsonConvert.SerializeObject(data1));
//        await Task.Delay(100);
//        DebugEx.Log("LoadingSceneRecordTest", "完成写入第1条", recTest.RecordCount());
//        
//        var data2 = new Hashtable();
//        data2.Add("record2", 2);
//        
//        recTest.WriteRecord(JsonConvert.SerializeObject(data2));
//        await Task.Delay(100);
//        DebugEx.Log("LoadingSceneRecordTest", "完成写入第2条", recTest.RecordCount());
//    }

//    private async void TestHttpClient()
//    {
//        var httpClient = new HttpClient();
//        var url = ESDataConfig.GetESUri();
//        for (var j = 0; j < 10; j++)
//        {
////            var httpClient = new HttpClient();
//            var postData = GetData(j);
//            var encodedItems = postData.Select(i => WebUtility.UrlEncode(i.Key) + "=" + WebUtility.UrlEncode(i.Value));
//            var content = new StringContent(string.Join("&", encodedItems), null, "application/x-www-form-urlencoded");
//            PostData(httpClient, content, url, j);
//        }
//    }
//
//    private async void PostData(HttpClient httpClient, StringContent content, string url, int j)
//    {
//        DebugEx.Log(j, "发送");
//        var response = await httpClient.PostAsync(url, content);
//        response.EnsureSuccessStatusCode();//用来抛异常的
//        var responseBody = await response.Content.ReadAsStringAsync();
//        Helper.Log(responseBody);
//            
//        if (response.IsSuccessStatusCode)
//        {
//            var responseObj = JObject.Parse(responseBody);
//            if ((int) responseObj["meta"]["code"] == 200)
//            {
//                DebugEx.Log("上报成功！", j);
//            }
//            else
//            {
//                DebugEx.Log("上报失败！", responseObj["meta"]["code"], j);
//            }
//        }
//        else
//        {
//            DebugEx.Log("发送失败", response.StatusCode, j);
//        }
//    }
//
//    private Dictionary<string, string> GetData(int i)
//    {
//        //事件名称，本局游戏结束时发送
//        var testAction = "GameOver";
// 
////本局游戏相关数据
//        var testGameData = new Dictionary<string, object>
//        {
//            {"Score", 100},
//            {"BestScore", 1000},
//            {"Gold", i}
//        };
// 
////设备信息
//        var testDevice = new Dictionary<string, object>
//        {
//            {"wifi", true},
//            {"language", "en"},
//            {"time-zone", "+5"},
//            {"timestamp", new DateTimeOffset(DateTime.UtcNow).ToUnixTimeMilliseconds()},
//            {"country", "IN"},
//            {"os_version", 25}
//        };
// 
////用户信息
//        var testUser = new Dictionary<string, object>
//        {
//            {"user_id", "0123456789"},
//            {"day", 1}
//        };
// 
////游戏版本信息
//        var testApp = new Dictionary<string, object>
//        {
//            {"network", "connected"},
//            {"first_version", "1.1.1"},
//            {"version", "1.1.1"},
//            {"name", "testAppName"},
//            {"time", new DateTimeOffset(DateTime.UtcNow).ToUnixTimeMilliseconds()},
//            {"first_code", 3037},
//            {"code", 3037},
//            {"type", "android"}
//        };
// 
////最终信息
//        var data = new Dictionary<string, object>
//        {
//            {"testAction", testAction},
//            {"testGameData", testGameData},
//            {"testDevice", testDevice},
//            {"testApp", testApp},
//            {"testUser", testUser},
//        };
//
//        var finalData = new Dictionary<string, object>
//        {
//            {"data", new List<object>{data}}
//        };
//        
//        var contentStr = JsonConvert.SerializeObject(finalData);
//        var postData = new Dictionary<string, string>();
//        postData.Add("sig_kv", ESDataConfig.GetESSigKey());
//        postData.Add("signature", ESDataHelper.GetESSignature(contentStr, ESDataConfig.GetESHashKey()));
//        postData.Add("content", contentStr);
//        postData.Add("cten", "p");
//        
//        return postData;
//    }
}
