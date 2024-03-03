package com.example.android.newsappproject;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private TextView emptyStateTextView;
    private NewsAdapter adapter;
    private ProgressBar spinner;
    private static final int NEWS_LOADER_ID = 1;


    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    private static final String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);


        emptyStateTextView = (TextView) findViewById(R.id.empty_view);


        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            emptyStateTextView.setText(R.string.no_internet_connection);
        } else {
            ListView newsListView = (ListView) findViewById(R.id.list);
            newsListView.setEmptyView(emptyStateTextView);
            adapter = new NewsAdapter(this, new ArrayList<News>());
            newsListView.setAdapter(adapter);

            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                        long l) {
                    News currentNews = adapter.getItem(position);
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(websiteIntent);
                }
            });

            LoaderManager loaderManager = getLoaderManager();


            loaderManager.initLoader(NEWS_LOADER_ID, null, this);


            spinner.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String articleNumber =
                sharedPrefs.getString(getString(R.string.settings_article_number_key),
                        getString(R.string.settings_article_number_default));

        String searchContent = sharedPrefs.getString(getString(R.string.settings_edit_text_key),
                getString(R.string.settings_edit_text_default));

        if (searchContent.replaceAll(" ", "").isEmpty()) {
            searchContent = getString(R.string.settings_edit_text_default);
        }
        ;


        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", searchContent);
        uriBuilder.appendQueryParameter("tag", "technology/technology");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", articleNumber);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsItem) {
        adapter.clear();

        emptyStateTextView.setText(R.string.no_news);

        if (newsItem != null && !newsItem.isEmpty()) {
            adapter.addAll(newsItem);
        }

        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}