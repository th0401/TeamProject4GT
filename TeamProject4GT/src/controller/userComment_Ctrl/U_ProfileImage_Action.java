package controller.userComment_Ctrl;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import controller.Action;
import controller.ActionForward;
import model.userInfo.UserInfoDAO;
import model.userInfo.UserInfoVO;

public class U_ProfileImage_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();

		UserInfoDAO UDAO = new UserInfoDAO();
		UserInfoVO UVO = new UserInfoVO();
		HttpSession session = request.getSession();
		UVO.setId((String)session.getAttribute("id"));

		UDAO.SelectOne(UVO);
		String realFolder = "";
		String filename1 = "";
		// ���� ũ�� 3MB�� ����
		int maxSize = 1024*1024*3;
		String encType = "utf-8";
		String savefile = "userProfile";

		// ������ ����� ������ ���
		ServletContext scontext = request.getSession().getServletContext();
		realFolder = scontext.getRealPath(savefile);

		
		try{
			// ���� ���ε�
			MultipartRequest multi=new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
			Enumeration<?> files = multi.getFileNames(); 
			String file1 = (String)files.nextElement();
			filename1 = multi.getFilesystemName(file1);
			UVO.setId(multi.getParameter("id"));
			UVO.setName(multi.getParameter("name"));
			UVO.setPw(multi.getParameter("pw"));
			session.setAttribute("userInfo", UVO);
			File oldFile = new File(realFolder +"/" +filename1);
			File newFile = new File(realFolder +"/" +UVO.getId()+"_profile");
			oldFile.renameTo(newFile);

		} catch(Exception e) {
			e.printStackTrace();
		}
		realFolder = "img";
		String fullpath = realFolder + "/" + filename1;


		if(UDAO.UpdateDB(UVO)){
			// ���� �������� �ٸ� ������ �̵��� ���� �ַ� redirect ����� �̿��� -> spring���� �ڼ���
			forward.setRedirect(true);
			forward.setPath("main.do");	
			return forward;
		}
		else{
			// ���ܸ� �߻����� ������������ �̵�
			try {
				throw new Exception("DB ���� ���� �߻�!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		forward.setRedirect(true);
		forward.setPath("login.jsp");	
		return forward;
	}


}

