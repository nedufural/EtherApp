/*
package com.fastcon.etherapp.ui.traded_volume_details

import com.fastcon.etherapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

class TradedVolumeDetailsChartAdapter{
    // generate Dates
    Calendar calendar = Calendar.getInstance();
    Date d1 = calendar.getTime();
    calendar.add(Calendar.DATE, 1);
    Date d2 = calendar.getTime();
    calendar.add(Calendar.DATE, 1);
    Date d3 = calendar.getTime();

    GraphView graph = (GraphView) findViewById(R.id.m_chart);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
        new DataPoint(d1, 1),
        new DataPoint(d2, 5),
        new DataPoint(d3, 3)
    });

    graph.addSeries(series);

// set date label formatter
    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
    graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
    graph.getViewport().setMinX(d1.getTime());
    graph.getViewport().setMaxX(d3.getTime());
    graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
    graph.getGridLabelRenderer().setHumanRounding(false);
}*/
