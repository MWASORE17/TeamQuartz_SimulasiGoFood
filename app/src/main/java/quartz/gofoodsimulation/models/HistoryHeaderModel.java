package quartz.gofoodsimulation.models;

/**
 * Created by sxio on 31-May-17.
 */

public class HistoryHeaderModel {
    private String idTransaksi;
    private String sellerName;
    private String sellerAddress;
    private String tglOrder;
    private String statusBayar;
    private long grandTotal;
    private long biayaAntar;

    public HistoryHeaderModel() {
    }

    public HistoryHeaderModel(String idTransaksi, String sellerName, String sellerAddress,
                              String tglOrder, long grandTotal, long biayaAntar, String statusBayar) {
        this.idTransaksi = idTransaksi;
        this.sellerName = sellerName;
        this.sellerAddress = sellerAddress;
        this.tglOrder = tglOrder;
        this.grandTotal = grandTotal;
        this.biayaAntar = biayaAntar;
        this.statusBayar = statusBayar;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getTglOrder() {
        return tglOrder;
    }

    public void setTglOrder(String tglOrder) {
        this.tglOrder = tglOrder;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public long getBiayaAntar() {
        return biayaAntar;
    }

    public void setBiayaAntar(long biayaAntar) {
        this.biayaAntar = biayaAntar;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(String statusOrder) {
        this.statusBayar = statusOrder;
    }
}
