package com.mdhgroup2.postmor.Compose;

import android.database.sqlite.SQLiteBlobTooBigException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.ArrayList;


public class Compose2HandwrittenViewModel extends ViewModel {
    private ILetterRepository letterRepo;
    public boolean isFirstTimeOpened;

    public Compose2HandwrittenViewModel(){
        letterRepo = DatabaseClient.getLetterRepository();
        editMsgDraft = new EditMsg();
        isFirstTimeOpened = true;
    }

    private MutableLiveData<EditMsg> draftMsg;
    private EditMsg editMsgDraft;

    public MutableLiveData<EditMsg> getDraftMsg(){
        if(draftMsg == null) {
            draftMsg = new MutableLiveData<>();
        }
        return draftMsg;
    }

    public void getDraft(int recipientID){
        GetDraftTask getDraftTask = new GetDraftTask();
        getDraftTask.execute(recipientID);
    }

    public void saveDraft(){
        SaveDraftTask saveDraftTask = new SaveDraftTask();
        saveDraftTask.execute(editMsgDraft);
    }

    public void addImage(Bitmap image){
        editMsgDraft.Images.add(image);
        draftMsg.postValue(editMsgDraft);
    }

    public void removeImage(int position){
        editMsgDraft.Images.remove(position);
        draftMsg.postValue(editMsgDraft);
    }

    public void changeRecipient(int recipientID){
        editMsgDraft.RecipientID = recipientID;
        draftMsg.postValue(editMsgDraft);
    }

    private class SaveDraftTask extends AsyncTask<EditMsg, Void, Void>{
        @Override
        protected Void doInBackground(EditMsg... editMsgs) {
            if(editMsgs != null){
                Log.d("test", "doInBackground: SAVING FOLLOWING MESSAGE:");
                Log.d("test", "doInBackground:      Imageslist size: "+editMsgs[0].Images.size());
                Log.d("test", "doInBackground:      internalMessageID: "+editMsgs[0].InternalMessageID);
                Log.d("test", "doInBackground:      recipient: "+editMsgs[0].RecipientID);
                Log.d("test", "doInBackground:      text: "+editMsgs[0].Text);
                Log.d("test", "doInBackground:      isDraft: "+editMsgs[0].IsDraft);
                Log.d("test", "doInBackground: letterRepo.saveDraft...");
                letterRepo.saveDraft(editMsgs[0]);
            }
            return null;
        }
    }

    private class GetDraftTask extends AsyncTask<Integer, Void, EditMsg>{
        @Override
        protected EditMsg doInBackground(Integer... integers) {
            // Get the draft
            Integer recipientID = integers[0];
            if (recipientID == null || recipientID == 0) {
                Log.d("test", "doInBackground: getOrStartGENERICDraft");
                return letterRepo.getOrStartGenericDraft();
            }
            Log.d("test", "doInBackground: getOrStartDraft");
            return letterRepo.getOrStartDraft(recipientID);

        }

        @Override
        protected void onPostExecute(EditMsg editMsg) {
            if(editMsg != null) {
                // Update the live data
                editMsgDraft = editMsg;
                Log.d("test", "onPostExecute: internalMessageID: " + editMsg.InternalMessageID);
                Log.d("test", "onPostExecute: recipientID: " + editMsg.RecipientID);
                if (editMsgDraft.Images == null) {
                    editMsgDraft.Images = new ArrayList<Bitmap>();
                }
                draftMsg.postValue(editMsg);
            }
        }
    }
}
