//
//  NSDictionary+Merge.h
//
//

#import <Foundation/Foundation.h>

void loadNSDictionaryMergeCategory(void);

@interface NSDictionary (Merge)
+ (NSDictionary *)dictionaryByMerging:(NSDictionary *)dict1 with:(NSDictionary *) dict2;
- (NSDictionary *)dictionaryByMergingWith:(NSDictionary *)dict;
@end
