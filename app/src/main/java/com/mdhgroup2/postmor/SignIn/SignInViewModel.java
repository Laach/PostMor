package com.mdhgroup2.postmor.SignIn;

import android.os.AsyncTask;

import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    private loginInformation account = new loginInformation();
    private final IAccountRepository accountDB;
    private MutableLiveData<Boolean> callResult;
    private MutableLiveData<Boolean> alreadyLoggedIn;

    public SignInViewModel(){
        accountDB = DatabaseClient.getAccountRepository();
    }

    public MutableLiveData<Boolean> getResult(){
        if(callResult == null) {
            callResult = new MutableLiveData<>();
        }
        return callResult;
    }

    public MutableLiveData<Boolean> amILoggedIn(){
        if(alreadyLoggedIn == null){
            alreadyLoggedIn = new MutableLiveData<>();
        }
        return  alreadyLoggedIn;
    }

    public void checkLoginStatus(){
        dbAlreadyLoggedIn status = new dbAlreadyLoggedIn();
        status.execute();
    }

    public void login(){
        dbLogin login = new dbLogin();
        login.execute(account);
    }

    public void setPassword(String string){
        account.password = string;
    }

    public String getPassword(){
        return account.password;
    }

    public void setEmail(String string){
        account.email = string;
    }

    public String getEmail(){
        return account.email;
    }

    public String checkValidity(){
        if(account.email == null || account.email.equals("")){
            return "email";
        }
        else if (account.password == null || account.password.equals("")){
            return "password";
        }
        else return "True";
    }

    private class dbLogin extends AsyncTask<loginInformation, Void, Boolean>{
        @Override
        protected Boolean doInBackground(loginInformation... login){
            Boolean result;
            try {
                result = accountDB.signIn(login[0].email, login[0].password);
            }catch(Exception e){
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            callResult.postValue(result);
        }
    }

    private class dbAlreadyLoggedIn extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... v){
            Boolean result;
            try{
                result = accountDB.isLoggedIn();
            }catch (Exception e){
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            alreadyLoggedIn.postValue(result);
        }
    }

    private class loginInformation{
        String email;
        String password;
    }
}
