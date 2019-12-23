#import "CordovaPluginFeedFm.h"
#import <Cordova/CDVPlugin.h>
#import "FeedMedia/FeedMediaCore.h"

@implementation CordovaPluginFeedFm

- (void)echo:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)play:(CDVInvokedUrlCommand*)command
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    NSLog(@"native play method calle");
    [player play];
}

@end
