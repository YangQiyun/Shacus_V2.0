package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */

public class RemarkModel implements Serializable {

    private int RMcmtid;
    private int RMcmtquesid;
    private String RMcmtcontent;
    private String RMcmtT;
    private String RMcmtuimg;
    private String RMcmtuname;
    private int RMcmtuid;
    private int RMcmtvalid;

    public RemarkModel(){

    }
    public int getRMcmtid() {
        return RMcmtid;
    }

    public void setRMcmtid(int RMcmtid) {
        this.RMcmtid = RMcmtid;
    }

    public int getRMcmtquesid() {
        return RMcmtquesid;
    }

    public void setRMcmtquesid(int RMcmtquesid) {
        this.RMcmtquesid = RMcmtquesid;
    }

    public String getRMcmtT() {
        return RMcmtT;
    }

    public void setRMcmtT(String RMcmtT) {
        this.RMcmtT = RMcmtT;
    }

    public String getRMcmtcontent() {
        return RMcmtcontent;
    }

    public void setRMcmtcontent(String RMcmtcontent) {
        this.RMcmtcontent = RMcmtcontent;
    }

    public int getRMcmtuid() {
        return RMcmtuid;
    }

    public void setRMcmtuid(int RMcmtuid) {
        this.RMcmtuid = RMcmtuid;
    }

    public int getRMcmtvalid() {
        return RMcmtvalid;
    }

    public void setRMcmtvalid(int RMcmtvalid) {
        this.RMcmtvalid = RMcmtvalid;
    }

    public String getRMcmtuimg() {
        return RMcmtuimg;
    }

    public void setRMcmtuimg(String RMcmtuimg) {
        this.RMcmtuimg = RMcmtuimg;
    }

    public String getRMcmtuname() {
        return RMcmtuname;
    }

    public void setRMcmtuname(String RMcmtuname) {
        this.RMcmtuname = RMcmtuname;
    }
}
