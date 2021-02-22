package ru.iss.vanil.volsu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class AppAdapters {

    public static class MySpinnerAdapter extends ArrayAdapter<String> {

        MySpinnerAdapter(@NonNull Context context, @NonNull String[] objects) {
            super(context, android.R.layout.simple_spinner_item, AppTools.getStringArrayWithFirstUnActive(context, objects));
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        MySpinnerAdapter(@NonNull Context context, @NonNull List<String> objects) {
            super(context, android.R.layout.simple_spinner_item, AppTools.getListStringArrayWithFirstUnActive(context, objects));
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public boolean isEnabled(int position) {
            return position != 0;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            TextView tv = (TextView) view;
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            } else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ((TextView) view).setTextColor(Color.DKGRAY);
            return view;
        }
    }

    public static class CardViewRecyclerAdapter extends RecyclerView.Adapter<CardViewRecyclerAdapter.ViewHolder> {

        private ArrayList<AppStructures.Subject> subjects;
        private Context context;

        public CardViewRecyclerAdapter(Context context, ArrayList<AppStructures.Subject> subjects) {
            this.context = context;
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_subject, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            TextView subjectTV = viewHolder.subjectTV;
            TextView pointTV = viewHolder.pointsTV;
            subjectTV.setText(subjects.get(i).getSubjectName());
            pointTV.setText(String.format(Locale.getDefault(), "%s %d",
                    context.getString(R.string.rating_points_word), subjects.get(i).getPoint()));
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subjectTV;
            TextView pointsTV;

            ViewHolder(View view) {
                super(view);
                this.subjectTV = itemView.findViewById(R.id.rating_subject);
                this.pointsTV = itemView.findViewById(R.id.rating_points);
            }
        }
    }

}
