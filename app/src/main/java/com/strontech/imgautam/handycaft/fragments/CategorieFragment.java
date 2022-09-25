package com.strontech.imgautam.handycaft.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ImagePagerAdapter;
import com.strontech.imgautam.handycaft.adapters.ProductRecyclerAdapter;
import com.strontech.imgautam.handycaft.helper.Constants;
import com.strontech.imgautam.handycaft.model.HandiCraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategorieFragment extends Fragment {
    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    String url2 = "https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp";
    String url3 = "https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png";
    ViewPager viewPager;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager RecyclerViewLayoutManager_larest;
    LinearLayoutManager HorizontalLayout;
    LinearLayoutManager HorizontalLayout_latest;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference_slider;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_latest;
    private RecyclerView.Adapter adapter, adapter_latest;


    private List<HandiCraft> handiCrafts_s;
    private List<HandiCraft> handiCrafts;

    private List<HandiCraft> handiCrafts_s_latest;
    private List<HandiCraft> handiCrafts_latest;

    private LinearLayout cat1, cat2, cat3, cat4;


    private DatabaseReference databaseReference;

    private CardView kids_card, electronics_card, laptop_card, other_card;


    View view;

    public CategorieFragment() {
        // Required empty public constructor
    }

    private void setUpRecyclerView() {

        recyclerView.addItemDecoration(new CategorieFragment.GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView_latest.addItemDecoration(new CategorieFragment.GridSpacingItemDecoration(2, dpToPx(5), true));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView_latest.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getContext());
        RecyclerViewLayoutManager_larest
                = new LinearLayoutManager(
                getContext());


        recyclerView.setLayoutManager(
                RecyclerViewLayoutManager);
        recyclerView_latest.setLayoutManager(
                RecyclerViewLayoutManager_larest);

        HorizontalLayout
                = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        HorizontalLayout_latest
                = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView_latest.setLayoutManager(HorizontalLayout_latest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_categorie, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_v);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        db = FirebaseFirestore.getInstance();
        kids_card = view.findViewById(R.id.kids_card);
        electronics_card = view.findViewById(R.id.electronics_card);
        laptop_card = view.findViewById(R.id.laptop_cart);
        other_card = view.findViewById(R.id.other_card);
        recyclerView_latest = view.findViewById(R.id.latest_rececler);

        initViews();
        initObjects();
        setUpRecyclerView();
        handiCrafts_s = new ArrayList<>();
        handiCrafts = new ArrayList<>();

        handiCrafts_s_latest = new ArrayList<>();
        handiCrafts_latest = new ArrayList<>();


        databaseReference_slider = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/")
                .getReference(Constants.DATABASE_PATH_UPLOADS.concat("/slider"));
        getDataFromFirebase(databaseReference_slider);
        getSlider(databaseReference_slider);
        databaseReference = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/")
                .getReference(Constants.DATABASE_PATH_UPLOADS.concat("/latest"));
        getDataFromFirebase(databaseReference);
        getDataFromFirebase_recicler(databaseReference);
        return view;
    }


    private void getDataFromFirebase_recicler(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (handiCrafts_s_latest != null) {
                    handiCrafts_s_latest.clear();
                }
                if (handiCrafts_latest != null) {
                    handiCrafts_latest.clear();
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HandiCraft handiCraft = postSnapshot.getValue(HandiCraft.class);

                    handiCrafts_s_latest.add(handiCraft);
                    // Collections.reverse(handiCrafts_s);


                }
                for (int i = 0; i < handiCrafts_s_latest.size() && i <= 5; i++) {
                    handiCrafts_latest.add(handiCrafts_s_latest.get(i));
                }

                adapter_latest = new ProductRecyclerAdapter(getActivity(), handiCrafts_latest);
                recyclerView_latest.setAdapter(adapter_latest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromFirebase(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (handiCrafts_s != null) {
                    handiCrafts_s.clear();
                }
                if (handiCrafts_s != null) {
                    handiCrafts.clear();
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HandiCraft handiCraft = postSnapshot.getValue(HandiCraft.class);

                    handiCrafts_s.add(handiCraft);
                    Collections.reverse(handiCrafts_s);


                }
                for (int i = 0; i < handiCrafts_s.size() && i <= 5; i++) {
                    handiCrafts.add(handiCrafts_s.get(i));
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
     * This method is to initialize views
     */
    private void initViews() {
        sharedPreferences = getActivity().getSharedPreferences("category", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cat1 = view.findViewById(R.id.cat1);
        cat2 = view.findViewById(R.id.cat2);
        cat3 = view.findViewById(R.id.cat3);
        cat4 = view.findViewById(R.id.cat4);
        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat1");
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat2");
            }
        });
        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat3");
            }
        });
        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat4");
            }
        });
        electronics_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat1");
            }
        });
        kids_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat2");
            }
        });
        laptop_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat3");
            }
        });
        other_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory("cat4");
            }
        });
    }


    /**
     * This method is to initialize objects
     */
    @Override
    // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1000) {
            return;
        }
        if (grantResults[0] == 0) {
            Toast.makeText(getContext(), "Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Not granted", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }


    private void initObjects() {
        String[] permission = {"android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_SMS", "android.permission.WRITE_SMS"};
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permission, 1000);
        }

        // openSMSappChooser(getContext());


    }

    /**
     * This method to setup RecyclerView
     */


    /**
     * This method is to get data from Firebase data
     */


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


    public void selectCategory(String catName) {

        editor.putString("category", catName);

        editor.commit();

        AppCompatActivity activity = (AppCompatActivity) view.getContext();


        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < 3 - 1) {

                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
        }
    }

    private void getSlider(DatabaseReference databaseReference) {
        ArrayList<String> strt = new ArrayList<>();
        db.collection("slider").document("slider")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (!strt.isEmpty()) {
                            strt.clear();
                        }
                        if (value != null) {

                            strt.add((String) value.get("img1"));
                            strt.add((String) value.get("img2"));
                            strt.add((String) value.get("img3"));

                            viewPager.setAdapter(new ImagePagerAdapter(getContext(), strt));

                            Timer timer = new Timer();
                            timer.scheduleAtFixedRate(new SliderTimer(), 6000, 10000);

                        }


                    }
                });


    }
}
