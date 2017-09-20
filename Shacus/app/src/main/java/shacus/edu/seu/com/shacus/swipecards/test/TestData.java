package shacus.edu.seu.com.shacus.swipecards.test;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import shacus.edu.seu.com.shacus.swipecards.util.BaseModel;
import shacus.edu.seu.com.shacus.swipecards.util.CardEntity;

public class TestData {

    public static ArrayList<CardEntity> getApiData(Context context) {
        BaseModel<ArrayList<CardEntity>> model = null;
        try {
            model = new GsonBuilder().create().fromJson(
                    new InputStreamReader(context.getAssets().open("test.json")),
                    new TypeToken<BaseModel<ArrayList<CardEntity>>>() {}.getType()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model != null ? model.results : null;
    }

}
