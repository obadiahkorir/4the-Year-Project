package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 10/23/2018.
 */

public class Chats {
    private String mName;
    private String mMessage;
    private String mUid;

    public Chats() {}  // Needed for Firebase

    public Chats(String name, String message, String uid) {
        mName = name;
        mMessage = message;
        mUid = uid;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public String getMessage() { return mMessage; }

    public void setMessage(String message) { mMessage = message; }

    public String getUid() { return mUid; }

    public void setUid(String uid) { mUid = uid; }
}