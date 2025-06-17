
package com.example.flutter_auto_update;

import java.util.HashMap;

public class GithubResults{
    private final String assetUrl;
    private final String body;
    private final String tag;
    private final String createdAt;
    private final String updatedAt;

    public GithubResults(String assetUrl, String body, String tag, String createdAt, String updatedAt) {
        this.assetUrl = assetUrl;
        this.body = body;
        this.tag = tag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public GithubResults(){
        this.assetUrl = "";
        this.body = "";
        this.tag = "";
        this.createdAt = "";
        this.updatedAt = "";
    }

    static GithubResults upToDate(){
        return new GithubResults("up-to-date", "", "", "", "");
    }

    public HashMap<String, String> toMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("assetUrl", assetUrl);
        map.put("body", body);
        map.put("tag", tag);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public String getBody() {
        return body;
    }

    public String getTag() {
        return tag;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
