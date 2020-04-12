package com.lollykrown.coronavirusstats;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lollykrown.coronavirusstats.model.Corona;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CoronaViewModel extends AndroidViewModel {
    private static final String TAG = CoronaViewModel.class.getSimpleName();
    private String stats = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/worldstat.php";
    private String byCountry = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/cases_by_country.php";
    private String countryTotal = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/affected.php";
    private String API_KEY = "000d3c92ddmsh6004d289f7b3a66p197932jsn0b72061aa98a";


    private CoronaRepository mRepository;
    List<Corona> mCorona = new ArrayList<>();
    private LiveData<List<Corona>> mAllCases;
    String totalCases;
    int nigeriaCases, nigIndex;

    private MutableLiveData<String> total_cases, total_deaths, total_recovered, new_cases, new_deaths, statistic_taken_at;
    private MutableLiveData<Integer> total, nigNew;

    public CoronaViewModel(Application application) {
        super(application);
        mRepository = new CoronaRepository(application);
        mAllCases = mRepository.getAllCases();
        total_cases = new MutableLiveData<>();
        total_deaths = new MutableLiveData<>();
        total_recovered = new MutableLiveData<>();
        new_cases = new MutableLiveData<>();
        new_deaths = new MutableLiveData<>();
        statistic_taken_at = new MutableLiveData<>();
        total = new MutableLiveData<>();
//        nigNew = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<String> getTotalCases() {
        return total_cases;
    }

    public LiveData<String> getTotalDeaths() {
        return total_deaths;
    }

    public LiveData<String> getTotalRecovered() {
        return total_recovered;
    }

    public LiveData<String> getNewCases() {
        return new_cases;
    }

    public LiveData<String> getNewDeaths() {
        return new_deaths;
    }

    public LiveData<String> getStatisticsTime() {
        return statistic_taken_at;
    }

    public LiveData<Integer> getTotalNo() {
        return total;
    }
//    public LiveData<Integer> getNig() {
//        return nigNew;
//    }

    public LiveData<List<Corona>> getAll() {
        return mAllCases;
    }

    public void insertAll(List<Corona> m) {
        mRepository.insertAll(m);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void loadAllStats() {loadStatsData(stats);}
    public void loadAllCountriesData() {loadCountriesData(byCountry);}
    public void loadTotalNumberOfCountries() {
        loadTotal(countryTotal);
    }

    public void loadCountriesData(String url){

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("countries_stat");
                            for (int i = 0; i < results.length(); i++) {

                                JSONObject coro = results.getJSONObject(i);

                                String country = coro.getString("country_name");
                                String cas = coro.getString("cases");
                                String rep = cas.replace(",", "");
                                int cases = Integer.parseInt(rep);

                                String deaths = coro.getString("deaths");
                                String recovered = coro.getString("total_recovered");

                                String de = coro.getString("new_deaths");
                                String nd = de.replace(",", "");
                                int newd = Integer.parseInt(nd);

                                String newc = coro.getString("new_cases");

                                String serious = coro.getString("serious_critical");

                                Corona c = new Corona(country, cases, deaths, recovered, newc, newd, serious);
                                mCorona.add(c);
                            }
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                   int existingTotal = mRepository.getAllCa().get(nigIndex).getCases();
//                                    if (nigeriaCases > existingTotal) {
//                                        nigNew.setValue(nigeriaCases - existingTotal);
//                                    }
//                                }
//                            });
                            insertAll(mCorona);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplication(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse (VolleyError error){
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplication(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com");
                params.put("x-rapidapi-key", API_KEY);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy p = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(p);
        requestQueue.add(req);
    }

    public void loadStatsData(String url){

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            totalCases = response.getString("total_cases");
                            String totalDeaths = response.getString("total_deaths");
                            String totalRecovered = response.getString("total_recovered");
                            String newCases = response.getString("new_cases");
                            String newDeaths = response.getString("new_deaths");
                            String statisticTakenAt = response.getString("statistic_taken_at");

                            total_cases.setValue(totalCases);
                            total_deaths.setValue(totalDeaths);
                            total_recovered.setValue(totalRecovered);
                            new_cases.setValue(newCases);
                            new_deaths.setValue(newDeaths);
                            statistic_taken_at.setValue(statisticTakenAt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplication(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplication(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com");
                params.put("x-rapidapi-key", API_KEY);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy p = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(p);
        requestQueue.add(req);
    }

    public void loadTotal(String url){

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray ja = response.getJSONArray("affected_countries");
                            total.setValue(ja.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplication(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplication(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com");
                params.put("x-rapidapi-key", API_KEY);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy p = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(p);

        requestQueue.add(req);
    }
}