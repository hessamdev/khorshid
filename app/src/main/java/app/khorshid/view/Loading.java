package app.khorshid.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import app.khorshid.R;
import app.khorshid.misc.Utility;

public class Loading extends View
{
    private int ViewWidth;
    private int ViewSpeed;
    private float SpeedArc;

    private float Arc;
    private Paint PaintMain;
    private RectF RectMain;

    private int TopDegree = 10;
    private int BottomDegree = 190;
    private boolean IsRunning = false;
    private boolean ChangeBigger = true;

    public Loading(Context context)
    {
        super(context);
        SetUp(context, null);
    }

    public Loading(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SetUp(context, attrs);
    }

    public Loading(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        SetUp(context, attrs);
    }

    @Override
    protected void onSizeChanged(int Width, int Height, int OldWidth, int OldHeight)
    {
        super.onSizeChanged(Width, Height, OldWidth, OldHeight);

        Arc = 10;

        RectMain = new RectF(2 * ViewWidth, 2 * ViewWidth, Width - 2 * ViewWidth, Height - 2 * ViewWidth);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (!IsRunning)
            return;

        canvas.drawArc(RectMain, TopDegree, Arc, false, PaintMain);
        canvas.drawArc(RectMain, BottomDegree, Arc, false, PaintMain);

        TopDegree += ViewSpeed;
        BottomDegree += ViewSpeed;

        if (TopDegree > 360)
            TopDegree = TopDegree - 360;

        if (BottomDegree > 360)
            BottomDegree = BottomDegree - 360;

        if (ChangeBigger)
        {
            if (Arc < 160)
            {
                Arc += SpeedArc;
                invalidate();
            }
        }
        else
        {
            if (Arc > ViewSpeed)
            {
                Arc -= 2 * SpeedArc;
                invalidate();
            }
        }

        if (Arc >= 160 || Arc <= 10)
        {
            ChangeBigger = !ChangeBigger;
            invalidate();
        }
    }

    private void SetUp(Context context, AttributeSet attrs)
    {
        ViewSpeed = 10;
        ViewWidth = Utility.ToDP(2);
        int ViewColor = Color.BLACK;

        if (attrs != null)
        {
            TypedArray Typed = context.obtainStyledAttributes(attrs, R.styleable.Loading);

            ViewColor = Typed.getColor(R.styleable.Loading_color, ViewColor);
            ViewWidth = Typed.getDimensionPixelSize(R.styleable.Loading_width, ViewWidth);
            ViewSpeed = Typed.getInt(R.styleable.Loading_speed, ViewSpeed);

            Typed.recycle();
        }

        SpeedArc = ViewSpeed / 4.0f;

        PaintMain = new Paint();
        PaintMain.setColor(ViewColor);
        PaintMain.setAntiAlias(true);
        PaintMain.setStyle(Paint.Style.STROKE);
        PaintMain.setStrokeWidth(ViewWidth);
        PaintMain.setStrokeCap(Paint.Cap.ROUND);

        setVisibility(GONE);
    }

    public void Start()
    {
        setVisibility(VISIBLE);

        ObjectAnimator ScaleX = ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1);
        ScaleX.setInterpolator(new LinearInterpolator());
        ScaleX.setDuration(300);

        ObjectAnimator ScaleY = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1);
        ScaleY.setInterpolator(new LinearInterpolator());
        ScaleY.setDuration(300);

        AnimatorSet Anim = new AnimatorSet();
        Anim.playTogether(ScaleX, ScaleY);
        Anim.start();

        IsRunning = true;
        invalidate();
    }

    public void Stop()
    {
        setVisibility(GONE);

        ObjectAnimator ScaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
        ScaleX.setInterpolator(new LinearInterpolator());
        ScaleX.setDuration(300);

        ObjectAnimator ScaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
        ScaleY.setInterpolator(new LinearInterpolator());
        ScaleY.setDuration(300);

        AnimatorSet Anim = new AnimatorSet();
        Anim.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                IsRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        Anim.playTogether(ScaleX, ScaleY);
        Anim.start();

        invalidate();
    }
}
