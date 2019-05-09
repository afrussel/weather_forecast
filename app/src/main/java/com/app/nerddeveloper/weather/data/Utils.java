package com.app.nerddeveloper.weather.data;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.nerddeveloper.weather.R;
import com.app.nerddeveloper.weather.model.ItemLocation;

public class Utils {
	/**
	 * Preference for location
	 */
	public static ItemLocation getLocation(Context context) {
		SharedPreferences pref = context.getSharedPreferences(Constant.S_KEY_CURRENT_LOC, context.MODE_PRIVATE);
		ItemLocation itemloc = new ItemLocation();
		itemloc.setId(pref.getString("id", "null"));
		itemloc.setName(pref.getString("name", "null"));
		itemloc.setCode(pref.getString("code", "null"));
		itemloc.setJsonWeather(pref.getString("jsonWeather", "null"));
		itemloc.setJsonForecast(pref.getString("jsonForecast", "null"));
		return itemloc;
	}
	
//	public static void setUpdateWidget(Context context){
//		final Intent intent = new Intent(context, MyWidgetProvider.class);
//		final PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);
//		final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		alarm.cancel(pending);
//		long interval = 1000*5;
//		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
//	}
	
	public static void saveLocation(ItemLocation itemloc, Context context) {
		SharedPreferences pref = context.getSharedPreferences(Constant.S_KEY_CURRENT_LOC, context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putString("id", itemloc.getId());
		prefEditor.putString("name", itemloc.getName());
		prefEditor.putString("code", itemloc.getCode());
		prefEditor.putString("jsonWeather", itemloc.getJsonWeather());
		prefEditor.putString("jsonForecast", itemloc.getJsonForecast());
		prefEditor.commit();
	}
	
	public static String getCurrentCityId(Context context){
		return getStringPref(Constant.S_KEY_CURRENT_ID, getDefaultCity(context), context );
	}
	
	/**
	 * Universal shared preference
	 * for string
	 */
	public static String getStringPref(String key_val, String def_val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
		return pref.getString(key_val, def_val);
	}
	
	public static void setStringPref(String key_val, String val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putString(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for integer
	 */
	public static int getIntPref(String key_val, int def_val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val, context.MODE_PRIVATE);
		return pref.getInt(key_val, def_val);
	}
	
	public static void setIntPref(String key_val, int val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val, context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putInt(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for boolean
	 */
	public static boolean getBooleanPref(String key_val, boolean def_val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
		return pref.getBoolean(key_val, def_val);
	}
	
	public static void setBooleanPref(String key_val, boolean val, Context context) {
		SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putBoolean(key_val, val);
		prefEditor.commit();
	}
	
	public static String getDefaultCity(Context context){
		return context.getResources().getString(R.string.default_city_code);
	}
	
	public static String getTemp(Double d, Context context){
		if(getIntPref(Constant.I_KEY_UNIT, 0, context)==0){ // for celcius
			return Constant.sSpiltter(d)+" \u00b0C";
			
		}else{ // for farhenheit
			Double F = ( d * (9/5)) + 32 ;
			return Constant.sSpiltter(F)+" \u00b0F";
		}
	}
	public static boolean isLocationBase(Context context){
		if(getStringPref(Constant.S_KEY_LNG_LAT, "null", context).equals("null")){
			return false;
		}else{
			return true;
		}
	}
	public static String getDay(Long l, Context context) {
		Date time=new Date(l *1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int daynum = cal.get(Calendar.DAY_OF_WEEK);

		switch (daynum) {
		case 1:
			return context.getString(R.string.sunday);
		case 2:
			return context.getString(R.string.monday);
		case 3:
			return context.getString(R.string.tuesday);
		case 4:
			return context.getString(R.string.wednesday);
		case 5:
			return context.getString(R.string.thursday);
		case 6:
			return context.getString(R.string.friday);
		case 7:
			return context.getString(R.string.saturday);
	}
		return null;
	}
}
