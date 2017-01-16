package com.webduino.chart;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 26/06/2016.
 */
public class HistoryChart {

    Context mContext;
    private LineChart chart;
    //private LineChart mTrendChart;
    //private LineChart mTemperatureChart;

    private LineData windData;
    //private LineData trendData;
    //private LineData temperatureData;

    private long startTimeMilliseconds;
    private long endTimeMilliseconds;

    public HistoryChart(Context context, LineChart chart) {
        mContext = context;
        this.chart = chart;
        //mTrendChart = trendChart;
        //mTemperatureChart = temperatureChart;
    }

    public LineData getWindLineData() {
        return windData;
    }

    public void drawChart(List<HistoryData> historyDatas) {

        if (historyDatas == null )
            return;
        List<Entry> valsCompRemoteTemperature = new ArrayList<Entry>();
        List<Entry> valsCompTargetTemperature = new ArrayList<Entry>();
        List<Entry> valsCompReleStatus = new ArrayList<Entry>();
        //List<Entry> valsCompActiveProgram = new ArrayList<Entry>();
        //List<Entry> valsCompActiveTimeRange = new ArrayList<Entry>();

        Date lastTime = null;
        float x = 0f;
        startTimeMilliseconds = historyDatas.get(0).date.getTime();
        endTimeMilliseconds = historyDatas.get(historyDatas.size()-1).date.getTime();

        for (int i = 0; i < historyDatas.size(); i++) {

            HistoryData md = historyDatas.get(i);
            Date date = null;
            if (md.date == null) {
                int k = 0;
                k++;
                continue;
            }
            date = md.date;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());

            if (!md.date.equals(lastTime)) {

                //x = date.getTime();
                x = date.getTime() - startTimeMilliseconds;

                Float y;
                Entry entry;

                if (md.remoteTemperature != -1)
                    y = Float.valueOf(String.valueOf(md.remoteTemperature));
                else
                    y = 0.0F;
                entry = new Entry(x, y);
                valsCompRemoteTemperature.add(entry);

                if (md.targetTemperature != -1)
                    y = Float.valueOf(String.valueOf(md.targetTemperature));
                else
                    y = 0.0F;
                entry = new Entry(x, y);
                valsCompTargetTemperature.add(entry);

                if (md.releStatus)
                    y = 1F;
                else
                    y = 0F;
                entry = new Entry(x, y);
                valsCompReleStatus.add(entry);

                /*if (md.activeProgram != -1)
                    y = Float.valueOf(String.valueOf(md.activeProgram));
                else
                    y = 0.0F;
                entry = new Entry(x, y);
                valsCompActiveProgram.add(entry);

                if (md.activeTimeRange != -1)
                    y = Float.valueOf(String.valueOf(md.activeTimeRange));
                else
                    y = 0.0F;
                entry = new Entry(x, y);
                valsCompActiveTimeRange.add(entry);*/


            } else {
                int k = 0;
                k++;
                continue;
            }
        }

        LineDataSet setCompRemote = new LineDataSet(valsCompRemoteTemperature, "remote");
        setCompRemote.setAxisDependency(YAxis.AxisDependency.LEFT);
        setCompRemote.setColor(Color.RED);
        setCompRemote.setDrawCircles(true);
        setCompRemote.setCircleColor(Color.RED);

        LineDataSet setCompTarget = new LineDataSet(valsCompTargetTemperature, "target");
        setCompTarget.setAxisDependency(YAxis.AxisDependency.LEFT);
        setCompTarget.setColor(Color.BLUE);
        setCompTarget.setDrawCircles(true);
        setCompTarget.setCircleColor(Color.BLUE);

        LineDataSet setCompReleStatus = new LineDataSet(valsCompReleStatus, "rele");
        setCompReleStatus.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setCompReleStatus.setColor(Color.BLACK);
        setCompReleStatus.setDrawCircles(true);
        setCompReleStatus.setCircleColor(Color.BLACK);
        //setCompReleStatus.enableDashedLine(10f,10f,10f);
        
