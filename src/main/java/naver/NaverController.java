package naver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NaverController {
	
    @Autowired
    private NaverDAO newsDAO;
    
    @RequestMapping("naverBooks")
    public String searchBooks(@RequestParam("query") String query, Model model) {
        List<NaverVO> bookList = newsDAO.findBooks(query);
        model.addAttribute("books", bookList);
        return "book/naverBooks";
    }

//    @RequestMapping(value = "naverBooks", produces = "text/html; charset=UTF-8")
//    @ResponseBody
//    public String searchBooks(@RequestParam(value = "query", required = false) String query, Model model) {
//        if (query != null && !query.trim().isEmpty()) {
//            List<NaverVO> books = newsDAO.findBooks(query);
//            model.addAttribute("books", books);
//        }
//        return "book/naverBooks :: #books";
//    }


//    @RequestMapping("naverBooks")
//    public String searchBooks(@RequestParam(value = "query", required = false) String query, Model model) {
//        if (query != null && !query.trim().isEmpty()) {
//            List<NaverVO> newsList = newsDAO.findBooks(query);
//            model.addAttribute("newsList", newsList);
//        }
//        return "book/naverBooks";
//    }
}
