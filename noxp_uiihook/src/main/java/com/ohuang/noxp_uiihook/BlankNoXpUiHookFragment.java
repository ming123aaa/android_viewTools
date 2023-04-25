package com.ohuang.noxp_uiihook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankNoXpUiHookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankNoXpUiHookFragment extends Fragment {



    public BlankNoXpUiHookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_no_xp_ui_hook, container, false);
    }
}