package com.uniwheelsapp.uniwheelsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RegisterActivity extends AppCompatActivity {

    ImageView googleButton;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            System.out.println("HACE ESTOOOOO");
            email = account.getEmail();
            checkEmail(email);
        }
    }

    private void checkEmail(String email){
        db.collection("users")
            .document(email).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Person persona = documentSnapshot.toObject(Person.class);
                        Log.d("USER", persona.toString());
                    } else {
                        Log.w("USER", "NO EXISTE");
                    }
                }
            });

    }

    public void continueButton(View view){
        EditText nombreView = (EditText) findViewById(R.id.signupNameInput);
        EditText apellidoView = (EditText) findViewById(R.id.signupLastNameInput);
        EditText passwordView = (EditText) findViewById(R.id.signupPassword);
        EditText confirmPasswordView = (EditText) findViewById(R.id.signupPasswordConfirm);
        String nombre = nombreView.getText().toString();
        String apellido = apellidoView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();

        if(!password.equals(confirmPassword) || nombre.isEmpty() || apellido.isEmpty() || password.isEmpty()){
            Log.w("ERROR", "No se puede continuar");
        } else {
            Map<String, Object> userData = new HashMap<String, Object>();
            userData.put(Person.MAIL_KEY, email);
            userData.put(Person.NAME_KEY, nombre);
            userData.put(Person.LASTNAME_KEY, apellido);
            userData.put(Person.PASSWORD_KEY, password);
            Log.d("VALORES", userData.values().toString());
            db.collection("users").document(email).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("SUCCESS", "wooooo");
                    } else {
                        Log.w("ERROR", "jeejejee");
                    }
                }
            });
        }
    }

    private void MainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}