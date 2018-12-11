package com.example.battleships.View;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.battleships.Logic.ScoreTable;
import com.example.battleships.Logic.TableRow;
import com.example.battleships.Logic.Utility;
import com.example.battleships.R;

/**
 * Created by User on 02/01/2018.
 */

public class RowAdapter extends BaseAdapter {

    private Context mContext;
    private ScoreTable mTable;

    public RowAdapter(Context context, ScoreTable table) {
        mTable = table;
        mContext = context;
    }

    @Override
    public int getCount() {
        return Utility.TABLENUMOFROWS;
    }

    @Override
    public Object getItem(int position) {
        return mTable.getRow(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowView rowView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            rowView = new RowView(mContext);
            rowView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        else
            rowView = (RowView)convertView;

        TableRow row = mTable.getRow(position);
        if(row.isChosen())
            rowView.setBackgroundColor(Color.BLUE);
        else
            rowView.setBackgroundColor(Color.WHITE);

        rowView.setTexts(row.getName(),(row.getScore()==-1)? "": row.getScore()+"",row.getLocation());


        return rowView;    }


    public ScoreTable getTable() {
        return mTable;
    }
}
