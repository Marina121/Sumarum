package ba.sum.fpmoz.filmoteka;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.filmoteka.model.UserProfile;

public class RegisterActivity extends AppCompatActivity {


    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://filmoteka-378b8-default-rtdb.europe-west1.firebasedatabase.app/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);


        EditText fullNameTxt = findViewById(R.id.fullnameTxt);
        EditText phoneTxt = findViewById(R.id.phoneTxt);
        EditText registerEmailTxt = findViewById(R.id.registerEmailTxt);
        EditText registerPasswordTxt = findViewById(R.id.registerPasswordTxt);
        EditText registerPasswordCnfTxt = findViewById(R.id.registerPasswordCnfTxt);
        TextView noAccount = findViewById(R.id.noAccount);

        noAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        // Gumb za registraciju
        Button registerBtn = findViewById(R.id.registerBtn);
        // Što se događa nakon klika
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dohvaćanje podataka
                String fullname = fullNameTxt.getText().toString();
                String phone = phoneTxt.getText().toString();
                String email = registerEmailTxt.getText().toString();
                String password = registerPasswordTxt.getText().toString();
                String passwordCnf = registerPasswordCnfTxt.getText().toString();

                Log.d(TAG, "userData:" + fullname + " " + phone);

                if (!fullname.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && password.equals(passwordCnf)) {
                    Log.d(TAG, "ifSuccess");
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                UserProfile userProfile = new UserProfile(fullname, email, phone, fullname);
                                DatabaseReference profileRef = mDatabase.getReference("Profil");
                                profileRef.child(firebaseUser.getUid()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser.sendEmailVerification();

                                            fullNameTxt.setText("");
                                            phoneTxt.setText("");
                                            registerEmailTxt.setText("");
                                            registerPasswordTxt.setText("");
                                            registerPasswordCnfTxt.setText("");

                                            Log.d(TAG, "uploadProfile: Uspješno");
                                            Toast.makeText(getApplicationContext(), "Registracija je uspješna, molimo verificirajte email!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        } else {
                                            Log.d(TAG, "uploadProfile:Neuspješno!");
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Greška prilikom unosa ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });
                }

            }
        });
    }
}