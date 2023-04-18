package ba.sum.fpmoz.filmoteka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.filmoteka.model.UserProfile;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_profile);

        FirebaseUser currentUser  = mAuth.getCurrentUser();
        EditText fullnameTxt = findViewById(R.id.fullnameTxt);
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText phoneTxt = findViewById(R.id.phoneTxt);
        Button submitBtn = findViewById(R.id.submitBtn);

        if(currentUser != null){
            DatabaseReference profileRef = mDatabase.getReference("Profil").child(currentUser.getUid());

            profileRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    UserProfile userProfile= task.getResult().getValue(UserProfile.class);
                    if(userProfile != null){
                        fullnameTxt.setText(userProfile.getFullNameTxt());
                        emailTxt.setText(userProfile.getEmail());
                        phoneTxt.setText(userProfile.getPhoneTxt());
                    }
                }
            });
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fullname = fullnameTxt.getText().toString();
                    String email = emailTxt.getText().toString();
                    String phone = phoneTxt.getText().toString();

                    UserProfile user = new UserProfile(fullname, email, phone, email);
                    profileRef.setValue(user);

                    Toast.makeText(getApplicationContext(), "Uspješno ste promijenili podatke", Toast.LENGTH_SHORT);
                    startActivity(new Intent(EditProfileActivity.this,AccountActivity.class));
                }
            });
        }
    }

}