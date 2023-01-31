package com.example.messenger.ui.users;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.messenger.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    private DatabaseReference usersRef;

    private MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();
    public LiveData<FirebaseUser> getFirebaseUser() {return firebaseUser;}

    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    public LiveData<List<User>> getUsers() {
        return users;
    }

    public UsersViewModel(){
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser.setValue(firebaseAuth.getCurrentUser());
            }
        });
        dataBase = FirebaseDatabase.getInstance();
        usersRef = dataBase.getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser == null){
                    return;
                }
                List<User> userFromdb = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId()==null){
                        return;
                    }
                    if(!user.getId().equals(currentUser.getUid())){
                        userFromdb.add(user);
                    }
                }
                users.setValue(userFromdb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout(){
        auth.signOut();
    }
}
