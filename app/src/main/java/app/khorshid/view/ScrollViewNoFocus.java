package app.disruptor.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ScrollViewNoFocus extends ScrollView
{
    public ScrollViewNoFocus(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ScrollViewNoFocus(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScrollViewNoFocus(Context context)
    {
        super(context);
    }

    @Override
    public ArrayList<View> getFocusables(int direction)
    {
        return new ArrayList<>();
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect)
    {
        return true;
    }
}
