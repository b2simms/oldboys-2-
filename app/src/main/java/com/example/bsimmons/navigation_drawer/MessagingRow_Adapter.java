package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 12/06/2015.
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bsimmons on 09/06/2015.
 */

public class MessagingRow_Adapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<MessageInfo> info;
    private String date_last_viewed;

    public MessagingRow_Adapter(Context context, ArrayList<MessageInfo> info, String[] values, String date_last_viewed) {
        super(context, R.layout.adapter_messagingrow, values);
        this.context = context;
        this.info = info;
        this.date_last_viewed = date_last_viewed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_messagingrow, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.From);
        ImageView team_image = (ImageView) rowView.findViewById(R.id.icon);
        TextView sub = (TextView) rowView.findViewById(R.id.subject);
        TextView content = (TextView) rowView.findViewById(R.id.content);
        TextView date = (TextView) rowView.findViewById(R.id.date);


        name.setText(" " + info.get(position).getName());
        team_image = setTeamIcon(info.get(position).getTeam(), team_image);
        sub.setText("Subject: " + info.get(position).getSub());
        content.setText("\n  " + info.get(position).getContent() + "\n");

        String[] splitTime = info.get(position).getDate().split("\\s+");
        String[] splitDate = splitTime[1].split(",");

        String displayDate = splitTime[0] + "-" + splitDate[0];

        date.setText(displayDate);


        try {
            //parse String to Date
            DateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
            Date parseToDateMessageCreated = format.parse(info.get(position).getDate());
            Date parseToLastViewed = format.parse(date_last_viewed);

            System.out.println("created:" + parseToDateMessageCreated + "=" + parseToLastViewed);

            //change background if user has not read message yet
            if(parseToDateMessageCreated.after(parseToLastViewed)){

                System.out.println("****************"+ parseToDateMessageCreated + " is GREATER THAN " + parseToLastViewed);

                rowView.setBackgroundColor(Color.CYAN);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return rowView;
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
}