//
//  BFJSONLoader.h
//
//

#import <Foundation/Foundation.h>

@class BFURLLoader;

@protocol HSJSONLoaderDelegate;

@interface BFJSONLoader : NSObject {
}

@property (nonatomic, copy) NSString *name;
@property (nonatomic, strong) id jsonData;
@property (nonatomic, strong) BFURLLoader *loader;
@property (nonatomic, weak) id <HSJSONLoaderDelegate> delegate;

- (id)initWithURLString:(NSString *)urlString userAgent:(NSString *)userAgent name:(NSString *)name delegate:(id)delegate;
- (id)initWithURLString:(NSString *)urlString name:(NSString *)name delegate:(id)delegate;
- (id)initWithURLString:(NSString *)urlString delegate:(id)delegate;
- (void)cancel;

@end

@protocol HSJSONLoaderDelegate <NSObject>

@optional
- (void)jsonLoaderDidFinishLoading:(BFJSONLoader *)jsonLoader;
- (void)jsonLoader:(BFJSONLoader *)jsonLoader didFailWithError:(NSError *)error;

@end
