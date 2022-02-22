package com.harjeet.conortrashtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Talk> mDataSet;
    private final Context ctx;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, desc;
        private final LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            title = v.findViewById(R.id.title);
            desc = v.findViewById(R.id.desc);
            linearLayout = v.findViewById(R.id.topView);
        }

        public TextView getTitle() {
            return title;
        }
        public TextView getDesc() {
            return desc;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param ctx
     */
    public MyAdapter(ArrayList<Talk> data, Context ctx) {
        mDataSet = data;
        this.ctx = ctx;
    }



    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_row, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTitle().setText(mDataSet.get(position).getTitle());
        viewHolder.getDesc().setText(mDataSet.get(position).getDesc());


        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = v.findViewById(R.id.imagePlaying);
                AudioPlay.stopAudio();
                int milli = AudioPlay.playAudio(ctx, mDataSet.get(position).getUrl(), imageView);

                Animation animation1 = new AlphaAnimation(0.2f, 1.0f);
                animation1.setDuration(700);
                v.startAnimation(animation1);

                if(milli != -1){
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setAlpha(1.0f);
                    imageView.animate()
                            .setDuration(milli+250)
                            .alpha(0.0f)
                            .setListener(null);
                }


            }
        });
        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Talk> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        mDataSet = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}