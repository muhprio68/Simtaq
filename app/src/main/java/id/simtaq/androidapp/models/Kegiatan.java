package id.simtaq.androidapp.models;

public class Kegiatan {

    private String idKegiatan;
    private boolean isUmum;
    private String namaKegiatan;
    private String tanggalKegiatan;
    private String waktuKegiatan;
    private String deskripsiKegiatan;
    private String tempatKegiatan;
    private String pembicaraKegiatan;

    public Kegiatan(String idKegiatan, boolean isUmum, String namaKegiatan, String tanggalKegiatan, String waktuKegiatan, String deskripsiKegiatan, String tempatKegiatan, String pembicaraKegiatan) {
        this.idKegiatan = idKegiatan;
        this.isUmum = isUmum;
        this.namaKegiatan = namaKegiatan;
        this.tanggalKegiatan = tanggalKegiatan;
        this.waktuKegiatan = waktuKegiatan;
        this.deskripsiKegiatan = deskripsiKegiatan;
        this.tempatKegiatan = tempatKegiatan;
        this.pembicaraKegiatan = pembicaraKegiatan;
    }

    public String getIdKegiatan() {
        return idKegiatan;
    }

    public void setIdKegiatan(String idKegiatan) {
        this.idKegiatan = idKegiatan;
    }

    public boolean isUmum() {
        return isUmum;
    }

    public void setUmum(boolean umum) {
        isUmum = umum;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public String getTanggalKegiatan() {
        return tanggalKegiatan;
    }

    public void setTanggalKegiatan(String tanggalKegiatan) {
        this.tanggalKegiatan = tanggalKegiatan;
    }

    public String getWaktuKegiatan() {
        return waktuKegiatan;
    }

    public void setWaktuKegiatan(String waktuKegiatan) {
        this.waktuKegiatan = waktuKegiatan;
    }

    public String getDeskripsiKegiatan() {
        return deskripsiKegiatan;
    }

    public void setDeskripsiKegiatan(String deskripsiKegiatan) {
        this.deskripsiKegiatan = deskripsiKegiatan;
    }

    public String getTempatKegiatan() {
        return tempatKegiatan;
    }

    public void setTempatKegiatan(String tempatKegiatan) {
        this.tempatKegiatan = tempatKegiatan;
    }

    public String getPembicaraKegiatan() {
        return pembicaraKegiatan;
    }

    public void setPembicaraKegiatan(String pembicaraKegiatan) {
        this.pembicaraKegiatan = pembicaraKegiatan;
    }
}
