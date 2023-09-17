package book;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import board.BoardVO;

@Component
public class BookDAO {

	@Autowired
	private DataSource dataSource;
	
	public static final int ITEMS_PER_PAGE = 10;

    public int getTotalPage() {
        String sql = "SELECT COUNT(*) FROM books";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int totalCount = template.queryForObject(sql, Integer.class);
        int totalPage = (int) Math.ceil((double) totalCount / ITEMS_PER_PAGE);
        return totalPage;
    }

    public List<BookVO> getBookList(BookVO bookVO, int pageNumber) {
        String sql =
                " SELECT * " +
                " FROM ( SELECT bookno, title, author, publisher, category, ROWNUM AS rnum " +
                "        FROM books " +
                "        ORDER BY bookno DESC) " +
                " WHERE rnum BETWEEN (? - 1) * ? + 1 AND ? * ? ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<BookVO> bookList = template.query(sql, new BeanPropertyRowMapper<>(BookVO.class), pageNumber, ITEMS_PER_PAGE, pageNumber, ITEMS_PER_PAGE);
        return bookList;
    }
	
    public List<BookVO> searchBook(String search) {
        String query = " select * from books where title LIKE ? OR author LIKE ? ORDER BY bookno DESC ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template.query(query, new BeanPropertyRowMapper<>(BookVO.class), "%" + search + "%", "%" + search + "%");
    }
    
    public BookVO getBook(String bookno) {
		String sql = " select * from books where bookno = ? ";

		JdbcTemplate template = new JdbcTemplate(dataSource);
		BookVO book = template.queryForObject(sql, new BeanPropertyRowMapper<>(BookVO.class), bookno);
		
		return book;
	}
	
	public void addBook(BookVO vo) {
	    String description = vo.getDescription();
	    if (description.length() > 1000) {
	        description = description.substring(0, 1000);
	    }
		
		String sql = 
			" insert into books " +
				" (bookno, title, author, publisher, publicationyear," +
							" isbn, category, totaln, availablen, image, description) " +
				" values((select nvl(max(bookno), 0) + 1 from books),?,?,?,?,?,?,?,?,?,?) ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getTitle(), vo.getAuthor(), vo.getPublisher(),
								vo.getPublicationyear(), vo.getIsbn(),vo.getCategory(),
								vo.getTotaln(),vo.getAvailablen(),vo.getImage(), vo.getDescription());
	}
	
	public void updateBook(BookVO vo) {
		String sql = 
			 " update books " +
				 " set title=?, author=?, publisher=?, publicationyear=?, " +
				     " isbn=?, category=?, totaln=?, availablen=?, description=? " +
				 " where bookno=? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getTitle(), vo.getAuthor(), vo.getPublisher(),
				vo.getPublicationyear(), vo.getIsbn(), vo.getCategory(), 
				vo.getTotaln(), vo.getAvailablen(), vo.getDescription(), vo.getBookno());
	}
	
	public void deleteBook(String bookno) {
		String sql = " delete from books where bookno = ? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, bookno);
	}
    
}
