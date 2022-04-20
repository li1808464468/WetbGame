//
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#ifdef DEBUG
#define DebugLog(format, ...) NSLog(format, ## __VA_ARGS__)
#else
#define DebugLog(format, ...)
#endif

#define RGBA(R, G, B, A) ([UIColor colorWithRed:(R)/255.0 green:(G)/255.0 blue:(B)/255.0 alpha:(A)])

#define RGBCOLOR_HEX(hexColor) [UIColor colorWithRed:(((hexColor >> 16) & 0xFF))/255.0f\
                                               green:(((hexColor >> 8) & 0xFF))/255.0f\
                                                blue:((hexColor & 0xFF))/255.0f\
                                              alpha:1]

@interface BFUtils : NSObject {

}


/**
 *   判断当前系统版本是否早于 version
 *
 *  @param version version
 *
 *  @return 是否早于
 */
+ (BOOL)isOSVersionPriorTo:(float)version;


/**
 *   在系统版本早于 version 的情况下执行 priorBlock，大于等于 version 的版本上执行 geqBlock
 *
 *  @param priorBlock priorBlock
 *  @param version    version
 *  @param geqBlock   geqBlock
 */
+ (void)executeBlock:(void(^)(void))priorBlock ifOSVersionPriorTo:(float)version otherwiseExecuteBlock:(void(^)(void))geqBlock;

/**
 *  JSON serialize
 *
 *  @param object object 需要序列化的 json 对象， NSArray 或者 NSDictionary
 *
 *  @return 反序列化之后的 json 串
 */
+ (NSString *)jsonStringWithObject:(id)object;


/**
 *  JSON deserialize
 *
 *  @param jsonString 需要解析的 json 串
 *
 *  @return 反序列化后的 json 对象
 */
+ (id)jsonObjectWithString:(NSString *)jsonString;

/**
 *  获取 Document 目录的完整路径
 *
 *  @return Document 目录的完整路径
 */
+ (NSString *)documentDirectoryPath;

+ (NSString *)encodedStringForString:(NSString *)string;
+ (NSString *)decodedStringForString:(NSString *)string;

/**
 *  获取程序 Entitlements 中 com.apple.security.application-groups 中的值，我们只配置一项，
 *
 libCommons:
   Entitlements:
     AppleSecurityApplicationGroup: "apple.security.application-groups"
 *  @return AppleSecurityApplicationGroup 对应的值
 */
+ (NSString *)appleSecurityApplicationGroup;

// 返回相对于格林威治标准时间的偏移秒数
+ (NSInteger)timeZone;

// 本地读取手机配置的国家/地区简写 e.g. US
+ (NSString *)region;

+ (NSString *)deviceId;

// 目前只返回@"Apple"
+ (NSString *)deviceBrand;

//// e.g. @"iPhone7", @"iPhone 7 Plus"
//+ (NSString *)deviceModel;

// deviceToken, return nil if not fetched
+ (NSString *)deviceToken;

// AppId, return nil if not configured in .ya
+ (NSString *)appId;

// 国家/地区简写 e.g. US      目前实现同region
+ (NSString *)countryCode;

@end

@interface UIImage (HSTallScreen)

+ (UIImage *)imageNamed:(NSString *)name withTallScreenVersion:(BOOL)withTallVersion;

@end

@interface NSDictionary (Localization)

/**
 *  获取字典中键为 key 的本地化值
 *  如果 key 对应的值为字符串，则直接返回字符串
 *  如果 key 对应的值为字典，则返回该字典中按当前语言情况配置的值，优先级为 locale > language > default > en
 *  @return 键为 key 所对应的本地化值
 */

- (NSString *)localizedStringForKey:(id)key;

@end
