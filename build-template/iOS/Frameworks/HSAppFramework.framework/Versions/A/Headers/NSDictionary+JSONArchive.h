//
//  NSDictionary+JSONArchive.
//

#import <Foundation/Foundation.h>

void loadNSDictionaryJSONArchiveCategory(void);

@interface NSDictionary (JSONArchive)

+ (NSDictionary *)dictionaryWithJSONArchiveData:(NSData *)data;

@end
