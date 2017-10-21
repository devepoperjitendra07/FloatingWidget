package com.floatingwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    // Activity request codes
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "My Camera";
    public Uri fileUri; // file url to store image/video



    public String imagefrom="none";

    private static final int APP_PERMISSION_REQUEST = 102;
    Button apps;
    private PopupWindow myPopUp;
    CircleImageView profile_image;
    private int PICK_IMAGE_REQUEST = 10;
    static final int REQUEST_IMAGE_CAPTURE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_PERMISSION_REQUEST);
        }

        else {
            initializeView();
        }

    }
    private void initializeView() {

        Button mButton= (Button) findViewById(R.id.createBtn);
        apps = (Button) findViewById(R.id.button);
        profile_image= (CircleImageView) findViewById(R.id.profile_image);
        apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showApps = new Intent(MainActivity.this,RecyclerActivity.class);
                startActivity(showApps);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, FloatWidgetService.class);
                String fileuri1= String.valueOf(fileUri);
                intent.putExtra("fileuri",fileuri1);
                intent.putExtra("imagefrom",imagefrom);
                startService(intent);
                 finish();
            }
        });
    }

    public void showPopUp(View view){

        LayoutInflater layoutInflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView =layoutInflater.inflate(R.layout.popup_layout,null);
        Button camera_button = (Button)customView.findViewById(R.id.camera_btn);
        Button gallery_button = (Button) customView.findViewById(R.id.gallery_btn);

        myPopUp = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        myPopUp.setOutsideTouchable(true);

        // myPopUp.setElevation((float) 14.5);
        myPopUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        myPopUp.showAsDropDown(view);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // startActivityForResult(intent,0);
                // capture picture
               // captureImage();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE );


                myPopUp.dismiss();


            }
        });
        gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                myPopUp.dismiss();
            }
        });
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE );
    }



    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == APP_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            initializeView();
        } else {
            Toast.makeText(this, "Draw over other app permission not enable.", Toast.LENGTH_SHORT).show();
        }


       // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
       // profile_image.setImageBitmap(bitmap);



        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK && data!=null) {


            fileUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                // Log.d(TAG, String.valueOf(bitmap));

                Toast.makeText(getApplicationContext(),fileUri +"Gallery",Toast.LENGTH_LONG).show();

                profile_image.setImageBitmap(bm);

                imagefrom="gallery";
            } catch (IOException e) {

                e.printStackTrace();
            }
        }



        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {


            try {

                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                fileUri= Uri.parse(fileUri.getPath());



                Toast.makeText(getApplicationContext(),fileUri +"Camera",Toast.LENGTH_LONG).show();

                final Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(fileUri), options);

                profile_image.setImageBitmap(bitmap);

                imagefrom="camera";
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }



    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

//    private void getImage(Intent data,int requestCode,int resultCode) {
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        profile_image.setImageBitmap(bitmap);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//
//            try {
//                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                profile_image.setImageBitmap(bm);
//            } catch (IOException e) {
//                Toast.makeText(this, "Exception caught", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    }
public void dispatchTakePictureIntent(){
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
}

}
