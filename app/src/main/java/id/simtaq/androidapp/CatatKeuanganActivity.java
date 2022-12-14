package id.simtaq.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import id.simtaq.androidapp.adapter.SectionPagerAdapter;
import id.simtaq.androidapp.fragments.PemasukanFragment;
import id.simtaq.androidapp.fragments.PengeluaranFragment;

public class CatatKeuanganActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catat_keuangan);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Catat Pemasukan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbCatatKeuangan);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PemasukanFragment.newInstance(), "Pemasukan");
        adapter.addFragment(PengeluaranFragment.newInstance(), "Pengeluaran");
        viewPager.setAdapter(adapter);
    }
}