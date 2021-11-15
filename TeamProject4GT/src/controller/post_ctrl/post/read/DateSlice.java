package controller.post_ctrl.post.read;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.post.PostVO;

public class DateSlice {
	private ArrayList<PostVO> originData;
	private ArrayList<PostVO> newData;
	private Date today;
	private Date pdate;
	private int index;


	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ArrayList<PostVO> getOriginData() {
		return originData;
	}
	public void setOriginData(ArrayList<PostVO> originData) {
		this.originData = originData;
	}
	public ArrayList<PostVO> getNewData() {
		return newData;
	}
	public void setNewData(ArrayList<PostVO> newData) {
		this.newData = newData;
	}
	public Date getToday() {
		return today;
	}
	public void setToday(Date today) {
		this.today = today;
	}
	public Date getPdate() {
		return pdate;
	}
	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}

	public DateSlice(ArrayList<PostVO> originData, Date today, int index) {
		super();
		this.originData = originData;
		this.today = today;
		this.index = index;
		this.newData = new ArrayList<PostVO>();
	}

	public void excuteSlice() {
		for(int i=(this.index-1)*6; i<this.index*6; i++) { // ���� �ε����� -1*6~�����ε���*6 ������ �����͸� �Ѱ��ֱ�
			PostVO vo = this.originData.get(i);
			// vo�� �ִ� pdate
			String pdate = vo.getPdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String now = sdf.format(this.today);

			// ������ ��¥�� ���� ��¥ ���� Ÿ������ ��ȯ
			Date datePdate = null;
			Date dateToday = null;
			try {
				datePdate = sdf.parse(pdate);
				dateToday = sdf.parse(now);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ���� ���
			long diff = dateToday.getTime() - datePdate.getTime();
			long timeDif = diff / (60 * 60 * 1000); 

			vo.setNew(false);	// ����Ʈ false
			if(timeDif<24) {	// ����ð� - pdate �� 24�ð� ���ϸ� true
				vo.setNew(true);
			}
			// �Ѱ��� ��¥ �����̽�
			SimpleDateFormat sliceDate = new SimpleDateFormat("yyyy-MM-dd");
			pdate = sliceDate.format(datePdate);
			vo.setPdate(pdate);
			System.out.println("slicedata�� ���� data = "+vo);
			this.newData.add(vo);
			if(i==this.originData.size()-1) {// 6�� �̸��� ��� breakó��
				break;
			}
			
		}

	}
}
