package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/2/2018.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
public class ViewActivity extends Activity {

    ListView listCollege;
    ProgressBar proCollageList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view);

        listCollege = (ListView)findViewById(R.id.listCollege);
        proCollageList = (ProgressBar)findViewById(R.id.proCollageList);

        new GetHttpResponse(this).execute();
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        private Context context;
        String result;
        List<fire> collegeList;
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
            HttpService httpService = new HttpService("https://chemisoftsolutions.000webhostapp.com/android/courses.php");
            try
            {
                httpService.ExecutePostRequest();

                if(httpService.getResponseCode() == 200)
                {
                    result = httpService.getResponse();
                    Log.d("Result", result);
                    if(result != null)
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(result);

                            JSONObject object;
                            JSONArray array;
                           fire college;
                            collegeList = new ArrayList<fire>();
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                college = new fire();
                                object = jsonArray.getJSONObject(i);

                                college.fire_location = object.getString("fire_location");
                                college.fire_description = object.getString("fire_description");

                                collegeList.add(college);
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
                    Toast.makeText(context, httpService.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
            proCollageList.setVisibility(View.GONE);
            listCollege.setVisibility(View.VISIBLE);
            if(collegeList != null)
            {
                ViewListAdapter adapter = new ViewListAdapter(collegeList, context);
                listCollege.setAdapter(adapter);
            }
        }
    }

    public class HttpService
    {
        private ArrayList <NameValuePair> params;
        private ArrayList <NameValuePair> headers;

        private String url;
        private int responseCode;
        private String message;
        private String response;

        public String getResponse()
        {
            return response;
        }

        public String getErrorMessage()
        {
            return message;
        }

        public int getResponseCode()
        {
            return responseCode;
        }

        public HttpService(String url)
        {
            this.url = url;
            params = new ArrayList<NameValuePair>();
            headers = new ArrayList<NameValuePair>();
        }

        public void AddParam(String name, String value)
        {
            params.add(new BasicNameValuePair(name, value));
        }

        public void AddHeader(String name, String value)
        {
            headers.add(new BasicNameValuePair(name, value));
        }

        public void ExecuteGetRequest() throws Exception
        {
            String combinedParams = "";
            if(!params.isEmpty())
            {
                combinedParams += "?";
                for(NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                    if(combinedParams.length() > 1)
                    {
                        combinedParams += "&" + paramString;
                    }
                    else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request, url);
        }

        public void ExecutePostRequest() throws Exception
        {
            HttpPost request = new HttpPost(url);
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if(!params.isEmpty())
            {
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }

            executeRequest(request, url);
        }

        private void executeRequest(HttpUriRequest request, String url)
        {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse;
            try
            {
                httpResponse = client.execute(request);
                responseCode = httpResponse.getStatusLine().getStatusCode();
                message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null)
                {
                    InputStream instream = entity.getContent();
                    response = convertStreamToString(instream);
                    instream.close();
                }
            }
            catch (ClientProtocolException e)
            {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }
            catch (IOException e)
            {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }
        }

        private String convertStreamToString(InputStream is)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }



}