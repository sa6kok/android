package com.example.dailysmarts;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.net.Uri;

import com.example.dailysmarts.database.DatabaseInstance;
import com.example.dailysmarts.database.SmartEntity;
import com.example.dailysmarts.fragments.DailyQuoteFragment;
import com.example.dailysmarts.fragments.MyQuotesFragment;
import com.example.dailysmarts.retrofit.RetrofitInstance;
import com.example.dailysmarts.retrofit.SmartModel;
import com.example.dailysmarts.retrofit.SmartService;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dailysmarts.databinding.ActivityMainBinding;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DailyQuoteFragment.OnFragmentInteractionListener,
        MyQuotesFragment.OnFragmentInteractionListener {

    private ActivityMainBinding binding;
    private String language = RetrofitInstance.ENGLISH;
    private SharedPreferences sharedPreferences;
    private DatabaseInstance db;
    private boolean toUpdateTodaySmarty;
    private int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initClicks();
        db = DatabaseInstance.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("IS_FIRST_TIME", true)) {
            chooseLanguage();
        } else {
            checkDatabaseForTodaySmarty();
        }
    }

    private void chooseLanguage() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Welcome to Daily Smarts. Choose your language for the smarties:");
        String[] types = {"English", "Russian"};
        b.setItems(types, (dialog, which) -> {
            dialog.dismiss();
            switch (which) {
                case 0:
                    initRetrofit(RetrofitInstance.ENGLISH);
                    break;
                case 1:
                    language = RetrofitInstance.RUSSIAN;
                    initRetrofit(RetrofitInstance.RUSSIAN);
                    break;
            }
        });
        b.show();
    }

    private void initRetrofit(String language) {
        showProgressBar();
        RetrofitInstance retrofit = RetrofitInstance.getInstance(language);
        SmartService smartService = retrofit.getSmartService();
        Call<SmartModel> smartCall = smartService.getItem("");
        smartCall.enqueue(getSmartyFromAPI());
    }

    private void checkDatabaseForTodaySmarty() {
        String current = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String currentDate = current.substring(0, 10);
        db.getEntityById(data -> {
            if (data != null && data.getDate().startsWith(currentDate)) {
                showFragment(DailyQuoteFragment.newInstance(data.getQuoteText(), data.getQuoteAuthor()));
                hideProgressBar();
            } else {
                initRetrofit(language);
            }
        }, 0);
    }

    private Callback<SmartModel> getSmartyFromAPI() {
        return new Callback<SmartModel>() {
            @Override
            public void onResponse(Call<SmartModel> call, Response<SmartModel> response) {
                if (response.isSuccessful()) {
                    SmartModel result = response.body();
                    SmartEntity todaysSmarty = new SmartEntity(result.getQuoteText(), result.getQuoteAuthor());
                    if (sharedPreferences.getBoolean("IS_FIRST_TIME", true)) {
                        db.insertSingleAsync(todaysSmarty);
                        showFragment(DailyQuoteFragment.newInstance(result.getQuoteText(), result.getQuoteAuthor()));
                        hideProgressBar();
                        sharedPreferences.edit().putBoolean("IS_FIRST_TIME", false).apply();
                    } else {
                        if (toUpdateTodaySmarty) {
                            db.updateTodaySmarty(todaysSmarty.getQuoteText(), todaysSmarty.getQuoteAuthor(), todaysSmarty.getDate());
                            toUpdateTodaySmarty = false;
                            showFragment(DailyQuoteFragment.newInstance(result.getQuoteText(), result.getQuoteAuthor()));
                            hideProgressBar();
                        } else {
                            showTodaySmartyFromDB();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SmartModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There is no Internet", Toast.LENGTH_SHORT).show();
                showTodaySmartyFromDB();
                hideProgressBar();
            }
        };
    }

    private void showTodaySmartyFromDB() {
        db.getEntityById(data -> {
            if (data != null) {
                showFragment(DailyQuoteFragment.newInstance(data.getQuoteText(), data.getQuoteAuthor()));
            }
            hideProgressBar();
        }, 1);
    }

    private void initClicks() {
        binding.tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTabs(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switchTabs(tab);
            }
        });

        binding.btnRefresh.setOnClickListener(v -> {
            toUpdateTodaySmarty = true;
            initRetrofit(language);
        });
        SwipeRefreshLayout swipe = binding.swipeRefresh;
        swipe.setOnRefreshListener(() -> {
            if (tabPosition == 0) {
                toUpdateTodaySmarty = true;
                initRetrofit(language);
                swipe.setRefreshing(false);
            } else if (tabPosition == 1) {
                showFragment(MyQuotesFragment.newInstance());
                swipe.setRefreshing(false);
            }
        });
    }

    private void switchTabs(TabLayout.Tab tab) {
        tabPosition = tab.getPosition();
        switch (tabPosition) {
            case 0:
                changeRefresh(tabPosition);
                checkDatabaseForTodaySmarty();
                break;
            case 1:
                changeRefresh(tabPosition);
                showFragment(MyQuotesFragment.newInstance());
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        checkFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void checkFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void changeRefresh(int position) {
        ImageButton refresh = binding.btnRefresh;
        if (position == 1) {
            refresh.setVisibility(View.GONE);
        } else if (position == 0) {
            refresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void showProgressBar() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        binding.progress.setVisibility(View.GONE);
    }
}
