//
//  BFConnectionProtocol.h
//
//
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, BFConnectionStatus)
{
    kBFConnectionStatus_Init,
    kBFConnectionStatus_Running,
    kBFConnectionStatus_Finished,
    kBFConnectionStatus_Failed,
    kBFConnectionStatus_Cancelled,
};

@protocol BFConnectionProtocol <NSObject>

@required
- (void)startAsyncronously;
- (void)cancel;
- (NSString *)tag;
- (BFConnectionStatus)status;

@end
