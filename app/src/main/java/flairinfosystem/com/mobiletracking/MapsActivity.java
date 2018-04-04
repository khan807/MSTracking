package flairinfosystem.com.mobiletracking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.Tracker;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnCameraChangeListener  {

    //implements GeoQueryEventListener, GoogleMap.OnCameraChangeListener

    private double latitudes;
    private double longitudes;

    private String postkey;
    private GoogleMap googleMap;
    private DatabaseReference mRef;

    private List<Address> addressList;

    private TextView textView,textView1,textView2;

    double llt,lln;

    private String user,contact,vehicle;


    private static final GeoLocation INITIAL_CENTER = new GeoLocation(17.3916, 78.4401);
    private static final int INITIAL_ZOOM_LEVEL = 16;

    private static final String GEO_FIRE_DB = "https://mybusapplication-aca44.firebaseio.com/";
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/users";

    private GoogleMap map;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;


    private Geocoder geocoder;

    private Marker marker1;

    private GoogleApiClient mGoogleApiClient;

    private Map<String, Marker> markers;

    private LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
    // Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5*1000; // in
    // Milliseconds


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    private Button button,button1;

    private android.support.v7.app.AlertDialog.Builder dialog;
    boolean gps_enabled,network_enabled;

    public Criteria criteria;
    public String bestProvider;

    private Location locations;




    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textView=(TextView)findViewById(R.id.mapuser);
        textView1=(TextView)findViewById(R.id.mapcontact);
        textView2=(TextView)findViewById(R.id.mapVehicleNo);

        button=(Button)findViewById(R.id.butnlogout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent1 = new Intent(MapsActivity.this, LoginAs.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                    finish();
                }
            }
        };
        mCurrentUser = mAuth.getCurrentUser();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());


        if(locationManager==null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());





        //criteria = new Criteria();
        //bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        //locationManager.requestLocationUpdates(bestProvider, 1000, 0, new MyLocationListener());



        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            dialog = new android.support.v7.app.AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapsActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

        }

        locations = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //locate=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //latitudes = locations.getLatitude();
        //longitudes = locations.getLongitude();

        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = (String) dataSnapshot.child("user").getValue();
                vehicle = (String) dataSnapshot.child("busno").getValue();
                contact = (String) dataSnapshot.child("contactno").getValue();
                latitudes=(Double)dataSnapshot.child("lat").getValue();
                longitudes=(Double)dataSnapshot.child("lon").getValue();




                textView.setText(user);
                textView2.setText(vehicle);
                textView1.setText(contact);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mRef= FirebaseDatabase.getInstance().getReference().child("users");

  /* Intent intent = getIntent();
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
                    //latitudes=(Double)dataSnapshot.child("lat").getValue();
                    //longitudes=(Double)dataSnapshot.child("lon").getValue();
                    user=(String)dataSnapshot.child("user").getValue();
                    contact=(String)dataSnapshot.child("contactno").getValue().toString();
                    vehicle=(String)dataSnapshot.child("busno").getValue().toString();



                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map=googleMap;

                            //latitudes=locations.getLatitude();
                            //longitudes=locations.getLongitude();

                            GeoLocation GEO_INI=new GeoLocation(locations.getLatitude(),locations.getLongitude());
                            LatLng latLngCenter=new LatLng(GEO_INI.latitude,GEO_INI.longitude);

                            latitudes=GEO_INI.latitude;
                            longitudes=GEO_INI.longitude;

                            mRef.child(postkey).child("lat").setValue(latitudes);
                            mRef.child(postkey).child("lon").setValue(longitudes);

                            StringBuilder str=new StringBuilder();
                            try {
                                geocoder=new Geocoder(MapsActivity.this, Locale.getDefault());
                                addressList=geocoder.getFromLocation(latitudes,longitudes,1);
                                Address returnaddress=addressList.get(0);
                                String addressLine= returnaddress.getAddressLine(0);
                                String area=returnaddress.getSubLocality();
                                String locality=returnaddress.getLocality();
                                String city=returnaddress.getCountryName();

                                str.append(area + " ");
                                str.append(locality +"" );
                                str.append(city+"");
                                textView1.setText("contact :" +str);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            MapsActivity.this.marker1=MapsActivity.this.map.addMarker(new MarkerOptions().position(latLngCenter).title(user));
                            MapsActivity.this.searchCircle = MapsActivity.this.map.addCircle(new CircleOptions().center(latLngCenter));
                            MapsActivity.this.searchCircle.setFillColor(Color.argb(1, 1, 1, 1));
                            MapsActivity.this.searchCircle.setStrokeColor(Color.argb(66, 2, 2, 2));
                            MapsActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
                            MapsActivity.this.map.setOnCameraChangeListener(MapsActivity.this);
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
            */

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


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                //latitudes=locations.getLatitude();
                //longitudes=locations.getLongitude();

            }
        });


        FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
