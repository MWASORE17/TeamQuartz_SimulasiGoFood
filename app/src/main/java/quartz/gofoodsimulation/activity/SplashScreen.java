package quartz.gofoodsimulation.activity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.data.DataBaseHelper;
import quartz.gofoodsimulation.data.DriverData;
import quartz.gofoodsimulation.data.FoodCategoryData;
import quartz.gofoodsimulation.data.FoodData;
import quartz.gofoodsimulation.data.FoodGroupData;
import quartz.gofoodsimulation.data.MasterSequenceData;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.models.UserModel;

/**
 * Created by sxio on 01-Apr-17.
 */

public class SplashScreen extends ParentActivity {
    DataBaseHelper dbManager;
    MasterSequenceData mstseq;
    UserData user;
    SellerData sellerData;
    FoodData fd;
    FoodCategoryData fcd;
    FoodGroupData fgd;
    DriverData driverData;
    ArrayList<SellerModel> sellers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        initiateDatabase();

        Thread splash_thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    doChangeActivity(SplashScreen.this, MainActivity.class);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash_thread.start();
    }

    private void initiateDatabase() {
        dbManager = new DataBaseHelper(SplashScreen.this, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
        dbManager.deleteTable();

        user = new UserData(this);
        user.createUser(new UserModel("q@gmail.com", "quartz", "08123456789", "password88"));

        sellerData = new SellerData(this);
        sellers = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 5, 4);
        sellers.add(new SellerModel(
                "Martabak Yurich",
                "08123456789",
                "Jl. Putri Merak Jingga, Medan Barat, Medan",
                "seller_1.jpg",
                "martabak",
                100,
                "00.00 - 23.59",
                calendar.getTime(),
                3.598378,
                98.676359
        ));//1
        calendar.set(2016, 5, 4);
        sellers.add(new SellerModel(
                "Martabak Cetar, D'Cruise",
                "08123456798",
                "Gedung D'Cruise, Jl. Yos Sudarso No.88, Medan Barat, Medan",
                "seller_2.jpg",
                "martabak",
                2,
                "17.30 - 21.30",
                calendar.getTime(),
                3.615020,
                98.672540
        ));//2
        calendar.set(2017, 4, 4);
        sellers.add(new SellerModel(
                "Nasi Prang 1881 Khas Tanjung Balai",
                "0811646296",
                "Jl. Madong Lubis No.16/28 KL. V, Medan Perjuangan, Medan",
                "seller_3.jpg",
                "nasi",
                103,
                "07.00 - 16.00",
                calendar.getTime(),
                3.595464,
                98.693715
        ));//3
        calendar.set(2017, 2, 4);
        sellers.add(new SellerModel(
                "Es Campur Amo",
                "08143245679",
                "Jl. Sei Kera No. 117 i, Medan Perjuangan, Medan",
                "seller_4.jpg",
                "minuman",
                104,
                "00.00 - 23.59",
                calendar.getTime(),
                3.595524,
                98.690223
        ));//4
        calendar.set(2015, 5, 4);
        sellers.add(new SellerModel(
                "V Bread Pizza (Vegan/Vegetarian)",
                "08143245679",
                "Komplek Cemara Asri",
                "seller_5.jpg",
                "healthy",
                0,
                "07.00 - 17.00",
                calendar.getTime(),
                3.636746,
                98.702119
        ));//5
        calendar.set(2012, 6, 5);
        sellers.add(new SellerModel(
                "Nasi Ayam Mama Koki",
                "08143245679",
                "Jl. Singa No. 158/22B Simpang Jl.Gajah, Medan Kota",
                "seller_2.jpg",
                "ayambebek",
                0,
                "10.31 - 21.30",
                calendar.getTime(),
                3.590549,
                98.696184
        ));//6
        calendar.set(2014, 9, 23);
        sellers.add(new SellerModel(
                "Texas Chicken, Thamrin Plaza",
                "08143245679",
                "Thamrin Plaza, Lantai 1, Blok 2, Jl. MH Thamrin No 75, Medan Area, Medan",
                "seller_3.jpg",
                "fastfood",
                0,
                "07.00 - 17.00",
                calendar.getTime(),
                3.586550,
                98.692504
        ));//7
        calendar.set(2016, 11, 29);
        sellers.add(new SellerModel(
                "Sushi Tei, Center Point Mall",
                "08143245679",
                "Centre Point Mall, Level 2 #07-08, Jl.Jawa, Medan Timur, Medan",
                "seller_4.jpg",
                "japanese",
                0,
                "11.00 - 22.00",
                calendar.getTime(),
                3.592061,
                98.680621
        ));//8
        calendar.set(2010, 8, 30);
        sellers.add(new SellerModel(
                "Kimbab Nara",
                "08143245679",
                "Jl. Brigjend Katamso Dalam No. 56 E, Medan Maimun, Medan",
                "seller_5.jpg",
                "korean",
                0,
                "09.00 - 18.45",
                calendar.getTime(),
                3.578583,
                98.683719
        ));//9
        calendar.set(2017, 1, 1);
        sellers.add(new SellerModel(
                "Bihun Daging Ikan Aguan",
                "08143245679",
                "Komplek Asia Mega Mas, Jl. Asia, Medan Area",
                "seller_1.jpg",
                "chinese",
                0,
                "15.30 - 22.00",
                calendar.getTime(),
                3.587009,
                98.703494
        ));//10
        sellerData.createSeller(sellers);

        fcd = new FoodCategoryData(this);
        /* Seller1 */
        fcd.createFoodCategory("martabak1", 1, "Martabak");
        fcd.createFoodCategory("dummy1", 1, "Dummy");
        fcd.createFoodCategory("minuman1", 1, "Minuman");
        /* Seller2 */
        fcd.createFoodCategory("dummy2", 2, "Dummy");
        fcd.createFoodCategory("minuman2", 2, "Minuman");
        /* Seller3 */
        fcd.createFoodCategory("menu3", 3, "Makanan");
        /* Seller4 */
        fcd.createFoodCategory("menu4", 4, "Menu");
        /*Seller5*/
        fcd.createFoodCategory("PizzaVegan5", 5, "Pizza Vegan");
        fcd.createFoodCategory("MinumanOriginal5", 5, "Minuman Original");
        /*Seller6*/
        fcd.createFoodCategory("recommended6", 6, "Recommended");
        fcd.createFoodCategory("minuman6", 6, "Minuman");
        /*Seller7*/
        fcd.createFoodCategory("menu7", 7, "Menu");
        /*Seller8*/
        fcd.createFoodCategory("menu8", 8, "Menu");
        /*Seller9*/
        fcd.createFoodCategory("menu9", 9, "Menu");
        /*Seller10*/
        fcd.createFoodCategory("menu10", 10, "Menu");

        fd = new FoodData(this);
        //1
        fd.createFood("martabak_BCBB", "martabak1", "Black Ceres Blue Band", 45000, "martabakYurich_1_1.jpg");
        fd.createFood("martabak_PKBB", "martabak1", "Pandan Keju Blue Band", 50000, "martabakYurich_1_2.jpg");
        fd.createFood("dummy_1_1", "dummy1", "Dummy_1_1", 100000, "martabakYurich_2_1.jpg");
        fd.createFood("dummy_1_2", "dummy1", "Dummy_1_2", 100000, "martabakYurich_2_2.jpg");
        fd.createFood("dummy_1_3", "dummy1", "Dummy_1_3", 100000, "");
        fd.createFood("dummy_1_4", "dummy1", "Dummy_1_4", 100000, "");
        fd.createFood("dummy_1_5", "dummy1", "Dummy_1_5", 100000, "");
        fd.createFood("dummy_1_6", "dummy1", "Dummy_1_6", 100000, "");
        fd.createFood("dummy_1_7", "dummy1", "Dummy_1_7", 100000, "");
        fd.createFood("dummy_1_8", "dummy1", "Dummy_1_8", 100000, "");
        fd.createFood("dummy_1_9", "dummy1", "Dummy_1_9", 100000, "");
        fd.createFood("dummy_1_10", "dummy1", "Dummy_1_10", 100000, "");
        fd.createFood("minuman_soyaBean", "minuman1", "Soya Bean", 5000, "martabakYurich_3_1.jpg");
        fd.createFood("minuman_premiumCoffee", "minuman1", "Premium Coffee", 10000, "martabakYurich_3_2.jpg");
        fd.createFood("minuman_airMineralBotol", "minuman1", "Air Mineral Botol", 5000, "martabakYurich_3_3.jpg");
        fd.createFood("minuman_tehBunga", "minuman1", "Teh Bunga", 5000, "martabakYurich_3_4.jpg");
        //2
        fd.createFood("dummy_2_1", "dummy2", "Dummy_2_1", 50000, "");
        fd.createFood("dummy_2_2", "dummy2", "Dummy_2_2", 50000, "");
        fd.createFood("dummy_2_3", "dummy2", "Dummy_2_3", 50000, "");
        fd.createFood("dummy_2_4", "dummy2", "Dummy_2_4", 50000, "");
        fd.createFood("dummy_2_5", "dummy2", "Dummy_2_5", 50000, "");
        fd.createFood("dummy_2_6", "dummy2", "Dummy_2_6", 50000, "");
        fd.createFood("dummy_2_7", "dummy2", "Dummy_2_7", 50000, "");
        fd.createFood("dummy_2_8", "dummy2", "Dummy_2_8", 50000, "");
        fd.createFood("dummy_2_9", "dummy2", "Dummy_2_9", 50000, "");
        fd.createFood("dummy_2_10", "dummy2", "Dummy_2_10", 50000, "");
        fd.createFood("minuman_tehBunga2", "minuman2", "Teh Bunga", 6000, "");
        //3
        fd.createFood("menu_3_1", "menu3", "menu_3_1", 15000, "");
        fd.createFood("menu_3_2", "menu3", "menu_3_2", 15000, "");
        fd.createFood("menu_3_3", "menu3", "menu_3_3", 15000, "");
        fd.createFood("menu_3_4", "menu3", "menu_3_4", 15000, "");
        fd.createFood("menu_3_5", "menu3", "menu_3_5", 15000, "");
        fd.createFood("menu_3_6", "menu3", "menu_3_6", 15000, "");
        fd.createFood("menu_3_7", "menu3", "menu_3_7", 15000, "");
        fd.createFood("menu_3_8", "menu3", "menu_3_8", 15000, "");
        fd.createFood("menu_3_9", "menu3", "menu_3_9", 15000, "");
        fd.createFood("menu_3_10", "menu3", "menu_3_10", 15000, "");
        //4
        fd.createFood("menu_4_1", "menu4", "menu_4_1", 7000, "amo_1_1.jpg");
        fd.createFood("menu_4_2", "menu4", "menu_4_2", 7000, "amo_1_2.jpg");
        fd.createFood("menu_4_3", "menu4", "menu_4_3", 7000, "");
        fd.createFood("menu_4_4", "menu4", "menu_4_4", 7000, "");
        fd.createFood("menu_4_5", "menu4", "menu_4_5", 7000, "");
        fd.createFood("menu_4_6", "menu4", "menu_4_6", 7000, "");
        fd.createFood("menu_4_7", "menu4", "menu_4_7", 7000, "");
        fd.createFood("menu_4_8", "menu4", "menu_4_8", 7000, "");
        fd.createFood("menu_4_9", "menu4", "menu_4_9", 7000, "");
        fd.createFood("menu_4_10", "menu4", "menu_4_10", 7000, "");
        //5
        fd.createFood("Pizza_SLH", "PizzaVegan5", "Pizza Sosis Lada Hitam", 55000, "");
        fd.createFood("Pizza_DJ", "PizzaVegan5", "Pizza Dendeng Jagung", 50000, "");
        fd.createFood("Pizza_5_3", "PizzaVegan5", "Pizza 3", 50000, "");
        fd.createFood("Pizza_5_4", "PizzaVegan5", "Pizza 4", 60000, "");
        fd.createFood("Pizza_5_5", "PizzaVegan5", "Pizza 5", 65000, "");
        fd.createFood("Pizza_5_6", "PizzaVegan5", "Pizza 6", 50000, "");
        fd.createFood("Pizza_5_7", "PizzaVegan5", "Pizza 7", 55000, "");
        fd.createFood("Pizza_5_8", "PizzaVegan5", "Pizza 8", 55000, "");
        fd.createFood("Pizza_5_9", "PizzaVegan5", "Pizza 9", 70000, "");
        fd.createFood("Pizza_5_10", "PizzaVegan5", "Pizza 10", 75000, "");

        fd.createFood("Pizza_drink_1", "MinumanOriginal5", "Soya Bean", 15000, "martabakYurich_3_1.jpg");
        fd.createFood("Pizza_drink_2", "MinumanOriginal5", "Cincau Hitam", 10000, "");
        fd.createFood("Pizza_drink_3", "MinumanOriginal5", "Winter Melon", 15000, "");
        fd.createFood("Pizza_drink_4", "MinumanOriginal5", "Markisa", 15000, "");
        //6
        fd.createFood("koki_recommended1", "recommended6", "Nasi Ayam Komplit", 33000, "");
        fd.createFood("koki_recommended2", "recommended6", "Peh Cam Ke", 165000, "");
        fd.createFood("koki_recommended3", "recommended6", "Ayam Goreng", 110000, "");

        fd.createFood("koki_drink_1", "minuman6", "Soya Bean", 15000, "martabakYurich_3_1.jpg");
        fd.createFood("koki_drink_2", "minuman6", "Cincau Hitam", 10000, "");
        fd.createFood("koki_drink_3", "minuman6", "Winter Melon", 15000, "");
        fd.createFood("koki_drink_4", "minuman6", "Markisa", 15000, "");
        //7
        fd.createFood("menu_7_1", "menu7", "menu_7_1", 70000, "");
        fd.createFood("menu_7_2", "menu7", "menu_7_2", 70000, "");
        fd.createFood("menu_7_3", "menu7", "menu_7_3", 70000, "");
        fd.createFood("menu_7_4", "menu7", "menu_7_4", 70000, "");
        fd.createFood("menu_7_5", "menu7", "menu_7_5", 70000, "");
        fd.createFood("menu_7_6", "menu7", "menu_7_6", 70000, "");
        fd.createFood("menu_7_7", "menu7", "menu_7_7", 70000, "");
        fd.createFood("menu_7_8", "menu7", "menu_7_8", 70000, "");
        fd.createFood("menu_7_9", "menu7", "menu_7_9", 70000, "");
        fd.createFood("menu_7_10", "menu7", "menu_7_10", 70000, "");
        //8
        fd.createFood("menu_8_1", "menu8", "menu_8_1", 25000, "");
        fd.createFood("menu_8_2", "menu8", "menu_8_2", 25000, "");
        fd.createFood("menu_8_3", "menu8", "menu_8_3", 25000, "");
        fd.createFood("menu_8_4", "menu8", "menu_8_4", 25000, "");
        fd.createFood("menu_8_5", "menu8", "menu_8_5", 25000, "");
        fd.createFood("menu_8_6", "menu8", "menu_8_6", 25000, "");
        fd.createFood("menu_8_7", "menu8", "menu_8_7", 25000, "");
        fd.createFood("menu_8_8", "menu8", "menu_8_8", 25000, "");
        fd.createFood("menu_8_9", "menu8", "menu_8_9", 25000, "");
        fd.createFood("menu_8_10", "menu8", "menu_8_10", 25000, "");
        //9
        fd.createFood("menu_9_1", "menu9", "menu_9_1", 15000, "");
        fd.createFood("menu_9_2", "menu9", "menu_9_2", 25000, "");
        fd.createFood("menu_9_3", "menu9", "menu_9_3", 35000, "");
        fd.createFood("menu_9_4", "menu9", "menu_9_4", 45000, "");
        fd.createFood("menu_9_5", "menu9", "menu_9_5", 55000, "");
        fd.createFood("menu_9_6", "menu9", "menu_9_6", 65000, "");
        fd.createFood("menu_9_7", "menu9", "menu_9_7", 75000, "");
        fd.createFood("menu_9_8", "menu9", "menu_9_8", 85000, "");
        fd.createFood("menu_9_9", "menu9", "menu_9_9", 95000, "");
        fd.createFood("menu_9_10", "menu9", "menu_9_10", 105000, "");
        //10
        fd.createFood("menu_10_1", "menu10", "menu_10_1", 21000, "");
        fd.createFood("menu_10_2", "menu10", "menu_10_2", 22000, "");
        fd.createFood("menu_10_3", "menu10", "menu_10_3", 23000, "");
        fd.createFood("menu_10_4", "menu10", "menu_10_4", 24000, "");
        fd.createFood("menu_10_5", "menu10", "menu_10_5", 25000, "");
        fd.createFood("menu_10_6", "menu10", "menu_10_6", 26000, "");
        fd.createFood("menu_10_7", "menu10", "menu_10_7", 27000, "");
        fd.createFood("menu_10_8", "menu10", "menu_10_8", 28000, "");
        fd.createFood("menu_10_9", "menu10", "menu_10_9", 29000, "");
        fd.createFood("menu_10_10", "menu10", "menu_10_10", 23000, "");


        fgd = new FoodGroupData(this);
        fgd.createFoodGroup("healthy", "Healthy");
        fgd.createFoodGroup("nasi", "Nasi");
        fgd.createFoodGroup("ayambebek", "Ayam & Bebek");
        fgd.createFoodGroup("snack", "Snack");
        fgd.createFoodGroup("minuman", "Minuman");
        fgd.createFoodGroup("fastfood", "Fast Food");
        fgd.createFoodGroup("japanese", "Japanese");
        fgd.createFoodGroup("korean", "Korean");
        fgd.createFoodGroup("roti", "Roti");
        fgd.createFoodGroup("seafood", "Seafood");
        fgd.createFoodGroup("chinese", "Chinese");
        fgd.createFoodGroup("martabak", "Martabak");

        mstseq = new MasterSequenceData(this);
        mstseq.addSequence("TRHTRANSAKSI", "Sequence TRHTRANSAKSI"); //id = 1

        driverData = new DriverData(this);
        driverData.createDriver("Mr. X", "081234567890", 3.598651, 98.694406);
        driverData.createDriver("Mr. Y", "083199773608", 3.599123, 98.687820);
        driverData.createDriver("Mr. Z", "087799885566", 3.587461, 98.690708);
    }

    @Override
    public void onBackPressed() {
        //need to Override empty onBackPressed event to block SplashScreen back
        return;
    }
}
