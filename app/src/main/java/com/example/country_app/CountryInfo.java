package com.example.country_app;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryInfo extends AppCompatActivity {

    ListView listview_country;
    ImageView ivCountryFlag;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        listview_country = findViewById(R.id.listview_country);
        ivCountryFlag = findViewById(R.id.ivCountryFlag);
//        webView = findViewById(R.id.webView);
        String countryName = this.getIntent().getStringExtra("CountryKey");
        if(countryName.isEmpty()){
            countryName = "Philippines";
        }
        ArrayAdapter arrayAdapter;
        Log.d("COUNTRY", countryName);

        RequestQueue queue = Volley.newRequestQueue(CountryInfo.this);
        String url ="https://restcountries.com/v3.1/name/"+countryName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    JSONObject countryInfo = response.getJSONObject(0);
                    JSONObject common = countryInfo.getJSONObject("name");
                    JSONObject flag = countryInfo.getJSONObject("flags");
                    JSONArray capital = response.getJSONObject(0).getJSONArray("capital");
                    Integer totalPopulation = response.getJSONObject(0).getInt("population");
                    Log.d("TOTALPOPULATION", String.valueOf(totalPopulation));
                    JSONObject gMaps = countryInfo.getJSONObject("maps");
                    String gMapWeb = gMaps.getString("googleMaps");
                    Log.d("MAPS", gMapWeb);
                    String cFlag = flag.getString("png");
                    String cCommonName = common.getString("common");
                    String cOfficialName = common.getString("official");
                    String cCapital = capital.toString().replaceAll("[^\\p{L}\\p{Z}]","");
                    List<String> countryResult = new ArrayList<String>();
                    ImageView ivBasicImage = (ImageView) findViewById(R.id.ivCountryFlag);
                    Picasso.get().load(cFlag).into(ivBasicImage);
                    countryResult.add("Common Name: "+ cCommonName);
                    countryResult.add("Official Name: "+ cOfficialName);
                    countryResult.add("Capital: "+ cCapital);
                    countryResult.add("Total Population: "+ totalPopulation);
                    ArrayAdapter arrayAdapter =
                            new ArrayAdapter(CountryInfo.this,android.R.layout.simple_list_item_1, countryResult);
                    listview_country.setAdapter(arrayAdapter);
//                    WebSettings webSettings = webView.getSettings();
//                    webSettings.setJavaScriptEnabled(true);
//                    webView.loadUrl(gMapWeb);
                } catch (Exception e){
                    Log.d("EXCEPTION", e.toString());
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CountryInfo.this, "No country found", Toast.LENGTH_LONG);
            }
        });

        queue.add(request);
    }
}
