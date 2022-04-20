//
//  BFConnection.h
//
//
//

#import <Foundation/Foundation.h>
#import "BFConnectionProtocol.h"

/*
 `BFURLConnection` 用于发异步的网络请求s
 # 特点：
 1. 只要传入一个 NSURLRequest 即可，如果是积木签名服务器请求，传入一个 BFServerAPIRequest 即可
 2. 支持 GCD，在没有 RunLoop 的 GCD queue 上发请求也可以得到异步返回
 3. 网络请求、数据组装、JSON 的反序列化都在一个非主线程上实现
 4. 从 NSOperation 继承而来，可以良好配合 NSOperationQueue 使用，以限制并发数量
 5. Block 方式回调，相比 delegate 形式更方便将逻辑代码集中处理
 6. 支持设定附加信息 userInfo
 7. 支持指定 OutputStream 到磁盘文件，即可以实现文件下载功能，并有下载进度报告功能
 8. 支持 BackgroundTask
 9. 对于返回结果为 JSON 的请求，可以指定 type 为 kBFConnectionType_JSON，请求可以自动进行返回结果的 JSON 的反序列化，使用者直接使用 jsonResponse 即可
 
 # 注意点：
 1. 回调采用 block 形式，注意避免 block 造成循环引用
 */
typedef NS_ENUM(NSInteger, BFConnectionType)
{
    kBFConnectionType_Normal,
    kBFConnectionType_JSON,
};

typedef NSURLSessionAuthChallengeDisposition (^BFURLConnectionReceiveAuthenticationChallengeBlock)(NSURLSession *session, NSURLAuthenticationChallenge *challenge, NSURLCredential * __autoreleasing *credential);
typedef NSURLSessionAuthChallengeDisposition (^BFURLConnectionTaskReceiveAuthenticationChallengeBlock)(NSURLSession *session, NSURLSessionTask *task, NSURLAuthenticationChallenge *challenge, NSURLCredential * __autoreleasing *credential);

@interface BFURLConnection : NSOperation <BFConnectionProtocol>

@property (nonatomic, readonly) BFConnectionStatus status;
@property (nonatomic, strong, readonly) NSData *responseData;
@property (nonatomic, strong, readonly) id jsonResponse;
@property (nonatomic, readonly) BFConnectionType type;
@property (nonatomic, strong, readonly) NSURLRequest *request;
@property (nonatomic, strong, readonly) NSURLResponse *response;
@property (nonatomic, strong, readonly) NSError *error;

@property (nonatomic, strong) NSOutputStream *outputStream; // 默认输出流为内存，如果下载文件可以设置文件类型的输出流
@property (nonatomic, copy) NSString *tag; // 设置此项以标示请求
@property (nonatomic, strong) NSDictionary *userInfo; // 设置此属性以添加附加信息

@property (nonatomic, copy) BFURLConnectionReceiveAuthenticationChallengeBlock sessionDidReceiveAuthenticationChallenge;
@property (nonatomic, copy) BFURLConnectionTaskReceiveAuthenticationChallengeBlock taskDidReceiveAuthenticationChallenge;

/**
 *  构造函数
 *
 *  @param request    需要发送的 NSURLRequest，可以是 BFServerAPIRequest
 *  @param type       返回结果为普通还是 JSON，如果为 JSON，请求成功会自动做 JSON 的反序列化，在另外线程，返回的 id response 对应 jsonResponse 属性，为对应的 NSArray 或者 NSDictionary，否则为 responseData 属性
 *  @param queue      回调执行的队列
 *  @param completion 完成后回调 block
 *
 *  @return BFURLConnection 对象
 */
- (id)initWithRequest:(NSURLRequest *)request
                 type:(BFConnectionType)type
                queue:(dispatch_queue_t)queue
           completion:(void (^)(BFURLConnection *connection, BOOL success, id response, NSError *error))completion;

/**
 *  构造函数
 *
 *  @param request    需要发送的 NSURLRequest，可以是 BFServerAPIRequest
 *  @param type       返回结果为普通还是 JSON，如果为 JSON，请求成功会自动做 JSON 的反序列化，在另外线程，返回的 id response 对应 jsonResponse 属性，为对应的 NSArray 或者 NSDictionary，否则为 responseData 属性
 *  @param queue      回调执行的队列
 *  @param progress   下载进度更新回调
 *  @param completion 完成后回调 block
 *s
 *  @return BFURLConnection 对象
 */
- (id)initWithRequest:(NSURLRequest *)request
                 type:(BFConnectionType)type
                queue:(dispatch_queue_t)queue
             progress:(void (^)(BFURLConnection *connection, long long totalBytesRead, long long totalBytesExpectedToRead))progress
           completion:(void (^)(BFURLConnection *connection, BOOL success, id response, NSError *error))completion;

#ifndef TARGET_IS_EXTENSION

/**
 *  设置请求为后台持续
 *
 *  @param handler 过期后想要执行的 block
 */
- (void)setShouldExecuteAsBackgroundTaskWithExpirationHandler:(void (^)(void))handler;


/**
 添加临时的接口给installMe，决定是否记录服务器返回的原始响应数据，如果没有调用此接口不会记flurry的
 确定了结果之后会删除

 @param available 是否记录flurry，YES是记录
 */
- (void)setFlurryRecordAvailable:(BOOL)available;

#endif

@end
