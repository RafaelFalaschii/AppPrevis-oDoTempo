package com.example.previsontempo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.previsontempo.R;
import com.example.previsontempo.SharedViewModel;
import com.example.previsontempo.adapters.ForecastAdapter;
import com.example.previsontempo.api.RetrofitClient;
import com.example.previsontempo.models.Forecast;
import com.example.previsontempo.models.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragmentDebug";
    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String KEY_CITY = "city";
    private static final String API_KEY = "c1bab0f7"; // 🔹 substitua pela sua chave HG Brasil

    private TextView textViewCity, textViewTemp, textViewDescription, textViewMaxMin, textViewFeels, textViewHumidity;
    private EditText editCityInput;
    private Button btnSearch;
    private ImageView weatherIcon;
    private RecyclerView recyclerForecast;
    private ForecastAdapter adapter;

    private FusedLocationProviderClient fusedLocationClient;
    private SharedViewModel sharedViewModel;
    private Handler mainHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        textViewCity = view.findViewById(R.id.textViewCity);
        textViewTemp = view.findViewById(R.id.textViewTemp);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewMaxMin = view.findViewById(R.id.textViewMaxMin);
        textViewFeels = view.findViewById(R.id.textViewFeels);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);
        editCityInput = view.findViewById(R.id.editCityInput); // O EditText
        btnSearch = view.findViewById(R.id.btnSearch);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        recyclerForecast = view.findViewById(R.id.recyclerForecast);

        adapter = new ForecastAdapter(new ArrayList<>());
        recyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerForecast.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        btnSearch.setOnClickListener(v -> {
            String newCity = editCityInput.getText().toString().trim();
            if (!newCity.isEmpty()) {
                saveCityPreference(newCity);
                loadWeatherByCity(newCity);
            } else {
                Toast.makeText(getActivity(), "Digite o nome de uma cidade!", Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedCity = prefs.getString(KEY_CITY, "Umuarama");

        if (editCityInput != null) {
            editCityInput.setText(savedCity);
        }

        loadWeatherByCity(savedCity);
        requestWeatherByLocation();

        return view;
    }

    private void saveCityPreference(String city) {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_CITY, city).apply();
    }

    @SuppressLint("MissingPermission")
    private void requestWeatherByLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                loadWeatherByCoordinates(lat, lon);

            } else {
                Log.d(TAG, "Localização GPS não encontrada. Usando cidade salva/padrão.");
            }
        });
    }

    public void loadWeatherByScannedCity(String city) {
        if (city != null && !city.isEmpty()) {
            loadWeatherByCity(city);
            editCityInput.setText(city);
            saveCityPreference(city);
            Toast.makeText(getContext(), "Clima atualizado para: " + city, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWeatherByCity(String city) {
        try {
            String encodedCity = URLEncoder.encode(city.trim(), "UTF-8");

            Call<WeatherResponse> call = RetrofitClient.getWeatherService()
                    .getWeatherByCity(API_KEY, encodedCity, "json");

            call.enqueue(new WeatherCallback(city));
        } catch (Exception e) {
            Log.w(TAG, "Erro ao codificar cidade ou na busca", e);
        }
    }

    private void loadWeatherByCoordinates(double lat, double lon) {
        Call<WeatherResponse> call = RetrofitClient.getWeatherService()
                .getWeatherByCoordinates(API_KEY, lat, lon, "json");

        call.enqueue(new WeatherCallback(null));
    }

    private class WeatherCallback implements Callback<WeatherResponse> {
        private final String cityName;

        public WeatherCallback(String cityName) {
            this.cityName = cityName;
        }

        @Override
        public void onResponse(@NonNull Call<WeatherResponse> call,
                               @NonNull Response<WeatherResponse> response) {
            if (!isAdded()) return;

            if (response.isSuccessful() && response.body() != null) {
                WeatherResponse w = response.body();
                if (w.getResults() != null) {
                    updateUI(w);

                    WeatherResponse.Results r = w.getResults();
                    double lat = r.getLatitude();
                    double lon = r.getLongitude();
                    String city = r.getCity();

                    if (lat != 0.0 && lon != 0.0) {
                        sharedViewModel.setLocationAndCity(lat, lon, city);
                        Log.d(TAG, "Coordenadas enviadas ao mapa pela API: " + lat + ", " + lon);
                    } else if (cityName != null) {
                        Log.d(TAG, "API não retornou Lat/Lon. Tentando Geocoder para: " + cityName);
                        getCoordinatesFromCity(cityName);
                    }

                } else {
                    Toast.makeText(getActivity(), "Erro: Cidade não encontrada ou dados inválidos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                String err = "Código: " + response.code();
                try {
                    if (response.errorBody() != null) err += " - " + response.errorBody().string();
                } catch (IOException e) {
                    Log.e(TAG, "Erro lendo errorBody", e);
                }
                Toast.makeText(getActivity(), "Erro ao carregar dados: " + err, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
            if (!isAdded()) return;
            Toast.makeText(getActivity(), "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getCoordinatesFromCity(String cityName) {
        new Thread(() -> {
            try {

                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

                @SuppressWarnings("deprecation")
                List<Address> addresses = geocoder.getFromLocationName(cityName, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();

                    mainHandler.post(() -> {
                        sharedViewModel.setLocationAndCity(lat, lon, cityName);
                        Log.d(TAG, "Geocodificado e enviado ao mapa: " + cityName + " -> " + lat + ", " + lon);
                    });
                } else {
                    Log.w(TAG, "Geocoder não encontrou coordenadas para: " + cityName);
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro fatal ao geocodificar cidade", e);
            }
        }).start();
    }

    private void updateUI(WeatherResponse weatherResponse) {
        WeatherResponse.Results r = weatherResponse.getResults();
        if (r == null) return;

        String cityName = r.getCity();
        if (cityName != null) {
            cityName = cityName.replace(", Brazil", "").trim();
        }

        if (editCityInput.getText().toString().isEmpty() && cityName != null && !cityName.isEmpty()) {
            editCityInput.setText(cityName);
        }

        textViewCity.setText(cityName);
        textViewTemp.setText(r.getTemp() + " °C");
        textViewDescription.setText(r.getDescription());
        textViewHumidity.setText("Umidade: " + r.getHumidity() + "%");
        textViewFeels.setText("Sensação: " + r.getTemp() + "°C");

        List<Forecast> list = r.getForecast();
        if (list != null && !list.isEmpty()) {
            Forecast today = list.get(0);
            textViewMaxMin.setText("Máx " + today.getMax() + "°C / Mín " + today.getMin() + "°C");

            List<Forecast> upcoming = list.subList(1, Math.min(list.size(), 7));
            adapter.updateData(upcoming);
        }

        String slug = r.getConditionSlug();
        int iconRes = R.drawable.ic_sunny;
        if (slug != null) {
            if (slug.contains("cloud")) iconRes = R.drawable.ic_cloudy;
            else if (slug.contains("rain") || slug.contains("storm")) iconRes = R.drawable.ic_rainy;
            else if (slug.contains("clear")) iconRes = R.drawable.ic_sunny;
            else if (slug.contains("snow")) iconRes = R.drawable.ic_snowy;
            else if (slug.contains("fog")) iconRes = R.drawable.ic_foggy;
        }
        weatherIcon.setImageResource(iconRes);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestWeatherByLocation();
        }
    }
}