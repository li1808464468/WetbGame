//
//  BFAlertManager.h
//  BFAppFramework
//
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

extern NSString * const kBFNotificationName_RateAlertClicked;
extern NSString * const kBFNotificationUserInfoKey_EngagementID;

extern NSString * const kBFAlertName_MessageAlert;
extern NSString * const kBFAlertName_FirstLaunchAlert;
extern NSString * const kBFAlertName_UpdateAlert;
extern NSString * const kBFAlertName_PromoteAlert;
extern NSString * const kBFAlertName_RateAlert;
extern NSString * const kBFAlertName_CustomAlert;

typedef NS_OPTIONS(NSUInteger, AlertShowOption)
{
    kAlertShowOption_None = 0,
    kAlertShowOption_MessageAlert = 1 << 0,
    kAlertShowOption_FirstLaunchAlert = 1 << 1,
    kAlertShowOption_UpdateAlert = 1 << 2,
    kAlertShowOption_PromoteAlert = 1 << 3,
    kAlertShowOption_RateAlert = 1 << 4,
    kAlertShowOption_All = kAlertShowOption_MessageAlert | kAlertShowOption_FirstLaunchAlert | kAlertShowOption_UpdateAlert | kAlertShowOption_PromoteAlert | kAlertShowOption_RateAlert
};

typedef NS_OPTIONS(NSUInteger, AlertsDelayOption)
{
    kAlertDelayOption_None = 0,
    kAlertDelayOption_UpdateAlert = 1 << 0,
    kAlertDelayOption_RateAlert = 1 << 1,
    kAlertDelayOption_All = kAlertDelayOption_UpdateAlert | kAlertDelayOption_RateAlert
};

typedef NS_ENUM(NSInteger, CustomAlertPosition)
{
    kCustomAlertPosition_BeforeRate,
    kCustomAlertPosition_AfterRate
};


typedef NS_ENUM(NSInteger, ReviewMethod)
{
    kReviewMethod_JumpOut,
    kReviewMethod_StoreKit,
};


@interface BFAlertManager : NSObject

// 如果需要显示 Custom Alert 设置此 Block，将显示的内容放在 Block 内并根据显示时机返回 YES 即可，如果返回了 YES，在此 Alert 级别之后的 Alert 不会再显示
@property (nonatomic, copy) BOOL(^showCustomAlertIfNeedInPostion)(CustomAlertPosition position);


/**
 *  如果需要使用自定义的 UI 来显示对应的 Alert，设置此 Block，在 Block 内根据内容构建 UI 显示，并返回 YES 即可。
 *
 *  @param alertName           Alert 的名字，比如 kHSAlertName_RateAlert
 *  @param title               Custom alert title 的文字
 *  @param body                Custom alert body 的文字
 *  @param actionTexts         NSString array, 按钮上的文字
 *  @param ^actionClickHandler 库提供的按钮点击 Handler
 *
 *  @return BOOL, YES 表示 Custom Alert 已经显示完成，此 Alert 级别以后的 Alert 不会再显示
 */
@property (nonatomic, copy) BOOL (^showCustomUIForAlert)(NSString *alertName, NSString *title, NSString *body, NSArray *actionTexts, void(^actionClickHandler)(NSInteger actionIndex));


@property (nonatomic, assign) AlertsDelayOption alertDelayOptions; // 如果 app 想延迟某种 Alert 的显示，可以通过将相应的比特位设置为 1
@property (nonatomic, readonly) ReviewMethod reviewMethod;
@property (nonatomic, readonly) NSString* rateLink;
@property (nonatomic, assign) AlertShowOption alertShowOptions; // 如果 app 不想显示某种 Alert，可以通过将对应比特位置 0

+ (instancetype)sharedInstance;

- (void)handleRemoteNotificationUserInfo:(NSDictionary *)userInfo;
- (void)handleLocalNotification:(UILocalNotification *)notification;


/**
 *  如果当前条件满足显示 Delay Alert，则显示 Delay Alert
 */
- (void)showDelayAlert;

/**
 *  设置永远不再显示 Rate Alert，当 app 需要有自己 rate 逻辑时才需要调用，否则请不要调用
 */
+ (void)doNotShowRateForever;

@end
