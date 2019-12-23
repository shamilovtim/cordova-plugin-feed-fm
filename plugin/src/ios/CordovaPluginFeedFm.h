#import <Cordova/CDVPlugin.h>

@interface CordovaPluginFeedFm : CDVPlugin

@property (nonatomic, weak) FMAudioPlayer *player;

- (void)echo:(CDVInvokedUrlCommand*)command;
- (void)play:(CDVInvokedUrlCommand*)command;

@end