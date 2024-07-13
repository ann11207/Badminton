
package com.example.badminton.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.UserAccountModel;
import com.example.badminton.R;

import java.util.List;

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.UserViewHolder> {

    private List<UserAccountModel> userList;
    private OnUserClickListener listener;

    public UserAccountAdapter(List<UserAccountModel> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_account_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserAccountModel user = userList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewRole, textPhoneNumber, textViewEmail;
        ImageButton buttonEdit, buttonDelete, buttonInfo;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);

            buttonInfo = itemView.findViewById(R.id.buttonInfoDetail);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(UserAccountModel user, OnUserClickListener listener) {
            textViewName.setText(user.getNameAccount());
            textViewEmail.setText(user.getEmail());
            textPhoneNumber.setText(user.getPhoneNumber());
            textViewRole.setText(user.getRole());
            buttonEdit.setOnClickListener(v -> listener.onEditClick(user));
            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(user));
            buttonInfo.setOnClickListener(v -> listener.onInfoClick(user));

        }
    }

    public interface OnUserClickListener {
        void onEditClick(UserAccountModel user);

        void onDeleteClick(UserAccountModel user);

        void onInfoClick(UserAccountModel user);
    }
}