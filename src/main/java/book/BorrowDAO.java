package book;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BorrowDAO {

    @Autowired
    private DataSource dataSource;

    // 대출 정보 전체 조회 메서드 - title, username 출력
    public List<BorrowVO> getBorrowList(BorrowVO vo) {
        String query =
                " SELECT b.borrowno, u.userid AS username, bo.title as booktitle, b.borrowdate, b.duedate, b.returndate,b.userno,b.bookno "
                        + " FROM borrows b "
                        + " JOIN users u ON b.userno = u.userno "
                        + " JOIN books bo ON b.bookno = bo.bookno "
                        + " order by borrowdate desc ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BorrowVO.class));
    }

    // 대출 정보 조회 메서드 - bookno, userno로 조회
    public BorrowVO getBorrow(BorrowVO vo) {
        String query = " SELECT * from borrows where userno = ? and bookno = ? ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<BorrowVO> results = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BorrowVO.class), vo.getUserno(), vo.getBookno());

        return results.isEmpty() ? null : results.get(0);
    }

    // 대출 정보 조회 - userno로 조회, 도서명 출력
    public List<BorrowVO> getBorrowUserNo(BorrowVO vo) {
        String query = 
        		" SELECT a.borrowno, a.userno, books.title AS booktitle, " +
                       " a.borrowdate, a.duedate, a.returndate "
                + " FROM borrows a"
                + " JOIN books ON a.bookno = books.bookno "
                + " WHERE a.userno = ? "
                + " order by returndate desc ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BorrowVO.class), vo.getUserno());
    }

    // 대출 정보 조회 - userno로 조회, 미반납
    public List<BorrowVO> getBorrowingUser(BorrowVO vo) {
        String query =
                " SELECT * FROM borrows "
                        + " WHERE userno = ? ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BorrowVO.class), vo.getUserno());
    }

    
    // 대출 메서드
    public void borrowBook(BorrowVO vo) {
        insertBorrowInfo(vo);
        updateBookAvailablen(vo);
        updateUserBorrown(vo);
    }

    // 대출 정보를 추가하는 메서드
    private void insertBorrowInfo(BorrowVO vo) {
        String query = "INSERT INTO borrows (userno, bookno, borrowdate, duedate) "
                + "VALUES (?, ?, SYSDATE, SYSDATE + 7)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getUserno(), vo.getBookno());
    }

    // 도서 재고를 -1 메서드
    private void updateBookAvailablen(BorrowVO vo) {
        String query = "UPDATE books SET availablen = availablen - 1 WHERE bookno = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getBookno());
    }

    // 대출 가능 권수 -1 메서드
    private void updateUserBorrown(BorrowVO vo) {
        String query = "UPDATE users SET borrown = borrown - 1 WHERE userno = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getUserno());
    }

    // 반납 메서드
    public void returnBook(BorrowVO vo) {
        deleteBorrowInfo(vo);
        addBookAvailablen(vo);
        addUserBorrown(vo);
    }

    // 대출 정보를 삭제하는 메서드
    private void deleteBorrowInfo(BorrowVO vo) {
        String query = " delete borrows WHERE borrowno = ? ";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getBorrowno());
    }

    // 도서 재고를 +1 메서드
    private void addBookAvailablen(BorrowVO vo) {
        String query = "UPDATE books SET availablen = availablen + 1 WHERE bookno = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getBookno());
    }

    // 대출 가능 권수 +1 메서드
    private void addUserBorrown(BorrowVO vo) {
        String query = "UPDATE users SET borrown = borrown + 1 WHERE userno = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, vo.getUserno());
    }
}
