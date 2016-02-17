package com.csc.lesson1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private BitmapDrawable image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageDownloader downloader = new ImageDownloader();
        downloader.execute();
    }

    void setImage(Bitmap bitmap) {
        image = new BitmapDrawable(getResources(), bitmap);
    }

    private void updateView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(image);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.topMargin += height * 0.1;
        imageView.setLayoutParams(layoutParams);

        mainLayout.addView(imageView);

        TextView textView = new TextView(this);
        textView.setText(R.string.name);
        textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textLayoutParams.weight = 2;
        textLayoutParams.bottomMargin += height * 0.1;
        textView.setLayoutParams(textLayoutParams);
        textView.setTextSize(height * 0.02f);
        mainLayout.addView(textView);

        mainLayout.setGravity(Gravity.CENTER);
        setContentView(mainLayout);
    }

    private class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL url = new URL(getString(R.string.imageURL));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            setImage(result);
            updateView();
        }
    }
}
