package com.groupwork.kinyua.okoa_loan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.groupwork.kinyua.okoa_loan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.payToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.payExp);
        expListView.setChildDivider(getResources().getDrawable(R.color.transparent));

        // preparing list data
        prepareListData();

        listAdapter = new com.groupwork.kinyua.okoa_loan.utility.ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("M-Pesa");
        listDataHeader.add("Airtel Money");
        listDataHeader.add("T-Kash");

        // Adding child data
        List<String> Mpesa = new ArrayList<String>();
        Mpesa.add("- Go to M-Pesa menu");
        Mpesa.add("- Tap 'Lipa na M-Pesa' ");
        Mpesa.add("- Pay Bill");
        Mpesa.add("- Enter Business number 123456");
        Mpesa.add("- Enter your mobile number");
        Mpesa.add("- Enter amount you want to repay");
        Mpesa.add("- Enter your M-Pesa PIN and send");


        List<String> Airtel = new ArrayList<String>();
        Airtel.add("- Select Airtel Money on your menu");
        Airtel.add("- Select Make Payments");
        Airtel.add("- Select Paybill");
        Airtel.add("- Select Other");
        Airtel.add("- Enter business Name OKOA LOAN");
        Airtel.add("- Enter the amount in Kshs you want to repay");
        Airtel.add("- Enter your secret Airtel Money PIN");
        Airtel.add("- Under reference, Enter your Mobile Number");

        List<String> Tkash = new ArrayList<>();
        Tkash.add("- Go to your T-Kash Menu and select 'Pay Bill'");
        Tkash.add("- Enter Biller Number 123456");
        Tkash.add("- Enter your mobile number as the Account Number");
        Tkash.add("- Enter amount you want to repay");
        Tkash.add("- Enter your T-Kash PIN");
        Tkash.add("- Wait for the T-Kash confirmation SMS");

        listDataChild.put(listDataHeader.get(0), Mpesa); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Airtel);
        listDataChild.put(listDataHeader.get(2), Tkash);

    }
}


