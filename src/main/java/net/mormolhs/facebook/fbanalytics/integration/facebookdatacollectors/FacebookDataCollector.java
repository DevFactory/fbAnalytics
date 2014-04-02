package net.mormolhs.facebook.fbanalytics.integration.facebookdatacollectors;

import facebook4j.Facebook;
import net.mormolhs.facebook.fbanalytics.data.pages.PageTable;
import net.mormolhs.facebook.fbanalytics.integration.facebookclients.FacebookClient;

/**
 * Created by toikonomakos on 3/17/14.
 */
public class FacebookDataCollector {

    FacebookClient facebookClient = new FacebookClient();
    Facebook fb = facebookClient.getConnectionToFacebookApi();
    FacebookPageLoader fbPageLoader = new FacebookPageLoader();


    public PageTable getAllFacebookPagesForAccount() {
        return fbPageLoader.loadPages(fb);
    }


    public PageTable getAllPostsForFacebookPage(PageTable data, String pageId) {
        return fbPageLoader.loadPagePosts(fb, data, pageId);
    }

}
