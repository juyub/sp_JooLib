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
public class BorrowController {

    @Autowired
    private BorrowDAO borrowDAO;
    
    @Autowired
    private BorrowVO borrowVO;
    
    @RequestMapping("getBorrowList")
    public String getBorrowList(Model model) {
        List<BorrowVO> borrowList = borrowDAO.getBorrowList(borrowVO);

        model.addAttribute("borrowList", borrowList);

        return "book/getBorrowList";
    }

    @RequestMapping("getBorrowUser")
    public String getBorrowUser(HttpSession session, Model model) {
        UserVO user = (UserVO) session.getAttribute("login");

        borrowVO.setUserno(user.getUserno());
        List<BorrowVO> borrowUser = borrowDAO.getBorrowUserNo(borrowVO);

        model.addAttribute("borrowUser", borrowUser);

        return "book/getBorrowUser";
    }

    @RequestMapping("borrowBook")
    public String borrowBook(@RequestParam("bookno") int bookno,
    						 @RequestParam("userno") int userno) {
        borrowVO.setBookno(bookno);
        borrowVO.setUserno(userno);

        borrowDAO.borrowBook(borrowVO);

        return "redirect:/getBook?bookno="+bookno;
    }
    
    @RequestMapping("returnBook")
    public String returnBook(@RequestParam("borrowno") int borrowno,
    						 @RequestParam("bookno") int bookno,
    						 @RequestParam("userno") int userno) {
        borrowVO.setBookno(bookno);
        borrowVO.setUserno(userno);
        borrowVO.setBorrowno(borrowno);

        borrowDAO.returnBook(borrowVO);

        return "redirect:/getBorrowList";
    }
}
