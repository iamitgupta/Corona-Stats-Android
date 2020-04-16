package com.corona.stats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.corona.beans.AdapterBean;
import com.corona.beans.Area;
import com.corona.beans.CovidCountry;
import com.corona.beans.Graph;
import com.corona.beans.Report;
import com.corona.services.covid19.CovidAPIService;
import com.corona.services.covid19.CovidApiRetrofitInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateActivity extends AppCompatActivity {

    AdapterBean adapterBean;
    ListView list;
    AutoCompleteTextView filterText;
    List<AdapterBean> adpterData = new ArrayList<>();
    CountryAdapter adapter;
    String confirmed = "/status/confirmed/live";
    String death = "/status/deaths/live";
    String recovered = "/status/recovered/live";
    String apiInit = "dayone/country/";

    boolean isClicked = false;
    ProgressBar progressBar;

    boolean isConfirmed = false, isDeaths = false, isRecovered = false;
    List<CovidCountry> recoveredList, confirmedList, deathList;
    List<Graph> graphList;

    ArrayAdapter<String> textAdapter;


    String countryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        progressBar = findViewById(R.id.progressBar);
        list = (ListView) findViewById(R.id.list);
        filterText = (AutoCompleteTextView) findViewById(R.id.et_search);
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

        //input
        adapterBean = (AdapterBean) getIntent().getSerializableExtra("AdapterBean");


        countryName = adapterBean.getArea().getDisplayName().trim();
        setTitle(countryName);

        if (countryName.equals("United States")) {
            countryName = "us";

        } else {


            String[] tempArr;
            countryName = countryName.toLowerCase();
            if (countryName.contains("(")) {
                tempArr = countryName.split(" ");
                countryName = tempArr[0];
            }
            if (countryName.contains(" ")) {
                countryName = countryName.replace(" ", "-");
            }
        }



        confirmed = apiInit + countryName + confirmed;
        death = apiInit + countryName + death;
        recovered = apiInit + countryName + recovered;

        getConfirmd();
        getDeath();
        getRecovered();

        setTextAdapter(adapterBean.getArea().getAreas());
        setAdapter(adapterBean.getArea().getAreas());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterBean a1 = adpterData.get(position);
                if (a1.getArea().getAreas() != null) {
                    if (a1.getArea().getAreas().size() > 0) {
                        Intent stateIntent = new Intent(StateActivity.this, CityActivity.class);
                        stateIntent.putExtra("AdapterBean", a1);
                        startActivity(stateIntent);
                    }
                }

            }
        });


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

        listViewProp(list, adapter);

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

    public void listViewProp(ListView view, Adapter adapter) {
        int totalHeight = 10;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, view);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = view.getLayoutParams();
        par.height = totalHeight + (view.getDividerHeight() * (adapter.getCount() - 1));
        view.setLayoutParams(par);
        view.requestLayout();
    }


    public void getConfirmd() {
        CovidAPIService apiService = CovidApiRetrofitInstance.getApiService();

        final Call<List<CovidCountry>> call1 = apiService.getCovidCountry(confirmed);
        call1.enqueue(new Callback<List<CovidCountry>>() {
            @Override
            public void onResponse(Call<List<CovidCountry>> call, Response<List<CovidCountry>> response) {
                if (response.isSuccessful()) {
                    confirmedList = response.body();
                    System.out.println("List<confirmedList>" + confirmedList);
                    if(confirmedList==null){
                        confirmedList = new ArrayList<>();
                    }
                    isConfirmed = true;
                    prepareaData();
                }
            }

            @Override
            public void onFailure(Call<List<CovidCountry>> call, Throwable t) {
                System.out.println("List<confirmedList>" + t.getMessage());

            }
        });
    }

    public void getDeath() {
        CovidAPIService apiService = CovidApiRetrofitInstance.getApiService();

        final Call<List<CovidCountry>> call1 = apiService.getCovidCountry(death);
        call1.enqueue(new Callback<List<CovidCountry>>() {
            @Override
            public void onResponse(Call<List<CovidCountry>> call, Response<List<CovidCountry>> response) {
                if (response.isSuccessful()) {
                    deathList = response.body();
                    System.out.println("List<deathList>" + deathList);
                    if(deathList==null){
                        deathList = new ArrayList<>();
                    }
                    isDeaths = true;
                    prepareaData();

                }
            }

            @Override
            public void onFailure(Call<List<CovidCountry>> call, Throwable t) {
                System.out.println("List<deathList>" + t.getMessage());

            }
        });
    }

    public void getRecovered() {
        CovidAPIService apiService = CovidApiRetrofitInstance.getApiService();

        final Call<List<CovidCountry>> call1 = apiService.getCovidCountry(recovered);
        call1.enqueue(new Callback<List<CovidCountry>>() {
            @Override
            public void onResponse(Call<List<CovidCountry>> call, Response<List<CovidCountry>> response) {
                if (response.isSuccessful()) {

                    recoveredList = response.body();
                    System.out.println("List<recoveredList>" + recoveredList);

                    if(recoveredList==null){
                        recoveredList = new ArrayList<>();
                    }

                    isRecovered = true;
                    prepareaData();
                }
            }

            @Override
            public void onFailure(Call<List<CovidCountry>> call, Throwable t) {
                System.out.println("List<recoveredList>" + t.getMessage());

            }
        });
    }

    public void prepareaData() {
        TreeSet<String> dateList = new TreeSet<>();

        graphList = new ArrayList<>();
        if (isConfirmed && isDeaths && isRecovered) {
            for (CovidCountry country : confirmedList) {
                dateList.add(country.getDate());
            }
            for (CovidCountry country : deathList) {
                dateList.add(country.getDate());
            }
            for (CovidCountry country : recoveredList) {
                dateList.add(country.getDate());
            }
            long tempDeath = 0;
            long tempRecov = 0;
            long tempConfirmed = 0;

            long temp = 0;

            for (String date : dateList) {
                temp = 0;
                for (CovidCountry country : confirmedList) {
                    if (country.getDate().equalsIgnoreCase(date)) {
                        System.out.println(country);
                        temp += country.getCases();
                    }
                }
                if (temp > 0) {
                    tempConfirmed = temp;
                }

                temp = 0;
                for (CovidCountry country : deathList) {
                    if (country.getDate().equalsIgnoreCase(date)) {
                        temp += country.getCases();
                    }
                }
                if (temp > 0) {
                    tempDeath = temp;
                }

                temp = 0;

                for (CovidCountry country : recoveredList) {
                    if (country.getDate().equalsIgnoreCase(date)) {
                        temp += country.getCases();
                    }
                }
                if (temp > 0) {
                    tempRecov = temp;
                }

                Graph graph = new Graph();
                graph.setDate(date.substring(5, 10));
                graph.setConfirmed(tempConfirmed);
                graph.setDeath(tempDeath);
                graph.setRecovered(tempRecov);

                graphList.add(graph);

            }
            drawGraph(graphList);
        }

    }

    public void drawGraph(List<Graph> list) {



        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(progressBar);
        anyChartView.setVisibility(View.VISIBLE);
        anyChartView.addCss("file:///android_asset/style.css");

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Number of people affected due to Corona Virus");

        cartesian.yAxis(0).title("Total Corona Virus cases");
        cartesian.xAxis(0).title("Month-Day");

        List<DataEntry> seriesData = new ArrayList<>();
        for (Graph graph : list) {
            seriesData.add(new CustomDataEntry(graph.getDate(), graph.getConfirmed(), graph.getDeath(), graph.getRecovered()));
        }
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Confirmed");
        series1.stroke("2 blue");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Death");
        series2.stroke("2 red");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Recovered");
        series3.stroke("2 green");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
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
