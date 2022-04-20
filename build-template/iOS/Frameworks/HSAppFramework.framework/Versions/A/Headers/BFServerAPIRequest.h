//
//
//
//

#import <Foundation/Foundation.h>

@protocol BFMultipartFormData;

typedef NS_ENUM(NSInteger, BFURLRequestMethod)
{
    kBFURLRequestMethod_Get,
    kBFURLRequestMethod_Post,
};

@interface BFServerAPIRequest : NSMutableURLRequest

/**
 *  iHandy API 专用请求
 *
 *  @param url      url
 *  @param dataBody 请求中 content key 对应的字典或数组
 *  @param method   HTTP 请求 GET 或者 POST
 *
 *  @return BFServerAPIRequest 实例
 */
+ (instancetype)requestWithURL:(NSString *)url
                      dataBody:(id)dataBody
                        method:(BFURLRequestMethod)method;

//  hashKey/sigKey为nil将使用配置里面提供的值(正常使用场景)，也可以用指定的hashKey/sigKey
+ (instancetype)requestWithURL:(NSString *)url
                      dataBody:(id)dataBody
                        method:(BFURLRequestMethod)method
                       hashKey:(NSString *)hashKey
                        sigKey:(NSString *)sigKey;


// /**
//  *  iHandy API 专用请求, 支持加密
//  *
//  *  @param url              url
//  *  @param dataBody         请求中 content key 对应的字典或数组
//  *  @param method           HTTP 请求 GET 或者 POST
//  *  @param enableEncryption 是否加密
//  *
//  *  @return BFServerAPIRequest 实例
//  */
// + (instancetype)requestWithURL:(NSString *)url
//                       dataBody:(id)dataBody
//                         method:(BFURLRequestMethod)method
//               enableEncryption:(BOOL)enableEncryption;

/**
 *  生成一个 multipart 的 POST 请求的 request
 *
 *  @param url      URL
 *  @param dataBody 请求中 content key 对应的字典或数组
 *  @param block    拼 multipart
 *
 *  @return BFServerAPIRequest 实例
 */
+ (instancetype)multipartFormRequestWithURL:(NSString *)url
                                   dataBody:(NSDictionary *)dataBody
                  constructingBodyWithBlock:(void (^)(id <BFMultipartFormData> formData))block;

//  hashKey/sigKey为nil将使用配置里面提供的值(正常使用场景)，也可以用指定的hashKey/sigKey
+ (instancetype)multipartFormRequestWithURL:(NSString *)url
                                   dataBody:(NSDictionary *)dataBody
                  constructingBodyWithBlock:(void (^)(id <BFMultipartFormData> formData))block
                                    hashKey:(NSString *)hashKey
                                     sigKey:(NSString *)sigKey;
// /**
//  *  生成一个 multipart 的 POST 请求的 request, 支持加密
//  *
//  *  @param url              URL
//  *  @param dataBody         请求中 content key 对应的字典或数组
//  *  @param enableEncryption 是否加密
//  *  @param block            拼 multipart
//  *
//  *  @return BFServerAPIRequest 实例
//  */
// + (instancetype)multipartFormRequestWithURL:(NSString *)url
//                                    dataBody:(NSDictionary *)dataBody
//                            enableEncryption:(BOOL)enableEncryption
//                   constructingBodyWithBlock:(void (^)(id <BFMultipartFormData> formData))block;

/**
 *  根据 content 内容算出签名后的字典
 *
 *  @param dataBody 请求中 content key 对应的字典或数组
 *
 *  @return 签名后的字典
 */
+ (NSMutableDictionary *)signedParametersWithDataBody:(NSDictionary *)dataBody;

/**
 *  做 form URL encode
 *
 *  @param parameters 需要 form URL encode 的键值对
 *
 *  @return URL encode 后的串
 */
+ (NSString *)formURLEncodedStringFromParameters:(NSDictionary *)parameters;

@end

@protocol BFMultipartFormData

/**
 *  加入一个 Part
 *
 *  @param fileURL     本地文件的 URL
 *  @param name        传到服务器的 name，例如 cover
 *  @param fileName    文件名， 例如 cover.png
 *  @param contentType MIME 类型，例如 image/png
 *  @param error       error 回写地址
 *
 *  @return 是否成功
 */
- (BOOL)appendPartWithFileURL:(NSURL *)fileURL
                         name:(NSString *)name
                     fileName:(NSString *)fileName
                  contentType:(NSString *)contentType
                        error:(NSError * __autoreleasing *)error;

- (BOOL)appendPartWithFileURL:(NSURL *)fileURL
                         name:(NSString *)name
                        error:(NSError * __autoreleasing *)error;

- (void)appendPartWithInputStream:(NSInputStream *)inputStream
                             name:(NSString *)name
                         fileName:(NSString *)fileName
                           length:(unsigned long long)length
                      contentType:(NSString *)contentType;


- (void)appendPartWithFileData:(NSData *)data
                          name:(NSString *)name
                      fileName:(NSString *)fileName
                   contentType:(NSString *)contentType;


- (void)appendPartWithFormData:(NSData *)data
                          name:(NSString *)name;

- (void)appendPartWithHeaders:(NSDictionary *)headers
                         body:(NSData *)body;


@end


