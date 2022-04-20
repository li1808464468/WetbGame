//
//  BFConfig.h
//
//

#import <Foundation/Foundation.h>

// config 变化之后的通知
extern NSString * const kBFNotificationName_ConfigChanged;

// fetchRemote 调用之后，当获取远程配置结束时发此通知（无论获取成功还是失败）
extern NSString * const kBFNotificationName_RemoteFetchFinished;


// 成功 YES 失败 NO: userInfo = @{kHSNotificationUserInfoKey_RemoteFetchFinishedStatus: @(YES/NO)}
extern NSString * const kBFNotificationUserInfoKey_RemoteFetchFinishedStatus;

@interface BFConfig : NSObject
@property (nonatomic, strong, readonly) NSDictionary *data;
@property (nonatomic, assign, readonly) BOOL isRestrictedUser;
@property (nonatomic, copy, readonly) NSString *restrictedDescription;
@property (nonatomic, readonly) NSString *segmentName;

+ (instancetype)sharedInstance;

/**
 *  配置
 *
 *  @param yamlFileName           yaml 文件在 bundle 中名称
 *  @param shouldDeleteCachedFile 是否要删除沙盒中的文件，app 版本升级时需要删除，today 和 watch 自行处理
 */
- (void)configureWithYamlFileName:(NSString *)yamlFileName shouldDeleteCachedFile:(BOOL)shouldDeleteCachedFile;

/// 发起RemoteConfig的请求
- (void)fetchRemote;

/**
 *  仅供调试使用
 *
 *  @param token 设置用户组，0-999
 */
+ (void)setSegmentToken:(NSInteger)token;

@end
