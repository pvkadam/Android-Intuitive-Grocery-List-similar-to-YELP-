package domain.comany.venkat.srollview;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> all_lats = new ArrayList<Double>();
    private ArrayList<Double> all_longs = new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        googleMap.setMyLocationEnabled(true);


        Intent intent = getIntent();


        Bundle bundle1 = new Bundle();
//        Bundle bundle2 = new Bundle();

        bundle1 = intent.getExtras().getBundle("latitude");
//        bundle2 = intent.getExtras().getBundle("longitude");

//        all_lats = (ArrayList<Double>)bundle1.getSerializable("latitude");
//        all_longs = (ArrayList<Double>)bundle2.getSerializable("longitude");
        ArrayList<HashMap<String,String>> new_list =null;

        new_list = ( ArrayList<HashMap<String,String>>)bundle1.getSerializable("all_values");

        double my_lat=(double)bundle1.getSerializable("mylat");
        double my_long=(double)bundle1.getSerializable("mylong");
        LatLng mlatLng = new LatLng(my_lat, my_long);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//        for (int i =0; i<all_lats.size();i++){
//            LatLng marker = new LatLng(all_lats.get(i),all_longs.get(i));
//            mMap.addMarker(new MarkerOptions().position(marker).title("Name here"));
//        }

        for(int i=0;i<new_list.size();i++){

            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Getting a place from the places list
            HashMap<String, String> hmPlace = new_list.get(i);

            // Getting latitude of the place
            double lat = Double.parseDouble(hmPlace.get("lat"));
            //all_lats.add(lat);

            // Getting longitude of the place
            double lng = Double.parseDouble(hmPlace.get("lng"));
            //all_longs.add(lng);
            // Getting name
            String name = hmPlace.get("place_name");

            // Getting vicinity
            String vicinity = hmPlace.get("vicinity");

            LatLng latLng = new LatLng(lat, lng);

            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker.
            //This will be displayed on taping the marker
            markerOptions.title(name );

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

        }
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
