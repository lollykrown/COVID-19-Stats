package com.lollykrown.coronavirusstats;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lollykrown.coronavirusstats.model.Corona;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class CountriesTableAdapter extends RecyclerView.Adapter<CountriesTableAdapter.CountriesTableViewHolder> implements Filterable {
    Context mContext;
    List<Corona> mCountries;
    ArrayList<Corona> searchCount = new ArrayList<>();

    public CountriesTableAdapter(Context context, List<Corona> stats) {
        mContext = context;
        this.mCountries = stats;
    }

    @Override
    public CountriesTableViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.countries_table_list_items,
                viewGroup, false);
        return new CountriesTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountriesTableViewHolder holder, int i) {
        Corona u = mCountries.get(i);
        holder.countryName.setText(u.getCountryName());
        int cas = u.getCases();
        DecimalFormat f = new DecimalFormat("#,###,###");
        holder.cases.setText(f.format(cas));

        //if (u.getNewCases() != 0) {
            //holder.newCases.setBackgroundResource(R.drawable.bg2);
            //DecimalFormat fo = new DecimalFormat("#,###,###");
            holder.newCases.setText(u.getNewCases());
        //}

        holder.deaths.setText(u.getDeaths());
        if (u.getNewDeaths() != 0) {
            holder.newDeaths.setBackgroundResource(R.drawable.bg3);
            holder.newDeaths.setTextColor(Color.parseColor("#ffffff"));
            holder.newDeaths.setText(String.valueOf(u.getNewDeaths()));
        }
        holder.total_recovered.setText(u.getTotal_recovered());

        holder.seriousCritical.setText(u.getSeriousCritical());
    }

    public void setCountries(List<Corona> coro){
        this.mCountries = coro;
        searchCount.addAll(mCountries);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCountries != null)
            return mCountries.size();
        else return 0;
    }

    class CountriesTableViewHolder extends RecyclerView.ViewHolder {
        LinearLayout table;
        TextView countryName;
        TextView cases;
        TextView deaths;
        TextView total_recovered;
        TextView newCases;
        TextView newDeaths;
        TextView seriousCritical;

        public CountriesTableViewHolder(final View itemView) {
            super(itemView);
            table = itemView.findViewById(R.id.table);
            countryName = itemView.findViewById(R.id.country);
            cases = itemView.findViewById(R.id.casee);
            newCases = itemView.findViewById(R.id.new_c);
            deaths = itemView.findViewById(R.id.death);
            newDeaths = itemView.findViewById(R.id.new_d);
            total_recovered = itemView.findViewById(R.id.recov);
            seriousCritical = itemView.findViewById(R.id.critical);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase().trim();
                mCountries.clear();

                if (constraint.length() == 0) {
                    mCountries.addAll(searchCount);
                } else {
                    for (Corona i : searchCount) {
                        if (i.getCountryName().toLowerCase().contains(constraint)) {
                            mCountries.add(i);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = mCountries;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}
