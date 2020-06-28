package com.teamwork.teamwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

//    private final List<PostData> mpostdata;

    public PostRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.post_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mUsersImg;
        private final TextView mUsername;
        private final TextView mPostTitle;
        private final Button mPostComment;
        private final Button mPostFlag;
        private final TextView mPostTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mUsersImg = itemView.findViewById(R.id.usersImg);
            mUsername = itemView.findViewById(R.id.btnFullname);
            mPostTitle = itemView.findViewById(R.id.postTitle);
            mPostComment = itemView.findViewById(R.id.btnComment);
            mPostFlag = itemView.findViewById(R.id.btnLogin);
            mPostTime = itemView.findViewById(R.id.displayTime);
        }
    }
}
