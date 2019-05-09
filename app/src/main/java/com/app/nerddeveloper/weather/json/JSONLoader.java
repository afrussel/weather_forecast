package com.app.nerddeveloper.weather.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nerddeveloper.weather.ActivityMain;
import com.app.nerddeveloper.weather.data.Constant;
import com.app.nerddeveloper.weather.data.DatabaseManager;
import com.app.nerddeveloper.weather.data.GlobalVariable;
import com.app.nerddeveloper.weather.data.Utils;
import com.app.nerddeveloper.weather.model.City;
import com.app.nerddeveloper.weather.model.ForecastResponse;
import com.app.nerddeveloper.weather.model.ItemLocation;
import com.app.nerddeveloper.weather.model.WeatherResponse;
import com.google.gson.Gson;

public class JSONLoader extends AsyncTask<String, String, ItemLocation>{
	private JSONParser jsonParser = new JSONParser();
	private String jsonWeather = null, 
			jsonForecast= null, 
			status="null";
	
	private Context ctx;
	private LinearLayout lyt_form;
	private LinearLayout lyt_progress;
	private TextView tv_message; 
	private Dialog dialog;
	private DatabaseManager db;
	private GlobalVariable global;
	private ActivityMain act;
	
	public JSONLoader(ActivityMain act, LinearLayout lyt_form, LinearLayout lyt_progress, TextView tv_message, Dialog dialog) {
		this.act=act;
		this.ctx=act.getApplicationContext();
		this.lyt_form=lyt_form;
		this.lyt_progress=lyt_progress;
		this.tv_message=tv_message;
		this.dialog=dialog;
		global 	= (GlobalVariable) act.getApplication();
		db = new DatabaseManager(act);
	}


	@Override
	protected void onPreExecute() {
		lyt_form.setVisibility(View.GONE);
		lyt_progress.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	@Override
	protected ItemLocation doInBackground(String... params) {
		ItemLocation itemLocation 	= new ItemLocation();
		WeatherResponse weather		= new WeatherResponse();
		ForecastResponse forecast 	= new ForecastResponse();
		try {
			Thread.sleep(50);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String url_weather=null;
			String url_forecast=null;
			JSONObject json_weather 	= null;
			JSONObject json_forecast 	= null;
			if(Utils.isLocationBase(ctx)){
				Gson gson = new Gson();
				url_weather 	= Constant.getURLweather2(global.getLongLat("null"));
				url_forecast 	= Constant.getURLforecast2(global.getLongLat("null"));
				Log.d("URL w", url_weather);
				Log.d("URL f", url_forecast);
				json_weather 	= jsonParser.makeHttpRequest(url_weather,"POST", param);
				json_forecast 	= jsonParser.makeHttpRequest(url_forecast,"POST", param);
				jsonWeather 	= json_weather.toString();
				jsonForecast	= json_forecast.toString();
				WeatherResponse w = gson.fromJson(jsonWeather, WeatherResponse.class);
				itemLocation.setJsonWeather(jsonWeather);
				itemLocation.setJsonForecast(jsonForecast);
				itemLocation.setId(w.id+"");
				itemLocation.setName(w.name);
				itemLocation.setCode(w.sys.country);
				
				status="success";
			}else{
				City city = db.getWordsFormAutocomplate(params[0]);
				if(city!=null){
					url_weather 	= Constant.getURLweather(city.getId());
					url_forecast 	= Constant.getURLforecast(city.getId());
					itemLocation.setId(city.getId());
					itemLocation.setName(city.getName());
					itemLocation.setCode(city.getCode());
					json_weather 	= jsonParser.makeHttpRequest(url_weather,"POST", param);
					json_forecast 	= jsonParser.makeHttpRequest(url_forecast,"POST", param);
					jsonWeather 	= json_weather.toString();
					jsonForecast	= json_forecast.toString();
				
					itemLocation.setJsonWeather(jsonWeather);
					itemLocation.setJsonForecast(jsonForecast);
					
					status="success";
				}else{
					status="Invalid city name";
				}
			}
			
		} catch (Exception e) {
			status = "failed";
			e.printStackTrace();
		}
		
		return itemLocation;
	}
	
	protected void onPostExecute(ItemLocation result) {
		lyt_form.setVisibility(View.VISIBLE);
		lyt_progress.setVisibility(View.GONE);
		if(status.equals("success")){
			global.setCurrentId(result.getId());
			global.saveLocation(result);
			dialog.dismiss();
			act.displayData(result.getJsonWeather(), result.getJsonForecast());
		}
		tv_message.setText(status);
		//Toast.makeText(ctx, status, Toast.LENGTH_LONG).show();
	};

}
