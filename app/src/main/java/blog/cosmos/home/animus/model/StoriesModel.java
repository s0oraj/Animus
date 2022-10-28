package blog.cosmos.home.animus.model;

public class StoriesModel {

    String videoUrl, id, name, uid;

    public StoriesModel() {
    }

    public StoriesModel(String videoUrl, String id, String name, String uid) {
        this.videoUrl = videoUrl;
        this.id = id;
        this.name = name;
        this.uid = uid;
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
