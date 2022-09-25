package com.strontech.imgautam.handycaft.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ProductRecyclerAdapter;
import com.strontech.imgautam.handycaft.adapters.SliderData;
import com.strontech.imgautam.handycaft.helper.Constants;
import com.strontech.imgautam.handycaft.model.HandiCraft;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    String url2 = "https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp";
    String url3 = "https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png";

    ViewPager viewPager;
    private LinearLayout circleProgressBarLayout;
    private CircleProgressBar circleProgressBar;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private DatabaseReference databaseReference;

    private List<HandiCraft> handiCrafts;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews();
        initObjects();
        return view;
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {

        circleProgressBarLayout = view.findViewById(R.id.circleProgressBarLayout);
        circleProgressBar = view.findViewById(R.id.circleProgressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ArrayList<String> strt = new ArrayList<>();
        strt.add(url1);
        strt.add(url2);
        strt.add(url2);
        // initializing the slider view.


    }


    /**
     * This method is to initialize objects
     */
    String categorie;

    private void initObjects() {
        sharedPreferences = getActivity().getSharedPreferences("category", Context.MODE_PRIVATE);

        categorie = sharedPreferences.getString("category", "cat1");

        String[] permission = {"android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_SMS", "android.permission.WRITE_SMS"};
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permission, 1000);
        }

        // openSMSappChooser(getContext());
        circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
        setUpRecyclerView();

        handiCrafts = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/")
                .getReference(Constants.DATABASE_PATH_UPLOADS.concat("/").concat(categorie));
        getDataFromFirebase(databaseReference);


    }

    /**
     * This method to setup RecyclerView
     */
    private void setUpRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * This method is to get data from Firebase data
     */
    private void getDataFromFirebase(DatabaseReference databaseReference) {
        circleProgressBarLayout.setVisibility(View.VISIBLE);
        circleProgressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                circleProgressBarLayout.setVisibility(View.GONE);
                circleProgressBar.setVisibility(View.GONE);
                if (handiCrafts != null) {
                    handiCrafts.clear();
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HandiCraft handiCraft = postSnapshot.getValue(HandiCraft.class);

                    handiCrafts.add(handiCraft);
                }

                adapter = new ProductRecyclerAdapter(getActivity(), handiCrafts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * RecyclerView Item Decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); //item position
            int column = position % spanCount; //item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }

                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;

                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }

        }
    }


    /**
     * converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math
                .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
