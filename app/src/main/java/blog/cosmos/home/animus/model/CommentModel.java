package blog.cosmos.home.animus.model;

public class CommentModel {



    String comment, commentID, postID, uid, name, profileImageUrl;


    public CommentModel(){

    }

    public CommentModel(String comment, String commentID, String postID, String uid){
        this.comment = comment;
        this.commentID = commentID;
        this.postID = postID;
        this.uid= uid;
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
}
