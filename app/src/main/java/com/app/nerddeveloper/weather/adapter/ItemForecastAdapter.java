package com.app.nerddeveloper.weather.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nerddeveloper.weather.ActivityMain;
import com.app.nerddeveloper.weather.R;
import com.app.nerddeveloper.weather.data.GlobalVariable;
import com.app.nerddeveloper.weather.model.ItemForecast;

public class ItemForecastAdapter extends BaseAdapter {

	private static ArrayList<ItemForecast> itemDetailsrrayList;
	Context contex;
	//private WeatherResponse weather= new WeatherResponse();
	ActivityMain act;
	private LayoutInflater l_Inflater;
	GlobalVariable global;

	public ItemForecastAdapter(ActivityMain act, ArrayList<ItemForecast> results) {
		this.contex=act.getApplicationContext();
		this.act=act;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(act);
		global = (GlobalVariable) act.getApplication();
	}

	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public ItemForecast getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.list_item_forecast_style_1, null);
			holder = new ViewHolder();
			holder.tv_f_temp	= (TextView) convertView.findViewById(R.id.tv_f_temp);
			holder.tv_f_day		= (TextView) convertView.findViewById(R.id.tv_f_day);
			holder.tv_f_desc	= (TextView) convertView.findViewById(R.id.tv_f_desc);
			holder.img_f_icon	= (ImageView) convertView.findViewById(R.id.img_f_icon);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_f_temp.setText(itemDetailsrrayList.get(position).getTemp());
		holder.tv_f_day.setText(itemDetailsrrayList.get(position).getDay());
		holder.tv_f_desc.setText(itemDetailsrrayList.get(position).getDesc());
		global.setDrawableSmallIcon(itemDetailsrrayList.get(position).getIcon(), holder.img_f_icon);
		
		return convertView;
	}
	

	static class ViewHolder {
		TextView tv_f_temp;
		TextView tv_f_day;
		TextView tv_f_desc;
		ImageView img_f_icon;
	}
	
}
