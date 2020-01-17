#import "CordovaPluginFeedFm.h"
#import <Cordova/CDVPlugin.h>
#import "FeedMediaCore.h"

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

    [pluginResult setKeepCallbackAsBool:true];
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

-(void)setVolume:(CDVInvokedUrlCommand*)command
{
    NSNumber *volume = [command argumentAtIndex:0];
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    NSLog(@"set volume to %fl", [volume floatValue]);
    player.mixVolume = [volume floatValue];
}

-(void)requestClientId:(CDVInvokedUrlCommand*)command
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    NSString *str = [player getClientId];

    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type":@"REQUEST_CLIENT_ID",
        @"payload":@{
                @"ClientID":str
        }
    }];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)setClientId:(CDVInvokedUrlCommand*)command
{
    NSString* cid = [command.arguments objectAtIndex:0];
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player setClientId:cid];
}

-(void)createNewClientID:(CDVInvokedUrlCommand*)command
{
    FMAudioPlayer *player = [FMAudioPlayer sharedPlayer];
    [player createNewClientId];
}

-(void)setActiveStation:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* id = [command.arguments objectAtIndex:0];
    NSLog(@"native setActiveStation method called");

    NSUInteger index = [_player.stationList indexOfObjectPassingTest:^BOOL(FMStation *station, NSUInteger idx, BOOL * _Nonnull stop) {
        NSLog(@"checked for station.identifier %@ and id %@", station.identifier, id);
        return [station.identifier isEqualToString:id];
    }];

    NSLog(@"cannot set station with id, %@ and index %lu", id, index);
    if (index == NSNotFound) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"type":@"SET_ACTIVE_STATION_FAIL",
            @"payload":@""
        }];
        return;
    }

    _player.activeStation = _player.stationList[index];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type":@"SET_ACTIVE_STATION_SUCCESS",
        @"payload":@""
    }];
    NSLog(@"did set station with id, %@ and index %lu", id, index);
}

-(void) initializeWithToken:(CDVInvokedUrlCommand*)command {
    NSString* token = [command.arguments objectAtIndex:0];
    NSString* secret = [command.arguments objectAtIndex:1];
    BOOL enableBackgroundMusic = [[command.arguments objectAtIndex:2] boolValue];
    callbackId = command.callbackId;

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
            @"type":@"INITIALIZE",
            @"payload":@{
                    @"available": @YES,
                    @"stations": [self mapStationListToDictionary:self->_player.stationList],
                    @"activeStationId": station.identifier
            }
        }];
        [pluginResult setKeepCallbackAsBool:true];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    } notAvailable:^{
        CDVPluginResult* pluginResult = nil;
        [pluginResult setKeepCallbackAsBool:true];

        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"type":@"INITIALIZE_FAIL",
            @"payload":@{
                    @"available": @NO
            }
        }];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
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

- (void) onNewClientIDGenerated: (NSNotification*)notification  {
    NSString *str = [notification.userInfo valueForKey:@"client_id"];
    CDVPluginResult* pluginResult = nil;

    if(str != nil){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
            @"type":@"NEW_CLIENT_ID",
            @"payload":@{
                    @"ClientID":str
            }
        }];
        [pluginResult setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    }
}

- (void) onSkipFailedNotification: (NSNotification *)notification {
    CDVPluginResult* pluginResult = nil;

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type":@"SKIP_FAIL",
        @"payload":@""
    }];

    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) onActiveStationDidChangeNotification: (NSNotification *)notification {
    CDVPluginResult* pluginResult = nil;

    // @"station-change"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type":@"STATION_CHANGE",
        @"payload":@{
                @"activeStationId": _player.activeStation.identifier }
    }];
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

    // @"state-change"
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type": [self getStringOfState:state],
        @"payload": @""
    }];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (NSString *) getStringOfState: (NSUInteger)stateNumber {
    switch(stateNumber){
        case 0:
            return @"OFFLINE_ONLY";
            break;
        case 1:
            return @"UNINITIALIZED";
            break;
        case 2:
            return @"UNAVAILABLE";
            break;
        case 3:
            return @"WAITING_FOR_ITEM";
            break;
        case 4:
            return @"READY_TO_PLAY";
            break;
        case 5:
            return @"PLAYING";
            break;
        case 6:
            return @"PAUSED";
            break;
        case 7:
            return @"STALLED";
            break;
        case 8:
            return @"REQUESTING_SKIP";
            break;
        case 9:
            return @"COMPLETE";
            break;
        default :
            return @"UNKNOWN";
    }
}

- (void) onCurrentItemDidBeginPlaybackNotification: (NSNotification *)notification {
    FMAudioItem *current = _player.currentItem;

    long duration = lroundf(_player.currentItemDuration);

    CDVPluginResult* pluginResult = nil;
    // @"play-started"
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
        @"type": @"PLAYBACK_STARTED",
        @"payload": @{
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
@end
