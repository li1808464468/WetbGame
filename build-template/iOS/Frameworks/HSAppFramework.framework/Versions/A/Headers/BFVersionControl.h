//
//  BFVersionControl.h
//  BFAppFramework
//s
//

#import <Foundation/Foundation.h>

@interface BFVersionControl : NSObject

+ (NSString *)appVersion; // 当前 app 版本，ShortVersion
+ (NSString *)osVersion; // 当前系统版本

+ (BOOL)isFirstLaunchSinceInstallation;
+ (BOOL)isFirstLaunchSinceOSUpgrade;
+ (BOOL)isFirstLaunchSinceUpgrade;

+ (BOOL)isFirstSessionSinceInstallation;
+ (BOOL)isFirstSessionSinceOSUpgrade;
+ (BOOL)isFirstSessionSinceUpgrade;

+ (BOOL)isUpdateUser;

+ (NSComparisonResult)compareVersionString1:(NSString *)version1 toVersionString2:(NSString *)version2;

@end
