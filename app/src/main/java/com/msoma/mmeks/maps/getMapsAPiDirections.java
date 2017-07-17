package com.msoma.mmeks.maps;

import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.msoma.mmeks.R;

public class getMapsAPiDirections extends FragmentActivity {

        //GoogleMap mGoogleMap;
        private ProgressDialog pDialog;
        private GoogleMap googleMap;
        GPSTracker gps;
        Double setLat, setLong;

        String[] mPlaceType = null;
        String[] mPlaceTypeName = null;

        double mLatitude = 0;
        double mLongitude = 0;


        public static String TAG_ID = "map_id";
        public static String TAG_MAPS = "maps";
        public static String TAG_LAT = "Latitude";
        public static String TAG_DESCRIPTION = "description";
        public static String TAG_LONG = "Longitude";
        private static String url = "http://www.m-soma.com/twendehaja/twendehajaapi.php";


        JSONArray maps = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.maps_direction);

            Intent i = getIntent();
            double lat   = i.getDoubleExtra("Latitude",50);
            double Lng   = i.getDoubleExtra("Longitude",50);

            Toast.makeText(
                    getApplicationContext(),
                    "Your Location is - \nLat: " + lat + "\nLong: "
                            + Lng, Toast.LENGTH_LONG).show();


            gps = new GPSTracker(getMapsAPiDirections.this);

            if (gps.canGetLocation()) {
                setLat = gps.getLatitude();
                setLong = gps.getLongitude();
                Toast.makeText(
                        getApplicationContext(),
                        "Your Location is - \nLat: " + setLat + "\nLong: "
                                + setLong, Toast.LENGTH_LONG).show();

                initilizeMap();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }


        }


        private void initilizeMap() {
            if (googleMap == null) {
                // googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                // R.id.map)).getMap();
                googleMap = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();

                // check if map is created successfully or not
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }

                new GetValues().execute();


            }
        }


        private class GetValues extends AsyncTask<Void, Void, Void> {
            JSONObject json;
            ArrayList<HashMap<String, String>> gpsLatLongValues = new ArrayList<>();

            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(getMapsAPiDirections.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }


            protected Void doInBackground(Void... arg0) {

                try {

                    ServiceHandler sh = new ServiceHandler();
                    String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,
                            null);
                    Log.wtf("data", jsonStr);
                    if (jsonStr != null) {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        maps = jsonObj.getJSONArray(TAG_MAPS);

                        for (int i = 0; i < maps.length(); i++) {
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
                    googleMap
                            .addMarker(new MarkerOptions()
                                    .position(
                                            new LatLng(
                                                    Double.parseDouble(gpsLatLongValues
                                                            .get(i).get(TAG_LAT)),
                                                    Double.parseDouble(gpsLatLongValues
                                                            .get(i).get(TAG_LONG))))
                                    .title(gpsLatLongValues.get(i).get(
                                            TAG_DESCRIPTION))
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                } // end of for loop

                googleMap.setMyLocationEnabled(true);

                // create marker
                //MarkerOptions marker = new MarkerOptions().position(new LatLng(setLat, setLong)).title("ME");
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(setLat, setLong)).zoom(17).build();

                // adding marker
                // googleMap.addMarker(marker);
                // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();

            }// end of onPostExecute
        }



        @Override
        protected void onResume() {
            super.onResume();
            initilizeMap();
        }


    }