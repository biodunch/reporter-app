package com.biodunch.reporter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonTake, buttonUpload, buttonChoose, submit;
    EditText editText,editText2;
    ImageView imageView;
    Spinner spinnerLocation, spinnerCategory;
    String category,location;

    private BottomSheetBehavior bottomSheetBehavior;


    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    private Uri fileUri;

    TextView textView;
    ArrayAdapter adapterLocation, adapterCategory;

    View bottomSheet;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonChoose = (Button) this.findViewById(R.id.buttonChoose);
        buttonTake = (Button) this.findViewById(R.id.buttonTake);
        buttonUpload = (Button) this.findViewById(R.id.buttonUpload);


        editText = (EditText) this.findViewById(R.id.caption);
        imageView = (ImageView) this.findViewById(R.id.imageView);


        imageView.setOnClickListener(this);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonTake.setOnClickListener(this);


        //bottom

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheet);

        submit = (Button) bottomSheet.findViewById(R.id.button);
        submit.setOnClickListener(this);

        editText2 = (EditText) bottomSheet.findViewById(R.id.editText2);


        //Location Spinner
        adapterLocation = ArrayAdapter.createFromResource(this, R.array.locations, R.layout.simple_spinner);
       // adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation = (Spinner) bottomSheet.findViewById(R.id.spinnerLocation);
        spinnerLocation.setAdapter(adapterLocation);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = spinnerLocation.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"Location :"+location, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Category Spinner
        adapterCategory = ArrayAdapter.createFromResource(this, R.array.category,R.layout.simple_spinner);
       // adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory = (Spinner) bottomSheet.findViewById(R.id.spinnerCategory);
        spinnerCategory.setAdapter(adapterCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinnerCategory.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),"Category :"+category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChoose:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.imageView:
                Intent gallerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallerIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.buttonTake:
                captureImage();
                break;

            case R.id.buttonUpload:
                bottomSheetDialog.show();
                break;

            case R.id.button:
                String caption = editText.getText().toString();
                String name = editText.getText().toString();
                String image = encodeImage();
                Toast.makeText(this,"Hey "+editText2.getText(),Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private String encodeImage(){
        Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream boa = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,boa);
        String encoded = Base64.encodeToString(boa.toByteArray(),Base64.DEFAULT);

    return encoded;
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + "Android File Upload" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedData = data.getData();
            imageView.setImageURI(selectedData);
        }
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Uri capturedData = fileUri;
                imageView.setImageURI(capturedData);
                Log.d(TAG,"done");
            }

            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            }
        }
    }

}
