package com.example.messenger.ui.users;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersViewModel extends ViewModel {

    private FirebaseAuth auth;

    private MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();
    public LiveData<FirebaseUser> getFirebaseUser() {return firebaseUser;}

    public UsersViewModel(){
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }

    public void logout(){
        auth.signOut();
    }
}
