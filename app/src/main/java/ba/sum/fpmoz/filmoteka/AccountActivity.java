package ba.sum.fpmoz.filmoteka;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import ba.sum.fpmoz.filmoteka.model.UserProfile;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView viewLl, viewEmailTxt, fullnameTv, emailTv, phoneTv;
    ShapeableImageView imgTv;
    //ImageView imgTv;
    Button editProfile;
    ImageButton changePassword, logoutBtn;
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");


        editProfile = findViewById(R.id.editProfile);
        changePassword = findViewById(R.id.changePassword);
        logoutBtn = findViewById(R.id.logoutBtn);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);



        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setStrokeWidth(4f);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);


        viewLl = findViewById(R.id.viewLl);
        viewEmailTxt = findViewById(R.id.viewEmailTxt);
        fullnameTv = findViewById(R.id.fullnameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        imgTv = findViewById(R.id.imgTv);
        imgTv.setImageDrawable(shapeDrawable);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference profileRef = mDatabase.getReference("Profil").child(currentUser.getUid());
            profileRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    UserProfile profileUser = task.getResult().getValue(UserProfile.class);
                    if (profileUser != null) {

                        viewLl.setText(profileUser.getFullNameTxt());
                        viewEmailTxt.setText(profileUser.getEmail());
                        fullnameTv.setText(profileUser.getFullNameTxt());
                        emailTv.setText(profileUser.getEmail());
                        phoneTv.setText(profileUser.getPhoneTxt());

                        StorageReference imageRef = storageRef.child("Profil/files/" + profileUser.getImage());

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("TAG", "Slika uspješno preuzeta iz Firebase pohrane.");

                                Picasso
                                        .get()
                                        .load(uri)
                                        .into(imgTv);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d("TAG", "Neuspješno.");

                            }
                        });
                    }
                }
            });

            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editProfileActivity = new Intent(AccountActivity.this, EditProfileActivity.class);
                    startActivity(editProfileActivity);
                }
            });

            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent changePasswordActivity = new Intent(AccountActivity.this, ChangePasswordActivity.class);
                    startActivity(changePasswordActivity);
                }
            });

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }
            });


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.navigation_aboutUs:
                            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.navigation_faculty:
                            startActivity(new Intent(getApplicationContext(), FacultyActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.navigation_saved:
                            startActivity(new Intent(getApplicationContext(), SavedActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.navigation_account:
                            return true;
                    }
                    return false;
                }
            });
        }
    }
    @Override
    protected void onStart () {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}