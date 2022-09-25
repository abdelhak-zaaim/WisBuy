package com.strontech.imgautam.handycaft.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;

import java.util.ArrayList;
import java.util.List;

public class ProductWishRecyclerAdapter extends RecyclerView.Adapter<ProductWishRecyclerAdapter.ViewHolder> {


    private Context context;
    private List<CartHandiCraft> cartHandiCraftList;


    /**
     * This is constructor
     *
     * @param context            context reference
     * @param cartHandiCraftList list of CartHandiCraft model class
     */
    public ProductWishRecyclerAdapter(Context context, List<CartHandiCraft> cartHandiCraftList) {
        this.context = context;
        this.cartHandiCraftList = cartHandiCraftList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_wish_list_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, cartHandiCraftList, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartHandiCraft cartHandiCraft = cartHandiCraftList.get(position);

        holder.textViewProductName.setText(cartHandiCraft.getProduct_name());
        holder.textViewProductResellerName.setText(cartHandiCraft.getProduct_reseller_name());
        holder.textViewProductStock.setText("Only " + cartHandiCraft.getProduct_quantity() + " are in stock");
        holder.textViewProductTotalAmt.setText("RM" + cartHandiCraft.getProduct_sp());
        holder.textInputEditTextProductMRP.setText("RM" + cartHandiCraft.getProduct_mrp());
        holder.textViewProductDiscount.setText(cartHandiCraft.getProduct_discount() + " Off");

        Glide.with(context).load(cartHandiCraft.getProduct_image()).into(holder.imageViewProductImage);
    }

    @Override
    public int getItemCount() {
        return cartHandiCraftList.size();
    }


    /**
     * This is ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView imageViewProductImage;
        TextView textViewProductName;
        TextView textViewProductResellerName;
        TextView textViewProductStock;
        TextView textViewProductTotalAmt;
        TextView textInputEditTextProductMRP;
        TextView textViewProductDiscount;

        Button buttonMoveToWishListCartItem;
        Button buttonRemoveWishItem;

        List<CartHandiCraft> cartHandiCraftList = new ArrayList<CartHandiCraft>();
        Context context;
        DatabaseReference databaseReferenceCart, databaseReferenceWish;

        String product_idd;
        String product_image;
        String product_name;
        String product_reseller_name;
        String product_mrp;
        String product_sp;
        String product_quantity;
        String product_discount;
        String countQuantity;
        String product_highlight;
        String product_desc;


        /**
         * This is constructor
         *
         * @param itemView           to get id of views
         * @param cartHandiCraftList list of CartHandiCraft
         * @param context            reference of Context
         */
        public ViewHolder(View itemView, List<CartHandiCraft> cartHandiCraftList, Context context) {
            super(itemView);
            this.cartHandiCraftList = cartHandiCraftList;
            this.context = context;

            imageViewProductImage = itemView.findViewById(R.id.imageViewProductImage);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductResellerName = itemView.findViewById(R.id.textViewProductResellerName);
            textViewProductStock = itemView.findViewById(R.id.textViewProductStock);
            textViewProductTotalAmt = itemView.findViewById(R.id.textViewProductTotalAmt);
            textInputEditTextProductMRP = itemView.findViewById(R.id.textInputEditTextProductMRP);
            textViewProductDiscount = itemView.findViewById(R.id.textViewProductDiscount);

            buttonMoveToWishListCartItem = itemView.findViewById(R.id.buttonMoveToWishListCartItem);
            buttonRemoveWishItem = itemView.findViewById(R.id.buttonRemoveWishItem);

            buttonMoveToWishListCartItem.setOnClickListener(this);
            buttonRemoveWishItem.setOnClickListener(this);

            databaseReferenceCart = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Cart Items");
            databaseReferenceWish = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Wish Item");
        }


        /**
         * This implemented method is to listen the click on view
         *
         * @param v to get View id
         */
        @Override
        public void onClick(View v) {

            final int position = getAdapterPosition();
            CartHandiCraft cartHandiCraft = this.cartHandiCraftList.get(position);

            product_idd = cartHandiCraft.getProduct_id();
            product_image = cartHandiCraft.getProduct_image();
            product_name = cartHandiCraft.getProduct_name();
            product_reseller_name = cartHandiCraft.getProduct_reseller_name();
            product_mrp = cartHandiCraft.getProduct_mrp();
            product_sp = cartHandiCraft.getProduct_sp();
            product_quantity = cartHandiCraft.getProduct_quantity();
            product_discount = cartHandiCraft.getProduct_discount();
            countQuantity = cartHandiCraft.getProduct_spinner_pos();
            product_highlight = cartHandiCraft.getProduct_highlight();
            product_desc = cartHandiCraft.getProduct_desc();


            if (v.getId() == buttonMoveToWishListCartItem.getId()) {

                CartHandiCraft cartHandiCraftWish = new CartHandiCraft();
                cartHandiCraftWish.setProduct_id(cartHandiCraft.getProduct_id());
                cartHandiCraftWish.setProduct_image(cartHandiCraft.getProduct_image());
                cartHandiCraftWish.setProduct_name(cartHandiCraft.getProduct_name());
                cartHandiCraftWish.setProduct_reseller_name(cartHandiCraft.getProduct_reseller_name());
                cartHandiCraftWish.setProduct_sp(cartHandiCraft.getProduct_sp());
                cartHandiCraftWish.setProduct_mrp(cartHandiCraft.getProduct_mrp());
                cartHandiCraftWish.setProduct_quantity(cartHandiCraft.getProduct_quantity());
                cartHandiCraftWish.setProduct_discount(cartHandiCraft.getProduct_discount());
                cartHandiCraftWish.setProduct_highlight(cartHandiCraft.getProduct_highlight());
                cartHandiCraftWish.setProduct_desc(cartHandiCraft.getProduct_desc());

                databaseReferenceCart.child(cartHandiCraft.getProduct_id()).setValue(cartHandiCraft);
                databaseReferenceWish.child(cartHandiCraft.getProduct_id()).removeValue();

            } else if (v.getId() == buttonRemoveWishItem.getId()) {

                databaseReferenceWish.child(cartHandiCraft.getProduct_id()).removeValue();
            }

        }
    }
}
