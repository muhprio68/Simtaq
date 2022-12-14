package id.simtaq.androidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.simtaq.androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PemasukanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PemasukanFragment extends Fragment {

    public PemasukanFragment() {
        // Required empty public constructor
    }

    public static PemasukanFragment newInstance() {
        PemasukanFragment fragment = new PemasukanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pemasukan, container, false);
    }
}