package com.example.country_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_search;
    EditText et_countryinput;
    ListView lv_countries;
    ImageView iv_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       btn_search = findViewById(R.id.btn_search);
       et_countryinput = findViewById(R.id.editTextCountry);
       lv_countries = findViewById(R.id.listview_countries);
//       iv_flag = findViewById(R.id.ivCountryFlags);

        ArrayList<String> countryName = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="https://restcountries.com/v3.1/all";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    int clength = response.length();
                for(int i = 0; i < clength ; i++){
                    try{
                        JSONObject countryInfo = response.getJSONObject(i);
                        JSONObject common = countryInfo.getJSONObject("name");
//                            JSONObject flag = countryInfo.getJSONObject("flags");
//                            String cFlag = flag.getString("png");
                        String cCommonName = common.getString("common");
//                            String cOfficialName = common.getString("official");
                        Log.d("COUNTRY", cCommonName);
                        countryName.add(cCommonName);
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, countryName);
                        ListView listView = (ListView) lv_countries;
                        listView.setAdapter(adapter);
                        listView.setClickable(true);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String s = listView.getItemAtPosition(position).toString();
                                Intent cInfoIntent = new Intent(MainActivity.this, CountryInfo.class);
                                cInfoIntent.putExtra("CountryKey",s);
                                view.getContext().startActivity(cInfoIntent);
                            }
                        });
//                        List<String> countryResult = new ArrayList<String>();
//                        countryResult.add(""+i+". "+ cCommonName);
//                        ArrayAdapter arrayAdapter =
//                                new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1, countryResult);
//                        lv_countries.setAdapter(arrayAdapter);
                    } catch (Exception e){
                        Log.d("EXCEPTION", e.toString());
                    }
                    }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

       btn_search.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               if(et_countryinput.getText().toString() == null || et_countryinput.getText().toString().isEmpty() || et_countryinput.getText().toString() == "country"){
                   Toast.makeText(MainActivity.this, "Select or search a country", Toast.LENGTH_LONG);
               } else{
                   Intent cInfoIntent = new Intent(MainActivity.this, CountryInfo.class);
                   cInfoIntent.putExtra("CountryKey",et_countryinput.getText().toString());
                   view.getContext().startActivity(cInfoIntent);
               }
           }
       });



    }
    
}