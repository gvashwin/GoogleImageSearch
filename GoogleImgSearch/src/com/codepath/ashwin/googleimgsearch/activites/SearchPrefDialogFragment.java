package com.codepath.ashwin.googleimgsearch.activites;

import com.codepath.ashwin.googleimgsearch.R;
import com.codepath.ashwin.googleimgsearch.models.SearchOptions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchPrefDialogFragment extends DialogFragment {
	public static final String TAG = "SearchPrefDFrag";
	
	// Size spinner and label
	private Spinner spnrImgSizeSpinner;
	private ArrayAdapter aSpnrImgSize;
	private String imgSize;
	
	// Type spinner and label
	private Spinner spnrImgType;
	private ArrayAdapter aSpnrImgType;
	private String imgType;
	
	// Color spinner and label
	private Spinner spnrImgColor;
	private ArrayAdapter aSpnrImgColor;
	private String imgColor;
	
	// Site text view and label
	private EditText etImgSite;
	private String imgSite;
	
	// Save button
	private Button btnSave;
	private Button btnClear;
	
	private static SearchOptions searchOpt=null;
	
	public interface SearchPrefDialogListener {
		public void onSaveSerchoptions(SearchOptions opts);
	}
	public SearchPrefDialogFragment() {
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		Log.i("DialogFrag","OnCreateView called");
		View view = inflater.inflate(R.layout.search_preference_dialog, container);

		getDialog().setTitle("Advanced Search Options");
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		// Size
		spnrImgSizeSpinner = (Spinner) view.findViewById(R.id.spnrImgSize);
		aSpnrImgSize = ArrayAdapter.createFromResource(getActivity(), 
				R.array.image_sizes, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		aSpnrImgSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnrImgSizeSpinner.setAdapter(aSpnrImgSize);
		
		// Type
		spnrImgType = (Spinner) view.findViewById(R.id.spnrImgType);
		aSpnrImgType = ArrayAdapter.createFromResource(getActivity(), 
				R.array.image_type, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		aSpnrImgType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnrImgType.setAdapter(aSpnrImgType);
		
		// Color
		spnrImgColor = (Spinner) view.findViewById(R.id.spnrImgColor);
		aSpnrImgColor = ArrayAdapter.createFromResource(getActivity(), 
				R.array.image_colors, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		aSpnrImgColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnrImgColor.setAdapter(aSpnrImgColor);
		
		//Site
		etImgSite = (EditText) view.findViewById(R.id.etImgSite);
		if(SearchPrefDialogFragment.searchOpt!=null) {
			Log.i("SearchOpt", "is not null");
			spnrImgSizeSpinner.setSelection(aSpnrImgSize.getPosition(searchOpt.imgSize));
			spnrImgType.setSelection(aSpnrImgType.getPosition(searchOpt.imgType));
			spnrImgColor.setSelection(aSpnrImgColor.getPosition(searchOpt.imgColor));
			etImgSite.setText(searchOpt.imgSite);
		}
		//save button
		btnSave = (Button) view.findViewById(R.id.btnSavePref);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveSearchPrefs(v);
				
			}
			
		});
		// Clear Button
		btnClear = (Button) view.findViewById(R.id.btnClearSearchOptions);
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetSearchOptions(v);
				
			}
			
		});
		
		return view;
	}
	public void resetSearchOptions(View v) {
		SearchPrefDialogFragment.searchOpt = null;
		spnrImgSizeSpinner.setSelection(aSpnrImgSize.getPosition("none"));
		spnrImgType.setSelection(aSpnrImgType.getPosition("none"));
		spnrImgColor.setSelection(aSpnrImgColor.getPosition("none"));
		etImgSite.setText("");
		etImgSite.setHint("example.com");
	}
	public void saveSearchPrefs(View v) {
		SearchOptions imgSo = new SearchOptions();
		SearchPrefDialogFragment.searchOpt = imgSo;
		imgSo.imgSize = spnrImgSizeSpinner.getSelectedItem().toString();
		imgSo.imgType = spnrImgType.getSelectedItem().toString();
		imgSo.imgColor = spnrImgColor.getSelectedItem().toString();
		imgSo.imgSite = etImgSite.getText().toString();
		Log.i(TAG,"Save button pressed size="+imgSo.imgSize+" type="+imgSo.imgType+" color="+imgSo.imgColor+" site="+imgSo.imgSite);
		SearchPrefDialogListener mainActivity = (SearchPrefDialogListener) getActivity();
        mainActivity.onSaveSerchoptions(imgSo);
        dismiss();
		//Log.i(TAG,"Save button pressed size="+imgSo.imgSize+" type="+imgSo.imgType+" color="+imgSo.imgColor+" site="+imgSo.imgSite);
	}
	
	

}
