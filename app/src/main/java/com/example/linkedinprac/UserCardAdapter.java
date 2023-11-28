package com.example.linkedinprac;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.ViewHolder> {

    private List<User> userList;
    private OnEmailButtonClickListener emailButtonClickListener;
    private User selectedUser;

    public UserCardAdapter(List<User> userList, OnEmailButtonClickListener listener) {
        this.userList = userList;
        this.emailButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.unameTextView.setText(user.getUsername());
        holder.genderTextView.setText(user.getGender());
        holder.phoneNoTextView.setText(user.getPhoneNo());
        holder.shortBioTextView.setText(user.getShortBio());
        holder.skillsTextView.setText(user.getSkills());

        holder.emailButton.setTag(user.getEmail());

        holder.emailButton.setOnClickListener(v -> {
            selectedUser = user; // Update the selected user
            if (emailButtonClickListener != null) {
                emailButtonClickListener.onEmailButtonClick(user.getEmail());
            }
        });

        // Load user profile picture using Picasso or Glide
        // For example, assuming you have a storage reference for user profile pictures:
        // StorageReference profileRef = storageReference.child("users/" + user.getUserId() + "/profile.jpeg");
        // Picasso.get().load(profileRef).into(holder.profilePicImageView);
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePicImageView;
        public TextView unameTextView;
        public TextView genderTextView;
        public TextView phoneNoTextView;
        public TextView shortBioTextView;
        public TextView skillsTextView;
        public FloatingActionButton emailButton;// Assuming email button is an ImageView

        public ViewHolder(View itemView) {
            super(itemView);
            profilePicImageView = itemView.findViewById(R.id.card_profile_pic);
            unameTextView = itemView.findViewById(R.id.card_uname);
            genderTextView = itemView.findViewById(R.id.card_gender);
            phoneNoTextView = itemView.findViewById(R.id.card_phone);
            shortBioTextView = itemView.findViewById(R.id.card_bio);
            skillsTextView = itemView.findViewById(R.id.card_skills);
            emailButton = itemView.findViewById(R.id.email_button); // Replace with the actual ID of the email button
        }
    }

    public interface OnEmailButtonClickListener {
        void onEmailButtonClick(String email);
    }
}
