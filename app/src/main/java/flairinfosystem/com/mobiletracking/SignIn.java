package flairinfosystem.com.mobiletracking;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {
    TextInputLayout username_TIL,password_TIL;
    TextView forgotpwd_txt,signup_txt;
    Button signin_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username_TIL= findViewById(R.id.username);
        password_TIL=findViewById(R.id.password);
        forgotpwd_txt=findViewById(R.id.forget_password);
        signup_txt=findViewById(R.id.signup);
        signin_btn=findViewById(R.id.signin);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminDashboard.class));
            }
        });
        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));

            }
        });

        forgotpwd_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));

            }
        });
    }
}
