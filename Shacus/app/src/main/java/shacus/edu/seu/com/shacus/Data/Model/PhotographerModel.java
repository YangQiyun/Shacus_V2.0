package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;

//数据模型：摄影师发布的约拍,展示再摄影师招募会中
public class PhotographerModel implements Serializable {

    private String Userimg;  //头像
  //  private String APtitle;  //不存在
    private int APid;        //约拍单子的id
//    private String APimgurl;
    private String APcreatetime; //创建时间
    private int APlikeN;  //不存在
    private String APregistN;  //感兴趣人数
 //   private int Userliked;
    private String APtime;  //约拍时间要求
    private UserModel userModel;  //发起人的信息
    private List<String> APimgurl;
    private String APcontent; //约拍内容描述
    private int APpricetype; //约拍费用说明类型
    private String APprice;   //约拍费用
    private int APgroup;   //约拍tab类型
    private int APstatus;  //约拍状态 0 报名中 1进行中 2已完成

    public int getAPstatus() {
        return APstatus;
    }

    public void setAPstatus(int APstatus) {
        this.APstatus = APstatus;
    }

    public String getAPcreatetime() {
        return APcreatetime;
    }

    public void setAPcreatetime(String APcreatetime) {
        this.APcreatetime = APcreatetime;
    }

    public int getAPid() {
        return APid;
    }

    public void setAPid(int APid) {
        this.APid = APid;
    }

    public int getAPlikeN() {
        return APlikeN;
    }

    public void setAPlikeN(int APlikeN) {
        this.APlikeN = APlikeN;
    }

    public String getAPregistN() {
        return APregistN;
    }

    public void setAPregistN(String APregistN) {
        this.APregistN = APregistN;
    }

    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }



    public String getAPtime() {
        return APtime;
    }

    public void setAPtime(String APtime) {
        this.APtime = APtime;
    }


    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<String> getAPimgurl() {
        return APimgurl;
    }

    public void setAPimgurl(List<String> APimgurl) {
        this.APimgurl = APimgurl;
    }

    public String getAPcontent() {
        return APcontent;
    }

    public void setAPcontent(String APcontent) {
        this.APcontent = APcontent;
    }

    public int getAPpricetype() {
        return APpricetype;
    }

    public void setAPpricetype(int APpricetype) {
        this.APpricetype = APpricetype;
    }

    public String getAPprice() {
        return APprice;
    }

    public void setAPprice(String APprice) {
        this.APprice = APprice;
    }

    public int getAPgroup() {
        return APgroup;
    }

    public void setAPgroup(int APgroup) {
        this.APgroup = APgroup;
    }
}
