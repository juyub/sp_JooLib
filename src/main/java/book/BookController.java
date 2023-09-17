package book;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import user.UserVO;

@Controller
public class BookController {

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private BookVO bookVO;
	
    @Autowired
    private BorrowDAO borrowDAO;
	
	@RequestMapping("getBookList")
    public String getBookList(@RequestParam(defaultValue = "1") int pageNumber, Model model) {
        List<BookVO> bookList = bookDAO.getBookList(bookVO, pageNumber);
        int totalPage = bookDAO.getTotalPage();

        model.addAttribute("bookList", bookList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPage", totalPage);

        return "book/getBookList";
    }
	
//	@RequestMapping("getBook")
//	public String getBook(String bookno, Model model) {
//		BookVO book = bookDAO.getBook(bookno);
//		model.addAttribute("book", book);
//
//		return "book/getBook";
//	}

	@RequestMapping("searchBook")
	public String searchBook(@RequestParam String search, Model model) {
	    List<BookVO> bookList = bookDAO.searchBook(search);
	    model.addAttribute("bookList", bookList);

	    return "/book/getBookList";
	}
	
	@RequestMapping("getBook")
	public String getBook(@RequestParam("bookno") String bookno, Model model, HttpSession session) {
	    BookVO book = bookDAO.getBook(bookno);
	    model.addAttribute("book", book);

	    UserVO user = (UserVO) session.getAttribute("login");
	    if(user != null) {
	        BorrowVO vo = new BorrowVO();
	        vo.setBookno(Integer.parseInt(bookno));
	        vo.setUserno(user.getUserno());

	        BorrowVO borrow = borrowDAO.getBorrow(vo);
	        if (borrow != null) {
	            model.addAttribute("borrow", borrow);
	        }
	    }

	    return "book/getBook";
	}

	
	@RequestMapping("addBook")
	public String addBook(BookVO vo) {
		bookDAO.addBook(vo);

		return "redirect:/getBookList";
	}

	@RequestMapping("updateBook")
	public String updateBook(BookVO vo) {
		bookDAO.updateBook(vo);

		return "redirect:getBook?bookno=" + vo.getBookno();
	}

	@RequestMapping("deleteBook")
	public String deleteBook(String bookno) {
		bookDAO.deleteBook(bookno);

		return "redirect:/getBookList";
	}
	
}
