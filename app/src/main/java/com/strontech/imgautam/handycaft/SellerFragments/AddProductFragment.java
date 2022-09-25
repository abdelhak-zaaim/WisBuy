package com.strontech.imgautam.handycaft.SellerFragments;


import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.helper.Constants;
import com.strontech.imgautam.handycaft.helper.InputValidation;
import com.strontech.imgautam.handycaft.model.HandiCraft;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment implements OnClickListener {

    private Toolbar toolbarAddProductFragment;
    private Spinner categories;
    private TextInputLayout textInputLayoutProductName;
    private TextInputLayout textInputLayoutResellerName;
    private TextInputLayout textInputLayoutProductMRP;
    private TextInputLayout textInputLayoutProductSP;
    private TextInputLayout textInputLayoutProductDiscount;
    private TextInputLayout textInputLayoutProductQuantity;
    private TextInputLayout textInputLayoutProductHighlight;
    private TextInputLayout textInputLayoutProductDesc;

    private TextInputEditText textInputEditTextProductName;
    private TextInputEditText textInputEditTextResellerName;
    private TextInputEditText textInputEditTextProductMRP;
    private TextInputEditText textInputEditTextProductSP;
    private TextInputEditText textInputEditTextProductDiscount;
    private TextInputEditText textInputEditTextProductQuantity;
    private TextInputEditText textInputEditTextProductHighlight;
    private TextInputEditText textInputEditTextProductDesc;
    private static final int PICK_IMAGE_REQUEST = 0;

    private ImageView imageViewProductImage;
    private Button buttonChooseProductImage;
    private Button buttonAddProduct;
    private DatabaseReference  databaseReference_latest;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
private String Categories_list[];
    private InputValidation inputValidation;
    View view;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * This is override method to hide activity toolbar on onResume method
     */
    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
Categories_list= new String[]{"cat1", "cat2", "cat3", "cat4"};
        initViews();
        initListeners();
        initObjects();
        return view;
    }

    /**
     * This method is to initialization views
     */
    private void initViews() {

        toolbarAddProductFragment = view.findViewById(R.id.toolbarAddProductFragment);
        categories = view.findViewById(R.id.categories_spinner);
        textInputLayoutProductName = view.findViewById(R.id.textInputLayoutProductName);
        textInputLayoutResellerName = view.findViewById(R.id.textInputLayoutResellerName);
        textInputLayoutProductMRP = view.findViewById(R.id.textInputLayoutProductMRP);
        textInputLayoutProductSP = view.findViewById(R.id.textInputLayoutProductSP);
        textInputLayoutProductDiscount = view.findViewById(R.id.textInputLayoutProductDiscount);
        textInputLayoutProductQuantity = view.findViewById(R.id.textInputLayoutProductQuantity);
        textInputLayoutProductHighlight = view.findViewById(R.id.textInputLayoutProductHighlight);
        textInputLayoutProductDesc = view.findViewById(R.id.textInputLayoutProductDesc);

        textInputEditTextProductName = view.findViewById(R.id.textInputEditTextProductName);
        textInputEditTextResellerName = view.findViewById(R.id.textInputEditTextResellerName);
        textInputEditTextProductMRP = view.findViewById(R.id.textInputEditTextProductMRP);
        textInputEditTextProductSP = view.findViewById(R.id.textInputEditTextProductSP);
        textInputEditTextProductDiscount = view.findViewById(R.id.textInputEditTextProductDiscount);
        textInputEditTextProductQuantity = view.findViewById(R.id.textInputEditTextProductQuantity);
        textInputEditTextProductHighlight = view.findViewById(R.id.textInputEditTextProductHighlight);
        textInputEditTextProductDesc = view.findViewById(R.id.textInputEditTextProductDesc);

        imageViewProductImage = view.findViewById(R.id.imageViewProductImage);
        buttonChooseProductImage = view.findViewById(R.id.buttonChooseProductImage);
        buttonAddProduct = view.findViewById(R.id.buttonAddProduct);
    }


    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        buttonChooseProductImage.setOnClickListener(this);
        buttonAddProduct.setOnClickListener(this);
    }


    /**
     * This method is to initialization Objects
     */
    private void initObjects() {
        setUpToolbar();

        storageReference = FirebaseStorage.getInstance().getReference();
        inputValidation = new InputValidation(getActivity());
    }

    /**
     * This method shows toolbar
     */
    private void setUpToolbar() {
        toolbarAddProductFragment.setTitle("Sell Products");
        toolbarAddProductFragment.setTitleTextColor(Color.WHITE);
        toolbarAddProductFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarAddProductFragment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get id of view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChooseProductImage:
                showImageChooser();
                break;
            case R.id.buttonAddProduct:
                postProductInfo();
                break;
        }
    }


    /**
     * This method is to Show Image Chooser
     */
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"),
                PICK_IMAGE_REQUEST);
    }


    /**
     * This is override method (get image from device)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageViewProductImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Select ProductImage", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method is to get image file extension
     */
    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * This method is to upload product data to server
     */
    private void postProductInfo() {

        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductName, textInputLayoutProductName,
                "Enter Product Name")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextResellerName, textInputLayoutResellerName,
                "Enter Reseller name")) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductSP, textInputLayoutProductSP,
                "Enter Product Selling Price")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductMRP, textInputLayoutProductMRP,
                "Enter Product MRP")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductDiscount, textInputLayoutProductDiscount,
                "Enter Product Discount")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductQuantity, textInputLayoutProductQuantity,
                "Enter Product Quantity")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductHighlight, textInputLayoutProductHighlight,
                "Enter Product Quantity")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextProductDesc, textInputLayoutProductDesc,
                "Enter Product Description")) {
            return;
        }


        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference storageRef = storageReference.child(
                    Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis()
                            + "." + getFileExtension(filePath)
            );

            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                        @Override
                        public void onSuccess(TaskSnapshot taskSnapshot) {
                            databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference(Constants.DATABASE_PATH_UPLOADS.concat("/").concat(Categories_list[categories.getSelectedItemPosition()]));
                            databaseReference_latest = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference(Constants.DATABASE_PATH_UPLOADS.concat("/latest"));

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Data uploaded", Toast.LENGTH_SHORT).show();

                            String uploadId = databaseReference.push().getKey();

                            HandiCraft handiCrafts = new HandiCraft();
                            handiCrafts.setCategory(Categories_list[categories.getSelectedItemPosition()]);
                            handiCrafts.setProduct_id(uploadId);
                            handiCrafts.setProduct_image(taskSnapshot.getDownloadUrl().toString());
                            handiCrafts.setProduct_name(textInputEditTextProductName.getText().toString().trim());
                            handiCrafts.setProduct_reseller_name(textInputEditTextResellerName.getText().toString().trim());
                            handiCrafts.setProduct_sp(textInputEditTextProductSP.getText().toString().trim());
                            handiCrafts.setProduct_mrp(textInputEditTextProductMRP.getText().toString().trim());
                            handiCrafts.setProduct_discount(textInputEditTextProductDiscount.getText().toString().trim());
                            handiCrafts.setProduct_quantity(textInputEditTextProductQuantity.getText().toString().trim());
                            handiCrafts.setProduct_highlight(textInputEditTextProductHighlight.getText().toString().trim());
                            handiCrafts.setProduct_desc(textInputEditTextProductDesc.getText().toString().trim());


                            databaseReference.child(uploadId).setValue(handiCrafts);
                            databaseReference_latest.child(uploadId).setValue(handiCrafts);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                        @Override
                        public void onProgress(TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

        } else {
            Toast.makeText(getActivity(), "Please File select", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is override method to show toolbar of activity
     */
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}

























