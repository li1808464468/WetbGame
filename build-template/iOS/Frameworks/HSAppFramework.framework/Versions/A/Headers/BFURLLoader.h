//
//  BFURLURLLoader.h
//
//

#import <Foundation/Foundation.h>

extern NSString * const HSURLErrorDomain;

typedef NS_ENUM(NSInteger, HSURLErrorCode)
{
	HSURLErrorUnknown =						-1,
	HSURLErrorCannotCreateConnection = 		-1000,
	HSURLErrorPlistWrongFormat =			-1001,
};

@protocol BFURLLoaderDelegate;

@interface BFURLLoader : NSObject 
{
}

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *username;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, strong) NSMutableData *receivedData;
@property (nonatomic, strong) NSURLConnection *connection;
@property (nonatomic, weak) id <BFURLLoaderDelegate> delegate;

- (id)initWithURLString:(NSString *)urlString delegate:(id)delegate;
- (id)initWithURLString:(NSString *)urlString username:(NSString *)username password:(NSString *)password delegate:(id)delegate;
- (id)initWithURLRequest:(NSURLRequest *)urlRequest name:(NSString *)name delegate:(id)delegate;
- (id)initWithURLRequest:(NSString *)urlString userAgent:(NSString *)userAgent name:(NSString *)name delegate:(id)delegate;
- (id)initWithURLRequest:(NSURLRequest *)urlRequest name:(NSString *)name username:(NSString *)username password:(NSString *)password delegate:(id)delegate;
- (void)cancel;

@end

@protocol BFURLLoaderDelegate <NSObject>

@optional
- (void)urlLoaderDidFinishLoading:(BFURLLoader *)urlLoader;
- (void)urlLoader:(BFURLLoader *)urlLoader didFailWithError:(NSError *)error;
- (void)urlLoader:(BFURLLoader *)urlLoader didReceiveResponse:(NSURLResponse *)response;
- (void)urlLoader:(BFURLLoader *)urlLoader didReceiveData:(NSData *)data;

@end
