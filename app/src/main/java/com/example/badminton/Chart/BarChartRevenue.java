package com.example.badminton.Chart;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.badminton.Model.BillDBModel;

public class BarChartRevenue {

    public static void updateBarChart(List<BillDBModel> invoiceList, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Map để lưu tổng doanh thu theo ngày
        Map<String, Double> revenueByDate = new HashMap<>();

        // Tính tổng doanh thu cho từng ngày
        for (BillDBModel bill : invoiceList) {
            String date = bill.getDate(); // Định dạng ngày của bạn có thể cần điều chỉnh
            double totalPrice = bill.getTotalPrice();
            revenueByDate.put(date, revenueByDate.getOrDefault(date, 0.0) + totalPrice);
        }

        // Lấy các ngày và tổng doanh thu, sắp xếp theo thứ tự
        List<String> sortedDates = new ArrayList<>(revenueByDate.keySet());
        sortedDates.sort((d1, d2) -> d2.compareTo(d1)); // Sắp xếp ngày từ mới nhất đến cũ nhất

        // Chỉ lấy 3 ngày gần nhất
        int start = Math.max(0, sortedDates.size() - 3);
        for (int i = start; i < sortedDates.size(); i++) {
            String date = sortedDates.get(i);
            labels.add(date);
            // Explicitly convert Double to float
            entries.add(new BarEntry(i - start, revenueByDate.get(date).floatValue()));
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
