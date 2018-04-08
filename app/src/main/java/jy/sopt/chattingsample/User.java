package jy.sopt.chattingsample;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jyoung on 2017. 12. 18..
 */

public class User {

    private String name;
    private String token;

    public User(String name, String token) {
        this.name = name;
        this.token = token;
    }

    @Exclude
    public Map<String, Object> userReg() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("token", token);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
