package com.example.user.journalapp;

import com.google.firebase.auth.FirebaseAuth;

public class JournalModel {
    private String journalNote;
    private Object journalNoteTimestamp;
    private String myNoteId;

    public JournalModel(){}

    public JournalModel(String journalNote, Object journalNoteTimestamp, String myNoteId) {
        this.journalNote = journalNote;
        this.journalNoteTimestamp = journalNoteTimestamp;
        this.myNoteId = myNoteId;
    }

    public String getJournalNote() { return journalNote; }

    public void setJournalNote(String journalNote) { this.journalNote = journalNote; }

    public Object getJournalNoteTimestamp() { return journalNoteTimestamp; }

    public void setJournalNoteTimestamp(Object journalNoteTimestamp) { this.journalNoteTimestamp = journalNoteTimestamp; }

    public String getMyNoteId() { return myNoteId; }

    public void setMyNoteId(String myNoteId) { this.myNoteId = myNoteId; }

    public static final String getUid() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
