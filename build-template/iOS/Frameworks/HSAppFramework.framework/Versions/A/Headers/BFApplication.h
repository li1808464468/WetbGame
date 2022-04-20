//
//  BFApplication.h
//  BFAppFramework
//
//

#import <UIKit/UIKit.h>


typedef NS_ENUM(NSInteger, BFLaunchInfoType)
{
    kBFLaunchInfoType_Unknown,
    kBFLaunchInfoType_First,
    kBFLaunchInfoType_Last,
};

/**
 *  此类所有属性只读
 */
@interface BFLaunchInfo : NSObject <NSCopying>
@property (nonatomic, assign) NSInteger launchID; //打开程序时的 launch ID，首次打开为 1，以后每次全新打开 app 此数值 +1
@property (nonatomic, copy) NSString *appVersion;
@property (nonatomic, copy) NSString *osVersion;

/**
 *  此函数非 app 调用，为库自身调用，请勿使用
 */
+ (void)upgrade;

@end

@interface BFApplication : UIResponder <UIApplicationDelegate>
@property (nonatomic, readonly) BFLaunchInfo *firstLaunchInfo; // 首次打开程序时的版本信息
@property (nonatomic, readonly) BFLaunchInfo *lastLaunchInfo; // 上次打开程序时的版本信息
@property (nonatomic, readonly) BFLaunchInfo *currentLaunchInfo; // 当前程序运行时的版本信息

+ (instancetype)sharedInstance;

/**
 *  debug 配置一下一定要设置为 YES，否则 token 会被发往生产环境，开启此项同时会显示 Flurry Log
 *
 *  @param enabled 设置为 debug 模式
 */
+ (void)setDebugEnabled:(BOOL)enabled;

/**
 *  如果 app 需要在 session 开始先做事情，请将代码写在此函数内，函数内代码会在 Session 开始前执行
 */
- (void)onApplicationStart;

/**
 *  yaml 配置文件文件名，默认为 config.ya，app 根据 debug 和 release 两种不同配置根据宏来返回不同文件名即可
 *
 *  @return 文件名称 config.ya
 */
- (NSString *)configFileName;

/**
 *  是否开启了 debug 模式
 *
 *  @return 是否开启了 debug 模式
 */
+ (BOOL)isDebugEnabled;

/**
 *  用于Appdelegate不继承HSApplication时，HSApplication相关初始化
 *  @param fileName yaml 配置文件文件名
 *  @param appID    apppID
 */
+(void)initializeWithConfigFileName:(NSString *)fileName appID:(NSUInteger)appID;

@end


/**

 App 必须覆写的函数：

 - (NSString *)configFileName

 App 可选覆写的函数：

 - (void)onApplicationStart
 
 App 覆写 如下函数时必须要掉 super 对应函数，否则 session 会出错：
 
 - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
 
 - (void)applicationWillEnterForeground:(UIApplication *)application
 
 - (void)applicationDidEnterBackground:(UIApplication *)application
 
 
 - (void)applicationDidBecomeActive:(UIApplication *)application
 
 - (void)applicationWillResignActive:(UIApplication *)application
 
 - (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
 
 - (void)applicationWillTerminate:(UIApplication *)application
 
 - (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
 
 
 - (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
 
 
 - (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
 
 - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
 
 - (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification
 
 - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult result))completionHandler
 */
