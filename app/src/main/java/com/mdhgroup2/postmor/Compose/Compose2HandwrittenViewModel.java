package com.mdhgroup2.postmor.Compose;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.ArrayList;

public class Compose2HandwrittenViewModel extends ViewModel {
    private ILetterRepository letterRepo;

    public Compose2HandwrittenViewModel(){
        letterRepo = DatabaseClient.getLetterRepository();
        editMsgDraft = new EditMsg();
    }

    private MutableLiveData<EditMsg> draftMsg;
    private EditMsg editMsgDraft;

    public MutableLiveData<EditMsg> getDraftMsg(){
        if(draftMsg == null) {
            draftMsg = new MutableLiveData<>();
        }
        return draftMsg;
    }

    public void getDraft(Integer recipientID){
        GetDraftTask getDraftTask = new GetDraftTask();
        getDraftTask.execute(recipientID);
    }

    public void addImage(Bitmap image){
        editMsgDraft.Images.add(image);
        draftMsg.postValue(editMsgDraft);
    }

    private class GetDraftTask extends AsyncTask<Integer, Void, EditMsg>{

        @Override
        protected EditMsg doInBackground(Integer... integers) {
            // Get the draft
            Integer recipientID = integers[0];
            if(recipientID == null){
                return letterRepo.getOrStartGenericDraft();
            }
            return letterRepo.getOrStartDraft(recipientID);
        }

        @Override
        protected void onPostExecute(EditMsg editMsg) {
            // Update the live data
            editMsgDraft = editMsg;
            editMsgDraft.Images = new ArrayList<Bitmap>();
            draftMsg.postValue(editMsg);
        }
    }
}
