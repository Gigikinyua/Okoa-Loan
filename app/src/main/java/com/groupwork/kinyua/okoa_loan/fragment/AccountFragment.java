package com.groupwork.kinyua.okoa_loan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.groupwork.kinyua.okoa_loan.R;
import com.groupwork.kinyua.okoa_loan.activity.HistoryActivity;
import com.groupwork.kinyua.okoa_loan.activity.PayActivity;
import com.groupwork.kinyua.okoa_loan.activity.ProfileActivity;

public class AccountFragment extends Fragment {

    View view;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_account, container, false);

        LinearLayout profilebutton = (LinearLayout) view.findViewById(R.id.profileButton);
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        LinearLayout paybutton = (LinearLayout) view.findViewById(R.id.PayButton);
        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PayActivity.class));
            }
        });

        LinearLayout historybutton = (LinearLayout) view.findViewById(R.id.HistoryButton);
        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
     return view;
    }
}
