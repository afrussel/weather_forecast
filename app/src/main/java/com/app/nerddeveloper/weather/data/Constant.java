package com.app.nerddeveloper.weather.data;

import android.net.Uri;
import android.util.Log;

import com.app.nerddeveloper.weather.R;

public class Constant {
	
	public static final String S_KEY_CURRENT_ID ="s_key_cur_id"; //default id
	
	public static final String S_KEY_CURRENT_LOC ="s_key_cur_loc"; //default id 
	
	public static final String S_KEY_LIST_LOCATION ="s_key_list_locatio";
	
	public static final String I_KEY_UNIT ="i_key_unit"; // integer
	
	public static final String S_KEY_LNG_LAT ="i_key_lng_lat";
	
	public static final String I_KEY_THEME ="i_key_theme";
	
	public static final String S_KEY_MAP ="s_key_map";
	
	public static final String I_KEY_MAP ="i_key_map";
	

	public static final String WEATHER_API_KEY = "503d45095d60b15d1eaa25f7afe77214";
	

	public static String getURLmap(String code){
		//http://openweathermap.org/help/tiles.html?opacity=0.6&l=temp
		Uri.Builder builder = new Uri.Builder();
		String URL;
		builder.scheme("http").authority("openweathermap.org")
			.appendPath("help")
			.appendPath("tiles.html")
			.appendQueryParameter("opacity", "0.6")
			.appendQueryParameter("l", code);
		URL = builder.build().toString();
		Log.d("URL map", URL);
		return URL;
	}
	
	//api.openweathermap.org/data/2.5/weather?lat=35&lon=139
	public static String getURLweather(String id){
		Uri.Builder builder = new Uri.Builder();
		String URL;
		builder.scheme("http").authority("api.openweathermap.org")
			.appendPath("data").appendPath("2.5")
			.appendPath("weather")
			.appendQueryParameter("id", id)
			.appendQueryParameter("mode", "json")
			.appendQueryParameter("units", "metric")
			.appendQueryParameter("APPID", WEATHER_API_KEY);
		URL = builder.build().toString();
		Log.d("URL weather", URL);
		return URL;
	}
	
	public static String getURLweather2(String loc){
		Uri.Builder builder = new Uri.Builder();
		String URL;
		builder.scheme("http").authority("api.openweathermap.org")
			.appendPath("data").appendPath("2.5")
			.appendPath("weather")
			.appendQueryParameter("lat", splitLoc(loc)[0])
			.appendQueryParameter("lon", splitLoc(loc)[1])
			.appendQueryParameter("mode", "json")
			.appendQueryParameter("units", "metric")
			.appendQueryParameter("APPID", WEATHER_API_KEY);
		URL = builder.build().toString();
		Log.d("URL weather", URL);
		return URL;
	}
	
	public static String getURLforecast(String id){
		Uri.Builder builder = new Uri.Builder();
		String URL;
		builder.scheme("http").authority("api.openweathermap.org")
			.appendPath("data").appendPath("2.5")
			.appendPath("forecast").appendPath("daily")
			.appendQueryParameter("id", id)
			.appendQueryParameter("cnt", "7")
			.appendQueryParameter("mode", "json")
			.appendQueryParameter("units", "metric")
			.appendQueryParameter("APPID", WEATHER_API_KEY);
		URL = builder.build().toString();
		Log.d("URL forecast", URL);
		return URL;
	}
	
	public static String getURLforecast2(String loc){
		Uri.Builder builder = new Uri.Builder();
		String URL;
		builder.scheme("http").authority("api.openweathermap.org")
			.appendPath("data").appendPath("2.5")
			.appendPath("forecast").appendPath("daily")
			.appendQueryParameter("lat", splitLoc(loc)[0])
			.appendQueryParameter("lon", splitLoc(loc)[1])
			.appendQueryParameter("cnt", "7")
			.appendQueryParameter("mode", "json")
			.appendQueryParameter("units", "metric")
			.appendQueryParameter("APPID", WEATHER_API_KEY);
		URL = builder.build().toString();
		Log.d("URL forecast", URL);
		return URL;
	}
	
	private static String[] splitLoc(String loc){
		//Double d=Double.valueOf(s);
		String s[] = null;
		if(loc.contains("|")){
			s=loc.split("\\|");
		}
		return s;
	}
	
	public static String toCelcius(Double d){
		//Double d=Double.valueOf(s);
		String s =(Double.toString(Math.round(d) - 273));
		if(s.contains(",")){
			s=s.split("\\,")[0];
		}else if(s.contains(".")){
			s=s.split("\\.")[0];
		}
		return s;
	}
	
	public static String sSpiltter(Double d){
		//Double d=Double.valueOf(s);
		String s = String.valueOf(d);
		if(s.contains(",")){
			s=s.split("\\,")[0];
		}else if(s.contains(".")){
			s=s.split("\\.")[0];
		}
		return s;
	}
	
	public static int getDrawableWidgetIcon(String icon) {
		if (icon.equals("01d")||icon.equals("01n")) { // clear sky
			return R.drawable.w_small_clear;
			
		} else if (icon.equals("02d")||icon.equals("02n")) { //few clouds
			return R.drawable.w_small_fewcloud;
			
		}else if (icon.equals("03d")||icon.equals("03n")) { // scattered clouds
			return R.drawable.w_small_cloud;
			
		}else if (icon.equals("04d")||icon.equals("04n")) { //broken clouds
			return R.drawable.w_small_cloud;
			
		}else if (icon.equals("09d")||icon.equals("09n")) {  //shower rain
			return R.drawable.w_small_shower;
			
		}else if (icon.equals("10d")||icon.equals("10n")) { //rain
			return R.drawable.w_small_rain;
			
		}else if (icon.equals("11d")||icon.equals("11n")) { //thunderstorm
			return R.drawable.w_small_thunderstorm;
			
		}else if (icon.equals("13d")||icon.equals("13n")) { //snow
			return R.drawable.w_small_snow;
			
		}else if (icon.equals("50d")||icon.equals("50n")) { //mist
			return R.drawable.w_small_mist;
			
		} else {
			return R.drawable.w_small_fewcloud;
		}
	}
	
	public static String getLytWidgetColor(String icon, String color[]){
	
		if (icon.equals("01d") || icon.equals("01n")) { // clear sky
			return color[0];
			
		} else if (icon.equals("02d") || icon.equals("02n")) { // few clouds
			return color[1];
			
		} else if (icon.equals("03d") || icon.equals("03n")) { // scatteredclouds
			return color[2];
			
		} else if (icon.equals("04d") || icon.equals("04n")) { // broken clouds
			return color[3];
			
		} else if (icon.equals("09d") || icon.equals("09n")) { // shower rain
			return color[4];
			
		} else if (icon.equals("10d") || icon.equals("10n")) { // rain
			return color[5];
			
		} else if (icon.equals("11d") || icon.equals("11n")) { // thunderstorm
			return color[6];
			
		} else if (icon.equals("13d") || icon.equals("13n")) { // snow
			return color[7];
			
		} else if (icon.equals("50d") || icon.equals("50n")) { // mist
			return color[8];
			
		} else {
			return color[9];
		}
	}
}
