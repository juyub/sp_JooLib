package page;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
	
	@RequestMapping("index")
	public String index() {
		return "main/index";
	}
	
	@RequestMapping("loginPage")
	public String loginPage() {
		return "user/login";				
	} 
	
	@RequestMapping("addUserPage")
	public String addUserPage() {
		return "user/addUser";				
	} 
	
	@RequestMapping("addBoardPage")
	public String addBoardPage() {
		return "board/addBoard";
	}
	
	@RequestMapping("replyPage")
	public String replyPage(String boardNO, Model model) {
	    model.addAttribute("boardNO", boardNO);
	    return "board/replyBoard";
	}
	
	@RequestMapping("naverBooksPage")
	public String naverBooksPage() {
		return "book/naverBooks";
	}
	
	@RequestMapping("addBookPage")
	public String addBookPage(@RequestParam Map<String,String> params) {
		return "book/addBook";
	}
	
	@RequestMapping("movieMain")
	public String movieMain() {
		return "movie/movieMain";
	}
	
	@RequestMapping("movieDetail")
	public String movieDetail(@RequestParam("id") String id, Model model) {
	    model.addAttribute("id", id);
	    return "movie/movieDetail";
	}
	
}
