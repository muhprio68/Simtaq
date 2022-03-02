package id.simtaq.androidapp.models;

public class Pengaturan {

    private int idPengaturan;
    private int iconPengaturan;
    private String namaPengaturan;

    public Pengaturan(int idPengaturan, int iconPengaturan, String namaPengaturan) {
        this.idPengaturan = idPengaturan;
        this.iconPengaturan = iconPengaturan;
        this.namaPengaturan = namaPengaturan;
    }

    public int getIdPengaturan() {
        return idPengaturan;
    }

    public void setIdPengaturan(int idPengaturan) {
        this.idPengaturan = idPengaturan;
    }

    public int getIconPengaturan() {
        return iconPengaturan;
    }

    public void setIconPengaturan(int iconPengaturan) {
        this.iconPengaturan = iconPengaturan;
    }

    public String getNamaPengaturan() {
        return namaPengaturan;
    }

    public void setNamaPengaturan(String namaPengaturan) {
        this.namaPengaturan = namaPengaturan;
    }
}
