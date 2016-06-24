package com.jojodelivery.wardrobe.Presenters;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jojodelivery.wardrobe.R;

/**
 * Created by asus on 24-06-2016.
 */
public class ImageFragment extends Fragment {
    private static final String IMAGE_ID = "imageId";
    String imagePath;

    static ImageFragment init(String val) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_ID, val);
        imageFragment.setArguments(args);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imageId = getArguments() != null ? getArguments().getString(IMAGE_ID) : "0";
        imagePath = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+imageId+".jpg";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView layoutView = (ImageView) inflater.inflate(R.layout.cloth_image_holder, container,
                false);
        Glide.with(getContext())
                .load(imagePath)
                .into(layoutView);
        return layoutView;
    }

}
