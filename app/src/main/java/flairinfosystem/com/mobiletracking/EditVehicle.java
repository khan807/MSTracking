package flairinfosystem.com.mobiletracking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditVehicle extends Activity {
    TextInputEditText drivername_TIL,vehicleno_TIL,drimobile_TIL;
    Button update_btn;
    String drivernameStr,vehiclenoStr,drimobileStr,getdname,getvno,getdno;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    private static final String TAG_SUCCESS = "success";


    public String addUrl = "http://practoe.flairinfosystems.in/practoe/editvehicle.php";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        getdname=getIntent().getStringExtra("driname");
        getdno=getIntent().getStringExtra("drino");
        getvno=getIntent().getStringExtra("vehno");

        drivername_TIL=findViewById(R.id.dri_name);
        drivername_TIL.setText(getdname);
        vehicleno_TIL=findViewById(R.id.veh_no);
        vehicleno_TIL.setText(getvno);
        drimobile_TIL=findViewById(R.id.mobile);
        drimobile_TIL.setText(getdno);

        update_btn=findViewById(R.id.update);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((drimobile_TIL.getText().toString().length())>0 &&
                        (drivername_TIL.getText().toString().length())>0 &&
                        (vehicleno_TIL.getText().toString().length())>0 )
                {

                    new LineAdding().execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Fill all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public class LineAdding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditVehicle.this);
            progressDialog.setMessage("Please wait......");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            vehiclenoStr=vehicleno_TIL.getText().toString();
            drimobileStr=drimobile_TIL.getText().toString();
            drivernameStr=drivername_TIL.getText().toString();

            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("driverno",getIntent().getStringExtra("drino")));
            args.add(new BasicNameValuePair("vehicleno",vehiclenoStr));
            args.add(new BasicNameValuePair("drivername",drivernameStr));
            args.add(new BasicNameValuePair("newdriverno",drimobileStr));

            JSONObject json = parser.makeHttpRequest(addUrl, "POST", args);

            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user

                    Intent i=new Intent(EditVehicle.this,VehicleManagement.class);
                    startActivity(i);
                    // closing this screen
                    finish();
                } else {
                    // failed to create user
                    Log.d("failed to create user", json.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i=new Intent(EditVehicle.this,VehicleManagement.class);
            startActivity(i);
            progressDialog.dismiss();



        }
    }

}
