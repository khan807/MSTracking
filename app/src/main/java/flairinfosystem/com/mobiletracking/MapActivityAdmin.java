package flairinfosystem.com.mobiletracking;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivityAdmin extends AppCompatActivity implements GoogleMap.OnCameraChangeListener {

    private double latitudes;
    private double longitudes;

    private String postkey;
    private GoogleMap googleMap;
    private DatabaseReference mRef;

    private List<Address> addressList;

    private TextView textView,textView1,textView2;

    private String user,contact,vehicle;


    private static final GeoLocation INITIAL_CENTER = new GeoLocation(17.3916, 78.4401);
    private static final int INITIAL_ZOOM_LEVEL = 17;

    private static final String GEO_FIRE_DB = "https://mybusapplication-aca44.firebaseio.com/";
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "users";

    private GoogleMap map;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;

    float bearing;


    private Geocoder geocoder;

    private Marker marker1;

    private Map<String, Marker> markers;

    private LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
    // Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
    // Milliseconds




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_admin);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        textView=(TextView)findViewById(R.id.mapusera);
        textView1=(TextView)findViewById(R.id.mapcontacta);
        textView2=(TextView)findViewById(R.id.mapVehicleNoa);


        mRef= FirebaseDatabase.getInstance().getReference().child("users");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        //if (b != null) {
        //namaPenolong = (String) b.get("name");
        //namaUser = (String) b.get("nameUser");
        //latitude = (Double) b.get("lati");
        //longitude = (Double) b.get("longi");
        //distance = (Integer) b.get("distance");
        //latitudeUser = (Double) b.get("latitudeUser");
        //longitudeUser = (Double) b.get("longitudeUser");
        //Log.d("TAG", "namaPenolong= " + namaPenolong + " & distance= " + distance);
        postkey=(String)b.get("postkey");

        mRef.child(postkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                latitudes=(Double)dataSnapshot.child("lat").getValue();
//                longitudes=(Double)dataSnapshot.child("lon").getValue();

               GPSTracker gps=new GPSTracker(MapActivityAdmin.this);

                if(gps.canGetLocation()) {
                    latitudes = gps.getLatitude();
                    longitudes = gps.getLongitude();
                }
                user=(String)dataSnapshot.child("user").getValue();
                contact=(String)dataSnapshot.child("contactno").getValue().toString();
                vehicle=(String)dataSnapshot.child("busno").getValue().toString();



                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapadmin);
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map=googleMap;

                        //latitudes=locations.getLatitude();
                        //longitudes=locations.getLongitude();

                        GeoLocation GEO_INI=new GeoLocation(latitudes,longitudes);
                        LatLng latLngCenter=new LatLng(GEO_INI.latitude,GEO_INI.longitude);

                        StringBuilder str=new StringBuilder();
                        try {
                            geocoder=new Geocoder(MapActivityAdmin.this, Locale.getDefault());
                            addressList=geocoder.getFromLocation(latitudes,longitudes,1);
                            Address returnaddress=addressList.get(0);
                            String addressLine= returnaddress.getAddressLine(0);
                            String area=returnaddress.getSubLocality();
                            String locality=returnaddress.getLocality();
                            String city=returnaddress.getCountryName();

                            str.append(area + " ");
                            str.append(locality +" ");
                            str.append(city+" ");
                            textView1.setText("Current Location :" +str);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(marker1 !=null){
                            marker1.remove();
                        }

                        MapActivityAdmin.this.marker1=MapActivityAdmin.this.map.addMarker(new MarkerOptions().position(latLngCenter).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcherschoolbus)).title(user).flat(true));
                        marker1.setRotation(bearing);
                        //MapActivityAdmin.this.searchCircle = MapActivityAdmin.this.map.addCircle(new CircleOptions().center(latLngCenter));
                        //MapActivityAdmin.this.searchCircle.setFillColor(Color.argb(1, 1, 1, 1));
                        //MapActivityAdmin.this.searchCircle.setStrokeColor(Color.argb(66, 2, 2, 2));
                        MapActivityAdmin.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
                        MapActivityAdmin.this.map.setOnCameraChangeListener(MapActivityAdmin.this);
                        MapActivityAdmin.this.animateMarkerTo(marker1,latitudes,longitudes);
                    }
                });






                textView.setText("  Name: " + user);
                //textView1.setText(" Contact No: " + contact);
                textView2.setText(" Vehicle No: " + vehicle);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //try{
        //     initializeMap();
        // }
        // catch (Exception e){
        //    e.printStackTrace();
        //}
        // }

        //else{
        //  Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show();
        //}





        FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
//        FirebaseApp app = FirebaseApp.initializeApp(this, options);

        // setup GeoFire
        //      this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));


        this.geoFire=new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(GEO_FIRE_DB));
        // setup markers
        this.markers = new HashMap<String, Marker>();


        this.geoQuery = MapActivityAdmin.this.geoFire.queryAtLocation(INITIAL_CENTER, 1);



    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        //this.geoQuery.removeAllListeners();
        //for (Marker marker: this.markers.values()) {
        //   marker.remove();
        //}
        //this.markers.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
        //this.geoQuery.addGeoQueryEventListener(this);

    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        //this.searchCircle.setCenter(center);
        //this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        //this.geoQuery.setRadius(radius/1000);

    }

    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }
}
