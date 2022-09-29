package id.simtaq.androidapp.models;

public class Keuangan {
    private int idKeuangan;
    private String noKeuangan;
    private String tipeKeuangan;
    private String tglKeuangan;
    private String ketKeuangan;
    private String statusKeuangan;
    private Long nominalKeuangan;
    private Long jmlKasAwal;
    private Long jmlKasAkhir;
    private String deskripsiKeuangan;
    private String createAt;
    private String updateAt;

    public Keuangan(int idKeuangan, String noKeuangan, String tipeKeuangan, String tglKeuangan, String ketKeuangan, String statusKeuangan, Long nominalKeuangan, Long jmlKasAwal, Long jmlKasAkhir, String deskripsiKeuangan, String createAt, String updateAt) {
        this.idKeuangan = idKeuangan;
        this.noKeuangan = noKeuangan;
        this.tipeKeuangan = tipeKeuangan;
        this.tglKeuangan = tglKeuangan;
        this.ketKeuangan = ketKeuangan;
        this.statusKeuangan = statusKeuangan;
        this.nominalKeuangan = nominalKeuangan;
        this.jmlKasAwal = jmlKasAwal;
        this.jmlKasAkhir = jmlKasAkhir;
        this.deskripsiKeuangan = deskripsiKeuangan;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getIdKeuangan() {
        return idKeuangan;
    }

    public void setIdKeuangan(int idKeuangan) {
        this.idKeuangan = idKeuangan;
    }

    public String getNoKeuangan() {
        return noKeuangan;
    }

    public void setNoKeuangan(String noKeuangan) {
        this.noKeuangan = noKeuangan;
    }

    public String getTipeKeuangan() {
        return tipeKeuangan;
    }

    public void setTipeKeuangan(String tipeKeuangan) {
        this.tipeKeuangan = tipeKeuangan;
    }

    public String getTglKeuangan() {
        return tglKeuangan;
    }

    public void setTglKeuangan(String tglKeuangan) {
        this.tglKeuangan = tglKeuangan;
    }

    public String getKetKeuangan() {
        return ketKeuangan;
    }

    public void setKetKeuangan(String ketKeuangan) {
        this.ketKeuangan = ketKeuangan;
    }

    public String getStatusKeuangan() {
        return statusKeuangan;
    }

    public void setStatusKeuangan(String statusKeuangan) {
        this.statusKeuangan = statusKeuangan;
    }

    public Long getNominalKeuangan() {
        return nominalKeuangan;
    }

    public void setNominalKeuangan(Long nominalKeuangan) {
        this.nominalKeuangan = nominalKeuangan;
    }

    public Long getJmlKasAwal() {
        return jmlKasAwal;
    }

    public void setJmlKasAwal(Long jmlKasAwal) {
        this.jmlKasAwal = jmlKasAwal;
    }

    public Long getJmlKasAkhir() {
        return jmlKasAkhir;
    }

    public void setJmlKasAkhir(Long jmlKasAkhir) {
        this.jmlKasAkhir = jmlKasAkhir;
    }

    public String getDeskripsiKeuangan() {
        return deskripsiKeuangan;
    }

    public void setDeskripsiKeuangan(String deskripsiKeuangan) {
        this.deskripsiKeuangan = deskripsiKeuangan;
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
