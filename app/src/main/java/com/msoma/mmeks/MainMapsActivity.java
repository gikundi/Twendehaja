package com.msoma.mmeks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.msoma.mmeks.maps.AlertDialogManager;
import com.msoma.mmeks.app.AppController;
import com.msoma.mmeks.maps.ConnectionDetector;
import com.msoma.mmeks.maps.CustomListAdapter;
import com.msoma.mmeks.maps.GPSTracker;
import com.msoma.mmeks.maps.Movie;
import com.msoma.mmeks.maps.ServiceHandler;
import com.msoma.mmeks.maps.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainMapsActivity extends Activity implements SlidingUpPanelLayout.PanelSlideListener {
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private ListView mListView;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    private View mTransparentHeaderView;
    private View mTransparentView;
    private View mSpaceView;

    private MapFragment mMapFragment;

    private GoogleMap mMap;

    private ProgressDialog pDialog;
    private GoogleMap googleMap;
    GPSTracker gps;
    Double setLat, setLong;

    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;

    double mLatitude = 0;
    double mLongitude = 0;
    public static String queryLat;
    public static String queryLong;

    public static String TAG_ID = "map_id";
    public static String TAG_MAPS = "feeds";
    public static String TAG_LAT = "Latitude";
    public static String TAG_DESCRIPTION = "description";
    public static String TAG_LONG = "Longitude";
    private static String url = "http://www.m-soma.com/twendehaja/twendehaja.php";


    JSONArray maps = null;

    private String mGlobalLatitude="";
    private String mGlobalLongitude="";
    private String Latt;
    private String Lonn;


    private static final String TAG = MainMapsActivity.class.getSimpleName();

    // Movies json url
    private static final String url2 = "http://www.m-soma.com/twendehaja/mark.php";
    private List<Movie> movieList = new ArrayList<Movie>();
    private CustomListAdapter adapter;
    JSONArray feedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);

        pDialog = new ProgressDialog(MainMapsActivity.this);
        gps = new GPSTracker(MainMapsActivity.this);


        if (gps.canGetLocation()) {
            setLat = gps.getLatitude();
            setLong = gps.getLongitude();

            mGlobalLatitude= String.valueOf(setLat);
            mGlobalLongitude= String.valueOf(setLong);
            try {
                Latt = URLEncoder.encode(mGlobalLatitude, "UTF-8");
                Lonn = URLEncoder.encode(mGlobalLongitude,"UTF-8");
                GetListData(Latt,Lonn);




            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //Toast.makeText(
            //     getApplicationContext(),
            // "Your Location is - \n Lat: " + mGlobalLatitude + "\nLong: "
            //     + mGlobalLongitude, Toast.LENGTH_LONG).show();

            mMapFragment = MapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
            fragmentTransaction.commit();
            initilizeMap();

        } else {
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        mListView = (ListView) findViewById(R.id.list);

        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);

        int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height);

        mSlidingUpPanelLayout.setPanelHeight(mapHeight); // you can use different height here
        mSlidingUpPanelLayout.setScrollableView(mListView, mapHeight);

        mSlidingUpPanelLayout.setPanelSlideListener(this);

        // transparent view at the top of ListView
       mTransparentView = findViewById(R.id.transparentView);

        // init header view for ListView
        mTransparentHeaderView = LayoutInflater.from(this).inflate(R.layout.transparent_header_view, null, false);
        mSpaceView = mTransparentHeaderView.findViewById(R.id.space);





        mListView.addHeaderView(mTransparentHeaderView);


        adapter = new CustomListAdapter(this, movieList);
        mListView.setAdapter(adapter);





        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //mSlidingUpPanelLayout.collapsePane();

               final Movie data=movieList.get(position-1);
               double latitude=data.getRating();
               double longitude=data.getGenre();
               //Toast.makeText(MainMapsActivity.this,"Position " + position + " Latitude= "+ latitude + " and Longitude= " + longitude, Toast.LENGTH_LONG).show();

              Intent intent = new Intent(Intent.ACTION_VIEW,
                       Uri.parse("http://maps.google.com/maps?f=d&"+ "saddr=" + setLat + ","+ setLong +"&daddr="+ latitude + ","+ longitude));
               intent.setComponent(new ComponentName("com.google.android.apps.maps",
                       "com.google.android.maps.MapsActivity"));
                        startActivity(intent);






          }
        });

      //  collapseMap();




    }

    private void GetListData(String latt, String latt1) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url2+"?Lat="+latt + "&Lon=" +latt1 , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());



                        try {
                            if (response.getString("success") != null) {

                                String res = response.getString("success");

                                if(Integer.parseInt(res) == 1){

                                    try {


                                        feedArray = response.getJSONArray("feeds");
                                       // feedArray2 = feedArray;



                                        for (int i = 0; i < 4; i++) {
                                            JSONObject obj = (JSONObject) feedArray.get(i);

                                            Movie movie = new Movie();
                                            movie.setTitle(obj.getString("description"));
                                            movie.setThumbnailUrl(obj.getString("profilePic"));
                                            movie.setRating(obj.getDouble("Latitude"));
                                            movie.setYear(obj.getDouble("distance"));
                                            movie.setGenre(obj.getDouble("Longitude"));

                                            movieList.add(movie);
                                        }

                                        // notify data changes to list adapater
                                        adapter.notifyDataSetChanged();



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "No public Toilets around you", Toast.LENGTH_SHORT).show();

                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            messageDialog("Status", "No Public Toilets 500m from where you are");

                                        }

                                    });
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        }) {

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void initilizeMap() {
        if (googleMap == null) {

            googleMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.

        }

        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Loading places...", Toast.LENGTH_SHORT).show();
        }

        new GetGPSValues().execute();


    }

    private class GetGPSValues extends AsyncTask<Void, Void, Void> {
        JSONObject json;
        ArrayList<HashMap<String, String>> gpsLatLongValues = new ArrayList<>();

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(MainMapsActivity.this);
            pDialog.setMessage("Loading Maps...");
            pDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
            pDialog.setIndeterminate(true);
            pDialog.show();

        }


        protected Void doInBackground(Void... arg0) {

            try {

                ServiceHandler sh = new ServiceHandler();
                String jsonStr = sh.makeServiceCall(url2+"?Lat="+Latt + "&Lon=" +Lonn, ServiceHandler.GET, null);
                Log.wtf("data", jsonStr);

                Log.d("data 2", url+"?Lat="+Latt + "&Lon=" +Lonn );
                System.out.println( url+"?Lat="+Latt + "&Lon=" +Lonn );

                if (jsonStr != null) {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    maps = jsonObj.getJSONArray(TAG_MAPS);

                    for (int i = 0; i < 5; i++) {
                        HashMap<String, String> map = new HashMap<>();

                        JSONObject obj = maps.getJSONObject(i);

                        map.put(TAG_ID, obj.getString(TAG_ID));
                        map.put(TAG_DESCRIPTION, obj.getString(TAG_DESCRIPTION));
                        map.put(TAG_LAT, obj.getString(TAG_LAT));
                        map.put(TAG_LONG, obj.getString(TAG_LONG));

                        gpsLatLongValues.add(map);

                    }


                }


            } catch (Exception e) {
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            for (int i = 0; i < gpsLatLongValues.size(); i++) {

                // load marker
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(
                                Double.parseDouble(gpsLatLongValues.get(i).get(TAG_LAT)),
                                Double.parseDouble(gpsLatLongValues.get(i).get(TAG_LONG))))
                        .title(gpsLatLongValues.get(i).get(TAG_DESCRIPTION))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } // end of for loop



            googleMap.setMyLocationEnabled(true);

            // create marker
            //MarkerOptions marker = new MarkerOptions().position(new LatLng(setLat, setLong)).title("ME");
            // adding marker
            // googleMap.addMarker(marker);
            // Creates a CameraPosition from the builder
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(setLat, setLong)).zoom(15).build();


            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }// end of onPostExecute
    }

    @Override
    protected void onResume() {
        super.onResume();
        //GetListData(Latt,Lonn);
        adapter.notifyDataSetChanged();
        initilizeMap();

    }

    private void collapseMap() {
        mSpaceView.setVisibility(View.VISIBLE);
        mTransparentView.setVisibility(View.GONE);
    }

    private void expandMap() {
        mSpaceView.setVisibility(View.GONE);
        mTransparentView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
        expandMap();
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
    }

    @Override
    public void onPanelExpanded(View view) {
        collapseMap();
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11f), 1000, null);
    }

    @Override
    public void onPanelAnchored(View view) {

    }


    public void messageDialog(String title, String message){


        new AlertDialog.Builder(MainMapsActivity.this)
                .setTitle(title)
                .setMessage(message)

                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        finish();

                        //startActivity(new Intent(MainMapsActivity.this,MainMapsActivity.class));
                        //startActivity(new Intent(MainMapsActivity.this,MainMapsActivity.class).putExtra("Employeeid", employee_id));
                    }
                })
                .show();

    }

    //@Override
    //protected void onStop() {
    //    super.onStop();
    //    System.exit(0); //kill the app

   // }

    //@Override
    //public void onBackPressed() { //here I capture the event onBackPress
      ///  super.onBackPressed();
        //onStop(); //call onStop
      //  Intent i = new Intent(MainMapsActivity.this, Home.class);
     //   startActivity(i);
        //finish();
   // }

}