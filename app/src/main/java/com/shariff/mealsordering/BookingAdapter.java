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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Booking> mUploads;
    private OnItemClickListener mListener;
    private DatabaseReference mDatabaseRef;
    private String pName,pPrice;



    public BookingAdapter(Context mContext, List<Booking> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }




    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.booking_card_view,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        Booking currentBookig = mUploads.get(position);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods").child(currentBookig.getProductId());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Upload post = snapshot.getValue(Upload.class);
                Integer tPrice=(Integer.parseInt(currentBookig.getQuantity())*Integer.parseInt(post.getPrice().toString()));
                holder.o_ID.setText(post.getName().toString());
                holder.o_price.setText(""+tPrice+" TZS");
                holder.o_address.setText("Address: "+currentBookig.getAddress());
                holder.o_email.setText(currentBookig.getEmail());
                holder.o_status.setText(currentBookig.getStatus());
                holder.o_quantity.setText(currentBookig.getQuantity());
                holder.o_phone.setText(currentBookig.getPhone());
                holder.o_date.setText(currentBookig.getDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "cc"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        //Glide.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.defaultimg)
          //      .centerInside().into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private TextView o_address,o_date,o_email,o_phone,o_quantity,o_status,o_ID,o_price;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            o_ID= itemView.findViewById(R.id.o_id);
            o_price= itemView.findViewById(R.id.o_price);
            o_address= itemView.findViewById(R.id.o_address);
            o_date= itemView.findViewById(R.id.o_date);
            o_email= itemView.findViewById(R.id.o_email);
            o_phone= itemView.findViewById(R.id.o_phone);
            o_quantity= itemView.findViewById(R.id.o_quatity);
            o_status= itemView.findViewById(R.id.o_status);


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
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1,"Edit");
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

    public void setSearchOperation(List<Booking> newList){
        mUploads = new ArrayList<>();
        mUploads.addAll(newList);
        notifyDataSetChanged();
    }


}
