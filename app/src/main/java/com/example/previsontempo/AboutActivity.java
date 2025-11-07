package com.example.previsontempo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class AboutActivity extends AppCompatActivity {

    private ImageView imgQrCodeCity;
    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String KEY_CITY = "city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView studentName = findViewById(R.id.studentName);
        TextView studentRA = findViewById(R.id.studentRA);
        TextView studentCourse = findViewById(R.id.studentCourse);
        TextView studentPolo = findViewById(R.id.studentPolo);
        imgQrCodeCity = findViewById(R.id.imgQrCodeCity);

        displayCityQrCode();

    }
    private void displayCityQrCode() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String cityToEncode = prefs.getString(KEY_CITY, "Umuarama");

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(
                    cityToEncode,
                    BarcodeFormat.QR_CODE,
                    400,
                    400
            );

            imgQrCodeCity.setImageBitmap(bitmap);

            Toast.makeText(this, "QR Code gerado para: " + cityToEncode, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao gerar QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}