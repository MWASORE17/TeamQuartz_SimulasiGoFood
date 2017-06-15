package quartz.gofoodsimulation.models;

/**
 * Created by sxio on 25-May-17.
 */

public class TransactionDetailModel {
    private String idTransaksi;
    private String idFood;
    private int quantity;
    private long pcsHarga;

    public TransactionDetailModel() {
    }

    public TransactionDetailModel(String idTransaksi, String idFood, int quantity, long pcsHarga) {
        this.idTransaksi = idTransaksi;
        this.idFood = idFood;
        this.quantity = quantity;
        this.pcsHarga = pcsHarga;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPcsHarga() {
        return pcsHarga;
    }

    public void setPcsHarga(long pcsHarga) {
        this.pcsHarga = pcsHarga;
    }
}
