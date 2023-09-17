package user;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

	@Autowired
	private DataSource dataSource;
	
	// 로그인
	public UserVO getUser(String id, String password) {
		String sql = " select * from users " + " where userid = ? and password = ? ";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		UserVO user = (UserVO) template.queryForObject(sql, new BeanPropertyRowMapper<>(UserVO.class), id, password); 
		return user;
	}
		
	// 중복ID체크
	public boolean checkId(String id) {
		String sql = " select count(*) from users where userid = ? ";
		JdbcTemplate template = new JdbcTemplate(dataSource);
        Integer count = template.queryForObject(sql, new Object[]{id}, Integer.class);

        return count != null && count > 0;
    }
		
	// 사용자 추가
	public void addUser(UserVO vo) {
		String sql =
//			" insert into users (userid, password, name, phone) " +
//			            " values (?,?,?,?) ";
		    " insert into users (userno, userid, password, name, phone) " +
                        " values ((select nvl(max(userno), 0) + 1 from users), ?,?,?,?) ";
		JdbcTemplate template= new JdbcTemplate(dataSource);
		template.update(sql, vo.getUserid(), vo.getPassword(), vo.getName(), vo.getPhone());
	}

	// 전체 사용자 목록 조회
	public List<UserVO> getUserList() {
		String sql= " select * from users order by userno desc";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		List<UserVO> userList = template.query(sql, new BeanPropertyRowMapper<>(UserVO.class));
		
		return userList;
   }

	// 전체 사용자 목록 상세 조회
	public Object getUserId(String userid) {
		String sql= " select * from users where userid = ? ";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		UserVO user = (UserVO) template.queryForObject(sql, new BeanPropertyRowMapper<>(UserVO.class), userid);
		
		return user;
	}
	
	// mypage
	public UserVO getUserNo(String userno) {
		String sql= " select * from users where userno = ? ";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		return template.queryForObject(sql, new BeanPropertyRowMapper<>(UserVO.class), userno);
	}
	
	// 사용자 정보 삭제
	public void deleteUser(String userno) {
		String sql = " delete from users WHERE userno = ? ";
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, userno);
	}

	// 사용자 정보 수정
	public void updateUser(UserVO vo) {
		String sql = 
				" update users "
				+ " set password=?, name=?, phone=?, borrown=?, role=? "
				+ " where userno=? ";

		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, vo.getPassword(), vo.getName(), vo.getPhone(),
							 vo.getBorrown(), vo.getRole(), vo.getUserno());
	}

	// 회원 검색 - 이름
	public List<UserVO> searchUserName(String name) {
        String sql = " select * from users where name LIKE ? ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template.query(sql, new BeanPropertyRowMapper<>(UserVO.class), "%" + name + "%");
    }

	// 회원 검색 - 연락처
    public List<UserVO> searchUserPhone(String phone) {
        String sql = " select * from users where phone LIKE ? ";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template.query(sql, new BeanPropertyRowMapper<>(UserVO.class), "%" + phone + "%");
    }

//		public UserVO getUserByID(String id) {
//			String query = " select * from users where userid = ? ";
//			UserVO user = null;
	//
//			try {
//				conn = JDBCUtil.getConnection();
//				stmt = conn.prepareStatement(query);
//				stmt.setString(1, id);
	//
//				rs = stmt.executeQuery();
//				if (rs.next()) {
//					user = new UserVO();
//					user.setUserno(rs.getInt("userno"));
//					user.setUserid(rs.getString("userid"));
//					user.setPassword(rs.getString("password"));
//					user.setName(rs.getString("name"));
//					user.setBorrown(rs.getInt("borrown"));
//					user.setRole(rs.getString("role"));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				JDBCUtil.close(stmt, conn);
//			}
//			return user;
//		}
	
}
