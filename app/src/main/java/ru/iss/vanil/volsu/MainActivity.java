package ru.iss.vanil.volsu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SparseArray<Stack<Fragment>> fragmentsStack;
    private int currentTabInNavBottom;
    private NewsFragment newsFragment;
    private RatingFragment ratingFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    showTab(R.id.navigation_news);
                    return true;
                case R.id.navigation_rating:
                    showTab(R.id.navigation_rating);
                    return true;
                    default:
                        Toast.makeText(getApplicationContext(), R.string.error_string, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentsStack = new SparseArray<>();
        fragmentsStack.put(R.id.navigation_news, new Stack<Fragment>());
        fragmentsStack.put(R.id.navigation_rating, new Stack<Fragment>());

        navigation.setSelectedItemId(R.id.navigation_news);
    }

    private void showTab(final int tabID) {
        currentTabInNavBottom = tabID;
        if (fragmentsStack.get(tabID).empty()) {
            switch (tabID) {
                case R.id.navigation_news:
                    newsFragment = new NewsFragment();
                    pushFragment(R.id.navigation_news, newsFragment);
                    showFragment(newsFragment);
                    break;
                case R.id.navigation_rating: {
                    ratingFragment = new RatingFragment();
                    pushFragment(R.id.navigation_rating, ratingFragment);
                    showFragment(ratingFragment);
                    break;
                }
                default:
                    Toast.makeText(getApplicationContext(), R.string.error_string, Toast.LENGTH_LONG).show();
            }
        } else showFragment(fragmentsStack.get(tabID).lastElement());
    }

    void pushFragment(final int tabID, final Fragment fragment) {
        fragmentsStack.get(tabID).push(fragment);
    }

    void showFragment(final Fragment fragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_content, fragment);
        fragmentTransaction.commit();
    }

    private void closeCurrentFragment() {
        fragmentsStack.get(currentTabInNavBottom).pop();
        Fragment fragment = fragmentsStack.get(currentTabInNavBottom).lastElement();
        showFragment(fragment);
    }

    //Fragments returns true if an action was done
    @Override
    public void onBackPressed() {
        switch (currentTabInNavBottom) {
            case R.id.navigation_news:
                if (newsFragment.onBackPressed()) return;
                break;
            case R.id.navigation_rating:
                if (ratingFragment.onBackPressed()) return;
                break;
            default:
                break;
        }
        if (fragmentsStack.get(currentTabInNavBottom).size() == 1) {
            finish();
        } else {
            closeCurrentFragment();
        }
    }


    @Override
    public void onClick(View v) {
        switch (currentTabInNavBottom) {
            case R.id.navigation_news:
                newsFragment.onSuperClick(v);
                break;
            case R.id.navigation_rating:
                ratingFragment.onSuperClick(v);
                break;
            default:
                Log.wtf(getString(R.string.log_tag), "Seriously... i don't know what say about this.");
                break;
        }
    }
}
