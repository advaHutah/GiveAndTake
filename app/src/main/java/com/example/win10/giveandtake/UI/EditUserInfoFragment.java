package com.example.win10.giveandtake.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.win10.giveandtake.DBLogic.FirebaseManager;
import com.example.win10.giveandtake.Logic.AppManager;
import com.example.win10.giveandtake.R;

public class EditUserInfoFragment extends Fragment {

    private View view;
    private EditText inputFirstName, inputLastName, inputPhoneNumber;
    RadioGroup inputGenderGroup;
    RadioButton inputGenderButton;
    private String gender;
    private Button confirm_btn;
    private AppManager appManager;
    private FirebaseManager firebaseManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        appManager = AppManager.getInstance();
        firebaseManager = FirebaseManager.getInstance();

        inputFirstName = (EditText) view.findViewById(R.id.first_name_text_edit);
        inputLastName = (EditText) view.findViewById(R.id.last_name_text_edit);
        inputPhoneNumber = (EditText) view.findViewById(R.id.phone_text_edit);
        confirm_btn = (Button) view.findViewById(R.id.edit_user_info_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputFirstName.getText().toString().equals("") && !inputLastName.getText().toString().equals("") && !inputPhoneNumber.getText().toString().equals("") ) {
                    firebaseManager.updateUserInfoInDB(appManager.getCurrentUser().getId(), inputFirstName.getText().toString(), inputLastName.getText().toString(), inputPhoneNumber.getText().toString());
                    //change fragment to defult
                    UserHomeDefultFragment userHomeDefultFragment = new UserHomeDefultFragment();
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.content_frame, userHomeDefultFragment)
//                            .commit();
                }
            }
        });

        return view;
    }

}
