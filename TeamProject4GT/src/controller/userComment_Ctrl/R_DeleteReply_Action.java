package controller.userComment_Ctrl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.reply.ReplyDAO;
import model.reply.ReplyVO;

public class R_DeleteReply_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();

		// VO DAO �ν��Ͻ�ȭ
		ReplyDAO replyDAO = new ReplyDAO();
		ReplyVO replyVO = new ReplyVO();

		// DAO���� �ʿ䵥���� SET
		replyVO.setRnum(Integer.parseInt(request.getParameter("rnum")));
		replyVO = replyDAO.SelectOne(replyVO);


		String path = null; // uri���� �ʱ�ȭ

		//DAO ����
		if(replyDAO.DeleteDB(replyVO)) {
			System.out.println("���� �����ߴ�");
			response.setContentType("text/html; charset=UTF-8"); 
			PrintWriter out=response.getWriter();
			/*commentVO = commentDAO.SelectOne(commentVO);*/
			out.println("true"); // ajax ��ü ����
		}
		// �ݿ� ���� -> ���� ����
		else {
			try {
				throw new Exception("R_DeleteReply_Action ���� �߻�!");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}


		// ���� ����

		return null;
	}

}
