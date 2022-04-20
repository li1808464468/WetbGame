//
//  BFGDPRConsent.h
//  BFAppFramework
//
//  Created by Shawn Clovie on 2018/5/17.
//  Copyright © 2018 iHandySoft Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, HSGDPRConsentState) {
	/// 未知状态，同步版本中不会出现
	HSGDPRConsentStateUnknown,
	/// 用户未做出选择
	HSGDPRConsentStateToBeConfirmed,
	/// 用户已同意收集数据，或者用户不在GDPR范围
	HSGDPRConsentStateAccepted,
	/// 用户已拒绝收集数据
	HSGDPRConsentStateDeclined,
};

typedef NS_ENUM(NSUInteger, HSGDPRConsentAlertStyle) {
	/// 用户必须接受的样式
	HSGDPRConsentAlertStyleContinue,
	/// 用户可以接受或拒绝的样式
	HSGDPRConsentAlertStyleAgree,
};

extern NSNotificationName const HSGDPRConsentDidChangeGrantNotification;

@interface BFGDPRConsent : NSObject

+ (instancetype)sharedInstance;

/// 用户是否授权，仅当isEUUser时有效
/// 当未授权时，集成的第三方库如flurry、appsflyer不会记录事件
@property (nonatomic, getter=isGranted) BOOL granted;

/// GDPR状态
@property (nonatomic, readonly) HSGDPRConsentState gdprState;

/// 用户不属于GDPR，或已授权
/// @return gdprState == HSGDPRConsentStateAccepted
- (BOOL)isGDPRAccepted;

/// 以NSLocale.current中的region判断是否为欧盟国家
- (BOOL)isGDPRUser;

/// 用户是否曾在下面显示的对话框中作出选择
- (BOOL)isAlertMadeChoice;

/// 显示确认对话框
/// @param parent 对话框的父ViewController
/// @param style 样式，有细微差别
/// @param moreURL 点击Read后显示的Privacy Policy页面URL
/// @param completion 用户的授权结果回调
- (void)showAlertWithParent:(UIViewController *)parent
					  style:(HSGDPRConsentAlertStyle)style
					moreURL:(NSURL *)moreURL
				 completion:(void(^)(BOOL))completion;
@end

NS_ASSUME_NONNULL_END
