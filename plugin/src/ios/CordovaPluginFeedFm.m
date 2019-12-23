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
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Arg was null"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)play:(CDVInvokedUrlCommand*)command
{
    NSLog(@"native play method called");
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player play];
}

-(void)pause:(CDVInvokedUrlCommand*)command
{
    NSLog(@"native pause method called");
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player pause];
}

-(void)skip:(CDVInvokedUrlCommand*)command
{
    NSLog(@"native skip method called");
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player skip];
}

-(void)stop:(CDVInvokedUrlCommand*)command
{
    NSLog(@"native stop method called");
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player stop];
}

-(void)setVolume: (float)volume
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    player.mixVolume = volume;
}

-(void)setClientId: (NSString*)cid
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player setClientId:cid];
}

-(void)createNewClientID
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player createNewClientId];
}

-(void)setActiveStation:(NSString *)id
{
    CDVPluginResult* pluginResult = nil;
    NSString *errorMessage = [NSString stringWithFormat:@"Cannot set active station to %@ because no station found with that id", id];

    NSUInteger index = [_player.stationList indexOfObjectPassingTest:^BOOL(FMStation *station, NSUInteger idx, BOOL * _Nonnull stop) {
        return [station.identifier isEqualToString:id];
    }];

    if (index == NSNotFound) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMessage];
        return;
    }

    _player.activeStation = _player.stationList[index];
}

- (void) onNewClientIDGenerated: (NSNotification*)notification  {
    NSString *str = [notification.userInfo valueForKey:@"client_id"];
    CDVPluginResult* pluginResult = nil;

    if(str != nil){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{@"ClientID":str}];
    }
}

- (NSArray<NSDictionary *> *) mapStationListToDictionary: (FMStationArray *) inStations {
    NSMutableArray<NSDictionary *> *outStations = [[NSMutableArray alloc] init];

    for (FMStation *station in inStations) {
        [outStations addObject:@{
            @"id": station.identifier,
            @"name": station.name,
            @"options": station.options
        }];
    }

    return outStations;
}

-(void) initializeWithToken:(NSString *)token secret:(NSString *)secret enableBackgroundMusic:(BOOL)enableBackgroundMusic {
    FMLogSetLevel(FMLogLevelDebug);

    _player = FMAudioPlayer.sharedPlayer;
    _player.disableSongStartNotifications = YES;

    [FMAudioPlayer setClientToken:token secret:secret];

    FMAudioPlayer.sharedPlayer.doesHandleRemoteCommands = enableBackgroundMusic;

    [FMAudioPlayer.sharedPlayer whenAvailable:^{
        CDVPluginResult* pluginResult = nil;

        // the active station is not set at this time, so assume it is the first station
        FMStation *station = [self->_player.stationList firstObject];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"available": @YES,
            @"stations": [self mapStationListToDictionary:self->_player.stationList],
            @"activeStationId": station.identifier
        }];
    } notAvailable:^{
        CDVPluginResult* pluginResult = nil;

        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"available": @NO
        }];
    }];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onNewClientIDGenerated:) name:FMAudioPlayerNewClientIdAvailable object:_player];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onPlaybackStateDidChangeNotification:) name:FMAudioPlayerPlaybackStateDidChangeNotification object:_player];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onActiveStationDidChangeNotification:) name:FMAudioPlayerActiveStationDidChangeNotification object:_player];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onCurrentItemDidBeginPlaybackNotification:) name:FMAudioPlayerCurrentItemDidBeginPlaybackNotification object:_player];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onSkipFailedNotification:) name:FMAudioPlayerSkipFailedNotification object:_player];
}

// RN thing
- (NSArray<NSString *> *)supportedEvents {
    return @[
        @"newClientID",
        @"availability",
        @"state-change",
        @"station-change",
        @"play-started",
        @"skip-failed"
    ];
}

// RN thing
- (NSDictionary *)constantsToExport
{
    return @{
        @"audioPlayerPlaybackStateOfflineOnly": @(FMAudioPlayerPlaybackStateOfflineOnly),
        @"audioPlayerPlaybackStateUninitialized": @(FMAudioPlayerPlaybackStateUninitialized),
        @"audioPlayerPlaybackStateUnavailable": @(FMAudioPlayerPlaybackStateUnavailable),
        @"audioPlayerPlaybackStateWaitingForItem": @(FMAudioPlayerPlaybackStateWaitingForItem),
        @"audioPlayerPlaybackStateReadyToPlay": @(FMAudioPlayerPlaybackStateReadyToPlay),
        @"audioPlayerPlaybackStatePlaying": @(FMAudioPlayerPlaybackStatePlaying),
        @"audioPlayerPlaybackStatePaused": @(FMAudioPlayerPlaybackStatePaused),
        @"audioPlayerPlaybackStateStalled": @(FMAudioPlayerPlaybackStateStalled),
        @"audioPlayerPlaybackStateRequestingSkip": @(FMAudioPlayerPlaybackStateRequestingSkip)
    };
};

@end
