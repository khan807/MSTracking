package flairinfosystem.com.mobiletracking;

/**
 * Created by mazher807 on 28-02-2018.
 */
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Administrator on 1/11/2017.
 */
public class MyFirebaseInstanceID extends FirebaseInstanceIdService {

    private static final String REG_TOKEN="REG_TOKEN";

    @Override
    public void onTokenRefresh() {


        String recent_token= FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);


    }
}
