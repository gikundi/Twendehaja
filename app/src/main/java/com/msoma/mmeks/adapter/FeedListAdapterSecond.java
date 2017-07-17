package com.msoma.mmeks.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.msoma.mmeks.Comments;
import com.msoma.mmeks.R;

import com.msoma.mmeks.FeedImageView;
import com.msoma.mmeks.app.AppController;
import com.msoma.mmeks.data.FeedItem;

public class FeedListAdapterSecond extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapterSecond(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);

        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

        TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);

        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);

        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);

        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

        TextView commentsCount = (TextView) convertView.findViewById(R.id.commentCount);



        FeedItem item = feedItems.get(position);

        name.setText(item.getName());


        commentsCount.setText(item.getCommentCount());
        //name.setText(item.getId());

        //name.setText(item.getName());

        // Converting timestamp into x ago format
        //CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
        //		Long.parseLong(item.getTimeStamp()),
        //		System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        //timestamp.setText(timeAgo);
        timestamp.setText(item.getTimeStamp());

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }

        // user profile pic
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }
        final int nm=item.getId();

        final int pi = item.getPortId();


        //final String nm=item.getName();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText( activity, nm + "unanifinya", Toast.LENGTH_LONG).show();
                //Log.i("Hi", nm);
                Intent intent = new Intent(activity.getApplicationContext(), Comments.class);
                //Log.i("1aaaa", nm);
                intent.putExtra("organisation_id", nm);
                intent.putExtra("portfolio_id" ,pi);
                //Log.i("2bbbbb", nm);
                activity.startActivity(intent);
                //Log.i("3cccc", nm);
                //activity.startActivity(new Intent(activity, ListViewCompany.class));
                //Intent intent = new Intent(v.getContext(),ListViewCompany.class);
                // Intent intent = new Intent(activity.getApplicationContext(), ListViewCompany.class);
                //Intent intent = new Intent(activity.getBaseContext(),ListViewCompany.class);
                //intent.putExtra("id", nm);
                // startActivity(i);
                //intent.putExtra("id",nm); //Put your id to your next Intent
                ///activity.startActivity(intent);
                //Log.i("clicked", nm);
            }
        });
        return convertView;
    }

}
