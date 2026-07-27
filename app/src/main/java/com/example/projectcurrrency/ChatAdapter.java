package com.example.projectcurrrency;

import static android.text.format.DateUtils.isToday;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<Message> messages;
    String currentUserName;

    public ChatAdapter(ArrayList<Message> messages, String currentUserName) {
        this.messages = messages;
        this.currentUserName = currentUserName;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtSender, txtTime;
        LinearLayout container;

        public ViewHolder(View view) {
            super(view);
            txtMessage = view.findViewById(R.id.txtMessage);
            txtSender = view.findViewById(R.id.txtSender);
            txtTime = view.findViewById(R.id.txtTime);
            container = view.findViewById(R.id.container);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message m = messages.get(position);
        holder.txtMessage.setText(m.getText());
        holder.txtSender.setText(m.getSenderName());
        long ts = m.getTimestamp();
        Date msgDate = new Date(ts);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        if (isToday(ts)) {
            holder.txtTime.setText(timeFormat.format(msgDate));
        } else {
            holder.txtTime.setText(dateFormat.format(msgDate)+ " " + timeFormat.format(msgDate));
        }
        if (m.getSenderName().equals(currentUserName)) {
            holder.container.setGravity(Gravity.END);
            holder.txtMessage.setBackgroundColor(Color.parseColor("#DCF8C6"));
        } else {
            holder.container.setGravity(Gravity.START);
            holder.txtMessage.setBackgroundColor(Color.parseColor("#E0E0E0"));
        }
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }
}
