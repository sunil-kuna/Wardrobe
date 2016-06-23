package com.jojodelivery.wardrobe.Presenters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jojodelivery.wardrobe.DataDefitions.Cloth;
import com.jojodelivery.wardrobe.DataDefitions.Constants;
import com.jojodelivery.wardrobe.Modal.WardrobeLoader;
import com.jojodelivery.wardrobe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSelector extends AppCompatActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Bundle>{

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private static final int LOADER_ID = -1;
    private String userChoosenTask = "";
    private ImageView ivImage;
    Bitmap finalBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        selectImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ok:
                if(finalBitmap == null)
                {
                    Toast.makeText(this,"Bitmap is Null",Toast.LENGTH_LONG).show();
                    return;
                }
                Cloth cloth = new Cloth();
                cloth.setImage(finalBitmap);
                //cloth.setType(getIntent().getExtras().getString(Constants.CLOTH_TYPE, Constants.CLOTH_SHIRT));
                cloth.setType(Constants.CLOTH_SHIRT);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.CLOTH,cloth);
                getLoaderManager().initLoader(LOADER_ID, bundle, ImageSelector.this).forceLoad();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new WardrobeLoader(this, Constants.INSERT_IMAGE,args);
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
        if (data.getString(Constants.RESULT).equals(Constants.RESULT_OK)) {
            Toast.makeText(this,"SuccessFully added Image",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"Unable to add Image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(ImageSelector.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                    Toast.makeText(this,"This Permission needs to be granted , Please enable this permision in the Settings of the app ",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
        finalBitmap = bm;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
        finalBitmap = thumbnail;
    }
}
