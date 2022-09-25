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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;

import java.util.ArrayList;
import java.util.List;

public class ProductCartRecyclerAdapter extends
        RecyclerView.Adapter<ProductCartRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<CartHandiCraft> cartHandiCrafts;


    /**
     * This is constructor
     *
     * @param context         context reference
     * @param cartHandiCrafts list of CartHandiCraft model class
     */
    public ProductCartRecyclerAdapter(Context context, List<CartHandiCraft> cartHandiCrafts) {
        this.context = context;
        this.cartHandiCrafts = cartHandiCrafts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cart_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(v, cartHandiCrafts, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartHandiCraft cartHandiCraft = cartHandiCrafts.get(position);

        holder.textViewProductName.setText(cartHandiCraft.getProduct_name());
        holder.textViewProductResellerName.setText("Sold by " + cartHandiCraft.getProduct_reseller_name());
        holder.textViewProductStock.setText("Only " + cartHandiCraft.getProduct_quantity() + " are in stock");
        holder.textViewProductTotalAmt.setText("RM" + cartHandiCraft.getProduct_sp());
        holder.textViewProductDiscount.setText(cartHandiCraft.getProduct_discount() + " Off");
        holder.textViewProductQuantity.setText(cartHandiCraft.getProduct_spinner_pos());

        Glide.with(context).load(cartHandiCraft.getProduct_image()).into(holder.imageViewProductImage);

    }


    @Override
    public int getItemCount() {
        return cartHandiCrafts.size();
    }


    /**
     * This is Inner Class For Adapter ViewHolder Class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        int initialProductPrice;
        ImageView imageViewProductImage;

        TextView textViewProductName;
        TextView textViewProductResellerName;
        TextView textViewProductStock;
        TextView textViewProductTotalAmt;
        TextView textViewProductDiscount;

        Button buttonProductQuantityMinus;
        Button buttonProductQuantityPlus;
        TextView textViewProductQuantity;

        Button buttonRemoveCartItem;
        Button buttonMoveToWishListCartItem;

        //update price
        DatabaseReference databaseReferenceCart, databaseReferenceInitial;
        List<CartHandiCraft> cartHandiCraftList = new ArrayList<CartHandiCraft>();
        Context context;

        String product_idd;
        String product_image;
        String product_name;
        String product_reseller_name;
        String product_mrp;
        String product_sp;
        String product_quantity;
        String product_discount;
        String product_highlight;
        String product_desc;

        List<String> stringList;

        int countQuantity;
        int totalAmount;

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
            textViewProductDiscount = itemView.findViewById(R.id.textViewProductDiscount);


            buttonProductQuantityMinus = itemView.findViewById(R.id.buttonProductQuantityMinus);
            buttonProductQuantityPlus = itemView.findViewById(R.id.buttonProductQuantityPlus);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);

            buttonRemoveCartItem = itemView.findViewById(R.id.buttonRemoveCartItem);
            buttonMoveToWishListCartItem = itemView.findViewById(R.id.buttonMoveToWishListCartItem);

            buttonMoveToWishListCartItem.setOnClickListener(this);
            buttonRemoveCartItem.setOnClickListener(this);
            buttonProductQuantityPlus.setOnClickListener(this);
            buttonProductQuantityMinus.setOnClickListener(this);

            stringList = new ArrayList<String>();
            databaseReferenceCart = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Cart Items");
            databaseReferenceInitial = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("uploads");

        }


        /**
         * this implemented method is to listen the click on view
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
            initialProductPrice=Integer.valueOf(cartHandiCraft.getProduct_mrp());
            countQuantity = Integer.parseInt(cartHandiCraft.getProduct_spinner_pos());
            product_highlight = cartHandiCraft.getProduct_highlight();
            product_desc = cartHandiCraft.getProduct_desc();

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/")
                    .getReference("Cart Items").child(cartHandiCraft.getProduct_id());
            if (v.getId() == buttonRemoveCartItem.getId()) {

                databaseReference1.removeValue();


            } else if (v.getId() == buttonMoveToWishListCartItem.getId()) {

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

                DatabaseReference databaseReferenceWish = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/")
                        .getReference("Wish Item")
                        .child(cartHandiCraft.getProduct_id());

                databaseReferenceWish.setValue(cartHandiCraftWish);
                databaseReference1.removeValue();

            } else if (v.getId() == buttonProductQuantityPlus.getId()) {

                if (countQuantity < Integer.valueOf(product_quantity)) {

                    countQuantity++;
                    System.out.println(countQuantity);
                    textViewProductQuantity.setText("" + countQuantity);
                    totalAmount = initialProductPrice * countQuantity;
                    textViewProductTotalAmt.setText("RM" + totalAmount);

                    updatePrice(product_idd, String.valueOf(totalAmount), product_image, product_name,
                            product_reseller_name, product_mrp, product_quantity, product_discount, String.valueOf(countQuantity), product_highlight,
                            product_desc);
                } else {
                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                }

            } else if (v.getId() == buttonProductQuantityMinus.getId()) {
                // initialProductPrice = Integer.parseInt(stringList.get(9));
                if (countQuantity > 1) {
                    countQuantity--;
                    textViewProductQuantity.setText("" + countQuantity);
                    totalAmount = initialProductPrice * countQuantity;
                    textViewProductTotalAmt.setText("RM" + totalAmount);


                  updatePrice(product_idd, String.valueOf(totalAmount), product_image, product_name,
                            product_reseller_name, product_mrp, product_quantity, product_discount, String.valueOf(countQuantity), product_highlight,
                            product_desc);
                } else {
                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                }
            }

        }


        /**
         * This method updates price and set items
         *
         * @param id                    of product
         * @param price                 of product
         * @param product_image         of product
         * @param product_name          of product
         * @param product_reseller_name of product
         * @param product_mrp           of product
         * @param product_quantity      of product
         * @param product_discount      of product
         * @param product_spinner_pos   of product
         * @param product_highlight     of product
         * @param product_desc          of product
         */
        private void updatePrice(String id, String price, String product_image, String product_name,
                                 String product_reseller_name,
                                 String product_mrp,
                                 String product_quantity,
                                 String product_discount,
                                 String product_spinner_pos,
                                 String product_highlight,
                                 String product_desc) {

            CartHandiCraft cartHandiCraft = new CartHandiCraft();
            cartHandiCraft.setProduct_id(id);
            cartHandiCraft.setProduct_sp(price);
            cartHandiCraft.setProduct_image(product_image);
            cartHandiCraft.setProduct_name(product_name);
            cartHandiCraft.setProduct_reseller_name(product_reseller_name);
            cartHandiCraft.setProduct_mrp(product_mrp);
            cartHandiCraft.setProduct_quantity(product_quantity);
            cartHandiCraft.setProduct_discount(product_discount);
            cartHandiCraft.setProduct_spinner_pos(product_spinner_pos);
            cartHandiCraft.setProduct_highlight(product_highlight);
            cartHandiCraft.setProduct_desc(product_desc);

            DatabaseReference df = FirebaseDatabase.getInstance("https://wisybuy-default-rtdb.firebaseio.com/").getReference("Cart Items").child(id);
            df.setValue(cartHandiCraft);

        }
    }
}
