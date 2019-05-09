package com.app.nerddeveloper.weather.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.nerddeveloper.weather.model.City;

public class DatabaseManager extends SQLiteOpenHelper   {

	private static final String DATABASE_NAME = "db_city.sqlite";
	private static final String TABLE_NAME = "city_list";
	
	// Contacts table name
	//private static final String TABLE_DATA = "table_data";
	
	private static final int DATABASE_VERSION = 1;
	
	
	public static SQLiteDatabase db;
	private DataBaseHelper dbHelper;
	public static Context context;
	//public GlobalVariable global;
	
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		//Activity act = (Activity) context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME);
		db = dbHelper.openDataBase();
		//createTable(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close() {
		db.close();
	}
	
	
	
	public ArrayList<City> getCity() {
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME;
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				City city = new City();
				city.setId(c.getString(0));
				city.setName(c.getString(1));
				city.setLat(c.getString(2));
				city.setLng(c.getString(3));
				city.setCode(c.getString(4));
				data.add(city);
			} while (c.moveToNext());
		}
		db.close();
		return data;

	}
	public ArrayList<City> getWords(String string) {
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE name LIKE '"+string+"%' OR name LIKE '"+string+"' LIMIT 5";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				City city = new City();
				city.setId(c.getString(0));
				city.setName(c.getString(1));
				city.setLat(c.getString(2));
				city.setLng(c.getString(3));
				city.setCode(c.getString(4));
				data.add(city);
			} while (c.moveToNext());
		}
		db.close();
		return data;
	}
	public City getWordsFormAutocomplate(String s) {
		City city = null;
		String scity = "", scode="";
		if(s.contains(",")){
			scity=s.split("\\,")[0];
			scode=s.split("\\,")[1].trim();
		}else{
			return null;
		}
		
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE name= '"+scity+"' AND code= '"+scode+"'";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			city = new City();
			city.setId(c.getString(0));
			city.setName(c.getString(1));
			city.setLat(c.getString(2));
			city.setLng(c.getString(3));
			city.setCode(c.getString(4));
		}
		
		db.close();
		return city;
	}
}
