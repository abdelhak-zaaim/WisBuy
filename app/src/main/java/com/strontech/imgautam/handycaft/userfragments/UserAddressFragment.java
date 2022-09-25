package com.strontech.imgautam.handycaft.userfragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.activities.byMethod;
import com.strontech.imgautam.handycaft.helper.InputValidation;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddressFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbarUserAddressFragment;

    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutUserMobNumber;
    private TextInputLayout textInputLayoutUserEmail;
    private TextInputLayout textInputLayoutUserHouseNo;
    private TextInputLayout textInputLayoutUserLocality;
    private TextInputLayout textInputLayoutUserPincode;

    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextUserMobNumber;
    private TextInputEditText textInputEditTextUserEmail;
    private TextInputEditText textInputEditTextUserHouseNo;
    private TextInputEditText textInputEditTextUserLocality;
    private TextInputEditText textInputEditTextUserPincode;



    private RadioButton radioButtonHome;
    private RadioButton radioButtonOffice;
    private RadioButton radioButtonOther;

    private Button buttonSaveAndContinue;

    View view;
    String[] states;
    private InputValidation inputValidation;


    public UserAddressFragment() {
        // Required empty public constructor
    }


    /**
     * This is override method to hide activity toolbar on onResume method
     */
    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_address, container, false);

        initViews();
        initListeners();
        initObjects();
        return view;
    }

    /**
     * This method is to initialization Views
     */
    private void initViews() {
        toolbarUserAddressFragment = view.findViewById(R.id.toolbarUserAddressFragment);

        textInputLayoutUserName = view.findViewById(R.id.textInputLayoutUserName);
        textInputLayoutUserMobNumber = view.findViewById(R.id.textInputLayoutUserMobNumber);
        textInputLayoutUserEmail = view.findViewById(R.id.textInputLayoutUserEmail);
        textInputLayoutUserHouseNo = view.findViewById(R.id.textInputLayoutUserHouseNo);
        textInputLayoutUserLocality = view.findViewById(R.id.textInputLayoutUserLocality);
        textInputLayoutUserPincode = view.findViewById(R.id.textInputLayoutUserPincode);
        textInputEditTextUserName = view.findViewById(R.id.textInputEditTextUserName);
        textInputEditTextUserMobNumber = view.findViewById(R.id.textInputEditTextUserMobNumber);
        textInputEditTextUserEmail = view.findViewById(R.id.textInputEditTextUserEmail);
        textInputEditTextUserHouseNo = view.findViewById(R.id.textInputEditTextUserHouseNo);
        textInputEditTextUserLocality = view.findViewById(R.id.textInputEditTextUserLocality);
        textInputEditTextUserPincode = view.findViewById(R.id.textInputEditTextUserPincode);



        radioButtonHome = view.findViewById(R.id.radioButtonHome);
        radioButtonOffice = view.findViewById(R.id.radioButtonOffice);
        radioButtonOther = view.findViewById(R.id.radioButtonOther);

        buttonSaveAndContinue = view.findViewById(R.id.buttonSaveAndContinue);
    }

    /**
     * This method is to initialization Listeners
     */
    private void initListeners() {
        buttonSaveAndContinue.setOnClickListener(this);
    }


    /**
     * This method is to initialization Objects
     */
    private void initObjects() {
        setUpToolbar();

        inputValidation = new InputValidation(getActivity());

        states = getResources().getStringArray(R.array.states_india);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, states);

    }

    /**
     * This method shows toolbar
     */
    private void setUpToolbar() {
        toolbarUserAddressFragment.setTitle("Address");
        toolbarUserAddressFragment.setTitleTextColor(Color.WHITE);
        toolbarUserAddressFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarUserAddressFragment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get id of view
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == buttonSaveAndContinue.getId()) {
            saveUserAddress();
        }

    }


    /**
     * This method is for saving user address
     */
    private void saveUserAddress() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserName, textInputLayoutUserName,
                "Enter username")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserMobNumber, textInputLayoutUserMobNumber,
                "Enter Mob. number")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserEmail, textInputLayoutUserEmail,
                "Enter email")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextUserEmail, textInputLayoutUserEmail,
                "Enter email")) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserHouseNo, textInputLayoutUserHouseNo,
                "Enter House No")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserLocality, textInputLayoutUserLocality,
                "Enter Locality")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserPincode, textInputLayoutUserPincode,
                "Enter Pincode")) {
            return;
        }


        if (!(radioButtonHome.isChecked()) && !(radioButtonOffice.isChecked()) && !(radioButtonOther.isChecked())) {
            Toast.makeText(getActivity(), "Please select address type", Toast.LENGTH_SHORT).show();
        }

        String phone = textInputEditTextUserMobNumber.getText().toString().trim();


        String msg = " This item is delivered within 3-4 working days. \nThanks for shopping";
        startActivity(new Intent(getActivity(), byMethod.class));
        sendOrderDetailsMsg(phone, msg);
    }


    /**
     * This method is to send Order details on phone number
     *
     * @param phone customer phone number
     * @param msg   which message to be send
     */
    private void sendOrderDetailsMsg(String phone, String msg) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.SEND_SMS}, 0);
            return;
        }

        Random random = new Random();
        int orderId = (int) (Math.random() * ((1000000 - 11111) + 1)) + 11111;
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(phone, null, "Your order ID " + orderId + "." + msg, null, null);
        Toast.makeText(getActivity(), "Purchase details sent via sms.", Toast.LENGTH_SHORT).show();
    }

    /**
     * This is override method to show toolbar of activity
     */
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
