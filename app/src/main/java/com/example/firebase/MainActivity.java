package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

class CustomItems {
    String url;

    public CustomItems(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Context mContext;
    List<Model> mImages;

    public ImageAdapter(Context context, List<Model> images) {
        this.mContext = context;
        this.mImages = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_layout, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Model image = mImages.get(position);
        Picasso.get().load(image.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ViewActivity.class);
            intent.putExtra("images", mImages.get(getAdapterPosition()).getUrl());
            mContext.startActivity(intent);
        }
    }
}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {
    List<CustomItems> itemsList;
    Context context;

    public RecyclerViewAdapter(List<CustomItems> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CustomItems item = itemsList.get(position);
        Picasso.get().load(item.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("images", itemsList.get(getAdapterPosition()).getUrl());
            context.startActivity(intent);
        }
    }
}

public class RenklerActivity extends AppCompatActivity {

    RecyclerView recyclerView_renkler;
    ImageAdapter imageAdapter;
    List<Model> images;
    DatabaseReference databaseReference;
    Toolbar toolbar_renkler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renkler);

        toolbar_renkler = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_renkler);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView_renkler = findViewById(R.id.recyclerView_renkler);
        recyclerView_renkler.setHasFixedSize(true);
        recyclerView_renkler.setLayoutManager(new GridLayoutManager(this, 2));

        images = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Renkler");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model image = postSnapshot.getValue(Model.class);
                    images.add(image);
                }
                imageAdapter = new ImageAdapter(RenklerActivity.this, images);
                recyclerView_renkler.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
