package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import id.simtaq.androidapp.fragments.HomeFragment;
import id.simtaq.androidapp.fragments.KegiatanFragment;
import id.simtaq.androidapp.fragments.PengaturanFragment;
import id.simtaq.androidapp.fragments.InfoKasFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, new HomeFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.menu_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.menu_infokas:
                        selectedFragment = new InfoKasFragment();
                        break;
                    case R.id.menu_kegiatan:
                        selectedFragment = new KegiatanFragment();
                        break;
                    case R.id.menu_pengaturan:
                        selectedFragment = new PengaturanFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flPageContainer, selectedFragment).commit();

                return true;

            }
        });
    }

    //Menampilkan halaman Fragment
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flPageContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}