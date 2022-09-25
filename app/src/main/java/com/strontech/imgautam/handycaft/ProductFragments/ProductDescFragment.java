package com.strontech.imgautam.handycaft.ProductFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.activities.LoginActivity;
import com.strontech.imgautam.handycaft.activities.byMethod;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.userfragments.UserAddressFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDescFragment extends Fragment implements OnClickListener {


    private Toolbar toolbarProductDescFragment;
    private ImageView imageViewProductImage;

    private ImageButton imageButtonWishList;
    private ImageButton imageButtonShare;

    private TextView textViewProductName;
    private TextView textViewProductResellerName;
    private TextView textViewProductSP;
    private TextView textViewProductMRP;
    private TextView textViewProductDiscount;
    private TextView textViewProductHighlights;
    private TextView textViewProductDetails;

    private Button buttonAddToCartItem;
    private Button buttonGoToCartItem;
    private Button buttonBuyProduct;

    private String productId;
    private String productImage;
    private String productName;
    private String productResellerName;
    private String productSP;
    private String productMRP;
    private String productDiscount;
    private String productQuantity;
    private String productHighlight;
    private String productDesc;

    private DatabaseReference databaseReference;

    View view;

    //To check user logged in or not
    SharedPreferences sharedPreferences;
    String email;
    String email_google;
    String first_name;
    String last_name;

    public ProductDescFragment() {
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
        view = inflater.inflate(R.layout.fragment_product_desc, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {

        toolbarProductDescFragment=view.findViewById(R.id.toolbarProductDescFragment);
        imageViewProductImage = view.findViewById(R.id.imageViewProductImage);

        imageButtonWishList = view.findViewById(R.id.imageButtonWishList);
        imageButtonShare = view.findViewById(R.id.imageButtonShare);

        textViewProductName = view.findViewById(R.id.textViewProductName);
        textViewProductResellerName = view.findViewById(R.id.textViewProductResellerName);
        textViewProductSP = view.findViewById(R.id.textViewProductSP);
        textViewProductMRP = view.findViewById(R.id.textViewProductMRP);
        textViewProductDiscount = view.findViewById(R.id.textViewProductDiscount);
        textViewProductHighlights = view.findViewById(R.id.textViewProductHighlights);
        textViewProductDetails = view.findViewById(R.id.textViewProductDetails);

        buttonAddToCartItem = view.findViewById(R.id.buttonAddToCartItem);
        buttonGoToCartItem = view.findViewById(R.id.buttonGoToCartItem);
        buttonBuyProduct = view.findViewById(R.id.buttonBuyProduct);

    }



    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        buttonAddToCartItem.setOnClickListener(this);
        buttonGoToCartItem.setOnClickListener(this);
        buttonBuyProduct.setOnClickListener(this);
    }


    /**
     * This method is to initialize objects
     */
    private void initObjects() {


        sharedPreferences = getActivity().getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Cart Items");
        Bundle b = getArguments();
        if (b != null) {
            setData(b);

            String pName=b.getString("product_name");
            setUpToolbar(pName);
        }
    }




    /**
     * This method shows toolbar
     */
    private void setUpToolbar(String prName) {
        toolbarProductDescFragment.setTitle(prName);
        toolbarProductDescFragment.setTitleTextColor(Color.WHITE);
        toolbarProductDescFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarProductDescFragment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }



    /**
     * This method to set data on fields
     *
     * @param b to get data from Bundle
     */
    private void setData(Bundle b) {

        productId = b.getString("product_key");
        productImage = b.getString("product_image");
        productName = b.getString("product_name");
        productResellerName = b.getString("product_reseller_name");
        productSP = b.getString("product_sp");
        productMRP = b.getString("product_mrp");
        productDiscount = b.getString("product_discount");
        productQuantity = b.getString("product_quantity");
        productHighlight = b.getString("product_highlight");
        productDesc = b.getString("product_description");


        Glide.with(getActivity()).load(productImage).into(imageViewProductImage);
        textViewProductName.setText(productName);
        textViewProductSP.setText("RM" + productSP);
        textViewProductMRP.setText("RM" + productMRP);
        textViewProductDiscount.setText(productDiscount + "% off");
        textViewProductHighlights.setText(productHighlight);
        textViewProductDetails.setText(productDesc);
        textViewProductResellerName.setText(productResellerName);
        strikeThroughText(textViewProductMRP);
    }

    /**
     * This method to strike through text
     *
     * @param price of product
     */
    private void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }


    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get id of view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddToCartItem:
                addItemToCart();
                break;
            case R.id.buttonGoToCartItem:
                goToProductCartFragment();
                break;
            case R.id.buttonBuyProduct:
                goToAddressFragment();
                break;
        }
    }


    /**
     * This is method is to add item to cart
     */
    private void addItemToCart() {

        int index = 1;

        if (index == 1) {

            //take another Bean class to set data of cart items
            CartHandiCraft cartHandiCraft = new CartHandiCraft();
            cartHandiCraft.setProduct_id(productId);
            cartHandiCraft.setProduct_image(productImage);
            cartHandiCraft.setProduct_name(productName);
            cartHandiCraft.setProduct_reseller_name(productResellerName);
            cartHandiCraft.setProduct_mrp(productMRP);
            cartHandiCraft.setProduct_sp(productSP);
            cartHandiCraft.setProduct_discount(productDiscount);
            cartHandiCraft.setProduct_quantity(productQuantity);
            cartHandiCraft.setProduct_highlight(productHighlight);
            cartHandiCraft.setProduct_desc(productDesc);

            databaseReference.child(productId).setValue(cartHandiCraft);
            Toast.makeText(getActivity(), "Item added to cart", Toast.LENGTH_SHORT).show();

        }
        buttonAddToCartItem.setVisibility(View.GONE);
        buttonGoToCartItem.setVisibility(View.VISIBLE);

    }


    /**
     * This method to go to ProductCartFragment
     */
    private void goToProductCartFragment() {


  /*      //Send Initial data to cart fragment
        Bundle p = new Bundle();
        p.putString("initial_price", productSP);

        new ProductCartFragment().setArguments(p);

        //==================================
*/

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new ProductCartFragment());
        ft.addToBackStack(null);
        ft.commit();
    }


    /**
     * This method to go AddressFragment
     * Verify user logged in or not
     * If logged in go to AddressFragment
     * Other wise go to Login Activity
     */
    private void goToAddressFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new UserAddressFragment());
        ft.addToBackStack(null);
        ft.commit();

          //  Intent intent = new Intent(getActivity(), byMethod.class);
          //  startActivity(intent);

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
