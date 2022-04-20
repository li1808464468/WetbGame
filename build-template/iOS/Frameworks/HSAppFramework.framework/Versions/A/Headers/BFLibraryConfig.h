//
//  BFLibraryConfig
//  BFAppFramework
//
//

#import <Foundation/Foundation.h>

@protocol BFLibraryProtocol;

@interface BFLibraryConfig : NSObject
{
}

+ (BFLibraryConfig *)sharedInstance;

/**
 *  开始一次库的配置的读取和获取，即需要调用此函数才能触发一次远程配置的下载
 *
 *  @param library     代理
 *  @param urlString   远程配置文件的 url
 *  @param initialData 初始的配置内容
 */
- (void)startForLibrary:(id<BFLibraryProtocol>)library withURLString:(NSString *)urlString initialData:(NSDictionary *)initialData;

/**
 *  结束库配置的托管，如果存在下载则取消下载
 *
 *  @param library 代理
 */
- (void)endForLibrary:(id<BFLibraryProtocol>)library;

/**
 *  获取库的配置
 *
 *  @param library 代理
 *
 *  @return 对应配置的数据字典
 */
- (NSDictionary *)dataForLibrary:(id<BFLibraryProtocol>)library;

@end


/**
 *  需要实现此协议
 */
@protocol BFLibraryProtocol <NSObject>
@required
- (NSUInteger)libraryVersionNumber;
- (NSString *)hsLibName;

@optional
- (void)libraryRemoteConfigDidFinishInitialization;
- (void)libraryRemoteConfigDataChanged;

@end
