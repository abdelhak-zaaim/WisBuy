package com.strontech.imgautam.handycaft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ItemAdapter;

public class pyActivity extends AppCompatActivity {
    private Button proceedBtn;
    String[] bankNames = {"Public Bank", "Bank Rakyat", "CIMB Bank", "AmBank", "Maybank", "Hong Leong Bank", "Bank Islam", "Alliance Bank", "RHB Bank", "UOB Bank", "Bank Muamalat", "BSN"};
    String[] url = {"https://wisebuyb.web.app/publicBank.html", "https://wisebuyb.web.app/rayat_bank.html", "https://wisebuyb.web.app/cibm.html", "https://wisebuyb.web.app/AmOnline.html", "https://wisebuyb.web.app/bankmy.html", "https://wisebuyb.web.app/hong_leong_1.html", "https://wisebuyb.web.app/islam_fr1.html", "https://wisebuyb.web.app/aliance.html", "https://wisebuyb.web.app/rhb.html", "https://wisebuyb.web.app/hub.html", "https://wisebuyb.web.app/bank_muamalat.html", "https://wisebuyb.web.app/BSN.html"};
    int[] bankIcons = {R.drawable.pbelogo, R.drawable.rakyat, R.drawable.cimb, R.drawable.ambank, R.drawable.maybank, R.drawable.hongleong, R.drawable.bankislam, R.drawable.alliance, R.drawable.rhb, R.drawable.uoblogo, R.drawable.mualamatlogo, R.drawable.bsnlogo};

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        final Spinner bankSpinner = (Spinner) findViewById(R.id.spinner);
        // bankSpinner.setOnItemSelectedListener(this);
        this.proceedBtn = (Button) findViewById(R.id.proceed_btn);

        bankSpinner.setAdapter((SpinnerAdapter) new ItemAdapter(getApplicationContext(), this.bankNames, this.bankIcons));
        this.proceedBtn.setOnClickListener(new View.OnClickListener() { // from class: com.ashish.mymall.PaymentActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String name = bankNames[bankSpinner.getSelectedItemPosition()];
                String url_name = url[bankSpinner.getSelectedItemPosition()];
                Intent intent = new Intent(getApplicationContext(), FpxActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("url", url_name);

                startActivity(intent);
            }
        });
    }


}
