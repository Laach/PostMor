package com.mdhgroup2.postmor.Register;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class register2 extends Fragment {
    private Uri outputFileUri;
    private File currentPhotoFile;
    private String currentPhotoPath;
    private static final int REQUEST_CODE = 1;

    public static register2 newInstance() {
        return new register2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register2_fragment, container, false);
        RegisterViewModel mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        Button uploadProfilePic = view.findViewById(R.id.register_upload_button);
        ImageView profilePic = view.findViewById(R.id.imageCard);
        profilePic.setImageBitmap(mViewModel.getAccountImage());

        TextView tv = view.findViewById(R.id.cardName);
        tv.setText(mViewModel.getAccountName());

        tv = view.findViewById(R.id.cardAddress);
        tv.setText(mViewModel.getAddress());

        uploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                    // Create temp file for the image (android.developer)
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_"+timestamp+"_";
                    File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    currentPhotoFile = null;
                    try {
                        currentPhotoFile = File.createTempFile(
                                imageFileName,
                                ".jpg",
                                storageDir
                        );
                        currentPhotoPath = currentPhotoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(currentPhotoFile != null){
                        outputFileUri = FileProvider.getUriForFile(getActivity(), "com.mdhgroup2.postmor.fileprovider",
                                currentPhotoFile);
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

                    // Create chooser for gallery option
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent chooserIntent = Intent.createChooser(pickIntent, "Select Source");

                    // Add the camera options.
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

                    // Start the chooser
                    startActivityForResult(chooserIntent, REQUEST_CODE);
                }
        });

        Button next = view.findViewById(R.id.register_next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(register2Directions.actionRegister2ToRegister3());
            }
        });

        Button previous = view.findViewById(R.id.register_prev_button);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(register2Directions.actionRegister2ToRegister1());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterViewModel mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        // Check if the request is from our camera/gallery intent and if it finished with the correct resultcode
        if(requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK){
            boolean isCamera;
            Bitmap photo = null;

            // Determine whether the camera or the gallery was used
            if(data == null || data.getData() == null){
                isCamera = true;
            }
            else{
                String action = data.getAction();
                if(action == null){
                    isCamera = false;
                }else {
                    isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            // Depending on if the camera was used or not, choose the correct action
            final Uri selectedImageUri;
            if(isCamera){
                selectedImageUri = outputFileUri;
            }else{
                selectedImageUri = data == null ? null : data.getData();
            }
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                // If the image is rotated, reset its rotation
                if(isCamera){
                    ExifInterface exif= new ExifInterface(currentPhotoPath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Matrix matrix = new Matrix();
                    switch(orientation){
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.setRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.setRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.setRotate(270);
                            break;
                    }
                    Bitmap rotatedBitmap = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                    photo = rotatedBitmap;
                }else{
                    // If the image was picked from the gallery, copy the image to app storage

                    // First get file path for the gallery image
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String galleryPath = cursor.getString(column_index);
                    cursor.close();

                    // Copy the file to the temp image in app storage
                    File sourceFile = new File(galleryPath);
                    FileChannel source = null;
                    FileChannel destination = null;
                    source = new FileInputStream(sourceFile).getChannel();
                    destination = new FileOutputStream(currentPhotoFile).getChannel();
                    if(destination != null && source != null){
                        destination.transferFrom(source, 0, source.size());
                    }
                    if(source != null){
                        source.close();
                    }
                    if(destination != null){
                        destination.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Add the image to the recycler view
            //mAdapter.addItem(photo,currentPhotoFile.getName(),currentPhotoFile.getName());
            mViewModel.setAccountImage(photo);
            ImageView profilePic = getView().findViewById(R.id.imageCard);
            profilePic.setImageBitmap(photo);
        }
        else{
            // f the user closes the intent without choosing/taking photo, display a toast
            Toast.makeText(getActivity(), "No image selected or taken", Toast.LENGTH_SHORT).show();
        }
    }

}
