package com.example.badminton.View.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.R;

import java.util.List;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {
    private List<CourtDBModel> courtList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CourtAdapter(List<CourtDBModel> courtList) {
        this.courtList = courtList;
    }

    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_court_item, parent, false);
        return new CourtViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
        CourtDBModel court = courtList.get(position);
        holder.name.setText(court.getName());
        holder.status.setText(court.getStatusCourt());
        holder.image.setImageBitmap(convertToBitmap(court.getImage()));
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }

    private Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static class CourtViewHolder extends RecyclerView.ViewHolder {
        TextView name, status;
        ImageView image;
        ImageButton editButton, deleteButton;

        public CourtViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameCourt);
            status = itemView.findViewById(R.id.textViewStatusCourt);
            image = itemView.findViewById(R.id.imgViewCourt);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
