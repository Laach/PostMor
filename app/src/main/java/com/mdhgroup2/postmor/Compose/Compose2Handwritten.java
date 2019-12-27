package com.mdhgroup2.postmor.Compose;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compose2Handwritten extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Compose2HandwrittenViewModel mViewModel;

    private ImageView image;
    private Uri outputFileUri;
    private String currentPhotoPath;
    public static Compose2Handwritten newInstance() {
        return new Compose2Handwritten();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose2_handwritten_fragment, container, false);
        ConstraintLayout addItemLayout = view.findViewById(R.id.addItemConstraint);

        recyclerView = view.findViewById(R.id.compose2HandwrittenRecyclerView);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new Compose2HandRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        image = view.findViewById(R.id.imageView3);

        addItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determine Uri of camera image to save.
                //honestly what does this even do?

                /*
                File root = new File(Environment.getExternalStorageDirectory()+ File.separator);
                root.mkdirs();
                String fname = "img_"+ System.currentTimeMillis() + ".jpg";
                File sdImageMainDirectory = new File(root, fname);
                outputFileUri = Uri.fromFile(sdImageMainDirectory);*/

                //Create temp file for camera image (android.developer)
                String timestamp = String.valueOf(System.currentTimeMillis());
                String imageFileName = "JPEG_"+timestamp+"_";
                File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = null;
                try {
                     image = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir
                    );
                    currentPhotoPath = image.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(image != null){
                    outputFileUri = FileProvider.getUriForFile(getActivity(), "com.mdhgroup2.postmor.fileprovider",
                            image);
                }

                // Camera.
                List<Intent> cameraIntents = new ArrayList<Intent>();
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager packageManager = getActivity().getPackageManager();
                List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                for(ResolveInfo res : listCam) {
                    String packageName = res.activityInfo.packageName;
                    Intent intent = new Intent(captureIntent);
                    intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                    intent.setPackage(packageName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntents.add(intent);
                }

                //Create chooser for gallery option
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent chooserIntent = Intent.createChooser(pickIntent, "Select Source");

                // Add the camera options.
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

                //Start the chooser
                startActivityForResult(chooserIntent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Compose2HandwrittenViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            //image.setImageBitmap(photo);
            boolean isCamera;
            if(data == null || data.getData() == null){
                isCamera = true;
            }
            else{
                String action = data.getAction();
                Log.d("test", "onActivityResult: "+action);
                if(action == null){
                    isCamera = false;
                }else {
                    isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            final Uri selectedImageUri;
            if(isCamera){
                selectedImageUri = outputFileUri;
                Log.d("test", "onActivityResult: isCamera == true\n"+selectedImageUri);
            }else{
                selectedImageUri = data == null ? null : data.getData();
                Log.d("test", "onActivityResult: isCamera != true \n"+selectedImageUri);

            }
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                image.setImageBitmap(photo);
                //image.setImageURI(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            Log.d("test", "onActivityResult: sending to super");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
