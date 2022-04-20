//
//  BFSystem.h
//  iHandyCommonLibrary
//
//  Created by Jian Zhou on 7/14/10.
//  Copyright 2010 iHandySoft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface BFSystem: NSObject
{
}

@property (nonatomic, readonly, getter=isMultitaskingSupported) BOOL multitaskingSupported;
@property (nonatomic, readonly, getter=isCameraFlashAvailable) BOOL cameraFlashAvailable;
@property (nonatomic, readonly, getter=isSMSSupported) BOOL smsSupported;
@property (nonatomic, readonly, getter=isHighResolutionSupported) BOOL highResolutionSupported;
@property (nonatomic, readonly, getter=isJailbroken) BOOL jailbroken;
@property (nonatomic, readonly, getter=isIAPbroken) BOOL iapbroken;

// 这个 API 只适用于没有针对 iPhone6 和 6 Plus 适配的 app，大家写程序尽可能不要使用此 API 了，6 和 6P 的适配用这个 API 已经不合适了
@property (nonatomic, readonly, getter=isScreenTall) BOOL screenTall;

@property (nonatomic, readonly) UIUserInterfaceIdiom userInterfaceIdiom;
@property (nonatomic, copy, readonly) NSString *platform;
@property (nonatomic, copy, readonly) NSString *bundleSeedID;

@property (nonatomic, readonly) BOOL isGoogleBlocked;
@property (nonatomic, readonly) BOOL isFacebookBlocked;


+ (BFSystem *)currentSystem;

/**
 *  需要检测 Google 或者 Facebook 是否被墙前调用此函数，然后读 isGoogleBlocked 和 isFacebookBlocked 两个属性即可，
 *  异步，不保证结果立刻返回，推荐在 didFinishLaunch 里调用此函数，之后每次程序 session 开始都会重新检测。
 */
- (void)startCheckingNetworkBlockStatus;

@end
