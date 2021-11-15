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
import model.post.Paging;
import model.post.PostDAO;
import model.post.PostVO;

public class MainAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward action = new ActionForward();
		PostDAO PDAO = new PostDAO();
		PostVO PVO = new PostVO();
		ArrayList<PostVO> datas = new ArrayList<PostVO>();
		// ����¡ ó�� ����
		String url="main.jsp";	
		String indexx=request.getParameter("index");
		int index=1;
		if(indexx!=null){
			index=Integer.parseInt(indexx);
		}
		url= url+ "?index="+index;

		datas = PDAO.SelectAll();
		// System.out.println("datas == "+datas);
		/*ArrayList<CommentsVO> CommentDatas = new ArrayList<CommentsVO>();
		CommentDatas = (ArrayList<CommentsVO>) request.getAttribute("CommentDatas");
		ArrayList<Integer> commentsCnt = new ArrayList<Integer>(); // ����Ʈ ����Ʈ�� ���� ũ�⸦ ���� ��ۼ� AL����
		for (int i = 0; i < datas.size(); i++) { // ��ۼ� AL 0���� �ʱ�ȭ
			commentsCnt.add(i, 0);
		}
		for (int i = 0; i < CommentDatas.size(); i++) {
			int index = (CommentDatas.get(i).getC_post() - 1);
			// System.out.println("index : "+index);
			commentsCnt.add(index, (commentsCnt.get(index) + 1)); // commentsCnt index = postnum - 1
		}*/	// ��ۼ� ���� -> 0927 model column �߰��� ����

		// ���������� ���� �Խù� ��
		int pagingSize = 6;
		// ������ ������� int pageSize(�������� �Խù� ��), int thisPageNum(���� ������ ��ȣ), int totalPostCnt(�� ����Ʈ ����)
		Paging paging = new Paging(pagingSize,index,datas.size());
		paging.makePaging();
		// ����¡ for������ ǥ�� �� �� �ֵ��� �ϱ�
		ArrayList<Integer> pagingIndex = new ArrayList<Integer>();// �信�� ��Ÿ���� ����¡ ���� ��ư���� ��ü
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
		request.setAttribute("isLast", paging.isLast());
		request.setAttribute("isFirst", paging.isFirst());
		request.setAttribute("pagingIndex", pagingIndex);
		request.setAttribute("index", index);
		request.setAttribute("PostList", ds.getNewData()); // PostList�� SelectAll �����͸� �ѱ�
		//System.out.println("mainActio ���� �����ִ� datas == "+datas);
		// request.setAttribute("commentsCnt", commentsCnt); // ��� �� AL�� �ѱ� 0�� �ε����� 1�� ����Ʈ�� ��� ���� �������! ��ۼ� ���� -> 0927 model column �߰��� ����
		action.setPath(url);
		action.setRedirect(false);
		return action;
	}

}
