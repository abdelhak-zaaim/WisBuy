package com.strontech.imgautam.handycaft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.strontech.imgautam.handycaft.R;

public class byMethod extends AppCompatActivity {
    private Button proceedBtn;
    private RadioButton rad1, rad2;
    private TextView text_note;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);
        text_note = findViewById(R.id.text_note);
        text_note.setVisibility(View.GONE);
        rad1 = findViewById(R.id.rad1);
        rad1.setChecked(true);
        rad2 = findViewById(R.id.rad2);
        proceedBtn= findViewById(R.id.proceed_btn);
        rad1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rad2.setChecked(false);
                    text_note.setVisibility(View.GONE);
                }
            }
        });
        rad2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rad1.setChecked(false);
                    text_note.setVisibility(View.VISIBLE);
                }
            }
        });


        proceedBtn.setOnClickListener(new View.OnClickListener() { // from class: com.ashish.mymall.PaymentMethod.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), pyActivity.class));
            }
        });
    }


}
