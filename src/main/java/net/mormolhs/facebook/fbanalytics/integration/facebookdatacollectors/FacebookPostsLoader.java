package net.mormolhs.facebook.fbanalytics.integration.facebookdatacollectors;

import facebook4j.Facebook;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import net.mormolhs.facebook.fbanalytics.data.posts.DataRow;
import net.mormolhs.facebook.fbanalytics.data.posts.DataTable;
import net.mormolhs.facebook.fbanalytics.integration.facebookclients.FacebookQueryExecutor;
import net.mormolhs.facebook.fbanalytics.resources.GlobalParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by toikonomakos on 3/23/14.
 */
public class FacebookPostsLoader {

    FacebookQueryExecutor facebookQueryExecutor = new FacebookQueryExecutor();

    public DataTable loadPagePostData(Facebook fb, String pageId) {
        DataTable pagePostData = new DataTable();
        List<DataRow> dataRows = new ArrayList<DataRow>();
        for (String postId : this.getPagePostIds(fb, pageId)) {
            dataRows.add(loadPostData(fb, postId));
        }
        pagePostData.setTable(dataRows);
        return pagePostData;
    }

    private DataRow loadPostData(Facebook fb, String postId) {
        DataRow postData = new DataRow();
        Map<String, String> results = this.getPostGenericInfo(fb, postId);
        postData.setThumbnail(results.get("attachment") != null ? results.get("attachment") : "N/A");
        postData.setPostText(results.get("message") != null ? results.get("message") : "N/A");
        postData.setPostLink(this.getPostLinks(fb, postId));
        postData.setLikes(results.get("likes"));
        postData.setComments(results.get("comments"));
        postData.setShares(results.get("shares"));
        postData.setType(results.get("type"));
        postData.setDatePosted(results.get("created_time"));
        postData.setReached((this.getPostReach(fb, postId)));
        postData.setClicks((this.getPostClicks(fb, postId)));
        return postData;
    }

//    TODO: Add date range parameters in epoch format
    private List<String> getPagePostIds(Facebook fb, String pageId) {
        JSONArray jsonArray = null;
        if (GlobalParameters.DATE_FROM.equals("") || GlobalParameters.DATE_TO.equals("")){
            jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT post_id FROM stream WHERE source_id='" + pageId + "' and type in (80,247,46) order by created_time desc LIMIT " + GlobalParameters.RESULT_SIZE);
        } else {
            jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT post_id FROM stream WHERE source_id='" + pageId + "' AND created_time < '" + GlobalParameters.DATE_TO + "' AND created_time > '" + GlobalParameters.DATE_FROM + "' and type in (80,247,46) order by created_time desc LIMIT " + GlobalParameters.RESULT_SIZE);
        }
        List<String> listOfPostIds = new ArrayList<String>();
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    if (!jsonArray.getJSONObject(i).get("post_id").equals(null)) {
                        listOfPostIds.add(jsonArray.getJSONObject(i).get("post_id").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return listOfPostIds;
    }

    private Map<String, String> getPostGenericInfo(Facebook fb, String postId) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT attachment,message,like_info.like_count,comment_info.comment_count,share_count,type,created_time FROM stream WHERE post_id='" + postId + "' and type in (80,247,46)");
        JSONObject jsonObject;
        Map<String, String> results = new HashMap<String, String>();
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                jsonObject = jsonArray.getJSONObject(0);
                results.put("message", (jsonObject.get("message")).toString());
                results.put("likes", (((JSONObject) jsonObject.get("like_info")).get("like_count")).toString());
                results.put("comments", (((JSONObject) jsonObject.get("comment_info")).get("comment_count")).toString());
                results.put("shares", (jsonObject.get("share_count")).toString());
                results.put("type", (jsonObject.get("type")).toString());
                results.put("created_time", (jsonObject.get("created_time")).toString());
                if (((JSONObject) jsonObject.get("attachment")).has("media")) {
                    results.put("attachment", ((JSONObject) ((JSONArray) ((JSONObject) jsonObject.get("attachment")).get("media")).get(0)).get("src").toString());
                }
                return results;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return results;
        }
        return results;
    }

    private String getPostLinks(Facebook fb, String postId) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "select url from object_url where url in (SELECT strip_tags(attachment.media.href) FROM stream WHERE post_id='" + postId + "')");
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                if (jsonArray.getJSONObject(0).equals(null)) {
                    return "N/A";
                } else {
                    return jsonArray.getJSONObject(0).get("url").toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return "N/A";
        }
        return null;
    }

    private String getPostReach(Facebook fb, String postId) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT metric, value FROM insights WHERE metric='post_impressions' AND period=0 AND object_id='" + postId + "'");
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                if (jsonArray.getJSONObject(0).get("value").equals(null)) {
                    return "N/A";
                } else {
                    return jsonArray.getJSONObject(0).get("value").toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return "N/A";
        }
        return null;
    }

    private String getPostClicks(Facebook fb, String postId) {
        JSONArray jsonArray = facebookQueryExecutor.executeFQL(fb, "SELECT metric, value FROM insights WHERE metric='post_engaged_users' AND period=0 AND object_id='" + postId + "'");
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                if (jsonArray.getJSONObject(0).get("value").equals(null)) {
                    return "N/A";
                } else {
                    return jsonArray.getJSONObject(0).get("value").toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return "N/A";
        }
        return null;
    }

}
