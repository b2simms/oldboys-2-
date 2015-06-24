package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 09/06/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Standings extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<Info_Team> info;

    public Adapter_Standings(Context context, ArrayList<Info_Team> info, String[] values) {
        super(context, R.layout.adapter_standings, values);
        this.context = context;
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_standings, parent, false);
        TextView team = (TextView) rowView.findViewById(R.id.name_text);
        ImageView team_image = (ImageView) rowView.findViewById(R.id.team_image);
        //TextView loss = (TextView) rowView.findViewById(R.id.loss_text);
        TextView win = (TextView) rowView.findViewById(R.id.team_text);
        TextView points = (TextView) rowView.findViewById(R.id.points_text);


        team.setText(" " + info.get(position).getTeam());
        win.setText(" Win: " + info.get(position).getWin()+ "      Tie: " + info.get(position).getTie() + "      Loss: " + info.get(position).getLoss());
        team_image = setTeamIcon(info.get(position).getTeam(), team_image);

        //loss.setText(" Loss: " + info.get(position).getLoss());
        points.setTextSize(30);
        points.setText(info.get(position).getPoints() + " ");


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