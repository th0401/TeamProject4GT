package controller.userComment_Ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

public class U_ProfileEdit_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		PrintWriter out=response.getWriter();

		String realFolder = "";
		String filename1 = "";
		// ���� ũ�� 3MB�� ����
		int maxSize = 1024*1024*3;
		String encType = "utf-8";   

		// �̳༮�� ���ε� �� ���� �̸�
		String savefile = "userProfile";

		String filename = "";

		// ������ ����� ������ ���
		ServletContext scontext = request.getSession().getServletContext();
		realFolder = scontext.getRealPath(savefile);
		System.out.println(realFolder);

		UserInfoDAO UDAO = new UserInfoDAO();
		UserInfoVO UVO = new UserInfoVO();

		try{
			// ���� ���ε�
			MultipartRequest multi=new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
			Enumeration<?> files = multi.getFileNames(); 
			String file1 = (String)files.nextElement();
			filename1 = multi.getFilesystemName(file1);
			System.out.println(filename1);

			// ���ε� �� ������ �̸� �����ϴ� ����
			HttpSession session = request.getSession();
			UVO = (UserInfoVO)session.getAttribute("userInfoData");
			System.out.println("���� UVO : "+UVO);
			/*File oldFile = new File(realFolder +"/" +filename1);
			System.out.println("�õ����� : "+oldFile);
			File newFile = new File(realFolder +"/" +UVO.getId()+"_profile.jpg");
			System.out.println("������ : "+newFile);
			oldFile.renameTo(newFile);
			System.out.println("�ٲ� �õ����� : "+oldFile);*/
			Path oldfile = Paths.get(realFolder +"/" +filename1);
			Path newfile = Paths.get(realFolder +"/" +UVO.getId()+"_profile.jpg"); 
			Files.move(oldfile, newfile, StandardCopyOption.REPLACE_EXISTING);
			realFolder = "userProfile";
			String fullpath = realFolder + "/" + UVO.getId()+"_profile.jpg";

			// DB������Ʈ
			UVO.setProfile(fullpath);
			System.out.println("���� UVO : "+UVO);
		} catch(Exception e) {
			e.printStackTrace();
		}


		if(UDAO.UpdateProfile(UVO)){
			// �θ� ������ ���ΰ�ħ
			out.println("<script>opener.location.reload();</script>");
			out.println("<script>window.close();</script>");
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

		return null;
	}

}
