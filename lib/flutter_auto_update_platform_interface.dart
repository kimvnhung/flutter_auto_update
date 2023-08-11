import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_auto_update_method_channel.dart';

abstract class FlutterAutoUpdatePlatform extends PlatformInterface {
  /// Constructs a FlutterAutoUpdatePlatform.
  FlutterAutoUpdatePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterAutoUpdatePlatform _instance = MethodChannelFlutterAutoUpdate();

  /// The default instance of [FlutterAutoUpdatePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterAutoUpdate].
  static FlutterAutoUpdatePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterAutoUpdatePlatform] when
  /// they register themselves.
  static set instance(FlutterAutoUpdatePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
