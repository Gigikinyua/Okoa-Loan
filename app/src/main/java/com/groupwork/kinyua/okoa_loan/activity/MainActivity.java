package com.groupwork.kinyua.okoa_loan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupwork.kinyua.okoa_loan.R;
import com.groupwork.kinyua.okoa_loan.fragment.AboutFragment;
import com.groupwork.kinyua.okoa_loan.fragment.AccountFragment;
import com.groupwork.kinyua.okoa_loan.fragment.LoanFragment;
import com.groupwork.kinyua.okoa_loan.users.PhoneAuthActivity;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView navigationView;
    private Fragment fragment;
    private Toolbar toolbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new AccountFragment());
        tx.commit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Okoa Loan");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_account);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_account:
                        toolbar.setTitle("Okoa Loan");
                        fragment = new AccountFragment();
                        break;
                    case R.id.nav_loan:
                        toolbar.setTitle("Request Loan");
                        fragment = new LoanFragment();
                        break;
                    case R.id.nav_about:
                        toolbar.setTitle("About us");
                        fragment = new AboutFragment();
                        break;
                    case R.id.nav_logout:
                        logOut();
                        break;
                }
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();

                drawer.closeDrawers();

                return true;
            }
        });

    }

    public void logOut() {
        auth.signOut();

        phonePref("");

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), PhoneAuthActivity.class));
                    finish();
                }
            }
        };
    }

    public void phonePref(String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
