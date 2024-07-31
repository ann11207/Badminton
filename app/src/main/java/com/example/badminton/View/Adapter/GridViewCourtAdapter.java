package com.example.badminton.View.Adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;
import com.example.badminton.R;

import java.util.List;
public class GridViewCourtAdapter extends BaseAdapter{private Context context;
    private List<CourtDBModel> courtList;
    private Context contexCus;
    private DBHelper dbHelper;
    public GridViewCourtAdapter(Context context, List<CourtDBModel> courtList) {
        this.context = context;
        this.courtList = courtList;
    }

    @Override
    public int getCount() {
        return courtList.size();
    }

    @Override
    public Object getItem(int position) {
        return courtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_grid_view_court, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imgViewCourt1);
        TextView nameView = convertView.findViewById(R.id.textViewNameCourt1);
        ImageView statusIcon = convertView.findViewById(R.id.statusIcon);

        CourtDBModel court = courtList.get(position);
        nameView.setText("Sân "+court.getName());
//        statusView.setText(court.getStatusCourt());

        if (court.getImage() != null) {
            imageView.setImageBitmap(convertToBitmap(court.getImage()));

        }
        switch (court.getStatusCourt()) {
            case "Hoạt động":
                statusIcon.setImageResource(R.drawable.icon_active);
                break;
            case "Bảo trì":
                statusIcon.setImageResource(R.drawable.baotri);
                break;
            case "Trống":
                statusIcon.setImageResource(R.drawable.empty); // Màu xám
                break;
            default:
                statusIcon.setImageResource(R.drawable.empty); // Mặc định
                break;
        }

        return convertView;
    }

    private Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}