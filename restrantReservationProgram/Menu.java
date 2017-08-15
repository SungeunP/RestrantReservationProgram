package restrantReservationProgram;

import java.util.*;

public class Menu {
	
	static Scanner scan = new Scanner(System.in);
	static Engine engine = new Engine();
	static int menuNumber;
	
	public void mainInstall(){
		menuSelect(-1);
	}
	
	public static void menuSelect(int menuNumber){
		
		while(menuNumber != 0){
			listPrint();
			int a = scanNumber();
			switch(a){
			case 1:
				engine.Booking();
				break;
			case 2:
				engine.DeleteBooking();
				break;
			case 3:
				engine.viewMonthBooking();
				break;
			case 4:
				engine.viewAllOfReservationInfo();
				break;
			case 0:
				System.out.println("종료합니다.");
				System.exit(0);
				break;
			default:
				System.out.println("메뉴 중에서 선택해주세요");
				break;
			}
		}
	}
	
	public static void listPrint(){
		System.out.print("1. 예약하기                         ");
		System.out.println("2. 예약취소하기");
		System.out.print("3. 일별 예약횟수 보기           ");
		System.out.println("4. 모든 예약정보 보기");
		System.out.println("0. 종료하기");
	}
	
	public static int scanNumber(){
		System.out.print("번호를 입력해주세요  > ");
		menuNumber = scan.nextInt();
		
		return menuNumber;
	}
	
}
