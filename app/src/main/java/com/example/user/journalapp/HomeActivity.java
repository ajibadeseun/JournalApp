package com.example.user.journalapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView journalRecyclerView;
    private RelativeLayout emptyListView;
    private List<JournalModel> getNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getNotes = new ArrayList<>();


        journalRecyclerView = (RecyclerView) findViewById(R.id.home_recyclerview);

        emptyListView = (RelativeLayout) findViewById(R.id.empty_view);


        journalRecyclerView.setHasFixedSize(true);
        journalRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myNoteRef = mDatabase.child(new JournalModel().getUid()).child("my_posts");
        try {
            getJournalList(myNoteRef, new GetJournalList() {
                @Override
                public void onCallback(List<JournalModel> journalModels) {

                    if (journalModels.size() > 0) {
                        emptyListView.setVisibility(View.GONE);

                        HomeActivityAdapter adapter = new HomeActivityAdapter
                                (HomeActivity.this, journalModels);
                        adapter.notifyDataSetChanged();
                        journalRecyclerView.setAdapter(adapter);


                    } else {
                        emptyListView.setVisibility(View.VISIBLE);
                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, "Exception: " + e, Toast.LENGTH_SHORT).show();

        }


//        List<JournalModel> getJournalList = new ArrayList<>();
//        getJournalList.add(new JournalModel("Hello there Journal app"
//                , System.currentTimeMillis(), "xxVgzgysijz"));
//        getJournalList.add(new JournalModel("We're writing to the database now"
//                , System.currentTimeMillis(), "hifBIOikdlsdnl"));
//        getJournalList.add(new JournalModel("Our app will be ready soon"
//                , System.currentTimeMillis(), "hjdewheoeofh<Bkddk"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("EditCreds", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("noteId",null);
                editor.putString("myNote",null);
                editor.putLong("noteTimestamp",0);
                editor.apply();

                startActivity(new Intent(HomeActivity.this,NoteDetailsActivity.class));
            }
        });
    }

    public interface GetJournalList {
        void onCallback(List<JournalModel> journalModels);
    }

    private void getJournalList(DatabaseReference reference, final GetJournalList getJournalList) {



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getNotes.clear();
                    for (DataSnapshot postNoteSnapShot : dataSnapshot.getChildren()) {
                        JournalModel model = postNoteSnapShot.getValue(JournalModel.class);
                        getNotes.add(model);
//                        HomeActivityAdapter adapter = new HomeActivityAdapter(HomeActivity.this, getNotes);
//                        journalRecyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();

                    }
                    getJournalList.onCallback(getNotes);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,
                        "Error: " + databaseError, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.ic_menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(HomeActivity.this, "" + e, Toast.LENGTH_LONG).show();
                            Log.e("Error","error: "+e);
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