//        FirebaseApp app = FirebaseApp.initializeApp(this, options);

        // setup GeoFire
        //      this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));


        this.geoFire = new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(GEO_FIRE_DB));
        // setup markers
        this.markers = new HashMap<String, Marker>();


        this.geoQuery = MapsActivity.this.geoFire.queryAtLocation(INITIAL_CENTER, 1);




    }


    public Location getCoarseLocation(){
        if(locations !=null){
            return locations;
        }else return  null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        this.geoQuery.removeAllListeners();
        for (Marker marker: this.markers.values()) {
            marker.remove();
        }
        this.markers.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
        // this.geoQuery.addGeoQueryEventListener(this);

        mAuth.addAuthStateListener(mAuthListener);

    }


    private void initializeMap(){

        if(googleMap==null){
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);


                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes, longitudes));
                    googleMap.addMarker(marker);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(latitudes, longitudes)).zoom(8).build();




                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(latitudes, longitudes))
                            .width(5)
                            .color(Color.RED));
                }
            });
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //initializeMap();
    }
/*
    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        Marker marker = this.map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));//location.latitude,location.longitude is here
        this.markers.put(key, marker);

    }

    @Override
    public void onKeyExited(String key) {
        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }
    }
*/
 /*   @Override
    public void onKeyMoved(String key, GeoLocation location) {

        Marker marker = this.markers.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);//location.latitude,loc.long was here
        }

    }
*/
  /*  @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    */

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
        final long DURATION_MS = 100;
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






    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            double lt=location.getLatitude();
            double ln=location.getLongitude();



            //LatLng latlng1=new LatLng(lt,ln);

            //marker1=googleMap.addMarker(new MarkerOptions().position(latlng1));

            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng1));

            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(8));


            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());

            Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();

            if(location!=null) {
                final GeoLocation GEO_INI = new GeoLocation(location.getLatitude(), location.getLongitude());
                LatLng latLngCenter = new LatLng(GEO_INI.latitude, GEO_INI.longitude);

                //latitudes = GEO_INI.latitude;
                //longitudes = GEO_INI.longitude;

                //mRef.child(postkey).child("lat").setValue(latitudes);
                //mRef.child(postkey).child("lon").setValue(longitudes);




                StringBuilder str = new StringBuilder();
                try {
                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    addressList = geocoder.getFromLocation(lt, ln, 1);
                    Address returnaddress = addressList.get(0);
                    String addressLine = returnaddress.getAddressLine(0);
                    String area = returnaddress.getSubLocality();
                    String locality = returnaddress.getLocality();
                    String city = returnaddress.getCountryName();


                    str.append(addressLine + "");
                    str.append(area + " ");
                    str.append(locality + " ");
                    str.append(city + " ");
                    textView1.setText("contact :" + str);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(marker1!=null){
                    marker1.remove();
                }

                MapsActivity.this.marker1 = MapsActivity.this.map.addMarker(new MarkerOptions().position(latLngCenter).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcherschoolbus)).title(user));

                //MapActivity.this.searchCircle = MapActivity.this.map.addCircle(new CircleOptions().center(latLngCenter));
                //MapActivity.this.searchCircle.setFillColor(Color.argb(1, 1, 1, 1));
                //MapActivity.this.searchCircle.setStrokeColor(Color.argb(66, 2, 2, 2));
                MapsActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
                MapsActivity.this.map.setOnCameraChangeListener(MapsActivity.this);
                MapsActivity.this.animateMarkerTo(marker1,lt,ln);

            }




            llt=location.getLatitude();
            llt=location.getLongitude();
            try{
                mDatabase.child("lat").setValue(lt);
                mDatabase.child("lon").setValue(ln);


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(MapsActivity.this, "GPS is ON", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(MapsActivity.this, "Location is Off", Toast.LENGTH_SHORT).show();
        }
    }



}
