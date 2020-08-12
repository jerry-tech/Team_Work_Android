package com.teamwork.teamwork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Bitmap bitmap;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    public final String URL = "https://myteamworkproject.herokuapp.com";
    private final List<PostData> mpostdata;
    public static final int POST_ARTICLE = 0;
    public static final int POST_IMAGE = 1;


    public PostRecyclerAdapter(Context context, List<PostData> mpostdata) {
        mContext = context;
        this.mpostdata = mpostdata;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        // check here the viewType and return RecyclerView.ViewHolder based on view type
        if (viewType == POST_ARTICLE) {
            itemView = mLayoutInflater.inflate(R.layout.post_list, parent, false);
            return new ArticleHolder(itemView);
        } else if (viewType == POST_IMAGE) {
            itemView = mLayoutInflater.inflate(R.layout.post_img_list, parent, false);
            return new ImgViewHolder(itemView);
        } else {
            return null;
        }

    }

    @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//        Log.d("TAG", "onBindViewHolder:" + postData.getTitle());
        final int itemType = getItemViewType(position);
        PostData postData = mpostdata.get(position);

        if (itemType == POST_ARTICLE) {
            //calling the article holder class and type casting the Recycler viewHolder to an article Holder
            ArticleHolder articleHolder = (ArticleHolder)holder;

            //setting text for the post title
            String title = postData.getTitle().substring(0, 1).toUpperCase() + postData.getTitle().substring(1).toLowerCase();
            articleHolder.mPostTitle.setText(title);

            //setting text for the post fullname
            String fullName = postData.getLastName() + " " + postData.getFirstName();
            articleHolder.mUsername.setText(fullName);

            //setting text for the post content
            articleHolder.mPostContent.setText(postData.getArticle());

            //setting text for the post time
            articleHolder.mPostTime.setText(postData.getDateCreated());

            Log.d("Image URL", URL + "/" + postData.getUserImage());
//        calling the Image downloader class constructor in order to download the image from the server(Users profile image)
            new ImageDownloader(articleHolder.mUsersImg).execute(URL + "/images/" + postData.getUserImage());
            articleHolder.mUsersImg.setImageBitmap(bitmap);

            //adding article Id
            articleHolder.articleId = postData.getArticle_id();

        } else if (itemType == POST_IMAGE) {

            //calling the ImgViewHolder holder class and type casting the Recycler viewHolder to an ImgViewHolder Holder
            ImgViewHolder imgViewHolder = (ImgViewHolder)holder;

            //setting text for the image post title
            String title = postData.getTitle().substring(0, 1).toUpperCase() + postData.getTitle().substring(1).toLowerCase();
            imgViewHolder.imgTitle.setText(title);

            //setting text for the image post fullname
            String fullName = postData.getLastName() + " " + postData.getFirstName();
            imgViewHolder.imgUsername.setText(fullName);

            //setting the image for the post image
            new ImageDownloader(imgViewHolder.mPostImg).execute(URL + "/images/" + postData.getImageUrl());
            imgViewHolder.mPostImg.setImageBitmap(bitmap);

            //setting text for the image post time
            imgViewHolder.imgDate.setText(postData.getDateCreated());

//            Log.d("Image URL", URL + "/" + postData.getUserImage());
//        calling the Image downloader class constructor in order to download the image from the server(Users profile image)
            new ImageDownloader(imgViewHolder.imgUser2).execute(URL + "/images/" + postData.getUserImage());
            imgViewHolder.imgUser2.setImageBitmap(bitmap);
        }


    }

    @Override
    public int getItemViewType(int position) {

        if (mpostdata.get(position).getArticle_id() == 0) {
            return POST_IMAGE;
        } else {
            return POST_ARTICLE;
        }
    }

    @Override
    public int getItemCount() {
        return mpostdata.size();
    }

    //view holder class for article posts
    public class ArticleHolder extends RecyclerView.ViewHolder {

        public CircleImageView mUsersImg;
        public TextView mUsername;
        public TextView mPostTitle;
        public TextView mPostContent;
        public TextView mPostTime;
        public ImageButton mCommentBtn;

        //article Id
        public int articleId;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            mUsersImg = itemView.findViewById(R.id.usersImg);
            mUsername = itemView.findViewById(R.id.btnFullname);
            mPostTitle = itemView.findViewById(R.id.pTitle);
            mPostContent = itemView.findViewById(R.id.postContent);
            mPostTime = itemView.findViewById(R.id.txtDate);
            mCommentBtn = itemView.findViewById(R.id.btnMainComment);

            //setting on click listener for one of the recycler view buttons
            mCommentBtn.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ArticleComment.class);
                intent.putExtra("ArticleId", articleId);
                mContext.startActivity(intent);
            });
        }
    }


    //view holder class for image posts
    public class ImgViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgUser2;
        public TextView imgUsername;
        public TextView imgTitle;
        public ImageView mPostImg;
        public TextView imgDate;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser2 = itemView.findViewById(R.id.usersImg2);
            imgUsername = itemView.findViewById(R.id.btnFullname2);
            imgTitle = (TextView) itemView.findViewById(R.id.pTitle2);
            imgDate = itemView.findViewById(R.id.txtDate2);
            mPostImg = itemView.findViewById(R.id.postImg);
        }
    }
}
