//
//

#import <Foundation/Foundation.h>

/**
 *  Token 变化后的通知
 */
extern NSString * const kBFNotficationName_DeviceTokenDidChange;

@interface BFPushManager : NSObject
@property (nonatomic, copy, readonly) NSString *deviceToken;

+ (instancetype)sharedInstance;

/**
 *  如果 app 需要自己控制注册 Push，请将 yaml 中的 AutoRegister 设置为 false，然后需要注册时调用此函数
 */
- (void)registerUserNotification;

@end
