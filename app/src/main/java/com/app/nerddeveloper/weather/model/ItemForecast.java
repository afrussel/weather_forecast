package com.app.nerddeveloper.weather.model;

public class ItemForecast {

	private String day;
	private String temp;
	private String desc;
	private String icon;
	public ItemForecast() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ItemForecast(String day, String temp, String desc, String icon) {
		super();
		this.day = day;
		this.temp = temp;
		this.desc = desc;
		this.icon = icon;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}
