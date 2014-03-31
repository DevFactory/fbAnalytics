package net.mormolhs.facebook.fbanalytics.integration.facebookdatacollectors;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.ResponseList;
import net.mormolhs.facebook.fbanalytics.data.pages.PageData;
import net.mormolhs.facebook.fbanalytics.data.pages.PageTable;
import net.mormolhs.facebook.fbanalytics.utils.HttpGetClient;
import net.mormolhs.facebook.fbanalytics.utils.JSonParser;
import org.apache.commons.httpclient.URIException;

import java.util.*;

/**
 * Created by toikonomakos on 3/23/14.
 */
public class FacebookPageLoader {

    FacebookPostsLoader fbPostsLoader = new FacebookPostsLoader();

    public PageTable loadPages(Facebook fb) {
        PageTable data = new PageTable();
        Map<String, PageData> unsortedPagesDataMap = new HashMap<String, PageData>();
        ResponseList<Account> accounts = getFacebookUserAccounts(fb);
        for (Account account: accounts) {
            unsortedPagesDataMap.put(account.getId(), this.loadPageData(fb, account.getId(), false));
            unsortedPagesDataMap.get(account.getId()).setPageName(account.getName());
        }
//        TODO:  Check if needs sorting
        data.setPageDetails(unsortedPagesDataMap);
        return data;
    }

    public PageTable loadPagePosts(Facebook fb,PageTable data, String pageId) {
        data.getPageDetails().get(pageId).setPostData(this.loadPageData(fb,pageId,true).getPostData());
        return data;
    }

    private PageData loadPageData(Facebook fb, String pageId, boolean loadPageDetails) {
        PageData pageData = new PageData();
        HttpGetClient httpGetClient = new HttpGetClient();
        String responseGeneralData = null;
        String responseProfilePicture = null;
        try {
            responseGeneralData = httpGetClient.sendGetRequest("http://graph.facebook.com/" + pageId);
            responseProfilePicture = httpGetClient.sendGetRequest("http://graph.facebook.com/" + pageId + "/?fields=picture.height(150).width(150)");
        } catch (URIException e) {
            e.printStackTrace();
        }
        pageData.setLikes(JSonParser.getValueFromJson(responseGeneralData, "likes")!=null ? JSonParser.getValueFromJson(responseGeneralData, "likes") : "0");
        pageData.setTalkingAbout(JSonParser.getValueFromJson(responseGeneralData, "talking_about_count")!=null ? JSonParser.getValueFromJson(responseGeneralData, "talking_about_count") : "0");
        pageData.setPageProfilePicture(JSonParser.getValueFromJson(responseProfilePicture, "url").replace("https","http").replace("\\","").replaceAll("\"",""));
        pageData.setPageCoverPicture(JSonParser.getValueFromJson(responseGeneralData, "source")!=null ? JSonParser.getValueFromJson(responseGeneralData, "source").replace("https","http").replace("\\","").replaceAll("\"","") : "N/A");
        if (loadPageDetails) {
            pageData.setPostData(fbPostsLoader.loadPagePostData(fb, pageId));
        }
        return pageData;
    }

    private ResponseList<Account> getFacebookUserAccounts(Facebook fb) {
        try {
            return fb.getAccounts();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <String, Integer extends Comparable<? super Integer>> SortedSet<Map.Entry<String, Integer>> entriesSortedByValues(Map<String, Integer> map) {
        SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<Map.Entry<String, Integer>>(
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
