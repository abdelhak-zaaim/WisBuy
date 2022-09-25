package com.strontech.imgautam.handycaft.ProductFragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ProductWishRecyclerAdapter;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductWishListFragment extends Fragment implements OnClickListener {

    private LinearLayout linearLayoutWishListFragmentEmpty;
    private LinearLayout linearLayoutWishListFragment;
    private Button buttonContinueShopping;
    private Toolbar toolbarWishListFragment;
    private RecyclerView recyclerViewShowWishListItems;
    private RecyclerView.Adapter adapter;

    private LinearLayout circleProgressBarLayout;
    private CircleProgressBar circleProgressBar;

    private View view;

    private List<CartHandiCraft> cartHandiCraftList;

    private DatabaseReference databaseReference;

    public ProductWishListFragment() {
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
        view = inflater.inflate(R.layout.fragment_product_wish_list, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {

        toolbarWishListFragment = view.findViewById(R.id.toolbarWishListFragment);

        linearLayoutWishListFragmentEmpty = view.findViewById(R.id.linearLayoutWishListFragmentEmpty);
        linearLayoutWishListFragment = view.findViewById(R.id.linearLayoutWishListFragment);

        buttonContinueShopping = view.findViewById(R.id.buttonContinueShopping);

        circleProgressBarLayout = view.findViewById(R.id.circleProgressBarLayout);
        circleProgressBar = view.findViewById(R.id.circleProgressBar);
        recyclerViewShowWishListItems = view.findViewById(R.id.recyclerViewShowWishListItems);
    }


    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        buttonContinueShopping.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects
     */
    private void initObjects() {
        setUpToolbar();
        cartHandiCraftList = new ArrayList<CartHandiCraft>();
        circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
        setUpRecyclerView();
        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Wish Item");
        getDataFromDatabase();
    }




    /**
     * This method shows toolbar
     */
    private void setUpToolbar() {
        toolbarWishListFragment.setTitle("Wishlist Items");
        toolbarWishListFragment.setTitleTextColor(Color.WHITE);
        toolbarWishListFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbarWishListFragment.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new HomeFragment());
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.commit();
            }
        });

    }


    /**
     * This method to setup recyclerView
     * */
    private void setUpRecyclerView() {
        recyclerViewShowWishListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewShowWishListItems.setItemAnimator(new DefaultItemAnimator());

    }


    /**
     * This method is to get data from FirebaseDatabase
     * */
    private void getDataFromDatabase() {

        circleProgressBarLayout.setVisibility(View.VISIBLE);
        circleProgressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                circleProgressBarLayout.setVisibility(View.GONE);
                circleProgressBar.setVisibility(View.GONE);

                if (cartHandiCraftList != null) {
                    cartHandiCraftList.clear();
                }

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CartHandiCraft cartHandiCraft = postSnapshot.getValue(CartHandiCraft.class);
                    cartHandiCraftList.add(cartHandiCraft);
                }

                adapter = new ProductWishRecyclerAdapter(getActivity(), cartHandiCraftList);
                recyclerViewShowWishListItems.setAdapter(adapter);

                if (cartHandiCraftList.size() == 0) {
                    linearLayoutWishListFragment.setVisibility(View.GONE);
                    linearLayoutWishListFragmentEmpty.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutWishListFragment.setVisibility(View.VISIBLE);
                    linearLayoutWishListFragmentEmpty.setVisibility(View.INVISIBLE);
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

        if (v.getId() == buttonContinueShopping.getId()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new HomeFragment());
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
        }

    }
}
