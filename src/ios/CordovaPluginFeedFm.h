#import <Cordova/CDVPlugin.h>
#import "FeedMedia/FeedMediaCore.h"

@interface CordovaPluginFeedFm : CDVPlugin

@property (nonatomic, weak) FMAudioPlayer *player;

- (void)echo:(CDVInvokedUrlCommand*)command;
- (void)play:(CDVInvokedUrlCommand*)command;
- (void)pause:(CDVInvokedUrlCommand*)command;

@end
