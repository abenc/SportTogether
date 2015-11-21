package fr.esilv.sport_together;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {
    @InjectView(R.id.userProfilePicture)   ProfilePictureView userProfilePictureView;
    @InjectView(R.id.userName)   TextView userNameView;
    @InjectView(R.id.userGender)   TextView userGenderView;
    @InjectView(R.id.userEmail)     TextView userEmailView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //fetching facebook user info if logged in or display user info if profile exists
        ParseUser currentUser = ParseUser.getCurrentUser();
        if((currentUser!= null) && currentUser.isAuthenticated() && !(currentUser.has("profile")) ){
        makeMeRequest();
        }
        else if(currentUser.has("profile")){
            updateViewsWithProfileInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeMeRequest(){

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {//Creates a new Request configured to retrieve a user's own profile : source fb sdk doc
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) { // une fois la callback reussi, one verifie si le jsonobject n'est pas vide
            displaytoast(AccessToken.ACCESS_TOKEN_KEY);
                if(jsonObject!=null){ // si il ne l'est pas, nous creeons un JSONobject pour creer le profil de l'utilisateur
                    JSONObject userProfile = new JSONObject();
                    try{
                        userProfile.put("facebookId",jsonObject.getLong("id"));
                        userProfile.put("name",jsonObject.getString("name"));
                        if(jsonObject.getString("gender")!=null){
                            userProfile.put("gender",jsonObject.getString("gender"));
                        }
                        /*if(jsonObject.getString("email")!=null){
                            userProfile.put("email",jsonObject.getString("email"));
                        }*/
                        // on sauvegarde le profil du user
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.put("profile",userProfile);
                        currentUser.saveInBackground();
                        //show the user infos
                        //todo func
                        updateViewsWithProfileInfo();
                    }
                    catch(JSONException e){
                        Log.d(app_init.TAG,"error parsing usr data."+ e);
                    }
                }
                else if(graphResponse.getError() !=null){
                    Log.d(app_init.TAG,"GRAPHRESPONSE SHIT " + graphResponse.getError());
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void updateViewsWithProfileInfo(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.has("profile")) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.has("facebookId")) {
                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureView.setProfileId(null);
                }

                if (userProfile.has("name")) {
                    userNameView.setText(userProfile.getString("name"));
                } else {
                    userNameView.setText("");
                }

                if (userProfile.has("gender")) {
                    userGenderView.setText(userProfile.getString("gender"));
                } else {
                    userGenderView.setText("");
                }

                if (userProfile.has("email")) {
                    userEmailView.setText(userProfile.getString("email"));
                } else {
                    userEmailView.setText("");
                }

            } catch (JSONException e) {
                Log.d(app_init.TAG, "Error parsing saved user data.");
            }
        }
    }
    public void onLogoutClick(View v){
        ParseUser.logOut();

        // Go to the login view
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    public void displaytoast(String _todisplay) {
        Toast.makeText(getApplicationContext(), _todisplay, Toast.LENGTH_LONG).show();
        //function used to debug stuff
    }



}
