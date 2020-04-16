package com.corona.stats;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.corona.beans.AdapterBean;
import com.corona.beans.Area;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    ListView list;
    AutoCompleteTextView filterText;
    List<AdapterBean> adpterData = new ArrayList<>();
    CountryAdapter adapter;
    AdapterBean adapterBean;
    ArrayAdapter<String> textAdapter;

    boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);




        list = (ListView) findViewById(R.id.list);
        filterText = (AutoCompleteTextView) findViewById(R.id.et_search);

        adapterBean = (AdapterBean) getIntent().getSerializableExtra("AdapterBean");
        setAdapter(adapterBean.getArea().getAreas());
        setTextAdapter(adapterBean.getArea().getAreas());



        filterText.addTextChangedListener(countryTextWatcher);
        filterText.setThreshold(1);
        filterText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                isClicked = true;

                List<Area> areaList = new ArrayList<>();
                for(Area area  : adapterBean.getArea().getAreas()){
                    if(parent.getItemAtPosition(position).equals(area.getDisplayName())){
                        areaList.add(area);
                    }
                }
                if(areaList.size()>0){
                    setAdapter(areaList);
                }

            }
        });



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterBean a1 = adpterData.get(position);
                if(a1.getArea().getAreas()!=null) {
                    if(a1.getArea().getAreas().size()>0)
                        setAdapter(a1.getArea().getAreas());
                }
            }
        });
    }
    private void setTextAdapter(List<Area> areas) {
        String[] countries = new String[adapterBean.getArea().getAreas().size()];
        for (int i = 0; i < adapterBean.getArea().getAreas().size(); i++) {
            countries[i] = adapterBean.getArea().getAreas().get(i).getDisplayName();
        }

        textAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, countries);

        filterText.setAdapter(textAdapter);
    }
    public void setAdapter(List<Area> areaList) {
        adpterData.clear();
        for (Area area : areaList) {
            if (area != null) {
                String def = "0";
                String death;
                String recovered;
                String confirmed;


                if (area.getTotalConfirmed() != null) {
                    confirmed = area.getTotalConfirmed().toString();
                } else {
                    confirmed = def;
                }


                if (area.getTotalDeaths() != null) {
                    death = area.getTotalDeaths().toString();
                } else {
                    death = def;
                }

                if (area.getTotalRecovered() != null) {
                    recovered = area.getTotalRecovered().toString();
                } else {
                    recovered = def;
                }
                adpterData.add(new AdapterBean(area, area.getDisplayName(), confirmed, death, recovered));
            }

        }
        adapter = new CountryAdapter(this, adpterData);
        list.setTextFilterEnabled(true);
        list.setAdapter(adapter);

    }
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }

    private TextWatcher countryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {



        }

        @Override
        public void afterTextChanged(Editable s) {
            String name = filterText.getText().toString().trim();
            if (!name.isEmpty()) {
                setAdapter(adapterBean.getArea().getAreas());
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(isClicked){
            setAdapter(adapterBean.getArea().getAreas());
            isClicked = false;
        }else{
            super.onBackPressed();
        }
    }
}
