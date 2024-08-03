package com.example.badminton.View.Adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;
import com.example.badminton.Model.Queries.customerDB;
import com.example.badminton.R;

import java.util.List;
public class GridViewCourtAdapter extends BaseAdapter{private Context context;
    private List<CourtDBModel> courtList;
    private Context contexCus;
    private DBHelper dbHelper;
    private List<CustomerDBModel>customerList;

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
                statusIcon.setImageResource(R.drawable.empty);
                break;
            default:
                statusIcon.setImageResource(R.drawable.empty);
                break;
        }

        convertView.setOnClickListener(v -> showCustomerList(court));

        return convertView;
    }

    private Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private void showCustomerList(CourtDBModel court){
        Dialog customerDialog = new Dialog(context);
        customerDialog.setContentView(R.layout.activity_dialog_customer_list);
        GridView gridViewCustomer = customerDialog.findViewById(R.id.grid_view_customer);

        customerDB customerDB = new customerDB(context);
        customerList = customerDB.getAllCustomers();

        GridViewCustomerAdapter customerAdapter = new GridViewCustomerAdapter(context, customerList);
        gridViewCustomer.setAdapter(customerAdapter);
        customerDialog.show();
    }
}