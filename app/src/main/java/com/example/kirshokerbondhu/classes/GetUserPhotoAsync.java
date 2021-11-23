package com.example.kirshokerbondhu.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.kirshokerbondhu.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Objects;

public class GetUserPhotoAsync extends AsyncTask<Void, Void, Bitmap> {

    WeakReference<MainActivity> weakReference;
    Bitmap mIcon_val;
    FirebaseAuth mAuth;
    URL newurl = null;

    public GetUserPhotoAsync(MainActivity activity, FirebaseAuth mAuth) {
        weakReference = new WeakReference<>(activity);
        this.mAuth = mAuth;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        try {
            newurl = new URL(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString());
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mIcon_val;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        MainActivity mainActivity = weakReference.get();
        mainActivity.image_dp.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mainActivity.image_dp.setImageBitmap(mIcon_val);

    }
}