package com.example.battleships.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.battleships.Database.DBHelper;
import com.example.battleships.Logic.ScoreTable;
import com.example.battleships.Logic.TableRow;
import com.example.battleships.Logic.Utility;
import com.example.battleships.R;
import com.example.battleships.StartActivity;
import com.example.battleships.View.RowAdapter;

/**
 * Created by User on 15/01/2018.
 */

public class TableFragment extends Fragment {

    private ScoreTable scoreTableEasy;
    private ScoreTable scoreTableMedium;
    private ScoreTable scoreTableHard;
    private GridView gridTable;
    private RowAdapter rowAdapterEasy;
    private RowAdapter rowAdapterMedium;
    private RowAdapter rowAdapterHard;
    private DBHelper dbHelper;
    private StartActivity startActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_table, container, false);
        gridTable = (GridView) view.findViewById(R.id.ScoreTableInFragment);
        start();
        return view;
    }


    public void setStartActivity(StartActivity startActivity){
        this.startActivity = startActivity;
    }

    private void start(){
        dbHelper = new DBHelper(startActivity.getApplicationContext(), Utility.DBNAME, null,Utility.DBVER);

        scoreTableEasy = new ScoreTable(dbHelper.getAllTableRows(Utility.Difficulty.EASY.toString()),Utility.Difficulty.EASY.toString());
        rowAdapterEasy = new RowAdapter(startActivity.getApplicationContext(), scoreTableEasy);

        scoreTableMedium = new ScoreTable(dbHelper.getAllTableRows(Utility.Difficulty.MEDIUM.toString()),Utility.Difficulty.MEDIUM.toString());
        rowAdapterMedium = new RowAdapter(startActivity.getApplicationContext(), scoreTableMedium);

        scoreTableHard = new ScoreTable(dbHelper.getAllTableRows(Utility.Difficulty.HARD.toString()), Utility.Difficulty.HARD.toString());
        rowAdapterHard = new RowAdapter(startActivity.getApplicationContext(), scoreTableHard);

        gridTable.setAdapter(rowAdapterEasy);
        gridTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                ((RowAdapter)gridTable.getAdapter()).getTable().chooseRow(position);
                updateViews();
            }
        });

        dbHelper.deleteData(Utility.DBNAME);
        dbHelper = new DBHelper(startActivity.getApplicationContext(), Utility.DBNAME, null,Utility.DBVER);
        for (TableRow row:scoreTableEasy.getRows()) {
            dbHelper.insertScore(row,row.getDifficulty());
        }
        for (TableRow row:scoreTableMedium.getRows()) {
            dbHelper.insertScore(row,row.getDifficulty());
        }
        for (TableRow row:scoreTableHard.getRows()) {
            dbHelper.insertScore(row,row.getDifficulty());
        }
    }

    public void updateViews() {
        startActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((RowAdapter) gridTable.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    public void setAdapter(int i){
        switch (i){
            case 1:
                gridTable.setAdapter(rowAdapterEasy);
                break;
            case 2:
                gridTable.setAdapter(rowAdapterMedium);
                break;
            case 3:
                gridTable.setAdapter(rowAdapterHard);
                break;

        }
    }

    public void chooseNone(){
        scoreTableEasy.chooseNone();
        scoreTableMedium.chooseNone();
        scoreTableHard.chooseNone();

    }

}
