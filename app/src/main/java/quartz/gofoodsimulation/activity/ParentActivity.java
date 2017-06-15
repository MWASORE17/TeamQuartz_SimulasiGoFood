package quartz.gofoodsimulation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by sxio on 01-Apr-17.
 * Modified by: sxio
 * Modified Remark:
 * 02-Apr-17
 * add intent flag (clear top)
 * 04-Apr-17
 * Set the whole app to Portrait Orientation
 */

public class ParentActivity extends AppCompatActivity {
    public static void doChangeActivity(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void changeFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).addToBackStack(null).commit();
    }
}
