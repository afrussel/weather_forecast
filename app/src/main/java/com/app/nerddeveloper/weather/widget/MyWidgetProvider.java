package com.app.nerddeveloper.weather.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.app.nerddeveloper.weather.R;
import com.app.nerddeveloper.weather.data.ConnectionDetector;
import com.app.nerddeveloper.weather.data.Constant;
import com.app.nerddeveloper.weather.data.Utils;
import com.app.nerddeveloper.weather.json.JSONParser;
import com.app.nerddeveloper.weather.model.ItemLocation;
import com.app.nerddeveloper.weather.model.WeatherResponse;
import com.google.gson.Gson;

@SuppressLint("NewApi")
public class MyWidgetProvider extends AppWidgetProvider {
	private ConnectionDetector cd;
	Gson gson = new Gson();
	String color[];
	private PendingIntent service = null;  
	private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		cd = new ConnectionDetector(context);
		color = context.getResources().getStringArray(R.array.color_weather);
		
		ItemLocation itemloca=Utils.getLocation(context);
		try {
			displayData(itemloca,context);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//new JSONLoad(itemloca, context).execute("");
		//Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
	}
	
	public void displayData(ItemLocation itemloc, Context context){
		color = context.getResources().getStringArray(R.array.color_weather);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		WeatherResponse weather = gson.fromJson(itemloc.getJsonWeather(), WeatherResponse.class);
		//views.setInt(R.id.lyt_w_bg, "setBackgroundColor", Color.parseColor("#000000"));
		views.setInt(R.id.lyt_w_bg, "setBackgroundColor", Color.parseColor(Constant.getLytWidgetColor(weather.weather.get(0).icon, color)));
		views.setOnClickPendingIntent(R.id.bt_w_refresh, getPendingSelfIntent(context, SYNC_CLICKED));
		if(Utils.isLocationBase(context)){
			views.setTextViewText(R.id.tv_w_address, "My Location");
		}else{
			views.setTextViewText(R.id.tv_w_address, weather.name+", "+weather.sys.country);
		}
		views.setTextViewText(R.id.tv_w_day, Utils.getDay(weather.dt, context));
		views.setTextViewText(R.id.tv_w_condition, weather.weather.get(0).main);
		views.setTextViewText(R.id.tv_w_temp, Utils.getTemp(weather.main.temp, context));
		views.setInt(R.id.img_icon, "setBackgroundResource", Constant.getDrawableWidgetIcon(weather.weather.get(0).icon));
		
		//Toast.makeText(context, ""+Constant.getLytWidgetColor(weather.weather.get(0).icon,color), Toast.LENGTH_SHORT).show();
			
		//}
		ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thisWidget, views);
	}

	@Override  
    public void onDisabled(Context context)  {  
//        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
//        m.cancel(service);  
    }  
	
	public class JSONLoad extends AsyncTask<String, String, String>{
		ItemLocation itemLocation;
		private JSONParser jsonParser = new JSONParser();
		private String jsonWeather = null, status="null";
		private Context ctx;
		
		public JSONLoad(ItemLocation itemLocation, Context ctx){
			this.itemLocation=itemLocation;
			this.ctx=ctx;
			Toast.makeText(ctx, "Loading..", Toast.LENGTH_SHORT).show();
		}
		@Override
		protected String doInBackground(String... params) {
			try {
				String url_weather=null;
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				if(Utils.isLocationBase(ctx)){
					url_weather = Constant.getURLweather2(Utils.getStringPref(Constant.S_KEY_LNG_LAT, "null", ctx));
				}else{
					url_weather = Constant.getURLweather(itemLocation.getId());
				}
				JSONObject json_weather 	= jsonParser.makeHttpRequest(url_weather,"POST", param);
				jsonWeather 	= json_weather.toString();
				status="success";
			} catch (Exception e) {
				status="failed";
			}
			return null;
		}
	
		@Override
		protected void onPostExecute(String result) {
			if(status=="success"){
				WeatherResponse weather = gson.fromJson(jsonWeather, WeatherResponse.class);
				try {
					itemLocation.setJsonWeather(jsonWeather);
					displayData(itemLocation,ctx);
					Utils.saveLocation(itemLocation, ctx);
					Utils.setStringPref(Constant.S_KEY_CURRENT_ID, itemLocation.getId(), ctx);
					Toast.makeText(ctx, "Widget updated", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(ctx, "Failed converting data", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(ctx, "Failed retrive data", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	
	}
	
	protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
	
	@Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {
        	cd = new ConnectionDetector(context);
        	if(cd.isConnectingToInternet()){
        		new JSONLoad(Utils.getLocation(context), context).execute("");
    		}else{
    			Toast.makeText(context, "Internet is offline", Toast.LENGTH_SHORT).show();
    		}
        	
        }
    }
	
	public String getLastUpdate(Long l){
		Date curDate = new Date(l *1000);
		//Wed, 4 Jul 2001 12:08:56 -0700
		SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yyyy HH:mm");
		String dateToStr = format.format(curDate);
		return dateToStr;
	}
}
