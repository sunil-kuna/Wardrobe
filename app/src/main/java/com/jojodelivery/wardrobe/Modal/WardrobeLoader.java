package com.jojodelivery.wardrobe.Modal;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.jojodelivery.wardrobe.DataDefitions.Constants;

/**
 * Created by asus on 24-06-2016.
 */
public class WardrobeLoader extends AsyncTaskLoader<Bundle> {
    Bundle bundle;
    Context context;
    String method;
    public WardrobeLoader(Context context,String method, Bundle bundle) {
        super(context);
        this.context = context;
        this.bundle = bundle;
        this.method = method;
    }

    @Override
    public Bundle loadInBackground() {
        return context.getContentResolver().call(WardrobeContentProvider.CONTENT_URI_BASE_URI, method,null,bundle);
    }
}
