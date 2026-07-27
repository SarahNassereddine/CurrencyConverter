package com.example.projectcurrrency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileFragment extends Fragment {
    ImageView profileIv;
    Button chooseImageBtn, saveBtn, deleteBtn;
    EditText usernameEt, locationEt;
    TextView emailTv;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;

    Uri imageUri;
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);

        profileIv = view.findViewById(R.id.profileImg);
        chooseImageBtn = view.findViewById(R.id.changePhotoBtn);
        saveBtn = view.findViewById(R.id.saveBtn);
        usernameEt = view.findViewById(R.id.UsernameEt);
        locationEt = view.findViewById(R.id.locationEt);
        emailTv = view.findViewById(R.id.EmailTv);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        emailTv.setText(user.getEmail());

        loadUserData();

        chooseImageBtn.setOnClickListener(v -> openGallery());
        saveBtn.setOnClickListener(v -> saveChanges());
        return view;
    }
    private void loadUserData() {
        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    usernameEt.setText(doc.getString("username"));
                    locationEt.setText(doc.getString("location"));

                    String uri = doc.getString("profileUrl");
                    if (uri != null) {
                        profileIv.setImageURI(Uri.parse(uri));
                    }
                });
    }
    private final ActivityResultLauncher<Intent> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                            imageUri = result.getData().getData();
                            requireContext().getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            profileIv.setImageURI(imageUri);
                            Toast.makeText(getContext(), "Image selected ✔", Toast.LENGTH_SHORT).show();}});
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        imagePicker.launch(intent);
    }
    private void saveChanges() {
        String username = usernameEt.getText().toString().trim();
        String location = locationEt.getText().toString().trim();
        if (username.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(imageUri!= null ) {
            firestore.collection("users")
                    .document(user.getUid())
                    .update(
                            "username", username,
                            "location", location,
                            "profileUrl", imageUri.toString()
                    )
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    });
        }
        else{
            firestore.collection("users")
                .document(user.getUid())
                .update(
                        "username", username,
                        "location", location
                )
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                });
        }
    }

}