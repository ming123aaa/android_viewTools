package com.ohuang.noxp_uiihook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankNoXpUiHookFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankNoXpUiHookFragment2 extends Fragment {



    public BlankNoXpUiHookFragment2() {
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