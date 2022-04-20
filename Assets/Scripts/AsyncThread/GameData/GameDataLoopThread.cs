using System;
using BFF.Lib;

public class GameDataLoopThread : BFFLoopThread
{
    private string TAG = "GameDataLoopThread----------";
    private static GameDataLoopThread _instance;
    private static readonly object LockInstance = new object();
        
    public static GameDataLoopThread Instance
    {
        get
        {
            lock (LockInstance)
            {
                if (_instance == null)
                {
                    _instance = new GameDataLoopThread();
                    _instance.Start("GameDataLoopThread");
                    Helper.Log("GameDataLoopThread", "start");
                }
                return _instance;
            }
        }
    }

    public void AddTask(Action act)
    {
        Helper.Log(TAG, "AddTask");
        Handler.Post(act);
    }
}
