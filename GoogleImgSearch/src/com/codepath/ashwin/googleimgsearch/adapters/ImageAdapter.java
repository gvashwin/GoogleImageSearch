package com.codepath.ashwin.googleimgsearch.adapters;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.ashwin.googleimgsearch.R;
import com.codepath.ashwin.googleimgsearch.models.Image;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends ArrayAdapter<Image> {
	
	private static class ViewHolder {
		ImageView ivImgThumbnail;
		TextView tvImgTitle;
	}

	public ImageAdapter(Context context, List<Image> images) {
		super(context,R.layout.item_image_view, images);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		Image img = getItem(position);
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_view,parent,false);
			viewHolder.ivImgThumbnail = (ImageView) convertView.findViewById(R.id.ivThumImg);
			viewHolder.tvImgTitle = (TextView) convertView.findViewById(R.id.tvImgTitle);
			viewHolder.ivImgThumbnail.setBackgroundColor(Color.GRAY);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ivImgThumbnail.setImageResource(0);
		Picasso.with(getContext()).load(img.thumbUrl).into(viewHolder.ivImgThumbnail);

		viewHolder.tvImgTitle.setText(Html.fromHtml(img.title));
		return convertView;
	}

}
