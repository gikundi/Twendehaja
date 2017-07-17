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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.msoma.mmeks.adapter.FeedListAdapterSecond;
import com.msoma.mmeks.app.AppController;
import com.msoma.mmeks.common.BaseActivity;
import com.msoma.mmeks.data.FeedItem;

public class ListViewCompany extends BaseActivity {


    TextView commentCount;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected static final String LOG_TAG = null;
    private ListView listView;
    private FeedListAdapterSecond listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://m-soma.com/twendehaja/portfolioCompany.php";

    public int OrgIdOne,portIdOne;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_main);



        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setIcon(R.drawable.ic_toile);



        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapterSecond(this, feedItems);
        listView.setAdapter(listAdapter);





        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
     //   getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#127aef")));
        //getActionBar().setIcon(
          //      new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();

        Intent i = getIntent();
        int orgId   = i.getIntExtra("organisation_id", 50);




       int portId = i.getIntExtra("portfolio_id", 50);


         //String org =String.valueOf(orgId).trim();
        OrgIdOne=orgId;

        portIdOne=portId;


//        Toast.makeText(getApplicationContext(), "Portfolio_id" +portIdOne,
//                Toast.LENGTH_SHORT).show();


        Entry entry = cache.get(URL_FEED+"?organisation_id="+OrgIdOne);
        if (entry != null) {
            // fetch the data from cache
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
                    URL_FEED+"?organisation_id="+OrgIdOne, null, new Response.Listener<JSONObject>() {

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


        Button book = (Button) findViewById(R.id.button_book);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Book Event",
                        Toast.LENGTH_SHORT).show();

                // startActivity(new Intent(ListViewCompany.this, BookWedding.class));

                startActivity(new Intent(ListViewCompany.this,BookEvent.class).putExtra("organisation_id", OrgIdOne));


            }
        });

    }


    @Override protected int getLayoutResource() {
        return R.layout.activity_laid;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("organisation_id"));
                item.setPortId(feedObj.getInt("portfolio_id"));
                item.setName(feedObj.getString("name"));
                //int id=item.getId();
                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));
                item.setCommentCount(feedObj.getString("commentCount"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
                item.setUrl(feedUrl);

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
            case R.id.action_settings:

                    startActivity(new Intent(ListViewCompany.this, Help.class));

                    return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStop(){
        super.onStop();
    }

    //Fires after the OnStop() state
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



