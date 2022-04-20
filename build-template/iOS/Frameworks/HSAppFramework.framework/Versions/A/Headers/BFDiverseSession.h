//
//  BFDiverseSession.h
//  BFAppFramework
//
//

#import <Foundation/Foundation.h>

extern NSString * const BFDiverseSession_Notification_SessionStart;
extern NSString * const BFDiverseSession_Notification_SessionEnd;

@interface BFDiverseSession : NSObject

+ (instancetype)sharedInstance;

/**
 * 发送BFDiverseSession_Notification_SessionStart
 * 注意：只有通过配置接管BFDiverseSession后才会发送
 */
-(void)start;

/**
 * 发送BFDiverseSession_Notification_SessionEnd
 * 注意：只有通过配置接管BFDiverseSession后才会发送
 */
-(void)end;

@end
