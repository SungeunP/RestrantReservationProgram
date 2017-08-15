package restrantReservationProgram;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import restrantReservationProgram.*;


public class Engine {
	static int DBdateSeqCount = 1;
	static Scanner scan = new Scanner(System.in, "utf-8");
	static final int maxDateCount = 6; // ������ �� �ִ� �������� ī��Ʈ
	static Calendar cal = new GregorianCalendar(); // Ķ���� ��ü
	static Calendar ccc = Calendar.getInstance();
	// static final int nowDate = cal.get(cal.DATE); // ���� ��¥
	static final int nowDate = cal.get(cal.DATE);
	static final int nowMonth = cal.get(cal.MONTH) + 1; // �̹� ��
	static final int possibleDateMax = nowDate + maxDateCount; // ���� ������ �� MAX
	static final int[] monthMax = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	static mysqlConnect connect = new mysqlConnect();
	static Menu menu = new Menu();

	public void Booking(){
		System.out.println("������ �� �ִ� �Ⱓ�� 1�� �� ���� �̸� ������ ����,�������� �� ���ֽ��ϴ�.");
		System.out.println("�����Ͻø� �Ʒ��� �ð����� �ڸ��� �����Ҽ��ֽ��ϴ�.");
		System.out.println("���� : 11:00 - 4:00   ���� : 6:00 - 10:00");
		System.out.print("������ �� �ִ� ��¥�� ");
		possibleDate();
		
		int day,lunchDinner,tableNumber;
		
		//�� �Է¹���
		System.out.print("������ ���� �Է����ּ��� (������-0) : ");
		while(true) {
			try{
				day = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("�ùٸ� ���� �Է����ּ���");
				System.out.print("�� : ");
			}
		}
		CloseCheck(day);
		
		//�ð��� �Է¹���
		System.out.print("������ �ð��븦 �Է����ּ���  ");
		System.out.print("1. ����(11:00-4:00) 2. ����(6:00-10:00) (������-0) : ");
		while(true) {
			try{
				lunchDinner = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("�ùٸ� ���� �Է����ּ���");
				System.out.print("�ð��� 1. ����(11:00-4:00) 2. ����(6:00-10:00) (������-0) : ");
			}
		}
		CloseCheck(lunchDinner);
		
		//���̺� ��ȣ �Է¹���
		System.out.print("������ ���̺� ��ȣ�� �Է����ּ��� (1~8) (������-0) : ");
		while(true) {
			try{
				tableNumber = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("�ùٸ� ���� �Է����ּ���");
				System.out.print("���̺� ��ȣ (1~8) (������-0) : ");
			}
		}
		CloseCheck(tableNumber);
		
		if (day >= nowDate 
				&& day <= possibleDateMax 
				&& lunchDinner >= 1 
				&& lunchDinner <= 2 
				&& tableNumber >= 1
				&& tableNumber <= 8 ) {
			if (connect.connectAlready == 0) {
				connect.DBSetting(); // db���� ����
				BookingDoing(day,nowMonth,lunchDinner,tableNumber);
			} else if (connect.connectAlready == 1) {
				BookingDoing(day, nowMonth, lunchDinner, tableNumber);
			}
		}else{
			System.out.println("**** �ùٸ� ���� �Է����ּ��� ****");
			Booking();
		}
	}

	public void DeleteBooking() {

		System.out.print("���� ����� ���� �Է����ּ��� : ");
		int DelDay = scanInt();
		
		System.out.print("1.����  2.���� �ð��밡 �������� �Է����ּ��� : ");
		int DelLD = scanInt();
		
		System.out.print("�� ��° ���̺����� �Է����ּ��� : ");
		int DelTable = scanInt();
		

		if (DelDay >= nowDate 
				&& DelDay <= possibleDateMax 
				&& DelTable >= 1 
				&& DelTable <= 8 
				&& DelLD >= 1
				&& DelLD <= 2) { // �� �˻�
			if (connect.connectAlready == 0) {
				connect.DBSetting();
			}
			DelBookingDoing(DelDay,DelLD,DelTable);
		} else {
			System.out.println("�ùٸ� ���� �Է����ּ���");
		}

	}
	
