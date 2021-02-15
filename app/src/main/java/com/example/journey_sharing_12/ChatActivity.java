package com.example.journey_sharing_12;
import com.example.journey_sharing_12.PermissionTool;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText edtInput;
    ListView lvDiscussion;
    ArrayList<String> listConversations = new ArrayList<String>();
    ArrayAdapter arrayAdapt;

    String UserName, selectedTopic,user_msg_key;

    private DatabaseReference dbr ;

    private Uri camera_uri;
    private File cameraFile;
    private String cameraFileName;
    private String cameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        edtInput = (EditText) findViewById(R.id.edtInput);

        lvDiscussion = (ListView) findViewById(R.id.Discussion);
        arrayAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listConversations);
        lvDiscussion.setAdapter(arrayAdapt);

        UserName = getIntent().getExtras().get("user_name").toString();
        selectedTopic = getIntent().getExtras().get("selected_topic").toString();
        setTitle("Topic : "+selectedTopic);

        dbr= FirebaseDatabase.getInstance().getReference().child(selectedTopic);
        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void updateConversation(DataSnapshot snapshot){
        String msg, user,conversation;
        Iterator i = snapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();

            conversation = user + ": " + msg;
            arrayAdapt.insert(conversation, arrayAdapt.getCount());
            arrayAdapt.notifyDataSetChanged();
        }
    }

    public void fabSend(View view) {
        Map<String,Object> map = new HashMap<String, Object>();
        user_msg_key = dbr.push().getKey();
        dbr.updateChildren(map);

        DatabaseReference dbr2 = dbr.child(user_msg_key);
        Map<String, Object> map2 = new  HashMap<String, Object>();
        map2.put("msg",edtInput.getText().toString());
        map2.put("user",UserName);
        dbr2.updateChildren(map2);

    }

    public void fabAlbum(View view) {
        if(checkAlbumPermission()){
            //....

        }
        else{
            PermissionTool.getInstance().requestReadExternalStoragePermission(this);

        }
    }

    private boolean checkAlbumPermission() {
        if (PermissionTool.getInstance().isWriteExternalStorageGranted(this)
                && PermissionTool.getInstance().isCameraGranted(this)) {
            return true;
        } else {
            return false;
        }
    }
}