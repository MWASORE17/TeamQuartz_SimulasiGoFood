package quartz.gofoodsimulation.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sxio on 25-May-17.
 */

public class TransactionHeaderModel implements Serializable {
    public static final String CANCEL = "Canceled";
    public static final String PAID = "Paid";
    public static final String UNPAID = "Unpaid";

    public static final int GOPAY = 0;
    public static final int GOPOINT = 1;
    public static final int CASH = 2;

    private String idTransaksi;
    private long idUser;
    private Date tglTransaksi;
    private long idSeller;
    private long grandTotal;

    /* Status: Cancel, Paid, Unpaid */
    private String statusBayar;
    private int idDriver;
    private long deliveryFee;

    private int paymentMethod;

    public TransactionHeaderModel() {
    }

    public TransactionHeaderModel(long idUser, long idSeller, long grandTotal,
                                  String statusBayar, int idDriver, long deliveryFee) {
        this.idUser = idUser;
        this.idSeller = idSeller;
        this.grandTotal = grandTotal;
        this.statusBayar = statusBayar;
        this.idDriver = idDriver;
        this.deliveryFee = deliveryFee;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Date getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(Date tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public long getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(long idSeller) {
        this.idSeller = idSeller;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(String statusBayar) {
        this.statusBayar = statusBayar;
    }

    public int getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(int idDriver) {
        this.idDriver = idDriver;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
