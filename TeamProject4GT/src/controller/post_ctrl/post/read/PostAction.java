package controller.post_ctrl.post.read;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.comments.CommentsVO;
import model.post.Paging;
import model.post.PostDAO;
import model.post.PostVO;

public class PostAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward action = new ActionForward();
		PostDAO PDAO = new PostDAO();
		PostVO PVO = new PostVO();
		String initial = request.getParameter("category");
		String category = "";
		if(initial.equals("hot")) {
			category = "�α��";
		}else if(initial.equals("chi")) {
			category = "ġŲ";
		}else if(initial.equals("piz")) {
			category = "����";
		}else if(initial.equals("ham")) {
			category = "�ܹ���";
		}else if(initial.equals("kor")) {
			category = "�ѽ�";
		}else if(initial.equals("cha")) {
			category = "�߽�";
		}else if(initial.equals("jap")) {
			category = "�Ͻ�";
		}
		ArrayList<PostVO> datas = new ArrayList<PostVO>();
		if(category.equals("�α��")) {
			datas = PDAO.SelectViews();
		}else {
			PVO.setCategory(category);
			datas = PDAO.SelectCategory(PVO);
		}
		String url="ShowList.jsp";	
		String indexx=request.getParameter("index");
		int index=1;
		if(indexx!=null){
			index=Integer.parseInt(indexx);
		}
		url= url+ "?index="+index;

		int pagingSize = 6;
		// ������ ������� int pageSize(�������� �Խù� ��), int thisPageNum(���� ������ ��ȣ), int totalPostCnt(�� ����Ʈ ����)
		Paging paging = new Paging(pagingSize,index,datas.size());
		paging.makePaging();
		// ����¡ for������ ǥ�� �� �� �ֵ��� �ϱ�
		ArrayList<Integer> pagingIndex = new ArrayList<Integer>();
		int page = paging.getStartPageNum();
		//					1							5
		for(int i = paging.getStartPageNum();i<=paging.getEndPageNum();i++) {
			pagingIndex.add(page);
			page++;
		}

		// �ش��ϴ� �������� �����͸� �ֱ� ĸ��ȭ ����
		Date now = new Date();
		DateSlice ds = new DateSlice(datas, now, index);
		if(datas.size()!=0) {
			ds.excuteSlice();
		}
		request.setAttribute("category", category);
		request.setAttribute("isLast", paging.isLast());
		request.setAttribute("isFirst", paging.isFirst());
		request.setAttribute("pagingIndex", pagingIndex);
		request.setAttribute("index", index);
		request.setAttribute("PostList", ds.getNewData()); // PostList�� SelectAll �����͸� �ѱ�
		//System.out.println("mainActio ���� �����ִ� datas == "+datas);
		// request.setAttribute("commentsCnt", commentsCnt); // ��� �� AL�� �ѱ� 0�� �ε����� 1�� ����Ʈ�� ��� ���� �������! ��ۼ� ���� -> 0927 model column �߰��� ����
		System.out.println("showList�� ���� : "+url);
		action.setPath(url);
		action.setRedirect(false);
		return action;
	}

}
