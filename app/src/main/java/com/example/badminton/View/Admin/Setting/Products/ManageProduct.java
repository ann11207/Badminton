package com.example.badminton.View.Admin.Setting.Products;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.ProductDBModel;
import com.example.badminton.Model.Queries.catalogDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.ProductAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ManageProduct extends AppCompatActivity {

    private EditText etCatalogName, etProductName, etProductPrice, etProductQuantity;
    private Button btnAddCatalog, btnAddProduct, btnSelectImage;
    private Spinner spinnerCatalog;
    private catalogDB catalogDB;
    private List<String> catalogList;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView imageViewProduct;
    private List<ProductDBModel> productList;

    private byte[] imageBytes; // Khai báo biến toàn cục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        catalogDB = new catalogDB(this);

        etCatalogName = findViewById(R.id.et_catalog_name);
        etProductName = findViewById(R.id.et_product_name);
        etProductQuantity = findViewById(R.id.et_product_quantity);
        etProductPrice = findViewById(R.id.et_product_price);
        btnAddCatalog = findViewById(R.id.btn_add_catalog);
        btnAddProduct = findViewById(R.id.btn_add_product);
        spinnerCatalog = findViewById(R.id.spinner_catalog);
        imageViewProduct = findViewById(R.id.iv_product_image);
        btnSelectImage = findViewById(R.id.btn_select_image);

        recyclerView = findViewById(R.id.rv_showProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load catalogs into spinner
        loadCatalogs();

        btnSelectImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            chooseImageLauncher.launch(pickImageIntent);
        });

        // Add catalog button click
        btnAddCatalog.setOnClickListener(v -> {
            String catalogName = etCatalogName.getText().toString().trim();
            if (!catalogName.isEmpty()) {
                long result = catalogDB.addCatalog(catalogName);
                if (result != -1) {
                    Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    etCatalogName.setText("");
                    loadCatalogs();
                } else {
                    Toast.makeText(this, "Failed to add catalog", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter catalog name", Toast.LENGTH_SHORT).show();
            }
        });

        // Add product button click
        btnAddProduct.setOnClickListener(v -> {
            String productName = etProductName.getText().toString().trim();
            String productPriceStr = etProductPrice.getText().toString().trim();
            String productQuantityStr = etProductQuantity.getText().toString().trim();

            if (!productName.isEmpty() && !productPriceStr.isEmpty() && !productQuantityStr.isEmpty()) {
                double productPrice = Double.parseDouble(productPriceStr);
                int productQuantity = Integer.parseInt(productQuantityStr);
                int catalogId = spinnerCatalog.getSelectedItemPosition() + 1;

                long result = catalogDB.addProduct(productName, productPrice, imageBytes, productQuantity, catalogId);
                if (result != -1) {
                    Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                    etProductName.setText("");
                    etProductPrice.setText("");
                    etProductQuantity.setText("");
                    loadProducts(catalogId);
                } else {
                    Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter product name, price, and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        // Show products when a catalog is selected
        spinnerCatalog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int catalogId = position + 1;
                loadProducts(catalogId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadCatalogs() {
        catalogList = catalogDB.getAllCatalogs();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catalogList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCatalog.setAdapter(adapter);
    }

    private void loadProducts(int catalogId) {
        productList = catalogDB.getProductsByCatalog(catalogId);
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
    }

    private final ActivityResultLauncher<Intent> chooseImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageViewProduct.setImageBitmap(bitmap);
                        imageBytes = getBitmapAsByteArray(bitmap);
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
}
