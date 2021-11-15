package controller.userComment_Ctrl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.comments.CommentsDAO;
import model.comments.CommentsVO;

public class C_LikeUp_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward action = new ActionForward();
		// likeCntUp�� �ʿ��� ���� : cnum
		// �������� ���ư������� �ʿ��� ���� : pnum, index
		String pnum = request.getParameter("pnum");
		int cnum = Integer.parseInt(request.getParameter("cnum"));

		// DB������Ʈ�� ���� �ʿ��� ����
		CommentsVO CVO = new CommentsVO();
		CommentsDAO CDAO = new CommentsDAO();
		CVO.setCnum(cnum);

		// DB������Ʈ, ����¡ ó��
		String path = null;
		if (CDAO.likeCntUp(CVO)) {
			response.setContentType("text/html; charset=UTF-8"); 
			PrintWriter out=response.getWriter();
			
			CommentsVO newData = CDAO.SelectOne(CVO);
			String result = "[{\"clikeCnt\":\"" + newData.getClikeCnt()+"\"}]";
			out.println(result); // ajax ��ü ����
			
		}
		else {
			try {
				throw new Exception("C_LikeUp_Action �����߻�!");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
				
		return null;
	}

}
