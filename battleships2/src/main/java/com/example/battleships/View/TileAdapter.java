package com.example.battleships.View;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.example.battleships.Logic.Board;
import com.example.battleships.Logic.Utility;
import com.example.battleships.Logic.Tile;
import com.example.battleships.R;

/**
 * Created by User on 14/12/2017.
 */

public class TileAdapter extends BaseAdapter {
    private Context mContext;
    private Board mBoard;
    private AnimationDrawable animation;


    private Integer[] mThumbIds = { // array that hold all the icons
            R.drawable.ocean,
            R.drawable.hit,
            R.drawable.miss,
            R.drawable.shipupdown,
            R.drawable.shipleftright,
            R.drawable.explotionhit,
            R.drawable.explotioneast,
            R.drawable.explotionnorth,
            R.drawable.wave
    };

    public TileAdapter(Context context, Board board) {
        mBoard = board;
        mContext = context;
    }

    @Override
    public int getCount() {
        return Utility.BOARDSIZE*Utility.BOARDSIZE;
    }

    @Override
    public Object getItem(int position) {
        return mBoard.getTile(position/10,position%10);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            tileView = new TileView(mContext);
            tileView.setLayoutParams(new GridView.LayoutParams(110, 110));
        }
        else
            tileView = (TileView) convertView;

        //decides what to put on cell
        Tile tile = mBoard.getTile(position/10,position%10);
        Utility.TileState temp = tile.getState();
        if(!tile.isTransperent()) {
            if (temp == Utility.TileState.NONE) {
                tileView.image.setImageResource(mThumbIds[0]);
                tileView.image.setImageAlpha(255);
            } else if (temp == Utility.TileState.HIT) {
                if(tile.isTouched()){
                    tile.setTouched(false);
                    tile.setFirst(false);
                    tileView.image.setImageAlpha(0);
                    tileView.setBackgroundResource(mThumbIds[5]);
                    animation = (AnimationDrawable) tileView.getBackground();
                    animation.start();
                }else {
                    tileView.image.setImageResource(mThumbIds[1]);
                    tileView.image.setImageAlpha(255);
                }
            } else {
                if (tile.isTouched()) {
                    tile.setTouched(false);
                    tile.setFirst(false);
                    tileView.image.setImageAlpha(0);
                    tileView.setBackgroundResource(mThumbIds[8]);
                    animation = (AnimationDrawable) tileView.getBackground();
                    animation.start();
                } else {
                    tileView.image.setImageResource(mThumbIds[2]);
                    tileView.image.setImageAlpha(255);
                }
            }
        }
        else{
            if(tile.isHasShip()){
                if((tile.getDirection() == Utility.Direction.EAST)||(tile.getDirection() == Utility.Direction.WEST)) {
                    if(tile.isFirst()) {
                        tile.setFirst(false);
                        tileView.image.setImageAlpha(0);
                        tileView.setBackgroundResource(mThumbIds[6]);
                        animation = (AnimationDrawable) tileView.getBackground();
                        animation.start();
                    }else {
                        tileView.image.setImageResource(mThumbIds[3]);
                        tileView.image.setImageAlpha(255);
                    }
                }
                else{
                    if(tile.isFirst()) {
                        tile.setFirst(false);
                        tileView.image.setImageAlpha(0);
                        tileView.setBackgroundResource(mThumbIds[7]);
                        animation = (AnimationDrawable) tileView.getBackground();
                        animation.start();
                    }else {
                        tileView.image.setImageResource(mThumbIds[4]);
                        tileView.image.setImageAlpha(255);
                    }
                }
            }else{
                tileView.image.setImageAlpha(255);
                tileView.image.setImageResource(mThumbIds[2]);
            }
        }

        return tileView;    }



}
