package com.lollykrown.coronavirusstats;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blongho.country_data.World;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lollykrown.coronavirusstats.model.Corona;

//import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    List<Corona> mCoun;
    private ExtendedFloatingActionButton view;
    CoordinatorLayout cord;
    private TextView total_cases, total_deaths, total_recovered, new_cases, new_deaths, last_updated, info;
    private RecyclerView recyclerView;
    private CoronaViewModel coronaViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        World.init(getApplicationContext());

        coronaViewModel = ViewModelProviders.of(this).get(CoronaViewModel.class);
        coronaViewModel.loadAllStats();
        coronaViewModel.loadTotalNumberOfCountries();
        coronaViewModel.loadAllCountriesData();

        cord = findViewById(R.id.snack);
        view = findViewById(R.id.fab);
        info = findViewById(R.id.info);
        total_cases = findViewById(R.id.total_cases);
        total_deaths = findViewById(R.id.total_deaths);
        total_recovered = findViewById(R.id.total_recovered);
        new_cases = findViewById(R.id.new_cases);
        new_deaths = findViewById(R.id.new_deaths);
        last_updated = findViewById(R.id.last_updated);
        recyclerView = findViewById(R.id.rv);

        final CountriesAdapter adapter = new CountriesAdapter(getApplicationContext(), mCoun);

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        coronaViewModel.getTotalCases().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                total_cases.setText(s);
            }
        });
        coronaViewModel.getTotalDeaths().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                total_deaths.setText(s + " Total deaths");
            }
        });
        coronaViewModel.getTotalRecovered().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                total_recovered.setText(s + " Total Recovered");
            }
        });
        coronaViewModel.getNewDeaths().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                new_deaths.setText(s + " Deaths today");
            }
        });
        coronaViewModel.getNewCases().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                new_cases.setText(s + " Cases today");
            }
        });
        coronaViewModel.getTotalNo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                s=s-1;
                info.setText("COVID-19 is affecting " + s + " Countries and Territories around " +
                        "the world and 1 International Conveyance.");
            }
        });

        coronaViewModel.getStatisticsTime().observe(this, new Observer<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable String s) {
//                Calendar c = Calendar.getInstance(Locale.ENGLISH);
//                String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";
//                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_8);
//                String df = sdf.format(s);
                last_updated.setText("updated at: " + s);
            }
        });

        coronaViewModel.getAll().observe(this, new Observer<List<Corona>>() {
            @Override
            public void onChanged(@Nullable final List<Corona> coronas) {
                mCoun = coronas;
                adapter.setCountries(coronas);
                if (adapter.getItemCount() == 0) {
                    //coronaViewModel.loadAllCountriesData();
                    Snackbar.make(cord, "Retrieving data", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CountriesTableActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.add).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            Snackbar.make(cord, "Refreshing... please wait", Snackbar.LENGTH_LONG).show();
            coronaViewModel.loadAllStats();
            coronaViewModel.loadAllCountriesData();
            coronaViewModel.loadTotalNumberOfCountries();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
