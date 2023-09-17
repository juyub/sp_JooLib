package board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommentController {

	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private CommentVO commentVO;
	
	@RequestMapping("addComment")
	public String addComment(CommentVO vo) {
		commentDAO.addComment(vo);

		return "redirect:getBoard?boardNO=" + vo.getBoardNO();
	}
	
	@RequestMapping("updateComment")
	public String updateComment(CommentVO vo) {
		commentDAO.updateComment(vo);

		return "redirect:getBoard?boardNO=" + vo.getBoardNO();
	}

	@RequestMapping("deleteComment")
	public String deleteComment(CommentVO vo) {
		commentDAO.deleteComment(vo);

		return "redirect:getBoard?boardNO=" + vo.getBoardNO();
	}
	
}