package com.example.badminton.Chart;

import android.database.Cursor;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {

    public static void setupChart(BarChart barChart, Cursor courtsCursor) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        if (courtsCursor != null) {
            int nameIndex = courtsCursor.getColumnIndex("name");
            if (nameIndex == -1) {
                // Cột 'name' không tồn tại, xử lý lỗi ở đây
                throw new IllegalStateException("Column 'name' does not exist in the Cursor.");
            }

            while (courtsCursor.moveToNext()) {
                String courtName = courtsCursor.getString(nameIndex);
                labels.add(courtName);

                // Thêm điểm dữ liệu vào danh sách
                entries.add(new BarEntry(index, 1)); // Sử dụng giá trị 1 làm ví dụ để đại diện cho mỗi sân
                index++;
            }

            BarDataSet dataSet = new BarDataSet(entries, "Danh Sách Sân");
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            // Cấu hình trục X
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setLabelRotationAngle(45);
            xAxis.setGranularity(1f); // Đảm bảo không bị trùng lặp

            // Cấu hình trục Y
            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setLabelCount(5, true); // Số lượng nhãn trên trục Y

            barChart.getAxisRight().setEnabled(false); // Tắt trục Y bên phải
            barChart.setFitBars(true); // Điều chỉnh kích thước cột cho phù hợp
            barChart.invalidate(); // Cập nhật biểu đồ

            courtsCursor.close(); // Đảm bảo đóng cursor
        }
    }
}
