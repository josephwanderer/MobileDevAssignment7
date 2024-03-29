package com.murach.newsreader;

/**
 * Created by Joe on 5/23/2017.
 */


import android.app.Fragment;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import android.net.Uri;


public class ItemFragment extends Fragment implements View.OnClickListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the default values for the preferences
        //PreferenceManager.setDefaultValues(getActivity(),R.xml.preferences, false);

        // get default SharedPreferences object
        //prefs = PreferenceManager.getDefaultSharedPreferences((getActivity()));

        // turn on the options menu
        //setHasOptionsMenu(true);

//        io = new FileIO(getApplicationContext());
//
//        titleTextView = (TextView) findViewById(R.id.titleTextView);
//        itemsListView = (ListView) findViewById(R.id.itemsListView);
//
//        itemsListView.setOnItemClickListener(this);
//
//        new ItemsFragment.DownloadFeed().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // get references to widgets
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView pubDateTextView = (TextView) view.findViewById(R.id.pubDateTextView);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        TextView linkTextView = (TextView) view.findViewById(R.id.linkTextView);

        // get the intent
        Intent intent = getActivity().getIntent();

        // get data from the intent
        String pubDate = intent.getStringExtra("pubdate");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description").replace('\n', ' ');

        // display data on the widgets
        pubDateTextView.setText(pubDate);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // set listener
        linkTextView.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        // get the intent
        Intent intent = getActivity().getIntent();

        // get the Uri for the link
        String link = intent.getStringExtra("link");
        Uri viewUri = Uri.parse(link);

        // create the intent and start it
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
    }
    }



