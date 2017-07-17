package com.msoma.mmeks.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.msoma.mmeks.R;
import com.msoma.mmeks.data.Comment;

public class CommentListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Comment> comments;

    public CommentListAdapter(Activity activity, List<Comment> comments) {
        this.activity = activity;
        this.comments = comments;
    }
    @Override
    public int getCount() {
        return comments.size();
    }
    @Override
    public Object getItem(int location) {
        return comments.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.comments, null);

        TextView name  = (TextView) convertView.findViewById(R.id.Com_txvUsername);
        TextView comment = (TextView) convertView.findViewById(R.id.Com_txvComment);
       // TextView time = (TextView) convertView.findViewById(R.id.Com_txvTime);


        Comment com = comments.get(position);


        name.setText(com.getName());
        comment.setText(com.getComment());

         return convertView;
    }
}