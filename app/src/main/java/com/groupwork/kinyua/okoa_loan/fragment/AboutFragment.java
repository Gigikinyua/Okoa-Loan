package com.groupwork.kinyua.okoa_loan.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupwork.kinyua.okoa_loan.R;

public class AboutFragment extends Fragment {

    View view;
    TextView HyperlinkTerms;
    Spanned termsText;

    public AboutFragment() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_about, container, false);


         HyperlinkTerms = (TextView) view.findViewById(R.id.termsTextView);
         termsText = Html.fromHtml("<a href = 'https://www.TermsandConditions.com//'>Terms and conditions</a>");

         HyperlinkTerms.setMovementMethod(LinkMovementMethod.getInstance());
         HyperlinkTerms.setText(termsText);

     return view;
    }

}
