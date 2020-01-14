package com.mdhgroup2.postmor.Compose;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.EditMsg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compose2Handwritten extends Fragment implements OnStartDragListener {
    private RecyclerView recyclerView;
    private Compose2HandRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Compose2HandwrittenViewModel mViewModel;
    private MainActivityViewModel mainVM;

    private Uri outputFileUri;
    private File currentPhotoFile;
    private String currentPhotoPath;

    private ConstraintLayout addItemLayout;
    private Button sendButton;

    private ItemTouchHelper itemTouchHelper;

    private static final int REQUEST_CODE = 1;

    public static Compose2Handwritten newInstance() {
        return new Compose2Handwritten();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose2_handwritten_fragment, container, false);
        addItemLayout = view.findViewById(R.id.addItemConstraint);
        sendButton = view.findViewById(R.id.c2hand_Send);


        recyclerView = view.findViewById(R.id.compose2HandwrittenRecyclerView);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new Compose2HandRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);
        itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback(mAdapter, getContext(), this));

        itemTouchHelper.attachToRecyclerView(recyclerView);

        mViewModel = ViewModelProviders.of(this).get(Compose2HandwrittenViewModel.class);
        mainVM = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);


        // Ask user for permission to read/write
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        final String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_CODE);
        }

        // Onclick listener to open/take photo
        addItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getItemCount() < 3){
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
            }
        });

        // Find the recipient ID from the recipient fragment
        final Integer recipientID = null;
        final Contact recipient = mainVM.getChosenRecipient();

        // Observe livedata for draft
        mViewModel.getDraftMsg().observe(this, new Observer<EditMsg>() {
            @Override
            public void onChanged(EditMsg editMsg) {
                // Update the ui
                // Add all images
                int i = 1;
                //if(recipientID != null)
                if(editMsg.Images != null){
                    mAdapter.clear();
                    for (Bitmap image : editMsg.Images) {
                        mAdapter.addItem(image, String.valueOf(i), String.valueOf(i));
                        i++;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        // If compose is reopened from choosing contact,this will return false
        // and not get a new draft.
        if(mViewModel.isFirstTimeOpened){
            if(recipient == null) {// If no recipient has been chosen, send null
                mViewModel.getDraft(0);
                mViewModel.isFirstTimeOpened = false;
            }else{// Else send id
                mViewModel.getDraft(recipient.UserID);
                mViewModel.isFirstTimeOpened = false;
            }
        }



        mainVM.getChosenRec().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if(contact != null) {
                    mViewModel.changeRecipient(contact.UserID);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get all images

                if(mainVM.getChosenRecipient() != null) {
                    if (mViewModel.getMsg().Images != null) {

                        Log.d("test", "onClick: send");
                        //mViewModel.sendMessage();
                        SendMessageTask2 sendMessageTask2 = new SendMessageTask2(getContext(), mViewModel);
                        sendMessageTask2.execute(mViewModel.getMsg());
                    } else {
                        Toast.makeText(getActivity(),"No message!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "No recipient selected!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        //mViewModel.saveDraft();
    }

    @Override
    public void onDestroy() {
        mViewModel.saveDraft();

        super.onDestroy();
    }

    public void removeFile(String fileName, int position){
        // Delete the file from internal storage
        String path =getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        File file = new File(path+"/"+fileName);
        boolean deleted = file.delete();

        mViewModel.removeImage(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            mViewModel.addImage(photo);
        }
        else{
            // f the user closes the intent without choosing/taking photo, display a toast
            Toast.makeText(getActivity(), "No image selected or taken", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}


class SendMessageTask2 extends AsyncTask<EditMsg, Void, Boolean> {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Compose2HandwrittenViewModel mViewModel;
    private AlertDialog.Builder alertBuilder;

    public SendMessageTask2(Context context,Compose2HandwrittenViewModel viewmodel){
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
        mViewModel = viewmodel;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.setMessage("Preparing letter...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        alertBuilder = new AlertDialog.Builder(mContext);
    }

    @Override
    protected Boolean doInBackground(EditMsg... editMsgs) {

        boolean returnValue = false;
        if(editMsgs != null){
            Log.d("test", "doInBackground: text: "+editMsgs[0].Text);
            Log.d("test", "doInBackground: recipient: "+editMsgs[0].RecipientID);
            Log.d("test", "doInBackground: messageID: "+editMsgs[0].InternalMessageID);
            editMsgs[0].Text = null;

            returnValue = mViewModel.sendMessage(editMsgs[0]);
            //returnValue = true;
            Log.d("test", "doInBackground: return value: "+returnValue);
        }
        return returnValue;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

        if(result){
            //Update UI
            alertBuilder.setTitle("Success!")
                    .setMessage("Your letter will be sent at 16:00")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss
                        }
                    });
            AlertDialog dialog = alertBuilder.create();
            dialog.setCanceledOnTouchOutside(false);

            dialog.show();

        }else{
            Toast.makeText(mContext, "Error: message could not be sent", Toast.LENGTH_SHORT).show();
        }
    }
}
