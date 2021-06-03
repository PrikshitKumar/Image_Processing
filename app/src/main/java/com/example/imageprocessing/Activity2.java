package com.example.imageprocessing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class Activity2 extends AppCompatActivity {

    private ImageView imageView;
    private Button left,right,crop,save;
    private final static String camera="capture_The_Image", gallery= "select_The_Image_From_Gallery";
    public final static String key="check_The_Selection";
    private final static int camera_Result_Code= 0, gallery_Result_Code= 1, pic_Crop = 2;
    private int rotation= 0;
    Uri image_URI;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        initialize_The_Fields();

        Intent intent= getIntent();
        temp= intent.getStringExtra(key);
        if(temp.equals(camera)){
            openCamera();
        }
        if(temp.equals(gallery)){
            openGallery();
        }

        left.setOnClickListener(v -> left_Rotation());
        right.setOnClickListener(v -> right_Rotation());
        crop.setOnClickListener(v -> crop_Image());
        save.setOnClickListener(v -> save_Image());
    }

    private void initialize_The_Fields(){
        imageView= (ImageView)findViewById(R.id.image_ID);
        left= (Button)findViewById(R.id.left_Rotation);
        right= (Button)findViewById(R.id.right_Rotation);
        crop= (Button)findViewById(R.id.crop_Image);
        save= (Button)findViewById(R.id.save_Image);
    }

    private void openCamera(){
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, camera_Result_Code);
    }

    private void openGallery(){
//        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, gallery_Result_Code);
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick an Image: "),gallery_Result_Code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==camera_Result_Code && data!=null && resultCode==RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            image_URI= data.getData();

            // Documentation about Bitmap: https://twiserandom.com/android/graphics/what-is-a-bitmap-in-android/index.html#:~:text=Everything%20that%20is%20drawn%20in,by%20using%20the%20BitmapFactory%20class
        }
        else if(requestCode==gallery_Result_Code && data!=null && resultCode==RESULT_OK){
            image_URI= data.getData();
//            String []path= { MediaStore.Images.Media.DATA };  // read picked image path using content resolver.
//            Cursor cursor= getContentResolver().query(image_URI, path, null, null, null);
//            cursor.moveToFirst();     //Move the cursor to the first row. This method will return false if the cursor is empty.
//            int column_Index= cursor.getColumnIndex(path[0]);
//            String image_Path= cursor.getString(column_Index);
//            Bitmap bitmap= (Bitmap) BitmapFactory.decodeFile(image_Path);
//            cursor.close();   //If we doesn't close the cursor then we will get the runtime exception.
//            imageView.setImageBitmap(bitmap);

            try {
                InputStream inputStream= getContentResolver().openInputStream(image_URI);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                inputStream.close();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                finish();
            }
        }
        else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), "Couldn't Crop the Image! "+result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
            // CropImage Library: https://github.com/ArthurHub/Android-Image-Cropper/
        }
        else{
            finish();
        }
    }

    private void left_Rotation(){
        if(rotation<=0) { rotation=360; }
        else { rotation -= 90; }
        imageView.setRotation(rotation);
    }

    private void right_Rotation(){
        if(rotation>=360) { rotation=0; }
        else { rotation += 90; }
        imageView.setRotation(rotation);
    }

    private void crop_Image() {
            CropImage.activity(image_URI).start(this);
    }

    private void save_Image(){
        BitmapDrawable bitmapDrawable= (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap= bitmapDrawable.getBitmap();

        File file= getFilesDir();
        File directory= new File(file.getAbsolutePath() + "Image Processing");
        directory.mkdirs();

        String file_Name= "Picture.png";
        File outFile= new File(directory, file_Name);
        try {
            OutputStream outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Saved to Gallery -- "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        finish();
    }
}