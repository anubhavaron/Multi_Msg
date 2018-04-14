package com.example.tusharsk.multi_msg;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
/**
 * Created by tusharsk on 9/4/18.
 */


public class SaveSettings {
    public  static String email="";
    public  static String user_name="";
    public  static String phone_number="";
    public  static String password="";
    Context context;

    SharedPreferences ShredRef;
    public  SaveSettings(Context context){
        this.context=context;
        ShredRef=context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }

    void SaveData(String email,String user_name,String phone_number,String password){

        SharedPreferences.Editor editor=ShredRef.edit();
        editor.putString("email",email);
        editor.putString("user_name",user_name);
        editor.putString("phone_number",phone_number);
        editor.putString("password",password);


        editor.commit();
        LoadData();
    }

    void LoadData(){
        email= ShredRef.getString("email","0");
        user_name=ShredRef.getString("user_name","0");
        phone_number=ShredRef.getString("phone_number","0");
        password=ShredRef.getString("password","0");
        if (phone_number.equals("0")){

            Intent intent=new Intent(context, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
    String UserPresent()
    {
        phone_number= ShredRef.getString("phone_number","0");
        if(phone_number.equals("0"))
            return "0";
        else
            return phone_number;
    }

    void DeleteData()
    {
        SharedPreferences.Editor editor=ShredRef.edit();
        editor.clear();
        editor.commit();
    }
}