package controller.userComment_Ctrl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class M_Mail  extends Authenticator{
	// ������ ���� ���̵� �Է�
	private PasswordAuthentication pa;
	
	// �߽�ó �̸��� �ۼ�
	public M_Mail() {
		String mail_id = "4grouptuna@gmail.com"; // �̸���
		String mail_pw = "vtzyrhamiqdoasgq";	// ��й�ȣ
		pa = new PasswordAuthentication(mail_id, mail_pw);
	}
	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
