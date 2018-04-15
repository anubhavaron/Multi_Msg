package com.example.tusharsk.multi_msg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pc on 4/14/2018.
 */

public class history_f extends Fragment {
    ArrayList<String> date=new ArrayList<String>();
    ArrayList<String> time=new ArrayList<String>();
    ArrayList<String> msg=new ArrayList<String>();
    RecyclerView recyclerView;
    History_adapter history_adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history,null);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_9);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);




        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        history_adapter=new History_adapter(getActivity());
        recyclerView.setAdapter(history_adapter);
        Background_history background_history=new Background_history();
        background_history.execute();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });






    }

    @Override
    public void onResume() {
        super.onResume();
        time.clear();
        date.clear();
        msg.clear();
        Background_history background_history=new Background_history();
        background_history.execute();

    }



    void refreshItems() {
        // Load items
        // ...

        // Load complete
        time.clear();
        date.clear();
        msg.clear();
        Background_history background_history=new Background_history();
        background_history.execute();
        mSwipeRefreshLayout.setRefreshing(false);

    }




    class Background_history extends AsyncTask<Void,Void,String>
    {   String json_url="https://anubhavaron000001.000webhostapp.com/history_fetching.php";

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String JSON_STRING) {
            time.clear();
            date.clear();
            msg.clear();
            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;
                jsonArray=jsonObject.getJSONArray("server response");

                String phone_number_a;

                int c=0;

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    phone_number_a=JO.getString("phone_number");

                    if(phone_number_a.equals(SaveSettings.phone_number))
                    {
                        c++;
                    }


                    count++;
                }

                count=0;


                c=0;
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    phone_number_a=JO.getString("phone_number");

                    if(phone_number_a.equals(SaveSettings.phone_number))
                    {   date.add(JO.getString("date"));
                        msg.add(JO.getString("msg"));
                        time.add(JO.getString("time"));


                        c++;
                    }


                    count++;
                }

                history_adapter.swapCursor(date,time,msg);
                //  history_adapter.swapCursor(cab_no,destination);







            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(JSON_STRING);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
