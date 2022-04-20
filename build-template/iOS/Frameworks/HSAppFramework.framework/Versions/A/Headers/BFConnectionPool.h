//
//  BFConnectionPool.h
//
//
//

#import <Foundation/Foundation.h>
#import "BFConnectionProtocol.h"

typedef NS_ENUM(NSInteger, BFConnectionPriority)
{
    kBFConnectionPriority_Normal,
    kBFConnectionPriority_High
};

@interface BFConnectionPool : NSObject
@property (nonatomic, readonly) NSInteger maxConcurrentCount;
@property (nonatomic, readonly) NSInteger currentRunningConnectionCount; // 当前正在运行的连接数
@property (nonatomic, readonly) NSInteger currentConnectionCount; // pool 中的所有连接数

/**
 *  初始化方法
 *
 *  @param maxConcurrentCount 最大同时发出请求的数量
 *
 *  @return BFConnectionPool 对象
 */
- (id)initWithMaxConcurrentCount:(int)maxConcurrentCount;


/**
 *  添加 Connection 任务到 Pool 中
 *
 *  @param connection 需要添加的 Connection 任务，任务都要实现 BFConnectionProtocol 接口
 *  @param priority   优先级
 */
- (void)addConnection:(id<BFConnectionProtocol>)connection withPriority:(BFConnectionPriority)priority;


/**
 *  删除 Connection 任务
 *
 *  @param connection 需要删除的 Connection 任务，任务都要实现 BFConnectionProtocol 接口，并不会调用 cancel
 */
- (void)removeConnection:(id<BFConnectionProtocol>)connection;


/**
 *  根据 tag 删除请求
 *
 *  @param tag 字符串名称，使用时自己控制好命名空间
 */
- (void)removeConnectionWithTag:(NSString *)tag;

/**
 *  获取 pool 中带有指定 tag 的所有请求
 *
 *  @param tag 期待的 tag 值
 *
 *  @return 含有指定 tag 请求的数组
 */
- (NSArray *)connectionsWithTag:(NSString *)tag;

/**
 *  清空 pool，将所有请求全部取消掉
 */
- (void)drain;


@end
