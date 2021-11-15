package controller.userComment_Ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.comments.CommentsDAO;
import model.comments.CommentsVO;

public class C_EditComment_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
		response.setContentType("text/html; charset=UTF-8"); 
		PrintWriter out=response.getWriter();
		
		// VO DAO �ν��Ͻ�ȭ
	    CommentsVO commentVO = new CommentsVO();
	    CommentsDAO commentDAO = new CommentsDAO();
	    
	    // DAO���� �ʿ䵥���� SET
	    commentVO.setCment(request.getParameter("cment"));
	    commentVO.setCnum(Integer.parseInt(request.getParameter("cnum")));
	    CommentsVO newData = new CommentsVO();
	    
		String path = null; // uri���� �ʱ�ȭ
	    
	    //DAO ����
	    // ��� ���� �Ϸ� --> showPost�̵�
	    if (commentDAO.UpdateDB(commentVO)) {	
	    	newData = commentDAO.SelectOne(commentVO);	
	    }
	    // �ݿ� ���� -> ���� ����
	    else {
	    	try {
				throw new Exception("C_EditComment_Action ���� �߻�!");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	    }

	    String msg = newData.getCment().replace("\n"," ");
	    System.out.println(msg);
	  
	    String result = "[{\"cment\":\"" + msg+"\",\"cdate\":\"" + newData.getCdate() + "\"}]";
	    System.out.println(result);
	    out.println(result);

	   
	    return null;
	}

}
