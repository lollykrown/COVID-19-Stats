package com.lollykrown.coronavirusstats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.blongho.country_data.World;
import com.lollykrown.coronavirusstats.model.Corona;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>{
    private static final String TAG = CoronaViewModel.class.getSimpleName();
    Context mContext;
    List<Corona> mCountries;

    public CountriesAdapter(Context context, List<Corona> stats) {
        mContext = context;
        mCountries = stats;
    }

    @Override
    public CountriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.countries_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountriesViewHolder holder, int i) {

        Corona u = mCountries.get(i);
        holder.countryName.setText(u.getCountryName());
        int cas = u.getCases();
        String ca = String.valueOf(cas);
        if (ca.length() == 4) {
            String four = ca.substring(0,1) + "," + ca.substring(1,4);
            holder.cases.setText(four + " Confirmed cases");
        } else if (ca.length() == 5) {
            String five = ca.substring(0,2) + "," + ca.substring(2,5);
            holder.cases.setText(five + " Confirmed cases");
        } else if (ca.length() == 6) {
            String six = ca.substring(0,3) + "," + ca.substring(3,6);
            holder.cases.setText(six + " Confirmed cases");
        } else if (ca.length() == 7) {
            String seven = ca.substring(0,1) + "," + ca.substring(3,4) + "," + ca.substring(4,7);
            holder.cases.setText(seven + " Confirmed cases");
        } else {
            holder.cases.setText(ca + " Confirmed cases");
        }

        holder.deaths.setText (u.getDeaths() + " Total Deaths");
        holder.total_recovered.setText(u.getTotal_recovered() + " recovered");
        holder.newCases.setText(u.getNewCases() + " new cases");
        holder.newDeaths.setText(u.getNewDeaths() + " new deaths");
        final int flagg = World.getFlagOf(u.getCountryName());
        holder.flag.setImageResource(flagg);
    }

    public void setCountries(List<Corona> movies){
        mCountries = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCountries != null)
            return mCountries.size();
        else return 0;
    }

    class CountriesViewHolder extends RecyclerView.ViewHolder {

        ImageView flag;
        TextView countryName;
        TextView cases;
        TextView deaths;
        TextView total_recovered;
        TextView newCases;
        TextView newDeaths;

        public CountriesViewHolder(final View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.name);
            cases = itemView.findViewById(R.id.cases);
            deaths = itemView.findViewById(R.id.deaths);
            total_recovered = itemView.findViewById(R.id.recovered);
            newCases = itemView.findViewById(R.id.new_case);
            newDeaths = itemView.findViewById(R.id.new_death);
        }
    }
}
