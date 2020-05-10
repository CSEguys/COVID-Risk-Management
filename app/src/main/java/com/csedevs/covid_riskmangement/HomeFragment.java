package com.csedevs.covid_riskmangement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
private TextView tv_total_count,tv_cured_count,tv_death_count;
private RequestQueue req_queue;
private Calendar cal;
private SQLiteDatabase centers_db;
private  DatabaseHelper dbhelper;
private Cursor cursor;
private Spinner spinner;
private CentersAdapter centers_adapter;
private List<CenterData> center_list;
private  RecyclerView rv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View view =inflater.inflate(R.layout.fragment_home, container, false);
       tv_total_count=view.findViewById(R.id.total_count);
       tv_cured_count=view.findViewById(R.id.cured_count);
       tv_death_count=view.findViewById(R.id.death_count);


        req_queue= Volley.newRequestQueue(container.getContext());
        //String url1="https://api.covid19api.com/total/country/india?from=2020-05-05T00:00:00Z&to=2020-05-06T00:00:00Z";
        String url="https://api.covid19api.com/total/country/india?";
        cal= Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today=dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String yesterday=dateFormat.format(cal.getTime());

       /* LocalDate loc =LocalDate.now();
        String today=loc.toString();
        String yesterday=loc.minus(Period.ofDays(1)).toString();
        */

        String time="T00:00:00Z";
        url=url+"from="+yesterday+time+"&to="+today+time;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject response_obj=response.getJSONObject(0);
                            tv_total_count.setText(response_obj.getString("Confirmed"));
                            tv_cured_count.setText(response_obj.getString("Recovered"));
                            tv_death_count.setText(response_obj.getString("Deaths"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }
                }
        );
        req_queue.add(jsonArrayRequest);
        center_list=new ArrayList<>();
        //populate spinner
        spinner=view.findViewById(R.id.states_spinner);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity(),R.array.states,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //DB part
        dbhelper= new DatabaseHelper(getActivity());
        try {
            dbhelper.updateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        centers_db=dbhelper.getReadableDatabase();

        rv=view.findViewById(R.id.states_rv);
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lin);
        centers_adapter=new CentersAdapter(center_list);
        rv.setAdapter(centers_adapter);

        return  view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String query="SELECT * FROM sheet WHERE state_name=";
        query=query+"'"+adapterView.getItemAtPosition(i)+"'";
        cursor=centers_db.rawQuery(query,null);
        center_list.clear();
        if(cursor.moveToFirst()) {
            do {
                CenterData data= new CenterData(cursor.getString(cursor.getColumnIndex("name")));
                center_list.add(data);
                centers_adapter.notifyDataSetChanged();
            }
            while (cursor.moveToNext());

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
