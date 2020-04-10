package com.example.ep.myapplication.Activitys.Adapters;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.example.ep.myapplication.Activitys.Activitys.MainActivity;
import com.example.ep.myapplication.Activitys.Fragments.mainFirstFragment;
import com.example.ep.myapplication.Activitys.Model.State;

import com.example.ep.myapplication.R;


import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by EP on 19/07/2017.
 */

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateAdapterViewHolder> {
    private ArrayList<State> data;
    private Context context;
    private String nameFragment;


    public StateAdapter(FragmentActivity activity, ArrayList<State> allstates) {
        data = allstates;
        context = activity.getApplicationContext();
        Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fregment_container);

        nameFragment=f.getClass().getCanonicalName();

    }

    @NonNull
    @Override
    public StateAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater theInflater = LayoutInflater.from(parent.getContext());
        View theView = theInflater.inflate(R.layout.rowlayout, parent, false);
        return new StateAdapterViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StateAdapterViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewnativeName = holder.textViewnativeName;
        ImageView flagIcon=holder.flagIcon;

        textViewName.setText(data.get(position).getName());
        textViewnativeName.setText(data.get(position).getNativeName());
        String path=data.get(position).getFlag();
        System.out.println(position);
        System.out.println(data.get(position).getName());
        System.out.println(data.get(position).getFlag());
        if (path!=null) {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .listener(new SvgSoftwareLayerSetter<Uri>());

            requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(data.get(position).getFlag()))
                    .into(holder.flagIcon);

        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameFragment.equals(mainFirstFragment.class.getCanonicalName())) {//if you come from the mainFirstFragment

                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake);
                    v.startAnimation(hyperspaceJumpAnimation);

                    State s = data.get(position);
                    MainActivity ma = (MainActivity) v.getContext();
                    ma.LoadSecFragment(s);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class StateAdapterViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView flagIcon;
        TextView textViewName;
        TextView textViewnativeName;

        public StateAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textView1);
            textViewnativeName = (TextView) itemView.findViewById(R.id.textView2);
            cardView = itemView.findViewById(R.id.cardView);
            flagIcon = itemView.findViewById(R.id.flagIcon);

        }

    }


    public ArrayList<State> custumeFilter(ArrayList<State> input, String word) // for search edit text - filter function
    {
        ArrayList<State> arr = new ArrayList<State>();

        for (State s : input) {
            if (s.getName().toLowerCase().contains(word) || s.getNativeName().toLowerCase().contains(word)) {
                arr.add(s);
            }
        }
        return arr;
    }


}
