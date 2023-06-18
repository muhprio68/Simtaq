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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import java.util.Locale;
import java.util.Map;

import id.simtaq.androidapp.adapter.RiwayatListAdapter;
import id.simtaq.androidapp.helper.Preferences;
import id.simtaq.androidapp.models.Keuangan;
import id.simtaq.androidapp.utils.StringUtils;


import static id.simtaq.androidapp.helper.config.locale;
import static id.simtaq.androidapp.helper.config.url;

public class RiwayatActivity extends AppCompatActivity implements RiwayatListAdapter.IRiwayatListAdapter {

    private ConstraintLayout clRiwayatKeuangan;
    private Toolbar toolbar;
    private ProgressBar pbRiwayatKeuangan;
    private RecyclerView rvRiwayat;
    private ArrayList<Keuangan> keuanganList;
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
    String record, data, column, combined, sum;
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
        tvFilterBulanKeuangan.setText(sBulan);
        tvFilterTahunKeuangan.setText(sTahun);
        initLevel(level);
        tvFilterTahunKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSendEmail();
//                Uri l = FileProvider.getUriForFile(RiwayatActivity.this, RiwayatActivity.this.getApplicationContext().getPackageName()+".provider", exportToExcel());
//                //exportToExcel()
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("*/*");
//                step = 3;
//                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"simtaq9@gmail.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "Riwayat Kas Masjid Bulan "+tvFilterBulanKeuangan.getText().toString());
//                step=4;
//                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                i.putExtra(Intent.EXTRA_STREAM, l);
//                startActivity(Intent.createChooser(i, "Kirim laporan..."));

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

//    public void addData(){
//        riwayatKasArrayList = new ArrayList<>();
//        riwayatKasArrayList.add(new RiwayatKas("RK000001","Kotak Amal", true,"01/01/2022","Kotak Amal Sholat Jum'at",450000,8000000, 8450000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000002","Pembayaran Listrik", false,"05/01/2022","Pembayaran Listrik Bulan Desember ajdjhfhfkjhfj ajgdjhdgjhdgdhgd ajhsgjhdgdjhgdjh kjdgjhdgdjhgdjhgdjh",200000, 8450000, 8250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000003","Kotak Amal", true,"08/01/2022","Kotak Amal Sholat Jum'at",400000, 8250000, 8650000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000004","Renovasi Dinding", false,"13/01/2022","Pengecatan",500000, 8650000,8150000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000005","Kotak Amal", true,"16/01/2022","Kotak Amal Sholat Jum'at",900000, 8150000, 9050000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000006","Kotak Amal", true,"19/01/2022","Kotak Amal Sholat Jum'at",450000, 9050000, 9500000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000007","Pembayaran Listrik", false,"05/02/2022","Pembayaran Listrik Bulan Januari",200000, 9500000, 9300000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000008","Kotak Amal", true,"08/02/2022","Kotak Amal Sholat Jum'at",400000, 9300000, 9700000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000009","Renovasi Dinding", false,"11/02/2022","Pengecatan",500000, 9700000, 9200000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000010","Kotak Amal", true,"15/02/2022","Kotak Amal Sholat Jum'at",900000, 9200000, 10100000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000011","Kotak Amal", true,"27/02/2022","Kotak Amal Sholat Jum'at",450000, 10100000, 10550000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000012","Pembayaran Listrik", false,"05/03/2022","Pembayaran Listrik Bulan Februari",200000, 10550000, 10350000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000013","Kotak Amal", true,"06/03/2022","Kotak Amal Sholat Jum'at",400000, 10350000, 10750000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000014","Renovasi Dinding", false,"08/03/2022","Pengecatan",500000, 10750000, 10250000));
//        riwayatKasArrayList.add(new RiwayatKas("RK000015","Kotak Amal", true,"13/03/2022","Kotak Amal Sholat Jum'at",90000, 10250000, 11150000));
//    }

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
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");

        Row header1 = sheet.createRow(0);
        CellStyle header1Style = workbook.createCellStyle();
        header1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header1Style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        header1Style.setFont(font);
        Cell headerCell1 = header1.createCell(1);
        headerCell1.setCellValue("LAPORAN KEUANGAN MASJID AT - TAQWA");
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                1, //first column (0-based)
                17  //last column  (0-based)
        ));

        Row header = sheet.createRow(5);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
        cell.setCellStyle(style);

        File currDir = new File(".");
        String path = root.getAbsolutePath();
        String fileLocation = path + "/tempbcb.xlsx";
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

