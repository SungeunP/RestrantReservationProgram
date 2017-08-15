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
				System.out.println("�����մϴ�.");
				System.exit(0);
				break;
			default:
				System.out.println("�޴� �߿��� �������ּ���");
				break;
			}
		}
	}
	
	public static void listPrint(){
		System.out.print("1. �����ϱ�                         ");
		System.out.println("2. ��������ϱ�");
		System.out.print("3. �Ϻ� ����Ƚ�� ����           ");
		System.out.println("4. ��� �������� ����");
		System.out.println("0. �����ϱ�");
	}
	
	public static int scanNumber(){
		System.out.print("��ȣ�� �Է����ּ���  > ");
		menuNumber = scan.nextInt();
		
		return menuNumber;
	}
	
}
