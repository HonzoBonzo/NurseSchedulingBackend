package nsp;

public class Nurse {
//xd
	 int id;
	 int hoursPerWeek;

	 int nightShiftThisWeekend;
	 public int nightShiftsThisPeriod;
	 int consecutiveNightShifts;
	 public boolean workedYesterday;

	 int consecutiveShifts;
	 int totalWorkedTime;
	 public int workingWeekends;
	
	 boolean exceptionForRestAfterNightShift;
	
	 boolean startedShiftToday;



	public Nurse(int id, int hoursPerWeek){
		this.id = id;
		this.hoursPerWeek = hoursPerWeek;
	}
	
	

	
}
