package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 11/06/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_Messaging extends Fragment {

    private ArrayList<Info_Message> messages;
    private ListView listView ;
    private List<String> currentDateUpdate;
    private int selected_Item;
    private boolean already_selected = false;
    private  String name;
    private final String GUEST_NAME = "guest";
    private final String FUTURE_DATE = "Jun 23, 2999 11:04:17 AM";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_messaging,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    @Override
    public void onStart() {
        super.onStart();


        Button newMessage = (Button) getView().findViewById(R.id.new_message_button);
        //Listening to button event
        newMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                try {
                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    fm.beginTransaction()
                            .replace(R.id.container, new Fragment_New_Message())
                            .commit();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });



        //sets size of array
        currentDateUpdate = new ArrayList<String>(2);
        //First place in list
        Intent i = getActivity().getIntent();
        if(i.getStringExtra("Name") == null){
            name = GUEST_NAME;
            currentDateUpdate.add("0");
            currentDateUpdate.add("Jun 23, 2999 11:04:17 AM");

        }else{
            name = i.getStringExtra("Name");
            currentDateUpdate.add(i.getStringExtra("Player_id"));
            currentDateUpdate.add(i.getStringExtra("Date"));
        }
        //Second place in list; reset each time MessageViewTimeUpdateAsyncTask is called


        if(isConnected()) {
            new UpdateDateAsyncTask().execute("http://bsimms2.byethost5.com/index.php/login/" + name);


        } else {
            Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStop(){
        super.onStop();


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
            jsonObject.accumulate("player_id", list_data.get(0));
            jsonObject.accumulate("date", list_data.get(1));
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            //System.out.println("JSON String: " + json);

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
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class MessageViewTimeUpdateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0],currentDateUpdate);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {


            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class UpdateDateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            try {


                JSONObject json = new JSONObject(result);

                messages = new ArrayList<Info_Message>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");
                System.out.println(jsonarray);
                json = jsonarray.getJSONObject(0);
                if(name.equalsIgnoreCase(GUEST_NAME)){
                    currentDateUpdate.set(1,FUTURE_DATE);
                }else {
                    currentDateUpdate.set(1, json.optString("message_last_view_date"));
                }

                new MessagingAsyncTask().execute("http://bsimms2.byethost5.com/index.php/messages");


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class MessagingAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity(),R.style.MyTheme);
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject json = new JSONObject(result);

                messages = new ArrayList<Info_Message>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    Info_Message info = new Info_Message();

                    info.setName(json.optString("name"));
                    info.setSub(json.optString("subject"));
                    info.setTeam(json.optString("team"));
                    info.setContent(json.optString("content"));
                    info.setDate(json.optString("date"));


                    messages.add(info);

                }

                // Get ListView object from xml
                listView = (ListView) getView().findViewById(R.id.MessageList);

                // Defined Array values to show in ListView
                String[] values = new String[messages.size()];

                for(int i=0;i<messages.size()-1;i++){
                    values[i] = messages.get(i).getTeam();
                }



                // Define a new Adapter
                Adapter_Messaging adapter = new Adapter_Messaging(getActivity(),messages, values, currentDateUpdate.get(1));

                // Assign adapter to ListView
                listView.setAdapter(adapter);

                //Second place in list
                String current = DateFormat.getDateTimeInstance().format(new Date());
                //System.out.println("Current date updated?" + current);
                currentDateUpdate.set(1, current);

                new MessageViewTimeUpdateAsyncTask().execute("http://bsimms2.byethost5.com/index.php/player/updateMessageViewTime");

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
