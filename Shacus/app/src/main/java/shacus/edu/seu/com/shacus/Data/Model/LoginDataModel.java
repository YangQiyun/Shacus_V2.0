package shacus.edu.seu.com.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;


public class LoginDataModel implements Serializable {

	private UserModel userModel;
	private List<NavigationModel> bannerList;
	private List<YuePaiGroupModel> groupList;
	//关注的好友动态列表
	private List<CollectionModel> CollectionList;
	private List<DynamicModel>  trendList;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<NavigationModel> getDaohanglan() {
		return bannerList;
	}

	public void setDaohanglan(List<NavigationModel> daohanglan){
		this.bannerList = daohanglan;
	}

	public List<DynamicModel> getTrendList() {
		return trendList;
	}

	public void setTrendList(List<DynamicModel> trendList) {
		this.trendList = trendList;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public List<YuePaiGroupModel> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<YuePaiGroupModel> groupList) {
		this.groupList = groupList;
	}

	public List<CollectionModel> getCollectionList() {
		return CollectionList;
	}

	public void setCollectionList(List<CollectionModel> collectionList) {
		CollectionList = collectionList;
	}
}