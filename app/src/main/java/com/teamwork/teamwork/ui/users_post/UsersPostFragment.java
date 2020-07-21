package com.teamwork.teamwork.ui.users_post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.teamwork.teamwork.R;

public class UsersPostFragment extends Fragment {

    private UsersPostViewModel mPostViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mPostViewModel =
                ViewModelProviders.of(this).get(UsersPostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users_post,container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        mPostViewModel.getText().observe(this, textView::setText);

        return root;
    }

}