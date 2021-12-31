package controller.userComment_Ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.comments.CommentsDAO;
import model.comments.CommentsVO;

public class C_InsertComment_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// view���� �Ķ���͵��� �������ָ�(c_user, c_post, cment)
	    // set�� commentVO�� ����߰�
		
		
		ActionForward forward = new ActionForward();
		
		// VO DAO �ν��Ͻ�ȭ
	    CommentsVO commentVO = new CommentsVO();
	    CommentsDAO commentDAO = new CommentsDAO();
	    
	    
	    // DAO���� �ʿ䵥���� SET
	    commentVO.setCment(request.getParameter("cment"));
	    commentVO.setCwriter(request.getParameter("cwriter"));
	    commentVO.setC_user(request.getParameter("c_user"));
	    commentVO.setC_post(Integer.parseInt(request.getParameter("c_post")));
	    commentVO.setSecretNum(0); // �⺻ 0���� set
	    
	    // secretNum�����Ͱ� �Ѿ�Դٸ� ����
	    if(request.getParameter("secretNum") != null) {
	    	commentVO.setSecretNum(Integer.parseInt(request.getParameter("secretNum")));
	    }
	    

		String path = null; // uri���� �ʱ�ȭ
		
	    //DAO ����
	    // ��� �߰� �Ϸ�
	    if (commentDAO.InsertDB(commentVO)) {
			// [����¡ó�� �޼���] ȣ�� (uri ��ȯ)
	    	path = new Post_Action().paging(request.getParameter("c_post"));
			//path += "#pcmsg"+request.getParameter("pcmsg");
	    }
	    // �ݿ� ���� -> ���� ����
	    else {
	    	try {
				throw new Exception("C_InsertComment_Action ���� �߻�!");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	    }
	    
	    // ���� ����
	    forward.setRedirect(false); // forward
	    forward.setPath(path);
	    
	    return forward;
	}

}
