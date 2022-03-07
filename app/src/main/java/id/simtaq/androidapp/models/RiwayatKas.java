package id.simtaq.androidapp.models;

public class RiwayatKas {
    private String id;
    private String keterangan;
    private boolean isPemasukan;
    private String tanggal;
    private String deskripsi;
    private int nominal;

    public RiwayatKas(String id, String keterangan, boolean isPemasukan, String tanggal, String deskripsi, int nominal) {
        this.id = id;
        this.keterangan = keterangan;
        this.isPemasukan = isPemasukan;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.nominal = nominal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public boolean isPemasukan() {
        return isPemasukan;
    }

    public void setPemasukan(boolean pemasukan) {
        isPemasukan = pemasukan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
