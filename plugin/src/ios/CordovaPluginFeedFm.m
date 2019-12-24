#import "CordovaPluginFeedFm.h"
#import <Cordova/CDVPlugin.h>
#import "FeedMedia/FeedMediaCore.h"

@implementation CordovaPluginFeedFm {
    NSString* callbackId;
}

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
    callbackId = command.callbackId;
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

-(void)setVolume:(CDVInvokedUrlCommand*)command
{
    NSNumber *volume = [command argumentAtIndex:0];
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    NSLog(@"set volume to %fl", [volume floatValue]);
    player.mixVolume = [volume floatValue];
}

-(void)requestClientId
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    NSString *str = [player getClientId];

    CDVPluginResult* pluginResult = nil;
    // @"newClientID"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{@"ClientID":str}];
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

-(void)setActiveStation:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* id = [command.arguments objectAtIndex:0];
    NSLog(@"native setActiveStation method called");

    NSString *errorMessage = [NSString stringWithFormat:@"Cannot set active station to %@ because no station found with that id", id];

    NSUInteger index = [_player.stationList indexOfObjectPassingTest:^BOOL(FMStation *station, NSUInteger idx, BOOL * _Nonnull stop) {
        return [station.identifier isEqualToString:id];
    }];

    if (index == NSNotFound) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMessage];
        NSLog(@"cannot set thing with id, %@ and index %lu", id, index);
        return;
    }

    _player.activeStation = _player.stationList[index];
    NSLog(@"did set thing with id, %@ and index %lu", id, index);
}

- (void) onNewClientIDGenerated: (NSNotification*)notification  {
    NSString *str = [notification.userInfo valueForKey:@"client_id"];
    CDVPluginResult* pluginResult = nil;

    if(str != nil){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{@"ClientID":str}];
    }
}

-(void) initializeWithToken:(CDVInvokedUrlCommand*)command {
    NSString* token = [command.arguments objectAtIndex:0];
    NSString* secret = [command.arguments objectAtIndex:1];
    BOOL enableBackgroundMusic = [command.arguments objectAtIndex:2];
    
    FMLogSetLevel(FMLogLevelDebug);
    NSLog(@"native initializeWithToken called");

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
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } notAvailable:^{
        CDVPluginResult* pluginResult = nil;

        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"available": @NO
        }];
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
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

- (void) onSkipFailedNotification: (NSNotification *)notification {
    CDVPluginResult* pluginResult = nil;

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"skip failed"];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) onActiveStationDidChangeNotification: (NSNotification *)notification {
    CDVPluginResult* pluginResult = nil;

    // @"station-change"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"activeStationId": _player.activeStation.identifier }];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) onPlaybackStateDidChangeNotification: (NSNotification *)notification {
    FMAudioPlayerPlaybackState state =_player.playbackState;

    // this might cause a notice when the state doesn't actually change, but I think
    // it's worth it to weed this state out
    if (state == FMAudioPlayerPlaybackStateComplete) {
        state = FMAudioPlayerPlaybackStateReadyToPlay;
    }

    CDVPluginResult* pluginResult = nil;
    // @"state-change"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"state": @(state) }];;
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) onCurrentItemDidBeginPlaybackNotification: (NSNotification *)notification {
    FMAudioItem *current = _player.currentItem;

    long duration = lroundf(_player.currentItemDuration);

    CDVPluginResult* pluginResult = nil;
    // @"play-started"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"play": @{
                @"id": current.playId,
                @"title": current.name,
                @"artist": current.artist,
                @"album": current.album,
                @"metadata": current.metadata,
                @"canSkip": @(_player.canSkip),
                @"duration": @(duration)
        }
    }];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

// helper
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
