package com.danilov.orange.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LayoutSuppressingImageView extends ImageView {

    /**
     * @param context The {@link Context} to use
     * @param attrs The attributes of the XML tag that is inflating the view
     */
    public LayoutSuppressingImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestLayout() {
        forceLayout();
    }

}
