package flairinfosystem.com.mobiletracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {

    Button userManag_btn, vehicleManag_btn, routeManag_btn, sciManag_btn,
            tracking_btn, trackNoManag_btn;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        userManag_btn=findViewById(R.id.user_manag);
        vehicleManag_btn=findViewById(R.id.vehicle_manag);
        routeManag_btn=findViewById(R.id.route_manag);
        sciManag_btn=findViewById(R.id.sci_manag);
        tracking_btn=findViewById(R.id.tracking);
        trackNoManag_btn=findViewById(R.id.trackno_manag);

        userManag_btn.setOnClickListener(this);
        vehicleManag_btn.setOnClickListener(this);
        routeManag_btn.setOnClickListener(this);
        sciManag_btn.setOnClickListener(this);
        trackNoManag_btn.setOnClickListener(this);
        tracking_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent1 = new Intent(AdminDashboard.this, SignIn.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                    finish();
                }
            }
        };



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.user_manag:
                startActivity(new Intent(getApplicationContext(),UserManagement.class));
                break;
            case R.id.vehicle_manag:
                startActivity(new Intent(getApplicationContext(),VehicleManagement.class));
                break;
            case R.id.route_manag:
                startActivity(new Intent(getApplicationContext(),RouteManagement.class));
                break;
            case R.id.sci_manag:
                startActivity(new Intent(getApplicationContext(),SCIManagement.class));
                break;
            case R.id.tracking:
                startActivity(new Intent(getApplicationContext(),Tracking.class));
                break;
            case R.id.trackno_manag:
                startActivity(new Intent(getApplicationContext(),TrackingNoManagement.class));
                break;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_setting){
            mAuth.signOut();


        }
        return super.onOptionsItemSelected(item);
    }
}
