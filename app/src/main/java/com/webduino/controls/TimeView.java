package com.webduino.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.webduino.elements.TimeRange;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 30/12/2016.
 */

public class TimeView extends TextView {
    private String titleText;
    private boolean color;

    private Rect rectangle;
    private Paint paint;
    private List<TimePoint> list = new ArrayList<>();
    private TimePoint start;// = new TimePoint(18.4,0);
    private TimePoint end;// = new TimePoint(22.3,100);

    private int radius = 30;
    private int timeTextSize = 15;
    private int temperatureTextSize = 25;
    int lineHeight = 10;
    int xStart;
    int xEnd;
    int width;

    public void removeAll() {
        if (list != null)
            list.clear();
    }

    public void add(List<TimeRange> timeRanges) {

        if (timeRanges == null || timeRanges.size() == 0)
            return;

        list.clear();

        int hours = 0;
        int minutes = 0;

        /*GregorianCalendar cal = new GregorianCalendar(1970, 0, 0, hours, minutes);
        end = new TimePoint(34.4, cal);
        list.add(end);

        for (TimeRange timerange : timeRanges) {

        }*/

        //list.add(timePoint);
    }

    public TimeView(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            init(context);
        }
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            init(context);
        }
    }

    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            init(context);
        }
    }


    private void init(Context context) {

        paint = new Paint();
        paint.setColor(Color.RED);

        GregorianCalendar cal = new GregorianCalendar(1970, 0, 0, 7, 30);
        start = new TimePoint(10, cal);
        list.add(start);

        cal = new GregorianCalendar(1970, 0, 0, 12, 10);
        end = new TimePoint(15, cal);
        list.add(end);

        cal = new GregorianCalendar(1970, 0, 0, 17, 0);
        end = new TimePoint(25, cal);
        list.add(end);

        cal = new GregorianCalendar(1970, 0, 0, 17, 30);
        end = new TimePoint(18, cal);
        list.add(end);

        cal = new GregorianCalendar(1970, 0, 0, 23, 59);
        end = new TimePoint(30, cal);
        list.add(end);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(widthMeasureSpec, 3 * radius); // Example size for test
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        xStart = radius;
        xEnd = canvas.getWidth() - radius;
        width = xEnd - xStart;

        RectF rect = new RectF(xStart, radius - lineHeight / 2, xEnd, radius + lineHeight / 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);

        drawTimePoints(canvas);

        drawTemperatureColor(canvas);
    }

    private void drawTimePoints(Canvas canvas) {

        int maxTime = 60 * 24;
        int x0 = xStart;

        for (TimePoint tp : list) {

            int x = xStart + (width) * tp.value / maxTime;

            String text = "" + tp.temperature;

            drawTemperature(canvas, x0, x, /*(x + x0) / 2*/ text, (int)tp.temperature);
            //drawTime(canvas, x, tp.timeStr);

            x0 = x;
        }


    }

    private void drawTemperatureColor(Canvas canvas) {

        int num = 50;
        double len = (xEnd - xStart) / num;
        int x = 0;
        int topmargin = 40;


        for (int i = 0; i < num; i++) {



            int x0 = xStart + (int) (len*(i));
            int x1 = xStart + (int) (len*(i+1));

            RectF rect = new RectF(x0, topmargin + radius - lineHeight / 2, x1, topmargin + radius + lineHeight / 2);
            paint.setStyle(Paint.Style.FILL);


            int color = getColor(i);


            paint.setColor(color);
            canvas.drawRect(rect, paint);


        }
    }

    private int getColor(double temperature) { // i= 0 - 50

        double max = 50;

        double tmin = 10;
        double tmax = 30;
        if (temperature < tmin) temperature = tmin;
        if (temperature > tmax) temperature = tmax;

        double i = (temperature - tmin) * max / (tmax - tmin);



        int r;
        int b;
        int g;
        double delta = 255.0 / max;
        //b = (int)(255 -(delta) * i);
        //r = (int)((delta) * i);

        if (i < (max / 2) ) {
            g = (int) (2 * delta * i);
            b = (int)(255 - 2 * delta * i);
            r = 0;

        } else {
            g = (int) (255 - (2 * delta) * (i - max / 2));
            b = 0;
            r = (int)((2 * delta) * (i - max / 2));

        }
        return Color.rgb(r,g,b);
    }


    private void drawTemperature(Canvas canvas, int x0, int x, String text, int temperature) {

        int y = radius;

        Paint paint = new Paint();

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(temperatureTextSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();

        int xc = (x0+x)/2;
        RectF bounds = new RectF(xc - radius, y - radius, xc + radius, y + radius);
        paint.setColor(Color.BLUE);
        canvas.drawOval(bounds, paint);
        canvas.drawText(text, bounds.centerX(), bounds.centerY() + textOffset, textPaint);

        bounds = new RectF(x0, y - lineHeight + lineHeight, x, y + lineHeight + lineHeight);
        paint.setColor(getColor(temperature));
        canvas.drawRect(bounds, paint);

    }

    private void drawTime(Canvas canvas, int x, String time) {

        int y = radius;

        Paint paint = new Paint();

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(timeTextSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();

        RectF bounds = new RectF(x - radius, y + radius, x + radius, y + 2 * radius);
        paint.setColor(Color.BLUE);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(timeTextSize);
        textPaint.setColor(Color.BLACK);
        canvas.drawText(time, bounds.centerX(), bounds.centerY() + textOffset, textPaint);

    }


    /*protected void drawCircle(Canvas canvas, int val, String text) {

        int width = canvas.getWidth();
        int x = width / 2;
        int y = 100;
        int radius = 20;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(x,y,radius, paint);

        Rect r = new Rect();
        paint.setTextSize(60);
        paint.getTextBounds(text, 0, text.length(), r);
        int yPos = y  + (Math.abs(r.height()))/2;    // or maybe -= instead of +=, depends on your coordinates
        paint.setColor(Color.BLACK);
        //paint.setTextSize(20);
        canvas.drawText("Some Text", x, yPos, paint);

    }*/


}