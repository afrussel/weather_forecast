package com.app.nerddeveloper.weather;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.nerddeveloper.weather.adapter.PlacesAutoCompleteAdapter;
import com.app.nerddeveloper.weather.data.ConnectionDetector;
import com.app.nerddeveloper.weather.data.Constant;
import com.app.nerddeveloper.weather.data.GPSTracker;
import com.app.nerddeveloper.weather.data.GlobalVariable;
import com.app.nerddeveloper.weather.data.Utils;
import com.app.nerddeveloper.weather.json.JSONLoader;
import com.app.nerddeveloper.weather.json.JSONParser;
import com.app.nerddeveloper.weather.model.ForecastResponse;
import com.app.nerddeveloper.weather.model.ItemForecast;
import com.app.nerddeveloper.weather.model.ItemLocation;
import com.app.nerddeveloper.weather.model.WeatherResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;

public class ActivityMain extends Activity {

    private TextView tv_temp, tv_desc, tv_city, tv_day;
    private Button bt_refresh, bt_theme, bt_map;
    private ImageView img_icon;
    private LinearLayout listview;
    private RelativeLayout lyt_bg;
    private ProgressBar progressbar;

    private ConnectionDetector cd;
    private GlobalVariable global;
    // GPS
    private GPSTracker gps;
  //  private AdView mAdView;

    //for ads
    private InterstitialAd mInterstitialAd;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        global = (GlobalVariable) getApplication();

        switch (global.getIntTheme()) {
            case 0:
                setContentView(R.layout.activity_main_style_1);

                break;
            case 1:
                setContentView(R.layout.activity_main_style_2);
                break;
            case 2:
                setContentView(R.layout.activity_main_style_3);
                break;
            case 3:
                setContentView(R.layout.activity_main_style_4);
                break;
            case 4:
                setContentView(R.layout.activity_main_style_5);
                break;
            default:
                setContentView(R.layout.activity_main_style_1);
                break;
        }

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_day = (TextView) findViewById(R.id.tv_day);
        bt_refresh = (Button) findViewById(R.id.bt_refresh);
        bt_theme = (Button) findViewById(R.id.bt_theme);
        bt_map = (Button) findViewById(R.id.bt_map);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        lyt_bg = (RelativeLayout) findViewById(R.id.lyt_bg);
        listview = (LinearLayout) findViewById(R.id.listview);


        /*mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });*/

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        //prepare ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);


        cd = new ConnectionDetector(getApplicationContext());


        firstGetData();

