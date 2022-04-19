package com.kiralydavid.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Button btnScan, btnKiir;
    private TextView textResult;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QR SCAN");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();

            }
        });



        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Nincs adat", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        filebaIras(textResult.getText().toString());
                        Toast.makeText(MainActivity.this, "Sikeres file-ba írás!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"Kiléptél a scannelesből",Toast.LENGTH_SHORT).show();

            }else{
                textResult.setText(result.getContents());

            }

            try{
                Uri url = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW,url);
                startActivity(intent);

            }catch (Exception exception){
                Log.d("URL ERROR",exception.toString());
            }
        }

    }
    public void filebaIras(String adat) throws IOException {
        Date datum = Calendar.getInstance().getTime();

        SimpleDateFormat datumformazas = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formazasDatum = datumformazas.format(datum);
        String sor = String.format("%s, %s", formazasDatum, adat);

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.append(sor);
            bw.append(System.lineSeparator());
            bw.close();
        }
    }

    public void init(){
        textResult = findViewById(R.id.text_Scan_Result);
        btnScan =findViewById(R.id.btn_scan);
        btnKiir =findViewById(R.id.btn_kiir);
    }

}






