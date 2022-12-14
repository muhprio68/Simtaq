package id.simtaq.androidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.simtaq.androidapp.R;

public class PengeluaranFragment extends Fragment {


    public PengeluaranFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PengeluaranFragment newInstance() {
        PengeluaranFragment fragment = new PengeluaranFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengeluaran, container, false);
    }
}