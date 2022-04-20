//
//  BFPlistLoader.h
//

#import <Foundation/Foundation.h>

extern NSString * const HSPlistDataKey;
extern NSString * const HSPlistUpdateIntervalKey;

typedef NS_ENUM(NSInteger, HSPlistLoaderSource)
{
	HSPlistLoaderSourceLocalFile			= 0,
	HSPlistLoaderSourceServer				= 1,
};

@protocol HSPlistLoaderDelegate;

@interface BFPlistLoader : NSObject
{
}
@property (nonatomic, copy) NSString *tag;
@property (nonatomic, copy) NSString *plistName;
@property (nonatomic, copy) NSString *plistPath;
@property (nonatomic, strong) NSDictionary *plistData;
@property (nonatomic, assign) HSPlistLoaderSource plistSource;
@property (nonatomic, weak) id <HSPlistLoaderDelegate> delegate;

- (id)initWithName:(NSString *)name URLString:(NSString *)urlString delegate:(id)delegate;
- (id)initWithName:(NSString *)name URLString:(NSString *)urlString username:(NSString *)username password:(NSString *)password delegate:(id)delegate;

- (void)cancel;

+ (NSString *)plistFilePathForName:(NSString *)name;
+ (BOOL)savePlist:(NSDictionary *)data forName:(NSString *)name;
+ (NSDictionary *)loadPlistForName:(NSString *)name;

@end

@protocol HSPlistLoaderDelegate <NSObject>

@optional
- (void)plistLoaderDidFinishLoading:(BFPlistLoader *)plistLoader;
- (void)plistLoader:(BFPlistLoader *)plistLoader didFailWithError:(NSError *)error;

@end
