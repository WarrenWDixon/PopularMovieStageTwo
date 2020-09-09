package com.example.android.recyclerview;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static boolean dataRead = false;
    private static List<String> titlesArray      = new ArrayList<>();
    private static List<String> popularityArray  = new ArrayList<>();
    private static List<String> overviewArray    = new ArrayList<>();
    private static List<String> releaseDateArray = new ArrayList<>();
    private static List<String> imagePathArray   = new ArrayList<>();
    private static List<String> posterPathArray  = new ArrayList<>();

    public static void parseMovieJson(String json) {
        Log.d("WWD", "in parseMovieJson input json is " + json);
        // first convert entire response to JSON object
        JSONObject jOBJ = null;
        try {
            jOBJ = new JSONObject(json);
        } catch (JSONException e) {
            Log.d("WWD", "exception on creating JSON object from json string");
            e.printStackTrace();
        }
        Log.d("WWD", "created JSONObject form string " + jOBJ);

        // parse out results array of objects from response
        JSONArray jsonResults = new JSONArray();
        try {
            jsonResults  = jOBJ.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("WWD", "got exception parsing results");
            e.printStackTrace();
        }

        //Log.d("WWD", "jsonResults is " + jsonResults);
        int len = jsonResults.length();
        Log.d("WWD", "length of Results array is " + len);

        // create arrays and Strings to hold data
        List<String> results          = new ArrayList<>();

        String titleString       = "";
        String popularityString  = "";
        String overviewString    = "";
        String posterPathString  = "";
        String releaseDateString = "";

        if (!titlesArray.isEmpty())
            titlesArray.clear();
        if (!popularityArray.isEmpty())
            popularityArray.clear();
        if (!overviewArray.isEmpty())
            overviewArray.clear();
        if (!posterPathArray.isEmpty())
            posterPathArray.clear();
        if (!releaseDateArray.isEmpty())
            releaseDateArray.clear();

        for (int i =0 ; i < len ;i++) {
            try {
                JSONObject result = jsonResults.getJSONObject(i);

                titleString = result.get("title").toString();
                titlesArray.add(titleString);

                popularityString = result.get("popularity").toString();
                popularityArray.add(popularityString);

                overviewString = result.get("overview").toString();
                overviewArray.add(overviewString);

                posterPathString = result.get("poster_path").toString();
                posterPathArray.add(posterPathString);

                releaseDateString = result.get("release_date").toString();
                releaseDateArray.add(releaseDateString);

                Log.d("WWD", "i = " + i + " title is " + titleString);
                Log.d("WWD", "i = " + i + " popularity is " + popularityString);
                Log.d("WWD", "i = " + i + " overview is " + overviewString);
                Log.d("WWD", "i = " + i + " poster path is " + posterPathString);
                Log.d("WWD", "i = " + i + " release date is " + releaseDateString);
                dataRead = true;

            } catch (JSONException e) {
                dataRead = false;
                Log.d("WWD", "error parsing results JSONArray");
                e.printStackTrace();
            }
        }
    }
    public static boolean getDataRead() {
        return dataRead;
    }

    public static String getTitle(int index) {
        return titlesArray.get(index);
    }

    public static String getPopularity(int index) {
        return popularityArray.get(index);
    }

    public static String getOverview(int index) {
        return overviewArray.get(index);
    }

    public static String getReleaseDate(int index) {
        return releaseDateArray.get(index);
    }

    public static String getPosterPath(int index) {
        return posterPathArray.get(index);
    }
}
