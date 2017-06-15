package quartz.gofoodsimulation.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import quartz.gofoodsimulation.models.UserModel;

/**
 * Created by kened on 4/3/2017.
 */

public class SessionManager {
    public static final String PREF_NAME = "GoFoodSimulation_prefs";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String USER_DATA = "userdata";
    private final int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public static SessionManager with(Context context) {
        return new SessionManager(context);
    }

    /*
    * TO-DO
    * save the user data to let the system recognize the logged-in user
    * @param: User
    */
    public void createSession(UserModel user) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_DATA, new Gson().toJson(user));
        editor.commit();
    }

    /*
    * @return: boolean
    * checking if user logged-in
    */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /*
    * @return: User
    * return the object of User
    */
    public UserModel getUserLoggedIn() {
        Gson gson = new Gson();
        String json = pref.getString(USER_DATA, "");
        UserModel user = gson.fromJson(json, UserModel.class);
        return user;
    }

    /*
    * TO-DO
    * clear the shared preferences from the user data
    */
    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public void updateSession(UserModel user) {
        this.clearSession();
        this.createSession(user);
    }
}
