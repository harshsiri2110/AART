package com.example.aart;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostPreviewRecyclerAdapter extends RecyclerView.Adapter<PostPreviewRecyclerAdapter.ViewHolder> {

    private List<Uri> imageUrls = new ArrayList<Uri>();

    Context mcontext;

    public PostPreviewRecyclerAdapter(Context context, List<Uri> imageUrls) {
        this.imageUrls = imageUrls;
        mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_preview_image, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Uri currImgUrl = imageUrls.get(position);
        holder.imageView.setImageURI(currImgUrl);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.post_preview_image);

        }
    }


}
