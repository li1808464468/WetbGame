
#import "NativeAPI.h"

#import <CoreTelephony/CTCarrier.h>
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <GameKit/GameKit.h>
//#import <Crashlytics/Crashlytics.h>
#import <AdSupport/AdSupport.h>



NSString * const kES_Index = @"es_index";
NSString * const kES_UserDay = @"es_user_day";
NSString * const kES_Last_Game = @"es_last_game";
NSString * const kES_Data = @"es_data";
NSString * const File_ES = @"es_local_storage";
NSInteger const Max_ES_Data_Count = 3;

NSString * const kBFNotificationName_RemoveLogoView = @"ApplicationRemoveLogoView";

NSString * const File_AF = @"af_local_storage";
NSString * const kBFInstallMode = @"bf_install_mode";
NSString * const kBFMediaSource = @"bf_media_source";
NSString * const kBFCampaignID = @"bf_campaign_id";
NSString * const kBFCampaign = @"bf_campaign";
NSString * const kBFAgency = @"bf_agency";
NSString * const kBFAdSet = @"bf_adset";
NSString * const kBFAdSetID = @"bf_adset_id";
NSString * const kBFSiteID = @"bf_site_id";

NSInteger AppOpenTime = 0;

@interface NativeAPI ()<GKGameCenterControllerDelegate>

@property(nonatomic, strong) NSUserDefaults *esStorage;
@property(nonatomic, strong) NSUserDefaults *afStorage;

@property(nonatomic, strong) NSString *leaderboardIdentifier;

@end

@implementation NativeAPI

static dispatch_queue_t backgroundQueue;
CTCarrier *carrier = nil;

+ (instancetype) sharedInstance {
    static id inst = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        inst = [self.alloc init];
        backgroundQueue = dispatch_queue_create("com.background.queue", 0);
    });
    return inst;
}

- (id) init {
    self = [super init];
    if (self) {
        _afStorage = [[NSUserDefaults alloc] initWithSuiteName:File_AF];
        _leaderboardIdentifier = @"legendstone_leaderboard";
        
                [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(bannerShow) name:@"com.mopub.impression-notification.impression-received" object:nil];
    }
    return self;
}


- (void)applicationDidEnterBackground {
    
}

#pragma mark - Private

- (void)bannerShow {
    if ([[[BFConfig sharedInstance].data valueForKeyPath:@"Application.Banner.ESLog"] boolValue]) {
        NSMutableDictionary *esDic = [[NSMutableDictionary alloc] init];
        [esDic setValue:@"Banner_Ad_Shown" forKey:@"h5Action"];
        [self logSnapshoWithJson:esDic];
    }
//    [Answers logCustomEventWithName:@"Banner_Ad_Shown" customAttributes:nil];
}

- (NSString*)getCountryCode {
    if (carrier == nil) {
        carrier = [[[CTTelephonyNetworkInfo alloc] init] subscriberCellularProvider];
    }
    
    NSString *code = [carrier isoCountryCode];
    if (!code.length) {
        code = [[NSLocale currentLocale] countryCode];
    }
    
    return code;
}

- (void)storeAFData:(NSDictionary *)data {
    if (data == nil) {
        return;
    }
    if ([data objectForKey:@"af_status"]) {
        [self.afStorage setObject:[data objectForKey:@"af_status"] forKey:kBFInstallMode];
    }
    if ([data objectForKey:@"agency"]) {
        [self.afStorage setObject:[data objectForKey:@"agency"] forKey:kBFAgency];
    }
    if ([data objectForKey:@"media_source"]) {
        [self.afStorage setObject:[data objectForKey:@"media_source"] forKey:kBFMediaSource];
    }
    if ([data objectForKey:@"adset_id"]) {
        [self.afStorage setObject:[data objectForKey:@"adset_id"] forKey:kBFAdSetID];
    }
    if ([data objectForKey:@"adset"]) {
        [self.afStorage setObject:[data objectForKey:@"adset"] forKey:kBFAdSet];
    }
    if ([data objectForKey:@"campaign"]) {
        [self.afStorage setObject:[data objectForKey:@"campaign"] forKey:kBFCampaign];
    }
    if ([data objectForKey:@"campaign_id"]) {
        [self.afStorage setObject:[data objectForKey:@"campaign_id"] forKey:kBFCampaignID];
    }
    if ([data objectForKey:@"af_siteid"]) {
        [self.afStorage setObject:[data objectForKey:@"af_siteid"] forKey:kBFSiteID];
    }
}


