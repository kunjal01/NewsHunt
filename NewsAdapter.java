package com.example.android.newsappproject;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,
                    parent, false);
        }

        News currentNewsItem = getItem(position);

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.news_item_section);
        sectionTextView.setText(currentNewsItem.getSection());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.news_item_title);
        titleTextView.setText(currentNewsItem.getTitle());


        TextView authorTextView = (TextView) listItemView.findViewById(R.id.textview_author);
        authorTextView.setText(currentNewsItem.getAuthor());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.textview_date);

        String originalTime = currentNewsItem.getTime();


        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.US);
        Date myDate = null;
        try {

            myDate = originalDateFormat.parse(originalTime.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            Log.e("NewsAdapter", "Problem parsing the date string");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(myDate);
        dateTextView.setText(formattedDate);

        return listItemView;
    }
}
