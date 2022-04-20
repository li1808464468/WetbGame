//
//  BFLibraryVersionControlCenter.h
//
//

#import <Foundation/Foundation.h>
#import "BFLibraryConfig.h"

@interface BFLibrarySessionManager: NSObject
{
}

+ (BFLibrarySessionManager *)sharedInstance;

- (void)startSessionForLibrary:(id<BFLibraryProtocol>)library;


- (BOOL)isFirstTimeLaunchForLibrary:(id<BFLibraryProtocol>)library;
- (BOOL)isFirstTimeLaunchSinceUpdateForLibrary:(id<BFLibraryProtocol>)library;
- (BOOL)isLanguageChangedForLibrary:(id<BFLibraryProtocol>)library;

@end
