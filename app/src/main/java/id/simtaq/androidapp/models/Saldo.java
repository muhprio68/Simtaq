package id.simtaq.androidapp.models;

public class Saldo {
    private Long jmlSaldo;
    private String updateAt;

    public Saldo(Long jmlSaldo, String updateAt) {
        this.jmlSaldo = jmlSaldo;
        this.updateAt = updateAt;
    }

    public Long getJmlSaldo() {
        return jmlSaldo;
    }

    public void setJmlSaldo(Long jmlSaldo) {
        this.jmlSaldo = jmlSaldo;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
