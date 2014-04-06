package net.mormolhs.facebook.fbanalytics.integration.facebookdatacollectors;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.ResponseList;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import net.mormolhs.facebook.fbanalytics.data.pages.PageData;
import net.mormolhs.facebook.fbanalytics.data.pages.PageTable;
import net.mormolhs.facebook.fbanalytics.integration.facebookclients.FacebookQueryExecutor;
import net.mormolhs.facebook.fbanalytics.utils.HttpGetClient;
import net.mormolhs.facebook.fbanalytics.utils.JSonParser;
import org.apache.commons.httpclient.URIException;

import java.util.*;

/**
 * Created by toikonomakos on 3/23/14.
 */
public class FacebookPageLoader {

    FacebookQueryExecutor facebookQueryExecutor = new FacebookQueryExecutor();
    FacebookPostsLoader fbPostsLoader = new FacebookPostsLoader();

    public PageTable loadPages(Facebook fb) {
        PageTable data = new PageTable();
        Map<String, PageData> unsortedPagesDataMap = new HashMap<String, PageData>();
        ResponseList<Account> accounts = getFacebookUserAccounts(fb);
        for (Account account : accounts) {
            unsortedPagesDataMap.put(account.getId(), this.loadPageData(fb, account.getId(), false));
            unsortedPagesDataMap.get(account.getId()).setPageName(account.getName());
        }
//        TODO:  Check if needs sorting
        data.setPageDetails(unsortedPagesDataMap);
        return data;
    }

    public PageTable loadPagePosts(Facebook fb, PageTable data, String pageId) {
        data.getPageDetails().get(pageId).setPostData(this.loadPageData(fb, pageId, true).getPostData());
        return data;
    }

    private PageData loadPageData(Facebook fb, String pageId, boolean loadPageDetails) {
        PageData pageData = new PageData();
        HttpGetClient httpGetClient = new HttpGetClient();
        String responseGeneralData = null;
        String responseProfilePicture = null;
        try {
            responseGeneralData = httpGetClient.sendGetRequest("http://graph.facebook.com/" + pageId);
            responseProfilePicture = httpGetClient.sendGetRequest("http://graph.facebook.com/" + pageId + "/?fields=picture.height(130).width(130)");
            if (responseGeneralData.contains("Unsupported get request.") || responseProfilePicture.contains("Unsupported get request.")) {
                System.out.println("Possible cause: Facebook Page is not published");
                System.out.println("fgInsightsSniffer will try to fetch data with FQL.");
                this.getPageDataWithFql(fb, pageId, pageData);
            } else {
                this.getPageDataFromJSON(pageData, responseGeneralData, responseProfilePicture);
            }
        } catch (URIException e) {
            System.out.println("JSON fetching failed due to:" + e.getMessage());
            System.out.println("...but data have been collected through FQL");
        }
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

    private PageData getPageDataFromJSON(PageData pageData, String responseGeneralData, String responseProfilePicture) {
        pageData.setLikes(JSonParser.getValueFromJson(responseGeneralData, "likes") != null ? JSonParser.getValueFromJson(responseGeneralData, "likes") : "0");
        pageData.setTalkingAbout(JSonParser.getValueFromJson(responseGeneralData, "talking_about_count") != null ? JSonParser.getValueFromJson(responseGeneralData, "talking_about_count") : "0");
        if (JSonParser.getValueFromJson(responseProfilePicture, "url") == null) {
            pageData.setPageProfilePicture("N/A");
        }
        pageData.setPageProfilePicture(JSonParser.getValueFromJson(responseProfilePicture, "url").replace("https", "http").replace("\\", "").replaceAll("\"", ""));
        pageData.setPageCoverPicture(JSonParser.getValueFromJson(responseGeneralData, "source") != null
                ? JSonParser.getValueFromJson(responseGeneralData, "source").replace("https", "http").replace("\\", "").replaceAll("\"", "").replace("}","")
                : "N/A");

        return pageData;
    }

    private PageData getPageDataWithFql(Facebook fb, String pageId, PageData pageData) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT name,fan_count,talking_about_count,pic,pic_cover.source FROM page WHERE page_id='" + pageId + "'");
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                pageData.setLikes(jsonArray.getJSONObject(0).get("fan_count").toString());
                pageData.setTalkingAbout(jsonArray.getJSONObject(0).get("talking_about_count").toString());
                pageData.setPageProfilePicture(jsonArray.getJSONObject(0).get("pic").toString());
                if (!jsonArray.getJSONObject(0).get("pic_cover").equals(null)) {
                    pageData.setPageCoverPicture(((JSONObject) jsonArray.getJSONObject(0).get("pic_cover")).get("source").toString());
                } else {
                    pageData.setPageCoverPicture("N/A");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return pageData;
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
