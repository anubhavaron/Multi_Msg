package com.example.tusharsk.multi_msg;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


   // Button btnSendSMS;



    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    static ArrayList<String> contact_name=new ArrayList<String>();
    static ArrayList<String> contact_number=new ArrayList<String>();
    SaveSettings saveSettings;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveSettings= new SaveSettings(getApplicationContext());
        saveSettings.LoadData();
        if(saveSettings.UserPresent().matches("0"))
        {
            finish();
        }
        setContentView(R.layout.activity_main);

        //btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        //txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        //txtMessage = (EditText) findViewById(R.id.txtMessage);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        contact_name.clear();
        contact_number.clear();
        load_contacts();
        super.onStart();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onRestart() {
        contact_name.clear();
        contact_number.clear();
        load_contacts();

        super.onRestart();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void load_contacts()
    {

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&  (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        //   StringBuilder builder=new StringBuilder();


    }




    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver contentResolver=getContentResolver();
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
                    Toast.makeText(getApplicationContext(),contact_name.size()+"",Toast.LENGTH_LONG).show();
                    cursor.close();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }



        }


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new contacts_f(), "Contacts");
        adapter.addFragment(new history_f(), "History");
        adapter.addFragment(new profile_f(), "Profile");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //---sends an SMS message to another device---





}
