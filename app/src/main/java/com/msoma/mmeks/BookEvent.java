package com.msoma.mmeks;

/**
 * Created by gikundi on 2/3/15.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import com.msoma.mmeks.common.BaseActivity;

//import com.example.wedding.Portfolio;
//import com.example.wedding.R;
//import com.example.wedding.Portfolio.CreatePortfolio;

import android.annotation.SuppressLint;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("NewApi")
public class BookEvent extends BaseActivity {

    EditText name;
    EditText Place;
    EditText Email;
    EditText txtDate;
    EditText Package;
    EditText PhoneNumber;
    int orgId;

    JSONParser jsonParser = new JSONParser();
    private static String url="http://m-soma.com/twendehaja/clientbookings.php";
    private TextView tvDisplayDate, tvDisplayDate2;


    private static String KEY_SUCCESS = "success";
    private DatePicker dpResult;
    private Button btnChangeDate,btnChangeDate2;


    public StringBuilder fromDate;
    public StringBuilder toDate;

    public StringBuilder From;

    public StringBuilder To;



    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;


    AlertDialogManager alert= new AlertDialogManager();

    private static final String TAG_SUCCESS = "success";
    private ProgressDialog pDialog;

    String leave_type,from_date, to_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.setContentView(R.layout.activity_book_wedding);


        setCurrentDateOnView();
        addListenerOnButton();

        TextView txtProduct = (TextView) findViewById(R.id.textViewOrgId);

        name=(EditText) findViewById(R.id.name);
        PhoneNumber=(EditText) findViewById(R.id.PhoneNumber);
        Email=(EditText) findViewById(R.id.Email);
        Package=(EditText) findViewById(R.id.Package);
        Place=(EditText) findViewById(R.id.Place);
        //txtDate=(EditText) findViewById(R.id.txtDate);

        // getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#127aef")));
        // getActionBar().setIcon( new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        //Intent i = getIntent();
        // getting attached intent data
        //orgId = i.getStringExtra("organisation_id");
        //orgId   = i.getIntExtra("organisation_id", 50);
        //txtProduct.setText(String.valueOf(orgId));
        // displaying selected product name
        //txtProduct.setText(prior);

        Button btnClientBooking = (Button) findViewById(R.id.ClientBooking );
        btnClientBooking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                fromDate = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");

                //  Toast.makeText(getApplicationContext(), "You selected:" + fromDate, Toast.LENGTH_SHORT).show();




                toDate = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");


                from_date = fromDate.toString();
                to_date = toDate.toString();



                if (  ( !PhoneNumber.getText().toString().equals(""))
                        && ( !name.getText().toString().equals(""))
                        && ( !Email.getText().toString().equals(""))
                        && ( !Package.getText().toString().equals(""))
                        && ( !Place.getText().toString().equals(""))

                        )
                {

                    new ClientBooking().execute();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override protected int getLayoutResource() {
        return R.layout.activity_book_wedding;
    }

    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        tvDisplayDate2 = (TextView) findViewById(R.id.tvDate2);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        tvDisplayDate2.setText(tvDisplayDate.getText().toString());
    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        btnChangeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });
        btnChangeDate2 = (Button) findViewById(R.id.btnChangeDate2);

        btnChangeDate2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID2);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
            case DATE_DIALOG_ID2:
                cur = DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            if(cur == DATE_DIALOG_ID){
                // set selected date into textview
                tvDisplayDate.setText("From Date : " + new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));

                fromDate = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");


                From = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");


                // Toast.makeText(getApplicationContext(), "You selected:" + fromDate, Toast.LENGTH_SHORT).show();


            }
            else{
                tvDisplayDate2.setText("To Date : " + new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));


                toDate = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");



                To = new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" ");

                // Toast.makeText(getApplicationContext(), "You selected:" + toDate, Toast.LENGTH_SHORT).show();

            }

            //  Log.d("From Date: ", "> " + tvDisplayDate.getText().toString());
            // Log.d("To Date: ", "> " +  tvDisplayDate2.getText().toString());
            // Date date = new Date(year,month,day);
            //  Date date2 = new Date(year,month,day);


            // long currentMilli =  date.getTime();
            //long currentMilli2 =  date2.getTime();


        }
    };


    class ClientBooking extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookEvent.this);
            pDialog.setMessage("Submitting Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            String result = null;



            String from_date= From.toString();

            String to_date = To.toString();


            Log.d("From Date: ", "> " + from_date);
            Log.d("To Date: ", "> " + to_date);




            Intent i = getIntent();
            orgId   = i.getIntExtra("organisation_id", 50);

            System.out.print("id"+orgId);

            String name1 = name.getText().toString();
            String Email22 = PhoneNumber.getText().toString();
            String Email1 = Email.getText().toString();
            String Package1 = Package.getText().toString();
            String Place1 = Place.getText().toString();
            //String txtDate1 = txtDate.getText().toString();

            Log.d("Create Request", name1);
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("ClientName", name1));
            params.add(new BasicNameValuePair("PhoneNumber", Email22));
            params.add(new BasicNameValuePair("EmailAddress", Email1));
            params.add(new BasicNameValuePair("WeddingPackage", Package1));
            params.add(new BasicNameValuePair("WeddingLocation", Place1));
            // params.add(new BasicNameValuePair("WeddingDate", txtDate1));
            params.add(new BasicNameValuePair("from_date",from_date));
            params.add(new BasicNameValuePair("to_date",to_date));

            params.add(new BasicNameValuePair("organisation_id", String.valueOf(orgId)));
            //params.add(new BasicNameValuesPair("OrgId", organisation_id));

            Log.d("Create Request1", params.toString());
            Log.d("Url", url);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), Portfolio.class);
                    //startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }






}