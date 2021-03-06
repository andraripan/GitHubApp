package com.example.githubapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView avatar;
    private TextView name;
    private TextView fullName;
    private TextView owner;
    private TextView link;
    private TextView forks;
    private TextView watchers;
    private Button readMe;
    private Button backBtn;

    private String repoName;
    private String repoFullName;
    private String repoOwner;
    public static String repoLink;
    private int repoForks;
    private int repoWathchers;
    private String ownerImageUrl;
    private String readMeUrl;

    private String requestUrl;
    private String responseData = "";

    private Dialog dialog;
    private WebView webView;
    private Button close;
    private Button goToDropBox;
    private Button loginDropbox;

    private String READ_ME_DOMAIN = "https://api.github.com/repos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        avatar = (ImageView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        fullName = (TextView) findViewById(R.id.fullName);
        owner = (TextView) findViewById(R.id.owner);
        link = (TextView) findViewById(R.id.link);
        forks = (TextView) findViewById(R.id.forksCount);
        watchers = (TextView) findViewById(R.id.watchersCount);
        readMe = (Button) findViewById(R.id.readmeBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        goToDropBox = (Button) findViewById(R.id.goToDropBox);
        goToDropBox.setOnClickListener(this);


        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        repoName = preferences.getString("name", "");
        repoFullName = preferences.getString("fullName", "");
        repoOwner = preferences.getString("ownerName", "");
        repoLink = preferences.getString("link", "");
        repoForks = preferences.getInt("forks", 0);
        repoWathchers = preferences.getInt("watchers", 0);
        ownerImageUrl = preferences.getString("ownerImageUrl", "");


        if (avatar != null) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .load(ownerImageUrl)
                    .apply(options)
                    .into(avatar);
        }

        name.setText(repoName);
        fullName.setText(repoFullName);
        owner.setText(repoOwner);
        link.setText(repoLink);
        forks.setText(String.valueOf(repoForks));
        watchers.setText(String.valueOf(repoWathchers));

        backBtn.setOnClickListener(this);
        readMe.setOnClickListener(this);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.read_me_dialog);
        dialog.setCancelable(true);

        close = (Button) dialog.findViewById(R.id.bt_close);
        close.setOnClickListener(this);

        webView = (WebView) dialog.findViewById(R.id.wb_webview);
        webView.setScrollbarFadingEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.clearCache(true);

        new MyAsyncTask().execute();

        Fidel.apiKey = "pk_test_4f4e567e-2d77-405a-8c1b-a51a26ad0330";
        Fidel.programId = "1a74a811-575d-4e75-bffd-acfa99e7fd7e";
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.readmeBtn:
                dialog.show();
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.bt_close:
                if(dialog != null){
                    dialog.dismiss();
                }
                break;
            case R.id.goToDropBox:
                Fidel.present(DetailsActivity.this);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Fidel.FIDEL_LINK_CARD_REQUEST_CODE){
            if(data != null && data.hasExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD)){
                LinkResult card = (LinkResult) data.getParcelableExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD);
                LinkResultError error = card.getError();
                if (error != null) {
                    Toast.makeText(this, "Request failed!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Card added successfully!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(READ_ME_DOMAIN+repoOwner+"/"+repoName+"/readme").newBuilder();
            urlBuilder.addQueryParameter("ref", "master");

            requestUrl = urlBuilder.build().toString();
            String readMeUrl = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(requestUrl).build();
                Response response = client.newCall(request).execute();
                responseData = response.body().string();
                JSONObject object = new JSONObject(responseData);

                     readMeUrl = object.getString("html_url");

            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return readMeUrl;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            readMeUrl = url;
            webView.loadUrl(readMeUrl);
        }
    }
}
