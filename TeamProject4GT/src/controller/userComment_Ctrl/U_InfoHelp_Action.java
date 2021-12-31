package controller.userComment_Ctrl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.userInfo.UserInfoDAO;
import model.userInfo.UserInfoVO;

public class U_InfoHelp_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
System.out.println("���� : " + request.getParameter("type"));
		ActionForward forward = new ActionForward();

		// VO DAO �ν��Ͻ�ȭ
		UserInfoVO userInfoVO = new UserInfoVO();
		UserInfoDAO userInfoDAO = new UserInfoDAO();

		// DAO���� �ʿ䵥���� SET
		userInfoVO.setId(request.getParameter("id")+"@"+request.getParameter("mail"));
		//System.out.println(request.getParameter("id")+"@"+request.getParameter("mail"));

		// DAO ����
		userInfoVO = userInfoDAO.Find(userInfoVO);
		//System.out.println(userInfoVO+"11");
		
		// response ���� --- findUser
		request.setAttribute("findUser", userInfoVO);

		// IDã�� --> view ��ȯ -> ��ü userInfo
		if(request.getParameter("type").equals("id")) {
			// informID ������ ���� -> ������
			//forward.setPath("informID.jsp");
			
			response.setContentType("text/html; charset=UTF-8"); 
			PrintWriter out = response.getWriter();

			// ��ȯ���� ��
				// ������ȸ ����
			if(userInfoVO == null) {
				// �ڹٽ�ũ��Ʈ�� �̿��Ͽ� �˸�â ��, Login.jsp ������ �̵�
				out.println("<script>alert('�������� �ʴ� ID�Դϴ�.'); window.close(); </script>");
			}
			else { // ������ȸ ����
				// �ڹٽ�ũ��Ʈ�� �̿��Ͽ� �˸�â ��, Login.jsp ������ �̵�
				out.println("<script>alert('�α����� ������ ID�Դϴ�.'); window.close(); </script>");
			}

			return null;
		}
		// PWã�� --> view ��ȯ == ��ü userInfo
		else if (request.getParameter("type").equals("pw")) {

			// informPW ������ ����
			forward.setPath("informPW.jsp");
		}
		
		
		// ������ ���ۼ���
		forward.setRedirect(false); // forward

		
		return forward;
	}

}
