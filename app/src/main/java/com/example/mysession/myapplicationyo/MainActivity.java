package com.example.mysession.myapplicationyo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.necistudio.libarary.FilePickerActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    Uri chosenFIle=null;
    String path=null;


    private StorageReference storageRef;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=(Button)findViewById(R.id.button);
        Button buttonUpload=(Button)findViewById(R.id.buttonUpload);
        textView=(TextView)findViewById(R.id.textView);
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();


            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileToDatabase();
            }
        });

    }

    private static final int FILE_SELECT_CODE = 0;
    //l fonction hedhi hiya elli t5alli ta5ar fichier bech t'uploadih
    private void showFileChooser() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*//*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);*/
        Intent intent = new Intent(getApplicationContext(),FilePickerActivity.class);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //l l fonciton hedhi n7ottou chnou n7ebbou na3mlou lel uri mte3 l ficher mte3na
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file

                    path = data.getStringExtra("path");
                    chosenFIle = Uri.fromFile(new File(path));
                    textView.setText(chosenFIle.getLastPathSegment());

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //l fonction hedhi ta3mel upload lel ficher elli 7achetna bih fel base
    public void uploadFileToDatabase(){


        StorageReference fileRef = storageRef.child(path);

        final UploadTask uploadTask=fileRef.putFile(chosenFIle);
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        textView.setText("file uploaded bruh");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                        textView.setText("failed to upload");

                    }
                });
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
