package com.example.badminton.View.Admin.Setting.Customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.Model.Queries.customerDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.CustomerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ManageCustomer extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<CustomerDBModel> customerList;
    private customerDB customerDB;
    private ImageView imageViewCustomer;
    private byte[] customerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        recyclerView = findViewById(R.id.rcv_customer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerDB = new customerDB(this);
        loadCustomerData();

        findViewById(R.id.button_add_customer).setOnClickListener(v -> showAddCustomerDialog());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadCustomerData() {
        customerList = customerDB.getAllCustomers();
        customerAdapter = new CustomerAdapter(this, customerList);
        recyclerView.setAdapter(customerAdapter);
    }

    private void showAddCustomerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_edit_customer_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextPrice = view.findViewById(R.id.editTextPrice);
        imageViewCustomer = view.findViewById(R.id.imageViewCustomer);

        view.findViewById(R.id.buttonChooseImage).setOnClickListener(v -> chooseImage());

        view.findViewById(R.id.buttonSave).setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String priceStr = editTextPrice.getText().toString().trim();
            if (name.isEmpty() || priceStr.isEmpty() || customerImage == null) {
                Toast.makeText(ManageCustomer.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            double price = Double.parseDouble(priceStr);
            long result = customerDB.insertCustomer(name, price, customerImage);
            if (result > 0) {
                Toast.makeText(ManageCustomer.this, "Khách hàng đã được thêm", Toast.LENGTH_SHORT).show();
                loadCustomerData();
                dialog.dismiss();
            } else {
                Toast.makeText(ManageCustomer.this, "Thêm khách hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showEditCustomerDialog(CustomerDBModel customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_edit_customer_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextPrice = view.findViewById(R.id.editTextPrice);
        imageViewCustomer = view.findViewById(R.id.imageViewCustomer);

        editTextName.setText(customer.getName());
        editTextPrice.setText(String.valueOf(customer.getPrice()));
        imageViewCustomer.setImageBitmap(BitmapFactory.decodeByteArray(customer.getImage(), 0, customer.getImage().length));
        customerImage = customer.getImage();

        view.findViewById(R.id.buttonChooseImage).setOnClickListener(v -> chooseImage());

        view.findViewById(R.id.buttonSave).setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String priceStr = editTextPrice.getText().toString().trim();
            if (name.isEmpty() || priceStr.isEmpty() || customerImage == null) {
                Toast.makeText(ManageCustomer.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            double price = Double.parseDouble(priceStr);
            int id = customer.getId();
            if (customerDB.updateCustomer(id, name, price, customerImage)) {
                Toast.makeText(ManageCustomer.this, "Khách hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
                loadCustomerData();
                dialog.dismiss();
            } else {
                Toast.makeText(ManageCustomer.this, "Cập nhật khách hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImageLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> chooseImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageViewCustomer.setImageBitmap(bitmap);
                        customerImage = getBitmapAsByteArray(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void deleteCustomer(int customerId) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa khách hàng")
                .setMessage("Bạn có chắc chắn muốn xóa khách hàng này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (customerDB.deleteCustomer(customerId)) {
                        Toast.makeText(this, "Khách hàng đã được xóa", Toast.LENGTH_SHORT).show();
                        loadCustomerData();
                    } else {
                        Toast.makeText(this, "Xóa khách hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
