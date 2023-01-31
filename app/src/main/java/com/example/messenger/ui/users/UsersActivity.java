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
import com.example.messenger.data.User;
import com.example.messenger.ui.chat.ChatActivity;
import com.example.messenger.ui.login.MainActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel usersViewModel;
    private UserRVAdapter usersAdapter;
    private RecyclerView recyclerViewUser;

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();
        usersAdapter.setOnUserClickListener(new UserRVAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = ChatActivity.newIntent(UsersActivity.this, currentUserId, user.getId());
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerViewUser = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UserRVAdapter();
        recyclerViewUser.setAdapter(usersAdapter);
    }

    public static Intent newIntent(Context context,String currentUserId){
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID,currentUserId);
        return intent;
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
        usersViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersAdapter.setUsers(users);
            }
        });
    }
}