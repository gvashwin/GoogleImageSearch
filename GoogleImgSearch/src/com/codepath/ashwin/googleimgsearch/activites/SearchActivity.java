package com.codepath.ashwin.googleimgsearch.activites;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.ashwin.googleimgsearch.R;
import com.codepath.ashwin.googleimgsearch.R.id;
import com.codepath.ashwin.googleimgsearch.R.layout;
import com.codepath.ashwin.googleimgsearch.R.menu;
import com.codepath.ashwin.googleimgsearch.activites.SearchPrefDialogFragment.SearchPrefDialogListener;
import com.codepath.ashwin.googleimgsearch.adapters.ImageAdapter;
import com.codepath.ashwin.googleimgsearch.models.Image;
import com.codepath.ashwin.googleimgsearch.models.SearchOptions;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends FragmentActivity implements OnQueryTextListener, SearchPrefDialogListener{
	
	// Action bar items
	private MenuInflater menuInflater;
	private MenuItem searchItem;
	private MenuItem searchPrefs;
	
	// Image prefs dialog frament items
	private Spinner imgSizeSpinner;
	
	// Activites,elements and adapters
	private SearchView searchView;
	private String searchString;
	private ArrayList <String> searchHistory;
	private ArrayList <Image> imageList;
	private StaggeredGridView gvImages;
	private TextView tvWelcomeNote;
	private ImageAdapter aImage;
	private Boolean isNetworkAvailable;
	
	// Networking library
	private  AsyncHttpClient client;
	
	// Constants
	//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=monkey&rsz=8&start=8
	private final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
	private final String QUERY_TAG = "&q=";
	private final String REQUEST_SIZE = "&rsz=8";
	private final String START_TAG = "&start=";
	private final String COLOR_TAG = "&imgcolor=";
	private final String SIZE_TAG = "&imgsz=";
	private final String SITE_TAG = "&as_sitesearch=";
	private final String TYPE_TAG = "&imgtype=";
	
	// other variables
	private String imgColor = null;
	private String imgSize = null;
	private String imgType = null;
	private String imgSite = null;
	private int startOffset = 0;
	private int offsetIncrement = 8;
	private final int TOTAL_ITEM = 64;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search); 
        setupViews();
        searchHistory = new ArrayList<String>();
        imageList = new ArrayList<Image>();
        aImage = new ImageAdapter(this,imageList);
        gvImages.setAdapter(aImage);
        client = new AsyncHttpClient();
		setupItemClickListener();
		setupOnScrollListener();
    }

    
    
	private void setupViews() {
		gvImages = (StaggeredGridView) findViewById(R.id.gvImages);
		tvWelcomeNote = (TextView) findViewById(R.id.tvWelcome);
		if(!isNetworkAvailable()) {
			tvWelcomeNote.setHint(Html.fromHtml("Welcome!<br>" +
					"<i>Network Connection Error!</i>"));
				
			
		} else {
			tvWelcomeNote.setHint(Html.fromHtml("Welcome!<br>" +
					"To search for images,<br>" +
					"please enter a search query<br>" +
					"on the top."));
		}
		gvImages.setVisibility(View.GONE);

		
	}
	
	private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
	
	public void showAdvancedSearchOptions(MenuItem item) {
		FragmentManager fm = getSupportFragmentManager();
        //SearchPrefDialogFragment sdpf = SearchPrefDialogFragment.newInstance("AdvancedSearchOptions");
        SearchPrefDialogFragment sdpf = new SearchPrefDialogFragment();
        sdpf.setRetainInstance(true);
        sdpf.show(fm, "Advanced Search Option");
		
	}
	

	private void setupOnScrollListener() {
		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if(!isNetworkAvailable()) {
					displayNetworkError();
				} else {
					//tvWelcomeNote.setVisibility(View.GONE);
					//gvImages.setVisibility(View.VISIBLE);
					fetchImages(false);
				}
				
			}
			
		});
	}

	private void setupItemClickListener() {
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the image view activity
				Intent i = new Intent(SearchActivity.this, ImageViewActivity.class);
				Image chosenImage = imageList.get(position);
				i.putExtra("fullUrl", chosenImage.imgUrl);
				startActivity(i);
				
			}
			
		});
		
	}
	
	
	// this function is called when the user enters the search query and submits.
	@Override
	public boolean onQueryTextSubmit(String query) {
		if(!isNetworkAvailable()) {
			displayNetworkError();
		} else {
			searchString = new String(query);
			searchHistory.add(searchString);
			tvWelcomeNote.setVisibility(View.GONE);
			gvImages.setVisibility(View.VISIBLE);
			fetchImages(true);
			
		}
		searchView.clearFocus();
		return true;
	}
	
	public void displayNetworkError() {
		tvWelcomeNote.setVisibility(View.VISIBLE);
		gvImages.setVisibility(View.GONE);
		tvWelcomeNote.setHint(Html.fromHtml("<i>Network Connection Error!</i>"));
		
	}
	// this function makes the aysnc call to get the images
	// via google image search api
	private void fetchImages(boolean newQuery) {
		StringBuffer searchUrl;
		if(newQuery) {
			startOffset = 0;
			aImage.clear();
			//imageList.clear();
			aImage.notifyDataSetChanged();
			
		}
		if(startOffset < TOTAL_ITEM) {
			searchUrl = new StringBuffer();
			searchUrl.append(BASE_URL).append(QUERY_TAG).append(searchString).append(REQUEST_SIZE);
			if(imgColor!=null && !imgColor.equalsIgnoreCase("none"))
				searchUrl.append(COLOR_TAG).append(imgColor);
			if(imgSize!=null && !imgSize.equalsIgnoreCase("none"))
				searchUrl.append(SIZE_TAG).append(imgSize);
			if(imgType!=null && !imgType.equalsIgnoreCase("none"))
				searchUrl.append(TYPE_TAG).append(imgType);
			if(imgSite!=null && imgSite.length()>0) 
				searchUrl.append(SITE_TAG).append(imgSite);
			searchUrl.append(START_TAG+""+startOffset);
			startOffset = startOffset + offsetIncrement;
			Log.i("searchUrl", searchUrl.toString());
			client.get(searchUrl.toString(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					JSONArray imgArray = null;
					try {
						imgArray = response.getJSONObject("responseData").getJSONArray("results");
						ArrayList <Image> debug = Image.fromJsonObject(imgArray);
						//imageList.addAll(debug);
						aImage.addAll(debug);
						aImage.notifyDataSetChanged();
						searchView.setFocusable(false);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
				
			});
		} else {
			Log.i("FetchImages","Max Images reached");
		}
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setupActionbar(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void setupActionbar(Menu menu) {
		menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		searchItem = menu.findItem(R.id.miSearch);
		searchPrefs = menu.findItem(R.id.miSearchPrefs);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(this);
	}

	@Override
	public void onSaveSerchoptions(SearchOptions opts) {
		imgColor = new String(opts.imgColor);
		imgSize = new String(opts.imgSize);
		imgType = new String(opts.imgType);
		imgSite = new String(opts.imgSite);
		Log.i("saveOptions","Image preferences saved imgColor="+imgColor+" imgSize="+imgSize+" imgType="+imgType+" imgSite="+imgSite);
		
	}

	
    
    
}
