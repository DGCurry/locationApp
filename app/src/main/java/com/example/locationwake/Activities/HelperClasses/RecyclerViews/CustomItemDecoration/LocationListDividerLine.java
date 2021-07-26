package com.example.locationwake.Activities.HelperClasses.RecyclerViews.CustomItemDecoration;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;

public class LocationListDividerLine extends RecyclerView.ItemDecoration {
    Drawable divider;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};


    /**
     * Default divider will be used
     */
    public LocationListDividerLine(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public LocationListDividerLine(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }



    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int childAdapterPosition = parent.getChildAdapterPosition(child);
            if (parent.getAdapter().getItemViewType(childAdapterPosition) == AttributeInterface.LOCATION_TYPE) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            } else if (parent.getAdapter().getItemViewType(childAdapterPosition) == AttributeInterface.NAME_TYPE) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getTop() - params.topMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }


}
