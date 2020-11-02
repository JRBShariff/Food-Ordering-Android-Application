package com.shariff.mealsordering;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;

    public ProductAdapterAdmin(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_item_admin,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.tv_name.setText(uploadCurrent.getName());
        holder.tv_price.setText(uploadCurrent.getPrice());
        holder.tv_cat.setText(uploadCurrent.getCategory());
        Glide.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.defaultimg)
                .centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private TextView tv_name,tv_price,tv_cat;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tv_name= itemView.findViewById(R.id.pr_name);
            imageView= itemView.findViewById(R.id.pr_image);
            tv_price= itemView.findViewById(R.id.pr_price);
            tv_cat= itemView.findViewById(R.id.pr_cat);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                //Get the position of the clicked item
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
        // Handle Menu Items
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1,"Do Whatever");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                //Get the position of the clicked item
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setSearchOperation(List<Upload> newList){
        mUploads = new ArrayList<>();
        mUploads.addAll(newList);
        notifyDataSetChanged();
    }
}
/*

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public ProductAdapter (Context context,List<Upload> uploads){
        mContext=context;
        mUploads=uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.tv_name.setText(uploadCurrent.getName());
        holder.tv_price.setText(uploadCurrent.getPrice()+" TZS");
        holder.tv_cat.setText(uploadCurrent.getCategory());
        Glide.with(mContext).load(uploadCurrent.getImageUrl()).fitCenter
                ().into(holder.imageView);
        //Picasso.get().load(uploadCurrent.getmImageUrl()).fit().centerCrop().into(holder.imageView);
       // Picasso.with(mContext).load(uploadCurrent.getmImageUrl()).fit().centerCrop().into(holder.imageView);
       // holder.imageView.setImageURI(uploadCurrent.getmImageUrl());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name,tv_price,tv_cat;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name= itemView.findViewById(R.id.pr_name);
            imageView= itemView.findViewById(R.id.pr_image);
            tv_price= itemView.findViewById(R.id.pr_price);
            tv_cat= itemView.findViewById(R.id.pr_cat);

        }
    }
}*/


