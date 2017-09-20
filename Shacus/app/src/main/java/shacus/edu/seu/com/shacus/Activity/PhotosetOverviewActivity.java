package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Adapter.PhotosetOverviewAdapter;
import shacus.edu.seu.com.shacus.Data.Manager.PhotosetManager;
import shacus.edu.seu.com.shacus.Data.Model.Images;
import shacus.edu.seu.com.shacus.Data.Model.MyRecord;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by ljh on 2017/9/15.
 */

public class PhotosetOverviewActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    ImageButton backbtn;

    List<MyRecord> exampleRecords;
    private ArrayList<Integer> ZPJ_ID;
    private ArrayList<String> ZPJ_TITLE;
    private ArrayList<String> ZPJ_URL;
    ListView photosetListview;
    PhotosetManager DataManager ;
    private Handler myHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatusCode.REQUEST_PHOTOSET_DETAIL_FAIL:
                    Log.d("CCCCCCCCCCCCC", "失败");
                    break;
                case StatusCode.REQUEST_PHOTOSET_DETAIL_SUCCESS:
                    freshUrl();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoset_overview);
        photosetListview = (ListView) findViewById(R.id.photoset_listview);
        initData();
        photosetListview.setAdapter(new PhotosetOverviewAdapter(this,exampleRecords));
        photosetListview.setOnItemClickListener(this);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new BackButton());
    }
     private void initData() {
         this.ZPJ_ID = this.getIntent().getExtras().getIntegerArrayList("phtotset_cover_uid");
         this.ZPJ_URL = this.getIntent().getExtras().getStringArrayList("photoset_cover_url");
         this.ZPJ_TITLE = this.getIntent().getExtras().getStringArrayList("phtotset_cover_title");
         //Add some example records
         exampleRecords = new ArrayList<MyRecord>();
         for(int i=0;i<ZPJ_ID.size();i++) {
             exampleRecords.add(new MyRecord(ZPJ_ID.get(i), ZPJ_TITLE.get(i), ZPJ_URL.get(i)));
         }
     }
     private void freshUrl(){
         ZPJ_URL= DataManager.getZPJ_DETAIL_URL();
         Images.imageUrls = (String[]) ZPJ_URL.toArray(new String[0]);
         Intent intent=new Intent(this,PhotosetDetailActivity.class);
         // intent.putExtra("uid",otherId);
         startActivity(intent);
     }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //把对应的uid传进去
        int ucid=ZPJ_ID.get(position);
         DataManager=new PhotosetManager(this,myHandler,ucid);
        //manager 从acache中获得usermodel，获取id，authkey
        DataManager.initPhotosetInfo();
        //manager加载作品集中的照片
        //manager中更改images
        //进入作品集展示界面
    }
    class BackButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //返回上级界面
            finish();
        }
    }
}






