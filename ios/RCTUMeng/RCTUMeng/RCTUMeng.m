
#import "RCTUMeng.h"
#import <React/RCTUtils.h>
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
//#import <UMSocialCore/UMSocialCore.h>
#import <UShareUI/UShareUI.h>

static NSString*  UMS_Title = @"【友盟+】社会化组件U-Share";
static NSString*  UMS_Prog_Title = @"【友盟+】U-Share小程序";
static NSString*  UMS_Text = @"欢迎使用【友盟+】社会化组件U-Share，SDK包最小，集成成本最低，助力您的产品开发、运营与推广！";
static NSString*  UMS_Text_image = @"i欢迎使用【友盟+】社会化组件U-Share，SDK包最小，集成成本最低，助力您的产品开发、运营与推广！";
static NSString*  UMS_Web_Desc = @"W欢迎使用【友盟+】社会化组件U-Share，SDK包最小，集成成本最低，助力您的产品开发、运营与推广！";
static NSString*  UMS_Music_Desc = @"M欢迎使用【友盟+】社会化组件U-Share，SDK包最小，集成成本最低，助力您的产品开发、运营与推广！";
static NSString*  UMS_Video_Desc = @"V欢迎使用【友盟+】社会化组件U-Share，SDK包最小，集成成本最低，助力您的产品开发、运营与推广！";

static NSString*  UMS_THUMB_IMAGE = @"https://mobile.umeng.com/images/pic/home/social/img-1.png";
static NSString*  UMS_IMAGE = @"https://mobile.umeng.com/images/pic/home/social/img-1.png";

static NSString*  UMS_WebLink = @"https://mobile.umeng.com";

static NSString *UMS_SHARE_TBL_CELL = @"UMS_SHARE_TBL_CELL";

@interface RCTUMeng () <UMSocialShareMenuViewDelegate>

@end

@implementation RCTUMeng

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE(UmengShareModule);

RCT_EXPORT_METHOD(shareWebPage:(NSDictionary *)options)
{
    NSString* share_media;
    
    if(options != nil) {
        
        NSLog(@"options:", options);
        
        NSArray *keys = [options allKeys];
        
        if([keys containsObject:@"targetUrl"]) {
            UMS_WebLink = [options objectForKey:@"targetUrl"];
        }

        if([keys containsObject:@"share_media"]) {
            share_media = [options objectForKey:@"share_media"];
        }
        
        if([keys containsObject:@"text"]) {
            UMS_Web_Desc = [options objectForKey:@"text"];
        }
        
        

        if([keys containsObject:@"title"]) {
            UMS_Title = [options objectForKey:@"title"];
        }
        
        
        if([keys containsObject:@"imageurl"]) {
            UMS_THUMB_IMAGE = [options objectForKey:@"imageurl"];
        }
        
        if([keys containsObject:@"fileurl"]) {
            UMS_THUMB_IMAGE = [options objectForKey:@"fileurl"];
        }
        
    }
    int mediaType = [share_media intValue];
    [self shareWebPageToPlatformType:mediaType];
}

//网页分享
- (void)shareWebPageToPlatformType:(UMSocialPlatformType)platformType
{
    //创建分享消息对象
    UMSocialMessageObject *messageObject = [UMSocialMessageObject messageObject];
    
    //创建网页内容对象
    NSString* thumbURL =  UMS_THUMB_IMAGE;
    UMShareWebpageObject *shareObject = [UMShareWebpageObject shareObjectWithTitle:UMS_Title descr:UMS_Web_Desc thumImage:thumbURL];
    //设置网页地址
    shareObject.webpageUrl = UMS_WebLink;
    
    //分享消息对象设置分享内容对象
    messageObject.shareObject = shareObject;
    
        //调用分享接口
        [[UMSocialManager defaultManager] shareToPlatform:platformType messageObject:messageObject currentViewController:nil completion:^(id data, NSError *error) {
                NSString * result=@"111111111";
            if (error) {
                UMSocialLogInfo(@"************Share fail with error %@*********",error);
                result=error.description;
            }else{
                if ([data isKindOfClass:[UMSocialShareResponse class]]) {
                    UMSocialShareResponse *resp = data;
                    //分享结果消息
                    UMSocialLogInfo(@"response message is %@",resp.message);
                    result=resp.message;
                    //第三方原始返回的数据
                    UMSocialLogInfo(@"response originalResponse data is %@",resp.originalResponse);
                    
                }else{
                    UMSocialLogInfo(@"response data is %@",data);
                }
            }
            [self sendEventWithName:@"onShareResult" body:@{@"action": result,@"media": @(platformType),}];
            
        }];
    }
/**
 导出事件名称
 */
- (NSArray<NSString *> *)supportedEvents
{
    return @[@"onShareResult"];
}


- (NSDictionary *)constantsToExport
{
    return @{
             @"SHARE_MEDIA": @{
                     @"weixin":@(UMSocialPlatformType_WechatSession),
                     @"weixincircle": @(UMSocialPlatformType_WechatTimeLine),
                     @"weixinfavorite": @(UMSocialPlatformType_WechatFavorite),
                     @"qq": @(UMSocialPlatformType_QQ),
                     @"qzone": @(UMSocialPlatformType_Qzone),
                     @"sina": @(UMSocialPlatformType_Sina),
                    }
             };
}

@end
