package net.mormolhs.facebook.fbanalytics.data.pages;

import java.util.*;

/**
 * Created by toikonomakos on 3/19/14.
 */
public class PageTable {

    Map<String, PageData> pageDetails = new HashMap<String, PageData>();
    boolean showLikes;
    boolean showLinks;
    Date fromDate;
    Date toDate;

    public Map<String, PageData> getPageDetails() {
        return pageDetails;
    }

    public void setPageDetails(Map<String, PageData> pageDetails) {
        this.pageDetails = pageDetails;
    }

    public boolean isShowLikes() {
        return showLikes;
    }

    public void setShowLikes(boolean showLikes) {
        this.showLikes = showLikes;
    }

    public boolean isShowLinks() {
        return showLinks;
    }

    public void setShowLinks(boolean showLinks) {
        this.showLinks = showLinks;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
