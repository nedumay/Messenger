package com.example.messenger.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.R;
import com.example.messenger.data.Message;
import com.example.messenger.data.User;
import com.example.messenger.databinding.ActivityChatBinding;
import com.example.messenger.ui.users.UsersActivity;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    private ActivityChatBinding binding;
    private MessagesAdapter messagesAdapter;

    private String currentUserId;
    private String otherUserId;

    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);

        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);

        messagesAdapter = new MessagesAdapter(currentUserId);
        binding.recyclerViewChat.setAdapter(messagesAdapter);
        observeViewModel();
        binding.imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(
                        binding.editTextMessage.getText().toString().trim(),
                        currentUserId,
                        otherUserId
                );
                chatViewModel.sendMessage(message);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UsersActivity.newIntent(ChatActivity.this, currentUserId);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnline(false);
    }


    private void observeViewModel() {
        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });
        chatViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatViewModel.getIsSendMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    binding.editTextMessage.setText(" ");
                }
            }
        });
        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getLastName());
                binding.textViewTitle.setText(userInfo);
                int bgResId;
                if (user.isOnline()) {
                    bgResId = R.drawable.circle_green;
                    binding.textViewOnline.setText(R.string.online);
                } else {
                    bgResId = R.drawable.circle_red;
                    binding.textViewOnline.setText(R.string.offline);
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this, bgResId);
                binding.viewIsOnline.setBackground(background);
            }
        });
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}