package com.example.flutter_auto_update;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Github extends Thread {
    private static final String TAG = Github.class.getName();
    private static final String url = "https://api.github.com/repos/%s/%s/releases/latest";

    private final String userName;
    private final String githubToken;
    private final String packageName;
    private final String type;
    private final String fileName;
    private final String versionCode;

    public GithubResults githubResults;
    public Exception exception;
    public int fetched = 0;

    public Github(
            String userName, String githubToken, String packageName, String type, String fileName, String versionName) {
        this.userName = userName;
        this.githubToken = githubToken;
        this.packageName = packageName;
        this.type = type;
        this.fileName = fileName;
        this.versionCode = versionName;
        Log.d(TAG, "Github: " + userName + " " + githubToken + " " + packageName + " " + type + " " + fileName + " "
                + versionName);
    }

    @Override
    public void run() {
        try {
            URL requestUrl = new URL(String.format(url, userName, packageName));
            Log.d(TAG, "resquestUrl " + requestUrl.toString());
            HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();
            connection.setRequestProperty("User-Agent", "auto-update");

            if (githubToken != null && !githubToken.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + githubToken);
            }

            if (connection.getResponseCode() == 200) {
                try {
                    InputStreamReader inputStream = new InputStreamReader(
                            connection.getInputStream());
                    StringBuffer sb = new StringBuffer();
                    char[] chunk = new char[FileDownloader.chunkSize];
                    int read = 0;
                    while ((read = inputStream.read(chunk)) > 0) {
                        sb.append(chunk, 0, read);
                    }

                    String jsonData = sb.toString();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    Log.d(TAG, "tag_name " + jsonObject.get("tag_name") + " " + versionCode);
                    if (!jsonObject.get("tag_name").equals(versionCode)) {
                        String[] versionCodes = versionCode.split("\\.");
                        String[] tagNames = jsonObject.getString("tag_name").split("\\.");
                        boolean isUpdatable = false;
                        for (int i = 0; i < tagNames.length; i++) {
                            int targetVer = Integer.parseInt(tagNames[i]);
                            int curVer = Integer.parseInt(versionCodes[i]);
                                                        if (targetVer != curVer) {
                                if (targetVer > curVer) {
                                    isUpdatable = true;
                                }
                                break;
                            }
                        }
                        if (isUpdatable) {
                            JSONArray assets = (JSONArray) jsonObject.get("assets");
                            for (int i = 0; i < assets.length(); i++) {
                                JSONObject asset = assets.getJSONObject(i);
                                if (asset.get("content_type").toString().equals(type) &&
                                        asset.get("name").toString().equals(fileName)) {
                                    githubResults = new GithubResults(
                                            // asset.get("browser_download_url").toString(),
                                            asset.get("url").toString(),
                                            jsonObject.get("body").toString(),
                                            jsonObject.get("tag_name").toString());
                                    fetched = 1;
                                    break;
                                }
                            }
                        }else{
                            fetched = 2;
                        }
                    }
                    else {
                        fetched = 2;
                    }
                } finally {
                    connection.disconnect();
                }
            }
            Log.d(TAG, "response code " + connection.getResponseCode());
            Log.d(TAG, "response message " + connection.getResponseMessage());
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());
            exception = e;
            fetched = -1;
        }
    }

}
