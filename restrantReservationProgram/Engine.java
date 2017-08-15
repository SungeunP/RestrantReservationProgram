package restrantReservationProgram;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import restrantReservationProgram.*;


public class Engine {
	static int DBdateSeqCount = 1;
	static Scanner scan = new Scanner(System.in, "utf-8");
	static final int maxDateCount = 6; // 예약할 수 있는 날까지의 카운트
	static Calendar cal = new GregorianCalendar(); // 캘린더 객체
	static Calendar ccc = Calendar.getInstance();
	// static final int nowDate = cal.get(cal.DATE); // 오늘 날짜
	static final int nowDate = cal.get(cal.DATE);
	static final int nowMonth = cal.get(cal.MONTH) + 1; // 이번 달
	static final int possibleDateMax = nowDate + maxDateCount; // 예약 가능한 날 MAX
	static final int[] monthMax = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	static mysqlConnect connect = new mysqlConnect();
	static Menu menu = new Menu();

	public void Booking(){
		System.out.println("예약할 수 있는 기간은 1주 후 까지 이며 예약은 점심,저녁으로 할 수있습니다.");
		System.out.println("예약하시면 아래의 시간동안 자리를 예약할수있습니다.");
		System.out.println("점심 : 11:00 - 4:00   저녁 : 6:00 - 10:00");
		System.out.print("예약할 수 있는 날짜는 ");
		possibleDate();
		
		int day,lunchDinner,tableNumber;
		
		//일 입력받음
		System.out.print("예약할 일을 입력해주세요 (나가기-0) : ");
		while(true) {
			try{
				day = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("올바른 값을 입력해주세요");
				System.out.print("일 : ");
			}
		}
		CloseCheck(day);
		
		//시간대 입력받음
		System.out.print("에약할 시간대를 입력해주세요  ");
		System.out.print("1. 점심(11:00-4:00) 2. 저녁(6:00-10:00) (나가기-0) : ");
		while(true) {
			try{
				lunchDinner = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("올바른 값을 입력해주세요");
				System.out.print("시간대 1. 점심(11:00-4:00) 2. 저녁(6:00-10:00) (나가기-0) : ");
			}
		}
		CloseCheck(lunchDinner);
		
		//테이블 번호 입력받음
		System.out.print("예약할 테이블 번호을 입력해주세요 (1~8) (나가기-0) : ");
		while(true) {
			try{
				tableNumber = Integer.parseInt(scan.nextLine());
				break;
			}catch(Exception e){
				System.out.println("올바른 값을 입력해주세요");
				System.out.print("테이블 번호 (1~8) (나가기-0) : ");
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
				connect.DBSetting(); // db연결 세팅
				BookingDoing(day,nowMonth,lunchDinner,tableNumber);
			} else if (connect.connectAlready == 1) {
				BookingDoing(day, nowMonth, lunchDinner, tableNumber);
			}
		}else{
			System.out.println("**** 올바른 값을 입력해주세요 ****");
			Booking();
		}
	}

	public void DeleteBooking() {

		System.out.print("예약 취소할 일을 입력해주세요 : ");
		int DelDay = scanInt();
		
		System.out.print("1.점심  2.저녁 시간대가 언제인지 입력해주세요 : ");
		int DelLD = scanInt();
		
		System.out.print("몇 번째 테이블인지 입력해주세요 : ");
		int DelTable = scanInt();
		

		if (DelDay >= nowDate 
				&& DelDay <= possibleDateMax 
				&& DelTable >= 1 
				&& DelTable <= 8 
				&& DelLD >= 1
				&& DelLD <= 2) { // 값 검사
			if (connect.connectAlready == 0) {
				connect.DBSetting();
			}
			DelBookingDoing(DelDay,DelLD,DelTable);
		} else {
			System.out.println("올바른 값을 입력해주세요");
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
				
				if(RessLD==1){LD="점심";}else if(RessLD==2){LD="저녁";}else{ System.out.println("LD error");}
				
				if(RessDay < nowDate){
					DeleteSql="DELETE FROM Date WHERE Day="+RessDay+";";
					connect.stmt.executeUpdate(DeleteSql);
				}else{
				System.out.println(RessMonth+"월 "+RessDay+"일 "+LD+" "+RessTN+"번째 테이블");
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
				System.out.println(date+a +"일 : "+DaysRes[a]);
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
			System.out.println("이미 예약된 떄 입니다.");
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
					lunchDinnerString = "점심(11:00am - 3:00pm)";
				case 2:
					lunchDinnerString = "저녁(6:00pm - 10:00pm)";
				}
				System.out.println(nowMonth+"월 "+Day+"일 "+lunchDinnerString+"시간대 "+tableNumber+"번 테이블 예약이 완료되었습니다.");
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
						System.out.println("예약이 취소되었습니다.");
						Res = true;
						break;
				}
			}
			if(Res == false){
				System.out.println("예약된 날이 아닙니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void possibleDate() {
		if (possibleDateMax >= monthMax[nowMonth - 1]) {
			int nextMonth = nowMonth + 1;
			int nextDate = possibleDateMax - monthMax[nowMonth - 1];
			System.out.println(nowMonth + "월 " + nowDate + "일 부터 " + nextMonth + "월 " + nextDate + "일 까지 입니다.");
			System.out.println("이번 달의 마지막날은 " + monthMax[nowMonth - 1]);
		} else {
			System.out.println(nowMonth + "월 " + nowDate + "일 부터 " + possibleDateMax + "일 까지 입니다.");
		}
	}
	
	public void CloseCheck(int a/*메뉴 번호를 받아올 변수*/){
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
