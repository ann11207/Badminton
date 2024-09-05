
package com.example.badminton.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.ProductDBModel;
import com.example.badminton.Model.Queries.catalogDB;
import com.example.badminton.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductDBModel> productList;
    private catalogDB catalogDB;
//    private productDB productDB;

    public ProductAdapter(Context context, List<ProductDBModel> productList) {
        this.context = context;
        this.productList = productList;
        this.catalogDB = new catalogDB(context);
//        this.productDB = new productDB(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDBModel product = productList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()));
        holder.tvProductQuantity.setText(String.valueOf(product.getQuantity()));

        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            holder.ivProductImage.setImageBitmap(bitmap);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.products);
        }
        holder.itemView.setOnClickListener(v -> {
            showOptionsDialog(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductQuantity;
        ImageView ivProductImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
        }
    }

    private void showOptionsDialog(ProductDBModel product) {
        // Tạo và hiển thị một dialog để xóa hoặc chỉnh sửa sản phẩm
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lựa chọn : ")
                .setItems(new String[]{"Chỉnh sửa", "Xoá"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showEditProductDialog(product);
                            break;
                        case 1:
                            deleteProduct(product);
                            break;
                    }
                })
                .show();
    }
    private void showEditProductDialog(ProductDBModel product) {
        // Hiển thị dialog chỉnh sửa sản phẩm
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_edit_product, null);
        builder.setView(dialogView);

        EditText etProductName = dialogView.findViewById(R.id.et_edit_product_name);
        EditText etProductPrice = dialogView.findViewById(R.id.et_edit_product_price);
        EditText etProductQuantity = dialogView.findViewById(R.id.et_edit_product_quantity);
        ImageView ivProductImage = dialogView.findViewById(R.id.iv_edit_product_image);
        Button btnSave = dialogView.findViewById(R.id.btn_save_product);

        etProductName.setText(product.getName());
        etProductPrice.setText(String.valueOf(product.getPrice()));
        etProductQuantity.setText(String.valueOf(product.getQuantity()));

        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            ivProductImage.setImageBitmap(bitmap);
        }

        builder.setTitle("Edit Product");

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String name = etProductName.getText().toString().trim();
            String priceStr = etProductPrice.getText().toString().trim();
            String quantityStr = etProductQuantity.getText().toString().trim();

            if (!name.isEmpty() && !priceStr.isEmpty() && !quantityStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

//                byte[] imageBytes = ((BitmapDrawable) ivProductImage.getDrawable()).getBitmapAsByteArray();

                product.setName(name);
                product.setPrice(price);
                product.setQuantity(quantity);
//                product.setImage(imageBytes);

               catalogDB.updateProduct(product);

                Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(ProductDBModel product) {
        catalogDB.deleteProduct(product.getProductId());
        Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
        // Refresh the product list
        productList.remove(product);
        notifyDataSetChanged();
    }

//    public static class ProductViewHolder extends RecyclerView.ViewHolder {
//        TextView tvProductName, tvProductPrice, tvProductQuantity;
//        ImageView ivProductImage;
//
//        public ProductViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
//            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
//            ivProductImage = itemView.findViewById(R.id.iv_product_image);
//        }
//    }
}




