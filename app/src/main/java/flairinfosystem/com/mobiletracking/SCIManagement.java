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


public class SCIManagement extends AppCompatActivity {

    Button addSCI_btn;
    ListView regSCI_lv;
    String getIdStr;
    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String getUrl = "http://practoe.flairinfosystems.in/practoe/scilist.php";

    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> addressArray = new ArrayList<>();
    ArrayList<String> mobileArray = new ArrayList<>();
   /* ArrayList<String> floornoArray = new ArrayList<>();
    ArrayList<String> doornoArray = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scimanagement);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addSCI_btn =  findViewById(R.id.add);
        regSCI_lv =  findViewById(R.id.schools_list);

        addSCI_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(SCIManagement.this, AddSCI.class);
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
        nameArray.clear();



        new ViewUserList().execute();
    }

    public class ViewUserList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SCIManagement.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> args = new ArrayList<>();
                JSONObject object = parser.makeHttpRequest(getUrl, "GET", args);
                response = object.getJSONArray("scilist");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);

                    String name = c.getString("sciname");
                    String email = c.getString("address");
                    String mobile = c.getString("mobileno");

                    nameArray.add(name);
                    addressArray.add(email);
                    mobileArray.add(mobile);
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ArrayAdapter<String> fadapter = new ArrayAdapter<String>(SCIManagement.this, android.R.layout.simple_list_item_1, nameArray);
            regSCI_lv.setAdapter(new SCIbaseclass());

            progressDialog.dismiss();
        }
    }

    public class SCIbaseclass extends BaseAdapter
    {

        @Override
        public int getCount() {
            return nameArray.size();
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

            final String nameStr = nameArray.get(i).toString();
            final String addressStr = addressArray.get(i).toString();
            final String mobileStr = mobileArray.get(i).toString();

            TextView sno = view.findViewById(R.id.sno);
            sno.setText("" + (i + 1));
            TextView name = view.findViewById(R.id.name);
            name.setText(nameStr);

            Button edit = view.findViewById(R.id.edit);
            // Button delete = view.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i1 = new Intent(SCIManagement.this, EditSCI.class);
                    i1.putExtra("name", nameStr);
                    i1.putExtra("address", addressStr);
                    i1.putExtra("mobile", mobileStr);
                    startActivity(i1);
                }
            });



            return view;
        }
    }

}
