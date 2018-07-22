package com.example.win10.giveandtake.UI;

import android.app.DialogFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.win10.giveandtake.R;

public class SetMeetingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meeting);

        Button pickDate = (Button) findViewById(R.id.btn_date);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showTimePickerDialog(v);
                showDatePickerDialog(v);

            }
        });
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


}
