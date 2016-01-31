package com.example.bethechange.movieproject.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bethechange.movieproject.Favorites.Data.MoviesHelper;
import com.example.bethechange.movieproject.Interface.OnFavoriteStateChangeListener;
import com.example.bethechange.movieproject.Model.MovieClass;
import com.example.bethechange.movieproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Be The Change on 12/18/2015.
 */

public class DetailsFragment extends Fragment {
    MovieClass movieClass;
    ArrayList<MovieClass> favoritesList;
    ArrayAdapter<String> arrayAdapter;
    TextView title;
    TextView year;
    TextView rate;
    TextView vote_count;
    TextView overview;
    CheckBox fav;
    ImageView poster;
    ExpandableListView reviewsListView;
    MoviesHelper moviesHelper;

    boolean isFav = false;
    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY = "?api_key=b8be982434834910f4662f9e5fb3bacb";
    HashMap<String, String> trailerseMap;
    HashMap<String, String> reviewseMap;
    android.support.v4.widget.NestedScrollView rootview;

    enum LinearListType {TRAILERS_LIST, REVIEWS_LIST}


    public DetailsFragment() {
        favoritesList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = (NestedScrollView) inflater.inflate(R.layout.fragment_details, container, false);
        return rootview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          moviesHelper=new MoviesHelper(getActivity());

        favoritesList=moviesHelper.loadFavoritesMovies();
     //   favoritesList = FavoritesHandler.loadFavorites(getActivity());
        if (favoritesList.contains(movieClass)) {
            isFav = true;


        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        MoviesHelper moviesHelper=new MoviesHelper(getActivity());
        if (fav != null && fav.isChecked() != isFav) {

            if (fav.isChecked()){
                moviesHelper.InsertMovie(movieClass);
             //   favoritesList.add(movieClass);
            }
            else {
                moviesHelper.DeleteMovie(movieClass.getId());
               // favoritesList.remove(movieClass);

            }


            isFav = fav.isChecked();
        }
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (movieClass != null) {
            init();
            Picasso.with(getActivity()).load(movieClass.getFullImgPath()).placeholder(R.drawable.thumbnail).error(R.drawable.thumbnail).into(poster);
            poster.setScaleType(ImageView.ScaleType.FIT_START);
        }

    }

    public void init() {

        String[] arr = reviewseMap.keySet().toArray(new String[reviewseMap.size()]);

        title = (TextView) getActivity().findViewById(R.id.title);
        {
            LinearLayout trailersList = (LinearLayout) getActivity().findViewById(R.id.trailerlist);
            String traillersArray[] = trailerseMap.keySet().toArray(new String[trailerseMap.size()]);
            buildListinLinearLayout(LinearListType.TRAILERS_LIST, traillersArray, trailersList);
            LinearLayout reviewsList = (LinearLayout) getActivity().findViewById(R.id.reviewslinearlist);
            String reviewsArray[] = reviewseMap.keySet().toArray(new String[reviewseMap.size()]);
            buildListinLinearLayout(LinearListType.REVIEWS_LIST, reviewsArray, reviewsList);
        }
        year = (TextView) getActivity().findViewById(R.id.year);
        rate = (TextView) getActivity().findViewById(R.id.rate);
        vote_count = (TextView) getActivity().findViewById(R.id.vote_count);
        overview = (TextView) getActivity().findViewById(R.id.overview);
        fav = (CheckBox) getActivity().findViewById(R.id.fav);
        fav.setChecked(isFav);
        if (MainFragment.isTablet(getActivity())) {
            fav.setOnClickListener(new View.OnClickListener() {
                ArrayList<MovieClass> arrayList = new ArrayList<>(favoritesList);

                @Override
                public void onClick(View v) {


                    if (!fav.isChecked())
                        arrayList.remove(movieClass);
                    else
                        arrayList.add(movieClass);

                    ((OnFavoriteStateChangeListener) getActivity()).OnStateChanged(arrayList);


                }
            });
        }
        poster = (ImageView) getActivity().findViewById(R.id.poster);
        title.setText(movieClass.getTitle());
        vote_count.setText(String.valueOf(movieClass.getVote_count()) + " votes");
        year.setText(movieClass.getRelease_date());
        rate.setText(String.valueOf(movieClass.getVote_average()) + " / 10");
        overview.setText(movieClass.getOverview());
        poster.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void setMovieClass(MovieClass movieClass) {
        if (this.movieClass == null)
            this.movieClass = movieClass;
        setMaps();

    }

    private void setMaps() {
        ArrayList<MovieClass.VideoInfo> tempVideos = movieClass.getVideosInfo();
        ArrayList<MovieClass.Review> tempReviews = movieClass.getReviews();
        trailerseMap = new HashMap<>();
        reviewseMap = new HashMap<>();
        for (int i = 0; i < tempReviews.size(); i++)
            reviewseMap.put(tempReviews.get(i).getName(), tempReviews.get(i).getContent());
        for (int i = 0; i < tempVideos.size(); i++)
            trailerseMap.put(tempVideos.get(i).getName(), tempVideos.get(i).getFullLink());


    }


    private void buildListinLinearLayout(LinearListType type, String[] data, LinearLayout list) {

        for (int i = 0; i < data.length || data.length == 0; i++) {
            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.rgb(139, 137, 137));
            textView.setBackgroundResource(android.R.drawable.list_selector_background);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            if (data.length != 0) {
                textView.setText(data[i]);
                if (type == LinearListType.TRAILERS_LIST) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = trailerseMap.get(((TextView) v).getText());
                            if (str.length() > 0) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
                            }


                        }
                    });
                } else if (type == LinearListType.REVIEWS_LIST) {
                    final TextView reviewContent = (TextView) getActivity().findViewById(R.id.reviewContent);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = reviewseMap.get(((TextView) v).getText());
                            if (reviewContent.getText() != null || !str.equals(reviewContent.getText()))
                                reviewContent.setText(str);

                        }
                    });
                }


            } else {
                textView.setText("No Data Available");
                list.addView(textView);
                break;
            }
            // View vi = inflater.inflate(R.layout.product_item, null);
            list.addView(textView);
        }

    }

}
