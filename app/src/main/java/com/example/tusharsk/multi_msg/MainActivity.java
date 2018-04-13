package com.example.tusharsk.multi_msg;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {


   // Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;

    String phoneNo;
    String message;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1,REQUEST_READ_PHONE_STATE=2;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);


        sentPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(DELIVERED), 0);


    }


    //---sends an SMS message to another device---


    public void send(View view)
    {
         phoneNo = txtPhoneNo.getText().toString();
         message = txtMessage.getText().toString();
        Toast.makeText(getApplicationContext(), "buttone clicked", Toast.LENGTH_SHORT).show();


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
        else {
           // checkPermission();

            sendSMS(phoneNo,message);
        }

    }

    void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();

            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(phoneNo,message);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        }


    }


    private void sendSMS(String phoneNumber, String message)
    {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this,MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS send", Toast.LENGTH_LONG).show();

    }







}
