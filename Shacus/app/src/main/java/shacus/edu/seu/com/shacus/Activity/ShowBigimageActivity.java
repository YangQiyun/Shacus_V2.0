package shacus.edu.seu.com.shacus.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import shacus.edu.seu.com.shacus.Adapter.ImagePagerAdapter;
import shacus.edu.seu.com.shacus.Adapter.PhotoViewAttacher;
import shacus.edu.seu.com.shacus.Adapter.UploadViewPager;
import shacus.edu.seu.com.shacus.Network.OnSingleTapDismissBigPhotoListener;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Mind on 2017/9/18.
 */
public class ShowBigimageActivity extends BaseActivity implements OnSingleTapDismissBigPhotoListener{
    private List<String> imageBigDatas;
    private UploadViewPager image_viewpager;
    private ImagePagerAdapter imagePagerAdapter;
    private TextView position_in_total;
    public static final String LIST="IMAGELIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbigimage);
        Bundle data=this.getIntent().getExtras();
        imageBigDatas=data.getStringArrayList(LIST);
        image_viewpager=(UploadViewPager)findViewById(R.id.photoset_detail_viewpager);
        position_in_total=(TextView)findViewById(R.id.photoset_position_total);
        Log.d("BIgshow", "onCreate: "+imageBigDatas.size());
        imagePagerAdapter=new ImagePagerAdapter(this.getSupportFragmentManager(),imageBigDatas);
        image_viewpager.setAdapter(imagePagerAdapter);
        imagePagerAdapter.notifyDataSetChanged();
        image_viewpager.setOffscreenPageLimit(imagePagerAdapter.getCount());
        image_viewpager.setCurrentItem(0,true);
        position_in_total.setText((0 + 1) + "/" + imageBigDatas.size());
        image_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                position_in_total.setText((position + 1) + "/" + imageBigDatas.size());
            }



            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        imagePagerAdapter.notifyDataSetChanged();
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }


    @Override
    public void onDismissBigPhoto() {
        finish();
    }
}
