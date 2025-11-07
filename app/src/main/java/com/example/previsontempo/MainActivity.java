package com.example.previsontempo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.previsontempo.fragments.WeatherFragment;
import com.example.previsontempo.fragments.MapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MaterialToolbar toolbar;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return (position == 0) ? new WeatherFragment() : new MapFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText(R.string.weather);
            else tab.setText(R.string.map);
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {

            Intent intent = new Intent(this, AboutActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String scannedCity = data.getStringExtra("SCANNED_CITY");

            if (scannedCity != null && !scannedCity.isEmpty()) {

                Fragment weatherFragment = getSupportFragmentManager()
                        .findFragmentByTag("f" + viewPager.getCurrentItem());

                if (viewPager.getCurrentItem() == 0) {
                    weatherFragment = getSupportFragmentManager()
                            .findFragmentByTag("f" + 0);
                }

                if (weatherFragment == null) {
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        if (f instanceof WeatherFragment) {
                            weatherFragment = f;
                            break;
                        }
                    }
                }

                if (weatherFragment instanceof WeatherFragment) {
                    ((WeatherFragment) weatherFragment).loadWeatherByScannedCity(scannedCity);
                } else {
                    Toast.makeText(this, "Erro: Não foi possível encontrar o fragmento do tempo.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> toRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                toRequest.add(permission);
            }
        }
        if (!toRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    toRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}