package blog.cosmos.home.animus.model;

public class StoriesModel {

    String videoUrl, id, name, uid, videoName;

    public StoriesModel() {
    }


    public StoriesModel(String videoUrl, String id, String name, String uid, String videoName) {
        this.videoUrl = videoUrl;
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
