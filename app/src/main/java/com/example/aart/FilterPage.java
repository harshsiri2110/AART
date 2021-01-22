package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class FilterPage extends AppCompatActivity {

    RadioGroup speciesGroup, genderGroup;

    RadioButton selectedButton;

    Button applyButton,clearButton;

    SeekBar seekBar;

    Intent intent;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        final LoadingDialog loadingDialog = new LoadingDialog(FilterPage.this);

        speciesGroup = findViewById(R.id.filter_species_group);
        genderGroup = findViewById(R.id.filter_gender_group);

        seekBar = findViewById(R.id.seekBar3);

        intent = new Intent(getApplicationContext(),MainActivity.class);

        applyButton = findViewById(R.id.filter_apply_button);
        clearButton = findViewById(R.id.filter_clear_button);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Toast.makeText(getApplicationContext(), i , Toast.LENGTH_SHORT).show();
                progress = i;
                Log.d("TEST", "onProgressChanged: " + i );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), progress , Toast.LENGTH_SHORT).show();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isConnected(FilterPage.this))
                {
                    showCustomDialog();
                }
                else {

                    applyButton.setEnabled(false);
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }};
                    handler.postDelayed(r, 2000);

                    if (speciesGroup.getCheckedRadioButtonId() != -1) {
                        selectedButton = (RadioButton) findViewById(speciesGroup.getCheckedRadioButtonId());
                        intent.putExtra("Species", selectedButton.getText());
                    } else {
                        intent.putExtra("Species", "-1");
                    }

                    if (genderGroup.getCheckedRadioButtonId() != -1) {
                        selectedButton = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());
                        intent.putExtra("Gender", selectedButton.getText());
                    } else {
                        intent.putExtra("Gender", "-1");
                    }

                    if (progress == 0) {
                        intent.putExtra("Age", "-1");
                    } else {
                        intent.putExtra("Age", progress);
                    }

                    intent.putExtra("Activity", "Filter");
                    intent.putExtra("Filter", "on");

                    finish();
                    handler.removeCallbacks(r);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected(FilterPage.this))
                {
                    showCustomDialog();
                }
                else {
                    intent.putExtra("Activity", "Filter");
                    intent.putExtra("Filter", "off");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });


    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FilterPage.this);
        builder.setMessage("You're not connected to the internet! Please check your internet connection.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isConnected(FilterPage firstPage) {
        ConnectivityManager connectivityManager = (ConnectivityManager) firstPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isConnected(this))
        {
            showCustomDialog();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }

        return super.onOptionsItemSelected(item);
    }
}