        // use the interface ILineDataSet
        List<ILineDataSet> windDataSets = new ArrayList<ILineDataSet>();

        windDataSets.add(setCompRemote);
        windDataSets.add(setCompTarget);
        windDataSets.add(setCompReleStatus);
        /*windDataSets.add(setCompProgram);
        windDataSets.add(setCompTimeRange);*/

        setCompReleStatus.setValueFormatter(new DirectionValueFormatter());



        chart.setDescription("");
        //chart.setDescriptionPosition(0,);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setGranularity(60000L * 5f); // one minute in millis

        AxisValueFormatter avf = new AxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Float f = value;
                long milliseconds = f.longValue();
                return mFormat.format(new Date(startTimeMilliseconds + milliseconds));

                //return mFormat.format(new Date(milliseconds));
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        xAxis.setValueFormatter(avf);

        YAxis yLeftAxis = chart.getAxisLeft();
        yLeftAxis.setDrawAxisLine(true);
        yLeftAxis.setDrawGridLines(true);
        yLeftAxis.setAxisMinValue(0f); // start at zero
        yLeftAxis.setAxisMaxValue(35f); // the axis maximum is 100
        yLeftAxis.setGranularity(5f);
        yLeftAxis.setLabelCount(10);
        //yLeftAxis.setGranularity(22.5f); // one minute in millis
        //yLeftAxis.setInverted(true);


        YAxis yRightAxis = chart.getAxisRight();
        yRightAxis.setDrawAxisLine(true);
        yRightAxis.setDrawGridLines(true);
        yRightAxis.setAxisMinValue(0f); // start at zero
        yRightAxis.setAxisMaxValue(1.3f); // the axis maximum is 100
        yRightAxis.setLabelCount(2);
        yRightAxis.setGranularity(1);
        //yRightAxis.setInverted(true);
        yRightAxis.setGridColor(Color.GREEN);
        yRightAxis.setValueFormatter(new DirectionAxisValueFormatter());

        windData = new LineData(windDataSets);
        chart.setData(windData);

        Legend legend = chart.getLegend();
        legend.setFormSize(10f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(20f); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        legend.setEnabled(true);

        Calendar cal = Calendar.getInstance();
        cal.setTime(historyDatas.get(historyDatas.size()-1).date);
        cal.add(Calendar.HOUR_OF_DAY, -2); //minus number would decrement the hours
        Date leftDate = cal.getTime();
        long leftDateTimeMilliseconds = leftDate.getTime();

        //chart.zoom(1f,1f,1f,1f); // ripristina lo zoom iniziale dal draw precedente
        //chart.zoom(4f,1f,3*(endTimeMilliseconds-startTimeMilliseconds)/4,35/4);
        //chart.moveViewToX(leftDateTimeMilliseconds);
        //chart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
        //chart.moveViewToX(10); // set

        // muovi la finestra a leftdatetime
        //mTemperatureChart.zoom(4f,1f,3*(endTimeMilliseconds-startTimeMilliseconds)/4,35/4);
        //mTemperatureChart.zoom(10f,1f,leftDateTimeMilliseconds,35/2);
        //mTemperatureChart.moveViewToX(leftDateTimeMilliseconds);
        //mTemperatureChart.moveViewToX (3*(endTimeMilliseconds-startTimeMilliseconds)/4);

        //mTrendChart.zoom(2f,1f,3*(endTimeMilliseconds-startTimeMilliseconds)/4,35/4);
        //mTrendChart.moveViewToX (3*(endTimeMilliseconds-startTimeMilliseconds)/4);

        chart.invalidate(); // refresh
    }

    private final String[] directionSymbols = {
            "Spento", "Acceso"};

    private class DirectionAxisValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value == 1)
                return "" + directionSymbols[1];
            else
                return "" + directionSymbols[0];
        }

        @Override
        public int getDecimalDigits() {
            return 1;
        }
    }

    private class DirectionValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value > 22.5 * 15 || value < 0)
                return "" + value + "N/A";

            if (value == 1)
                return "" + directionSymbols[1];
            else
                return "" + directionSymbols[0];
        }
    }

}
