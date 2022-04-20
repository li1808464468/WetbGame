#import <UIKit/UIKit.h>
#import <HSAppFramework/HSAppFramework.h>

@interface NativeAPI : NSObject

extern NSString * const kBFNotificationName_RemoveLogoView;

+ (NativeAPI *) sharedInstance;

- (void)applicationDidEnterBackground;

- (void)storeAFData:(NSDictionary *)data;

-(void)showToast:(NSString *)content;

- (void)logSnapshoWithJson:(NSMutableDictionary *)json;

-(BOOL)isGameCenterBind;

-(void)authenticateLocalPlayer;

-(void)updateScore:(long)score;

-(void)showLeaderboard;

-(void)recordAppOpenTime;

-(const char*)getAppOpenTime;

-(NSMutableDictionary*)getNativeData;

-(const char*)getAdId;

@end
