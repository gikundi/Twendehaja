package com.msoma.mmeks;

/**
 * Created by gikundi on 2/3/15.
 */


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;


import com.msoma.mmeks.adapter.FeedListAdapter;
import com.msoma.mmeks.app.AppController;
import com.msoma.mmeks.common.BaseActivity;
import com.msoma.mmeks.data.FeedItem;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    protected static final String LOG_TAG = null;
    private ListView listView;
    private FeedListAdapter listAdapter;
  //  private Toolbar toolbar;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://m-soma.com/twendehaja/port.php";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);

     //   toolbar = (Toolbar) findViewById(R.id.app_bar);
      //  setSupportActionBar(toolbar);




        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_toile);



        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
      //  getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#127aef")));
       // getActionBar().setIcon(
              //  new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            //fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            //   for (int i = 0; i < feedArray.length(); i++) {

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("organisation_id"));
               // item.setPortId(feedObj.getInt("portfolio_id"));
               //tem.setName(feedObj.getString("OrganisationName"));
                //int id=item.getId();
                // Image might be null sometimes

                item.setLocation(feedObj.getString("location"));
                item.setOrganisationName(feedObj.getString("OrganisationName"));
                item.setProfilePic(feedObj.getString("profilePic"));


                // url might be null sometimes


                feedItems.add(item);
            }


            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:



                    startActivity(new Intent(MainActivity.this, Help.class));


                    return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Created by gikundi on 2/3/15.
     */
    public static class BookWedding {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }





}