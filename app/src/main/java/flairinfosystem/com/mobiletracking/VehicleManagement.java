package flairinfosystem.com.mobiletracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VehicleManagement extends AppCompatActivity {

    Button add_btn;
    ListView regVeh_lv;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String getUrl = "http://practoe.flairinfosystems.in/practoe/vehicleslist.php";

    ArrayList<String> drinameArray = new ArrayList<>();
    ArrayList<String> vehnoArray = new ArrayList<>();
    ArrayList<String> drinoArray = new ArrayList<>();
   /* ArrayList<String> floornoArray = new ArrayList<>();
    ArrayList<String> doornoArray = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_management);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_btn =  findViewById(R.id.add_vehicle);
        regVeh_lv =  findViewById(R.id.vehicle_list);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(VehicleManagement.this, AddVehicle.class);
                startActivity(in);
            }
        });

       /*regUser_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(UserManagement.this, ApartDetails.class);
                in.putExtra("appartname", appartnameArray.get(position).toString());
                in.putExtra("appartno", appartnoArray.get(position).toString());
                in.putExtra("appartloc", appartlocArray.get(position).toString());


                startActivity(in);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        vehnoArray.clear();

        new ViewUserList().execute();
    }

    public class ViewUserList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VehicleManagement.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> args = new ArrayList<>();
                JSONObject object = parser.makeHttpRequest(getUrl, "GET", args);
                response = object.getJSONArray("vehicleslist");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);

                    String vno = c.getString("vehicleno");
                    String name = c.getString("drivername");
                    String dno = c.getString("driverno");

                    drinameArray.add(name);
                    vehnoArray.add(vno);
                    drinoArray.add(dno);
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // ArrayAdapter<String> fadapter = new ArrayAdapter<String>(VehicleManagement.this, android.R.layout.simple_list_item_1, nameArray);
            regVeh_lv.setAdapter(new VehicleBaseClass());

            progressDialog.dismiss();
        }
    }


    public class VehicleBaseClass extends BaseAdapter {

        @Override
        public int getCount() {
            return vehnoArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.listview, null);

            final String nameStr = drinameArray.get(i).toString();
            final String vehnoStr = vehnoArray.get(i).toString();
            final String mobileStr = drinoArray.get(i).toString();

            TextView sno = view.findViewById(R.id.sno);
            sno.setText("" + (i + 1));
            TextView name = view.findViewById(R.id.name);
            name.setText(vehnoStr);

            Button edit = view.findViewById(R.id.edit);
            // Button delete = view.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i1 = new Intent(VehicleManagement.this, EditVehicle.class);
                    i1.putExtra("driname", nameStr);
                    i1.putExtra("vehno", vehnoStr);
                    i1.putExtra("drino", mobileStr);
                    startActivity(i1);
                }
            });



            return view;
        }
    }


}
