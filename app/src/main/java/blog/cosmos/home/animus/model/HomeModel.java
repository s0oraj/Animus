package blog.cosmos.home.animus.model;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class HomeModel {

    private String name,  profileImage, imageUrl, uid, description, id;

    @ServerTimestamp
    private Date timestamp;


    private List<String> likes;





    public HomeModel() {
    }

    public HomeModel(String name, String profileImage, String imageUrl, String uid, String description, String id, Date timestamp, List<String> likes) {
        this.name = name;
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.id = id;
        this.timestamp = timestamp;
        this.likes = likes;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }





  // Overriding of equals and hashcode so that we can eliminate duplicate items in a list.
    // Source of code fix https://stackoverflow.com/questions/6680157/how-to-remove-duplicate-objects-in-a-listmyobject-without-equals-hashcode
    //  answered Jul 13, 2011 at 14:53 Sandeep
    //edited Apr 26, 2021 at 16:42 abby
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof HomeModel)
        {
            HomeModel temp = (HomeModel) obj;
            if(this.name.equals(temp.name) && this.profileImage.equals(temp.profileImage) && this.imageUrl.equals(temp.imageUrl) && this.description.equals(temp.description) && this.uid.equals(temp.uid) && this.id.equals(temp.id))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.name.hashCode() + this.profileImage.hashCode() + this.imageUrl.hashCode() + this.description.hashCode() + this.uid.hashCode() + this.id.hashCode());
    }
}
