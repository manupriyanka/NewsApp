package com.example.priyanka.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,GetDataAsync.IData {

    Spinner spinner;
    TextView textView, tvnumber,tvpublish,tvtitle,et4,tvimageinvisible;
    Button btngo;
    ImageView imprev,imnext,imageView;
    ArrayList<News> arrayList = null;
    ScrollView scrollView2;
    LinearLayout vertical;
    ProgressDialog progressDialog;
    String src;
    int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");
        final Spinner spinner= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this, R.array.sources, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        progressDialog = new ProgressDialog(MainActivity.this);
        textView = (TextView)findViewById(R.id.textView);
        tvimageinvisible = (TextView)findViewById(R.id.tvimageinvisible);
        scrollView2 = (ScrollView)findViewById(R.id.scrollView2);
        tvnumber = (TextView)findViewById(R.id.tvnumber);
        tvpublish = (TextView)findViewById(R.id.tvpublish);
        tvtitle = (TextView)findViewById(R.id.tvtitle);
        btngo = (Button)findViewById(R.id.btngo);
        imprev = (ImageView)findViewById(R.id.imprev);
        imprev.setEnabled(false);

        et4 = new TextView(this);
        et4.setGravity(Gravity.CENTER);
        et4.setTextColor(Color.BLACK);
        imnext =(ImageView)findViewById(R.id.imnext);
        imnext.setEnabled(false);
        imageView = (ImageView)findViewById(R.id.imageView);
        vertical= (LinearLayout) findViewById(R.id.vertical);
        findViewById(R.id.btngo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    if(isConnectedOnline()) {

                        index = 0;
                        spinner.performClick();

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        imnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedOnline()) {
                    nextButton(v);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
                }
            }

        });

        imprev.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if(isConnectedOnline()) {

                    prevButton(v);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    public  void onGetNews(ArrayList<News> al){
        try {
            arrayList = al;
            Log.d("demoarraylist", al+"");
            if(al==null)
            {
                Toast.makeText(MainActivity.this,"No News found",Toast.LENGTH_SHORT).show();
                imprev.setEnabled(false);
                imnext.setEnabled(false);
                tvnumber.setText("");
                imageView.setImageResource(0);
                et4.setText("");
                tvpublish.setText("");
                tvtitle.setText("");
            }
            if(al.size() == 0 || al.size() ==1)
            {

                imprev.setEnabled(false);
                imnext.setEnabled(false);
            }
            else{
                News n = al.get(0);
                Log.d("demo2", al.get(0) + "" + al.size());
                setData(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void prevButton(View v){
        if(arrayList!=null){
            if(index >= 0){
                if (index == 0) {
                    index = arrayList.size();
                }
                index --;
                News n = arrayList.get(index);
                setData(n);
            }
        }
    }
    public void nextButton(View v){
        if(arrayList!=null){
            if(index <= arrayList.size()-1){
                index ++;
                if (index == arrayList.size()) {
                    index = 0;
                }
                News n = arrayList.get(index);
                setData(n);
            }
        }
    }
    public void setData(final News n) {
        try {
            tvnumber.setText((index + 1) + " out of " + arrayList.size());
            imageView.setImageResource(R.drawable.progress);
            tvpublish.setText(n.getPublishedAt());
            Picasso.with(MainActivity.this).load(n.getUrlToImage()).placeholder(R.drawable.progress).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnectedOnline()) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(n.getUrl()));
                        startActivity(viewIntent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            /*et4 = new TextView(this);
            et4.setGravity(Gravity.CENTER);
            et4.setTextColor(Color.BLACK);*/
            et4.setText(n.getDescription());
            vertical.removeAllViews();
            vertical.addView(et4);
            tvtitle.setText(String.valueOf(n.getTitle()));
            tvtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnectedOnline()) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(n.getUrl()));
                        startActivity(viewIntent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });

           if(n.getDescription().equals("") || n.getDescription().equals("null"))
            {
               et4.setText("<No description>");
            }
            if(n.getTitle().equals(""))
            {
                tvtitle.setText("<No title>");

            }
           if (n.getPublishedAt().equals("") || n.getPublishedAt().equals("null"))
            {
               tvpublish.setText("No publihed date");
            }

        }
        catch (Exception e)
        {
            Log.d("error","");
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        src=String.valueOf(parent.getSelectedItem().toString());
        try {
            if (isConnectedOnline()){
                if (src.equals("Show Categories")) {
                    progressDialog.dismiss();
                    et4.setText("");
                    tvpublish.setText("");
                    tvnumber.setText("");
                    imnext.setEnabled(false);
                    imprev.setEnabled(false);
                    imageView.setImageResource(0);
                    tvtitle.setText("");
                    imprev.setEnabled(false);
                    imnext.setEnabled(
                            false);

                    Toast.makeText(getApplicationContext(), "Choose Category", Toast.LENGTH_SHORT).show();
                } else {
                    index = 0;
                    imprev.setEnabled(true);
                    imnext.setEnabled(true);
                     if (src.equals("Top Stories"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_topstories.rss");
                    else if (src.equals("World"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_world.rss");
                    else if (src.equals("U.S."))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_us.rss");
                    else if (src.equals("Business"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/money_latest.rss");
                     else if(src.equals("Politics"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_allpolitics.rss");
                    else if(src.equals("Technology")) {
                         new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss");
                     }
                    else if(src.equals("Health"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_health.rss");
                    else if(src.equals("Entertainment"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_showbiz.rss");
                    else if(src.equals("Travel"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_travel.rss");
                    else if(src.equals("Living"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_living.rss");
                    else if(src.equals("Most Recent"))
                        new GetDataAsync(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_latest.rss");
                }
            }
            else {
                Toast.makeText(MainActivity.this,"No Internet",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
             //Toast.makeText(getApplicationContext(), "Choose Category", Toast.LENGTH_SHORT).show();
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    private  boolean isConnectedOnline()
    {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        if( networkInfo!= null && networkInfo.isConnected())
        {
            return  true;
        }
        return  false;
    }
}
