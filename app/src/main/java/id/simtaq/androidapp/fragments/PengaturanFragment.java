package id.simtaq.androidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.simtaq.androidapp.R;
import id.simtaq.androidapp.adapter.PengaturanListAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.models.Pengaturan;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PengaturanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengaturanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvPengaturan;
    private ArrayList<Pengaturan> pengaturanList;

    public PengaturanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PengaturanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PengaturanFragment newInstance(String param1, String param2) {
        PengaturanFragment fragment = new PengaturanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        addData();
        rvPengaturan = view.findViewById(R.id.rvPengaturan);
        rvPengaturan.setHasFixedSize(true);
        rvPengaturan.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPengaturan.setAdapter(new PengaturanListAdapter(view.getContext(), pengaturanList));

        return view;
    }

    void addData(){
        pengaturanList = new ArrayList<>();
        pengaturanList.add(new Pengaturan(0, R.drawable.ic_circled_up, "Ganti kata sandi"));
        pengaturanList.add(new Pengaturan(1, R.drawable.ic_activity_kuning, "Laporkan Bug"));
        pengaturanList.add(new Pengaturan(2, R.drawable.ic_circled_up, "Tentang"));
        pengaturanList.add(new Pengaturan(3, R.drawable.ic_add, "Keluar"));
    }
}