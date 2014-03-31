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

import java.util.*;

/**
 * Created by toikonomakos on 3/23/14.
 */
public class FacebookPageLoader {

    FacebookQueryExecutor facebookQueryExecutor = new FacebookQueryExecutor();
    FacebookPostsLoader fbPosts = new FacebookPostsLoader();

    public PageTable loadPages(Facebook fb) {
        PageTable data = new PageTable();
        Map<String, PageData> unsortedPagesDataMap = new HashMap<String, PageData>();
        for (int i = 0; i < getFacebookUserAccounts(fb).size(); i++) {
            String tmpPageId = getFacebookUserAccounts(fb).get(i).getId();
            unsortedPagesDataMap.put(tmpPageId, this.loadPageData(fb, tmpPageId, false));
        }
//        Sort pagesMap by fan_count TODO: Check if map should be sorted
//        Put mapEntries into descoped tempMap
        Map<String, Integer> tempMapForSorting = new HashMap<String, Integer>();
        for (Map.Entry<String, PageData> entry : unsortedPagesDataMap.entrySet()) {
            tempMapForSorting.put(entry.getKey(), Integer.valueOf(entry.getValue().getLikes()));
        }
//        Sort unsortedMap into sortedMap
        SortedSet<Map.Entry<String, Integer>> sortedSet = entriesSortedByValues(tempMapForSorting);
        Map<String, PageData> sortedPagesDataMap = new HashMap<String, PageData>();
        Iterator iterator = sortedSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
            sortedPagesDataMap.put(entry.getKey(), unsortedPagesDataMap.get(entry.getKey()));
        }
        data.setPageDetails(sortedPagesDataMap);
        return data;
    }

    public PageData loadPageData(Facebook fb, String pageId, boolean loadPageDetails) {
        PageData pageData = new PageData();
        Map<String, String> pageDataMap = this.getPageNameBasedOnId(fb, pageId);
        pageData.setPageName(pageDataMap.get("name"));
        if (pageDataMap.get("name").equals("Testronochannel")){
        pageData.setLikes("10000");
        }   else {
        pageData.setLikes(pageDataMap.get("fan_count"));
        }
        pageData.setTalkingAbout(pageDataMap.get("talking_about_count"));
        pageData.setPageProfilePicture(pageDataMap.get("pic"));
        pageData.setPageCoverPicture(pageDataMap.get("pic_cover"));
        if (loadPageDetails) {
            pageData.setPostData(fbPosts.loadPagePostData(fb, pageId));
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

    private Map<String, String> getPageNameBasedOnId(Facebook fb, String pageId) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT name,fan_count,talking_about_count,pic,pic_cover.source FROM page WHERE page_id='" + pageId + "'");
        Map<String, String> results = new HashMap<String, String>();
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                results.put("name", jsonArray.getJSONObject(0).get("name").toString());
                results.put("fan_count", jsonArray.getJSONObject(0).get("fan_count").toString());
                results.put("talking_about_count", jsonArray.getJSONObject(0).get("talking_about_count").toString());
                results.put("pic", jsonArray.getJSONObject(0).get("pic").toString());
                if (!jsonArray.getJSONObject(0).get("pic_cover").equals(null)) {
                    results.put("pic_cover", ((JSONObject) jsonArray.getJSONObject(0).get("pic_cover")).get("source").toString());
                } else {
                    results.put("pic_cover", "N/A");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return results;
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
