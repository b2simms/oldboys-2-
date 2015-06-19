package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 11/06/2015.
 */

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.util.GregorianCalendar;

public class Standings_fragment extends Fragment {

    private ArrayList<TeamInfo> standings;
    private ListView listView ;
    private String team_selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_standings,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isConnected()) {
            new StandingsAsyncTask().execute("http://bsimms2.byethost5.com/index.php/standings");
        } else {
            Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
        }

    }

    public static String GET(String url){
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
            e.printStackTrace();
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


    private int setMonthInt(String month){
        switch (month) {
            case "Jan":  return 1;
            case "Feb": return 2;
            case "Mar": return 3;
            case "Apr": return 4;
            case "May": return 5;
            case "Jun": return 6;
            case "Jul": return 7;
            case "Aug": return 8;
            case "Sep": return 9;
            case "Oct": return 10;
            case "Nov": return 11;
            case "Dec": return 12;
            default: return -1;
        }
    }

    private class StandingsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            try {


                JSONObject json = new JSONObject(result);

                standings = new ArrayList<TeamInfo>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    TeamInfo info = new TeamInfo();


                    info.setTeam(json.optString("team"));
                    info.setPlayed(json.optString("played"));
                    info.setWin(json.optString("win"));
                    info.setTie(json.optString("tie"));
                    info.setLoss(json.optString("loss"));
                    info.setGoal_for(json.optString("for"));
                    info.setAgainst(json.optString("against"));
                    info.setPoints(json.optString("points"));

                    standings.add(info);

                }

                // Get ListView object from xml
                listView = (ListView) getView().findViewById(R.id.listView);

                // Defined Array values to show in ListView
                String[] values = new String[standings.size()];

                for(int i=0;i<standings.size()-1;i++){
                    values[i] = standings.get(i).getTeam();
                }


                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Fourth - the Array of data

                StandingsRow_Adapter adapter = new StandingsRow_Adapter(getActivity(),standings,values);

                // Assign adapter to ListView
                listView.setAdapter(adapter);

                // Spinner on item click listener
                listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        System.out.println("onItemSelected Listview  ");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                ///SPINNER END

                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();

            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

}
