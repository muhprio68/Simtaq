package id.simtaq.androidapp.models;

public class Kegiatan {

    private int idKegiatan;
    private String noKegiatan;
    private String namaKegiatan;
    private String tipeKegiatan;
    private String tglKegiatan;
    private String waktuKegiatan;
    private String deskripsiKegiatan;
    private String tempatKegiatan;
    private String pembicaraKegiatan;
    private String createAt;
    private String updateAt;

    public Kegiatan(int idKegiatan, String noKegiatan, String namaKegiatan, String tipeKegiatan, String tglKegiatan, String waktuKegiatan, String deskripsiKegiatan, String tempatKegiatan, String pembicaraKegiatan, String createAt, String updateAt) {
        this.idKegiatan = idKegiatan;
        this.noKegiatan = noKegiatan;
        this.namaKegiatan = namaKegiatan;
        this.tipeKegiatan = tipeKegiatan;
        this.tglKegiatan = tglKegiatan;
        this.waktuKegiatan = waktuKegiatan;
        this.deskripsiKegiatan = deskripsiKegiatan;
        this.tempatKegiatan = tempatKegiatan;
        this.pembicaraKegiatan = pembicaraKegiatan;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getIdKegiatan() {
        return idKegiatan;
    }

    public void setIdKegiatan(int idKegiatan) {
        this.idKegiatan = idKegiatan;
    }

    public String getNoKegiatan() {
        return noKegiatan;
    }

    public void setNoKegiatan(String noKegiatan) {
        this.noKegiatan = noKegiatan;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public String getTipeKegiatan() {
        return tipeKegiatan;
    }

    public void setTipeKegiatan(String tipeKegiatan) {
        this.tipeKegiatan = tipeKegiatan;
    }

    public String getTglKegiatan() {
        return tglKegiatan;
    }

    public void setTglKegiatan(String tglKegiatan) {
        this.tglKegiatan = tglKegiatan;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
