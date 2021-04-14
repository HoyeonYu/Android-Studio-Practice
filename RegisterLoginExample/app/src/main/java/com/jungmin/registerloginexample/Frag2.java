package com.jungmin.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class Frag2 extends Fragment {
    private View view;
    private Intent kakaoIntent;
    private final String packageName = "com.kakao.talk";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);

        kakaoIntent = this.getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
        ImageView load = (ImageView)view.findViewById(R.id.chat_image_view);
        Glide.with(this).load(R.drawable.chat_image).into(load);

        Button kakaoButton = (Button)view.findViewById(R.id.chat_kakao_button);
        kakaoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frag2.this.startActivity(kakaoIntent);
            }
        });

        return view;
    }
}