#import "RCTBridgeModule.h"
#import <UMSocialCore/UMSocialCore.h>


@interface RCTUMeng : NSObject <RCTBridgeModule>

- (void)shareWebPageToPlatformType: (UMSocialPlatformType)platformType;

@end