//    private File getLaporanExcel(){
//        try {
//            //buat file csv attachment
//            requestRuntimePermission();
//            file = null;
//            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            String csvFile = "Laporan.xlsx";
//            if (root.canWrite()) {
//                File dir = new File(root.getAbsolutePath() + "/simtaq");
//
//                if (!dir.isDirectory()) {
//                    dir.mkdirs();
//                }
//
//                file = new File(dir, csvFile);
//
//                WorkbookSettings wbSettings = new WorkbookSettings();
//                wbSettings.setLocale(new Locale("en", "EN"));
//                WritableWorkbook workbook;
//                workbook = Workbook.createWorkbook(file, wbSettings);
//                // sheet 1
//                WritableSheet sheet = workbook.createSheet("Riwayat Kas Masjid", 0);
//
//                WritableFont cellJud = new WritableFont(WritableFont.ARIAL, 16);
//                cellJud.setColour(Colour.BLACK);
//                cellJud.setBoldStyle(WritableFont.BOLD);
//                WritableFont cellJud2 = new WritableFont(WritableFont.ARIAL, 16);
//                cellJud2.setColour(Colour.BLACK);
//                cellJud2.setBoldStyle(WritableFont.BOLD);
//                cellJud2.setUnderlineStyle(UnderlineStyle.SINGLE);
//
//                WritableFont cellHead = new WritableFont(WritableFont.TIMES, 14);
//                cellHead.setColour(Colour.BLACK);
//                cellHead.setBoldStyle(WritableFont.BOLD);
//
//                WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
//                cellFont.setColour(Colour.BLACK);
//
//                WritableCellFormat cellFormatJud = new WritableCellFormat(cellJud);
//                cellFormatJud.setAlignment(Alignment.CENTRE);
//                WritableCellFormat cellFormatJud2 = new WritableCellFormat(cellJud2);
//                cellFormatJud2.setAlignment(Alignment.CENTRE);
//
//                WritableCellFormat cellFormatHead = new WritableCellFormat(cellHead);
//                cellFormatHead.setAlignment(Alignment.CENTRE);
//                cellFormatHead.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
//
//                WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
//                cellFormat.setAlignment(Alignment.CENTRE);
//                cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
//
//                sheet.setColumnView(0, 5);
//                sheet.setColumnView(1, 10);
//                sheet.setColumnView(2, 26);
//                sheet.setColumnView(3, 28);
//                sheet.mergeCells(0,0,3,0);
//                //sheet.mergeCells(0,transaksiList.size()+2, 2,transaksiList.size()+2);
//                sheet.addCell(new Label(1 , 2, "LAPORAN KEUANGAN MASJID AT - TAQWA", cellFormatJud));
//                sheet.addCell(new Label(1 , 3, "Dsn/Ds. Jogoloyo - Sumobito - Jombang", cellFormatJud));
//                sheet.addCell(new Label(1 , 4, "Arus Kas  Keuangan Bulan "+tvFilterBulanKeuangan.getText().toString()+" Tahun "+tvFilterTahunKeuangan.getText().toString(), cellFormatJud2));
//
//                sheet.addCell(new Label(1, 6, "No", cellFormatHead));
//                sheet.addCell(new Label(2, 6, "Tanggal", cellFormatHead));
//                sheet.addCell(new Label(3, 6, "Keterangan", cellFormatHead));
//                sheet.addCell(new Label(4, 6, "Pemasukan perJenis", cellFormatHead));
////                            for (int i = 0; i < keuanganList.size(); i++) {
////                                sheet.addCell(new Label(1, i+2, i+1 + "", cellFormat));
////                                sheet.addCell(new Label(2, i+2, keuanganList.get(i).getTglKeuangan(), cellFormat));
////                                sheet.addCell(new Label(3, i+2, keuanganList.get(i).getKetKeuangan(), cellFormat));
////                                sheet.addCell(new Label(4, i+2, "Rp. "+toRupiah(transaksiList.get(i).getTotal() + ""), cellFormat));
////                            }
////                            sheet.addCell(new Label(0,transaksiList.size()+2,"Total", cellFormatHead));
////                            sheet.addCell(new Label(3,transaksiList.size()+2,sum, cellFormatHead));
//                workbook.write();
//                workbook.close();
//            } else {
//                Toast.makeText(RiwayatActivity.this, "Cannot write" , Toast.LENGTH_LONG).show();
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Toast.makeText(RiwayatActivity.this, "File excel tidak bisa dibuat" , Toast.LENGTH_LONG).show();
//        }
//        return file;
//    }

    private void doSendEmail() {
        final ProgressDialog pd = ProgressDialog.show(RiwayatActivity.this,"Loading", "Please Wait...",true);
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
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.eksporlap){
            //doSendEmail();
            //exportToExcel();
            getLaporanApache();
        }
        return true;
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