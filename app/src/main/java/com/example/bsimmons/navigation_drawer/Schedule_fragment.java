package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 11/06/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class Schedule_fragment extends Fragment {

    public static final String HTTP_BSIMMS2_BYETHOST5_COM_INDEX_PHP_SCHEDULE = "http://bsimms2.byethost5.com/index.php/schedule";
    private ArrayList<Game> games;
    private ListView listView;
    private String team_selected;
    private static boolean edit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
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

        edit = false;

        Intent i = getActivity().getIntent();
        team_selected = i.getStringExtra("Team");

        // Get ListView object from xml
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = listView.getItemAtPosition(position);

                if (edit) {
                    try {
                        TextView temp = (TextView) getActivity().findViewById(R.id.action_example);
                        temp.setBackgroundColor(Color.BLACK);

                        Fragment fragment_edit = new Edit_Schedule_fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("game_id", games.get(position).getGame_id());
                        bundle.putString("team1", games.get(position).getTeam1());
                        bundle.putString("team2", games.get(position).getTeam2());
                        bundle.putString("day", games.get(position).getDay());
                        bundle.putString("time", games.get(position).getTime());
                        bundle.putString("location", games.get(position).getLocation());
                        bundle.putString("date", games.get(position).getDate());
                        bundle.putString("team1_score", games.get(position).getTeam1_score());
                        bundle.putString("team2_score", games.get(position).getTeam2_score());
                        bundle.putString("updated", games.get(position).getUpdated());

                        fragment_edit.setArguments(bundle);

                        FragmentManager fm = getActivity().getSupportFragmentManager();

                        fm.beginTransaction()
                                .replace(R.id.container, fragment_edit)
                                .commit();

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });


        System.out.println(games);

        if(isConnected()) {
            new SpinnerAsyncTask().execute("http://bsimms2.byethost5.com/index.php/team");
        } else {
            Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
        }

    }

    public static String GET(String url) {
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
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


    private class SpinnerAsyncTask extends AsyncTask<String, Void, String> {

        ///SPINNER START
        ArrayList<String> teamlist = new ArrayList<String>();

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            try {


                JSONObject json = new JSONObject(result);


                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                teamlist.add("ALL");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    teamlist.add(json.optString("team1"));
                }

                // Locate the spinner in activity_main.xml
                Spinner mySpinner = (Spinner) getView().findViewById(R.id.my_spinner);

                // Spinner adapter
                mySpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item, teamlist));

                for (int i = 0; i < teamlist.size(); i++) {
                    if (team_selected.equals(teamlist.get(i))) {
                        String[] team_arr = team_selected.split("\\s+");
                        if (team_arr.length > 1) {
                            team_selected = team_arr[0] + "%20" + team_arr[1];

                        }
                        mySpinner.setSelection(i);

                    }
                }


                // Spinner on item click listener
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {


                        if(position == 0){
                            new HttpAsyncTask().execute(HTTP_BSIMMS2_BYETHOST5_COM_INDEX_PHP_SCHEDULE);
                        }
                        else {
                            String teamSelected = teamlist.get(position);

                            String[] splitStr = teamSelected.split("\\s+");
                            if (splitStr.length > 1) {
                                teamSelected = splitStr[0] + "%20" + splitStr[1];

                            }
                            if (isConnected()) {
                                new HttpAsyncTask().execute("http://bsimms2.byethost5.com/index.php/schedule/" + teamSelected);
                            } else {
                                Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                ///SPINNER END

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void setEdit(boolean edit){
        this.edit = edit;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject json = new JSONObject(result);

                ///SPINNER START

                games = new ArrayList<Game>();

                //get json array
                JSONArray jsonarray = json.getJSONArray("games");

                for (int i = 0; i < jsonarray.length(); i++) {
                    json = jsonarray.getJSONObject(i);

                    Game game = new Game();

                    game.setGame_id(json.optString("game_id"));
                    game.setTeam1(json.optString("team1"));
                    game.setTeam2(json.optString("team2"));
                    game.setDay(json.optString("day"));
                    game.setTime(json.optString("time"));
                    game.setDate(json.optString("date"));
                    game.setLocation(json.optString("location"));
                    game.setTeam1_score(json.optString("team1_score"));
                    game.setTeam2_score(json.optString("team2_score"));
                    game.setUpdated(json.optString("updated"));

                    games.add(game);

                }

                Spinner mySpinner = (Spinner) getView().findViewById(R.id.my_spinner);
                int pos = mySpinner.getSelectedItemPosition();




                // Defined Array values to show in ListView
                String[] values = new String[games.size()/2];

                for (int i = 0; i < (games.size()/2) - 1; i++) {
                    values[i] = games.get(i).getGame_id();
                }


                //Find current time
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                String[] splitTime = currentDateTimeString.split("\\s+");
                String[] splitDate = splitTime[1].split(",");

                int listViewSelection = 0;

                listViewSelection = setCurrentGame(splitTime[0], splitDate[0], listViewSelection);
                String currDate = splitTime[0] + "-" + splitDate[0];



                ScheduleRow_Adapter adapter = new ScheduleRow_Adapter(getActivity(), games, values, currDate);


                // Assign adapter to ListView
                listView.setAdapter(adapter);

                listView.setSelection(listViewSelection);
                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();



                ///SPINNER END

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private int setCurrentGame(String month, String string, int listViewSelection) {
        for (int i = 0; i < games.size(); i++) {

            String[] splitStr = games.get(i).getDate().split("-");

            if (setMonthInt(splitStr[0]) >= setMonthInt(month)) {
                if ((Integer) Integer.parseInt(splitStr[1]) >= (Integer) Integer.parseInt(string)) {
                    listViewSelection = i;
                    break;
                }
            }
        }
        return listViewSelection;
    }

    private int setMonthInt(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return -1;
        }
    }

}
