package com.example.user.journalapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class NoteDetailsActivity extends AppCompatActivity {

    private String myNoteId;
    private DatabaseReference mDatabase;
    private TextInputEditText myNoteEditText;
    private DatabaseReference myNoteRef;
    private String myNote;
    private long noteTimestamp;
    private MenuItem deleteIcon;
    private TextView myNoteTimestampTxt;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        myNoteEditText = (TextInputEditText) findViewById(R.id.journal_edit_text);

        myNoteTimestampTxt = (TextView) findViewById(R.id.note_timestamp_txt);
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        // initializing the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myNoteRef = mDatabase.child(new JournalModel().getUid()).child("my_posts");
        SharedPreferences preferences = getSharedPreferences("EditCreds",MODE_PRIVATE);

        myNoteId = preferences.getString("noteId",null);
        if(myNoteId != null){
            noteId = myNoteId;
        }
        myNote = preferences.getString("myNote",null);
        noteTimestamp = preferences.getLong("noteTimestamp",0);


        // Making a new note in the journal
        if(myNote == null) {

            myNoteEditText.setText("");
            myNoteTimestampTxt.setText("");
//            hideDeleteIcon();
            invalidateOptionsMenu();



        }

        // Updating a note in the journal
        else{
            myNoteTimestampTxt.setText(formatTimeStamp(noteTimestamp));
            myNoteEditText.setText(myNote);
            getSupportActionBar().setTitle("Update note");

        }



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myNoteString = myNoteEditText.getText().toString();
                if (!TextUtils.isEmpty(myNoteString)) {
                    if (noteId != null) {
                        myNoteRef.child(noteId)
                                .setValue(new JournalModel
                                        (myNoteString,ServerValue.TIMESTAMP,noteId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(NoteDetailsActivity.this,
                                                "Note updated successfully",Toast.LENGTH_SHORT)
                                                .show();
                                        myNoteEditText.setText("");

                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NoteDetailsActivity.this,
                                        "Note update failed, try again please",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        } else {
                       final String key = myNoteRef.push().getKey();
                        myNoteRef.child(key).setValue
                                (new JournalModel(myNoteString, ServerValue.TIMESTAMP,key))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(NoteDetailsActivity.this,
                                        "Note created successfully",Toast.LENGTH_SHORT)
                                        .show();
                                myNoteEditText.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NoteDetailsActivity.this,
                                                "Note not created, try again please",Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }


                } else {
                    myNoteEditText.setError("Required");
                }

            }
        });


    }


    private String formatTimeStamp(long timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        return formatter.format(new Date(timestamp));
    }


//    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.


        getMenuInflater().inflate(R.menu.note_details_activity_menu, menu);
        deleteIcon = menu.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_note) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete note ?");
            builder.setMessage("Are you sure to continue");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                  myNoteRef.setValue(null)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Toast.makeText(NoteDetailsActivity.this,
                                  "Note successfully deleted",Toast.LENGTH_SHORT).show();
                          myNoteEditText.setText("");
                          myNoteTimestampTxt.setText("");
                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(NoteDetailsActivity.this,
                                  "Note not deleted",Toast.LENGTH_SHORT).show();
                      }
                  });


                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            builder.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
