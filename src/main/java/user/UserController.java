package user;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import board.BoardVO;

@Controller
public class UserController {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserVO userVO;
	
	@RequestMapping("login")
	public String login(@RequestParam("id") String id,
	                    @RequestParam("password") String password,
	                    Model model, HttpSession session) {

		UserVO user = userDAO.getUser(id, password);
		
	    if (user != null) {
	    	session.setAttribute("login", user);
	        return "/main/index";
	    } else {
	        model.addAttribute("loginFailed", true);
	        return "redirect:login";
	    }
	}
	
	@RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "/main/index";
    }
	
	@RequestMapping("getUserList")
    public String getUserList(Model model) {
		
		model.addAttribute("userList", userDAO.getUserList());
		
        return "/user/getUserList";
    }
	
	@RequestMapping("getUserNo")
	public String getUserNo(HttpSession session, Model model) {
		UserVO login = (UserVO) session.getAttribute("login");
		String userno = Integer.toString(login.getUserno());
		
	    UserVO user = userDAO.getUserNo(userno);
	    model.addAttribute("user", user);

	    return "/user/getUserNo";
	}
	
	@RequestMapping("getUserId")
	public String getUserId(@RequestParam("userid") String userid, Model model) {
	    model.addAttribute("user", userDAO.getUserId(userid));

	    return "/user/getUserNo";
	}
	
	@RequestMapping("idCheck")
	public String idCheck(@RequestParam String userID, Model model) {
	    boolean check = userDAO.checkId(userID);
	    model.addAttribute("userID", userID);
	    model.addAttribute("check", check);
	    
	    return "/user/idCheck";
	}
	
	@RequestMapping("addUser")
	public String addUser(UserVO vo, HttpSession session) {
		userDAO.addUser(vo);

//		session.setAttribute("login", vo);
		
		return "/main/index";
	}
	
	@RequestMapping("updateUser")
	public String updateUser(UserVO vo, Model model) {
		userDAO.updateUser(vo);

		UserVO user = userDAO.getUserNo(Integer.toString(vo.getUserno()));
	    model.addAttribute("user", user);
		
		return "/user/getUserNo";
	}
	
	@RequestMapping("deleteUser")
    public String deleteUser(String userno, HttpSession session) {
		userDAO.deleteUser(userno);
        session.invalidate();
        return "/main/index";
    }
	
	@PostMapping("searchUser")
	public String searchUser(@RequestParam String searchBy, @RequestParam String search, Model model) {
		List<UserVO> userList;
		
		if ("name".equals(searchBy)) {
			userList = userDAO.searchUserName(search);
		} else if ("phone".equals(searchBy)) {
			userList = userDAO.searchUserPhone(search);
		} else {
			throw new IllegalArgumentException("Invalid search criteria: " + searchBy);
		}

	    model.addAttribute("userList", userList);

	    return "/user/getUserList";
	}
	
}
