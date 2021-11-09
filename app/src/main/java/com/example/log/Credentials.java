package com.example.log;

import java.util.HashMap;
import java.util.Map;

public class Credentials {
    //hash map to store credentials, stores as keys
    private HashMap<String, String> credentialMapper = new HashMap<String, String>();
    //add credentials to store
    public void addCredentials(String username, String password){
        credentialMapper.put(username, password);
    }
    //checking for credentials and to find matching keys to sign in
    public boolean checkUsername(String username){
        return credentialMapper.containsKey(username);
    }

    public boolean checkCredentials(String username, String password){

        if(credentialMapper.containsKey(username)){
            return password.equals(credentialMapper.get(username));
        }

        return false;
    }
    //loads the credentials of last user login when remember me checkbox is clicked
    public void loadCredentials(Map<String, ?> preferencesMap){
        for(Map.Entry<String, ?> entries : preferencesMap.entrySet()){
            if(!entries.getKey().equals("RememberMeCheckbox") || !entries.getKey().equals("LastSavedUsername") ||
                    !entries.getKey().equals("LastSavedPassword")){
                credentialMapper.put(entries.getKey(), entries.getValue().toString());
            }
        }
    }
}
