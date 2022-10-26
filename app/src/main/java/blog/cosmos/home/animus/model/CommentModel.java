package blog.cosmos.home.animus.model;

public class CommentModel {



    String comment, commentID, postID, uid, name, profileImageUrl;


    public CommentModel(){

    }


    public CommentModel(String comment, String commentID, String postID, String uid, String name, String profileImageUrl) {
        this.comment = comment;
        this.commentID = commentID;
        this.postID = postID;
        this.uid = uid;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }



    // Overriding of equals and hashcode so that we can eliminate duplicate items in a list.
    // Source of code fix https://stackoverflow.com/questions/6680157/how-to-remove-duplicate-objects-in-a-listmyobject-without-equals-hashcode
    //  answered Jul 13, 2011 at 14:53 Sandeep
    //edited Apr 26, 2021 at 16:42 abby
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof CommentModel)
        {
            CommentModel temp = (CommentModel) obj;

            if(this.commentID.equals(temp.commentID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.commentID.hashCode() );
    }


}
