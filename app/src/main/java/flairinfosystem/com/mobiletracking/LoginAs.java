package flairinfosystem.com.mobiletracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginAs extends AppCompatActivity implements View.OnClickListener {
    Button admin_btn,driver_btn,parents_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.MyTheme);
        setContentView(R.layout.activity_login_as);
        admin_btn=findViewById(R.id.adminbutton);
        driver_btn=findViewById(R.id.driverbutton);
        parents_btn=findViewById(R.id.parentbutton);
        admin_btn.setOnClickListener(this);
        driver_btn.setOnClickListener(this);
        parents_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.adminbutton:
                Intent i=new Intent(this,SignIn.class);
                startActivity(i);
                break;
            case R.id.driverbutton:
                Intent i1=new Intent(this,SignInDriver.class);
                startActivity(i1);
                break;
            case R.id.parentbutton:
                Intent i2=new Intent(this,SignInParent.class);
                startActivity(i2);
                break;
        }

    }
}
