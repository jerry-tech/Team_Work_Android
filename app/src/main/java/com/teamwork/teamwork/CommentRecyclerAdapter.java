package com.teamwork.teamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private Bitmap mBitmap;
    private final String URL = "https://myteamworkproject.herokuapp.com";
    private final List<CommentData> mCommentData;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public CommentRecyclerAdapter(Context context, List<CommentData> mCommentData) {
        mContext = context;
        this.mCommentData = mCommentData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
//        inflating the layout the was added to the recycler list then passing its value to the constructor of the view Holder class
        itemView = mLayoutInflater.inflate(R.layout.post_comment_list, parent, false);
        return new ViewHolder(itemView);
    }

    //method used to bind the data to the views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CommentData commentData = mCommentData.get(position);
        //fullName
        String fullName = toSentence(commentData.lastName) + " " + toSentence(commentData.firstName);
        holder.mComUserName.setText(fullName);

        //userImage
        new ImageDownloader(holder.mComUserImg).execute(URL + "/images/" + commentData.userImage);
        holder.mComUserImg.setImageBitmap(mBitmap);

        //comment
        holder.mComUserPost.setText(commentData.comment);

        //date of comment
        holder.mComUsersDate.setText(commentData.createdOn);


    }

    @Override
    public int getItemCount() {
        return mCommentData.size();
    }

    //class used to control all the components of the layout that is being inflated
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CircleImageView mComUserImg;
        public final TextView mComUserName;
        public final TextView mComUserPost;
        public final TextView mComUsersDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mComUserImg = itemView.findViewById(R.id.comment_image);
            mComUserName = itemView.findViewById(R.id.comment_name);
            mComUserPost = itemView.findViewById(R.id.comment_post);
            mComUsersDate = itemView.findViewById(R.id.comment_date);

        }
    }

    //method used to change a word to sentence case
    public String toSentence(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
