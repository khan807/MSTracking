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

public class AddSCI extends Activity {
    TextInputEditText name_TIL,address_TIL,mobile_TIL;
    Button add_btn;
    String nameString,noString,addressString;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    private static final String TAG_SUCCESS = "success";


    public String addUrl = "http://practoe.flairinfosystems.in/practoe/addsci.php";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sci);

        name_TIL=findViewById(R.id.name);
        address_TIL=findViewById(R.id.address);
        mobile_TIL=findViewById(R.id.mobile);
        add_btn=findViewById(R.id.add);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((name_TIL.getText().toString().length())>0 &&
                        (address_TIL.getText().toString().length())>0 &&
                        (mobile_TIL.getText().toString().length())>0 )
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
            progressDialog = new ProgressDialog(AddSCI.this);
            progressDialog.setMessage("Please wait......");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            nameString=name_TIL.getText().toString();
            addressString=address_TIL.getText().toString();
            noString=mobile_TIL.getText().toString();

            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("name",nameString));
            args.add(new BasicNameValuePair("address",addressString));
            args.add(new BasicNameValuePair("mobile",noString));

            JSONObject json = parser.makeHttpRequest(addUrl, "POST", args);

            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user

                    Intent i=new Intent(AddSCI.this,SCIManagement.class);
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
            Intent i=new Intent(AddSCI.this,SCIManagement.class);
            startActivity(i);
            progressDialog.dismiss();



        }
    }

}
