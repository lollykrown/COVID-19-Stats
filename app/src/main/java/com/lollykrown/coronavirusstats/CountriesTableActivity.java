package com.lollykrown.coronavirusstats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.lollykrown.coronavirusstats.model.Corona;

import java.util.List;

public class CountriesTableActivity extends AppCompatActivity {
    List<Corona> mCoun;
    private CoordinatorLayout cord;
    private RecyclerView rv;
    private CoronaViewModel coronaViewModel;
    TextView updated;
    CountriesTableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_table);

        cord = findViewById(R.id.cordi);
        updated = findViewById(R.id.updated);
        rv = findViewById(R.id.tableList);
        coronaViewModel = ViewModelProviders.of(this).get(CoronaViewModel.class);
        coronaViewModel.loadAllStats();

        adapter = new CountriesTableAdapter(getApplicationContext(), mCoun);

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(adapter);

        coronaViewModel.getStatisticsTime().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                updated.setText(s);
            }
        });

        coronaViewModel.getAll().observe(this, new Observer<List<Corona>>() {
            @Override
            public void onChanged(@Nullable final List<Corona> coronas) {
                mCoun = coronas;
                adapter.setCountries(coronas);
            }
        });

//        coronaViewModel.getNig().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer s) {
//                NewNigCasesNotification.notify(getApplicationContext(), s.toString(), 888 );
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView sv = (SearchView) item.getActionView();

        sv.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            Snackbar.make(cord, "Refreshing... please wait", Snackbar.LENGTH_LONG).show();
            coronaViewModel.loadAllCountriesData();
            return true;
        }
        if (id == R.id.add) {
            Snackbar.make(cord, "Refreshing... please wait", Snackbar.LENGTH_LONG).show();
            coronaViewModel.loadAllCountriesData();
            return true;
        }
        if (id == R.id.delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                coronaViewModel.deleteAll();
                Toast.makeText(getApplicationContext(), "All data deleted", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
