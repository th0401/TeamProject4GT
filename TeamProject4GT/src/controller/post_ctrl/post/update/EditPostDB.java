package controller.post_ctrl.post.update;

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
import model.likeInfo.LikeInfoDAO;
import model.likeInfo.LikeInfoVO;
import model.post.PostDAO;
import model.post.PostVO;
import model.userInfo.UserInfoVO;

public class EditPostDB implements Action{

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
		// 
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
			HttpSession session = request.getSession();
			PVO.setPnum(Integer.parseInt(multi.getParameter("pnum")));
			PVO.setCategory(multi.getParameter("category"));
			PVO.setContent(multi.getParameter("content"));
			UVO = (UserInfoVO) session.getAttribute("userInfoData"); // �̸��� ���ǿ��� VO�� ����� UserInfoVO ���!
			PVO.setWriter(UVO.getName()); 
			PVO.setP_user(UVO.getId()); // ID
			PVO.setTitle(multi.getParameter("title"));
			System.out.println("EditPostDB : "+filename1);
			
			
			// post ���� ��� ����
			if(filename1 == null) { // ����ڰ� ������ �������� �ʾҴٸ�
				System.out.println("filename1�� null : "+multi.getParameter("path"));
				PVO.setPath(multi.getParameter("path"));

			}else {
				// ���ε� �� ������ �̸� �����ϴ� ����
				Path oldfile = Paths.get(realFolder +"/" +filename1);
				Path newfile = Paths.get(realFolder +"/" +PVO.getPnum()+"_PostImage.jpg"); 
				Files.move(oldfile, newfile, StandardCopyOption.REPLACE_EXISTING);
				realFolder = "img";
				String fullpath = realFolder + "/" + PVO.getPnum()+"_PostImage.jpg";

				// DB������Ʈ
				PVO.setPath(fullpath);
			}
	
			System.out.println(PVO);


		} catch(Exception e) {
			e.printStackTrace();
		}


		if(PDAO.UpdateDB(PVO)){
			// ������Ʈ�� �Ϸ������ ���ư��� ������ ���� ����
			System.out.println("EditPostDB���� �Ѿ�� PVO�� �̰Ŵ�! "+ PVO);
			request.setAttribute("singlePost", PVO);
			LikeInfoDAO LDAO = new LikeInfoDAO();
			LikeInfoVO LVO = new LikeInfoVO();
			String ID = UVO.getId();
			LVO.setL_post(PVO.getPnum());
			LVO.setL_user(ID);
			request.setAttribute("likeInfo", LDAO.SelectOne(LVO)); // ���ƿ� ����

			forward.setRedirect(true);
			System.out.println(" ������������ �� : "+PVO.getPnum());
			forward.setPath("selectOne.pdo?pnum="+PVO.getPnum());	
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