        bt_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //fullScreenAds();
                showInterstitial();
                new LoadJson().execute("");
            }
        });
        bt_theme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThemeChooser();
            }
        });

        tv_city.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                dialogAddLocation();
            }
        });

        tv_temp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                dialogUnit();
            }
        });
        bt_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityMap.class);
                startActivity(i);
            }
        });
    }

    public void firstGetData() {
        if (global.getCurrentId().equals("null")) {
            new LoadJson().execute("");
        } else {
            Gson gson = new Gson();
            ItemLocation itemloc = global.getLocation();
            displayData(itemloc.getJsonWeather(), itemloc.getJsonForecast());
        }
    }

    public void fullScreenAds() {
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }


    public void displayData(String jsonW, String jsonF) {
        try {
            WeatherResponse w = gson.fromJson(jsonW, WeatherResponse.class);
            ForecastResponse f = gson.fromJson(jsonF, ForecastResponse.class);
            if (Utils.isLocationBase(getApplicationContext())) {
                tv_city.setText("My Location");
            } else {
                tv_city.setText(w.name + ", " + w.sys.country);
            }
            tv_temp.setText(global.getTemp(w.main.temp));
            tv_desc.setText(w.weather.get(0).main.toUpperCase());
            tv_day.setText(global.getDay(w.dt));
            global.setDrawableIcon(w.weather.get(0).icon, img_icon);
            global.setLytColor(w.weather.get(0).icon, lyt_bg);
            // prepare for list forecast
            ArrayList<ItemForecast> forecasts = new ArrayList<ItemForecast>();
            for (int i = 1; i < 7; i++) {
                ItemForecast fcs = new ItemForecast();
                fcs.setTemp(global.getTemp(f.list.get(i).temp.day));
                fcs.setDay(global.getDay(f.list.get(i).dt));
                fcs.setDesc(f.list.get(i).weather.get(0).main);
                fcs.setIcon(f.list.get(i).weather.get(0).icon);
                forecasts.add(fcs);
            }
            //listview.setAdapter(new ItemForecastAdapter(ActivityMain.this, forecasts));
            setViewList(forecasts);
            ItemLocation itemloc = new ItemLocation();
            itemloc.setId(w.id + "");
            itemloc.setName(w.name);
            itemloc.setCode(w.sys.country);
            itemloc.setJsonWeather(jsonW);
            itemloc.setJsonForecast(jsonF);
            global.saveLocation(itemloc);
            global.setCurrentId(itemloc.getId());
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.s_update), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.f_read), Toast.LENGTH_SHORT).show();
        }

    }


    public void setViewList(ArrayList<ItemForecast> forecasts) {
        LayoutInflater inflater = LayoutInflater.from(this);
        listview.removeAllViews();
        for (ItemForecast obje : forecasts) {
            View view;
            switch (global.getIntTheme()) {
                case 0:
                    view = inflater.inflate(R.layout.list_item_forecast_style_1, listview, false);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.list_item_forecast_style_2, listview, false);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.list_item_forecast_style_3, listview, false);
                    break;
                default:
                    view = inflater.inflate(R.layout.list_item_forecast_style_1, listview, false);
                    break;
            }

            ((TextView) view.findViewById(R.id.tv_f_temp)).setText(obje.getTemp());
            ((TextView) view.findViewById(R.id.tv_f_day)).setText(obje.getDay());
            ((TextView) view.findViewById(R.id.tv_f_desc)).setText(obje.getDesc());
            ImageView img = (ImageView) view.findViewById(R.id.img_f_icon);
            global.setDrawableSmallIcon(obje.getIcon(), img);

            listview.addView(view);
        }
    }

    public class LoadJson extends AsyncTask<String, String, String> {
        JSONParser jsonParser = new JSONParser();
        String jsonWeather = null,
                jsonForecast = null,
                status = "null";

        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            bt_refresh.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(50);
                if (cd.isConnectingToInternet()) {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    String url_weather = null;
                    String url_forecast = null;
                    if (Utils.isLocationBase(getApplicationContext())) {
                        url_weather = Constant.getURLweather2(global.getLongLat("null"));
                        url_forecast = Constant.getURLforecast2(global.getLongLat("null"));
                    } else {
                        url_weather = Constant.getURLweather(global.getCurrentId());
                        url_forecast = Constant.getURLforecast(global.getCurrentId());
                    }

                    JSONObject json_weather = jsonParser.makeHttpRequest(url_weather, "POST", param);
                    JSONObject json_forecast = jsonParser.makeHttpRequest(url_forecast, "POST", param);

                    jsonWeather = json_weather.toString();
                    jsonForecast = json_forecast.toString();
                    status = "success";
                } else {
                    status = "offline";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (status == "success") {
                displayData(jsonWeather, jsonForecast);
            } else if (status == "offline") {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.offline), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.f_retrive), Toast.LENGTH_SHORT).show();
            }
            progressbar.setVisibility(View.GONE);
            bt_refresh.setVisibility(View.VISIBLE);
            super.onPostExecute(result);
        }

    }

    boolean flag_usingps = false;
    double userLat, userLng;

    protected void dialogAddLocation() {
        flag_usingps = false;
        final Dialog dialog = new Dialog(ActivityMain.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_set_location);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_no = (Button) dialog.findViewById(R.id.button_no);
        final ToggleButton buttonGetLoc = (ToggleButton) dialog.findViewById(R.id.buttonDirLoc);
        ;
        final Button button_yes = (Button) dialog.findViewById(R.id.button_yes);
        final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
        final LinearLayout lyt_form = (LinearLayout) dialog.findViewById(R.id.lyt_form);
        final LinearLayout lyt_progress = (LinearLayout) dialog.findViewById(R.id.lyt_progress);
        final AutoCompleteTextView address = (AutoCompleteTextView) dialog.findViewById(R.id.address);
        address.setAdapter(new PlacesAutoCompleteAdapter(ActivityMain.this, android.R.layout.simple_dropdown_item_1line));
        button_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_usingps) {
                    global.setLongLat(userLat + "|" + userLng);
                } else {
                    global.setLongLat("null");
                }
                if (!address.getText().toString().trim().equals("")) {
                    if (!address.getText().toString().trim().equals("0.0,0.0")) {
                        if (cd.isConnectingToInternet()) {
                            JSONLoader jsload = new JSONLoader(ActivityMain.this, lyt_form, lyt_progress, tv_message, dialog);
                            jsload.execute(address.getText().toString());
                        } else {
                            tv_message.setText("Internet is offline");
                        }
                    } else {
                        tv_message.setText("Invalid location, please try again");
                    }
                } else {
                    tv_message.setText("Please fill location");
                }
            }
        });
        buttonGetLoc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    tv_message.setText("-");
                    // creating GPS Class object
                    gps = new GPSTracker(ActivityMain.this);
                    // check if GPS location can get
                    if (gps.canGetLocation()) {
                        v.setBackgroundResource(R.drawable.ic_action_location_found_green);
                        userLat = gps.getLatitude();
                        userLng = gps.getLongitude();
                        String userLoc = "" + userLat + "," + userLng;
                        address.setText(userLoc);
                        address.setEnabled(false);
                        flag_usingps = true;
                    } else {
                        buttonGetLoc.setChecked(false);
                    }

                } else {
                    v.setBackgroundResource(R.drawable.ic_action_location_found);
                    address.setEnabled(true);
                    tv_message.setText("-");
                    address.setText("");
                    flag_usingps = false;
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    AlertDialog the_dialog = null;
    String[] string_unit;

    public void dialogUnit() {

        // Creating and Building the Dialog 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        string_unit = getResources().getStringArray(R.array.string_unit);
        builder.setTitle("Select Unit Temperature");
        builder.setSingleChoiceItems(string_unit, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        global.setIntUnit(0);
                        break;
                    case 1:
                        global.setIntUnit(1);
                        break;
                }
                firstGetData();
                the_dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                the_dialog.dismiss();
            }
        });
        the_dialog = builder.create();
        the_dialog.show();
    }

    String[] string_theme;

    public void dialogThemeChooser() {

        // Creating and Building the Dialog 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        string_theme = getResources().getStringArray(R.array.string_theme);
        builder.setTitle("Select Display Theme");
        builder.setSingleChoiceItems(string_theme, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        global.setIntTheme(0);
                        break;
                    case 1:
                        global.setIntTheme(1);
                        break;
                    case 2:
                        global.setIntTheme(2);
                        break;
                    case 3:
                        global.setIntTheme(3);
                        break;
                    case 4:
                        global.setIntTheme(4);
                        break;
                }
                Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                the_dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                the_dialog.dismiss();
            }
        });
        the_dialog = builder.create();
        the_dialog.show();
    }

    /**
     * show ads
     */
    public void showInterstitial() {
        // Show the ad if it's ready
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

   /* @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }*/
}
