package quartz.gofoodsimulation.models;

/**
 * Created by meikelwis on 4/1/17.
 */

public class FoodGroupModel {
    private long id;
    private String codeGroup;
    private String nameGroup;

    public FoodGroupModel() {

    }

    public FoodGroupModel(long id) {
        this.id = id;
    }

    public FoodGroupModel(String codeGroup, String nameGroup) {
        this.codeGroup = codeGroup;
        this.codeGroup = codeGroup;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeGroup() {
        return codeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        this.codeGroup = codeGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }
}
