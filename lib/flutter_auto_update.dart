import 'dart:io';

import 'package:flutter/services.dart';

import './desktop.dart';
import './fetch_github.dart' as gb;

class FlutterAutoUpdate {
  static const MethodChannel _channel = MethodChannel('flutter_auto_update');

  static Future<String> getDocumentsFolder() async {
    if (Platform.isWindows) {
      return (await _channel.invokeMethod("getDocumentsFolder")).toString();
    }
    return "";
  }

  static Future<Map<dynamic, dynamic>> fetchGithub(
      String user, String packageName,
      {String fileType = ".exe", String githubToken = ""}) async {
    Map<dynamic, dynamic> res = {};
    if (Platform.isAndroid) {
      return await _channel.invokeMethod("fetchGithub", {
        "user": user,
        "githubToken": githubToken,
        "packageName": packageName
      });
    } else if (Platform.isWindows) {
      List<dynamic>? packageInfo =
          await _channel.invokeListMethod("getProductAndVersion");
      if (packageInfo != null) {
        res = await gb.fetchGithub(
          user,
          packageName,
          "application/octet-stream",
          packageInfo[1],
          packageInfo[0] + fileType,
        );
      }
      return res;
    }
    return res;
  }

  static Future<void> downloadAndUpdate(
    String url, {
    String githubToken = "",
    bool isFromService = false,
  }) async {
    if (Platform.isAndroid) {
      await _channel.invokeMethod("downloadAndUpdate", {
        "url": url,
        "githubToken": githubToken,
        "isFromService": isFromService,
      });
    } else if (Platform.isWindows) {
      String? filePath = await downloadFile(
          Uri.parse(url),
          "${await _channel.invokeMethod("getDownloadFolder")}\\",
          url.split("/").last);
      if (filePath != null) {
        if ((await _channel
                .invokeMethod("runFileWindows", {"filePath": filePath})) >
            32) {
          exit(0);
        }
      }
    }
  }
}
