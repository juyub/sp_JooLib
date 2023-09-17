package board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {

	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private BoardVO boardVO;
	
	@Autowired
	private CommentDAO commentDAO;

	@Autowired
	private CommentVO commentVO;

	@RequestMapping("getBoardList")
	public String getBoardList(@RequestParam(defaultValue = "1") int pageNumber, Model model) {
		List<BoardVO> boardList = boardDAO.getBoardList(pageNumber);
		int totalPage = boardDAO.getTotalPage();

		model.addAttribute("boardList", boardList);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("totalPage", totalPage);

		return "board/boardList";
	}
	
	@RequestMapping("searchBoard")
	public String searchBoard(@RequestParam String search, Model model) {
	    List<BoardVO> boardList = boardDAO.searchBoard(search);
	    model.addAttribute("boardList", boardList);

	    return "board/boardList";
	}
	
	@RequestMapping("getBoard")
	public String getBoard(String boardNO, Model model) {
		BoardVO board = boardDAO.getBorad(boardNO);
		model.addAttribute("board", board);
		
		List<CommentVO> commentList = commentDAO.getCommentListByBoardNO(boardNO);
		model.addAttribute("commentList", commentList);

		return "board/getBoard";
	}

	@RequestMapping("addBoard")
	public String addBoard(BoardVO vo) {
		boardDAO.addBoard(vo);

		return "redirect:/getBoardList";
	}

	@RequestMapping("updateBoard")
	public String updateBoard(BoardVO vo) {
		boardDAO.updateBoard(vo);

		return "redirect:getBoard?boardNO=" + vo.getBoardNO();
	}

	@RequestMapping("deleteBoard")
	public String deleteBoard(String boardNO) {
		boardDAO.deleteComment(boardNO);
		boardDAO.deleteBoard(boardNO);

		return "redirect:/getBoardList";
	}
	
	@RequestMapping("addReply")
	public String addReply(BoardVO vo) {
		boardDAO.addReply(vo);

		return "redirect:/getBoardList";
	}

}
