package board;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BoardDAO {
	
	@Autowired
	private DataSource dataSource;
	
	public static final int ITEMS_PER_PAGE = 10;

	public int getTotalPage() {
	    String sql = "SELECT COUNT(*) FROM tjl_board";
	    JdbcTemplate template = new JdbcTemplate(dataSource);
	    int totalCount = template.queryForObject(sql, Integer.class);
	    int totalPage = (int) Math.ceil((double)totalCount / ITEMS_PER_PAGE);
	    return totalPage;
	}
	
	public List<BoardVO> getBoardList(int pageNumber) {
        String sql = 
        		" SELECT * " +
        		  " FROM ( SELECT LEVEL, boardNO, parentNO, title, content, regtime, userid, hit, ROWNUM AS rnum " +
        		           " FROM tjl_board " +
        		          " START WITH parentNO = 0 " +
        		        " CONNECT BY PRIOR boardNO = parentNO " +
        		          " ORDER SIBLINGS BY boardNO DESC " +
        		       " ) " +
        		 " WHERE rnum BETWEEN (? - 1) * ? + 1 AND ? * ? ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<BoardVO> boardList = template.query(sql, new BeanPropertyRowMapper<>(BoardVO.class), pageNumber, ITEMS_PER_PAGE, pageNumber, ITEMS_PER_PAGE);
        return boardList;
    }

	public List<BoardVO> searchBoard(String search) {
        String query = " select * from tjl_board where title LIKE ? OR userid LIKE ? ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template.query(query, new BeanPropertyRowMapper<>(BoardVO.class), "%" + search + "%", "%" + search + "%");
    }
	
	public BoardVO getBorad(String boardNO) {
		String sql = " select * from tjl_board WHERE boardNO = ? ";

		JdbcTemplate template = new JdbcTemplate(dataSource);
		BoardVO board = template.queryForObject(sql, new BeanPropertyRowMapper<>(BoardVO.class), boardNO);
		
		return board;
	}
	
	public void addBoard(BoardVO vo) {
		String sql = 
				" insert into tjl_board(boardNO, userid, title, content) " +
						" values((select nvl(max(boardNO), 0) + 1 from tjl_board), ?, ?, ?) ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getUserid(), vo.getTitle(), vo.getContent());
	}
	
	public void updateBoard(BoardVO vo) {
		String sql = 
				" UPDATE tjl_board SET title = ?, content = ? WHERE boardNO = ? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getTitle(), vo.getContent(), vo.getBoardNO());
	}
	
	public void deleteBoard(String boardNO) {
		String sql = " delete from tjl_board WHERE boardNO = ? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, boardNO);
	}
	
	public void addReply(BoardVO vo) {
		String sql = 
				" insert into tjl_board(boardNO, parentNO, title, userid, content) " +
						" values((select nvl(max(boardNO), 0) + 1 from tjl_board), ?, ?, ?, ?) ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getBoardNO(), vo.getTitle(), vo.getUserid(), vo.getContent());
	}
	
	// 게시글 삭제시 댓글 삭제
	public void deleteComment(String boardNO) {
		String sql =
				" delete tjl_comment where boardNO=? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, boardNO);
	}
	
}