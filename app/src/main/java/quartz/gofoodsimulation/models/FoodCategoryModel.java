package quartz.gofoodsimulation.models;

/**
 * Created by meikelwis on 4/1/17.
 */

public class FoodCategoryModel {
    private String codeCategory;
    private int idSeller;
    private String nameCategory;

    public FoodCategoryModel() {

    }

    public FoodCategoryModel(String codeCategory, int idSeller, String nameCategory) {
        this.codeCategory = codeCategory;
        this.idSeller = idSeller;
        this.nameCategory = nameCategory;
    }

    public String getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public int getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(int idSeller) {
        this.idSeller = idSeller;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
