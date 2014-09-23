package com.codepath.ashwin.googleimgsearch.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class Image {
	public String imgUrl; // URL to full image
	public String thumbUrl; // URL to image thumbnail
	public String title; // Formatted title of the image
	public String rawTitle; // Unformatted title of the image
	public int iWidth; // Image width
	public int iHeight; // Image Height
	public int tWdith; // Thumbnail width
	public int tHeight; // Thumbnail height
	
	public Image(JSONObject jsonImageObj) {
		try {
			this.imgUrl = jsonImageObj.getString("url");
			this.thumbUrl = jsonImageObj.getString("tbUrl");
			this.iHeight = jsonImageObj.getInt("height");
			this.iWidth = jsonImageObj.getInt("width");
			this.tWdith = jsonImageObj.getInt("tbWidth");
			this.tHeight = jsonImageObj.getInt("tbHeight");
			this.title = jsonImageObj.getString("title");
			this.rawTitle = jsonImageObj.getString("titleNoFormatting");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Image> fromJsonObject(JSONArray JsonImageArray) {
		ArrayList <Image> imgList = new ArrayList<Image>();
		for(int i = 0; i < JsonImageArray.length(); i++) {
			try {
				JSONObject jImgObject = JsonImageArray.getJSONObject(i);
				imgList.add(new Image(jImgObject));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imgList;
	}
	
	public String toString() {
		return this.imgUrl;
	}
}
