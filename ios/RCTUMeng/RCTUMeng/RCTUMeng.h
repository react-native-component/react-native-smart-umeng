#import "RCTBridgeModule.h"
#import <UMSocialCore/UMSocialCore.h>
#import "RCTEventEmitter.h"


@interface RCTUMeng : RCTEventEmitter <RCTBridgeModule>

- (void)shareWebPageToPlatformType: (UMSocialPlatformType)platformType;

@end
