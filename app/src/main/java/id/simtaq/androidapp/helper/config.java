package id.simtaq.androidapp.helper;

import android.content.Context;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class config {
    public static Locale locale = new Locale("in", "ID");
    public static String url = "http://10.200.58.161:8080/restfulapi/public/kegiatan";
    public static String urlKeuangan = "http://10.200.58.161:8080/restfulapi/public/keuangan";
    public static String urlSaldo = "http://10.200.58.161:8080/restfulapi/public/saldo";
//https://run.mocky.io/v3/3d965384-7078-4ee5-8209-a71a4dfc02c0
    //kampus 10.200.58.161
    //kos 192.168.0.27

    public static String toRupiah(String nominal){
        String hasil = "";
        NumberFormat formatRupiah = NumberFormat.getInstance(locale);
        hasil = (String) formatRupiah.format(Double.valueOf(nominal));
        return "Rp "+hasil;
    }
}
