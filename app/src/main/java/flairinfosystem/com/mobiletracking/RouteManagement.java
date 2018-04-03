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


public class RouteManagement extends AppCompatActivity {

    Button addRoute_btn;
    ListView regRoute_lv;


    ProgressDialog progressDialog;
    JSONParser parser = new JSONParser();
    JSONArray response;
    public String getUrl = "http://practoe.flairinfosystems.in/practoe/routelist.php";
    public String deleteUrl = "http://practoe.flairinfosystems.in/practoe/deleteroute.php";

    private static final String TAG_SUCCESS = "success";


    ArrayList<String> nameArray = new ArrayList<>();
   /* ArrayList<String> appartnoArray = new ArrayList<>();
    ArrayList<String> appartlocArray = new ArrayList<>();
    ArrayList<String> floornoArray = new ArrayList<>();
    ArrayList<String> doornoArray = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addRoute_btn =  findViewById(R.id.add);
        regRoute_lv = findViewById(R.id.route_list);

        addRoute_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(RouteManagement.this, AddRoute.class);
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
            progressDialog = new ProgressDialog(RouteManagement.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> args = new ArrayList<>();
                JSONObject object = parser.makeHttpRequest(getUrl, "GET", args);
                response = object.getJSONArray("routelist");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);

                     String name  = c.getString("routename");

                    nameArray.add(name);
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  ArrayAdapter<String> fadapter = new ArrayAdapter<String>(RouteManagement.this, android.R.layout.simple_list_item_1, nameArray);
            regRoute_lv.setAdapter(new RouteBaseClass());

            progressDialog.dismiss();
        }
    }
    public class RouteBaseClass extends BaseAdapter {
        String nameArrayStr;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.listview, null);


            TextView sno = view.findViewById(R.id.sno);
            sno.setText("" + (i + 1));
            TextView name = view.findViewById(R.id.name);
            nameArrayStr=nameArray.get(i).toString();
            name.setText(nameArrayStr);

            // String nameStr = name.getText().toString();


            Button edit = view.findViewById(R.id.edit);
            //Button delete = view.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i1 = new Intent(RouteManagement.this, EditRoute.class);
                    i1.putExtra("name", nameArray.get(i).toString());
                    startActivity(i1);
                }
            });



            return view;
        }

    }

}
//        public class DeleteDetails extends AsyncTask<String, String, String> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog = new ProgressDialog(RouteManagement.this);
//                progressDialog.setMessage("Please wait...");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//
//
//                List<NameValuePair> args = new ArrayList<>();
//                args.add(new BasicNameValuePair("routename",nameArrayStr));
//                JSONObject json = parser.makeHttpRequest(deleteUrl, "POST", args);
//
//                Log.d("Create Response", json.toString());
//
//                // check for success tag
//                try {
//                    int success = json.getInt(TAG_SUCCESS);
//
//                    if (success == 1) {
//                        // successfully created a user
//
//                        Toast.makeText(RouteManagement.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
//                        // closing this screen
//                    } else {
//                        // failed to create user
//                        Log.d("failed to create user", json.toString());
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                return null;
//            }
//
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                Toast.makeText(RouteManagement.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
//
//                progressDialog.dismiss();
//
//
//            }
//        }

  //          delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RouteManagement.this);
//                    alertDialogBuilder.setMessage("Are you sure,you want to delete?");
//                    alertDialogBuilder.setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//
//                                    new DeleteDetails().execute();
//                                }
//                            });
//
//                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                }
//
//            });