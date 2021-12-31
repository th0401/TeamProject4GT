package model.reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

import model.comments.CommentsVO;
import model.common.DBCP;

public class ReplyDAO {
	// �⺻ ����Ͻ� ����
	private static String sql_SELECT_ALL = "SELECT * FROM reply";
	private static String sql_SELECT_ONE = "SELECT * FROM reply WHERE rnum=?";
//	MySql sql
	private static String sql_INSERT = "INSERT INTO reply (rment, rdate, rwriter, r_user, r_post, r_comments) VALUES(?, now(), ?, ?, ?, ?)";
//	oracle sql
//	private static String sql_INSERT = "INSERT INTO reply (rnum, rment, rdate, rwriter, r_user, r_post, r_comments) VALUES((SELECT NVL(MAX(rnum),0) + 1 FROM reply), ?, sysdate, ?, ?, ?, ?)";
	
	private static String sql_DELETE = "DELETE FROM reply WHERE rnum=?";
	
	// MySql sql
	private static String sql_UPDATE = "UPDATE reply SET rment=?, rdate=now() WHERE rnum=?";
	
	// oracle sql
//	private static String sql_UPDATE = "UPDATE reply SET rment=?, rdate=sysdate WHERE rnum=?";

	// ����� ���� �Լ�
	private static String sql_likeCntUp = "UPDATE reply SET rlikeCnt=rlikeCnt+1 WHERE rnum=?";
	private static String sql_comCntUp = "UPDATE post SET comCnt=comCnt+1 WHERE pnum=?";
	private static String sql_comCntDown = "UPDATE post SET comCnt=comCnt-1 WHERE pnum=?";
	private static String sql_replyCntUp = "UPDATE comments SET replyCnt=replyCnt+1 WHERE cnum=?";
	private static String sql_replyCntDown = "UPDATE comments SET replyCnt=replyCnt-1 WHERE cnum=?";
	
	// SELECT ALL -> ��ü DB���� ����
	public ArrayList<ReplyVO> SelectAll(){
		Connection conn = DBCP.connect();
		ArrayList<ReplyVO> datas = new ArrayList();
		PreparedStatement pstmt = null;

		try { 
			pstmt = conn.prepareStatement(sql_SELECT_ALL);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				ReplyVO vo = new ReplyVO();
				vo.setRnum(rs.getInt("rnum"));
				vo.setRment(rs.getString("rment"));
				vo.setRdate(rs.getDate("rdate"));
				vo.setRwriter(rs.getString("rwriter"));
				vo.setRlikeCnt(rs.getInt("rlikeCnt"));
				vo.setR_user(rs.getString("r_user"));
				vo.setR_post(rs.getInt("r_post"));
				vo.setR_comments(rs.getInt("r_comments"));
				datas.add(vo);
			}
			rs.close();
		}
		catch(Exception e) {
			System.out.println("ReplyDAO SelectAll()���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt, conn);
		}
		return datas;
	}

	// SELECT ONE 
	public ReplyVO SelectOne(ReplyVO vo) {
		Connection conn=DBCP.connect();
		ReplyVO data=null;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_SELECT_ONE);
			pstmt.setInt(1, vo.getRnum());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				data=new ReplyVO();
				data.setRnum(rs.getInt("rnum"));
				data.setRment(rs.getString("rment"));
				data.setRdate(rs.getDate("rdate"));
				data.setRwriter(rs.getString("rwriter"));
				data.setRlikeCnt(rs.getInt("rlikeCnt"));
				data.setR_user(rs.getString("r_user"));
				data.setR_post(rs.getInt("r_post"));
				data.setR_comments(rs.getInt("r_comments"));
			}   
			rs.close();
		}
		catch(Exception e){
			System.out.println("ReplyDAO SelectOne()���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return data;
	}

	// INSERT -> ��� DB ��� --> POST ���̺� ��� �� ++ Ʈ�����ó��
	public boolean InsertDB(ReplyVO vo) {
		Connection conn=DBCP.connect();
		boolean res = false;
		PreparedStatement pstmt=null;
		try{
			// ��� INSERT
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql_INSERT);
			pstmt.setString(1, vo.getRment());
			pstmt.setString(2, vo.getRwriter());
			pstmt.setString(3, vo.getR_user());
			pstmt.setInt(4, vo.getR_post());
			pstmt.setInt(5, vo.getR_comments());
			pstmt.executeUpdate();

			// POST ��� �� ++
			pstmt=conn.prepareStatement(sql_comCntUp);
			pstmt.setInt(1, vo.getR_post());
			pstmt.executeUpdate();
			
			// Comment ���̺� ��� �� ++
			pstmt=conn.prepareStatement(sql_replyCntUp);
			pstmt.setInt(1, vo.getR_comments());
			pstmt.executeUpdate();
			
			conn.commit();
			conn.setAutoCommit(true);
			res=true;
		}
		catch(Exception e){
			System.out.println("ReplyDAO InsertDB()���� ���");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}

	// DELETE -> ��� ���� --> POST ��� �� --, Comments ��� �� -- Ʈ����� ó��
	public boolean DeleteDB(ReplyVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			// ��ۻ���
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql_DELETE);
			pstmt.setInt(1, vo.getRnum());
			pstmt.executeUpdate();
			
			// POST ���̺� ��� �� --
			pstmt=conn.prepareStatement(sql_comCntDown);
			pstmt.setInt(1, vo.getR_post());
			pstmt.executeUpdate();
			
			// Comments ���̺� ��� �� --
			pstmt=conn.prepareStatement(sql_replyCntDown);
			pstmt.setInt(1, vo.getR_comments());
			pstmt.executeUpdate();
			
			conn.commit();
			conn.setAutoCommit(true);
			res=true;
		}
		catch(Exception e){
			System.out.println("ReplyDAO DeleteDB()���� ���");
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}

	// UPDATE -> ��� rment ����
	public boolean UpdateDB(ReplyVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_UPDATE);
			pstmt.setString(1, vo.getRment());
			pstmt.setInt(2, vo.getRnum());
			pstmt.executeUpdate();
			res=true;
		}
		catch(Exception e){
			System.out.println("ReplyDAO UpdateDB()���� ���");
			e.printStackTrace();
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}
	
	// ���ƿ� ++
	public boolean likeCntUp(ReplyVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_likeCntUp);
			pstmt.setInt(1, vo.getRnum());
			pstmt.executeUpdate();
			res=true;
		}
		catch(Exception e){
			System.out.println("ReplyDAO likeCntUp()���� ���");
			e.printStackTrace();
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}

}
