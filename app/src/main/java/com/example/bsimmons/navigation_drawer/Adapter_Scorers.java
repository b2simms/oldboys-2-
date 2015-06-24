package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 12/06/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bsimmons on 09/06/2015.
 */

public class Adapter_Scorers extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<Info_Score> info;

    public Adapter_Scorers(Context context, ArrayList<Info_Score> info, String[] values) {
        super(context, R.layout.adapter_scorers, values);
        this.context = context;
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_scorers, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name_text);
        ImageView team_image = (ImageView) rowView.findViewById(R.id.team_image);
        TextView team = (TextView) rowView.findViewById(R.id.team_text);
        TextView goals = (TextView) rowView.findViewById(R.id.goals_text);


        name.setText(" " + info.get(position).getFirst_name() + " " +
                            info.get(position).getLast_name());
        team.setText(" " + info.get(position).getTeam());
        team_image = setTeamIcon(info.get(position).getTeam(), team_image);
        goals.setTextSize(30);
        goals.setText(info.get(position).getGoals() + " ");

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
