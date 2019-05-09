package com.app.nerddeveloper.weather.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.nerddeveloper.weather.R;
import com.app.nerddeveloper.weather.model.ItemLocation;

public class GlobalVariable extends Application{
	
	
	
	/**
	 * Universal shared preference
	 * for boolean
	 */
	private boolean getBooleanPref(String key_val, boolean def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getBoolean(key_val, def_val);
	}
	
	private void setBooleanPref(String key_val, boolean val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putBoolean(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for integer
	 */
	private int getIntPref(String key_val, int def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getInt(key_val, def_val);
	}
	
	private void setIntPref(String key_val, int val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putInt(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for string
	 */
	private String getStringPref(String key_val, String def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getString(key_val, def_val);
	}
	
	private void setStringPref(String key_val, String val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putString(key_val, val);
		prefEditor.commit();
	}
	
	private String getDefaultCity(){
		return Utils.getDefaultCity(getApplicationContext());
	}
	
	public String getLongLat(String def_val){
		return getStringPref(Constant.S_KEY_LNG_LAT, def_val);
	}
	
	public void setLongLat(String val){
		setStringPref(Constant.S_KEY_LNG_LAT, val) ;
	}
	
	public void setCurrentId(String id){
		setStringPref(Constant.S_KEY_CURRENT_ID, id);
	}
	
	public String getCurrentId(){
		return getStringPref(Constant.S_KEY_CURRENT_ID, getDefaultCity());
	}
	
	public void setIntUnit(int unit){
		setIntPref(Constant.I_KEY_UNIT, unit);
	}
	
	
	public void setIntTheme(int index){
		setIntPref(Constant.I_KEY_THEME, index);
	}
	
	public int getIntTheme(){
		return getIntPref(Constant.I_KEY_THEME, 0);
	}
	
	public void setMapCode(String code){
		setStringPref(Constant.S_KEY_MAP, code);
	}
	
	public String getMapCode(){
		return getStringPref(Constant.S_KEY_MAP, "rain");
	}
	
	public void setMapCodeIndex(int index){
		setIntPref(Constant.I_KEY_MAP, index);
	}
	
	public int getMapCodeIndex(){
		return getIntPref(Constant.I_KEY_MAP, 0);
	}
	
	
	/**
	 * Preference for location
	 */
	public ItemLocation getLocation() {
		return Utils.getLocation(getApplicationContext());
	}
	
	public void saveLocation(ItemLocation itemloc) {
		Utils.saveLocation(itemloc, getApplicationContext());
	}
	
	
	public String generateCurrentDate(int format_key){
		Date curDate = new Date();
		String DateToStr = "";
		//default 11-5-2014 11:11:51
		SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

		switch (format_key) {
		case 1:
			
			format = new SimpleDateFormat("dd/MM/yyy");
			DateToStr = format.format(curDate);
			break;
			
		case 2:
			//May 11, 2014 11:37 PM
			DateToStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(curDate);
			break;
		case 3:
			//11-5-2014 11:11:51
			format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			DateToStr = format.format(curDate);
			break;
		}
		return DateToStr;
	}
	
	public void setDrawableIcon(String icon, ImageView im) {
		if (icon.equals("01d")||icon.equals("01n")) { // clear sky
			im.setBackgroundResource(R.drawable.w_clear);
			
		} else if (icon.equals("02d")||icon.equals("02n")) { //few clouds
			im.setBackgroundResource(R.drawable.w_fewcloud);
		}else if (icon.equals("03d")||icon.equals("03n")) { // scattered clouds
			im.setBackgroundResource(R.drawable.w_cloud);
		}else if (icon.equals("04d")||icon.equals("04n")) { //broken clouds
			im.setBackgroundResource(R.drawable.w_cloud);
			
		}else if (icon.equals("09d")||icon.equals("09n")) {  //shower rain
			im.setBackgroundResource(R.drawable.w_shower);
			
		}else if (icon.equals("10d")||icon.equals("10n")) { //rain
			im.setBackgroundResource(R.drawable.w_rain);
			
		}else if (icon.equals("11d")||icon.equals("11n")) { //thunderstorm
			im.setBackgroundResource(R.drawable.w_thunderstorm);
			
		}else if (icon.equals("13d")||icon.equals("13n")) { //snow
			im.setBackgroundResource(R.drawable.w_snow);
			
		}else if (icon.equals("50d")||icon.equals("50n")) { //mist
			im.setBackgroundResource(R.drawable.w_mist);
			
		} else {
			im.setBackgroundResource(R.drawable.w_fewcloud);
		}
	}
	
	public void setDrawableSmallIcon(String icon, ImageView im) {
		
		if (icon.equals("01d")||icon.equals("01n")) { // clear sky
			im.setBackgroundResource(R.drawable.w_small_clear);
			
		} else if (icon.equals("02d")||icon.equals("02n")) { //few clouds
			im.setBackgroundResource(R.drawable.w_small_fewcloud);
			
		}else if (icon.equals("03d")||icon.equals("03n")) { // scattered clouds
			im.setBackgroundResource(R.drawable.w_small_cloud);
			
		}else if (icon.equals("04d")||icon.equals("04n")) { //broken clouds
			im.setBackgroundResource(R.drawable.w_small_cloud);
			
		}else if (icon.equals("09d")||icon.equals("09n")) {  //shower rain
			im.setBackgroundResource(R.drawable.w_small_shower);
			
		}else if (icon.equals("10d")||icon.equals("10n")) { //rain
			im.setBackgroundResource(R.drawable.w_small_rain);
			
		}else if (icon.equals("11d")||icon.equals("11n")) { //thunderstorm
			im.setBackgroundResource(R.drawable.w_small_thunderstorm);
			
		}else if (icon.equals("13d")||icon.equals("13n")) { //snow
			im.setBackgroundResource(R.drawable.w_small_snow);
			
		}else if (icon.equals("50d")||icon.equals("50n")) { //mist
			im.setBackgroundResource(R.drawable.w_small_mist);
			
		} else {
			im.setBackgroundResource(R.drawable.w_small_fewcloud);
		}
	}
	
	public void setLytColor(String icon, RelativeLayout lyt){
		
		String color[] = getResources().getStringArray(R.array.color_weather);
		if (icon.equals("01d") || icon.equals("01n")) { // clear sky
			lyt.setBackgroundColor(Color.parseColor(color[0]));
			
		} else if (icon.equals("02d") || icon.equals("02n")) { // few clouds
			lyt.setBackgroundColor(Color.parseColor(color[1]));
			
		} else if (icon.equals("03d") || icon.equals("03n")) { // scatteredclouds
			lyt.setBackgroundColor(Color.parseColor(color[2]));
			
		} else if (icon.equals("04d") || icon.equals("04n")) { // broken clouds
			lyt.setBackgroundColor(Color.parseColor(color[3]));
			
		} else if (icon.equals("09d") || icon.equals("09n")) { // shower rain
			lyt.setBackgroundColor(Color.parseColor(color[4]));
			
		} else if (icon.equals("10d") || icon.equals("10n")) { // rain
			lyt.setBackgroundColor(Color.parseColor(color[5]));
			
		} else if (icon.equals("11d") || icon.equals("11n")) { // thunderstorm
			lyt.setBackgroundColor(Color.parseColor(color[6]));
			
		} else if (icon.equals("13d") || icon.equals("13n")) { // snow
			lyt.setBackgroundColor(Color.parseColor(color[7]));
			
		} else if (icon.equals("50d") || icon.equals("50n")) { // mist
			lyt.setBackgroundColor(Color.parseColor(color[8]));
			
		} else {
			lyt.setBackgroundColor(Color.parseColor(color[9]));
		}
	}
	
	public String getTime(Long a){
		//DateFormat ddf = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
		Date d = new Date(a * 1000);
		String h = d.getHours()+"";
		String m = d.getMinutes()+"";
		if(h.length()==1){
			h="0"+h;
		}
		if(m.length()==1){
			m="0"+m;
		}
		return h+":"+m;
	}
	
	public String getDay(Long l) {
		return Utils.getDay(l, getApplicationContext());
	}
	
	public String getLastUpdate(Long l){
		Date curDate = new Date(l *1000);
		//Wed, 4 Jul 2001 12:08:56 -0700
		SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yyyy HH:mm");
		String dateToStr = format.format(curDate);
		return "LAST UPDATE "+dateToStr.toUpperCase();
	}
	
	public String getTemp(Double d){
		return Utils.getTemp(d, getApplicationContext());
	}

}
