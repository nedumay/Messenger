package com.example.messenger.ui.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.messenger.R;
import com.example.messenger.ui.login.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel usersViewModel;
    private UserRVAdapter usersAdapter;
    private RecyclerView recyclerViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initViews();

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();

    }

    private void initViews() {
        recyclerViewUser = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UserRVAdapter();
        recyclerViewUser.setAdapter(usersAdapter);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, UsersActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_logout){
            usersViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeViewModel(){
        usersViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser == null){
                    Intent intent = MainActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}