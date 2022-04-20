/*
 * Copyright 2018, Oath Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "H5UnityPlugin.h"
#import <HSAppFramework/HSAppFramework.h>
#import "H5Adapter/NativeAPI.h"

@implementation H5UnityPlugin

static NSString * const AF_STATUS_KEY = @"af_status";
static NSString * const AF_AGENCY_KEY = @"agency";
static NSString * const AF_SITEID_KEY = @"af_siteid";
static NSString * const AF_MEDIA_SOURCE_KEY = @"media_source";
static NSString * const AF_AD_SET_ID_KEY = @"adset_id";
static NSString * const AF_AD_SET_NAME_KEY = @"adset";
static NSString * const AF_CAMPAIGN_KEY = @"campaign";
static NSString * const AF_CAMPAIGN_ID_KEY = @"campaign_id";
static NSString * const AF_ADSET_ID_KEY = @"adset";

static H5UnityPlugin *_sharedInstance;

+ (H5UnityPlugin*) shared
{
    static dispatch_once_t once;
    static id _sharedInstance;
    dispatch_once(&once, ^{
        NSLog(@"Creating H5UnityPlugin shared instance");
        _sharedInstance = [[H5UnityPlugin alloc] init];
    });
    return _sharedInstance;
}

-(id)init {
    self = [super init];
    return self;
}


char* h5StrDup(const char* str)
{
    if (!str)
        return NULL;
    
    return strcpy((char*)malloc(strlen(str) + 1), str);
    
}

NSString* strToNSString(const char* str)
{
    if (!str)
        return [NSString stringWithUTF8String: ""];
    
    return [NSString stringWithUTF8String: str];
}


NSString * encodeJSON(id value) {
    if (value == nil) {
        return @"";
    }
    NSData *data = [NSJSONSerialization dataWithJSONObject:value options:0 error:nil];
    return [NSString.alloc initWithData:data encoding:NSUTF8StringEncoding] ?: @"";
}


extern "C" {
    
    const char* mNotifyInited()
    {
        //todo   remove splash view
        [[NSNotificationCenter defaultCenter] postNotificationName:kBFNotificationName_RemoveLogoView object:nil];
        return h5StrDup("");
    }
    
    
    const char* mGetCustomUserID()
    {
        //todo   return adid
        return h5StrDup("");
    }
    
    
    const char* mGetDeviceId()
    {
        return h5StrDup([UIDevice.currentDevice.identifierForVendor.UUIDString UTF8String]);
    }
    
    const void mGotoMarket() {
        NSURL *url  = [NSURL URLWithString:[[BFConfig sharedInstance].data valueForKeyPath:@"Application.App.Store"]];
        [[UIApplication sharedApplication]openURL:url options:@{UIApplicationOpenURLOptionsSourceApplicationKey : @YES} completionHandler:^(BOOL success) {
        }];
    }
    
    const void mOnFinish() {
        
    }
    
    const void mShowToast(const char *content){
        NSString *contentStr = strToNSString(content);
        
        [[NativeAPI sharedInstance] showToast:contentStr];
    }
    
    
    const void mSetBGMMuted(bool mute){
        
    }
    
    const void mShareOverFacebook(){
        
    }
    
    
    const char* mGetConfigList(const char *keypath)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        return [value isKindOfClass:NSArray.class] ? h5StrDup([encodeJSON(value) UTF8String]) : NULL;
    }
    
    const char* mGetConfigMap(const char *keypath)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        return [value isKindOfClass:NSDictionary.class] ? h5StrDup([encodeJSON(value) UTF8String]) : NULL;
    }
    
    const char* mGetConfigInt(const char *keypath, const char *defaultValue)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        if (!value) {
            return h5StrDup(defaultValue);
        }
        
        if ([value isKindOfClass:NSNumber.class]) {
            return h5StrDup([((NSNumber *) value).stringValue UTF8String]);
        }
        return h5StrDup([[value description] UTF8String]);
    }
    
    const char* mGetConfigFloat(const char *keypath, const char *defaultValue)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        if (!value) {
            return h5StrDup(defaultValue);
        }
        if ([value isKindOfClass:NSNumber.class]) {
            return h5StrDup([((NSNumber *) value).stringValue UTF8String]);
        }
        return h5StrDup([[value description] UTF8String]);
    }
    
    const char* mGetConfigString(const char *keypath, const char *defaultValue)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        if (!value) {
            return h5StrDup(defaultValue);
        }
        if ([value isKindOfClass:NSString.class]) {
            return h5StrDup([value UTF8String]);
        }
        return h5StrDup([[value description] UTF8String]);
    }
    
    const char* mGetConfigBoolean(const char *keypath, bool defaultValue)
    {
        id value = [BFConfig.sharedInstance.data valueForKeyPath:strToNSString(keypath)];
        if (!value) {
            return defaultValue ? h5StrDup("TRUE") : h5StrDup("FALSE");
        }
        if ([value boolValue]) {
            return h5StrDup("TRUE");
        } else {
            return h5StrDup("FALSE");
        }
    }
    
    const char* mGetPackageName()
    {
        NSString * appName = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleIdentifier"];
        
        return h5StrDup([appName UTF8String]);
    }
    
    const void mOnAppsFlyerTrackerCallback(const char *conversion){
        NSString *json = strToNSString(conversion);
        NSDictionary *args = json.length > 0 ? [NSJSONSerialization JSONObjectWithData:[json dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil] : nil;
        if (![args isKindOfClass:NSDictionary.class]) {
            args = nil;
        }
        
        [NativeAPI.sharedInstance storeAFData:args];
    }
    
    const void mOnUpgradeFinish() {
        
    }
    
    
    const bool mShouldUpgrade() {
        return true;
    }
    
    const bool mIsFirstDay() {
        return [[NSDate date] isEqualToDate:[BFSessionManager.sharedInstance firstSessionStartTime]];
    }
    
    const char* mGetNetworkStatus() {
        BFReachabilityNetworkStatus status = BFReachability.reachabilityForInternetConnection.currentReachabilityStatus;
        switch (status) {
            case BFReachabilityNotReachable:
                return h5StrDup("");
            case BFReachabilityReachableViaWiFi:
                return h5StrDup("wifi");
            case BFReachabilityReachableViaWWAN:
                return h5StrDup("wwan");
        }
    }
    
    const char* mGetSystemApiLevel() {
        return h5StrDup([[BFVersionControl osVersion] UTF8String]);
    }
    
    const void mDoRibrator(const int time, const int amplitude) {
        if ([UIDevice currentDevice].systemVersion.doubleValue >= 10.0){
            UIImpactFeedbackGenerator *impactFeedBack = [[UIImpactFeedbackGenerator alloc] initWithStyle:UIImpactFeedbackStyleHeavy];
            [impactFeedBack prepare];
            [impactFeedBack impactOccurred];
        }
        
        //另一种震动方式
        //        #import <AudioToolbox/AudioServices.h>
        //        Actuate `Peek` feedback (weak boom)
        //        Actuate `Pop` feedback (strong boom)
        //        Actuate `Nope` feedback (series of three weak booms)
        //        kSystemSoundID_Vibrate  长震，来电震
        //        AudioServicesPlaySystemSound(1519);
    }
    
    const void mLogEventWithJSON(const char* eventName, const char* enableES, const char* enableFabric, const char* para) {
        BOOL fab = [strToNSString(enableFabric) boolValue];
        BOOL es = [strToNSString(enableES) boolValue];
        NSString *event = strToNSString(eventName);
        NSString *paras = strToNSString(para);
        
        NSError * error;
        NSDictionary *args = paras.length > 0 ? [NSJSONSerialization JSONObjectWithData:[paras dataUsingEncoding:NSUTF8StringEncoding] options:0 error:&error] : nil;
        if (![args isKindOfClass:NSDictionary.class]) {
            args = nil;
        }
        
        if (es) {
            NSMutableDictionary *esDic = [[NSMutableDictionary alloc] init];
            [esDic setValue:event forKey:@"h5Action"];
            if (args != nil) {
                [esDic setValue:args forKey:@"h5Para"];
            }
            
            [[NativeAPI sharedInstance] logSnapshoWithJson:esDic];
        }
    }

    const bool mGameCenterBind() {
        return [[NativeAPI sharedInstance] isGameCenterBind];
    }

    const void mBindGameCenter() {
        [[NativeAPI sharedInstance] authenticateLocalPlayer];
    }

    const void mUpdateScore(const long score) {
        [[NativeAPI sharedInstance] updateScore:score];
    }

    const void mShowLeaderboard() {
        [[NativeAPI sharedInstance] showLeaderboard];
    }
    
    const char* mGetAppOpenTime(){
        return h5StrDup([[NativeAPI sharedInstance] getAppOpenTime]);
    }
    
    const char* mGetNativeData(){
        return h5StrDup([encodeJSON([[NativeAPI sharedInstance] getNativeData]) UTF8String]);
    }
    
    const char* mGetAdId(){
        return h5StrDup([[NativeAPI sharedInstance] getAdId]);
    }
}

@end