#pragma mark - snap shoot -

- (void)logSnapshoWithJson:(NSMutableDictionary *)json {
    __weak __typeof(self) weakSelf = self;
    dispatch_async(backgroundQueue, ^{
        [weakSelf logESInBackground:json];
    });
}

-(void)logESInBackground:(NSMutableDictionary *)json {
    if (self.esStorage == nil) {
        self.esStorage = [[NSUserDefaults alloc] initWithSuiteName:File_ES];
    }
    
    //device
    NSMutableDictionary *device = [[NSMutableDictionary alloc] init];
    [device setValue:[[UIDevice currentDevice] systemVersion] forKey:@"os_version"];
    [device setValue:[self getCountryCode] forKey:@"country"];
    [device setValue:[[NSLocale currentLocale] languageCode] forKey:@"language"];
    [device setValue:[[NSTimeZone localTimeZone] abbreviation] forKey:@"time-zone"];
    [device setValue:[NSNumber numberWithLong:(long)[[NSDate date] timeIntervalSince1970] * 1000] forKey:@"timestamp"];
    [device setValue:([[BFReachability reachabilityForLocalWiFi] currentReachabilityStatus] == BFReachabilityReachableViaWiFi) ? @YES : @NO forKey:@"wifi"];
    [json setValue:device forKey:@"device"];
    
    //application
    NSMutableDictionary *app = [[NSMutableDictionary alloc] init];
    [app setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.App.AppName"] forKey:@"name"];
    [app setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.App.ConfigVersion"] forKey:@"config_version"];
    [app setValue:[BFVersionControl appVersion] forKey:@"version"];
    [app setValue:[BFApplication sharedInstance].firstLaunchInfo.appVersion forKey:@"first_version"];
    [app setValue:@"iOS" forKey:@"type"];
    [app setValue:[NSNumber numberWithLong:[[BFSessionManager sharedInstance].firstSessionStartTime timeIntervalSince1970] * 1000] forKey:@"time"];
    [app setValue: [[BFReachability reachabilityForInternetConnection] currentReachabilityStatus] == BFReachabilityNotReachable ? @"no_netwrok" : @"connected" forKey:@"network"];
    [json setValue:app forKey:@"app"];;
    
    //user
    NSMutableDictionary *user = [[NSMutableDictionary alloc] init];
    
    [user setValue:[self.afStorage stringForKey:kBFInstallMode] forKey:@"install_type"];
    [user setValue:[self.afStorage stringForKey:kBFMediaSource] forKey:@"media_source"];
    [user setValue:[self.afStorage stringForKey:kBFCampaign] forKey:@"campaign"];
    [user setValue:[self.afStorage stringForKey:kBFCampaignID] forKey:@"campaign_id"];
    [user setValue:[self.afStorage stringForKey:kBFAgency] forKey:@"agency"];
    [user setValue:[self.afStorage stringForKey:kBFAdSet] forKey:@"adset"];
    [user setValue:[self.afStorage stringForKey:kBFAdSetID] forKey:@"adset_id"];
    [user setValue:[self.afStorage stringForKey:kBFSiteID] forKey:@"site_id"];
    
    [user setValue:[BFUtils deviceId] forKey:@"user_id"];
    [user setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"SegmentName"] forKey:@"segment"];
    [user setValue:[[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString] forKey:@"ad_id"];
    [user setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.ConfigVersion"] forKey:@"config_version"];
    long lastTime = [self.afStorage integerForKey:kES_Last_Game];
    if (lastTime == 0) {
        lastTime = [[BFSessionManager sharedInstance].firstSessionStartTime timeIntervalSince1970];
    }
    long day = [self.afStorage integerForKey:kES_UserDay];
    if (![[NSCalendar currentCalendar] isDate:[NSDate dateWithTimeIntervalSince1970:lastTime] inSameDayAsDate:[NSDate date]]) {
        day ++;
        [self.afStorage setInteger:day forKey:kES_UserDay];
    }
    [user setValue:[NSNumber numberWithLong:day] forKey:@"day"];
    [self.afStorage setInteger:[[NSDate date] timeIntervalSince1970] forKey:kES_Last_Game];
    [json setValue:user forKey:@"user"];
    
    long index = [self.esStorage integerForKey:kES_Index];
    [self.esStorage setInteger:++index forKey:kES_Index];
    [json setValue:[NSNumber numberWithLong:index] forKey:@"es_index"];
    
    NSMutableArray *data = [[NSMutableArray alloc] init];
    [data addObject:json];
    
    [self loadESData:data];
    
    NSMutableDictionary *jsonData = [[NSMutableDictionary alloc] init];
    [jsonData setValue:data forKey:@"data"];
    
    BFServerAPIRequest *request = [BFServerAPIRequest requestWithURL:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.esUrl"] dataBody:jsonData method:kBFURLRequestMethod_Post];
    
    __weak __typeof(self) weakSelf = self;
    BFURLConnection *connection = [[BFURLConnection alloc] initWithRequest:request type:kBFConnectionType_JSON queue:backgroundQueue completion:^(BFURLConnection *connection, BOOL success, id response, NSError *error){
        if (success && (int)[[[response objectForKey:@"meta"] objectForKey:@"code"] intValue] == 200) {
            DebugLog(@"log Snapshot success");
        } else {
            DebugLog(@"log Snapshot error %@", error);
            [weakSelf storeData:data];
        }
    }];
    [connection startAsyncronously];
}

-(void)loadESData:(NSMutableArray *)data {
    NSString *storage = [self.esStorage objectForKey:kES_Data];
    if (storage != nil && storage.length > 0) {
        NSArray *localArray = [storage componentsSeparatedByString: @"$$"];
        long count = localArray.count <= Max_ES_Data_Count ? : Max_ES_Data_Count;
        for (int i = 0; i < count; i++) {
            [data addObject:[self decodeJSON:[localArray objectAtIndex:i]]];
        }
        
        [self.esStorage setObject:@"" forKey:kES_Data];
    }
}

-(void)storeData:(NSArray *)data {
    NSString *origin = [self.esStorage objectForKey:kES_Data];
    NSMutableString *content = [[NSMutableString alloc] init];
    if (origin != nil && origin.length > 0) {
        [content appendString:origin];
        [content appendString:@"$$"];
    }
    
    int index = 0;
    for (index = 0; index < data.count; index++) {
        [content appendString:[self encodeJSON:[data objectAtIndex:index]]];
        if (index < data.count - 1) {
            [content appendString:@"$$"];
        }
    }
    [self.esStorage setObject:content forKey:kES_Data];
}

-(id)decodeJSON:(NSString *)json {
    if (json.length == 0) {
        return nil;
    }
    return [NSJSONSerialization JSONObjectWithData:[json dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
}

-(NSString *)encodeJSON:(id)value {
    if (value == nil) {
        return @"";
    }
    NSData *data = [NSJSONSerialization dataWithJSONObject:value options:0 error:nil];
    return [NSString.alloc initWithData:data encoding:NSUTF8StringEncoding] ?: @"";
}

-(void)showToast:(NSString *)content {
    UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:@"" message:content preferredStyle:UIAlertControllerStyleActionSheet];
    UIAlertAction *sureBtn = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDestructive handler:nil];
    
    [sureBtn setValue:[UIColor grayColor] forKey:@"titleTextColor"];
    [alertVc addAction :sureBtn];
    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:alertVc animated:YES completion:nil];
}

-(void)recordAppOpenTime{
    NSTimeInterval interval = [[NSDate date] timeIntervalSince1970] * 1000;
    AppOpenTime = interval;
}

-(const char*)getAppOpenTime{
    NSTimeInterval interval = [[NSDate date] timeIntervalSince1970] * 1000;
    NSInteger nowTime = interval;
    
    NSInteger tmpTime = nowTime - AppOpenTime;
    NSNumber *longlongNumber = [NSNumber numberWithLongLong:tmpTime];
    NSString *stringNumber = [longlongNumber stringValue];
    return [stringNumber UTF8String];
}

-(NSMutableDictionary*)getNativeData{
    long lastTime = [self.afStorage integerForKey:kES_Last_Game];
    if (lastTime == 0) {
        lastTime = [[BFSessionManager sharedInstance].firstSessionStartTime timeIntervalSince1970];
    }
    long day = [self.afStorage integerForKey:kES_UserDay];
    if (![[NSCalendar currentCalendar] isDate:[NSDate dateWithTimeIntervalSince1970:lastTime] inSameDayAsDate:[NSDate date]]) {
        day ++;
        [self.afStorage setInteger:day forKey:kES_UserDay];
    }
    
    NSMutableDictionary *data = [[NSMutableDictionary alloc] init];
    [data setValue:[[NSLocale currentLocale] languageCode] forKey:@"language"];
    [data setValue:[self getCountryCode] forKey:@"country"];
    [data setValue:[[UIDevice currentDevice] systemVersion] forKey:@"os_version"];
//    [data setValue:[[NSLocale currentLocale] languageCode] forKey:@"debug"];
    [data setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"SegmentName"] forKey:@"segment"];
    [data setValue:[BFUtils deviceId] forKey:@"user_id"];
    [data setValue:[self.afStorage stringForKey:kBFInstallMode] forKey:@"install_type"];
    [data setValue:[NSNumber numberWithLong:day] forKey:@"day"];
    [data setValue:[BFApplication sharedInstance].firstLaunchInfo.appVersion forKey:@"first_version"];
    [data setValue:[NSNumber numberWithLong:[[BFSessionManager sharedInstance].firstSessionStartTime timeIntervalSince1970] * 1000] forKey:@"time"];
//    [data setValue:[self getCountryCode] forKey:@"first_code"];
//    [data setValue:[[UIDevice currentDevice] systemVersion] forKey:@"code"];
    [data setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.App.AppName"] forKey:@"name"];
    [data setValue:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.App.ConfigVersion"] forKey:@"config_version"];
    [data setValue:[BFVersionControl appVersion] forKey:@"version"];
    
    //appsflyer
    [data setValue:[self.afStorage stringForKey:kBFInstallMode] forKey:@"af_status"];
    [data setValue:[self.afStorage stringForKey:kBFMediaSource] forKey:@"media_source"];
    [data setValue:[self.afStorage stringForKey:kBFCampaign] forKey:@"campaign"];
    [data setValue:[self.afStorage stringForKey:kBFCampaignID] forKey:@"campaign_id"];
    [data setValue:[self.afStorage stringForKey:kBFAgency] forKey:@"agency"];
    [data setValue:[self.afStorage stringForKey:kBFAdSet] forKey:@"af_adset"];
    [data setValue:[self.afStorage stringForKey:kBFAdSetID] forKey:@"adset_id"];
    [data setValue:[self.afStorage stringForKey:kBFSiteID] forKey:@"af_siteid"];
    
    return data;
}

-(const char*)getAdId{
    return [[[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString] UTF8String];
}

#pragma gamecenter

-(BOOL)isGameCenterBind {
    return [[GKLocalPlayer localPlayer] isAuthenticated];
}

-(void)authenticateLocalPlayer {
    GKLocalPlayer *localPlayer = [GKLocalPlayer localPlayer];
    
    localPlayer.authenticateHandler = ^(UIViewController *viewController, NSError *error){
        if (viewController != nil) {
            [[BFApplication sharedInstance].window.rootViewController presentViewController:viewController animated:YES completion:nil];
        }
        else{
            if ([[GKLocalPlayer localPlayer] isAuthenticated]) {
                
                // Get the default leaderboard identifier.
                [[GKLocalPlayer localPlayer] loadDefaultLeaderboardIdentifierWithCompletionHandler:^(NSString *leaderboardIdentifier, NSError *error) {
                    
                    if (error != nil) {
                        NSLog(@"auth failed, %@", [error localizedDescription]);
                    }
                    else{
                        _leaderboardIdentifier = leaderboardIdentifier;
                    }
                }];
            }
            
            else{
                NSLog(@"auth failed!!!!");
            }
        }
    };
}


-(void)updateScore:(long)score {
    GKScore *scores = [[GKScore alloc] initWithLeaderboardIdentifier:_leaderboardIdentifier];
    scores.value = score;
    
    [GKScore reportScores:@[scores] withCompletionHandler:^(NSError *error) {
        if (error != nil) {
            NSLog(@"%@", [error localizedDescription]);
        }
    }];
}

-(void)showLeaderboard {
    
    if (![self isGameCenterBind]) {
        [self authenticateLocalPlayer];
        [[NativeAPI sharedInstance] showToast:@"Oops! You may authenticate player first by Game Center~"];
        return;
    }
    
    GKGameCenterViewController *gcViewController = [[GKGameCenterViewController alloc] init];
    
    gcViewController.gameCenterDelegate = self;
    gcViewController.viewState = GKGameCenterViewControllerStateLeaderboards;
    gcViewController.leaderboardIdentifier = _leaderboardIdentifier;
    
    [[BFApplication sharedInstance].window.rootViewController presentViewController:gcViewController animated:YES completion:nil];

}

-(void)gameCenterViewControllerDidFinish:(GKGameCenterViewController *)gameCenterViewController
{
    [gameCenterViewController dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - etc

- (NSString *)unobfuscateString:(const char *)string {
    if (string == NULL || strlen(string) != 32) {
        return nil;
    }
    char key[33];
    key[32] = 0;
    for (int i = 0; i < 32; ++i) {
        char v = string[i];
        if (v <= 112) {
            if (v <= 94) {
                if (v <= 81) {
                    if (v <= 54) {
                        if (v <= 46) {
                            if (v <= 41) {
                                if (v <= 33) {
                                    if (v <= 32) {
                                        if (v == 32) {
                                            key[i] = 102;
                                        }
                                    } else {
                                        key[i] = 136;
                                        if (v == 33) {
                                            key[i] = 118;
                                        }
                                    }
                                } else {
                                    key[i] = 162;
                                    if (v <= 37) {
                                        if (v <= 35) {
                                            if (v <= 34) {
                                                if (v == 34) {
                                                    key[i] = 114;
                                                }
                                            } else {
                                                key[i] = 54;
                                                if (v == 35) {
                                                    key[i] = 72;
                                                }
                                            }
                                        } else {
                                            key[i] = 218;
                                            if (v <= 36) {
                                                if (v == 36) {
                                                    key[i] = 59;
                                                }
                                            } else {
                                                key[i] = 64;
                                                if (v == 37) {
                                                    key[i] = 87;
                                                }
                                            }
                                        }
                                    } else {
                                        key[i] = 48;
                                        if (v <= 38) {
                                            if (v == 38) {
                                                key[i] = 86;
                                            }
                                        } else {
                                            key[i] = 145;
                                            if (v <= 40) {
                                                if (v <= 39) {
                                                    if (v == 39) {
                                                        key[i] = 109;
                                                    }
                                                } else {
                                                    key[i] = 140;
                                                    if (v == 40) {
                                                        key[i] = 73;
                                                    }
                                                }
                                            } else {
                                                key[i] = 216;
                                                if (v == 41) {
                                                    key[i] = 71;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                key[i] = 179;
                                if (v <= 42) {
                                    if (v == 42) {
                                        key[i] = 43;
                                    }
                                } else {
                                    key[i] = 227;
                                    if (v <= 44) {
                                        if (v <= 43) {
                                            if (v == 43) {
                                                key[i] = 70;
                                            }
                                        } else {
                                            key[i] = 2;
                                            if (v == 44) {
                                                key[i] = 96;
                                            }
                                        }
                                    } else {
                                        key[i] = 146;
                                        if (v <= 45) {
                                            if (v == 45) {
                                                key[i] = 90;
                                            }
                                        } else {
                                            key[i] = 219;
                                            if (v == 46) {
                                                key[i] = 122;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            key[i] = 115;
                            if (v <= 53) {
                                if (v <= 49) {
                                    if (v <= 48) {
                                        if (v <= 47) {
                                            if (v == 47) {
                                                key[i] = 107;
                                            }
                                        } else {
                                            key[i] = 67;
                                            if (v == 48) {
                                                key[i] = 69;
                                            }
                                        }
                                    } else {
                                        key[i] = 160;
                                        if (v == 49) {
                                            key[i] = 110;
                                        }
                                    }
                                } else {
                                    key[i] = 13;
                                    if (v <= 52) {
                                        if (v <= 51) {
                                            if (v <= 50) {
                                                if (v == 50) {
                                                    key[i] = 47;
                                                }
                                            } else {
                                                key[i] = 77;
                                                if (v == 51) {
                                                    key[i] = 57;
                                                }
                                            }
                                        } else {
                                            key[i] = 185;
                                            if (v == 52) {
                                                key[i] = 45;
                                            }
                                        }
                                    } else {
                                        key[i] = 76;
                                        if (v == 53) {
                                            key[i] = 50;
                                        }
                                    }
                                }
                            } else {
                                key[i] = 57;
                                if (v == 54) {
                                    key[i] = 124;
                                }
                            }
                        }
                    } else {
                        key[i] = 169;
                        if (v <= 67) {
                            if (v <= 64) {
                                if (v <= 55) {
                                    if (v == 55) {
                                        key[i] = 37;
                                    }
                                } else {
                                    key[i] = 41;
                                    if (v <= 56) {
                                        if (v == 56) {
                                            key[i] = 92;
                                        }
                                    } else {
                                        key[i] = 57;
                                        if (v <= 58) {
                                            if (v <= 57) {
                                                if (v == 57) {
                                                    key[i] = 83;
                                                }
                                            } else {
                                                key[i] = 83;
                                                if (v == 58) {
                                                    key[i] = 125;
                                                }
                                            }
                                        } else {
                                            key[i] = 84;
                                            if (v <= 61) {
                                                if (v <= 59) {
                                                    if (v == 59) {
                                                        key[i] = 84;
                                                    }
                                                } else {
                                                    key[i] = 213;
                                                    if (v <= 60) {
                                                        if (v == 60) {
                                                            key[i] = 116;
                                                        }
                                                    } else {
                                                        key[i] = 210;
                                                        if (v == 61) {
                                                            key[i] = 121;
                                                        }
                                                    }
                                                }
                                            } else {
                                                key[i] = 176;
                                                if (v <= 62) {
                                                    if (v == 62) {
                                                        key[i] = 100;
                                                    }
                                                } else {
                                                    key[i] = 95;
                                                    if (v <= 63) {
                                                        if (v == 63) {
                                                            key[i] = 108;
                                                        }
                                                    } else {
                                                        key[i] = 85;
                                                        if (v == 64) {
                                                            key[i] = 32;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                key[i] = 125;
                                if (v <= 66) {
                                    if (v <= 65) {
                                        if (v == 65) {
                                            key[i] = 111;
                                        }
                                    } else {
                                        key[i] = 150;
                                        if (v == 66) {
                                            key[i] = 103;
                                        }
                                    }
                                } else {
                                    key[i] = 184;
                                    if (v == 67) {
                                        key[i] = 91;
                                    }
                                }
                            }
                        } else {
                            key[i] = 26;
                            if (v <= 77) {
                                if (v <= 74) {
                                    if (v <= 70) {
                                        if (v <= 69) {
                                            if (v <= 68) {
                                                if (v == 68) {
                                                    key[i] = 97;
                                                }
                                            } else {
                                                key[i] = 24;
                                                if (v == 69) {
                                                    key[i] = 115;
                                                }
                                            }
                                        } else {
                                            key[i] = 142;
                                            if (v == 70) {
                                                key[i] = 89;
                                            }
                                        }
                                    } else {
                                        key[i] = 104;
                                        if (v <= 72) {
                                            if (v <= 71) {
                                                if (v == 71) {
                                                    key[i] = 88;
                                                }
                                            } else {
                                                key[i] = 15;
                                                if (v == 72) {
                                                    key[i] = 41;
                                                }
                                            }
                                        } else {
                                            key[i] = 38;
                                            if (v <= 73) {
                                                if (v == 73) {
                                                    key[i] = 117;
                                                }
                                            } else {
                                                key[i] = 198;
                                                if (v == 74) {
                                                    key[i] = 52;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    key[i] = 213;
                                    if (v <= 76) {
                                        if (v <= 75) {
                                            if (v == 75) {
                                                key[i] = 62;
                                            }
                                        } else {
                                            key[i] = 224;
                                            if (v == 76) {
                                                key[i] = 85;
                                            }
                                        }
                                    } else {
                                        key[i] = 96;
                                        if (v == 77) {
                                            key[i] = 39;
                                        }
                                    }
                                }
                            } else {
                                key[i] = 0;
                                if (v <= 80) {
                                    if (v <= 78) {
                                        if (v == 78) {
                                            key[i] = 81;
                                        }
                                    } else {
                                        key[i] = 32;
                                        if (v <= 79) {
                                            if (v == 79) {
                                                key[i] = 63;
                                            }
                                        } else {
                                            key[i] = 109;
                                            if (v == 80) {
                                                key[i] = 35;
                                            }
                                        }
                                    }
                                } else {
                                    key[i] = 247;
                                    if (v == 81) {
                                        key[i] = 79;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    key[i] = 141;
                    if (v <= 87) {
                        if (v <= 84) {
                            if (v <= 82) {
                                if (v == 82) {
                                    key[i] = 65;
                                }
                            } else {
                                key[i] = 131;
                                if (v <= 83) {
                                    if (v == 83) {
                                        key[i] = 123;
                                    }
                                } else {
                                    key[i] = 219;
                                    if (v == 84) {
                                        key[i] = 58;
                                    }
                                }
                            }
                        } else {
                            key[i] = 76;
                            if (v <= 86) {
                                if (v <= 85) {
                                    if (v == 85) {
                                        key[i] = 51;
                                    }
                                } else {
                                    key[i] = 123;
                                    if (v == 86) {
                                        key[i] = 34;
                                    }
                                }
                            } else {
                                key[i] = 10;
                                if (v == 87) {
                                    key[i] = 77;
                                }
                            }
                        }
                    } else {
                        key[i] = 233;
                        if (v <= 91) {
                            if (v <= 89) {
                                if (v <= 88) {
                                    if (v == 88) {
                                        key[i] = 66;
                                    }
                                } else {
                                    key[i] = 152;
                                    if (v == 89) {
                                        key[i] = 105;
                                    }
                                }
                            } else {
                                key[i] = 242;
                                if (v <= 90) {
                                    if (v == 90) {
                                        key[i] = 61;
                                    }
                                } else {
                                    key[i] = 70;
                                    if (v == 91) {
                                        key[i] = 113;
                                    }
                                }
                            }
                        } else {
                            key[i] = 86;
                            if (v <= 92) {
                                if (v == 92) {
                                    key[i] = 80;
                                }
                            } else {
                                key[i] = 166;
                                if (v <= 93) {
                                    if (v == 93) {
                                        key[i] = 104;
                                    }
                                } else {
                                    key[i] = 189;
                                    if (v == 94) {
                                        key[i] = 40;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                key[i] = 223;
                if (v <= 99) {
                    if (v <= 98) {
                        if (v <= 97) {
                            if (v <= 96) {
                                if (v <= 95) {
                                    if (v == 95) {
                                        key[i] = 48;
                                    }
                                } else {
                                    key[i] = 45;
                                    if (v == 96) {
                                        key[i] = 120;
                                    }
                                }
                            } else {
                                key[i] = 180;
                                if (v == 97) {
                                    key[i] = 99;
                                }
                            }
                        } else {
                            key[i] = 3;
                            if (v == 98) {
                                key[i] = 55;
                            }
                        }
                    } else {
                        key[i] = 62;
                        if (v == 99) {
                            key[i] = 106;
                        }
                    }
                } else {
                    key[i] = 157;
                    if (v <= 103) {
                        if (v <= 100) {
                            if (v == 100) {
                                key[i] = 95;
                            }
                        } else {
                            key[i] = 187;
                            if (v <= 101) {
                                if (v == 101) {
                                    key[i] = 74;
                                }
                            } else {
                                key[i] = 33;
                                if (v <= 102) {
                                    if (v == 102) {
                                        key[i] = 54;
                                    }
                                } else {
                                    key[i] = 240;
                                    if (v == 103) {
                                        key[i] = 38;
                                    }
                                }
                            }
                        }
                    } else {
                        key[i] = 167;
                        if (v <= 107) {
                            if (v <= 106) {
                                if (v <= 105) {
                                    if (v <= 104) {
                                        if (v == 104) {
                                            key[i] = 76;
                                        }
                                    } else {
                                        key[i] = 240;
                                        if (v == 105) {
                                            key[i] = 82;
                                        }
                                    }
                                } else {
                                    key[i] = 250;
                                    if (v == 106) {
                                        key[i] = 44;
                                    }
                                }
                            } else {
                                key[i] = 26;
                                if (v == 107) {
                                    key[i] = 119;
                                }
                            }
                        } else {
                            key[i] = 2;
                            if (v <= 110) {
                                if (v <= 108) {
                                    if (v == 108) {
                                        key[i] = 101;
                                    }
                                } else {
                                    key[i] = 88;
                                    if (v <= 109) {
                                        if (v == 109) {
                                            key[i] = 98;
                                        }
                                    } else {
                                        key[i] = 116;
                                        if (v == 110) {
                                            key[i] = 78;
                                        }
                                    }
                                }
                            } else {
                                key[i] = 42;
                                if (v <= 111) {
                                    if (v == 111) {
                                        key[i] = 68;
                                    }
                                } else {
                                    key[i] = 128;
                                    if (v == 112) {
                                        key[i] = 60;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            key[i] = 211;
            if (v <= 120) {
                if (v <= 115) {
                    if (v <= 113) {
                        if (v == 113) {
                            key[i] = 126;
                        }
                    } else {
                        key[i] = 7;
                        if (v <= 114) {
                            if (v == 114) {
                                key[i] = 46;
                            }
                        } else {
                            key[i] = 131;
                            if (v == 115) {
                                key[i] = 112;
                            }
                        }
                    }
                } else {
                    key[i] = 185;
                    if (v <= 117) {
                        if (v <= 116) {
                            if (v == 116) {
                                key[i] = 33;
                            }
                        } else {
                            key[i] = 146;
                            if (v == 117) {
                                key[i] = 42;
                            }
                        }
                    } else {
                        key[i] = 89;
                        if (v <= 119) {
                            if (v <= 118) {
                                if (v == 118) {
                                    key[i] = 93;
                                }
                            } else {
                                key[i] = 213;
                                if (v == 119) {
                                    key[i] = 94;
                                }
                            }
                        } else {
                            key[i] = 217;
                            if (v == 120) {
                                key[i] = 49;
                            }
                        }
                    }
                }
            } else {
                key[i] = 22;
                if (v <= 122) {
                    if (v <= 121) {
                        if (v == 121) {
                            key[i] = 64;
                        }
                    } else {
                        key[i] = 108;
                        if (v == 122) {
                            key[i] = 36;
                        }
                    }
                } else {
                    key[i] = 41;
                    if (v <= 123) {
                        if (v == 123) {
                            key[i] = 67;
                        }
                    } else {
                        key[i] = 161;
                        if (v <= 124) {
                            if (v == 124) {
                                key[i] = 75;
                            }
                        } else {
                            key[i] = 14;
                            if (v <= 125) {
                                if (v == 125) {
                                    key[i] = 56;
                                }
                            } else {
                                key[i] = 204;
                                if (v == 126) {
                                    key[i] = 53;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return [NSString stringWithCString:key encoding:NSUTF8StringEncoding];
}
@end
