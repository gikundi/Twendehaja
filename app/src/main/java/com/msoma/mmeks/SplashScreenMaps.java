package com.msoma.mmeks;


/**
 * Created by mmeks on 2/23/15.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.msoma.mmeks.maps.NetworkChangeReceiver;

public class SplashScreenMaps extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_maps);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER ))
        {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {

            }

            if (NetworkChangeReceiver.checkInternetConnection(SplashScreenMaps.this))
            {
                Intent i = new Intent(SplashScreenMaps.this, SplashScreen.class);
                startActivity(i);
                finish();
            } else{

                DialogBoxInternet();
            }
        }else{
            showGPSDisabledAlertToUser();

        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS?",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void DialogBoxInternet(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You do not have data connection available")
                .setCancelable(false)
                .setPositiveButton("Enable data connection?",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent InternetSettings = new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(InternetSettings);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
//@Override
    // protected void onStop() {
     //  super.onStop();
     // System.exit(0); //kill the app

   //  }
    @Override
    public void onBackPressed() {
        finish();

    }
    }









