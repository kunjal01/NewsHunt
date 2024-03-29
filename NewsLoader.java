package com.example.android.newsappproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;


    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<News> result = QueryUtils.fetchNewsData(url);
        return result;
    }
}

