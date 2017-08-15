package restrantReservationProgram;

public class stmtQueryEXCEPTION extends RuntimeException {
	public stmtQueryEXCEPTION(){}
	public stmtQueryEXCEPTION(String printErrorString){
		super(printErrorString);
	}
}
