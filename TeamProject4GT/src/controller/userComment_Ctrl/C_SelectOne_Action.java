package controller.userComment_Ctrl;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import controller.ActionForward;
import model.comments.CommentsDAO;
import model.comments.CommentsSet;
import model.comments.CommentsVO;
import model.post.PostVO;

public class C_SelectOne_Action implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// view���� 3���� ������ ��� ����
		// �� ���� post  �� ���ƿ� ��  �� 1�� ��۸��  ---- ��, ��� post��Ʈ�� ���Լ� ����

		ActionForward forward = new ActionForward();

		// �� singlePost
		request.setAttribute("singlePost", request.getAttribute("singlePost"));
		System.out.println(request.getAttribute("singlePost"));
		// �� like
		request.setAttribute("likeInfo", request.getAttribute("likeInfo"));
		System.out.println( request.getAttribute("likeInfo"));


		// �� postOne_comments
		// post ��Ʈ�ѿ��� ���� �Խù� ������ �޾ƿ���,
		// DAO�� ���� �ش� �Խù��� ��� ����� AL�� �޾ƿ�  

		// VO DAO �ν��Ͻ�ȭ
		CommentsVO commentVO = new CommentsVO();
		CommentsDAO commentDAO = new CommentsDAO();

		// DAO���� �ʿ䵥���� SET
		commentVO.setC_post(((PostVO)request.getAttribute("singlePost")).getPnum());

		// DAO����
		ArrayList<CommentsSet> postOne_comments = commentDAO.getSetData(commentVO);
		int commentCnt = 5;			
		String commentCntt = request.getParameter("ccnt");
		if (commentCntt!=null&&commentCntt.length()!=0) {
			commentCnt = Integer.parseInt(commentCntt);
		}
		String url = "ShowPost.jsp?ccnt="+commentCnt;
		
		if(postOne_comments.size()!=0) {
			ArrayList<CommentsSet> sliceComSet = new ArrayList<CommentsSet>();
			for(int i=0;i<commentCnt;i++) {
				CommentsSet vo = new CommentsSet();
				vo = postOne_comments.get(i);
				sliceComSet.add(vo);
				if(i+1==postOne_comments.size()) {
					break;
				}
			}
			// ����� 1���� �������� �߶� ����� ������!
			request.setAttribute("postOne_comments", sliceComSet);
			request.setAttribute("ccnt", commentCnt);
			
			// ������ ���ۼ���
			forward.setRedirect(false); // forward
			forward.setPath(url);
			return forward;
		}


		// �� request ����	// ����� �ϳ��� ���� �� ��� ���� ����
		request.setAttribute("postOne_comments", postOne_comments);

		// ������ ���ۼ���
		forward.setRedirect(false); // forward
		forward.setPath(url);


		return forward;
	}

}
