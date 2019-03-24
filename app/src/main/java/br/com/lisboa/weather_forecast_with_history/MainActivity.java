package br.com.lisboa.weather_forecast_with_history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected EditText locationEditText;
    protected ListView weatherListView;
    protected WeatherArrayAdapter weatherAdapter;
    protected List<Weather> weatherList;
    protected ArrayList<String> history;
    protected String cidade = "";
    protected ListView historyListView;
    protected static String endereco = "";
    private AlertDialog alerta;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationEditText = findViewById(R.id.locationEditText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        historyListView = findViewById(R.id.historyListView);
        setSupportActionBar(toolbar);
        history = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, history);
        runOnUiThread(()->{
            adapter.notifyDataSetChanged();
        });

        historyListView.setAdapter(adapter);

        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                show_confirmation(index);
                historyListView.setAdapter(adapter);
                return true;
            }
        });

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("User clicked ", history.get(position));
                endereco = getString(
                        R.string.web_service_url,
                        getString(R.string.desc_language),
                        history.get(position),
                        getString(R.string.api_key),
                        getString(R.string.measurement_unit));
                historyListView.setAdapter(adapter);
                startActivity(new Intent(MainActivity.this, second_activity.class));
            }

        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v) -> {
            cidade = locationEditText.
                    getEditableText().toString();
            if (history.indexOf(cidade) == -1) {
                history.add(cidade);
            }
            endereco = getString(
                    R.string.web_service_url,
                    getString(R.string.desc_language),
                    cidade,
                    getString(R.string.api_key),
                    getString(R.string.measurement_unit));
            historyListView.setAdapter(adapter);
            startActivity(new Intent(MainActivity.this, second_activity.class));
        });
    }

    private void show_confirmation(int position) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção!");
        //define a mensagem
        builder.setMessage(getString(R.string.confirmation_string,
                history.get(position)));
        //define um botão como positivo
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                history.remove(position);
                historyListView.setAdapter(adapter);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
