package com.msoma.mmeks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CommentHere extends Activity {

    //declarations
    EditText txtname,txtcomment;
    Button btnSubmit;

    int orgId, portId;

    //ProgressDialog pDialog;
    private ProgressDialog pDialog;
    AlertDialogManager alert= new AlertDialogManager();

    // URL to get contacts JSON
    private static String url = "http://m-soma.com/twendehaja/clientcomments.php";


    // JSON Node names
    private static final String KEY_SUCCESS = "success";

    String strname,strcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_here);



        Intent i = getIntent();
        orgId   = i.getIntExtra("organisation_id", 50);
        portId = i.getIntExtra("portfolio_id", 50);

//        Toast.makeText(getApplicationContext(), "Portfolio_id" + portId + "org id" + orgId,
//                Toast.LENGTH_SHORT).show();




        //initialize the button
        btnSubmit = (Button) findViewById(R.id.btn_addcomment);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //initialize the edittext

                txtname = (EditText) findViewById(R.id.name);
                txtcomment = (EditText) findViewById(R.id.comment);


                //read data entered from the screen


                strname = txtname.getText().toString().trim();
                strcomment = txtcomment.getText().toString().trim();



                new AddManager().execute();


            }
        });


    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class AddManager extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CommentHere.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();



        }

        @Override
        protected String doInBackground(Void... arg0) {
            String result = null;


            strname = txtname.getText().toString().trim();
            strcomment = txtcomment.getText().toString().trim();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("name",strname));
            params.add(new BasicNameValuePair("comment", strcomment));
            params.add(new BasicNameValuePair("organisation_id", String.valueOf(orgId)));

            params.add(new BasicNameValuePair("portfolio_id", String.valueOf( portId)));


            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,params);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonStr);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + jsonStr);

            // check for login response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    // loginErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);

                    if (Integer.parseInt(res) == 1) {
                        // user successfully logged in
                        result = "Success";

                    }


                }

                else {
                    alert.showAlertDialog(CommentHere.this, "Login Error",
                            "Failed to Login. Please Try Again!", false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // }
            // });
            // thread.start();
            return result;
        }

        protected void onPostExecute(String result) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (result.equalsIgnoreCase("Success")) {


                messageDialog("Success", "Comment submitted");



            }
        }


        public void messageDialog(String title, String message){


            new AlertDialog.Builder(CommentHere.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            startActivity(new Intent(CommentHere.this,Comments.class).putExtra("organisation_id",  orgId).putExtra("portfolio_id", portId));

                        }
                    })
                    .show();

        }
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


    public static class Help extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_help, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
}

