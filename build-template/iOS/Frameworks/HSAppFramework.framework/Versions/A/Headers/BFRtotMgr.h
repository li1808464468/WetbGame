//
//  RtotMgr.h
//  BFAppFramework
//
//

#import <Foundation/Foundation.h>
@class BFRtotTask;

extern NSString * const BFRtotMgrGetTaskConnectionDidFinish;

@interface BFRtotMgr : NSObject

/**
 *  获取单例对象
 *  @return BFRtotMgr单例
 */
+(instancetype)sharedInstance;

/**
 *  根据task名获取content
 *  @param taskName task名
 *  @return 对应task名的content字典
 */
- (NSDictionary *)getContent:(NSString *)taskName;

/**
 *  根据task名，向服务器发action
 *  @param taskName task名
 *  @param action   需要传递给服务器的action
 */
-(void)sendEventToServer:(NSString *)taskName action:(NSString *)action;

@end
