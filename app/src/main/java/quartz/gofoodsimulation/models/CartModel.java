package quartz.gofoodsimulation.models;

import java.util.ArrayList;

/**
 * Created by sxio on 23-May-17.
 * Modified:
 * sxio - 24-May-17: pindahkan Cart dr SellerActivity ke CartModel
 */

public class CartModel {
    public static CartModel cart;

    private long idSeller;
    private ArrayList<GroceryModel> groceries;

    public CartModel() {
        idSeller = -1;
        groceries = new ArrayList<>();
    }

    public int getTotalQuantity() {
        int q = 0;
        for (GroceryModel grocery : groceries) {
            q += grocery.getQuantity();
        }
        return q;
    }

    public long getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(long idSeller) {
        this.idSeller = idSeller;
    }

    public ArrayList<GroceryModel> getGroceries() {
        return groceries;
    }

    public void setGroceries(ArrayList<GroceryModel> groceries) {
        this.groceries = groceries;
    }

    public void addGrocery(GroceryModel g) {
        boolean _exist = false;
        for (GroceryModel grocery : groceries) {
            if (grocery.getFood().getIdFood().equals(g.getFood().getIdFood())) {
                _exist = true;
                grocery.setQuantity(g.getQuantity());
            }
        }
        if (!_exist) {
            groceries.add(g);
        }
    }

    public void subtractGrocery(GroceryModel g) {
        for (int i = 0; i < groceries.size(); i++) {
            if (groceries.get(i).getFood().getIdFood().equals(g.getFood().getIdFood())) {
                if (g.getQuantity() == 0) {
                    groceries.remove(i);
                    break;
                }
                groceries.get(i).setQuantity(g.getQuantity());
            }
        }
    }

    public void clearCart() {
        CartModel.cart = new CartModel();
    }

    public long getTotalPrice() {
        long price = 0;
        for (GroceryModel i : groceries) {
            price += i.getPrice();
        }
        return price;
    }
}