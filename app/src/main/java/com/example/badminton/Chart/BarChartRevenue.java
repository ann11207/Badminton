package com.example.badminton.Chart;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import com.example.badminton.Model.BillDBModel;

public class BarChartRevenue {

    public static void updateBarChart(List<BillDBModel> invoiceList, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();


        int start = Math.max(0, invoiceList.size() - 3);
        for (int i = start; i < invoiceList.size(); i++) {
            labels.add(invoiceList.get(i).getDate());
            entries.add(new BarEntry(i - start, (float) invoiceList.get(i).getTotalPrice()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tổng doanh thu");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.invalidate();
    }
}
