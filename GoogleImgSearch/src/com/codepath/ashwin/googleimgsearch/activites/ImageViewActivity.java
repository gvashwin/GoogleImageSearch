package com.codepath.ashwin.googleimgsearch.activites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.codepath.ashwin.googleimgsearch.R;
import com.codepath.ashwin.googleimgsearch.R.id;
import com.codepath.ashwin.googleimgsearch.R.layout;
import com.codepath.ashwin.googleimgsearch.R.menu;
import com.ortiz.touch.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
	
	private TouchImageView ivFullImg;
	private ImageView ivShareIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		getActionBar().hide();
		String url = getIntent().getStringExtra("fullUrl");
		ivShareIcon = (ImageView) findViewById(R.id.ivShareIcon);
		ivFullImg = (TouchImageView) findViewById(R.id.ivFullImage);
		ivFullImg.setImageResource(0);
		Picasso.with(this).load(url).resize(600, 600).centerInside().into(ivFullImg, new Callback() {

			@Override
			public void onError() {
				Log.i("Picaso", "Error Loading Image");
				
			}

			@Override
			public void onSuccess() {
				Log.i("Picaso", "Image Loaded");
				setupOnClickListener();
				
			}
			
		});
	}

	private void setupOnClickListener() {
		Log.i("ShareIntent", "Setting up onclick listener to share intent");
		ivShareIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TouchImageView tivImageView = (TouchImageView) v.getRootView().findViewById(R.id.ivFullImage);
				Uri bmpUri = getLocalBitmapUri(tivImageView);
			    if (bmpUri != null) {
			        // Construct a ShareIntent with link to image
			        Intent shareIntent = new Intent();
			        shareIntent.setAction(Intent.ACTION_SEND);
			        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			        shareIntent.setType("image/*");
			        // Launch sharing dialog for image
			        startActivity(Intent.createChooser(shareIntent, "Share Image"));	
			    } else {
			        Log.i("Sharing", "Failed to share image");
			    }
			}
			
		});
		
	}
	
	public Uri getLocalBitmapUri(TouchImageView imageView) {
	    // Extract Bitmap from ImageView drawable
		 Drawable drawable = imageView.getDrawable();
		    Bitmap bmp = null;
		    if (drawable instanceof BitmapDrawable){
		       bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		    } else {
		       return null;
		    }
		    // Store image to default external storage directory
		    Uri bmpUri = null;
		    try {
		        File file =  new File(Environment.getExternalStoragePublicDirectory(  
			        Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
		        file.getParentFile().mkdirs();
		        FileOutputStream out = new FileOutputStream(file);
		        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
		        out.close();
		        bmpUri = Uri.fromFile(file);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return bmpUri;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
