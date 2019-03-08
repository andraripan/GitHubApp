package com.example.githubapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ItemVIewHolder> {

    private ArrayList<Repository> repositories;
    private Context context;


    public RepoAdapter(Context context, ArrayList<Repository> repos) {
        this.context = context;
        this.repositories = repos;

    }

    @NonNull
    @Override
    public ItemVIewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemVIewHolder holder;
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, viewGroup, false);
        holder = new ItemVIewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ItemVIewHolder holder, int i) {
        final Repository repository = repositories.get(i);

        holder.repoName.setText(repository.getName());
        holder.repoFullName.setText(repository.getFullName());
        holder.stars.setText(String.valueOf(repository.getStars()));
        holder.starImage.setImageResource(R.drawable.star);
        holder.details.setText("Details");

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", repository.getName());
                editor.putString("fullName", repository.getFullName());
                editor.putString("ownerName", repository.getOwner().getUsername());
                editor.putString("ownerImageUrl", repository.getOwner().getImageUrl());
                editor.putString("link", repository.getUrl());
                editor.putInt("forks", repository.getForks());
                editor.putInt("watchers", repository.getWatchers());
                editor.apply();
                context.startActivity(new Intent(context, DetailsActivity.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return repositories.size();
    }


    public class ItemVIewHolder extends RecyclerView.ViewHolder {

        private TextView repoName;
        private TextView repoFullName;
        private ImageView starImage;
        private TextView stars;
        private Button details;

        public ItemVIewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = (TextView) itemView.findViewById(R.id.name);
            repoFullName = (TextView) itemView.findViewById(R.id.fullName);
            starImage = (ImageView) itemView.findViewById(R.id.star_image);
            stars = (TextView) itemView.findViewById(R.id.stars);
            details = (Button) itemView.findViewById(R.id.details);

        }
    }
}
