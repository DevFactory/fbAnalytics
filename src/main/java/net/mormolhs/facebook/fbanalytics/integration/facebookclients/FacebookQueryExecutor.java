package net.mormolhs.facebook.fbanalytics.integration.facebookclients;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.internal.org.json.JSONArray;

import java.util.Calendar;

/**
 * Created by toikonomakos on 3/17/14.
 */
public class FacebookQueryExecutor {

    public JSONArray executeFQL(Facebook facebook, String query) {
        JSONArray jsonArray = null;
        long counterStart = Calendar.getInstance().getTimeInMillis();
        try {
            System.out.println("Executing FQL query: " + query);
            jsonArray = facebook.executeFQL(query);
        } catch (FacebookException e) {
            System.out.println(e.getMessage());
        }
        long counterEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("FQL query execution took: " + String.valueOf(counterEnd - counterStart) + " milliseconds");
        return jsonArray;
    }
}
