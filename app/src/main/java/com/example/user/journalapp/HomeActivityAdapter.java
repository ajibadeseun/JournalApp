package com.example.user.journalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {
    private Context mComtext;
    private List<JournalModel> mJournalModelList;

    public HomeActivityAdapter(@NonNull Context comtext, List<JournalModel> journalModelList) {
        this.mComtext = comtext;
        this.mJournalModelList = journalModelList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView journalNoteTxt;
        private final TextView journalNoteTimeTxt;
        private final CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            journalNoteTxt = (TextView) itemView.findViewById(R.id.journal_note);
            journalNoteTimeTxt = (TextView) itemView.findViewById(R.id.journal_time_txt);
            cardView = (CardView) itemView.findViewById(R.id.cardview_layout);


        }
    }

    @NonNull
    @Override
    public HomeActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listBuses = inflater.inflate(R.layout.home_inflater_layout_view, parent, false);
        return new HomeActivityAdapter.ViewHolder(listBuses);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeActivityAdapter.ViewHolder holder, int position) {
        final JournalModel journalModel = mJournalModelList.get(position);
        holder.journalNoteTxt.setText(journalModel.getJournalNote());
        holder.journalNoteTimeTxt.setText(formatTimeStamp((Long) journalModel.getJournalNoteTimestamp()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = mComtext.getSharedPreferences("EditCreds",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("noteId",journalModel.getMyNoteId());
                editor.putString("myNote",journalModel.getJournalNote());
                editor.putLong("noteTimestamp",(Long) journalModel.getJournalNoteTimestamp());
                editor.apply();

                mComtext.startActivity(new Intent(mComtext,NoteDetailsActivity.class));
            }
        });


    }

    private String formatTimeStamp(long timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        return formatter.format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return mJournalModelList.size();
    }
}
