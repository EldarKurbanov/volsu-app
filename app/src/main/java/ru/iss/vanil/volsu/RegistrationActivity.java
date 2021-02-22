package ru.iss.vanil.volsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner instituteChoice, courseChoice, nameGroupChoice, recordBookChoice, semesterChoice;
    private ArrayList<String> listCourses, listNameGroups, listRecordBooks, listSemesters;
    private ArrayMap<String, String> courseMap, nameGroupsMap;
    private Button saveButton;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        sharedPreferences = getSharedPreferences(AppR.constantsSharedPreferences.STUDENT_DATA,
                MODE_PRIVATE);
        if (sharedPreferences.getString(AppR.constantsSharedPreferences.STUDENT_DATA_RECORD_BOOK, null) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        setupRegistrationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupRegistrationView() {

        LinearLayout registrationFieldsLinearLayout = findViewById(R.id.registrationFieldsLinearLayout);
        Context context = getApplicationContext();

        //Fields

        //Institute
        AppViews.RegistrationTextView tvInstitute =
                new AppViews.RegistrationTextView(context,
                        new AppStructures.Margins(0, 0, 0, 10));
        tvInstitute.setText(R.string.registration_tv_institute);
        registrationFieldsLinearLayout.addView(tvInstitute);
        instituteChoice = new Spinner(context);
        instituteChoice.setOnItemSelectedListener(this);
        instituteChoice.setPopupBackgroundResource(android.R.color.white);
        instituteChoice.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        instituteChoice.setId(AppR.id.spinner_university);
        AppAdapters.MySpinnerAdapter instituteSpinnerAdapter =
                new AppAdapters.MySpinnerAdapter(context,
                        getResources().getStringArray(R.array.institutes));
        instituteChoice.setAdapter(instituteSpinnerAdapter);
        registrationFieldsLinearLayout.addView(instituteChoice);

        //Course
        AppViews.RegistrationTextView tvCourse =
                new AppViews.RegistrationTextView(context,
                        new AppStructures.Margins(0, 20, 0, 10));
        tvCourse.setText(R.string.registration_tv_name_plan);
        registrationFieldsLinearLayout.addView(tvCourse);
        listCourses = new ArrayList<>();
        courseChoice = new Spinner(context);
        courseChoice.setOnItemSelectedListener(this);
        courseChoice.setPopupBackgroundResource(android.R.color.white);
        courseChoice.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        courseChoice.setId(AppR.id.spinner_course);
        AppAdapters.MySpinnerAdapter courseSpinnerAdapter =
                new AppAdapters.MySpinnerAdapter(context, listCourses);
        courseChoice.setAdapter(courseSpinnerAdapter);
        registrationFieldsLinearLayout.addView(courseChoice);

        //Name group
        AppViews.RegistrationTextView tvGroupName =
                new AppViews.RegistrationTextView(context,
                        new AppStructures.Margins(0, 20, 0, 10));
        tvGroupName.setText(R.string.registration_tv_name_group);
        registrationFieldsLinearLayout.addView(tvGroupName);
        listNameGroups = new ArrayList<>();
        nameGroupChoice = new Spinner(context);
        nameGroupChoice.setOnItemSelectedListener(this);
        nameGroupChoice.setPopupBackgroundResource(android.R.color.white);
        nameGroupChoice.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        nameGroupChoice.setId(AppR.id.spinner_name_group);
        AppAdapters.MySpinnerAdapter nameGroupSpinnerAdapter =
                new AppAdapters.MySpinnerAdapter(context, listNameGroups);
        nameGroupChoice.setAdapter(nameGroupSpinnerAdapter);
        registrationFieldsLinearLayout.addView(nameGroupChoice);

        //Record book
        AppViews.RegistrationTextView tvRecordBook =
                new AppViews.RegistrationTextView(context,
                        new AppStructures.Margins(0, 20, 0, 10));
        tvRecordBook.setText(R.string.registration_tv_record_book);
        registrationFieldsLinearLayout.addView(tvRecordBook);
        listRecordBooks = new ArrayList<>();
        recordBookChoice = new Spinner(context);
        recordBookChoice.setOnItemSelectedListener(this);
        recordBookChoice.setPopupBackgroundResource(android.R.color.white);
        recordBookChoice.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        recordBookChoice.setId(AppR.id.spinner_record_book);
        AppAdapters.MySpinnerAdapter recordBookSpinnerAdapter =
                new AppAdapters.MySpinnerAdapter(context, listRecordBooks);
        recordBookChoice.setAdapter(recordBookSpinnerAdapter);
        registrationFieldsLinearLayout.addView(recordBookChoice);

        //Semester
        AppViews.RegistrationTextView tvSemester =
                new AppViews.RegistrationTextView(context,
                        new AppStructures.Margins(0, 20, 0, 10));
        tvSemester.setText(R.string.registration_tv_semester);
        registrationFieldsLinearLayout.addView(tvSemester);
        listSemesters = new ArrayList<>();
        semesterChoice = new Spinner(context);
        semesterChoice.setOnItemSelectedListener(this);
        semesterChoice.setPopupBackgroundResource(android.R.color.white);
        semesterChoice.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
        semesterChoice.setId(AppR.id.spinner_semester);
        AppAdapters.MySpinnerAdapter semestersSpinnerAdapter =
                new AppAdapters.MySpinnerAdapter(context, listSemesters);
        semesterChoice.setAdapter(semestersSpinnerAdapter);
        registrationFieldsLinearLayout.addView(semesterChoice);

        //Save button
        saveButton = new Button(context);
        saveButton.setLayoutParams(AppTools.getLinearLayoutParams(context, 0, 20, 0,10));
        saveButton.setText(R.string.registration_button_save);
        saveButton.setId(AppR.id.button_save);
        saveButton.setOnClickListener(this);
        saveButton.setEnabled(false);
        registrationFieldsLinearLayout.addView(saveButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case AppR.id.button_save:
                saveStudentData();
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            default:
                Toast.makeText(getApplicationContext(), R.string.error_string, Toast.LENGTH_LONG).show();
                Log.wtf(getString(R.string.log_tag), "There is no handler for button in " + getLocalClassName());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) return;
        final int p = position - 1;
        switch (parent.getId()) {
            case AppR.id.spinner_university:
                courseChoice.setSelection(0);
                nameGroupChoice.setSelection(0);
                recordBookChoice.setSelection(0);
                semesterChoice.setSelection(0);
                saveButton.setEnabled(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            courseMap = AppNetTools.getCoursesByInstitute(p);
                            AppTools.clearListWithHintInSpinner(getApplicationContext(), listCourses);
                            listCourses.addAll(courseMap.keySet());
                        } catch (Exception e) {
                            showToast(null);
                            Log.e(getString(R.string.log_tag), e.getMessage());
                            showMessageIfServerBusy(e);
                        }
                    }
                });
                thread.start();
                break;
            case AppR.id.spinner_course:
                nameGroupChoice.setSelection(0);
                recordBookChoice.setSelection(0);
                semesterChoice.setSelection(0);
                saveButton.setEnabled(false);
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nameGroupsMap = AppNetTools.getGroupNamesByCourse(courseMap.get
                                    (courseChoice.getSelectedItem().toString()));
                            AppTools.clearListWithHintInSpinner(getApplicationContext(), listNameGroups);
                            listNameGroups.addAll(nameGroupsMap.keySet());
                        } catch (Exception e) {
                            showToast(null);
                            Log.e(getString(R.string.log_tag), e.getMessage());
                            showMessageIfServerBusy(e);
                        }
                    }
                });
                thread1.start();
                break;
            case AppR.id.spinner_name_group:
                recordBookChoice.setSelection(0);
                semesterChoice.setSelection(0);
                saveButton.setEnabled(false);
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AppTools.clearListWithHintInSpinner(getApplicationContext(), listRecordBooks);
                            listRecordBooks.addAll(AppNetTools.getRecordBooksByNameGroup(nameGroupsMap.get
                                    (nameGroupChoice.getSelectedItem().toString())));
                        } catch (Exception e) {
                            showToast(null);
                            Log.e(getString(R.string.log_tag), e.getMessage());
                            showMessageIfServerBusy(e);
                        }
                    }
                });
                thread2.start();
                break;
            case AppR.id.spinner_record_book:
                semesterChoice.setSelection(0);
                saveButton.setEnabled(false);
                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AppTools.clearListWithHintInSpinner(getApplicationContext(), listSemesters);
                            listSemesters.addAll(AppNetTools.getSemestersByRecordBook
                                    (recordBookChoice.getSelectedItem().toString()));
                        } catch (Exception e) {
                            showToast(null);
                            Log.e(getString(R.string.log_tag), e.getMessage());
                            showMessageIfServerBusy(e);
                        }
                    }
                });
                thread3.start();
                break;
            case AppR.id.spinner_semester:
                if (p == -1) saveButton.setEnabled(false);
                else saveButton.setEnabled(true);
                break;
            default:
                showToast(null);
                Log.wtf(getString(R.string.log_tag), "There is no handler for spinner in " + getLocalClassName());
        }
    }

    private void showMessageIfServerBusy(Exception e) {
        if (e.getMessage().equals(AppR.constants.ERROR_SERVER_BUSY)) {
            showToast(getString(R.string.error_server_busy));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing...
    }

    void showToast(String message) {
        if (message == null) message = getString(R.string.error_string);
        final String m = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveStudentData() {
        String instituteName = instituteChoice.getSelectedItem().toString();
        String courseName = courseChoice.getSelectedItem().toString();
        String groupName = nameGroupChoice.getSelectedItem().toString();
        String recordBook = recordBookChoice.getSelectedItem().toString();
        int currentSemester = Integer.parseInt(semesterChoice.getSelectedItem().toString());
        int firstSemester = Integer.parseInt(listSemesters.get(1));
        int lastSemester = Integer.parseInt(listSemesters.get(listSemesters.size() - 1));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppR.constantsSharedPreferences.STUDENT_DATA_INSTITUTE_NAME, instituteName);
        editor.putString(AppR.constantsSharedPreferences.STUDENT_DATA_COURSE_NAME, courseName);
        editor.putString(AppR.constantsSharedPreferences.STUDENT_DATA_GROUP_NAME, groupName);
        editor.putString(AppR.constantsSharedPreferences.STUDENT_DATA_RECORD_BOOK, recordBook);
        editor.putInt(AppR.constantsSharedPreferences.STUDENT_DATA_CURRENT_SEMESTER, currentSemester);
        editor.putInt(AppR.constantsSharedPreferences.STUDENT_DATA_FIRST_SEMESTER, firstSemester);
        editor.putInt(AppR.constantsSharedPreferences.STUDENT_DATA_LAST_SEMESTER, lastSemester);
        editor.apply();
    }

}
