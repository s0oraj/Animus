package blog.cosmos.home.animus.model;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotificationModel {

    String id, notification, userPhotoUrl;

    @ServerTimestamp
    Date time;

    public NotificationModel() {
    }


    public NotificationModel(String id, String notification, String userPhotoUrl, Date time) {
        this.id = id;
        this.notification = notification;
        this.userPhotoUrl = userPhotoUrl;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}