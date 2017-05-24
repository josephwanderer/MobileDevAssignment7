package com.murach.newsreader;

/**
 * Created by Joe on 5/23/2017.
 */



        import java.util.ArrayList;
        import java.util.HashMap;

        import android.app.Fragment;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Intent;
        import android.preference.PreferenceManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.widget.AdapterView.OnItemClickListener;

public class ItemsFragment extends Fragment implements OnItemClickListener{

    private RSSFeed feed;
    private FileIO io;

    private TextView titleTextView;
    private ListView itemsListView;

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
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        io = new FileIO(getActivity().getApplicationContext());

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        itemsListView = (ListView) view.findViewById(R.id.itemsListView);

        itemsListView.setOnItemClickListener(this);

        new ItemsFragment.DownloadFeed().execute();


        return view;
    }

    class DownloadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            io.downloadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded");
            new ItemsFragment.ReadFeed().execute();
        }
    }

    class ReadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            feed = io.readFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed read");

            // update the display for the activity
            ItemsFragment.this.updateDisplay();
        }
    }

    public void updateDisplay()
    {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // set the title for the feed
        titleTextView.setText(feed.getTitle());

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (RSSItem item : items) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", item.getPubDateFormatted());
            map.put("title", item.getTitle());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "title"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(getActivity(), data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent(getActivity(), ItemActivity.class);

        intent.putExtra("pubdate", item.getPubDate());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());

        this.startActivity(intent);
    }


}
