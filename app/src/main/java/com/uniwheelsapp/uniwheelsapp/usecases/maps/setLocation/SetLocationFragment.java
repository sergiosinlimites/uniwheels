package com.uniwheelsapp.uniwheelsapp.usecases.maps.setLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uniwheelsapp.uniwheelsapp.R;

import java.io.IOException;
import java.util.List;

public class SetLocationFragment extends Fragment {

    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMinZoomPreference(15);
            mMap = googleMap;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_set_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Button btnFindDeparture = view.findViewById(R.id.btnFindDeparture);
        Button btnFindArrival = view.findViewById(R.id.btnFindArrival);
        EditText departure = view.findViewById(R.id.departure);
        EditText arrival = view.findViewById(R.id.arrival);

        btnFindDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String departureText = departure.getText().toString();
                if(departureText.isEmpty()){
                    return;
                }
                Address address = getLocationFromAddress(departureText);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                Log.e("location finder", "lat: " + location.latitude + " lon: " + location.longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(address.getSubAdminArea()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        });
    }

    private Address getLocationFromAddress(String sAddress){
        Geocoder coder = new Geocoder(getContext());
        List<Address> addressList;
        try {
            addressList = coder.getFromLocationName(sAddress, 1);

            if(addressList == null || addressList.size() == 0){
                return null;
            }
            Log.d("address", addressList.toArray().toString());
            Address address = addressList.get(0);

            return address;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}