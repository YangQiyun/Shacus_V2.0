package shacus.edu.seu.com.shacus.Data.Manager;

import android.os.Handler;

import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;

/**
 * Created by Mind on 2017/9/19.
 */
public class YuepaiInfoManager {
    private PhotographerModel photographerModel;
    private Handler handler;

    public YuepaiInfoManager(PhotographerModel photographerModel,Handler handler){
        this.photographerModel=photographerModel;
        this.handler=handler;
    }
}
