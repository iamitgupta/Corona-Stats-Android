package com.corona.stats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.corona.beans.AdapterBean;
import com.corona.beans.Area;
import com.corona.beans.Report;
import com.corona.services.BingAPIService;
import com.corona.services.BingApiRetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ListView list;
    AutoCompleteTextView filterText;
    List<AdapterBean> adpterData = new ArrayList<>();
    ArrayAdapter<String> textAdapter;
    Report report;
    CountryAdapter adapter;
    ProgressDialog progressDialog;

    boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        st();
        showProgressDialogWithTitle();


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
                for (Area area : report.getAreas()) {
                    if (parent.getItemAtPosition(position).equals(area.getDisplayName())) {
                        areaList.add(area);
                    }
                }
                if (areaList.size() > 0) {
                    setAdapter(areaList);
                }

            }
        });
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterBean a1 = adpterData.get(position);
                if (a1.getArea().getAreas() != null) {
                    if (a1.getArea().getAreas().size() > 0) {
                        Intent stateIntent = new Intent(MainActivity.this, StateActivity.class);
                        stateIntent.putExtra("AdapterBean", a1);
                        startActivity(stateIntent);
                    }
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.privacyPolicy) {
            Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void st() {
        if (connection(this)) {
            getCoronaReport();
        } else {
            connectionDial();
        }
    }

    public static boolean connection(Context context) {
        boolean status = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = true;
            }
        } else {
            status = false;
        }
        return status;
    }


    public void connectionDial() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("Connectivity Issue");
        builder.setMessage("No Internet Connection");
        builder.setCancelable(false);
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        System.exit(0);
                    }
                });
        builder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        st();
                    }
                });
        builder.show();
    }


    public void getCoronaReport() {
        BingAPIService apiService = BingApiRetrofitInstance.getApiService();

        final Call<Report> call1 = apiService.getCoronaReports();
        call1.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.isSuccessful()) {
                    report = (response.body());
                    if (report.getAreas() != null) {
                        if (report.getAreas().size() > 0)
                            setAdapter(report.getAreas());
                        setTextAdapter(report.getAreas());


                    }
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                System.out.println("Report" + t.getMessage());

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
        hideProgressDialogWithTitle();
    }

    private void setTextAdapter(List<Area> areas) {
        String[] countries = new String[report.getAreas().size()];
        for (int i = 0; i < report.getAreas().size(); i++) {
            countries[i] = report.getAreas().get(i).getDisplayName();
        }

        textAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, countries);

        filterText.setAdapter(textAdapter);
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
                setAdapter(report.getAreas());
            }
        }
    };

    // Method to show Progress bar
    private void showProgressDialogWithTitle() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
    }

    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        if (isClicked) {
            setAdapter(report.getAreas());
            isClicked = false;
        } else {
            super.onBackPressed();
        }
    }
}
