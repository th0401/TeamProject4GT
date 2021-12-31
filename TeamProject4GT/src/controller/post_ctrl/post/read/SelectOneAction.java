package controller.post_ctrl.post.read;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Action;
import controller.ActionForward;
import model.likeInfo.LikeInfoDAO;
import model.likeInfo.LikeInfoVO;
import model.post.PostDAO;
import model.post.PostVO;
import model.userInfo.UserInfoVO;

public class SelectOneAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward action = new ActionForward();
		PostDAO PDAO = new PostDAO();
		PostVO PVO = new PostVO();
		UserInfoVO UVO = new UserInfoVO();
		LikeInfoVO LVO = new LikeInfoVO();
		LikeInfoDAO LDAO = new LikeInfoDAO();
		System.out.println("SelectOne123123 "+request.getParameter("pnum"));
		if(request.getParameter("pnum")!=null) {
			PVO.setPnum(Integer.parseInt(request.getParameter("pnum"))); // pnum������ ã�°Ŵ� Pnum�� ���� �� �Ѱ���
		}
		/*if (PDAO.ViewsUp(PVO)) { // ����Ʈ�� ���� view �� -> Model Ʈ����� ó��
			request.setAttribute("singlePost", PDAO.SelectOne(PVO));
		} else {
			throw new Exception("ViewUp ���� �߻�!");
		}*/
		System.out.println(PVO);
		// �Ѱ��� ��¥ �����̽�
		PVO = PDAO.SelectOne(PVO);
		String pdate = PVO.getPdate();
		Date datePdate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			datePdate = sdf.parse(pdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sliceDate = new SimpleDateFormat("yyyy-MM-dd");
		pdate = sliceDate.format(datePdate);
		PVO.setPdate(pdate);

		// ���� �� �� ���� �α��� �ø� ����
		HttpSession session = request.getSession();

		request.setAttribute("singlePost", PVO);
		request.setAttribute("likeInfo", false); // ����ڰ� ���� ���±ۿ� ���ƿ並 �������� Ȯ���ϴ� �� ����Ʈ false
		if (session.getAttribute("userInfoData") != null) {
			UVO = (UserInfoVO) session.getAttribute("userInfoData"); // �α��� ���� ������ ���ƿ並 �������� üũ
			String ID = UVO.getId();
			int pnum = Integer.parseInt(request.getParameter("pnum"));
			LVO.setL_post(pnum);
			LVO.setL_user(ID);
			request.setAttribute("likeInfo", LDAO.SelectOne(LVO)); // ���ƿ� ����
		}
		// ī�װ� �α��
		ArrayList<PostVO> categoryDatas = PDAO.SelectCategoryForViews(PVO);
		if(categoryDatas.size()!=0) {
			ArrayList<PostVO> sliceCategoryDatas = new ArrayList<PostVO>();
			for(int i=0;i<3;i++) {
				PostVO vo =new PostVO();
				vo = categoryDatas.get(i);
				sliceCategoryDatas.add(vo);
				if(categoryDatas.size()-1==i) {
					break;
				}
			}
			request.setAttribute("categoryDatas", sliceCategoryDatas);
		}
		action.setPath("selectOne.ucdo");
		action.setRedirect(false);
		return action;
	}

}
