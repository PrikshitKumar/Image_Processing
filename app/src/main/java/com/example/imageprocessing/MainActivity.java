package com.example.imageprocessing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button camera_btn, gallery_btn;
    private final static int Permission_Code= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize_The_Fields();

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Activity2.class);
                intent.putExtra(Activity2.key,"capture_The_Image");
                startActivity(intent);
            }
        });
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(getApplicationContext(),"Permissions are already Granted!!", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getApplicationContext(),Activity2.class);
            intent.putExtra(Activity2.key,"select_The_Image_From_Gallery");
            startActivity(intent);
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Permission_Code);
        }
    }

    private void initialize_The_Fields(){
        camera_btn= findViewById(R.id.btn_Capture);
        gallery_btn= findViewById(R.id.btn_Gallery);
    }
}