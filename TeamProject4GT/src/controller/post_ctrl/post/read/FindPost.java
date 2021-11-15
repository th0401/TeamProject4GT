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

public class FindPost implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward action = new ActionForward();
		PostDAO PDAO = new PostDAO();
		// �˻� �� ����
		String word = (String)request.getParameter("findWord");
		String find = "%"+word+"%";
		String condition = (String)request.getParameter("condition");
		// �� �׸� ����¡ ó��
		String url="result.jsp";	
		String indexx=request.getParameter("index");
		int index=1;
		if(indexx!=null){
			index=Integer.parseInt(indexx);
		}
		//url �۾�
		url= url+ "?index="+index;
		// ����˻� ���
		ArrayList<PostVO> result = PDAO.searchPost(condition,find);
		Date now = new Date();
		DateSlice ds = new DateSlice(result, now, index);
		ArrayList<Integer> pagingIndex = new ArrayList<Integer>();
		Paging paging =null;
		// ���������� ���� �Խù� ��
		int pagingSize = 6;
		// ������ ������� int pageSize(�������� �Խù� ��), int thisPageNum(���� ������ ��ȣ), int totalPostCnt(�� ����Ʈ ����)
		paging = new Paging(pagingSize,index,result.size());
		paging.makePaging();
		// ����¡ for������ ǥ�� �� �� �ֵ��� �ϱ�
		int page = paging.getStartPageNum();
		//					1							5
		for(int i = paging.getStartPageNum();i<=paging.getEndPageNum();i++) {
			pagingIndex.add(page);
			page++;
		}

		// �ش��ϴ� �������� �����͸� �ֱ� ĸ��ȭ ����
		if(result.size()!=0) {
			ds.excuteSlice();
		}

		// condition �ѱ�ȭ
		if(condition.equals("title")) {
			condition = "����";
		}else if(condition.equals("writer")) {
			condition = "�ۼ���";
		}else if(condition.equals("content")) {
			condition = "����";
		}
		request.setAttribute("isLast", paging.isLast());
		request.setAttribute("isFirst", paging.isFirst());
		request.setAttribute("pagingIndex", pagingIndex);
		request.setAttribute("index", index);
		request.setAttribute("PostList", ds.getNewData());
		request.setAttribute("condition", condition);
		request.setAttribute("word", word);
		action.setPath("result.jsp");
		action.setRedirect(false);
		return action;
	}

}
