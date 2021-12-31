package model.comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.common.DBCP;
import model.reply.ReplyVO;

public class CommentsDAO {

	// �⺻ CRUD
	private static String sql_SELECT_ALL = "SELECT * FROM comments";
	private static String sql_SELECT_ONE = "SELECT * FROM comments WHERE cnum=?";
	// �̿��� secretNum �߰�
	
//	MySql sql
	private static String sql_INSERT = "INSERT INTO comments (cment, cdate, cwriter, c_user, c_post, secretNum) VALUES(?, now(), ?, ?, ?, ?)";
	
//	oracle sql
//	private static String sql_INSERT = "INSERT INTO comments (cnum, cment, cdate, cwriter, c_user, c_post, secretNum) VALUES((SELECT NVL(MAX(cnum),0) + 1 FROM comments), ?, sysdate, ?, ?, ?, ?)";
	private static String sql_DELETE = "DELETE FROM comments WHERE cnum=?";
	
	// MySql sql
	private static String sql_UPDATE = "UPDATE comments SET cment=?, cdate=now() WHERE cnum=?";
	
	//oracle sql
//	private static String sql_UPDATE = "UPDATE comments SET cment=?, cdate=sysdate WHERE cnum=?";

	// ����� ���� �Լ�
	private static String sql_SELECT_POST = "SELECT * FROM comments WHERE c_post=? ORDER BY cnum"; // c_post�� �޾Ƽ� �� ���� ��۵��� ����
	private static String sql_likeCntUp = "UPDATE comments SET clikeCnt=clikeCnt+1 WHERE cnum=?";
	// �ٸ� ���̺� ���� ����
	private static String sql_SELECT_ALL_REPLY = "SELECT * FROM reply WHERE r_comments=? ORDER BY rnum";
	private static String sql_comCntUp = "UPDATE post SET comCnt=comCnt+1 WHERE pnum=?";
	private static String sql_comCntDown = "UPDATE post SET comCnt=comCnt-? WHERE pnum=?";
	private static String sql_deleteReply = "DELETE FROM reply WHERE r_comments=?";
	
	public ArrayList<CommentsSet> getSetData(CommentsVO vo) {
		Connection conn = DBCP.connect();
		ArrayList<CommentsSet> result = new ArrayList<CommentsSet>();
		PreparedStatement pstmt = null;

		try{
			// c_post(�� ��ȣ)�� �´� ��۵��� ���
			pstmt = conn.prepareStatement(sql_SELECT_POST);
			pstmt.setInt(1, vo.getC_post());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				// �� �ϳ��� CommentsSet �Ѱ��̹Ƿ� ���� ����
				CommentsSet cs = new CommentsSet(); 
				CommentsVO cvo = new CommentsVO();
				cvo.setCnum(rs.getInt("cnum"));
				cvo.setCment(rs.getString("cment"));
				cvo.setCdate(rs.getDate("cdate"));
				cvo.setCwriter(rs.getString("cwriter"));
				cvo.setReplyCnt(rs.getInt("replyCnt"));
				cvo.setClikeCnt(rs.getInt("clikeCnt"));
				cvo.setC_user(rs.getString("c_user"));
				cvo.setC_post(rs.getInt("c_post"));
				cvo.setSecretNum(rs.getInt("secretNum")); 
				// CommentsSet�� ����� set
				cs.setComment(cvo);

				// 2��° ���� -> ��ۿ� �ִ� ��۵��� ���
				pstmt = conn.prepareStatement(sql_SELECT_ALL_REPLY);
				pstmt.setInt(1, cvo.getCnum()); // ��� PK�������� 
				ResultSet rrs = pstmt.executeQuery();
				// rlist�� ���� ���(���) �߰�
				ArrayList<ReplyVO> rlist = new ArrayList<ReplyVO>();
				while(rrs.next()) {
					ReplyVO rvo = new ReplyVO();
					rvo.setRnum(rrs.getInt("rnum"));
					rvo.setRment(rrs.getString("rment"));
					rvo.setRdate(rrs.getDate("rdate"));
					rvo.setRwriter(rrs.getString("rwriter"));
					rvo.setRlikeCnt(rrs.getInt("rlikeCnt"));
					rvo.setR_user(rrs.getString("r_user"));
					rvo.setR_post(rrs.getInt("r_post"));
					rvo.setR_comments(rrs.getInt("r_comments"));
					rlist.add(rvo);
				}
				rrs.close();
				// CommentsSet�� ��� ����Ʈ set
				cs.setRlist(rlist);
				// result�� CommentsSet �߰�
				result.add(cs);
			}
			rs.close();
		}
		catch(Exception e) {
			System.out.println("CommentsDAO getSetData���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt, conn);
		}
		return result;
	}


