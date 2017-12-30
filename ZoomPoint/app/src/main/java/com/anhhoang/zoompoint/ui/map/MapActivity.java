package com.anhhoang.zoompoint.ui.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.anhhoang.zoompoint.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String LAT_LNG_KEY = "LatLngKey";
    private GoogleMap mMap;
    private LatLng photoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();

        if (!intent.hasExtra(LAT_LNG_KEY) || !(intent.getParcelableExtra(LAT_LNG_KEY) instanceof LatLng)) {
            throw new IllegalArgumentException("LatLng must be passed as parameter");
        }

        photoPosition = intent.getParcelableExtra(LAT_LNG_KEY);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(photoPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(photoPosition, 10));

    }

    public static Intent getStartingIntent(Context context, double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(LAT_LNG_KEY, latLng);

        return intent;
    }
}
