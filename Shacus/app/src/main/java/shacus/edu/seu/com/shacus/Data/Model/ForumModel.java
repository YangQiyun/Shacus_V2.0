package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static shacus.edu.seu.com.shacus.Utils.CommonUrl.url;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ForumModel implements Serializable {

    private int CQuesid;
    private int CQuid;
    private String CQtime;
    private int CQcommentN;
    private int CQlikedN;
    private String CQcontent;
    private String CQtitle;
    private String CQuimurl;
    private String CQuname;
    private List<String> CQimg = new ArrayList<String>();
    private int CQuiscollect;
    private int CQuisfavor;

    public ForumModel(){

    }

    public int getCQuesid() {
        return CQuesid;
    }

    public void setCQuesid(int CQuesid) {
        this.CQuesid = CQuesid;
    }

    public int getCQuid() {
        return CQuid;
    }

    public void setCQuid(int CQuid) {
        this.CQuid = CQuid;
    }

    public String getCQtime() {
        return CQtime;
    }

    public void setCQtime(String CQtime) {
        this.CQtime = CQtime;
    }

    public int getCQcommentN() {
        return CQcommentN;
    }

    public void setCQcommentN(int CQcommentN) {
        this.CQcommentN = CQcommentN;
    }

    public int getCQlikedN() {
        return CQlikedN;
    }

    public void setCQlikedN(int CQlikedN) {
        this.CQlikedN = CQlikedN;
    }

    public String getCQcontent() {
        return CQcontent;
    }

    public void setCQcontent(String CQcontent) {
        this.CQcontent = CQcontent;
    }

    public String getCQtitle() {
        return CQtitle;
    }

    public void setCQtitle(String CQtitle) {
        this.CQtitle = CQtitle;
    }

    public String getCQuname() {
        return CQuname;
    }

    public void setCQuname(String CQuname) {
        this.CQuname = CQuname;
    }

    public List<String> getCQimg() {
        return CQimg;
    }

    public void setCQimg(List<String> CQimg) {
        this.CQimg = CQimg;
    }

    public int getCQuiscollect() {
        return CQuiscollect;
    }

    public void setCQuiscollect(int CQuiscollect) {
        this.CQuiscollect = CQuiscollect;
    }

    public String getCQuimurl() {
        return CQuimurl;
    }

    public void setCQuimurl(String CQuimurl) {
        this.CQuimurl = CQuimurl;
    }

    public int getCQuisfavor() {
        return CQuisfavor;
    }

    public void setCQuisfavor(int CQuisfavor) {
        this.CQuisfavor = CQuisfavor;
    }
}
