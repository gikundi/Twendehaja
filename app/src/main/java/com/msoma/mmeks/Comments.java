package com.msoma.mmeks;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Comments extends ListActivity {
    private ProgressDialog pDialog;
    int orgId, portId;

    // URL to get contacts JSON
    private static String url = "http://m-soma.com/twendehaja/comments_selector.php";

    // JSON Node names
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_COMMENT = "comment";
    //private static final String TAG_MOBILE = "mobile";


    // contacts JSONArray
    JSONArray comments = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> Studentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_viewer);

        Studentlist = new ArrayList<HashMap<String,String>>();
        //creating an instance for listview
        ListView lv = getListView();

        // Calling async task to get json


        Intent i = getIntent();
        orgId = i.getIntExtra("organisation_id", 50);


        portId = i.getIntExtra("portfolio_id", 50);


        //showComments();

//        Toast.makeText(getApplicationContext(), "Portfolio_id" + portId + "org id" + orgId,
//                Toast.LENGTH_SHORT).show();

//

        //  Toast.makeText(getApplicationContext(), "Loading Please Wait",
//                Toast.LENGTH_SHORT).show();




        Button comment_here = (Button)findViewById(R.id.button_comment);

        comment_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Comments.this,CommentHere.class).putExtra("organisation_id",  orgId).putExtra("portfolio_id", portId));

            }
        });




//


        new GetSpecifications().execute();
    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetSpecifications extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Comments.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + "?organisation_id=" + orgId + "&portfolio_id=" + portId, ServiceHandler.POST);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node for the object we called student
                    comments = jsonObj.getJSONArray(TAG_COMMENTS);

                    // looping through All Contacts
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject s = comments.getJSONObject(i);

                        String id = s.getString(TAG_ID);

                        String name = s.getString(TAG_NAME);
                        String comment = s.getString(TAG_COMMENT);
                        //String mobile = s.getString(TAG_MOBILE);

                        // tmp hashmap for single student
                        HashMap<String, String> student = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        student.put(TAG_ID, id);
                        student.put(TAG_NAME, name);
                        student.put(TAG_COMMENT,comment);
                        // adding student to contact list
                        Studentlist.add(student);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             *
             * passing the various elements in the Array to the listview items in the layout we created earlier
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Comments.this, Studentlist,
                    R.layout.list_item, new String[] {TAG_NAME,TAG_COMMENT


            }, new int[] {
                    R.id.txtname,R.id.txtcomment});

            setListAdapter(adapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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