package com.fastcon.etherapp.ui.traded_volume_details

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.TradedIntervalsResponse
import com.fastcon.etherapp.util.HourAxisValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TradedVolumeGraphViewModel : ViewModel() {


    val tradedIntervalsMutableLiveData: MutableLiveData<List<TradedIntervalsResponse.Data>> =
        MutableLiveData()
    val tradedIntervalsResponseErrorMsg: MutableLiveData<String> = MutableLiveData()
    lateinit var errorMsg: String


    fun getTradedHistoryIntervals(id: String) {

        val call: Call<TradedIntervalsResponse> =
            DataManager.getApiService().getTradedHistoryIntervals(id)
        call.enqueue(object : Callback<TradedIntervalsResponse> {
            override fun onFailure(call: Call<TradedIntervalsResponse>, t: Throwable) {
                tradedIntervalsResponseErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<TradedIntervalsResponse>,
                response: Response<TradedIntervalsResponse>
            ) {
                tradedIntervalsMutableLiveData.value = response.body()?.data
            }
        })

    }

    /**
     * Get and prepare the data to draw in the chart.
     *
     * @param recordsList    list of records.
     * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
     * @return list of entries.
     */
     fun getChartData(recordsList:ArrayList<TradedIntervalsResponse.Data>, firstTimestamp:Long): List<Entry> {
        val records: Array<TradedIntervalsResponse.Data> = recordsList.toArray(arrayOfNulls<TradedIntervalsResponse.Data>(recordsList.size))
        val entries: MutableList<Entry> = ArrayList()
        for (record in records) {
            // Convert timestamp to seconds and relative to first timestamp
            val timestamp: Long = record.time / 1000 - firstTimestamp
            entries.add(Entry(timestamp.toFloat(), record.priceUsd.toFloat()))
        }
        return entries
    }

    /**
     * Style char lines (type, color, etc.).
     *
     * @param entries list of entries.
     * @return line data chart.
     */
     fun styleChartLines(context: Context, entries: List<Entry>): LineData {
        // Set styles
        var lineDataSet = LineDataSet(entries, "Rates History")
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.cubicIntensity = 0.2f
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.color = ContextCompat.getColor(context, R.color.colorAccent)
        if ((lineDataSet.yMax).toInt() != 0) {
            lineDataSet.setDrawFilled(true)
            lineDataSet.fillAlpha = 255
            // Fix bug with vectors in API < 21
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                val drawable: Drawable? = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.fade_blue, null
                )
                lineDataSet.fillDrawable = drawable
            } else {
                lineDataSet.fillColor = ContextCompat.getColor(context, R.color.colorPrimary)
            }
        }
        return LineData(lineDataSet)
    }

    /**
     * Setup chart (axis, grid, etc.).
     *
     * @param lineChart      chart to setup.
     * @param data           chart with the data.
     * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
     */
     fun setupChart(
        context: Context,
        lineChart: LineChart,
        data: LineData,
        firstTimestamp: Long
    ) {
        // General setup
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        lineChart.setViewPortOffsets(50f, 0f, 50f, 50f)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.setNoDataText(context.getString(R.string.no_data_available))
        // X axis setup
        val xAxisFormatter: ValueFormatter = HourAxisValueFormatter(firstTimestamp)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(false)
        xAxis.textColor = ContextCompat.getColor(context, R.color.green)
        // Y axis setup
        val yAxis: YAxis = lineChart.axisLeft
        yAxis.axisMaximum = 40f
        yAxis.axisMinimum = 0f
        yAxis.setDrawLabels(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(true)
        lineChart.axisRight.isEnabled = false
        // Add data
        lineChart.data = data
    }
}

