package com.example.tusharsk.multi_msg;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pc on 4/14/2018.
 */

public class Background_adding_history extends AsyncTask<String,Void,String> {
    Context context;
    String url_insert="https://anubhavaron000001.000webhostapp.com/adding_history_multimsg.php";

    public Background_adding_history(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String userid=strings[0];
        String cab_no=strings[1];
        String time=strings[2];
        String date=strings[3];


        try {
            URL url=new URL(url_insert);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data= URLEncoder.encode("phone_number","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"+
                    URLEncoder.encode("msg","UTF-8")+"="+URLEncoder.encode(cab_no,"UTF-8")+"&"+
                    URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8")+"&"+
                    URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream IS=httpURLConnection.getInputStream();
            IS.close();
            return "Added ";



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}