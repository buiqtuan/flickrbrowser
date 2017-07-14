package com.example.tuanbq.flickrbrower;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolBar(true);

        Intent i = getIntent();
        Photo photo = (Photo) i.getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null) {
            TextView photoTitle = (TextView) findViewById(R.id.photoTitle);
            TextView photoAuthor = (TextView) findViewById(R.id.photoAuthor);
            TextView photoTags = (TextView) findViewById(R.id.photoTags);

            photoTitle.setText("Title: " + photo.getmTitle());
            photoAuthor.setText("Author: " + photo.getmAuthor());
            photoTags.setText("Tags: " + photo.getmTag());

            ImageView photoImage = (ImageView) findViewById(R.id.photoImage);
            Picasso.with(this).load(photo.getmLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
