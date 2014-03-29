package net.mormolhs.facebook.fbanalytics.data.pages;


import net.mormolhs.facebook.fbanalytics.data.posts.DataTable;

/**
 * Created by toikonomakos on 3/19/14.
 */
public class PageData {

    String pageName;
    String pageProfilePicture;
    String pageCoverPicture;
    String likes;
    String talkingAbout;
    DataTable postData = new DataTable();

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageProfilePicture() {
        return pageProfilePicture;
    }

    public void setPageProfilePicture(String pageProfilePicture) {
        this.pageProfilePicture = pageProfilePicture;
    }

    public String getPageCoverPicture() {
        return pageCoverPicture;
    }

    public void setPageCoverPicture(String pageCoverPicture) {
        this.pageCoverPicture = pageCoverPicture;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public DataTable getPostData() {
        return postData;
    }

    public void setPostData(DataTable postData) {
        this.postData = postData;
    }

    public String getTalkingAbout() {
        return talkingAbout;
    }

    public void setTalkingAbout(String talkingAbout) {
        this.talkingAbout = talkingAbout;
    }
}
