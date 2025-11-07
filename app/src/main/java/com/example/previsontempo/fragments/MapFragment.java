package com.example.previsontempo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.previsontempo.R;
import com.example.previsontempo.SharedViewModel;
import com.example.previsontempo.SharedViewModel.LocationData;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragmentDebug";

    private MapView map;
    private IMapController mapController;
    private Marker marker;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Configuration.getInstance().load(
                requireContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );

        View view = inflater.inflate(R.layout.fragment_osm_map, container, false);

        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // Usa o provedor de tiles padrão
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(12.0);


        marker = new Marker(map);
        map.getOverlays().add(marker);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), this::atualizarMapa);


        LocationData initialLocation = sharedViewModel.getLocationLiveData().getValue();
        if (initialLocation != null) {
            atualizarMapa(initialLocation);
        }

        return view;
    }


    private void atualizarMapa(LocationData locationData) {
        if (locationData == null) {
            Log.d(TAG, "Dados de localização nulos.");
            return;
        }

        Double lat = locationData.getLatitude();
        Double lon = locationData.getLongitude();
        String city = locationData.getCity();

        // Evita mover o mapa se as coordenadas forem 0.0 (indicando falha na busca)
        if (lat == 0.0 || lon == 0.0) {
            Log.d(TAG, "Coordenadas 0,0 inválidas.");
            return;
        }

        GeoPoint point = new GeoPoint(lat, lon);


        mapController.animateTo(point);


        mapController.setZoom(12.0);

        marker.setPosition(point);
        marker.setTitle(city != null ? city : "Umuarama");

        map.invalidate();

        Log.d(TAG, "Mapa movido para: " + lat + ", " + lon + " (" + city + ")");
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}