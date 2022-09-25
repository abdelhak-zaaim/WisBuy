package com.strontech.imgautam.handycaft.ProductFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.SellerFragments.AddProductFragment;
import com.strontech.imgautam.handycaft.activities.LoginActivity;
import com.strontech.imgautam.handycaft.activities.byMethod;
import com.strontech.imgautam.handycaft.adapters.ProductCartRecyclerAdapter;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.userfragments.UserAddressFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCartFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbarFragmentCart;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private LinearLayout linearLayoutCart;
    private LinearLayout linearLayoutCartEmpty;

    private LinearLayout circleProgressBarLayout;
    private CircleProgressBar circleProgressBar;

    private DatabaseReference databaseReference;

    private List<CartHandiCraft> cartHandiCrafts;

    private TextView textViewItemCount;
    private TextView textViewTotalAmount;
    private TextView textViewTotalAmountPayable;

    private Button buttonTotalAmount;
    private Button buttonShopNow;
    private Button buttonContinueBuy;

    //To check user logged in or not
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email;
    String email_google;
    String first_name;
    String last_name;

    View view;
    String initial_price;
    int sumSp;

    public ProductCartFragment() {
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
        view = inflater.inflate(R.layout.fragment_product_cart, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }


    /**
     * This method ot initialize views
     */
    private void initViews() {

        toolbarFragmentCart = view.findViewById(R.id.toolbarFragmentCart);

        linearLayoutCart = view.findViewById(R.id.linearLayoutCart);
        linearLayoutCartEmpty = view.findViewById(R.id.linearLayoutCartEmpty);
        recyclerView = view.findViewById(R.id.recyclerViewShowCartItems);

        circleProgressBarLayout = view.findViewById(R.id.circleProgressBarLayout);
        circleProgressBar = view.findViewById(R.id.circleProgressBar);

        textViewItemCount = view.findViewById(R.id.textViewItemCount);
        textViewTotalAmount = view.findViewById(R.id.textViewTotalAmount);
        textViewTotalAmountPayable = view.findViewById(R.id.textViewTotalAmountPayable);

        buttonTotalAmount = view.findViewById(R.id.buttonTotalAmount);
        buttonShopNow = view.findViewById(R.id.buttonShopNow);
        buttonContinueBuy = view.findViewById(R.id.buttonContinueBuy);
    }


    /**
     * This method ot initialize Listeners
     */
    private void initListeners() {
        buttonShopNow.setOnClickListener(this);
        buttonContinueBuy.setOnClickListener(this);
        buttonTotalAmount.setOnClickListener(this);
    }


    /**
     * This method ot initialize Objects
     */
    private void initObjects() {
        setUpToolbar();
        cartHandiCrafts = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);

        circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
        setUpRecyclerView();

        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Cart Items");

        getDataFromFirebase();

    }


    /**
     * This method shows toolbar
     */
    private void setUpToolbar() {
        toolbarFragmentCart.setTitle("Your cart");
        toolbarFragmentCart.setTitleTextColor(Color.WHITE);
        toolbarFragmentCart.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarFragmentCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    /**
     * This method to setup the recyclerView
     */
    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * This method to get data from Firebase Database
     */
    private void getDataFromFirebase() {
        circleProgressBarLayout.setVisibility(View.VISIBLE);
        circleProgressBar.setVisibility(View.VISIBLE);


        final List<String> updatedSpList = new ArrayList<String>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                circleProgressBarLayout.setVisibility(View.GONE);
                circleProgressBar.setVisibility(View.GONE);

                if (cartHandiCrafts != null && updatedSpList != null) {
                    cartHandiCrafts.clear();
                    updatedSpList.clear();
                }
                for (DataSnapshot postDataSnapshot : dataSnapshot.getChildren()) {
                    CartHandiCraft cartHandiCraft = postDataSnapshot.getValue(CartHandiCraft.class);
                    cartHandiCrafts.add(cartHandiCraft);
                    updatedSpList.add(cartHandiCraft.getProduct_sp());

                }

                sumSp = 0;
                for (int i = 0; i < updatedSpList.size(); i++) {
                    sumSp = sumSp + (int)Double.parseDouble(updatedSpList.get(i));
                }
                adapter = new ProductCartRecyclerAdapter(getActivity(), cartHandiCrafts);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (cartHandiCrafts.size() == 0) {
                    linearLayoutCart.setVisibility(View.GONE);
                    linearLayoutCartEmpty.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutCartEmpty.setVisibility(View.GONE);
                    linearLayoutCart.setVisibility(View.VISIBLE);
                    textViewItemCount.setText("" + cartHandiCrafts.size());
                    textViewTotalAmount.setText("RM" + sumSp);
                    textViewTotalAmountPayable.setText("RM" + sumSp);
                    buttonTotalAmount.setText("RM" + sumSp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * This is override method to show toolbar of activity
     */
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    /**
     * this implemented method is to listen the click on view
     *
     * @param v to get id of view
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == buttonShopNow.getId()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new HomeFragment());
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
        } else if (v.getId() == buttonContinueBuy.getId()) {
            goToAddressOrLogin();
        }
    }


    /**
     * This method to go to Address Fragment
     * First verify user logged in or not
     * If user logged in go to address Fragment otherwise go to Login Activity
     */
    private void goToAddressOrLogin() {
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new UserAddressFragment());
        ft.addToBackStack(null);
        ft.commit();


   //  startActivity(new Intent(getActivity(), byMethod.class));
    }
}
