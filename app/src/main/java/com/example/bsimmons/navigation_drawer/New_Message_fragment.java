package com.example.bsimmons.navigation_drawer;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by bsimmons on 15/06/2015.
 */

public class New_Message_fragment extends Fragment {

    private ArrayList<Game> games;
    private ListView listView;
    private List<String> list;
    private String player_name;
    private String player_team;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        Intent i = getActivity().getIntent();
        player_name = i.getStringExtra("Name");
        player_team = i.getStringExtra("Team");



        final EditText enterSubject = (EditText) getView().findViewById(R.id.subject);
        //Listening to button event
        enterSubject.addTextChangedListener(new TextWatcher() {

            String temp = "";

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                TextView text_count_subject = (TextView) getView().findViewById(R.id.text_count_subject);

                text_count_subject.setText(String.valueOf(50 - enterSubject.getText().length()) + " ");

                if(enterSubject.getText().length()>40){
                    if(enterSubject.getText().length()<50)  {
                        enterSubject.setBackgroundColor(Color.LTGRAY);    }
                    else{
                        enterSubject.setBackgroundColor(Color.DKGRAY);

                    }
                }
                else {
                    enterSubject.setBackgroundColor(Color.WHITE);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });

        final EditText enterContent = (EditText) getView().findViewById(R.id.content);
        //Listening to button event
        enterContent.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                TextView text_count_content = (TextView) getView().findViewById(R.id.text_count_content);

                text_count_content.setText(String.valueOf(200 - enterContent.getText().length()) + " ");

                if(enterContent.getText().length()>175){
                    if(enterContent.getText().length()<200)  {
                        enterContent.setBackgroundColor(Color.LTGRAY);    }
                    else{
                        enterContent.setBackgroundColor(Color.DKGRAY);

                    }
                }
                else {
                    enterContent.setBackgroundColor(Color.WHITE);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });


        Button newMessage = (Button) getView().findViewById(R.id.btnPost);
        //Listening to button event
        newMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                try {

                    TextView text_sub = (TextView) getView().findViewById(R.id.text_subject);

                    TextView text_content = (TextView) getView().findViewById(R.id.text_content);

                    TextView subject = (TextView) getView().findViewById(R.id.subject);

                    TextView content = (TextView) getView().findViewById(R.id.content);

                    // Add your data
                    List<String> nameValuePairs = new ArrayList<String>(5);
                    nameValuePairs.add(player_name);
                    nameValuePairs.add(player_team);
                    nameValuePairs.add(subject.getText().toString());
                    nameValuePairs.add(content.getText().toString());

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    String[] splitTime = currentDateTimeString.split("\\s+");
                    String[] splitDate = splitTime[1].split(",");

                    String date = splitTime[0] + "-" + splitDate[0];

                    System.out.println("Date:" + currentDateTimeString);

                    nameValuePairs.add(currentDateTimeString);

                    list = nameValuePairs;

                    System.out.println(nameValuePairs.get(0));

                    if(isConnected()) {
                        new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index.php/messages");
                    } else {
                        Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
                    }


                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    fm.beginTransaction()
                            .replace(R.id.container, new Messaging_fragment(), "Messaging")
                            .commit();


                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public static String POST(String url, List<String> list_data){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", list_data.get(0));
            jsonObject.accumulate("team", list_data.get(1));
            jsonObject.accumulate("subject", list_data.get(2));
            jsonObject.accumulate("content", list_data.get(3));
            jsonObject.accumulate("date", list_data.get(4));

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            System.out.println("JSON String: " + json);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0],list);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //nothing...
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
