package com.example.bethechange.movieproject.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.bethechange.movieproject.Fragments.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieClass implements Parcelable {
    public float vote_average;
    String poster_path;
    boolean adult;
    String overview;
    String release_date;
    //Date releaseDate;
    int length;
    int[] genre_ids;
    int id;
    String original_title;
    String original_language;
    String title;
    String backdrop_path;
    float popularity;
    int vote_count;
    boolean video;
    ArrayList<VideoInfo>videosInfo;
    ArrayList<Review>reviews;
    String BASE_IMG_URL = "http://image.tmdb.org/t/p/w185/";
    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY = "?api_key=b8be982434834910f4662f9e5fb3bacb";
    @Override
    public boolean equals(Object o) {
        if(o instanceof  MovieClass){
           return this.id== ((MovieClass) o).getId();
        }
        return  false;
    }

    public MovieClass() {
    }


    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        if (overview.length() == 0)
            overview = "No Overview Available";
        return overview;
    }

    public void setOverView(String overView) {
        this.overview = overView;
    }

    public String getRelease_date() {
        if (release_date.length() == 0)
            release_date = "No Date Available";
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        if (original_title.length() == 0)
            original_title = "No Title Available";
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        if (title.length() == 0)
            title = "No Title Available";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getFullImgPath() {
        return BASE_IMG_URL + poster_path;

    }

   /* public ArrayList<String>getVideosNames(){
        ArrayList<String>names=new ArrayList<String>();
        for(int i=0;i<videosInfo.size();i++){
            names.add(videosInfo.get(i).getName());

        }
        return  names;
    }*/
    public ArrayList<VideoInfo> getVideosInfo() {
        if(videosInfo==null){
            videosInfo=new ArrayList<VideoInfo>();


        }

        loadTrailers();

        return videosInfo;


    }
    /*
    public void setVideosInfo(ArrayList<VideoInfo>videosInfo) {

            this.videosInfo=new ArrayList<VideoInfo>(videosInfo);

    }
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }*/

    public ArrayList<Review> getReviews() {
        if(reviews==null){
            reviews=new ArrayList<Review>();


        }
        loadReviews();
        return reviews;
    }

    protected MovieClass(Parcel in) {
        vote_average = in.readFloat();
        poster_path = in.readString();
        adult = in.readByte() != 0x00;
        overview = in.readString();
        release_date = in.readString();
        length = in.readInt();

        // genre_ids=new int[3];
        genre_ids = new int[length];
        in.readIntArray(genre_ids);
        id = in.readInt();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readFloat();
        vote_count = in.readInt();
        video = in.readByte() != 0x00;
        videosInfo=new ArrayList<VideoInfo>();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(vote_average);
        dest.writeString(poster_path);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(genre_ids.length);
        dest.writeIntArray(genre_ids);
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeFloat(popularity);
        dest.writeInt(vote_count);
        dest.writeByte((byte) (video ? 0x01 : 0x00));

    }
    private void loadReviews() {

        final String REVIEWS = "/reviews";
        String jsonStrReviews = "";
        Uri builtReviewsUri = Uri.parse(BASE_URL + getId() + REVIEWS + API_KEY);
        jsonStrReviews = MainFragment.buildJsonStr(builtReviewsUri.toString());
        if(jsonStrReviews==null){
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStrReviews);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                reviews.add(new MovieClass.Review(jsonArray.getJSONObject(i).getString("author"), jsonArray.getJSONObject(i).getString("content")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadTrailers() {
        final String VIDEOS = "/videos";
        String jsonStrTrailers = "";

        Uri builtTrailersUri = Uri.parse(BASE_URL + getId() + VIDEOS + API_KEY);

        jsonStrTrailers = MainFragment.buildJsonStr(builtTrailersUri.toString());
        if(jsonStrTrailers==null){
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStrTrailers);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                videosInfo.add(new MovieClass.VideoInfo(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("key")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieClass> CREATOR = new Parcelable.Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel in) {
            return new MovieClass(in);
        }

        @Override
        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };
    public static class VideoInfo{
        String key;
        String name;
        String fullLink;
        final String BASE_YOUTUBE_LINK="https://www.youtube.com/watch?v=";
       public VideoInfo(String name,String key){
           this.name=name;
           this.key=key;
           this.fullLink= BASE_YOUTUBE_LINK+key;
       }
        public String getkey() {
            return key;
        }

        public void setkey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof VideoInfo){
                return  ((VideoInfo)o).name==this.name;
            }
            return false;
        }

        public String getFullLink(){
            return fullLink;


        }
    }
    public static class Review{
        String content;
        String name;
        final String BASE_YOUTUBE_LINK="https://www.youtube.com/watch?v=";
        public Review(String name,String content){
            this.name=name;
            this.content=content;
        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof VideoInfo){
                return  ((VideoInfo)o).name==this.name;
            }
            return false;
        }

    }
}