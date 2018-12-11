package com.example.battleships.View;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.battleships.Logic.Utility;

/**
 * Created by User on 30/12/2017.
 */

public class RowView extends LinearLayout {
    //ImageView image;
    TextView name;
    TextView score;
    TextView location;

    public RowView(Context context) {
        super(context);

        this.setOrientation(HORIZONTAL);
        //image = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //image.setLayoutParams(layoutParams);
        //image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //addView(image);
        name = new TextView(context);
        score = new TextView(context);
        location = new TextView(context);
        name.setTypeface(Typeface.MONOSPACE);
        score.setTypeface(Typeface.MONOSPACE);
        location.setTypeface(Typeface.MONOSPACE);
        name.setTextSize(22);
        score.setTextSize(22);
        location.setTextSize(22);
        addView(name);
        addView(score);
        addView(location);
    }

    public void setTexts(String nameSTR,String scoreSTR,String locationSTR){
        for(int i = nameSTR.length();i<Utility.SCORETEXTLENGTH;i++)
            nameSTR+=' ';
        nameSTR = ' '+nameSTR;
        nameSTR=nameSTR.substring(0,Utility.SCORETEXTLENGTH-1);
        int length = scoreSTR.length()/2;
        for(int i = length;i<Utility.SCORETEXTLENGTH/3;i++)
            scoreSTR=' '+scoreSTR;
        length += scoreSTR.length()%2;
        for(int i = length;i<6*Utility.SCORETEXTLENGTH/10;i++)
            scoreSTR+=' ';
        scoreSTR = scoreSTR.substring(0,6*Utility.SCORETEXTLENGTH/10);
        for(int i = locationSTR.length();i<Utility.SCORETEXTLENGTH;i++)
            locationSTR+=' ';
        locationSTR=locationSTR.substring(0,Utility.SCORETEXTLENGTH-1);
        name.setText((CharSequence)(nameSTR+' '));
        score.setText((CharSequence)(scoreSTR+' '));
        location.setText((CharSequence)(locationSTR+' '));
    }









   /* public RowView(Context context, String score, String name, String location) {
        super(context);
        Button btn1 = new Button(context);
        btn1.setText(name);
        Button btn2 = new Button(context);
        btn2.setText(score);
        Button btn3 = new Button(context);
        btn3.setText(location);
//        setWeightSum(3f);
//        LinearLayout.LayoutParams prm = (LinearLayout.LayoutParams)getLayoutParams();
//        prm.weight = 1f;
//        btn1.setLayoutParams(prm);
//        btn2.setLayoutParams(prm);
//        btn3.setLayoutParams(prm);
        addView(btn1);
        addView(btn2);
        addView(btn3);


        /* btn.setText("button " + id_);
    btn.setBackgroundColor(Color.rgb(70, 80, 90));
    linear.addView(btn, params); *//*


    }*/
}
