package com.example.rview_compress_images.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rview_compress_images.R;

import java.io.File;
import java.util.ArrayList;


public class BookingsStatus  extends RecyclerView.Adapter<BookingsStatus.vholder>
{

    Context context;
    ArrayList<String> totalGalleryImagesList = new ArrayList<>();

    public BookingsStatus(Context context, ArrayList<String> totalGalleryImagesList)
    {
        this.context = context;
        this.totalGalleryImagesList = totalGalleryImagesList;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item, parent, false);
        vholder vh = new vholder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final vholder holder, final int position)
    {
        Uri imgUri= Uri.parse(totalGalleryImagesList.get(position));
        holder.gimg.setImageURI(imgUri);
        holder.gimg.setClipToOutline(true);

        holder.gimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, totalGalleryImagesList.get(holder.getAdapterPosition()));
                intent.setType("images/*");
                String imagePath =totalGalleryImagesList.get(holder.getAdapterPosition());
                File imageFileToShare = new File(imagePath);
                Uri uri = Uri.fromFile(imageFileToShare);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(intent, "Share Image!"));
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return totalGalleryImagesList.size();
    }

    public class vholder extends RecyclerView.ViewHolder
    {
        ImageView gimg;

        public vholder(@NonNull View itemView)
        {
            super(itemView);

             gimg = (ImageView) itemView.findViewById(R.id.gimg);
        }
    }
}
