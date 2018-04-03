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

public class EditTrackingNo extends Activity {
    TextInputEditText trackno_TIL;
    Button update_btn;
    String tracknoStr,getTrackNo;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    private static final String TAG_SUCCESS = "success";


    public String addUrl = "http://practoe.flairinfosystems.in/practoe/edittrackno.php";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracking_no);

        getTrackNo=getIntent().getStringExtra("trackno");
        trackno_TIL=findViewById(R.id.trackno);
        trackno_TIL.setText(getTrackNo);
        update_btn=findViewById(R.id.update);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((trackno_TIL.getText().toString().length())>0 )
                {

                    new LineAdding().execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Enter Tracking Number",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public class LineAdding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditTrackingNo.this);
            progressDialog.setMessage("Please wait......");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            tracknoStr=trackno_TIL.getText().toString();

            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("trackno",getTrackNo));
            args.add(new BasicNameValuePair("newtrackno",tracknoStr));


            JSONObject json = parser.makeHttpRequest(addUrl, "POST", args);

            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user

                    Intent i=new Intent(EditTrackingNo.this,TrackingNoManagement.class);
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
            Intent i=new Intent(EditTrackingNo.this,TrackingNoManagement.class);
            startActivity(i);
            progressDialog.dismiss();



        }
    }

}
