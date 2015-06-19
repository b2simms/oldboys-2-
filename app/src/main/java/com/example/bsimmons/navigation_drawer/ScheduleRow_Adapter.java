package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 09/06/2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleRow_Adapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<Game> games;
    private String currDate;

    public ScheduleRow_Adapter(Context context, ArrayList<Game> games, String[] values, String currDate) {
        super(context, R.layout.adapter_squareandtworow,values);
        this.context = context;
        this.games = games;
        this.currDate = currDate;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        try {

            //Find current time
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            String[] splitCurrTime = currentDateTimeString.split("\\s+");
            String[] splitCurrDate = splitCurrTime[1].split(",");
            String[] splitGameDate = games.get(position).getDate().split("-");

            //.out.println("Month: " + setMonthInt(splitGameDate[0]) + ">=" + setMonthInt(splitCurrTime[0]));

            //System.out.println("Day:" + (Integer) Integer.parseInt(splitGameDate[1])+  ">=" + (Integer) Integer.parseInt(splitCurrDate[0]));

            //GAME MONTH FUTURE
            if (setMonthInt(splitGameDate[0]) > setMonthInt(splitCurrTime[0])) {
                //System.out.println("schedule view");
                View scheduleView = generateScheduleView(position, parent);
                return scheduleView;
            }else {
                //GAME MONTH PAST
                if(setMonthInt(splitGameDate[0]) < setMonthInt(splitCurrTime[0])){
                        //System.out.println("SCORE BOARD");
                        View scoreBoard = generateScoreBoard(position, parent);
                        return scoreBoard;
                    }
                else{
                    //GAME DAY FUTURE OR TODAY
                    if (((Integer) Integer.parseInt(splitGameDate[1])) >= ((Integer) Integer.parseInt(splitCurrDate[0])) ) {
                        //System.out.println("schedule view");
                        View scheduleView = generateScheduleView(position, parent);
                        return scheduleView;
                    }else{
                        //System.out.println("SCORE BOARD");
                        View scoreBoard = generateScoreBoard(position, parent);
                        return scoreBoard;
                    }
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    private View generateScoreBoard(int position,  ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View scoreBoard = inflater.inflate(R.layout.adapter_schedulescoreboard, parent, false);
        TextView location = (TextView) scoreBoard.findViewById(R.id.location);
        ImageView image1 = (ImageView) scoreBoard.findViewById(R.id.team_image);
        TextView date = (TextView) scoreBoard.findViewById(R.id.date);
        ImageView image2 = (ImageView) scoreBoard.findViewById(R.id.team2_image);
        TextView score_board = (TextView) scoreBoard.findViewById(R.id.score_board);

        location.setText(" " + games.get(position).getLocation() );
        image1 = setTeamIcon(games.get(position).getTeam1(), image1);
        image2 = setTeamIcon(games.get(position).getTeam2(),image2);
        date.setText(games.get(position).getDay() + ", " + games.get(position).getDate() + " ");

        if(games.get(position).getUpdated().equals("true")) {
            score_board.setText(games.get(position).getTeam1_score() + " - " + games.get(position).getTeam2_score());
        }else{
            score_board.setText(" - ");
        }

        return scoreBoard;
    }

    private View generateScheduleView(int position, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View scheduleView = inflater.inflate(R.layout.adapter_squareandtworow, parent, false);
        //TextView team1 = (TextView) scheduleView.findViewById(R.id.name_text);
        ImageView image1 = (ImageView) scheduleView.findViewById(R.id.team_image);
        TextView team2 = (TextView) scheduleView.findViewById(R.id.date);
        ImageView image2 = (ImageView) scheduleView.findViewById(R.id.team2_image);
        TextView time = (TextView) scheduleView.findViewById(R.id.team_text);
        //TextView date = (TextView) scheduleView.findViewById(R.id.text_date);

        //team1.setText(" " + games.get(position).getTeam1());
        team2.setText(games.get(position).getDay() + ", " + games.get(position).getDate() + " ");
        image1 = setTeamIcon(games.get(position).getTeam1(), image1);
        image2 = setTeamIcon(games.get(position).getTeam2(),image2);
        time.setText(" " + games.get(position).getLocation());
        //date.setText(games.get(position).getDate() + " ");

        if(team2.getText().toString().equals(currDate + " ")){
            scheduleView.setBackgroundColor(Color.CYAN);
        }

        return scheduleView;
    }

    private ImageView setTeamIcon(String team, ImageView temp) {

        switch (team) {
            case "Fredericton Kia":
                temp.setImageResource(R.drawable.frederictonkia);
                break;
            case "Gunners":
                temp.setImageResource(R.drawable.gunners);
                break;
            case "Sporting":
                temp.setImageResource(R.drawable.sporting);
                break;
            case "Growlers United":
                temp.setImageResource(R.drawable.growlers);
                break;
            case "Picaroons":
                temp.setImageResource(R.drawable.picaroons);
                break;
            case "Rogue Galleons":
                temp.setImageResource(R.drawable.galleons);
                break;
            default:
                temp.setImageResource(R.drawable.soccerball);
                break;
        }
        return temp;
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