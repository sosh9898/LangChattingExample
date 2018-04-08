package jy.sopt.chattingsample.setting;

/**
 * Created by jyoung on 2017. 8. 18..
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import jy.sopt.chattingsample.SharedPreferencesService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        SharedPreferencesService.getInstance().load(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferencesService.getInstance().setPrefData("fcm_token", refreshedToken);
    }
}


