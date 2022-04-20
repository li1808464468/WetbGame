//
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

typedef NS_ENUM(NSInteger, LocationSource) {
    kLocationSource_NoValue = -1,
    kLocationSource_IP = 0,
    kLocationSource_Device = 1,
};

typedef NS_ENUM(NSInteger, LocationCategory) {
    kLocationCategory_NoValue = -1,
    kLocationCategory_GPS = 0,
    kLocationCategory_LastApp = 1,
    kLocationCategory_Default = 2,
};

typedef NS_ENUM(NSInteger, BFLocationManagerErrorCode)
{
    kBFLocationManagerErrorCode_LocationServiceNotEnabled = -1003,
};
@class BFLocationManager;

@interface BFLocationManager : NSObject

@property (nonatomic, readonly) CLLocation *location;
@property (nonatomic, readonly) CLLocation *cityCenterLocation;
@property (nonatomic, readonly, copy) NSString *city;
@property (nonatomic, readonly, copy) NSString *state;
@property (nonatomic, readonly, copy) NSString *country;
@property (nonatomic, readonly, copy) NSString *sublocality;
@property (nonatomic, readonly, copy) NSString *neighborhood;
@property (nonatomic, readonly, copy) NSString *countryCode;
@property (nonatomic, readonly) NSInteger timezone;
@property (nonatomic, readonly) LocationCategory category;

/**
 *  位置使用授权状态读取
 *
 *  @return 首选状态
 */
- (CLAuthorizationStatus)authStatus;

/**
 *  获取地理位置
 *
 *  @param source                      获取类型：设备/IP
 *  @param locationFetchedHandler      locationFetchedHandler 经纬度获取结束后回调：location
 *  @param geographyInfoFetchedHandler geographyInfoFetchedHandler 地理信息获取结束后回调：city，state，country
 */
- (void)fetchLocationWithLocationSource:(LocationSource)source
                 locationFetchedHandler:(void(^)(BOOL success, NSError *error, BFLocationManager *locationManager))locationFetchedHandler
            geographyInfoFetchedHandler:(void(^)(BOOL success, NSError *error, BFLocationManager *locationManager))geographyInfoFetchedHandler;

/**
 *  停止获取
 */
- (void)stopFetching;

/**
 设置默认的 location。
 如果设置了，那么在获取 device 或者 ip location 失败时，会返回该 location。

 @param defaultLocation 默认的 location
 */
- (void)setDefaultLocation:(CLLocation *)defaultLocation;

@end
