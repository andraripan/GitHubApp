package com.example.githubapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView repoRecyclerView;
    private Button retryButton;
    private RepoAdapter adapter;
    private ArrayList<Repository> repositories = new ArrayList<>();
    private String responseData = "";
    private String DOMAIN = "https://api.github.com/search/repositories?";
    private String requestUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repoRecyclerView = (RecyclerView) findViewById(R.id.repo_rv);
        repoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        retryButton = (Button) findViewById(R.id.retryBtn);
        retryButton.setOnClickListener(this);

        adapter = new RepoAdapter(this, repositories);
        repoRecyclerView.setAdapter(adapter);

        if(isNetworkConnected()) {
            new MyAsyncTask().execute();
            retryButton.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
            retryButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.details:
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.retryBtn:
                if(isNetworkConnected()) {
                    new MyAsyncTask().execute();
                    retryButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
                    retryButton.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<Repository>>{

        ProgressDialog progDailog = new ProgressDialog(MainActivity.this);

        @Override
        protected ArrayList<Repository> doInBackground(Void... voids) {

            ArrayList<Repository> localRepos = new ArrayList<>();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(DOMAIN).newBuilder();
            urlBuilder.addQueryParameter("q", "android");
            urlBuilder.addQueryParameter("sort", "stars");
            urlBuilder.addQueryParameter("order", "desc");
            urlBuilder.addQueryParameter("per_page", "100");

            requestUrl = urlBuilder.build().toString();
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(requestUrl).build();
                Response response = client.newCall(request).execute();
                responseData = response.body().string();
                JSONObject objData = new JSONObject(responseData);
                JSONArray array = objData.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String name = object.getString("name");
                    String fullName = object.getString("full_name");
                    int stars = object.getInt("stargazers_count");
                    int forks = object.getInt("forks_count");
                    int watchers = object.getInt("watchers_count");
                    JSONObject objOwner = object.getJSONObject("owner");
                    String link = object.getString("html_url");

                    String username = objOwner.getString("login");
                    String avatarUrl = objOwner.getString("avatar_url");

                    Owner owner = new Owner(username, avatarUrl);

                    Repository repository = new Repository(name, fullName, stars, forks, watchers, owner, link);
                    localRepos.add(repository);
                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return localRepos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Repository> repos) {
            super.onPostExecute(repos);

            repositories.addAll(repos);
            adapter.notifyDataSetChanged();
            progDailog.dismiss();
        }
    }
}
