package blog.cosmos.home.animus.model;

public class Users {

    private String email, name, profileImage, uid;

    public Users() {
    }

    public Users(String email, String name, String profileImage, String uid) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
