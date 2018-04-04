package flairinfosystem.com.mobiletracking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpParent extends AppCompatActivity {

    private EditText mNameTxt;
    // private EditText mEmailTxt;
    private EditText mPassText;
    private EditText mConPassText;
    private EditText mBusnoText;
    private EditText mContactText;
    double latitude,longitude;
    private Button mRegSubmitBtn;

    private FirebaseAuth mfireAuth;
    private DatabaseReference fireData;

    private ProgressDialog mProgess;

    GPSTracker gps;
    private static final int REQUEST_CODE_PERMISSION=2;
    String mPermission= Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_parent);

        try{
            if(ActivityCompat.checkSelfPermission(this,mPermission)!= MockPackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        mProgess=new ProgressDialog(this);

        mNameTxt=(EditText)findViewById(R.id.signUsername);
        // mEmailTxt=(EditText)findViewById(R.id.signupemail);
        mPassText=(EditText)findViewById(R.id.signPass);
        mConPassText=(EditText)findViewById(R.id.signCnfmPass);
        mBusnoText=(EditText)findViewById(R.id.busnotext);
        mContactText=(EditText)findViewById(R.id.contactnotext);

        mRegSubmitBtn=(Button)findViewById(R.id.getloc);

        mfireAuth=FirebaseAuth.getInstance();
        fireData= FirebaseDatabase.getInstance().getReference().child("users");

        mRegSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private  void startRegister(){
        gps=new GPSTracker(SignUpParent.this);

        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }


        final String nameval=mNameTxt.getText().toString().trim();
        //  final String emailval=mEmailTxt.getText().toString().trim();
        String passval=mPassText.getText().toString().trim();
        final String conpassval=mConPassText.getText().toString().trim();
        final String busnoval=mBusnoText.getText().toString().trim();
        final String contactnoval=mContactText.getText().toString().trim();

//        final double lat=17.3916;
//        final double lon=78.4401;



        if(!TextUtils.isEmpty(nameval)&& !TextUtils.isEmpty(contactnoval) && !TextUtils.isEmpty(passval) && !TextUtils.isEmpty(conpassval)){


            mProgess.setMessage("Signing Up...");
            mProgess.show();

            mfireAuth.createUserWithEmailAndPassword(contactnoval,passval).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String userid=mfireAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser=fireData.child(userid);

                        // currentUser.child("Email").setValue(emailval);
                        currentUser.child("user").setValue(nameval);
                        currentUser.child("busno").setValue(busnoval);
                        currentUser.child("contactno").setValue(contactnoval);
                        currentUser.child("lat").setValue(latitude);
                        currentUser.child("lon").setValue(longitude);
                        mProgess.dismiss();

                        Intent intent2=new Intent(SignUpParent.this,SignInDriver.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                    }

                }
            });

        }


    }
}
