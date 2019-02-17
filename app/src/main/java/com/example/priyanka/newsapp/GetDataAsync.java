package com.example.priyanka.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class GetDataAsync extends AsyncTask<String, Void, ArrayList<News>>{

    MainActivity activity;

    IData iData;
    public  GetDataAsync()
    {

    }

   /*// public GetDataAsync (MainActivity activity) {
        this.activity = activity;
    }*/

    public GetDataAsync(MainActivity activity, IData iData) {
        this.activity = activity;
        this.iData = iData;
    }

    public GetDataAsync(MainActivity mainActivity) {
        this.activity= mainActivity;
    }

    @Override
    protected void onPreExecute() {
        activity.progressDialog.setTitle("Loading News");
        activity.progressDialog.setCancelable(false);
        activity.progressDialog.show();
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {
        try {
            URL url= new URL(params[0]);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode= con.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK)
            {
                ArrayList<News> arrayList = NewsParser.NewsPullParser.parseNews(con.getInputStream());
                Log.d("demo",arrayList+"");
                return arrayList;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    protected void onPostExecute(ArrayList<News> newses) {
       activity.onGetNews(newses);
       activity.progressDialog.dismiss();

    }

     public  interface IData{
        public void onGetNews(ArrayList<News> al);
    }
}
