package nsp;

public class Nurse {

	 int id;
	 public int hoursPerWeek;
	 
	 int nightShiftThisWeekend;
	 public int nightShiftsThisPeriod;
	 int consecutiveNightShifts;
	 int lastConsecutiveNighShiftsSeries = 0;
	 public boolean workedYesterday;
	 int consecutiveShifts;
	 public int totalWorkedTime;
	 public int workingWeekends;
	 boolean exceptionForRestAfterNightShift;
	 boolean startedShiftToday;

	 boolean thisWeekend;
	 boolean notRestedAfterConsecutiveNights;
	public int softConstraint1 = 0;
	
	int nightShiftsThisPeriodToSubtract = 0;
	int nightShiftThisWeekendToSubtract = 0;
	int workingWeekendsToSubtract = 0;
	int hoursToSubtract = 0;


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
