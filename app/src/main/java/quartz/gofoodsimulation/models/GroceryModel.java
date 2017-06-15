package quartz.gofoodsimulation.models;

/**
 * Created by sxio on 23-May-17.
 */

public class GroceryModel {
    private FoodModel food;
    private int quantity;

    public GroceryModel(FoodModel food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public FoodModel getFood() {
        return food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return food.getPriceFood() * quantity;
    }
}
