//
//  BFSessionManager.h
//  BFAppFramework
//
//

#import <Foundation/Foundation.h>

/**
 *  Session 开始和结束通知，app 不要使用 UIApplicationDidEnterBackgroundNotification 和 UIApplicationWillEnterForegroundNotification 通知，
 *  统一改用下面两个通知
 */
extern NSString * const kBFNotificationName_SessionDidStart;
extern NSString * const kBFNotificationName_SessionDidEnd;


@interface BFSessionManager : NSObject
@property (nonatomic, readonly) NSInteger currentSessionID; // 相当于 1.0 中的 使用次数
@property (nonatomic, readonly) NSDate *firstSessionStartTime;
@property (nonatomic, readonly) NSDate *lastSessionEndTime;
@property (nonatomic, readonly) NSTimeInterval totalUsageSeconds; // 使用时常
@property (nonatomic, readonly) BOOL isFirstSessionInCurrentLaunch;

+ (instancetype)sharedInstance;

/**
 *  禁用 session 通知，默认为开启，如果想彻底禁用 session 通知请在 [super didFinishLaunching...] 之前调用
 *  有特殊需求的才需要此接口
 */
+ (void)disableSessionNotification;

/**
 *  开启 session 通知，如果当前 session 为 started 则发一个 session start 的通知
 *  此函数一定要在 [super didFinishLaunching...] 之后调用
 */
- (void)enableAndPostSessionNotificationIfNeeded;

- (void)startSession;
- (void)endSession;

@end