	public void viewAllOfReservationInfo(){
		
		PreparedStatement pstmt = null;
		String LD = null;
		
		if (connect.connectAlready == 0) {
			connect.DBSetting();
		}
		
		try {
			pstmt = connect.con.prepareStatement("SELECT Month,Day,LunchDinner,TableNumber FROM Date");
			ResultSet rs = pstmt.executeQuery();
			int RessMonth, RessDay, RessLD, RessTN;
			String DeleteSql="";
			while (rs.next()) {
				RessMonth = rs.getInt("Month");
				RessDay = rs.getInt("Day");
				
				RessLD = rs.getInt("LunchDinner");
				RessTN = rs.getInt("TableNumber");
				
				if(RessLD==1){LD="����";}else if(RessLD==2){LD="����";}else{ System.out.println("LD error");}
				
				if(RessDay < nowDate){
					DeleteSql="DELETE FROM Date WHERE Day="+RessDay+";";
					connect.stmt.executeUpdate(DeleteSql);
				}else{
				System.out.println(RessMonth+"�� "+RessDay+"�� "+LD+" "+RessTN+"��° ���̺�");
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	public void viewMonthBooking() {
		
		PreparedStatement pstmt = null;
		int[] DaysRes = {0,0,0,0,0,0,0};
		int date = nowDate;
		String DeleteSql="";
		if (connect.connectAlready == 0) {
			connect.DBSetting();
		}
		
		try {
			pstmt = connect.con.prepareStatement("SELECT Day FROM Date");
			ResultSet rs = pstmt.executeQuery();
			int ResDay = 0;
			
			while (rs.next()) {
				ResDay = rs.getInt("Day");
				if(ResDay < nowDate){
					DeleteSql="DELETE FROM Date WHERE Day="+ResDay+";";
					connect.stmt.executeUpdate(DeleteSql);
				}else if(ResDay == nowDate){
					DaysRes[0] +=1;
				}else if(ResDay == nowDate+1){
					DaysRes[1] +=1;
				}else if(ResDay == nowDate+2){
					DaysRes[2] +=1;
				}else if(ResDay == nowDate+3){
					DaysRes[3] +=1;
				}else if(ResDay == nowDate+4){
					DaysRes[4] +=1;
				}else if(ResDay == nowDate+5){
					DaysRes[5] +=1;
				}else if(ResDay == nowDate+6){
					DaysRes[6] +=1;
				}
			}
			for(int a=0;a<=6;a++){
				System.out.println(date+a +"�� : "+DaysRes[a]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public boolean CheckDay(int day, int month, int LD, int TN) {
		
		if (connect.connectAlready == 0) {
			connect.DBSetting();
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = connect.con.prepareStatement("SELECT Month,Day,LunchDinner,TableNumber FROM Date");
			ResultSet rs = pstmt.executeQuery();
			int RessMonth, RessDay, RessLD, RessTN;
			while (rs.next()) {
				RessMonth = rs.getInt("Month");
				RessDay = rs.getInt("Day");
				RessLD = rs.getInt("LunchDinner");
				RessTN = rs.getInt("TableNumber");
				if (month == RessMonth && day == RessDay && LD == RessLD && TN == RessTN) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public void BookingDoing(int Day,int Month,int lunchDinner,int tableNumber){
		boolean IsOverlapWhen = CheckDay(Day, nowMonth, lunchDinner, tableNumber);
		if (IsOverlapWhen) {
			System.out.println("�̹� ����� �� �Դϴ�.");
		} else {
			int LastSeq = 0;
			String sqlCommand;
			try {
				LastSeq = connect.GetLastSeqFromDatetable();
				if (LastSeq == 0) {
					sqlCommand = "insert into Date values(" + LastSeq + "," + nowMonth + "," + Day + ","
							+ lunchDinner + "," + tableNumber +");";
				} else {
					sqlCommand = "insert into Date values(" + LastSeq + "," + nowMonth + "," + Day + ","
							+ lunchDinner + "," + tableNumber +");";
				}
				connect.stmt.executeUpdate(sqlCommand);
				String lunchDinnerString= "";
				switch(lunchDinner){
				case 1:
					lunchDinnerString = "����(11:00am - 3:00pm)";
				case 2:
					lunchDinnerString = "����(6:00pm - 10:00pm)";
				}
				System.out.println(nowMonth+"�� "+Day+"�� "+lunchDinnerString+"�ð��� "+tableNumber+"�� ���̺� ������ �Ϸ�Ǿ����ϴ�.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	public void DelBookingDoing(int DelDay,int DelLunchDinner,int DelTableNumber) {
		
		int ResDay;
		int ResTable;
		int ResLD;
		boolean Res=false;
		try {
			PreparedStatement pstmt = null;
			pstmt = connect.con.prepareStatement("SELECT Day,TableNumber,LunchDinner FROM Date");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ResDay = rs.getInt("Day");
				ResTable = rs.getInt("TableNumber");
				ResLD = rs.getInt("LunchDinner");
				if (DelDay == ResDay && DelTableNumber == ResTable && DelLunchDinner == ResLD) {
						String delQuery = "DELETE FROM Date WHERE Day=" + ResDay + " AND TableNumber=" + ResTable + " AND LunchDinner=" + ResLD + ";";
						connect.stmt.executeUpdate(delQuery);
						System.out.println("������ ��ҵǾ����ϴ�.");
						Res = true;
						break;
				}
			}
			if(Res == false){
				System.out.println("����� ���� �ƴմϴ�.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void possibleDate() {
		if (possibleDateMax >= monthMax[nowMonth - 1]) {
			int nextMonth = nowMonth + 1;
			int nextDate = possibleDateMax - monthMax[nowMonth - 1];
			System.out.println(nowMonth + "�� " + nowDate + "�� ���� " + nextMonth + "�� " + nextDate + "�� ���� �Դϴ�.");
			System.out.println("�̹� ���� ���������� " + monthMax[nowMonth - 1]);
		} else {
			System.out.println(nowMonth + "�� " + nowDate + "�� ���� " + possibleDateMax + "�� ���� �Դϴ�.");
		}
	}
	
	public void CloseCheck(int a/*�޴� ��ȣ�� �޾ƿ� ����*/){
		if(a == 0){
			menu.menuSelect(-1);
		}
	}

	public int scanInt() {
		return (scan.nextInt());
	}

	public String scanString() {
		return scan.nextLine();
	}
}
