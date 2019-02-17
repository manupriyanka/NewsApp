package com.example.priyanka.newsapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class NewsParser {


    public static class NewsPullParser {
        News news;

        static public ArrayList<News> parseNews(InputStream inputStream) throws XmlPullParserException, IOException {
            ArrayList<News> newes = new ArrayList<>();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            //parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream, "UTF-8");
            News news = null;
            Boolean flag = false;
            Boolean m = false;
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            news = new News();
                            flag = true;
                            m =false;
                        } else if (parser.getName().equals("title") && flag == true) {
                            news.setTitle(parser.nextText());
                        } else if (parser.getName().equals("description") && flag == true) {
                            news.setDescription(descriptionParse(parser.nextText()));

                        } else if (parser.getName().equals("media:content") && flag == true && m == false) {
                            Log.d("Loop", "entered");
                            news.setUrlToImage(parser.getAttributeValue(null, "url"));
                            m = true;
                        }
                        else if (parser.getName().equals("media:thumbnail") && flag == true) {
                            news.setUrlToImage(parser.getAttributeValue(null, "url"));
                        } else if (parser.getName().equals("link") && flag == true) {

                            news.setUrl(parser.nextText());

                        } else if (parser.getName().equals("pubDate") && flag == true) {
                            news.setPublishedAt(parser.nextText());
                            Log.d("pub", news.publishedAt);
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item") && flag == true) {
                            Log.d("news", news + "");
                            newes.add(news);
                        }
                        break;

                    default:
                        break;
                }
                event = parser.next();
            }

            Log.d("newes", newes + "");
            return newes;
        }
    }

    static public String descriptionParse(String s) {
        if (s.equals("") || s.equals(null))
            return s;
        StringBuilder sb = new StringBuilder(s);
        int i;
        for (i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '<') {
                break;
            }

        }
        return sb.toString().substring(0, i);
    }
}







