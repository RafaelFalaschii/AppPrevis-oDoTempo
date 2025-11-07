package com.example.previsontempo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CityInputActivity extends AppCompatActivity {

    private EditText editCity;
    private Button btnContinue;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String KEY_CITY = "city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        prefs.edit().remove(KEY_CITY).apply();

        String savedCity = prefs.getString(KEY_CITY, null);

        if (savedCity != null && !savedCity.trim().isEmpty()) {
            goToMainActivity();
            return;
        }

        setContentView(R.layout.activity_city_input);

        editCity = findViewById(R.id.editCity);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editCity.getText().toString().trim();

                if (city.isEmpty()) {
                    Toast.makeText(CityInputActivity.this,
                            "Por favor, digite uma cidade", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_CITY, city);
                editor.apply();

                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}