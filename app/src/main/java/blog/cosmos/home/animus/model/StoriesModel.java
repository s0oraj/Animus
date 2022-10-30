package blog.cosmos.home.animus.model;

public class StoriesModel {

    String url, id, name, uid, type;

    public StoriesModel() {
    }

    public StoriesModel(String url, String id, String name, String uid, String type) {
        this.url = url;
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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



    // Overriding of equals and hashcode so that we can eliminate duplicate items in a list.
    // Source of code fix https://stackoverflow.com/questions/6680157/how-to-remove-duplicate-objects-in-a-listmyobject-without-equals-hashcode
    //  answered Jul 13, 2011 at 14:53 Sandeep
    //edited Apr 26, 2021 at 16:42 abby
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof StoriesModel)
        {
            StoriesModel temp = (StoriesModel) obj;

            if(this.getId().equals(temp.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.getId().hashCode() );
    }

}