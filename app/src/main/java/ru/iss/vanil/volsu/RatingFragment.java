package ru.iss.vanil.volsu;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    private int firstSemester, lastSemester, currentSemester;
    private String recordBook;
    private SharedPreferences sharedPreferences;
    private AppStructures.DataClass studentData;
    private ArrayList<AppStructures.Subject> subjects;
    private AppAdapters.CardViewRecyclerAdapter cardViewRecyclerAdapter;

    public RatingFragment() {
        //requires an empty constructor by fragment super class
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subjects = new ArrayList<>();
        //get data
        getStudentData();
        getStudentDataFromDatabase();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rating_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cardViewRecyclerAdapter = new AppAdapters.CardViewRecyclerAdapter(getContext(), subjects);
        recyclerView.setAdapter(cardViewRecyclerAdapter);

        return view;
    }

    private void getStudentDataFromDatabase() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabaseHelper databaseHelper = new AppDatabaseHelper(getContext());
                SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query(AppR.constantsDatabaseSQLite.TABLE_NAME, null,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    ArrayList<AppStructures.Subject> subjectsDataBase = new ArrayList<>();
                    //int idColumnIndex = cursor.getColumnIndex(AppR.constantsDatabaseSQLite.COLUMN_FIRST);
                    int subjectColumnIndex = cursor.getColumnIndex(AppR.constantsDatabaseSQLite.COLUMN_SECOND);
                    int pointColumnIndex = cursor.getColumnIndex(AppR.constantsDatabaseSQLite.COLUMN_THIRD);
                    do {
                        AppStructures.Subject subject =
                                new AppStructures.Subject(cursor.getString(subjectColumnIndex),
                                        cursor.getInt(pointColumnIndex));
                        subjectsDataBase.add(subject);
                    } while (cursor.moveToNext());
                    updateDataOnView(subjectsDataBase);
                } else {
                    getStudentDataFromInternet();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        });
        thread.start();
    }

    private void getStudentData() {
        sharedPreferences = getContext()
                .getSharedPreferences(AppR.constantsSharedPreferences.STUDENT_DATA, MODE_PRIVATE);
        firstSemester = sharedPreferences.
                getInt(AppR.constantsSharedPreferences.STUDENT_DATA_FIRST_SEMESTER, 1);
        lastSemester = sharedPreferences
                .getInt(AppR.constantsSharedPreferences.STUDENT_DATA_LAST_SEMESTER, 8);
        currentSemester = sharedPreferences
                .getInt(AppR.constantsSharedPreferences.STUDENT_DATA_CURRENT_SEMESTER, 1);
        recordBook = sharedPreferences
                .getString(AppR.constantsSharedPreferences.STUDENT_DATA_RECORD_BOOK, null);
    }

    private void getStudentDataFromInternet() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    studentData = AppNetTools.getDataOfStudent(recordBook, currentSemester);
                    updateDataOnView();
                    AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getContext());
                    SQLiteDatabase sqLiteDatabase = appDatabaseHelper.getWritableDatabase();
                    sqLiteDatabase.delete(AppR.constantsDatabaseSQLite.TABLE_NAME, null, null);
                    ContentValues contentValues = new ContentValues();
                    for (AppStructures.Subject subject : subjects) {
                        contentValues.put(AppR.constantsDatabaseSQLite.COLUMN_SECOND, subject.getSubjectName());
                        contentValues.put(AppR.constantsDatabaseSQLite.COLUMN_THIRD, subject.getPoint());
                        sqLiteDatabase.insert(AppR.constantsDatabaseSQLite.TABLE_NAME, null, contentValues);
                    }
                    sqLiteDatabase.close();
                } catch (Exception e) {
                    Looper.prepare();
                    if (e.getMessage().equals(AppR.constants.ERROR_SERVER_BUSY))
                        showToast(getString(R.string.error_server_busy));
                    else showToast(null);
                    Log.e(getString(R.string.log_tag), e.getMessage());
                }
            }
        });
        thread.start();
    }

    private void updateDataOnView() {
        subjects.clear();
        AppStructures.DataClass.Data data = studentData.dataParsed;
        for (int i = 0; i < data.predmet.length; i++) {
            subjects.add(new AppStructures.Subject(data.predmet[i], data.ball[i]));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardViewRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateDataOnView(ArrayList<AppStructures.Subject> subject) {
        subjects.clear();
        subjects.addAll(subject);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardViewRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    void showToast(String message) {
        if (message == null) message = getString(R.string.error_string);
        final String m = message;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onSuperClick(View v) {
        switch (v.getId()) {
            case R.id.refreshButton:
                getStudentDataFromInternet();
                break;
            default:
                Log.wtf(getString(R.string.error_string), "How you did it????");
        }
    }

    //calls from MainActivity
    public boolean onBackPressed() {
        return false;
    }
}


/*testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                TestFragment testFragment = new TestFragment();
                if (mainActivity != null) {
                    mainActivity.pushFragment(R.id.navigation_rating, testFragment);
                    mainActivity.showFragment(testFragment);
                } else Toast.makeText(getContext(), R.string.error_string, Toast.LENGTH_LONG).show();
            }
        });*/