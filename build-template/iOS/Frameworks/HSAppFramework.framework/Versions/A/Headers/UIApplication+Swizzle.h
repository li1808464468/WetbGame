//
//  UIApplication+Swizzle.h
//  iHandyCommonLibrary
//
//  Created by Song Liu on 13-11-24.
//
//

#import <UIKit/UIKit.h>

@interface UIApplication (Swizzle)

/**
 *  定制后的打开 URL 方式，调用此函数可以放开对此 URL 的拦截
 *
 *  @param url 要打开的 URL
 *
 *  @return 是否打开
 */
- (BOOL)hsOpenURL:(nonnull NSURL *)url;

/**
 *  定制后的打开 URL 方式，调用此函数可以放开对此 URL 的拦截
 *
 *  @param url 要打开的 URL
 *
 *  @completion 返回是否打开成功, 在主线程回调
 */
- (void)hsOpenUrl:(nonnull NSURL *)url completion:(nullable void (^)(BOOL success))completion;

/**
 *  设置是否拦截对 openURL: 的调用，
 *
 *  @param isOpenURLAllowed 是否拦截的标志位
 */
- (void)setIsOpenURLAllowed:(BOOL)isOpenURLAllowed;

/**
 *  设置是否拦截国际电话，默认拦截
 *
 *  @param isInternationalCallAllowed 是否允许国际电话，默认值 NO
 */
- (void)setIsInternationalCallAllowed:(BOOL)isInternationalCallAllowed;


- (void)swizzle;
@end