	// SELECT ALL -> ��ü DB���� ����
	public ArrayList<CommentsVO> SelectAll(){
		Connection conn = DBCP.connect();
		ArrayList<CommentsVO> datas = new ArrayList();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql_SELECT_ALL);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				CommentsVO vo = new CommentsVO();
				vo.setCnum(rs.getInt("cnum"));
				vo.setCment(rs.getString("cment"));
				vo.setCdate(rs.getDate("cdate"));
				vo.setCwriter(rs.getString("cwriter"));
				vo.setReplyCnt(rs.getInt("replyCnt"));
				vo.setClikeCnt(rs.getInt("clikeCnt"));
				vo.setC_user(rs.getString("c_user"));
				vo.setC_post(rs.getInt("c_post"));
				datas.add(vo);
			}
			rs.close();
		}
		catch(Exception e) {
			System.out.println("CommentsDAO SelectAll()���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt, conn);
		}
		return datas;
	}

	// SELECT ONE 
	public CommentsVO SelectOne(CommentsVO vo) {
		Connection conn=DBCP.connect();
		CommentsVO data=null;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_SELECT_ONE);
			pstmt.setInt(1, vo.getCnum());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				data=new CommentsVO();
				data.setCnum(rs.getInt("cnum"));
				data.setCment(rs.getString("cment"));
				data.setCdate(rs.getDate("cdate"));
				data.setCwriter(rs.getString("cwriter"));
				data.setReplyCnt(rs.getInt("replyCnt"));
				data.setClikeCnt(rs.getInt("clikeCnt"));
				data.setC_user(rs.getString("c_user"));
				data.setC_post(rs.getInt("c_post"));
			}   
			rs.close();
		}
		catch(Exception e){
			System.out.println("CommentsDAO SelectOne()���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return data;
	}

	// INSERT -> ��� DB ��� --> POST ���̺� ��� �� ���� Ʈ�����
	public boolean InsertDB(CommentsVO vo) {
		Connection conn=DBCP.connect();
		boolean res = false;
		PreparedStatement pstmt=null;
		try{
			// ��� INSERT
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql_INSERT);
			pstmt.setString(1, vo.getCment());
			pstmt.setString(2, vo.getCwriter());
			pstmt.setString(3, vo.getC_user());
			pstmt.setInt(4, vo.getC_post());
			pstmt.setInt(5, vo.getSecretNum());
			pstmt.executeUpdate();
			pstmt.close();

			// "UPDATE post SET comCnt=comCnt+1 WHERE pnum=?"
			// POST ��� �� ++
			pstmt=conn.prepareStatement(sql_comCntUp);
			pstmt.setInt(1, vo.getC_post());
			pstmt.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
			res=true;
		}
		catch(Exception e){
			System.out.println("CommentsDAO InsertDB()���� ���");
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

	// DELETE -> ��� ���� -> ��۵� ����
	public boolean DeleteDB(CommentsVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			conn.setAutoCommit(false);
			
			// ��ۻ���
			pstmt=conn.prepareStatement(sql_DELETE);
			pstmt.setInt(1, vo.getCnum());
			pstmt.executeUpdate();
			
			// �� �Խù��� ���cnt �ٿ�!
			pstmt=conn.prepareStatement(sql_comCntDown);
			pstmt.setInt(1, vo.getReplyCnt() + 1);
			pstmt.setInt(2, vo.getC_post());
			pstmt.executeUpdate();
			
			// �� �ش� ����� ��� ����
			pstmt=conn.prepareStatement(sql_deleteReply);
			pstmt.setInt(1, vo.getCnum());
			pstmt.executeUpdate();
			
			
			conn.commit();
			conn.setAutoCommit(true);
			res=true;
		}
		catch(Exception e){
			System.out.println("CommentsDAO DeleteDB()���� ���");
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

	// UPDATE -> ��� cment ����
	public boolean UpdateDB(CommentsVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_UPDATE);
			pstmt.setString(1, vo.getCment());
			pstmt.setInt(2, vo.getCnum());
			pstmt.executeUpdate();
			res=true;
		}
		catch(Exception e){
			System.out.println("CommentsDAO UpdateDB()���� ���");
			e.printStackTrace();
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}

	// ���ƿ� ++
	public boolean likeCntUp(CommentsVO vo) {
		Connection conn=DBCP.connect();
		boolean res=false;
		PreparedStatement pstmt=null;
		try{
			pstmt=conn.prepareStatement(sql_likeCntUp);
			pstmt.setInt(1, vo.getCnum());
			pstmt.executeUpdate();
			res=true;
		}
		catch(Exception e){
			System.out.println("CommentsDAO likeCntUp()���� ���");
			e.printStackTrace();
			//res=false;
		}
		finally {
			DBCP.disconnect(pstmt,conn);
		}
		return res;
	}

	// �Է� vo �ȿ��� c_post�� �����ϸ� �˴ϴ�. ������ �� �ۿ� �޸� ��۵鸸 �̾��ݴϴ�. 
	public ArrayList<CommentsVO> SelectPost(CommentsVO vo){
		Connection conn = DBCP.connect();
		ArrayList<CommentsVO> datas = new ArrayList();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql_SELECT_POST);
			pstmt.setInt(1,vo.getC_post());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				CommentsVO data = new CommentsVO();
				data.setCnum(rs.getInt("cnum"));
				data.setCment(rs.getString("cment"));
				data.setCdate(rs.getDate("cdate"));
				data.setCwriter(rs.getString("cwriter"));
				data.setClikeCnt(rs.getInt("clikeCnt"));
				data.setC_user(rs.getString("c_user"));
				data.setC_post(rs.getInt("c_post"));
				datas.add(data);
			}
			rs.close();
		}
		catch(Exception e) {
			System.out.println("CommentsDAO SelectPost()���� ���");
			e.printStackTrace();
		}
		finally {
			DBCP.disconnect(pstmt, conn);
		}
		return datas;
	}
}