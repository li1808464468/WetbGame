//
//  NSData+Compression.h
//

#import <Foundation/Foundation.h>

void loadNSDataCompressionCatergory(void);

@interface NSData (Compression)

- (NSData *)compressedData;

- (NSData *)decompressedData;

@end
