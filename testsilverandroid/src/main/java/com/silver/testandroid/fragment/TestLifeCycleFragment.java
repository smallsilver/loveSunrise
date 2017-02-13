package com.silver.testandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @PACKAGE com.silver.testandroid
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/10/17 23:46
 * @VERSION V1.0
 */
public class TestLifeCycleFragment extends Fragment{
    private static String TAG = TestLifeCycleFragment.class.getName();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"super.onActivityCreated();");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"super.onCreate();");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"super.onDestroy();");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"super.onDestroyView();");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"super.onCreate();");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG,"super.onCreate();");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"super.onPause();");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"super.onResume();");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"super.onSaveInstanceState();");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"super.onStart();");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"super.onStop();");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"super.onViewCreated();");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"super.onCreateView();");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"super.onAttach();");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.i(TAG,"super.onAttachFragment();");
    }
}
