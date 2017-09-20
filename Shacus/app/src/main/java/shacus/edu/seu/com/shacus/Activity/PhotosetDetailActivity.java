package shacus.edu.seu.com.shacus.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import shacus.edu.seu.com.shacus.R;

/**
 * Created by ljh on 2017/9/6.
 */

public class PhotosetDetailActivity extends BaseActivity {

    ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos_detail_1);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new BackButton());
    }
    class BackButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //返回上级界面
            finish();
        }
    }
}
