package com.example.tusharsk.multi_msg;

/**
 * Created by tusharsk on 14/4/18.
 */


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class dialoge_message extends DialogFragment {

    private static final String TAG = "MyCustomDialog";


    //widgets
    private EditText mInput;
    private Button mActionOk;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialoge_message, container, false);
        mActionOk = (Button)view.findViewById(R.id.btsend);

        mInput = (EditText) view.findViewById(R.id.etmessage);


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input.");

                String input = mInput.getText().toString();
                if(!input.equals("")){


                }


                getDialog().dismiss();
            }
        });

        return view;
    }


}







