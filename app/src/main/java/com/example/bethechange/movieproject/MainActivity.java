package com.example.bethechange.movieproject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bethechange.movieproject.Favorites.MoviesHelper;
import com.example.bethechange.movieproject.Fragments.DetailsFragment;
import com.example.bethechange.movieproject.Fragments.MainFragment;
import com.example.bethechange.movieproject.Interface.OnFavoriteStateChangeListener;
import com.example.bethechange.movieproject.Interface.OnMovieSelectionChangeListener;
import com.example.bethechange.movieproject.Model.MovieClass;
import com.example.bethechange.movieproject.Settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMovieSelectionChangeListener,OnFavoriteStateChangeListener {
    boolean mTwoPanel;
    MovieClass movieClass;
    MoviesHelper moviesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesHelper=new MoviesHelper(this);
//
        if (findViewById(R.id.details_container) != null) {
            mTwoPanel = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.details_container, new DetailsFragment())
                        .commit();
            }

        } else {
            mTwoPanel = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnSelectionChanged(MovieClass selectedMovie) {
        movieClass=selectedMovie;
        if(mTwoPanel){
            DetailsFragment detailsFragment=new DetailsFragment();
            detailsFragment.setMovieClass(movieClass);
            getSupportFragmentManager().beginTransaction().replace
                    (R.id.details_container,detailsFragment).commit();

        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("Movie", movieClass);
            startActivity(intent);

        }
    }

    @Override
    public void OnStateChanged(ArrayList<MovieClass>list) {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if(currentFragment instanceof MainFragment){
            ((MainFragment) currentFragment).changeFavorites(list);

        }

    }
}
