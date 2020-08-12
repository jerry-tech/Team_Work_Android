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


public class UsersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<PostData> mpostdata;
    private final Context mContext;
    private Bitmap mBitmap;
    public final LayoutInflater mLayoutInflater;
    public final String IMGURL = "https://myteamworkproject.herokuapp.com";
    public static final int POST_ARTICLE = 0;
    public static final int POST_IMAGE = 1;


    public UsersRecyclerAdapter(Context context, List<PostData> mpostdata) {
        mContext = context;
        this.mpostdata = mpostdata;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View usersView = null;
        if (viewType == POST_ARTICLE) {
            usersView = mLayoutInflater.inflate(R.layout.users_post_list, parent, false);
            return new UsersArticle(usersView);
        } else if (viewType == POST_IMAGE) {
            usersView = mLayoutInflater.inflate(R.layout.users_post_img, parent, false);
            return new ImageArticle(usersView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);
        PostData postData = mpostdata.get(position);

        if (itemType == POST_ARTICLE) {
            UsersArticle usersArticle = (UsersArticle) holder;

            //setting text for the post title
            String title = toSentence(postData.getTitle());
            usersArticle.idTitle.setText(title);

            //setting text for the post fullname
            String fullName = toSentence(postData.getLastName()) + " " + toSentence(postData.getFirstName());
            usersArticle.idFullName.setText(fullName);

            //setting text for the post content
            usersArticle.idPostContent.setText(postData.getArticle());

            //setting text for the post time
            usersArticle.idPostDate.setText(postData.getDateCreated());

//        calling the Image downloader class constructor in order to download the image from the server(Users profile image)
            new ImageDownloader(usersArticle.idImg).execute(IMGURL + "/images/" + postData.getUserImage());
            usersArticle.idImg.setImageBitmap(mBitmap);

            //adding article Id
            usersArticle.articleId = postData.getArticle_id();


        } else if (itemType == POST_IMAGE) {
            //calling the ImgViewHolder holder class and type casting the Recycler viewHolder to an ImgViewHolder Holder
            ImageArticle imgViewHolder = (ImageArticle) holder;

            //setting text for the image post title
            String title = toSentence(postData.getTitle());
            imgViewHolder.idTitle2.setText(title);
            Log.i("title", title);

            //setting text for the image post fullname
            String fullName = toSentence(postData.getLastName()) + " " + toSentence(postData.getFirstName());
            imgViewHolder.idFullName2.setText(fullName);

            //setting the image for the post image
            new ImageDownloader(imgViewHolder.idPostContent2).execute(IMGURL + "/images/" + postData.getImageUrl());
            imgViewHolder.idPostContent2.setImageBitmap(mBitmap);

            //setting text for the image post time
            imgViewHolder.idPostDate2.setText(postData.getDateCreated());

            //calling the Image downloader class constructor in order to download the image from the server(Users profile image)
            new ImageDownloader(imgViewHolder.idImg2).execute(IMGURL + "/images/" + postData.getUserImage());
            imgViewHolder.idImg2.setImageBitmap(mBitmap);

            //gif id
            imgViewHolder.gid_id = postData.getGif_id();

        }
    }

    @Override
    public int getItemCount() {
        return mpostdata.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mpostdata.get(position).getArticle_id() == 0) {
            return POST_IMAGE;
        } else {
            return POST_ARTICLE;
        }
    }

    //class for user's article
    public class UsersArticle extends RecyclerView.ViewHolder {

        public CircleImageView idImg;
        public TextView idFullName;
        public TextView idTitle;
        public TextView idPostContent;
        public TextView idPostDate;
        public ImageButton btnIdArticle;
        public int articleId;

        public UsersArticle(@NonNull View itemView) {
            super(itemView);
            idImg = itemView.findViewById(R.id.indImg);
            idFullName = itemView.findViewById(R.id.txtFullName);
            idTitle = itemView.findViewById(R.id.txtTitle);
            idPostContent = itemView.findViewById(R.id.txt_Content);
            idPostDate = itemView.findViewById(R.id.txtDateCreated);

            //comment btn
            btnIdArticle = itemView.findViewById(R.id.btnArtComment);

            btnIdArticle.setOnClickListener(event -> {
                Intent intent = new Intent(mContext, ArticleComment.class);
                intent.putExtra("ArticleId", articleId);
                mContext.startActivity(intent);
            });
        }
    }

    //class fro user's img post
    public class ImageArticle extends RecyclerView.ViewHolder {

        public CircleImageView idImg2;
        public TextView idFullName2;
        public TextView idTitle2;
        public ImageView idPostContent2;
        public TextView idPostDate2;
        public ImageButton btnImgComment;

        public int gid_id;

        public ImageArticle(@NonNull View itemView) {
            super(itemView);
            idImg2 = itemView.findViewById(R.id.indImg2);
            idFullName2 = itemView.findViewById(R.id.txtFullName2);
            idTitle2 = itemView.findViewById(R.id.txtTitle2);
            idPostContent2 = itemView.findViewById(R.id.img_Content2);
            idPostDate2 = itemView.findViewById(R.id.txtDateCreated2);
            btnImgComment = itemView.findViewById(R.id.btnImgComment);

            btnImgComment.setOnClickListener(event -> {
                Intent intent = new Intent(mContext, ImageComment.class);
                intent.putExtra("ImageId", gid_id);
                mContext.startActivity(intent);
            });
        }
    }

    //method used to change a word to sentence case
    public String toSentence(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }


}
