package com.example.tusharsk.multi_msg;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pc on 4/14/2018.
 */

public class contacts_f extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Button u;
    ArrayList<String> contact_name=new ArrayList<String>();
    ArrayList<String> contact_number=new ArrayList<String>();
    Button button;
    AutoCompleteTextView contacts;
    ArrayAdapter<String> arrayList;
    ArrayList<String> name_new=new ArrayList<String>();
    ArrayList<String> contact_new=new ArrayList<String>();
    EditText number;
    RecyclerView recyclerView;
    contacts_list_dapter contacts_list_dapter;
    Button b1;


    EditText txtPhoneNo;
    EditText txtMessage;

    String phoneNo;
    String message;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1,REQUEST_READ_PHONE_STATE=2;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts,null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contacts=(AutoCompleteTextView) view.findViewById(R.id.spinner_2);
        button=(Button)view.findViewById(R.id.add_contact_number_3);
        b1=(Button)view.findViewById(R.id.add_phone_number_3);
        number=(EditText)view.findViewById(R.id.phone_number_3);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_2);
        u=(Button)view.findViewById(R.id.u);
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });


        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        contacts_list_dapter=new contacts_list_dapter(getActivity());
        recyclerView.setAdapter(contacts_list_dapter);
        //load_contacts();
        contact_name=MainActivity.contact_name;
        contact_number=MainActivity.contact_number;

        sentPI = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, new Intent(DELIVERED), 0);

        arrayList = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, contact_name);
        arrayList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contacts.setAdapter(arrayList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getActivity().getApplicationContext(),contact_number.get(contact_name.indexOf(contacts.getText().toString())),Toast.LENGTH_LONG).show();
                name_new.add(contacts.getText().toString());
                contact_new.add(contact_number.get(contact_name.indexOf(contacts.getText().toString())));
                contacts_list_dapter.swapCursor(name_new,contact_new);


            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_new.add("no name");
                contact_new.add(number.getText().toString());
                contacts_list_dapter.swapCursor(name_new,contact_new);
            }
        });



    }

    void load_contacts()
    {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && AppCompatActivity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }*/
        //   StringBuilder builder=new StringBuilder();
        ContentResolver contentResolver=getActivity().getContentResolver();
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    /*Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);*/

                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?",
                            new String[] { id },
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

                    while (cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //  builder.append("Contact : ").append(name).append(",Phone Number : ").append(phoneNumber).append("\n");
                        contact_name.add(name);
                        contact_number.add(phoneNumber);

                    }
                    cursor2.close();
                }
            }
        }
        Toast.makeText(getActivity().getApplicationContext(),contact_name.size()+"",Toast.LENGTH_LONG).show();
        cursor.close();

    }



    public void send()
    {
        // phoneNo = txtPhoneNo.getText().toString();
        //message = txtMessage.getText().toString();

        Toast.makeText(getActivity().getApplicationContext(), "buttone clicked", Toast.LENGTH_SHORT).show();


        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
        else {
            // checkPermission();

            sendSMS(phoneNo,message);
        }

    }

    void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getActivity().getApplicationContext(), "SMS sent.",
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
                    Toast.makeText(getActivity().getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(phoneNo,message);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        }


    }


    private void sendSMS(String phoneNumber, String message)
    {
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
                new Intent(getActivity().getApplicationContext(),MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity().getApplicationContext(), "SMS send", Toast.LENGTH_LONG).show();

    }





}
