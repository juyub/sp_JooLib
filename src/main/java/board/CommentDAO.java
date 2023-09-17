package board;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentDAO {
	@Autowired
	private DataSource dataSource;

	public List<CommentVO> getCommentListByBoardNO(String boardNO) { 
	    String sql =
	        " SELECT * " +
	        " FROM tjl_comment " +
	        " WHERE boardNO = ? ";

	    JdbcTemplate template = new JdbcTemplate(dataSource);
	    List<CommentVO> commentList = template.query(sql, new BeanPropertyRowMapper<>(CommentVO.class), boardNO); 
		
	    return commentList;
	}
	
	public void addComment(CommentVO vo) {
		String sql =
				" INSERT INTO tjl_comment (commentNO, boardNO, userid, content) " + 
						" VALUES ((select nvl(max(commentNo), 0) + 1 from tjl_comment), ?, ?, ?) ";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	    jdbcTemplate.update(sql, vo.getBoardNO(), vo.getUserid(), vo.getContent());
	}
	
	public void updateComment(CommentVO vo) {
		String sql =
				" UPDATE tjl_comment SET content=? WHERE commentNO=? "; 
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, vo.getContent(), vo.getCommentNO());
	}
	
	public void deleteComment(CommentVO vo) {
        String sql =
            " DELETE FROM tjl_comment WHERE commentNO=? ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, vo.getCommentNO());
    }
}