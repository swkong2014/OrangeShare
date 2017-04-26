package cis400.orangeshare;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian on 4/18/2017.
 */

public class Post {
    String uid, author, title, date, info, address, postid;
    double lat, lng;                                        // TODO: Figure out date
    HashMap<String, Boolean> favorites;

    public Post () {

    }

    public Post (String uid, String author, String title, String date,
                 String info, String address, String postid, double lat, double lng, HashMap favorites) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.date = date;
        this.info = info;
        this.address = address;
        this.postid = postid;
        this.lat = lat;
        this.lng = lng;
        this.favorites = favorites;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getAuthor() { return author; }

    public void setAuthor(String name) { author = name; }

    public String getTitle() { return title; }

    public void setTitle(String name) { title = name; }

    public String getInfo() { return info; }

    public void setInfo(String details) { info = details; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getAddress() { return address; }

    public void setAddress(String addr) { address = addr; }

    public String getPostid() { return postid; }

    public void setPostid(String postid) { this.postid = postid; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLng() {return lng; }

    public void setLng(double lng) { this.lng = lng; }

//    public Map<String, Boolean> getFavorites() { return favorites; }
//
//    public void setFavorites(HashMap favorites) { this.favorites = favorites; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("date", date);
        result.put("info", info);
        result.put("address", address);
        result.put("postid", postid);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("favorites", favorites);

        return result;
    }
}
