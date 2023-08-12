package com.example.flutter_auto_update;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FileDownloader extends Thread{
    private static final String TAG = FileDownloader.class.getName();
    static final int chunkSize = 1024;

    private final String url;
    private final File file;
    private final String githubToken;

    public int downloaded = 0;
    public Exception exception;

    public FileDownloader(String url,String githubToken, File file){
        this.url = url;
        this.file = file;
        this.githubToken = githubToken;
    }

    public void run(){
        try {
            URL requestUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();
            connection.setRequestProperty("Accept-Encoding",
                    "gzip, deflate, br");
            connection.setRequestProperty("User-Agent", "auto-update");
            connection.setRequestProperty("Accept", "application/octet-stream");
            if(githubToken != null && !githubToken.isEmpty()){
                connection.setRequestProperty("Authorization", "Bearer "+githubToken);
            }
            if (connection.getResponseCode() == 200) {
                try {
                    BufferedInputStream inputStream = new BufferedInputStream(
                            connection.getInputStream());
                    FileOutputStream out = new FileOutputStream(file);

                    byte[] chunk = new byte[FileDownloader.chunkSize];
                    int read = 0;
                    while ((read = inputStream.read(chunk)) > 0) {
                        out.write(chunk, 0, read);
                    }

                    out.flush();
                    out.close();

                    inputStream.close();
                    downloaded = 1;
                } finally {
                    connection.disconnect();
                }
            }
            Log.d(TAG,"DownloadResponseCode "+connection.getResponseCode());
            Log.d(TAG,"DownloadResponseMessage "+connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
            exception = e;
            downloaded = -1;
        }
    }
}