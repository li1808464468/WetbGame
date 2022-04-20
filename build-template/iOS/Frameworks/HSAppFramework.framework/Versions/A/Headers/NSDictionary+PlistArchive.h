//
//  NSDictionary+PlistArchive.h
//

#import <Foundation/Foundation.h>

void loadNSDictionaryPlistArchiveCategory(void);

@interface NSDictionary (PlistArchive)

+ (NSDictionary *)dictionaryWithPlistArchiveFile:(NSString *)file;
+ (NSDictionary *)dictionaryWithPlistArchiveData:(NSData *)data;

- (BOOL)writeToPlistArchiveFile:(NSString *)path atomically:(BOOL)useAuxiliaryFile;
- (NSData *)plistArchiveData;

@end

@interface NSMutableDictionary (PlistArchive)

+ (NSMutableDictionary *)dictionaryWithPlistArchiveFile:(NSString *)file;
+ (NSMutableDictionary *)dictionaryWithPlistArchiveData:(NSData *)data;

@end
