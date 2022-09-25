package com.strontech.imgautam.handycaft.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.strontech.imgautam.handycaft.ProductFragments.ProductDescFragment;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.model.HandiCraft;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends
        RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context context;
    private List<String> list;



    public CategoriesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view, list, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.name.setText(list.get(position));


/*
        HandiCraft handiCraft = handiCrafts.get(position);
        holder.textViewProductName.setText(handiCraft.getProduct_name());
        holder.textViewProductSP.setText("RM" + handiCraft.getProduct_sp());
        holder.textViewProductMRP.setText("RM" + handiCraft.getProduct_mrp());
        holder.textViewProductDiscount.setText(handiCraft.getProduct_discount() + "% off");
        strikeThroughText(holder.textViewProductMRP);
        Glide.with(context).load(handiCraft.getProduct_image()).into(holder.imageViewProductImage);
        */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {


        TextView name;
        LinearLayout lincolor;

        List<HandiCraft> handiCraftsList = new ArrayList<HandiCraft>();

        HomeFragment homeFragment;
        Context context;


        public ViewHolder(View itemView, List<String> list, Context context) {
            super(itemView);

           // this.handiCraftsList = handiCraftsList;
            this.context = context;
            itemView.setOnClickListener(this);
            homeFragment = new HomeFragment();

            name = itemView.findViewById(R.id.name);

            lincolor = itemView.findViewById(R.id.lincolor);


        }


        /**
         * This implemented method is to listen the click on view
         *
         * @param v to get View id
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            Bundle b = new Bundle();

            b.putString("categorie", list.get(position));

            homeFragment.setArguments(b);

            AppCompatActivity activity = (AppCompatActivity) v.getContext();


            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, homeFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    /**
     * This method strike through text
     *
     * @param price of product
     */
    private void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
