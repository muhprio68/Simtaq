package id.simtaq.androidapp.models;

public class Donatur {
    private int idDonatur;
    private int idKeuangan;
    private String tglDonatur;
    private String wilayahDonatur;
    private String petugasDonatur;
    private Long nominaldonatur;
    private String createAt;
    private String updateAt;

    public Donatur(int idDonatur, int idKeuangan, String tglDonatur, String wilayahDonatur, String petugasDonatur, Long nominaldonatur, String createAt, String updateAt) {
        this.idDonatur = idDonatur;
        this.idKeuangan = idKeuangan;
        this.tglDonatur = tglDonatur;
        this.wilayahDonatur = wilayahDonatur;
        this.petugasDonatur = petugasDonatur;
        this.nominaldonatur = nominaldonatur;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getIdDonatur() {
        return idDonatur;
    }

    public void setIdDonatur(int idDonatur) {
        this.idDonatur = idDonatur;
    }

    public int getIdKeuangan() {
        return idKeuangan;
    }

    public void setIdKeuangan(int idKeuangan) {
        this.idKeuangan = idKeuangan;
    }

    public String getTglDonatur() {
        return tglDonatur;
    }

    public void setTglDonatur(String tglDonatur) {
        this.tglDonatur = tglDonatur;
    }

    public String getWilayahDonatur() {
        return wilayahDonatur;
    }

    public void setWilayahDonatur(String wilayahDonatur) {
        this.wilayahDonatur = wilayahDonatur;
    }

    public String getPetugasDonatur() {
        return petugasDonatur;
    }

    public void setPetugasDonatur(String petugasDonatur) {
        this.petugasDonatur = petugasDonatur;
    }

    public Long getNominaldonatur() {
        return nominaldonatur;
    }

    public void setNominaldonatur(Long nominaldonatur) {
        this.nominaldonatur = nominaldonatur;
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
