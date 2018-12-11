package com.example.battleships.View;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

/**
 * Created by User on 14/12/2017.
 */

public class TileView extends LinearLayout {

    ImageView image;

    public TileView(Context context) {
        super(context);

        this.setOrientation(VERTICAL);
        image = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(layoutParams);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(image);
    }
}
