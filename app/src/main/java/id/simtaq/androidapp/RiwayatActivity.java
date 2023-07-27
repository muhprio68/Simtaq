package id.simtaq.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Donatur;
import id.simtaq.androidapp.models.Kegiatan;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.poi.CellBordersHandler;
import id.simtaq.androidapp.utils.StringUtils;


import static id.simtaq.androidapp.helper.config.formatLihatTanggal;
import static id.simtaq.androidapp.helper.config.formatLihatTglExcel;
import static id.simtaq.androidapp.helper.config.getCurentDate;
import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.toRupiah;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatActivity extends AppCompatActivity implements RiwayatListAdapter.IRiwayatListAdapter {

    private ConstraintLayout clRiwayatKeuangan;
    private Toolbar toolbar;
    private ProgressBar pbRiwayatKeuangan;
    private RecyclerView rvRiwayat;
    private ArrayList<Keuangan> keuanganList;
    private ArrayList<Donatur> donaturList;
    public RiwayatListAdapter adapter;
    private RequestQueue queue;
    private String authToken;
    private String level;
    private String sBulanTahun, sBulan, sTahun, sFilterBulanTahun;
    private SimpleDateFormat bulanTahun, tahun, bulan, filterBulanTahun;
    private Calendar c;
    private TextView tvFilterBulanKeuangan;
    private TextView tvFilterTahunKeuangan;
    private ImageView ivNext;
    private ImageView ivPrev;
    int step;
    String record;
    File file, root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        initViews();

        authToken = Preferences.getKeyToken(RiwayatActivity.this);
        level = Preferences.getKeyLevel(RiwayatActivity.this);
        //addData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Riwayat Uang Kas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        keuanganList = new ArrayList<>();
        donaturList = new ArrayList<>();
        queue = Volley.newRequestQueue(RiwayatActivity.this);
        c = Calendar.getInstance();
        bulanTahun = new SimpleDateFormat("yyyy-MM", locale);
        tahun = new SimpleDateFormat("yyyy", locale);
        bulan = new SimpleDateFormat("MMMM", locale);
        filterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);

        sBulanTahun = bulanTahun.format(c.getTime());
        sTahun = tahun.format(c.getTime());
        sBulan = bulan.format(c.getTime());
        sFilterBulanTahun = filterBulanTahun.format(c.getTime());

        getDataKeuangan(sFilterBulanTahun, authToken);
        getDataDonatur(sFilterBulanTahun, authToken);
        tvFilterBulanKeuangan.setText(sBulan);
        tvFilterTahunKeuangan.setText(sTahun);
        initLevel(level);
        tvFilterTahunKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSendEmail();
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextPrev("N");
            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextPrev("P");
            }
        });
    }

    public void initViews(){
        toolbar = findViewById(R.id.tbRiwayatKas);
        clRiwayatKeuangan = findViewById(R.id.clRiwayatKeuangan);
        rvRiwayat = findViewById(R.id.rvRiwayat);
        pbRiwayatKeuangan = findViewById(R.id.pbRiwayatKeuangan);
        tvFilterBulanKeuangan = findViewById(R.id.tvFilterBlnRiwayat);
        tvFilterTahunKeuangan = findViewById(R.id.tvFilterThnRiwayat);
        ivNext = findViewById(R.id.ivNextKeuangan);
        ivPrev = findViewById(R.id.ivPrevKeuangan);
    }

    private void initLevel(String level){
        if (level.equals("2") || level.equals("4")) {
            enableSwipeToDelete();
        }
    }

    public void getDataKeuangan (String filter, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/keuangan", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pbRiwayatKeuangan.setVisibility(View.GONE);
                rvRiwayat.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String noKeuangan = responseObj.getString("no_keuangan");
                        String tipeKeuangan = responseObj.getString("tipe_keuangan");
                        String tglKeuangan = responseObj.getString("tgl_keuangan");
                        String ketKeuangan = responseObj.getString("keterangan_keuangan");
                        String jenisKeuangan = responseObj.getString("jenis_keuangan");
                        String statusKeuangan = responseObj.getString("status_keuangan");
                        Long nominalKeuangan = responseObj.getLong("nominal_keuangan");
                        Long jmlKasAwal = responseObj.getLong("jml_kas_awal");
                        Long jmlKasAkhir = responseObj.getLong("jml_kas_akhir");
                        String deskripsiKeuangan = responseObj.getString("deskripsi_keuangan");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglKeuangan.contains(filter)){
                            keuanganList.add(new Keuangan(idKeuangan, noKeuangan, tipeKeuangan, tglKeuangan, ketKeuangan, jenisKeuangan, statusKeuangan, nominalKeuangan, jmlKasAwal, jmlKasAkhir, deskripsiKeuangan, createAt, updateAt));
                            Collections.sort(keuanganList, new Comparator<Keuangan>() {
                                @Override
                                public int compare(Keuangan keuangan, Keuangan k1) {
                                    return keuangan.getTglKeuangan().compareTo(k1.getTglKeuangan());
                                }
                            });
                            buildRecyclerView();
                            doNextCurentTime();
                        } else{
                            buildRecyclerView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RiwayatActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void getDataDonatur (String filter, String token){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/donatur", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        int idDonatur = responseObj.getInt("id_donatur");
                        int idKeuangan = responseObj.getInt("id_keuangan");
                        String tglDonatur = responseObj.getString("tgl_donatur");
                        String wilayahDonatur = responseObj.getString("wilayah_donatur");
                        String petugasDonatur = responseObj.getString("petugas_donatur");
                        Long nominalDonatur = responseObj.getLong("nominal_donatur");
                        String createAt = responseObj.getString("create_at");
                        String updateAt = responseObj.getString("update_at");
                        if (tglDonatur.contains(filter)){
                            donaturList.add(new Donatur(idDonatur, idKeuangan, tglDonatur, wilayahDonatur, petugasDonatur, nominalDonatur, createAt, updateAt));
                            Collections.sort(donaturList, new Comparator<Donatur>() {
                                @Override
                                public int compare(Donatur donatur, Donatur d1) {
                                    return donatur.getTglDonatur().compareTo(d1.getTglDonatur());
                                }
                            });
                            buildRecyclerView();
                            doNextCurentTime();
                        } else{
                            buildRecyclerView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RiwayatActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void buildRecyclerView(){
        adapter = new RiwayatListAdapter(authToken, keuanganList, RiwayatActivity.this, 1, this, queue, clRiwayatKeuangan);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRiwayat.setHasFixedSize(true);
        rvRiwayat.setLayoutManager(manager);
        rvRiwayat.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                hapusDialog(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvRiwayat);
    }

    public void hapusDialog(int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Data Kegiatan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin menghapus kegiatan ini?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        adapter.removeItem(position);
                        buildRecyclerView();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                        buildRecyclerView();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void doNextPrev(String o) {
        try {
            int step = 8; //pindah sebelum-sesudah
            if (o.equals("N"))
                c.add(Calendar.MONTH, 1);
            else
                c.add(Calendar.MONTH, -1);

            sBulanTahun = bulanTahun.format(c.getTime());
            sTahun = tahun.format(c.getTime());
            sBulan = bulan.format(c.getTime());
            SimpleDateFormat sdfFilterBulanTahun = new SimpleDateFormat("yyyy-MM", locale);
            sFilterBulanTahun = sdfFilterBulanTahun.format(c.getTime());
            keuanganList.clear();
            getDataKeuangan(sFilterBulanTahun, authToken);
            tvFilterBulanKeuangan.setText(sBulan);
            tvFilterTahunKeuangan.setText(sTahun);
            doNextCurentTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(RiwayatActivity.this, "Gagal memuat list transaksi" , Toast.LENGTH_LONG).show();
        }
    }


    public void doNextCurentTime(){
        try {
            final Calendar b = Calendar.getInstance();
            if (sBulanTahun.equals(tampilkanTanggalDanWaktu(b.getTime(),"yyyy-MM", locale))) {
                ivNext.setClickable(false);
                ivNext.setImageResource(R.drawable.ic_next_abu);
                ivNext.setEnabled(false);
            } else {
                ivNext.setEnabled(true);
                ivNext.setImageResource(R.drawable.ic_next);
                ivNext.setClickable(true);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(RiwayatActivity.this, "Gagal memuat riwayat sebelum atau sesudah", Toast.LENGTH_LONG).show();
        }
    }

    private String tampilkanTanggalDanWaktu(Date tanggalDanWaktu, String pola, Locale lokal) {
        String tanggalStr;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        tanggalStr = formatter.format(tanggalDanWaktu);
        return tanggalStr;
    }

//    private File exportToExcel() {
//        final String fileName = "TodoList.xls";
//
//        //Saving file in external storage
//        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File directory = new File(sdCard.getAbsolutePath() + "/javatechig.todo");
//
//        //create directory if not exist
//        if(!directory.isDirectory()){
//            directory.mkdirs();
//        }
//
//        //file path
//        File file = new File(directory, fileName);
//
//        WorkbookSettings wbSettings = new WorkbookSettings();
//        wbSettings.setLocale(new Locale("en", "EN"));
//        WritableWorkbook workbook;
//
//        try {
//            workbook = Workbook.createWorkbook(file, wbSettings);
//            //Excel sheet name. 0 represents first sheet
//            WritableSheet sheet = workbook.createSheet("MyShoppingList", 0);
//
//            try {
//                sheet.addCell(new Label(0, 0, "Subject")); // column and row
//                sheet.addCell(new Label(1, 0, "Description"));
//            } catch (RowsExceededException e) {
//                e.printStackTrace();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//            workbook.write();
//            try {
//                workbook.close();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file;
//    }

    public int getWidth(int number){
        int hasil = (number * 256) + 200;
        return hasil;
    }

    private void getLaporanApache(){
        file = null;
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String csvFile = "Laporan.xlsx";
        File dir = null;
        if (root.canWrite()) {
            dir = new File(root.getAbsolutePath() + "/simtaq");

            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            file = new File(dir, csvFile);
        } else {
            Toast.makeText(RiwayatActivity.this, "Cannot write" , Toast.LENGTH_LONG).show();

        }
        int batasData = 7+keuanganList.size();
        int judulDonatur = batasData + 3;
        int dataDonatur = judulDonatur+2;
        int batasDonatur = dataDonatur+1+donaturList.size();
        int dataTtd = batasData + 5;
        int judulRekap = batasDonatur+3;
        int dataRekap = judulRekap +3;

        Workbook workbook = new XSSFWorkbook();
        PropertyTemplate pt = new PropertyTemplate();
        PropertyTemplate ptDonatur = new PropertyTemplate();
        PropertyTemplate ptRekap = new PropertyTemplate();

// #1) these borders will all be medium in default color
//        pt.drawBorders(new CellRangeAddress(5, 5, 1, 16),
//                BorderStyle.MEDIUM, BorderExtent.ALL);
        pt.drawBorders(new CellRangeAddress(5, 7+keuanganList.size(), 1, 16),
                BorderStyle.THIN, BorderExtent.ALL);
        ptDonatur.drawBorders(new CellRangeAddress(dataDonatur, dataDonatur+donaturList.size()+1, 1, 4),
                BorderStyle.THIN, BorderExtent.ALL);
        ptRekap.drawBorders(new CellRangeAddress(dataRekap, dataRekap+14, 1, 4),
                BorderStyle.THIN, BorderExtent.ALL);
        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(1, getWidth(6));
        sheet.setColumnWidth(2, getWidth(12));
        sheet.setColumnWidth(3, getWidth(18));
        sheet.setColumnWidth(4, getWidth(12));
        sheet.setColumnWidth(5, getWidth(12));
        sheet.setColumnWidth(6, getWidth(12));
        sheet.setColumnWidth(7, getWidth(12));
        sheet.setColumnWidth(8, getWidth(15));
        sheet.setColumnWidth(9, getWidth(15));
        sheet.setColumnWidth(10, getWidth(12));
        sheet.setColumnWidth(11, getWidth(12));
        sheet.setColumnWidth(12, getWidth(12));
        sheet.setColumnWidth(13, getWidth(12));
        sheet.setColumnWidth(14, getWidth(15));
        sheet.setColumnWidth(15, getWidth(15));
        sheet.setColumnWidth(16, getWidth(15));

        Row header1 = sheet.createRow(1);
        CellStyle header1Style = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight(16);
        header1Style.setFont(font);
        header1Style.setAlignment(HorizontalAlignment.CENTER);

        Cell headerCell1 = header1.createCell(1);
        headerCell1.setCellStyle(header1Style);
        headerCell1.setCellValue("LAPORAN KEUANGAN MASJID AT - TAQWA");
        sheet.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                1, //last row  (0-based)
                1, //first column (0-based)
                16  //last column  (0-based)
        ));
        Row header2 = sheet.createRow(2);
        Cell headerCell2 = header2.createCell(1);
        headerCell2.setCellStyle(header1Style);
        headerCell2.setCellValue("Dsn/Ds. Jogoloyo - Sumobito - Jombang");
        sheet.addMergedRegion(new CellRangeAddress(
                2, //first row (0-based)
                2, //last row  (0-based)
                1, //first column (0-based)
                16  //last column  (0-based)
        ));

        Row header3 = sheet.createRow(3);
        CellStyle header3Style = workbook.createCellStyle();
        XSSFFont font2 = ((XSSFWorkbook) workbook).createFont();
        font2.setFontName("Arial");
        font2.setUnderline(XSSFFont.U_DOUBLE);
        font2.setBold(true);
        font2.setFontHeight(16);
        header3Style.setFont(font2);
        header3Style.setAlignment(HorizontalAlignment.CENTER);
        Cell headerCell3 = header3.createCell(1);
        headerCell3.setCellStyle(header3Style);
        headerCell3.setCellValue("Arus Kas  Keuangan Bulan "+ tvFilterBulanKeuangan.
                getText().toString()+" "+tvFilterTahunKeuangan.getText().toString() );
        sheet.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                1, //first column (0-based)
                16  //last column  (0-based)
        ));

        Row headerTbl = sheet.createRow(5);
        CellStyle headTableStyle = workbook.createCellStyle();
        XSSFFont fontHeadTable = ((XSSFWorkbook) workbook).createFont();
        fontHeadTable.setFontName("Calibri");
        fontHeadTable.setBold(true);
        fontHeadTable.setFontHeight(14);
        headTableStyle.setFont(fontHeadTable);
        headTableStyle.setAlignment(HorizontalAlignment.CENTER);
        headTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headTableStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headTableStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headTableStyle.setWrapText(true);

        CellStyle styleNomor = workbook.createCellStyle();
        XSSFFont fontNomor = ((XSSFWorkbook) workbook).createFont();
        fontNomor.setFontName("Calibri");
        fontNomor.setFontHeight(12);
        styleNomor.setFont(fontNomor);
        styleNomor.setAlignment(HorizontalAlignment.CENTER);
        styleNomor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNomor.setWrapText(true);

        CellStyle styleHuruf = workbook.createCellStyle();
        XSSFFont fontHuruf = ((XSSFWorkbook) workbook).createFont();
        fontHuruf.setFontName("Calibri");
        fontHuruf.setFontHeight(12);
        fontHuruf.setBold(true);
        styleHuruf.setFont(fontHuruf);
        styleHuruf.setAlignment(HorizontalAlignment.CENTER);
        styleHuruf.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHuruf.setWrapText(true);

        Cell cellTbl1 = headerTbl.createCell(1);
        cellTbl1.setCellValue("No");
        cellTbl1.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                1, //first column (0-based)
                1  //last column  (0-based)
        ));

        Cell cellTbl2 = headerTbl.createCell(2);
        cellTbl2.setCellValue("Tanggal");
        cellTbl2.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                2, //first column (0-based)
                2  //last column  (0-based)
        ));

        Cell cellTbl3 = headerTbl.createCell(3);
        cellTbl3.setCellValue("Keterangan");
        cellTbl3.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                3, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellTbl4 = headerTbl.createCell(4);
        cellTbl4.setCellValue("Pemasukan per Jenis");
        cellTbl4.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                4, //first column (0-based)
                7  //last column  (0-based)
        ));

        Cell cellTbl5 = headerTbl.createCell(8);
        cellTbl5.setCellValue("Pemasukan");
        cellTbl5.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                8, //first column (0-based)
                8  //last column  (0-based)
        ));

        Cell cellTbl6 = headerTbl.createCell(9);
        cellTbl6.setCellValue("Akumulasi Pemasukan");
        cellTbl6.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                9, //first column (0-based)
                9  //last column  (0-based)
        ));

        Cell cellTbl7 = headerTbl.createCell(10);
        cellTbl7.setCellValue("Pengeluaran per Jenis");
        cellTbl7.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                10, //first column (0-based)
                13  //last column  (0-based)
        ));

        Cell cellTbl8 = headerTbl.createCell(14);
        cellTbl8.setCellValue("Pengeluaran");
        cellTbl8.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                14, //first column (0-based)
                14  //last column  (0-based)
        ));

        Cell cellTbl9 = headerTbl.createCell(15);
        cellTbl9.setCellValue("Akumulasi Pengeluaran");
        cellTbl9.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                15, //first column (0-based)
                15  //last column  (0-based)
        ));

        Cell cellTbl10 = headerTbl.createCell(16);
        cellTbl10.setCellValue("SALDO");
        cellTbl10.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                6, //last row  (0-based)
                16, //first column (0-based)
                16  //last column  (0-based)
        ));

        Row headerTblx1 = sheet.createRow(6);

        Cell cellTbl4x1 = headerTblx1.createCell(4);
        cellTbl4x1.setCellValue("Donatur");
        cellTbl4x1.setCellStyle(headTableStyle);

        Cell cellTbl4x2 = headerTblx1.createCell(5);
        cellTbl4x2.setCellValue("Sewa sawah");
        cellTbl4x2.setCellStyle(headTableStyle);

        Cell cellTbl4x3 = headerTblx1.createCell(6);
        cellTbl4x3.setCellValue("Infaq");
        cellTbl4x3.setCellStyle(headTableStyle);

        Cell cellTbl4x4 = headerTblx1.createCell(7);
        cellTbl4x4.setCellValue("Lain-lain");
        cellTbl4x4.setCellStyle(headTableStyle);

        Cell cellTbl10x1 = headerTblx1.createCell(10);
        cellTbl10x1.setCellValue("Perawatan Masjid");
        cellTbl10x1.setCellStyle(headTableStyle);

        Cell cellTbl10x2 = headerTblx1.createCell(11);
        cellTbl10x2.setCellValue("Renovasi Masjid");
        cellTbl10x2.setCellStyle(headTableStyle);

        Cell cellTbl10x3 = headerTblx1.createCell(12);
        cellTbl10x3.setCellValue("Dana Kegiatan");
        cellTbl10x3.setCellStyle(headTableStyle);

        Cell cellTbl10x4 = headerTblx1.createCell(13);
        cellTbl10x4.setCellValue("Lain-lain");
        cellTbl10x4.setCellStyle(headTableStyle);

        CellStyle dataCellStyle = workbook.createCellStyle();
        XSSFFont fontData = ((XSSFWorkbook) workbook).createFont();
        fontData.setFontName("Calibri");
        fontHeadTable.setFontHeight(12);
        dataCellStyle.setFont(fontData);

        Long akumPemasukan = Long.valueOf(0);
        Long akumPengeluaran = Long.valueOf(0);
        Long totalDonatur = Long.valueOf(0);
        Long totalSewaSawah = Long.valueOf(0);
        Long totalInfaq = Long.valueOf(0);
        Long totalLainPemasukan = Long.valueOf(0);
        Long totalPerawatanMasjid = Long.valueOf(0);
        Long totalRenovasiMasjid = Long.valueOf(0);
        Long totalDanaKegiatan = Long.valueOf(0);
        Long totalLainPengeluaran = Long.valueOf(0);

        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8;
        for(int  i=0;i<keuanganList.size();i++)
        {
            final Keuangan k = keuanganList.get(i);
            XSSFRow row = (XSSFRow) sheet.createRow(i+7);
            cell1 = row.createCell(1);
            cell1.setCellValue(i+1);
            cell1.setCellStyle(styleNomor);

            cell2 = row.createCell(2);
            cell2.setCellValue(formatLihatTglExcel(k.getTglKeuangan()));
            cell2.setCellStyle(dataCellStyle);

            cell3 = row.createCell(3);
            cell3.setCellValue(k.getKetKeuangan());
            cell3.setCellStyle(dataCellStyle);

            if (k.getTipeKeuangan().equals("Pemasukan")){
                cell5 = row.createCell(8);
                cell5.setCellValue(k.getNominalKeuangan());
                cell5.setCellStyle(dataCellStyle);
                akumPemasukan+=k.getNominalKeuangan();
                cell6 = row.createCell(9);
                cell6.setCellValue(akumPemasukan);
                cell6.setCellStyle(dataCellStyle);
                if (k.getJenisKeuangan().equals("Donatur")){
                    cell4 = row.createCell(4);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalDonatur+=k.getNominalKeuangan();
                } else if (k.getJenisKeuangan().equals("Sewa Sawah")){
                    cell4 = row.createCell(5);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalSewaSawah+=k.getNominalKeuangan();
                } else if (k.getJenisKeuangan().equals("Infaq")){
                    cell4 = row.createCell(6);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalInfaq+=k.getNominalKeuangan();
                } else {
                    cell4 = row.createCell(7);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalLainPemasukan+=k.getNominalKeuangan();
                }
            } else {
                cell5 = row.createCell(14);
                cell5.setCellValue(k.getNominalKeuangan());
                cell5.setCellStyle(dataCellStyle);
                akumPengeluaran+=k.getNominalKeuangan();
                cell7 = row.createCell(15);
                cell7.setCellValue(akumPengeluaran);
                cell7.setCellStyle(dataCellStyle);
                if (k.getJenisKeuangan().equals("Perawatan Masjid")){
                    cell4 = row.createCell(10);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalPerawatanMasjid+=k.getNominalKeuangan();
                } else if (k.getJenisKeuangan().equals("Renovasi Masjid")){
                    cell4 = row.createCell(11);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalRenovasiMasjid+=k.getNominalKeuangan();
                } else if (k.getJenisKeuangan().equals("Dana Kegiatan")){
                    cell4 = row.createCell(12);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalDanaKegiatan+=k.getNominalKeuangan();
                } else {
                    cell4 = row.createCell(13);
                    cell4.setCellValue(k.getNominalKeuangan());
                    cell4.setCellStyle(dataCellStyle);
                    totalLainPengeluaran+=k.getNominalKeuangan();
                }
            }

            cell8 = row.createCell(15);
            cell8.setCellValue(k.getJmlKasAkhir());
            cell8.setCellStyle(dataCellStyle);

        }

        Row rowTotal = sheet.createRow(batasData);
        Cell cellTotal = rowTotal.createCell(1);
        cellTotal.setCellValue("Total");
        cellTotal.setCellStyle(headTableStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                batasData, //first row (0-based)
                batasData, //last row  (0-based)
                1, //first column (0-based)
                3  //last column  (0-based)
        ));
        Cell cellTotalDonatur = rowTotal.createCell(4);
        cellTotalDonatur.setCellFormula("SUM(E8:E"+batasData+")");
        cellTotalDonatur.setCellStyle(headTableStyle);

        Cell cellTotalSewasaw = rowTotal.createCell(5);
        cellTotalSewasaw.setCellFormula("SUM(F8:F"+batasData+")");
        cellTotalSewasaw.setCellStyle(headTableStyle);

        Cell cellTotalInfaq = rowTotal.createCell(6);
        cellTotalInfaq.setCellFormula("SUM(G8:G"+batasData+")");
        cellTotalInfaq.setCellStyle(headTableStyle);

        Cell cellTotalLainPemasukan = rowTotal.createCell(7);
        cellTotalLainPemasukan.setCellFormula("SUM(H8:H"+batasData+")");
        cellTotalLainPemasukan.setCellStyle(headTableStyle);

        Cell cellTotalPemasukan = rowTotal.createCell(8);
        cellTotalPemasukan.setCellFormula("SUM(I8:I"+batasData+")");
        cellTotalPemasukan.setCellStyle(headTableStyle);

        Cell cellTotalPerawatanMasjid = rowTotal.createCell(10);
        cellTotalPerawatanMasjid.setCellFormula("SUM(K8:K"+batasData+")");
        cellTotalPerawatanMasjid.setCellStyle(headTableStyle);

        Cell cellTotalRenovMasjid = rowTotal.createCell(11);
        cellTotalRenovMasjid.setCellFormula("SUM(L8:L"+batasData+")");
        cellTotalRenovMasjid.setCellStyle(headTableStyle);

        Cell cellTotalDanaKeg = rowTotal.createCell(12);
        cellTotalDanaKeg.setCellFormula("SUM(M8:M"+batasData+")");
        cellTotalDanaKeg.setCellStyle(headTableStyle);

        Cell cellTotalLainPengeluaran = rowTotal.createCell(13);
        cellTotalLainPengeluaran.setCellFormula("SUM(N8:N"+batasData+")");
        cellTotalLainPengeluaran.setCellStyle(headTableStyle);

        Cell cellTotalPengeluaran = rowTotal.createCell(14);
        cellTotalPengeluaran.setCellFormula("SUM(O8:O"+batasData+")");
        cellTotalPengeluaran.setCellStyle(headTableStyle);



        CellStyle headDonaturStyle = workbook.createCellStyle();
        XSSFFont fontHeadDonatur = ((XSSFWorkbook) workbook).createFont();
        fontHeadDonatur.setFontName("Calibri");
        fontHeadDonatur.setBold(true);
        fontHeadDonatur.setFontHeight(14);
        headDonaturStyle.setFont(fontHeadDonatur);
        headDonaturStyle.setWrapText(true);

        Row rowJudulDonatur = sheet.createRow(judulDonatur);
        Cell cellJudulDonatur = rowJudulDonatur.createCell(1);
        cellJudulDonatur.setCellValue("DATA DONATUR MASJID");
        cellJudulDonatur.setCellStyle(headDonaturStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                judulDonatur, //first row (0-based)
                judulDonatur, //last row  (0-based)
                1, //first column (0-based)
                3  //last column  (0-based)
        ));

        CellStyle headTableDonaturStyle = workbook.createCellStyle();
        XSSFFont fontHeadDonaturTable = ((XSSFWorkbook) workbook).createFont();
        fontHeadDonaturTable.setFontName("Calibri");
        fontHeadDonaturTable.setBold(true);
        fontHeadDonaturTable.setFontHeight(12);
        headTableDonaturStyle.setFont(fontHeadDonaturTable);
        headTableDonaturStyle.setAlignment(HorizontalAlignment.CENTER);
        headTableDonaturStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headTableDonaturStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headTableDonaturStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headTableDonaturStyle.setWrapText(true);

        CellStyle cellTtdStyle = workbook.createCellStyle();
        XSSFFont fontTtd = ((XSSFWorkbook) workbook).createFont();
        fontTtd.setFontName("Calibri");
        fontTtd.setFontHeight(14);
        cellTtdStyle.setFont(fontTtd);
        cellTtdStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTtdStyle.setWrapText(true);

        Row rowHeadTableDonatur = sheet.createRow(dataDonatur);
        Cell cellHeadTableDonatur = rowHeadTableDonatur.createCell(1);
        cellHeadTableDonatur.setCellValue("NO");
        cellHeadTableDonatur.setCellStyle(headTableDonaturStyle);

        Cell cellHeadTableDonatur1 = rowHeadTableDonatur.createCell(2);
        cellHeadTableDonatur1.setCellValue("RT/RW");
        cellHeadTableDonatur1.setCellStyle(headTableDonaturStyle);

        Cell cellHeadTableDonatur2 = rowHeadTableDonatur.createCell(3);
        cellHeadTableDonatur2.setCellValue("PETUGAS");
        cellHeadTableDonatur2.setCellStyle(headTableDonaturStyle);

        Cell cellHeadTableDonatur3 = rowHeadTableDonatur.createCell(4);
        cellHeadTableDonatur3.setCellValue("TOTAL");
        cellHeadTableDonatur3.setCellStyle(headTableDonaturStyle);

        Cell cellTglTtd = rowHeadTableDonatur.createCell(14);
        cellTglTtd.setCellValue("Jombang, "+formatLihatTanggal(getCurentDate()));
        cellTglTtd.setCellStyle(cellTtdStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataTtd, //first row (0-based)
                dataTtd, //last row  (0-based)
                14, //first column (0-based)
                16  //last column  (0-based)
        ));

        Cell celDon1, celDon2, celDon3, celDon4;
        for(int  i=0;i<donaturList.size();i++) {
            final Donatur d = donaturList.get(i);
            XSSFRow row = (XSSFRow) sheet.createRow(dataDonatur+ 1 + i);
            celDon1 = row.createCell(1);
            celDon1.setCellValue(i + 1);
            celDon1.setCellStyle(styleNomor);

            celDon2 = row.createCell(2);
            celDon2.setCellValue(d.getWilayahDonatur());
            celDon2.setCellStyle(dataCellStyle);

            celDon3 = row.createCell(3);
            celDon3.setCellValue(d.getPetugasDonatur());
            celDon3.setCellStyle(dataCellStyle);

            celDon4 = row.createCell(4);
            celDon4.setCellValue(d.getNominaldonatur());
            celDon4.setCellStyle(dataCellStyle);
        }

        Row rowTableTotalDonatur = sheet.createRow(dataDonatur+donaturList.size()+1);
        Cell cellTotalRinciDonatur = rowTableTotalDonatur.createCell(1);
        cellTotalRinciDonatur.setCellValue("TOTAL");
        cellTotalRinciDonatur.setCellStyle(headTableDonaturStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataDonatur+donaturList.size()+1, //first row (0-based)
                dataDonatur+donaturList.size()+1, //last row  (0-based)
                1, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellValueTotalDonatur = rowTableTotalDonatur.createCell(4);
        cellValueTotalDonatur.setCellFormula("SUM(E"+(dataDonatur+1)+":E"+batasDonatur+")");
        cellValueTotalDonatur.setCellStyle(dataCellStyle);


        CellStyle headRekapStyle = workbook.createCellStyle();
        XSSFFont fontHeadRekap = ((XSSFWorkbook) workbook).createFont();
        fontHeadRekap.setFontName("Calibri");
        fontHeadRekap.setBold(true);
        fontHeadRekap.setFontHeight(14);
        headRekapStyle.setFont(fontHeadRekap);
        headRekapStyle.setWrapText(true);
        headRekapStyle.setAlignment(HorizontalAlignment.CENTER);
        headRekapStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row rowJudulRekap1 = sheet.createRow(judulRekap);
        Cell cellJudulRekap1 = rowJudulRekap1.createCell(1);
        cellJudulRekap1.setCellValue("REKAP LAPORAN KEUANGAN "+tvFilterBulanKeuangan.getText().toString().toUpperCase()+
                " "+tvFilterTahunKeuangan.getText().toString());
        cellJudulRekap1.setCellStyle(headRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                judulRekap, //first row (0-based)
                judulRekap, //last row  (0-based)
                1, //first column (0-based)
                4  //last column  (0-based)
        ));

        Row rowJudulRekap2 = sheet.createRow(judulRekap+1);
        Cell cellJudulRekap2 = rowJudulRekap2.createCell(1);
        cellJudulRekap2.setCellValue("MASJID AT-TAQWA");
        cellJudulRekap2.setCellStyle(headRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                judulRekap+1, //first row (0-based)
                judulRekap+1, //last row  (0-based)
                1, //first column (0-based)
                4  //last column  (0-based)
        ));

        CellStyle headTableRekapStyle = workbook.createCellStyle();
        XSSFFont fontHeadRekapTable = ((XSSFWorkbook) workbook).createFont();
        fontHeadRekapTable.setFontName("Calibri");
        fontHeadRekapTable.setBold(true);
        fontHeadRekapTable.setFontHeight(12);
        headTableRekapStyle.setFont(fontHeadRekapTable);
        headTableRekapStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headTableRekapStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headTableRekapStyle.setWrapText(true);

        CellStyle headTableRekapStyle2 = workbook.createCellStyle();
        XSSFFont fontHeadRekapTable2 = ((XSSFWorkbook) workbook).createFont();
        fontHeadRekapTable2.setFontName("Calibri");
        fontHeadRekapTable2.setBold(true);
        fontHeadRekapTable2.setFontHeight(12);
        headTableRekapStyle2.setFont(fontHeadRekapTable2);
        headTableRekapStyle2.setWrapText(true);

        Row rowRekapSaldoAwal = sheet.createRow(dataRekap);
        Cell cellKosongSaldo = rowRekapSaldoAwal.createCell(1);
        cellKosongSaldo.setCellStyle(headTableRekapStyle);

        Cell cellRekapSaldo = rowRekapSaldoAwal.createCell(2);
        cellRekapSaldo.setCellValue("Saldo Awal");
        cellRekapSaldo.setCellStyle(headTableRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap, //first row (0-based)
                dataRekap, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellValueRekapSaldo = rowRekapSaldoAwal.createCell(4);
        cellValueRekapSaldo.setCellValue(keuanganList.get(0).getJmlKasAwal());
        cellValueRekapSaldo.setCellStyle(headTableRekapStyle);

        Row rowHeadRekapPemasukan = sheet.createRow(dataRekap+1);
        Cell cellHeadRekapPemasukan = rowHeadRekapPemasukan.createCell(1);
        cellHeadRekapPemasukan.setCellValue("A");
        cellHeadRekapPemasukan.setCellStyle(styleHuruf);

        Cell cellHeadRekapPemasukan2 = rowHeadRekapPemasukan.createCell(2);
        cellHeadRekapPemasukan2.setCellValue("Pemasukan");
        cellHeadRekapPemasukan2.setCellStyle(headTableRekapStyle2);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+1, //first row (0-based)
                dataRekap+1, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Row rowRekapPemasukan1 = sheet.createRow(dataRekap+2);
        Cell cellRekapPemasukan1x1 = rowRekapPemasukan1.createCell(1);
        cellRekapPemasukan1x1.setCellValue(1);
        cellRekapPemasukan1x1.setCellStyle(styleNomor);

        Cell cellRekapPemasukan1x2 = rowRekapPemasukan1.createCell(2);
        cellRekapPemasukan1x2.setCellValue("Donatur Bulanan");
        cellRekapPemasukan1x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+2, //first row (0-based)
                dataRekap+2, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPemasukan1x3 = rowRekapPemasukan1.createCell(4);
        cellRekapPemasukan1x3.setCellValue(totalDonatur);
        cellRekapPemasukan1x3.setCellStyle(dataCellStyle);

        Row rowRekapPemasukan2 = sheet.createRow(dataRekap+3);
        Cell cellRekapPemasukan2x1 = rowRekapPemasukan2.createCell(1);
        cellRekapPemasukan2x1.setCellValue(2);
        cellRekapPemasukan2x1.setCellStyle(styleNomor);

        Cell cellRekapPemasukan2x2 = rowRekapPemasukan2.createCell(2);
        cellRekapPemasukan2x2.setCellValue("Sewa Sawah");
        cellRekapPemasukan2x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+3, //first row (0-based)
                dataRekap+3, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPemasukan2x3 = rowRekapPemasukan2.createCell(4);
        cellRekapPemasukan2x3.setCellValue(totalSewaSawah);
        cellRekapPemasukan2x3.setCellStyle(dataCellStyle);

        Row rowRekapPemasukan3 = sheet.createRow(dataRekap+4);
        Cell cellRekapPemasukan3x1 = rowRekapPemasukan3.createCell(1);
        cellRekapPemasukan3x1.setCellValue(3);
        cellRekapPemasukan3x1.setCellStyle(styleNomor);

        Cell cellRekapPemasukan3x2 = rowRekapPemasukan3.createCell(2);
        cellRekapPemasukan3x2.setCellValue("Infaq");
        cellRekapPemasukan3x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+4, //first row (0-based)
                dataRekap+4, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPemasukan3x3 = rowRekapPemasukan3.createCell(4);
        cellRekapPemasukan3x3.setCellValue(totalInfaq);
        cellRekapPemasukan3x3.setCellStyle(dataCellStyle);

        Row rowRekapPemasukan4 = sheet.createRow(dataRekap+5);
        Cell cellRekapPemasukan4x1 = rowRekapPemasukan4.createCell(1);
        cellRekapPemasukan4x1.setCellValue(4);
        cellRekapPemasukan4x1.setCellStyle(styleNomor);

        Cell cellRekapPemasukan4x2 = rowRekapPemasukan4.createCell(2);
        cellRekapPemasukan4x2.setCellValue("Lain-lain");
        cellRekapPemasukan4x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+5, //first row (0-based)
                dataRekap+5, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPemasukan4x3 = rowRekapPemasukan4.createCell(4);
        cellRekapPemasukan4x3.setCellValue(totalLainPemasukan);
        cellRekapPemasukan4x3.setCellStyle(dataCellStyle);

        Row rowRekapPemasukan5 = sheet.createRow(dataRekap+6);

        Cell cellRekapPemasukan5x1 = rowRekapPemasukan5.createCell(1);
        cellRekapPemasukan5x1.setCellStyle(headTableRekapStyle);

        Cell cellRekapPemasukan5x2 = rowRekapPemasukan5.createCell(2);
        cellRekapPemasukan5x2.setCellValue("Total Pemasukan");
        cellRekapPemasukan5x2.setCellStyle(headTableRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+6, //first row (0-based)
                dataRekap+6, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPemasukan5x3 = rowRekapPemasukan5.createCell(4);
        cellRekapPemasukan5x3.setCellFormula("SUM(E"+(dataRekap+3)+":E"+(dataRekap+6)+")");
        cellRekapPemasukan5x3.setCellStyle(headTableRekapStyle);

        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+7, //first row (0-based)
                dataRekap+7, //last row  (0-based)
                1, //first column (0-based)
                4  //last column  (0-based)
        ));

        Row rowHeadRekapPengeluaran = sheet.createRow(dataRekap+8);
        Cell cellHeadRekapPengeluaran = rowHeadRekapPengeluaran.createCell(1);
        cellHeadRekapPengeluaran.setCellValue("B");
        cellHeadRekapPengeluaran.setCellStyle(styleHuruf);

        Cell cellHeadRekapPengeluaran2 = rowHeadRekapPengeluaran.createCell(2);
        cellHeadRekapPengeluaran2.setCellValue("Pengeluaran");
        cellHeadRekapPengeluaran2.setCellStyle(headTableRekapStyle2);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+8, //first row (0-based)
                dataRekap+8, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Row rowRekapPengeluaran1 = sheet.createRow(dataRekap+9);
        Cell cellRekapPengeluaran1x1 = rowRekapPengeluaran1.createCell(1);
        cellRekapPengeluaran1x1.setCellValue(1);
        cellRekapPengeluaran1x1.setCellStyle(styleNomor);

        Cell cellRekapPengeluaran1x2 = rowRekapPengeluaran1.createCell(2);
        cellRekapPengeluaran1x2.setCellValue("Perawatan Masjid");
        cellRekapPengeluaran1x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+9, //first row (0-based)
                dataRekap+9, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPengeluaran1x3 = rowRekapPengeluaran1.createCell(4);
        cellRekapPengeluaran1x3.setCellValue(totalPerawatanMasjid);
        cellRekapPengeluaran1x3.setCellStyle(dataCellStyle);

        Row rowRekapPengeluaran2 = sheet.createRow(dataRekap+10);
        Cell cellRekapPengeluaran2x1 = rowRekapPengeluaran2.createCell(1);
        cellRekapPengeluaran2x1.setCellValue(2);
        cellRekapPengeluaran2x1.setCellStyle(styleNomor);

        Cell cellRekapPengeluaran2x2 = rowRekapPengeluaran2.createCell(2);
        cellRekapPengeluaran2x2.setCellValue("Renovasi Masjid");
        cellRekapPengeluaran2x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+10, //first row (0-based)
                dataRekap+10, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPengeluaran2x3 = rowRekapPengeluaran2.createCell(4);
        cellRekapPengeluaran2x3.setCellValue(totalRenovasiMasjid);
        cellRekapPengeluaran2x3.setCellStyle(dataCellStyle);

        Row rowRekapPengeluaran3 = sheet.createRow(dataRekap+11);
        Cell cellRekapPengeluaran3x1 = rowRekapPengeluaran3.createCell(1);
        cellRekapPengeluaran3x1.setCellValue(3);
        cellRekapPengeluaran3x1.setCellStyle(styleNomor);

        Cell cellRekapPengeluaran3x2 = rowRekapPengeluaran3.createCell(2);
        cellRekapPengeluaran3x2.setCellValue("Dana Kegiatan");
        cellRekapPengeluaran3x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+11, //first row (0-based)
                dataRekap+11, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPengeluaran3x3 = rowRekapPengeluaran3.createCell(4);
        cellRekapPengeluaran3x3.setCellValue(totalDanaKegiatan);
        cellRekapPengeluaran3x3.setCellStyle(dataCellStyle);

        Row rowRekapPengeluaran4 = sheet.createRow(dataRekap+12);
        Cell cellRekapPengeluaran4x1 = rowRekapPengeluaran4.createCell(1);
        cellRekapPengeluaran4x1.setCellValue(4);
        cellRekapPengeluaran4x1.setCellStyle(styleNomor);

        Cell cellRekapPengeluaran4x2 = rowRekapPengeluaran4.createCell(2);
        cellRekapPengeluaran4x2.setCellValue("Lain-lain");
        cellRekapPengeluaran4x2.setCellStyle(dataCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+12, //first row (0-based)
                dataRekap+12, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPengeluaran4x3 = rowRekapPengeluaran4.createCell(4);
        cellRekapPengeluaran4x3.setCellValue(totalLainPengeluaran);
        cellRekapPengeluaran4x3.setCellStyle(dataCellStyle);

        Row rowRekapPengeluaran5 = sheet.createRow(dataRekap+13);

        Cell cellRekapPengeluaran5x1 = rowRekapPengeluaran5.createCell(1);
        cellRekapPengeluaran5x1.setCellStyle(headTableRekapStyle);

        Cell cellRekapPengeluaran5x2 = rowRekapPengeluaran5.createCell(2);
        cellRekapPengeluaran5x2.setCellValue("Total Pengeluaran");
        cellRekapPengeluaran5x2.setCellStyle(headTableRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+13, //first row (0-based)
                dataRekap+13, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellRekapPengeluaran5x3 = rowRekapPengeluaran5.createCell(4);
        cellRekapPengeluaran5x3.setCellFormula("SUM(E"+(dataRekap+10)+":E"+(dataRekap+13)+")");
        cellRekapPengeluaran5x3.setCellStyle(headTableRekapStyle);

        Row rowRekapSaldoAkhir = sheet.createRow(dataRekap+14);
        Cell cellKosodngSaldoAkhir = rowRekapSaldoAkhir.createCell(1);
        cellKosodngSaldoAkhir.setCellStyle(headTableRekapStyle);

        Cell cellRekapSaldoAkhir = rowRekapSaldoAkhir.createCell(2);
        cellRekapSaldoAkhir.setCellValue("Saldo Akhir");
        cellRekapSaldoAkhir.setCellStyle(headTableRekapStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataRekap+14, //first row (0-based)
                dataRekap+14, //last row  (0-based)
                2, //first column (0-based)
                3  //last column  (0-based)
        ));

        Cell cellValueRekapSaldoAkhir = rowRekapSaldoAkhir.createCell(4);
        cellValueRekapSaldoAkhir.setCellValue(keuanganList.get(keuanganList.size()-1).getJmlKasAkhir());
        cellValueRekapSaldoAkhir.setCellStyle(headTableRekapStyle);

        Row rowTtdKetua;
        Cell cellTtdKetua;
        Cell cellTtdBendahara;
        if (sheet.getRow(dataTtd+2) ==null){
            rowTtdKetua = sheet.createRow(dataTtd+2);
            cellTtdKetua = rowTtdKetua.createCell(10);
            cellTtdBendahara = rowTtdKetua.createCell(14);
        } else{
            cellTtdKetua = sheet.getRow(dataTtd+2).createCell(10);
            cellTtdBendahara = sheet.getRow(dataTtd+2).createCell(14);
        }
        cellTtdKetua.setCellValue("Ketua Takmir");
        cellTtdKetua.setCellStyle(cellTtdStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataTtd+2, //first row (0-based)
                dataTtd+2, //last row  (0-based)
                10, //first column (0-based)
                12  //last column  (0-based)
        ));

        cellTtdBendahara.setCellValue("Bendahara");
        cellTtdBendahara.setCellStyle(cellTtdStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataTtd+2, //first row (0-based)
                dataTtd+2, //last row  (0-based)
                14, //first column (0-based)
                16  //last column  (0-based)
        ));

        Row rowNamaKetua;
        Cell cellNamaKetua;
        Cell cellNamaBendahara;
        if (sheet.getRow(dataTtd+6) == null){
            rowNamaKetua = sheet.createRow(dataTtd+6);
            cellNamaKetua = rowNamaKetua.createCell(10);
            cellNamaBendahara = rowNamaKetua.createCell(14);
        } else {
            cellNamaKetua = sheet.getRow(dataTtd+6).createCell(10);
            cellNamaBendahara = sheet.getRow(dataTtd+6).createCell(14);
        }

        cellNamaKetua.setCellValue("H. M. Supeno");
        cellNamaKetua.setCellStyle(cellTtdStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataTtd+6, //first row (0-based)
                dataTtd+6, //last row  (0-based)
                10, //first column (0-based)
                12  //last column  (0-based)
        ));

        cellNamaBendahara.setCellValue("Nanang Ma'ruf");
        cellNamaBendahara.setCellStyle(cellTtdStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                dataTtd+6, //first row (0-based)
                dataTtd+6, //last row  (0-based)
                14, //first column (0-based)
                16  //last column  (0-based)
        ));

        pt.applyBorders(sheet);
        ptDonatur.applyBorders(sheet);
        ptRekap.applyBorders(sheet);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
           // workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doSendEmail() {
        final ProgressDialog pd = ProgressDialog.show(this,"Loading", "Please Wait...",true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getLaporanApache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //atur data ke body email
                    record = "";
                    record += StringUtils.left("Riwayat Keuangan Kas Masjid At-Taqwa Dusun Jogoloyo", 32);
                    record += StringUtils.newLine(32);
                    record += StringUtils.left("Assalamu'alaikum Wr.Wb. : ", 48);
                    record += StringUtils.left("Berikut terlampir laporan keuangan kas Masjid At-Taqwa Jogoloyo pada Bulan "+tvFilterBulanKeuangan.getText().toString()+"" +
                            " Tahun "+tvFilterTahunKeuangan.getText().toString(), 32);
                    record += StringUtils.doubleLine(32);
                    record += StringUtils.newLine(32);

                    try {
                        step = 1;
                        Uri attachment;
                        attachment = FileProvider.getUriForFile(RiwayatActivity.this, "id.simtaq.androidapp.provider", file);
                        //attachment = Uri.fromFile(file);

                        step =2;
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("*/*");
                        step = 3;
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"simtaq9@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Riwayat Kas Masjid Bulan "+tvFilterBulanKeuangan.getText().toString());
                        step=4;
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        i.putExtra(Intent.EXTRA_STREAM, attachment);
                        i.putExtra(Intent.EXTRA_TEXT, record);
                        startActivity(Intent.createChooser(i, "Kirim laporan..."));
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(RiwayatActivity.this, "Tidak ada aplikasi pengirim yang terpasang."+step, Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(RiwayatActivity.this, "Tidak ada memuat email"+step, Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        pd.dismiss();
    }

    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(RiwayatActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RiwayatActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void doClick(int id) {
        Intent intent = new Intent(RiwayatActivity.this, DetailKeuanganActivity.class);
        intent.putExtra("intentDari", "riwayat keuangan");
        intent.putExtra("idKeuangan", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.riwayatmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eksporlap:
                doSendEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}