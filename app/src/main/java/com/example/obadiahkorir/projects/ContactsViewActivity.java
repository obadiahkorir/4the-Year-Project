package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/27/2018.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
public class ContactsViewActivity extends AppCompatActivity {

    ListView MobileDetailsListView;
    ProgressBar MobileProgressBar;
    String HttpUrl = "https://chemisoftsolutions.000webhostapp.com/android/MobileData.php";
    List<String> MobileList = new ArrayList<String>();
    ArrayAdapter<String> MobileArrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contactsview);

        MobileDetailsListView = (ListView)findViewById(R.id.listview1);

        MobileProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        new GetHttpResponse(ContactsViewActivity.this).execute();

        MobileDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(),ShowDetailsActivity.class);

                intent.putExtra("ListViewValue", MobileList.get(position).toString());

                startActivity(intent);

            }
        });


    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();

                if(httpServicesClass.getResponseCode() == 200)
                {
                    JSonResult = httpServicesClass.getResponse();

                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);

                                MobileList.add(jsonObject.getString("MobileCompany").toString()) ;

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            MobileProgressBar.setVisibility(View.GONE);

            MobileDetailsListView.setVisibility(View.VISIBLE);

            // Start code for remove duplicate listview values.

            HashSet<String> hashSet = new HashSet<String>();

            hashSet.addAll(MobileList);
            MobileList.clear();
            MobileList.addAll(hashSet);

            //End code here for remove duplicate values.

            MobileArrayAdapter = new ArrayAdapter<String>(ContactsViewActivity.this,android.R.layout.simple_list_item_2, android.R.id.text1, MobileList);

            MobileDetailsListView.setAdapter(MobileArrayAdapter);

        }
    }

}