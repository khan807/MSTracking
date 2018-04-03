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

    public class Tracking extends AppCompatActivity {

        private RecyclerView recyclerView;
        private ProgressDialog progressDialog;
        private DatabaseReference databaseReference;
        private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

        private DatabaseReference mDatabaseUsers;
        private FirebaseAuth mAuth;
        private FirebaseUser mCurrentUser;
        private FirebaseAuth.AuthStateListener mAuthListener;

        public static String UPlat = "";
        public static String UPlng = "";

        private DatabaseReference mDatabase;

        private String lat1,lat2;


        private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
        // Meters
        private static final long MINIMUM_TIME_BETWEEN_UPDATES = 60*1000; // in
        // Milliseconds

        protected LocationManager locationManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tracking);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


            recyclerView = (RecyclerView) findViewById(R.id.listadmin);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

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
                    viewHolder.setLon(model.getLon());

                    final String postid = getRef(position).getKey();
                    //String postlat=model.getLat().toString();
                    final double postlat = model.getLat();
                    final double postlongi = model.getLon();

                    final String stringlan = Double.toString(postlat);
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Tracking.this, stringlan, Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(Tracking.this, MapActivityAdmin.class);
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

            public void setLat(double lat) {
                //TextView textView1=(TextView)mView.findViewById(R.id.usrname2);
                //NumberFormat nm=NumberFormat.getNumberInstance();

                //String numlat=Double.toString(lat);
                //textView1.setText(numlat);
            }

            public void setLon(double lon) {
                TextView textView2 = (TextView) mView.findViewById(R.id.usrname3e);
                //NumberFormat nm=NumberFormat.getNumberInstance();

                String numlon = Double.toString(lon);
                textView2.setText(numlon);
            }

            public void setBusno(String busno) {
                TextView textView1 = (TextView) mView.findViewById(R.id.usrname2e);

                // String busn=Long.toString(busno);
                textView1.setText(busno);
            }

        }

    }



//    Spinner sci_spinner,veh_spinner;
//    Button track_btn;
//    String getsci_str;
//
//    ProgressDialog progressDialog;
//    JSONParser parser=new JSONParser();
//    JSONArray response;
//    private static final String TAG_SUCCESS="success";
//    String getsciURL="";
//    String getvehURL="";
//    ArrayList<String> scinameArray=new ArrayList<>();
//    ArrayList<String> drinameArray=new ArrayList<>();
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tracking);
//        sci_spinner=findViewById(R.id.sci_spinner);
//        veh_spinner=findViewById(R.id.veh_spinner);
//        track_btn=findViewById(R.id.track);
//
//        sci_spinner.setOnItemSelectedListener(this);
//        new GetSchool().execute();
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        switch (adapterView.getId()){
//            case R.id.sci_spinner:
//                getsci_str=adapterView.getItemAtPosition(i).toString();
//                new GetBusNumber().execute();
//                break;
//        }
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//    public  class GetSchool extends AsyncTask<String,String,String>{
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try{
//            List<NameValuePair> args=new ArrayList<>();
//            JSONObject object=parser.makeHttpRequest(getsciURL,"GET",args);
//                response=object.getJSONArray("getsci");
//                for(int i=0;i<response.length();i++){
//                    JSONObject c=response.getJSONObject(i);
//                    String name=c.getString("sciname");
//                    scinameArray.add(name);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//        @Override
//        protected  void onPostExecute(String s){
//            super.onPostExecute(s);
//            populateSpinner();
//        }
//    }
//    private void populateSpinner(){
//        List<String> label=new ArrayList<>();
//        for(int i=0;i<scinameArray.size();i++){
//            label.add(scinameArray.get(i).toString());
//        }
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,label);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        sci_spinner.setAdapter(adapter);
//    }
//    public class GetBusNumber extends AsyncTask<String,String,String>{
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try{
//                List<NameValuePair> args=new ArrayList<>();
//                args.add(new BasicNameValuePair("sciname",getsci_str));
//                JSONObject object=parser.makeHttpRequest(getvehURL,"POST",args);
//                response=object.getJSONArray("getvehicle");
//                for(int i=0;i<response.length();i++){
//                    JSONObject c=response.getJSONObject(i);
//                    String driname=c.getString("drivername");
//                    drinameArray.add(driname);
//                }
//
//            }catch (JSONException e){
//                e.printStackTrace();
//
//            }
//            return null;
//        }
//        protected void onPostExecute(String s){
//            super.onPostExecute(s);
//            populateVehSpinner();
//        }
//    }
//    public void populateVehSpinner(){
//        List<String> label=new ArrayList<>();
//
//        for(int i=0;i<drinameArray.size();i++){
//            label.add(drinameArray.get(i).toString());
//        }
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,label);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        veh_spinner.setAdapter(adapter);
//    }
//}
