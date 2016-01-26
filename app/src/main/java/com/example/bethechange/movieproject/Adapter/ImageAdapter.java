package com.example.bethechange.movieproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.bethechange.movieproject.Model.MovieClass;
import com.example.bethechange.movieproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Be The Change on 12/12/2015.
 */
public class ImageAdapter extends BaseAdapter {
    ArrayList<MovieClass> movieClassesList;
    private Context adpContext;

    public ImageAdapter(Context context,ArrayList<MovieClass>movieClassesList){
        adpContext=context;

        this.movieClassesList=movieClassesList;
    }
    @Override
    public int getCount() {
        return movieClassesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) adpContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=convertView;

        ImageView imageView=null;
        if(convertView==null){
            view=new View(adpContext);
            view=inflater.inflate(R.layout.item,null);
            imageView=(ImageView)view.findViewById(R.id.imageView1);

          //  imageView.setImageResource(R.drawable.thumbnail);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


            view.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));



            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundColor(Color.BLUE);
            view.setTag(imageView);
        }
        else
        {
            view =  convertView;
            imageView=(ImageView)view.getTag();
        }


        Picasso.with(adpContext)
                .load(movieClassesList.get(position).getFullImgPath().trim())
                .placeholder(R.drawable.thumbnail).error(R.drawable.thumbnail)
                .into(imageView);


        return view;



    }
}
