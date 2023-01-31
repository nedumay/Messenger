package com.example.messenger.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.data.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolderMessage>{

    private static final int VIEW_TYPE_MY_MASSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MASSAGE = 2;

    private String currentUserId;

    public MessagesAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private List<Message> messages = new ArrayList<>();
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if(viewType == VIEW_TYPE_MY_MASSAGE){
            layoutResId = R.layout.my_message_item;
        } else {
            layoutResId = R.layout.other_message_item;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResId,parent,false);
        return new ViewHolderMessage(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(message.getSenderId().equals(currentUserId)){
            return VIEW_TYPE_MY_MASSAGE;
        } else {
            return VIEW_TYPE_OTHER_MASSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolderMessage extends RecyclerView.ViewHolder{

        private TextView textViewMessage;

        public ViewHolderMessage(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
