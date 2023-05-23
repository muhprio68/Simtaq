package id.simtaq.androidapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.simtaq.androidapp.DetailPenggunaActivity;
import id.simtaq.androidapp.EditProfilActivity;
import id.simtaq.androidapp.HapusAkunActivity;
import id.simtaq.androidapp.JadwalKegiatanActivity;
import id.simtaq.androidapp.LaporkanBugActivity;
import id.simtaq.androidapp.ListPenggunaActivity;
import id.simtaq.androidapp.LoginActivity;
import id.simtaq.androidapp.MainActivity;
import id.simtaq.androidapp.R;
import id.simtaq.androidapp.SplashScreenActivity;
import id.simtaq.androidapp.TambahAkunActivity;
import id.simtaq.androidapp.TambahKegiatanActivity;
import id.simtaq.androidapp.TentangSimtaqActivity;
import id.simtaq.androidapp.UbahAkunActivity;
import id.simtaq.androidapp.UbahKataSandiActivity;
import id.simtaq.androidapp.adapter.PengaturanListAdapter;
import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Pengaturan;

public class PengaturanFragment extends Fragment implements PengaturanListAdapter.IPengaturanAdapter, View.OnClickListener {

    private RecyclerView rvPengaturan;
    private ArrayList<Pengaturan> pengaturanList;
    private TextView tvNama, tvEmail, tvTipePengguna;
    private String nama, email, levelPengguna, tipePengguna;
    private RelativeLayout rlBtnEditProfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        initViews(view);
        nama = Preferences.getKeyNama(getContext());
        email = Preferences.getKeyEmail(getContext());
        levelPengguna = Preferences.getKeyLevel(getContext());
        tipePengguna = initPengguna(Preferences.getKeyLevel(getContext()));
        setProfil();
        if (levelPengguna.equals("4")){
            addDataSuperAdmin();
        } else {
            addData();
        }
        rvPengaturan.setHasFixedSize(true);
        rvPengaturan.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPengaturan.setAdapter(new PengaturanListAdapter(view.getContext(), pengaturanList, this));
        rlBtnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfilActivity.class));
            }
        });
        return view;
    }

    private void initViews(View view){
        rvPengaturan = view.findViewById(R.id.rvPengaturan);
        tvNama = view.findViewById(R.id.tvNamaPengguna);
        tvEmail = view.findViewById(R.id.tvEmailPengguna);
        tvTipePengguna = view.findViewById(R.id.tvTipePengguna);
        rlBtnEditProfil = view.findViewById(R.id.rlBtnEditProfil);
    }

    void addData(){
        pengaturanList = new ArrayList<>();
        pengaturanList.add(new Pengaturan(0, R.drawable.ic_lock_primarytext, "Ganti Kata Sandi"));
        pengaturanList.add(new Pengaturan(1, R.drawable.ic_document_bug, "Laporkan Bug"));
        pengaturanList.add(new Pengaturan(2, R.drawable.ic_info_squared, "Tentang"));
        pengaturanList.add(new Pengaturan(3, R.drawable.ic_shutdown, "Keluar"));
    }

    void addDataSuperAdmin(){
        pengaturanList = new ArrayList<>();
        pengaturanList.add(new Pengaturan(0, R.drawable.ic_lock_primarytext, "Ganti kata sandi"));
        pengaturanList.add(new Pengaturan(4, R.drawable.ic_add_user, "Tambah Akun"));
        pengaturanList.add(new Pengaturan(5, R.drawable.ic_list_user, "Daftar Pengguna"));
        pengaturanList.add(new Pengaturan(1, R.drawable.ic_document_bug, "Laporkan Bug"));
        pengaturanList.add(new Pengaturan(2, R.drawable.ic_info_squared, "Tentang"));
        pengaturanList.add(new Pengaturan(3, R.drawable.ic_shutdown, "Keluar"));
    }

    private void setProfil(){
        tvNama.setText(nama);
        tvEmail.setText(email);
        tvTipePengguna.setText(tipePengguna);
    }

    private String initPengguna(String level){
        if(level.equals("1")){
            return "Jamaah Masjid";
        } else if (level.equals("2")){
            return "Bendahara Takmir";
        } else if (level.equals("3")){
            return "Humas Takmir";
        } else {
            return "Super Admin";
        }
    }

    @Override
    public void doClick(int id) {
        if (id == 0){
            Intent intent = new Intent(getContext(), UbahKataSandiActivity.class);
            intent.putExtra("id", Integer.parseInt(Preferences.getKeyId(getContext())));
            startActivity(intent);
        } else if (id == 1) {
            startActivity(new Intent(getContext(), LaporkanBugActivity.class));
        } else if (id == 2) {
            startActivity(new Intent(getContext(), TentangSimtaqActivity.class));
        } else if (id == 4) {
            startActivity(new Intent(getContext(), TambahAkunActivity.class));
        } else if (id == 5) {
            startActivity(new Intent(getContext(), ListPenggunaActivity.class));
        }  else {
            Preferences.setKeyToken(getContext(),"");
            Preferences.setKeyId(getContext(),"");
            Preferences.setKeyNama(getContext(),"");
            Preferences.setKeyEmail(getContext(),"");
            Preferences.setKeyLevel(getContext(),"");
            Preferences.setLoggedInStatus(getContext(),false);
            startActivity(new Intent(getContext(), SplashScreenActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View view) {

    }
}