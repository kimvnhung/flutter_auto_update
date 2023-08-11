import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_auto_update/flutter_auto_update.dart';
import 'package:flutter_auto_update/flutter_auto_update_platform_interface.dart';
import 'package:flutter_auto_update/flutter_auto_update_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterAutoUpdatePlatform
    with MockPlatformInterfaceMixin
    implements FlutterAutoUpdatePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterAutoUpdatePlatform initialPlatform = FlutterAutoUpdatePlatform.instance;

  test('$MethodChannelFlutterAutoUpdate is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterAutoUpdate>());
  });

  test('getPlatformVersion', () async {
    FlutterAutoUpdate flutterAutoUpdatePlugin = FlutterAutoUpdate();
    MockFlutterAutoUpdatePlatform fakePlatform = MockFlutterAutoUpdatePlatform();
    FlutterAutoUpdatePlatform.instance = fakePlatform;

    expect(await flutterAutoUpdatePlugin.getPlatformVersion(), '42');
  });
}
