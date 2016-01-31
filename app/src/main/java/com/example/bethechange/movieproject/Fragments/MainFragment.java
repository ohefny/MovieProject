package com.example.bethechange.movieproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bethechange.movieproject.Adapter.ImageAdapter;
import com.example.bethechange.movieproject.Favorites.Data.MoviesHelper;
import com.example.bethechange.movieproject.FetchThread.FetchTask;
import com.example.bethechange.movieproject.Interface.OnMovieSelectionChangeListener;
import com.example.bethechange.movieproject.Model.MovieClass;
import com.example.bethechange.movieproject.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/**
 * Created by Be The Change on 1/17/2016.
 */
public class MainFragment extends Fragment {
    ArrayList<MovieClass> movieClassesList;
    ArrayList<MovieClass> arrayList;
    ArrayList<MovieClass> favoritesList;
    final String KEY_PARAM = "&api_key=b8be982434834910f4662f9e5fb3bacb";
    final String BASEURL_PARAM = "http://api.themoviedb.org/3/discover/movie?page=";
    final String SORT_BASE = "&sort_by=";
    String sortBy = "popularity";
    String orderBy = ".desc";
    int loadedPages = 1;
    int visibleIndex;
    GridView grid;
    int maxPages = 12618;
    ImageAdapter imageAdapter;
    SharedPreferences sharedPreferences;
    boolean cachedDatePresent = false;
    MoviesHelper moviesHelper;

    public MainFragment() {
        movieClassesList = new ArrayList<>();
        arrayList = new ArrayList<>();

        favoritesList = new ArrayList<>();

    }
    public void changeFavorites(ArrayList<MovieClass>list){
        if(!sortBy.equals("favorites")&&!cachedDatePresent)
            return;
        movieClassesList=(ArrayList<MovieClass>)list.clone();
        imageAdapter=new ImageAdapter(getActivity(),movieClassesList);
        int first = grid.getFirstVisiblePosition();
        buildGrid();
        grid.setSelection(first);
    }


    @Override
    public void onStart() {
        super.onStart();

        
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = sharedPreferences.getString(getString(R.string.pref_sort), getString(R.string.sort_via_popularity));



         if (imageAdapter != null) {
            if (sort.equals("favorites")) {
                movieClassesList= moviesHelper.loadFavoritesMovies();
                sortBy = sort;
                imageAdapter = new ImageAdapter(getActivity(), movieClassesList);
                buildGrid();
            } else if (!sortBy.equals(sort)  || cachedDatePresent) {
                loadedPages=1;
                sortBy = sort;
                cachedDatePresent=false;
                buildMoviesArray(buildJsonStr(getURlInString()), true);
                imageAdapter.notifyDataSetChanged();
                buildGrid();

            }
        } else {
             loadedPages=1;

            sortBy = sort;
             String jsonStr = buildJsonStr(getURlInString());
            if (sort.equals("favorites"))
                movieClassesList= moviesHelper.loadFavoritesMovies();
            else
                buildMoviesArray(jsonStr, true);
            imageAdapter = new ImageAdapter(getActivity(), movieClassesList);
            buildGrid();
             if(isTablet(getActivity()) &&movieClassesList!=null&&movieClassesList.size()>0){
                 ((OnMovieSelectionChangeListener) getActivity()).OnSelectionChanged(movieClassesList.get(0));
             }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesHelper = new MoviesHelper(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int first = grid.getFirstVisiblePosition();

        buildGrid();
        grid.setSelection(first);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {


        }
        return super.onOptionsItemSelected(item);
    }

    public String getURlInString() {
        Uri builtUri = Uri.parse(BASEURL_PARAM + loadedPages + SORT_BASE + sortBy + orderBy + KEY_PARAM);

        return builtUri.toString();
    }

    public void buildMoviesArray(String jsonStr, boolean clear) {
        ObjectMapper objectMapper = new ObjectMapper();



        try {

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            maxPages = jsonObject.getInt("total_pages");

            if (movieClassesList.size() > 0 && clear){
                movieClassesList.clear();
                cachedDatePresent=false;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                movieClassesList.add(objectMapper.readValue(jsonArray.getJSONObject(i).toString(), MovieClass.class));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){

            Toast.makeText(getActivity(),"Connection Problem",Toast.LENGTH_LONG).show();
            if(movieClassesList==null||movieClassesList.size()==0){
                Toast.makeText(getActivity(),"Loading Favorite Movies ",Toast.LENGTH_LONG).show();
                movieClassesList= moviesHelper.loadFavoritesMovies();
                if(imageAdapter!=null)
                imageAdapter.notifyDataSetChanged();
                //buildGrid();
                cachedDatePresent=true;

            }
            else{
                Toast.makeText(getActivity(),"Loading Cached Movies",Toast.LENGTH_LONG).show();

            }

        }

    }


    public void buildGrid() {
        grid = (GridView) getActivity().findViewById(R.id.gridView1);

        grid.setAdapter(imageAdapter);
        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (!sortBy.equals("favorites") && loadedPages < maxPages && firstVisibleItem + visibleItemCount == totalItemCount) {

                    loadedPages++;
                    String jsonStr = buildJsonStr(getURlInString());
                    if (jsonStr != null) {

                        buildMoviesArray(jsonStr, cachedDatePresent);
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        loadedPages--;
                    }

                }
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OnMovieSelectionChangeListener listener = (OnMovieSelectionChangeListener) getActivity();
                listener.OnSelectionChanged(movieClassesList.get(position));

            }
        });

    }

    public static String buildJsonStr(String url) {
        String jsonStr = null;
        FetchTask fetchTask = new FetchTask();

        try {
            jsonStr = fetchTask.execute(url).get().toString();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }


        return jsonStr;
    }
  public static boolean isTablet(Context context) {
      return (context.getResources().getConfiguration().screenLayout
              & Configuration.SCREENLAYOUT_SIZE_MASK)
              >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }

}
