package com.corona.stats;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.corona.beans.AdapterBean;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<String> {

    private final Activity context;

    List<AdapterBean> data;


    public CountryAdapter(Activity context, List<AdapterBean> data) {
        super(context, R.layout.table_list_item, data.hashCode());
        this.context = context;
        this.data = data;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return data.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.table_list_item, null, true);

        TextView tv_name = (TextView) rowView.findViewById(R.id.tv_name);
        TextView tv_confirmed = (TextView) rowView.findViewById(R.id.tv_confirmed);
        TextView tv_death = (TextView) rowView.findViewById(R.id.tv_death);
        TextView tv_recovered = (TextView) rowView.findViewById(R.id.tv_recovered);


        tv_name.setText(data.get(position).getName());
        tv_confirmed.setText(data.get(position).getConfirmed().toString());
        tv_death.setText(data.get(position).getDeath().toString());
        tv_recovered.setText(data.get(position).getRecovered().toString());

        return rowView;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        if (data.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }


}
