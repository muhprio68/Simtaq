package id.simtaq.androidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;


public class InfoKasFragment extends Fragment {

    private RecyclerView rvRiwayat;

    public InfoKasFragment() {

    }

    public static InfoKasFragment newInstance(String param1, String param2) {
        InfoKasFragment fragment = new InfoKasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infokas, container, false);
        return view;
    }
}