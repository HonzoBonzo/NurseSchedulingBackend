package nsp;

public class Nurse {

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
	
	public Nurse copyNurse(){
		Nurse copy = new Nurse(id, hoursPerWeek);
		
		copy.nightShiftThisWeekend = nightShiftThisWeekend;
		copy.nightShiftsThisPeriod = nightShiftsThisPeriod;
		copy.consecutiveNightShifts = consecutiveNightShifts;
		copy.workedYesterday = workedYesterday;
		copy.consecutiveShifts = consecutiveShifts;
		copy.totalWorkedTime = totalWorkedTime;
		copy.workingWeekends = workingWeekends;
		copy.exceptionForRestAfterNightShift = exceptionForRestAfterNightShift;
		copy.startedShiftToday = startedShiftToday;

		
		return copy;
	}

	
}
