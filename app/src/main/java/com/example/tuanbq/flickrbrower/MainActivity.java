package com.example.tuanbq.flickrbrower;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener{
    public static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolBar(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryStr = sharedPreferences.getString(FLICKR_QUERY,"");

        if (queryStr.length() > 0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData("http://api.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);
            //getFlickrJsonData.executaeOnSameThread("android, nougat");
            getFlickrJsonData.execute(queryStr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.actionSearch) {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDataAvailable: data is " + data);
            mFlickrRecyclerViewAdapter.loadNewData(data);
        } else {
            Log.d(TAG, "onDataAvailable: downloading data fail with status " + status);
        }
    }

    @Override
    public void onItemClick(View view, int pos) {
        Log.d(TAG, "onItemClick starts");
        Toast.makeText(MainActivity.this, "Normal tap at position " + pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int pos) {
        Log.d(TAG, "onItemLongClick starts");
//        Toast.makeText(MainActivity.this, "Long tap at position " + pos, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, PhotoDetailActivity.class);
        //Convert object to byte stream to transfer data between activities
        i.putExtra(PHOTO_TRANSFER, mFlickrRecyclerViewAdapter.getPhoto(pos));
        startActivity(i);
    }
}
