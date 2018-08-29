package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/1/2018.
 */
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class SearchActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    String HttpURL = "https://chemisoftsolutions.000webhostapp.com/android/SubjectFullForm.php";
    ListAdapter listAdapter;
    ProgressBar progressBar ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.listView1);

        editText = (EditText) findViewById(R.id.edittext1);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

                Toast.makeText(SearchActivity.this, ListViewClickData.getSubName(), Toast.LENGTH_SHORT).show();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {

                listAdapter.getFilter().filter(stringVar.toString());
            }
        });

        new ParseJSonDataClass(this).execute();

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;

        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpServiceClass httpServiceClass = new HttpServiceClass(HttpURL);

            try {
                httpServiceClass.ExecutePostRequest();

                if (httpServiceClass.getResponseCode() == 200) {

                    FinalJSonResult = httpServiceClass.getResponse();

                    if (FinalJSonResult != null) {

                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(FinalJSonResult);

                            JSONObject jsonObject;

                            Subject subject;

                            SubjectList = new ArrayList<Subject>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String tempName = jsonObject.getString("SubjectName").toString();

                                String tempFullForm = jsonObject.getString("SubjectFullForm").toString();

                                subject = new Subject(tempName, tempFullForm);

                                SubjectList.add(subject);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.INVISIBLE);
            listAdapter = new ListAdapter(SearchActivity.this, R.layout.custom_layout, SubjectList);
            listView.setAdapter(listAdapter);
        }
    }

}