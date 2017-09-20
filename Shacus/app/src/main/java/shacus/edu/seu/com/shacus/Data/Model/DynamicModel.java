package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mind on 2017/9/13.
 */
public class DynamicModel implements Serializable {
    private int Tid;   //动态id
    private int Tsponsorid;   //发布者id
    private String TsponsT;  //创建时间
    private int TcommentN;  //评论数
    private int TlikeN;  //点赞数
    private String Tcontent;  //内容
    private String Ttitle;  //标题
    private String Tsponsorimg;  //用户头像url
    private List<String> TIimgurl; //动态图片url
    private String Tualais;  //发布者昵称
    private int Tislike;  //0没有关注 1关注

    public int getTislike() {
        return Tislike;
    }

    public void setTislike(int tislike) {
        Tislike = tislike;
    }

    public String getTsponsT() {
        return TsponsT;
    }

    public void setTsponsT(String tsponsT) {
        TsponsT = tsponsT;
    }

    public int getTid() {
        return Tid;
    }

    public void setTid(int tid) {
        Tid = tid;
    }

    public int getTsponsorid() {
        return Tsponsorid;
    }

    public void setTsponsorid(int tsponsorid) {
        Tsponsorid = tsponsorid;
    }

    public int getTcommentN() {
        return TcommentN;
    }

    public void setTcommentN(int tcommentN) {
        TcommentN = tcommentN;
    }

    public int getTlikeN() {
        return TlikeN;
    }

    public void setTlikeN(int tlikeN) {
        TlikeN = tlikeN;
    }

    public String getTcontent() {
        return Tcontent;
    }

    public void setTcontent(String tcontent) {
        Tcontent = tcontent;
    }

    public String getTtitle() {
        return Ttitle;
    }

    public void setTtitle(String ttitle) {
        Ttitle = ttitle;
    }

    public String getTsponsorimg() {
        return Tsponsorimg;
    }

    public void setTsponsorimg(String tsponsorimg) {
        Tsponsorimg = tsponsorimg;
    }

    public  List<String> getTIimgurl() {
        return TIimgurl;
    }

    public void setTIimgurl( List<String> TIimgurl) {
        this.TIimgurl = TIimgurl;
    }

    public String getTualais() {
        return Tualais;
    }

    public void setTualais(String tualais) {
        Tualais = tualais;
    }
}
