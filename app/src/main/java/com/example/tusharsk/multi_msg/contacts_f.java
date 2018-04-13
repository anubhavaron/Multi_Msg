package com.example.tusharsk.multi_msg;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        contacts_list_dapter=new contacts_list_dapter(getActivity());
        recyclerView.setAdapter(contacts_list_dapter);
        //load_contacts();
        contact_name=MainActivity.contact_name;
        contact_number=MainActivity.contact_number;

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

}
