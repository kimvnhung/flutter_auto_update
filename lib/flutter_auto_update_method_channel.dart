import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_auto_update_platform_interface.dart';

/// An implementation of [FlutterAutoUpdatePlatform] that uses method channels.
class MethodChannelFlutterAutoUpdate extends FlutterAutoUpdatePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_auto_update');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
