package com.app.nerddeveloper.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.nerddeveloper.weather.data.Constant;
import com.app.nerddeveloper.weather.data.GlobalVariable;

public class ActivityMap extends Activity {
	WebView webview;
	ProgressBar progress_load;
	GlobalVariable global;
	Spinner spinner_mode;
	Button bt_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		global 			= (GlobalVariable) getApplication();
		webview 		= (WebView) findViewById(R.id.webView1);
		spinner_mode	= (Spinner) findViewById(R.id.spinner1);
		progress_load 	= (ProgressBar) findViewById(R.id.progress_load);
		bt_back			= (Button) findViewById(R.id.bt_back);
		
		loadWeb();
		final String code_map[] = getResources().getStringArray(R.array.map_code_array);
		spinner_mode.setSelection(global.getMapCodeIndex());
		spinner_mode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				global.setMapCode(code_map[position]);
				global.setMapCodeIndex(position);
				loadWeb();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	public void loadWeb(){
		progress_load.setVisibility(View.VISIBLE);
		try {
			WebSettings settings = webview.getSettings();
			settings.setJavaScriptEnabled(true);
			webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			webview.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					Log.i("TAG", "Processing webview url click...");
					view.loadUrl(url);
					return true;
				}

				public void onPageFinished(WebView view, String url) {
					Log.i("TAG", "Finished loading URL: " + url);
					progress_load.setVisibility(View.GONE);
				}

				public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
					Log.e("TAG", "Error: " + description);
					Toast.makeText(ActivityMap.this, "Failed " + description,Toast.LENGTH_SHORT).show();
					alertDialog.setTitle("Error");
					alertDialog.setMessage(description);
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
					alertDialog.show();
				}
			});
			webview.loadUrl(Constant.getURLmap(global.getMapCode()));
		} catch (Exception e) {
			progress_load.setVisibility(View.GONE);
			Toast.makeText(ActivityMap.this, "Cannot view map",Toast.LENGTH_SHORT).show();
		}
		
	}

}
