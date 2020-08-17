package com.example.phoneotp;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    //defining view objects
    EditText username,name,weight,height,email,mobilenumber,password;
    Button signup;
    TextView login;
    RadioGroup radioGroup;
    Spinner country,countrycode;
    RadioButton b;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    String NameHolder, NumberHolder;




    //*defining firebaseauth object*//
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        name = (EditText) findViewById(R.id.fullname);

        email = (EditText) findViewById(R.id.email);
        mobilenumber = (EditText) findViewById(R.id.editTextPhone);
        countrycode = (Spinner) findViewById(R.id.spinnerCountriescode);
        countrycode.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryAreaCodes));
        password = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.buttonContinue);
        login = (TextView) findViewById(R.id.login);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();



        databaseReference=db.getReference("Cust_Signup_Data");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();

            }

        });

    /*    terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }); */
    }
    protected void onStart() {
        super.onStart();
    }

    public void CheckData() {
        final String Code = CountryData.countryAreaCodes[countrycode.getSelectedItemPosition()];
        final String Password = password.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        final String Username = username.getText().toString().trim();
        final String number = mobilenumber.getText().toString().trim();
        //final String id = databaseReference.push().getKey();

        if (Username.isEmpty() || !Username.contains("@")) {
            username.setError("Username cannot be empty/Name contains '@' characters");
            username.requestFocus();
            return;
        }

        if (Name.isEmpty() || Name.contains(".") || Name.contains("@") || Name.contains("[") || Name.contains("]") || Name.contains("!") || Name.contains("#") || Name.contains("$") || Name.contains("%") || Name.contains("^") || Name.contains("&") || Name.contains("*") || Name.contains("-") || Name.contains("+") || Name.contains(":") || Name.contains(",") || Name.contains("/") || Name.contains("'") || Name.contains(";")) {
            name.setError("Name cannot be empty/Name cannot contains special characters");
            name.requestFocus();
            return;
        }


        if (Email.isEmpty()) {
            email.setError("Valid email is required");
            email.requestFocus();
            return;
        }

        if (Password.isEmpty() || Password.length() < 8) {

            password.setError("Valid password is required");
            password.requestFocus();
            return;
        }

        if (((Email.contains(".@")) || (Email.contains("@.")) || (!Email.contains("@")) || (!Email.contains(".")) || (Email.contains("..")) || (Email.contains(" ")) || (Email.startsWith("@")) || (Email.endsWith("@")) || (Email.startsWith(".")) || (Email.endsWith(".")))) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }


        if (number.isEmpty() || number.length() < 10) {
            mobilenumber.setError("Valid number is required");
            mobilenumber.requestFocus();
            return;
        }
        if (Username.isEmpty() == false) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cust_Signup_Data");

            Query checkUser = databaseReference.orderByChild("username").equalTo(Username);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String usernamefromdb = dataSnapshot.child(Username).child("username").getValue(String.class);

                        if (usernamefromdb.equals(Username)) {
                            username.setError("User with this Username already exists  ");
                            username.requestFocus();
                            //Intent i = new Intent(getApplicationContext(),cust_signup.class);
                            //startActivity(i);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Register.....", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Create user with email and password
        auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // check if email is already in use or not

                        if (!task.isSuccessful()) {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //saving data to realtime database

                         //   Cust_Signup_Data data = new Cust_Signup_Data(Username,Name,Email,number,Password);
                           // databaseReference.child(Username).setValue(data);



                            //for Phone OTP

                            String phonenumber = "+" +Code +number;
                            Intent intent = new Intent(RegisterActivity.this,OtpActivity.class);
                            intent.putExtra("phonenumber", phonenumber);
                            intent.putExtra("name", Name);
                            intent.putExtra("username", Username);
                            intent.putExtra("email", Email);
                            intent.putExtra("password", Password);
                            startActivity(intent);

                        }

                    }
                });
    }







       /* findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phonenumber = "+" + code + number;

                Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);

            }
        });*/

  /*  @Override
   protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }*/
    }
