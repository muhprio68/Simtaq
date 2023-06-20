package id.simtaq.androidapp.helper;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class config {
    public static Locale locale = new Locale("in", "ID");
    //public static String url = "http://simtaq-app.herokuapp.com";
    //public static String url = "http://192.168.0.26:8080/restfulapi/public";
    //public static String url = "http://192.168.225.211:8080/restfulapi/public";
    //public static String url = "http://10.211.87.117:8080/restfulapi/public";
    //public static String url = "http://simtaq.my.id/restfulapi/public";
    public static String url = "https://simtaq.my.id";


//https://run.mocky.io/v3/3d965384-7078-4ee5-8209-a71a4dfc02c0
    //kampus 10.200.58.161
    //kos 192.168.0.27
    //10.208.178.101


    //level user
    //1. Jama'ah masjid/user biasa
    //2. Bendahara Takmir
    //3. Humas Takmir
    //4. Superadmin

    public static String toRupiah(String nominal){
        String hasil = "";
        NumberFormat formatRupiah = NumberFormat.getInstance(locale);
        hasil = (String) formatRupiah.format(Double.valueOf(nominal));
        return "Rp "+hasil;
    }

    public static String formatSimpanTanggal(String tgl){
        final String OLD_FORMAT = "dd MMMM yyyy";
        final String NEW_FORMAT = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, locale);
        Date d = null;
        try {
            d = sdf.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    public static String formatLihatTanggal(String tgl){
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd MMMM yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, locale);
        Date d = null;
        try {
            d = sdf.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    public static String formatLihatTglExcel(String tgl){
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd-MMM-yy";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, locale);
        Date d = null;
        try {
            d = sdf.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    public static String formatLihatFullTanggal(String tgl) {
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "EEEE, dd MMMM yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, locale);
        Date d = null;
        try {
            d = sdf.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    public static String formatSimpanWaktu(String waktu){
        String wkt = waktu;
        Locale locale = new Locale("in", "ID");
        java.text.DateFormat formatter = new SimpleDateFormat("hh:mm", locale); //dd/MM/yyyy  yyyy-MM-dd
        Date date = null;
        try {
            date = (Date)formatter.parse(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", locale);
        String wktBaru = newFormat.format(date);
        return wktBaru;
    }

    public static String formatLihatWaktu(String waktu){
        String wkt = waktu;
        Locale locale = new Locale("in", "ID");
        java.text.DateFormat formatter = new SimpleDateFormat("hh:mm:ss", locale);
        Date date = null;
        try {
            date = (Date)formatter.parse(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", locale);
        String wktBaru = newFormat.format(date);
        return wktBaru;
    }

    public static String getCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public static Date getFullCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -2);
        Date date = new Date();
        date = cal.getTime();
        //System.out.println(dateFormat.format(cal.getTime()));
        return date;
    }

    public static String getIdCurentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", locale);
        Calendar cal = Calendar.getInstance();
        //System.out.println(dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

//            ^                 # start-of-string
//            (?=.*[0-9])       # a digit must occur at least once
//            (?=.*[a-z])       # a lower case letter must occur at least once
//            (?=.*[A-Z])       # an upper case letter must occur at least once
//            (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
//            (?=\\S+$)          # no whitespace allowed in the entire string
//            .{4,}             # anything, at least six places though
//            $                 # end-of-string
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
