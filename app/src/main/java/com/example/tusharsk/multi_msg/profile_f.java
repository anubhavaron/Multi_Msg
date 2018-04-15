package com.example.tusharsk.multi_msg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pc on 4/14/2018.
 */

public class profile_f extends Fragment {
    TextView name,email,phone_number,password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,null);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name=(TextView) view.findViewById(R.id.tvname);
        email=(TextView) view.findViewById(R.id.tvemail);
        phone_number=(TextView) view.findViewById(R.id.tvphone_number);
        password=(TextView) view.findViewById(R.id.tvpassword);
        String url="https://anubhavaron000001.000webhostapp.com/multi_msg_show.php?phone_number="+SaveSettings.phone_number;
        new MyAsyncTaskgetNews().execute(url);


    }



    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    Operations operations=new Operations(getActivity().getApplicationContext());
                    NewsData = operations.ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json= new JSONObject(progress[0]);
                //display response data
                if (json.getString("msg")==null)
                    return;
                if (json.getString("msg").equalsIgnoreCase("Yes")) {
                   // Toast.makeText(getActivity().getApplicationContext(), "Info", Toast.LENGTH_LONG).show();
                    //login

                    JSONArray UserInfo=new JSONArray( json.getString("info"));
                    JSONObject UserCreintal= UserInfo.getJSONObject(0);

                    //Toast.makeText(getActivity().getApplicationContext(), UserCreintal.getString("phone_number"), Toast.LENGTH_LONG).show();
                    name.setText("Name: "+UserCreintal.getString("name"));
                    email.setText("Email: "+UserCreintal.getString("email"));
                    phone_number.setText("Phone No.: "+UserCreintal.getString("phone_number"));
                    password.setText("Password: "+UserCreintal.getString("password"));
                }

                if (json.getString("msg").equalsIgnoreCase("No")) {

                    Toast.makeText(getActivity().getApplicationContext(),"WRONG EMAIL OR PASSWORD",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                //Log.d("er",  ex.getMessage());
            }


        }

        protected void onPostExecute(String  result2){


        }

    }
}
