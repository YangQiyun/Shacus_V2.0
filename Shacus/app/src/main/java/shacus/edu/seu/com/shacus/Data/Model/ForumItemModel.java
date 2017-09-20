package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ForumItemModel implements Serializable {
    //帖子的id
    private int FCid;

    private int FCuserid;
    private String FCusername;
    private ImageData FCuserheadphoto;
    private String FCtitle;
    private String FCcontent;
    private int FCpicnum;
    private List<ImageData> FCpictures;
    private String FCremarktime;
    private int FCremarknum;
    private int FCfavornum;
    private int FCiscollect;
    public ForumItemModel() {
    }


    public String getFCtitle() {
        return FCtitle;
    }

    public void setFCtitle(String FCtitle) {
        this.FCtitle = FCtitle;
    }

    public String getFCcontent() {
        return FCcontent;
    }

    public void setFCcontent(String FCcontent) {
        this.FCcontent = FCcontent;
    }

    public int getFCpicnum() {
        return FCpicnum;
    }

    public void setFCpicnum(int FCpicnum) {
        this.FCpicnum = FCpicnum;
    }

    public List<ImageData> getFCpictures() {
        return FCpictures;
    }

    public void setFCpictures(List<ImageData> FCpictures) {
        this.FCpictures = FCpictures;
    }

    public String getFCusername() {
        return FCusername;
    }

    public void setFCusername(String FCusername) {
        this.FCusername = FCusername;
    }

    public int getFCuserid() {
        return FCuserid;
    }

    public void setFCuserid(int FCuserid) {
        this.FCuserid = FCuserid;
    }

    public String getFCremarktime() {
        return FCremarktime;
    }

    public void setFCremarktime(String FCremarktime) {
        this.FCremarktime = FCremarktime;
    }

    public int getFCremarknum() {
        return FCremarknum;
    }

    public void setFCremarknum(int FCremarknum) {
        this.FCremarknum = FCremarknum;
    }

    public int getFCfavornum() {
        return FCfavornum;
    }

    public void setFCfavornum(int FCfavornum) {
        this.FCfavornum = FCfavornum;
    }

    public int getFCid() {
        return FCid;
    }

    public void setFCid(int FCid) {
        this.FCid = FCid;
    }

    public ImageData getFCuserheadphoto() {
        return FCuserheadphoto;
    }

    public void setFCuserheadphoto(ImageData FCuserheadphoto) {
        this.FCuserheadphoto = FCuserheadphoto;
    }

    public int getFCiscollect() {
        return FCiscollect;
    }

    public void setFCiscollect(int FCiscollect) {
        this.FCiscollect = FCiscollect;
    }
}
