package controller.post_ctrl.post.create;

import java.io.IOException;
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
import model.post.PostDAO;
import model.post.PostVO;
import model.userInfo.UserInfoVO;

public class InsertPostAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();


		String realFolder = "";
		String filename1 = "";
		// ���� ũ�� 3MB�� ����
		int maxSize = 1024*1024*3;
		String encType = "utf-8";   

		// �̳༮�� ���ε� �� ���� �̸�
		String savefile = "img";

		String filename = "";

		// ������ ����� ������ ���
		ServletContext scontext = request.getSession().getServletContext();
		realFolder = scontext.getRealPath(savefile);
	
		
		PostDAO PDAO = new PostDAO();
		PostVO PVO = new PostVO();
		UserInfoVO UVO= new UserInfoVO();
		PVO.setPnum(PDAO.expectPnum());
		try{
			// ���� ���ε�
			MultipartRequest multi=new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
			Enumeration<?> files = multi.getFileNames(); 
			String file1 = (String)files.nextElement();
			filename1 = multi.getFilesystemName(file1);
			System.out.println(filename1);
			HttpSession session = request.getSession();
			PVO.setCategory(multi.getParameter("category"));
			PVO.setContent(multi.getParameter("content"));
			UVO = (UserInfoVO) session.getAttribute("userInfoData"); // �̸��� ���ǿ��� VO�� ����� UserInfoVO ���!
			PVO.setWriter(UVO.getName()); // Name ���� ����� �� �Ʒ����� nullpointer�ߴµ� ������ �α����� �ȵǼ� ���ǿ� userInfo�� ����!
			PVO.setP_user(UVO.getId()); // ID
			PVO.setTitle(multi.getParameter("title"));
			System.out.println(PVO);
			
			// ���ε� �� ������ �̸� �����ϴ� ����
			Path oldfile = Paths.get(realFolder +"/" +filename1);
			Path newfile = Paths.get(realFolder +"/" +PVO.getPnum()+"_PostImage.jpg"); 
			System.out.println("oldfile "+oldfile);
			System.out.println("newfile "+newfile);
			Files.move(oldfile, newfile, StandardCopyOption.REPLACE_EXISTING);
			realFolder = "img";
			String fullpath = realFolder + "/" + PVO.getPnum()+"_PostImage.jpg";

			// DB������Ʈ
			PVO.setPath(fullpath);
		} catch(Exception e) {
			e.printStackTrace();
		}


		if(PDAO.InsertDB(PVO)){
			// ������Ʈ�� �Ϸ������ ���ư��� ������
			forward.setRedirect(true);
			forward.setPath("main.pdo");	
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

		return null;
	}

}
