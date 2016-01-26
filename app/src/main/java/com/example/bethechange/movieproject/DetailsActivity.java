package com.example.bethechange.movieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.bethechange.movieproject.Fragments.DetailsFragment;
import com.example.bethechange.movieproject.Model.MovieClass;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        MovieClass movieClass = i.getExtras().getParcelable("Movie");


        if (savedInstanceState == null) {
            DetailsFragment detailsFragment=new DetailsFragment();
            detailsFragment.setMovieClass(movieClass);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, detailsFragment)
                    .commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
