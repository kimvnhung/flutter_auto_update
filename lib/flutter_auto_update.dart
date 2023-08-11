
import 'flutter_auto_update_platform_interface.dart';

class FlutterAutoUpdate {
  Future<String?> getPlatformVersion() {
    return FlutterAutoUpdatePlatform.instance.getPlatformVersion();
  }
}
