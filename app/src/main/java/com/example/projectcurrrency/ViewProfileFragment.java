package com.example.projectcurrrency;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class ViewProfileFragment extends Fragment {

    Button editBtn;
    TextView emailTv, usernameTv, locationTv;
    ImageView profileIv;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_profile, container, false);

        profileIv= view.findViewById(R.id.profileImg);
        emailTv=view.findViewById(R.id.EmailTv);
        usernameTv=view.findViewById(R.id.UsernameTv);
        locationTv=view.findViewById(R.id.locationTv);
        editBtn=view.findViewById(R.id.editProfileBtn);

        auth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        loadProfile(user.getUid(), user.getEmail());
        editBtn.setOnClickListener( V->{
            NavHostFragment.findNavController(ViewProfileFragment.this).navigate(R.id.action_profileToEditProfile);
        });
        return view;
    }
    private void  loadProfile( String uid, String emailFromAuth){
        emailTv.setText("Email: "+ emailFromAuth);
        firestore.collection("users").document(uid).get().addOnSuccessListener(doc ->{
           String username=doc.getString("username");
           usernameTv.setText("Username: "+ username);
           String location=doc.getString("location");
           locationTv.setText("Location: "+ location);
           String profileUri=doc.getString("profileUrl");
            if (profileUri != null && !profileUri.isEmpty())
                 profileIv.setImageURI(Uri.parse(profileUri));
        });
    }
}