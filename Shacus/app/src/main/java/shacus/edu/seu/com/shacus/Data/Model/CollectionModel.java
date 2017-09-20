package shacus.edu.seu.com.shacus.Data.Model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mind on 2017/9/16.
 */
public class CollectionModel implements Serializable, MultiItemEntity {
    private int UCid; //作品集ID
    private int UCuid;//作品集发布者id
    private String UCcreateT; //发布时间
    private String UCtitle; //作品集标题
    private int UCvalid; //作品集是否有效
    private int UClikeN; //作品集获赞数
    private String UCuimurl;//作品集发布者头像
    private ArrayList<String> UCIurl;//作品集图片url
    private String UCualais; //作品集发布者昵称
    public static final int TYPE=0; //布局样式，目前只有一种

    public int getUCid() {
        return UCid;
    }

    public void setUCid(int UCid) {
        this.UCid = UCid;
    }

    public int getUCuid() {
        return UCuid;
    }

    public void setUCuid(int UCuid) {
        this.UCuid = UCuid;
    }

    public String getUCcreateT() {
        return UCcreateT;
    }

    public void setUCcreateT(String UCcreateT) {
        this.UCcreateT = UCcreateT;
    }

    public String getUCtitile() {
        return UCtitle;
    }

    public void setUCtitile(String UCtitile) {
        this.UCtitle = UCtitile;
    }

    public int getUCvalid() {
        return UCvalid;
    }

    public void setUCvalid(int UCvalid) {
        this.UCvalid = UCvalid;
    }

    public int getUClikeN() {
        return UClikeN;
    }

    public void setUClikeN(int UClikeN) {
        this.UClikeN = UClikeN;
    }

    public String getUCuimurl() {
        return UCuimurl;
    }

    public void setUCuimurl(String UCuimurl) {
        this.UCuimurl = UCuimurl;
    }

    public ArrayList<String> getUClurl() {
        return UCIurl;
    }

    public void setUClurl(ArrayList<String> UClurl) {
        this.UCIurl = UClurl;
    }

    public String getUCualais() {
        return UCualais;
    }

    public void setUCualais(String UCualais) {
        this.UCualais = UCualais;
    }


    //目前默认只使用一种样式
    @Override
    public int getItemType() {
        return 0;
    }
}
