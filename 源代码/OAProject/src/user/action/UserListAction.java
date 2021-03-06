package user.action;

import java.util.List;

import pojo.TDatadic;
import pojo.TUser;
import user.dao.PageModel;
import user.service.UserService;

import com.opensymphony.xwork2.ModelDriven;


public class UserListAction extends BaseAction implements ModelDriven<TUser>{
	private TUser user = new TUser();
	private List<TDatadic> datalist;
	@Override
	public TUser getModel() {
		return user;
	}
	private UserService userService;
	/**
	 * 遍历用户列表
	 */
	public String view() {
		datalist = userService.query();
		result = userService.search(user, currentPage, pageSize);
		return SUCCESS;
	}
	public List<TDatadic> getDatalist() {
		return datalist;
	}
	public void setDatalist(List<TDatadic> datalist) {
		this.datalist = datalist;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	
}
