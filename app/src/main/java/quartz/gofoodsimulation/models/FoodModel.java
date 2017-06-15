package quartz.gofoodsimulation.models;

/**
 * Created by meikelwis on 4/1/17.
 */

public class FoodModel {
    private String idFood;
    private String codeCategory;
    private String nameFood;
    private int priceFood;
    private String photoFood;

    public FoodModel() {
    }

    public FoodModel(String idFood, String codeCategory, String nameFood,
                     int priceFood, String photoFood) {
        this.idFood = idFood;
        this.codeCategory = codeCategory;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.photoFood = photoFood;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public int getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(int priceFood) {
        this.priceFood = priceFood;
    }

    public String getPhotoFood() {
        return photoFood;
    }

    public void setPhotoFood(String photoFood) {
        this.photoFood = photoFood;
    }
}
