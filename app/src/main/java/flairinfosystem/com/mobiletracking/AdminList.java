package flairinfosystem.com.mobiletracking;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static String UPlat = "";
    public static double UPlng;

    private DatabaseReference mDatabase;

    private String lat1,lat2;


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
    // Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 60*1000; // in
    // Milliseconds

    protected LocationManager locationManager;
    double latitudes,longitudes;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.listadmin);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

         gps=new GPSTracker(AdminList.this);


        if(gps.canGetLocation()) {
            latitudes = gps.getLatitude();
            longitudes = gps.getLongitude();
        }

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHoldere>(
                Users.class,
                R.layout.list_user2,
                UserViewHoldere.class,
                databaseReference
        ) {


            @Override
            protected void populateViewHolder(UserViewHoldere viewHolder, Users model, int position) {
                viewHolder.setUser(model.getUser());
                viewHolder.setBusno(model.getBusno());
                viewHolder.setNumber(model.getNumber());

                final String postid = getRef(position).getKey();
                //String postlat=model.getLat().toString();
                final double postlat = model.getLat();
                final double postlongi = model.getLon();

                final String stringlat = Double.toString(latitudes);
                final String stringlong = Double.toString(longitudes);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AdminList.this, stringlat+""+stringlong, Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(AdminList.this, MapActivityAdmin.class);
                        //intent2.putExtra("lati",postlat);
                        //intent2.putExtra("longi",postlongi);
                        intent2.putExtra("postkey", postid);
                        startActivity(intent2);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();


    }

    public static class UserViewHoldere extends RecyclerView.ViewHolder {


        View mView;

        public UserViewHoldere(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setUser(String user) {
            TextView textView = (TextView) mView.findViewById(R.id.usrname1e);
            textView.setText(user);
        }

//        public void setLat(double lat) {
//            //TextView textView1=(TextView)mView.findViewById(R.id.usrname2);
//            //NumberFormat nm=NumberFormat.getNumberInstance();
//
//            //String numlat=Double.toString(lat);
//            //textView1.setText(numlat);
//        }

        public void setNumber(String contactno) {
            TextView textView2 = (TextView) mView.findViewById(R.id.usrname3e);
            //NumberFormat nm=NumberFormat.getNumberInstance();

            //String numlon = Double.toString(number);
            textView2.setText(contactno);
        }

        public void setBusno(String busno) {
            TextView textView1 = (TextView) mView.findViewById(R.id.usrname2e);

            // String busn=Long.toString(busno);
            textView1.setText(busno);
        }

    }

}

