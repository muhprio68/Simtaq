package id.simtaq.androidapp.models;

import java.util.Comparator;

public class Bulan {
    public static Comparator<Bulan> BulanComparator = new Comparator<Bulan>() {

        public int compare(Bulan b1, Bulan b2) {
            String Bulan1 = b1.tanggal;
            String Bulan2 = b2.tanggal;

            //ascending order
            return Bulan1.compareTo(Bulan2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    public String tanggal;
    public Integer jumlah;

    public Bulan() {
    }

    public Bulan(String tanggal, Integer jumlah) {
        this.tanggal = tanggal;
        this.jumlah = jumlah;
    }

    public static Comparator<Bulan> TanggalComparator = new Comparator<Bulan>() {

        public int compare(Bulan b1, Bulan b2) {
            String Tanggal1 = b1.tanggal.toUpperCase();
            String Tanggal2 = b2.tanggal.toUpperCase();

            //ascending order
            return Tanggal1.compareTo(Tanggal2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

}

