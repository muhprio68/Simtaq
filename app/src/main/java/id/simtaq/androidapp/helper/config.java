package id.simtaq.androidapp.helper;

import android.content.Context;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class config {
    public static Locale locale = new Locale("in", "ID");
    //public static String url = "http://simtaq-app.herokuapp.com";
    public static String url = "http://192.168.0.13:8080/restfulapi/public";
    public static String urlKegiatan = "http://10.200.58.161:8080/restfulapi/public/kegiatan";
    public static String urlKeuangan = "http://10.200.58.161:8080/restfulapi/public/keuangan";
    public static String urlSaldo = "http://10.200.58.161:8080/restfulapi/public/saldo";
//https://run.mocky.io/v3/3d965384-7078-4ee5-8209-a71a4dfc02c0
    //kampus 10.200.58.161
    //kos 192.168.0.27
    //10.208.178.101

    public static String toRupiah(String nominal){
        String hasil = "";
        NumberFormat formatRupiah = NumberFormat.getInstance(locale);
        hasil = (String) formatRupiah.format(Double.valueOf(nominal));
        return "Rp "+hasil;
    }

    public static String getCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public static String getIdCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", locale);
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }
}
