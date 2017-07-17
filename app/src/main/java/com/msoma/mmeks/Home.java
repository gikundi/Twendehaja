package com.msoma.mmeks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.msoma.mmeks.common.BaseActivity;

/**
 * Created by gikundi on 2/4/15.
 */

public class Home extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setActionBarIcon(R.drawable.twende_logo);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.drawable.ic_toile);



        Button btn_portable_toilet = (Button) findViewById(R.id.btn_portable_toilet);

        btn_portable_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Loading...wait",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Home.this, MainActivity.class));


            }
        });

        Button btn_public_toilets = (Button) findViewById(R.id.btn_public_toilet);

        btn_public_toilets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Loading...wait",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Home.this, MainMapsActivity.class));


            }
        });


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sales_mart;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {


            startActivity(new Intent(Home.this, Help.class));



        }
        return super.onOptionsItemSelected(item);
    }



  // @Override
   // protected void onStop() {
    //    super.onStop();
     //  System.exit(0); //kill the app

    // }
    @Override
    public void onBackPressed() {
        finish();

    }
}