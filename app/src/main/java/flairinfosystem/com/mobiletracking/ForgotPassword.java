package flairinfosystem.com.mobiletracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {
    TextInputLayout email_TIL,mobile_TIL,newpwd_TIL,conpwd_TIL;
    Button proceed_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_TIL=findViewById(R.id.email);
        mobile_TIL=findViewById(R.id.mobile);
        newpwd_TIL=findViewById(R.id.newpwd);
        conpwd_TIL=findViewById(R.id.conpwd);
        proceed_btn=findViewById(R.id.proceed);

        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),SignIn.class));
            }
        });



    }
}
