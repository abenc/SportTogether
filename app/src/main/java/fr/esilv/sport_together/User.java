package fr.esilv.sport_together;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by anirb on 20/11/2015.
 */
public class User extends ParseUser{
    ParseUser currentUser;
    boolean isLoggedIn;
    public User(){
        if(currentUser ==null && !(ParseFacebookUtils.isLinked(currentUser))){
            currentUser= ParseUser.getCurrentUser();
            isLoggedIn = false;
        }
        else {
        isLoggedIn = true;
        }

    }
}
