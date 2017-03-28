package nsp;

public class Constraints {
	Nurse nurse;

	int nurseId;
	int shift;
	int[][] schedule;

	public Constraints() {

	}

	/*
	 * HELPER FUNCTIONS
	 */
	public int[] getNurseDaySchedule(int nurseId, int shift) {
		int[] nurseDayShedule = new int[4];
		int day = NurseCalculations.convertShiftToDay(shift);
		int firstShiftThatDay = NurseCalculations.getFirstShiftFromTheDay(day);

		nurseDayShedule[0] = schedule[nurseId][firstShiftThatDay];
		nurseDayShedule[1] = schedule[nurseId][firstShiftThatDay + 1];
		nurseDayShedule[2] = schedule[nurseId][firstShiftThatDay + 2];
		nurseDayShedule[3] = schedule[nurseId][firstShiftThatDay + 3];

		return nurseDayShedule;
	}

	public int getPreviousShift() {
		int[] nurseSchedule = schedule[nurseId];
		for (int i = shift - 1; i >= 0; i--) {
			if (nurseSchedule[i] == 1)
				return i;
		}

		return -1;
	}

	public boolean checkSchedule(int nurseId, int shift, int[][] schedule) {
		this.nurseId = nurseId;
		this.shift = shift;
		this.schedule = schedule;
		this.nurse = NurseManager.getNurse(nurseId);

		if (nurse.totalWorkedTime > nurse.hoursPerWeek * 4)
			return false;

		boolean h1 = !isNurseAlreadyWorkingToday();
		boolean h2 = isNumberOfNightShiftsLessOrEqualThanThree();
		boolean h3 = isNumberOfFreeWeekendsMoreOrEqualThenTwo();
		boolean h4 = enoughRestAfterConsecutiveNightShifts();
		boolean h5 = enoughRestIn24Hours();
		boolean h6 = enoughRestAfterNightShift();
		boolean h7 = consecutiveNightShiftsConstraint();
		boolean h8 = consecutiveWorkdaysConstraint();

		if (h1 && h2 && h3 && h4 && h5 && h6 && h7 && h8)
			return true;

		return false;
	}

	/*
	 * For each day a nurse may start only one shift
	 */
	public boolean isNurseAlreadyWorkingToday() {
		int[] nurseScheduleForTheDay = getNurseDaySchedule(nurseId, shift);
		for (int i = 0; i < 4; i++) {
			if (nurseScheduleForTheDay[i] == 1)
				return true;
		}

		return false;
	}

	/*
	 * The maximum number of night shifts is 3 per period of 5 consecutive
	 * weeks.
	 */
	public boolean isNumberOfNightShiftsLessOrEqualThanThree() {
		if (nurse.nightShiftsThisPeriod < 3)
			return true;

		return false;
	}

	/*
	 * A nurse must receive at least 2 weekends off duty per 5 week period. A
	 * weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00.
	 */
	public boolean isNumberOfFreeWeekendsMoreOrEqualThenTwo() {
		int freeWeekends = 4 - nurse.workingWeekends;

		if (freeWeekends >= 2)
			return true;

		return false;
	}

	/*
	 * Following a series of at least 2 consecutive night shifts a 42 hours rest
	 * is required.
	 */
	public boolean enoughRestAfterConsecutiveNightShifts() {
		int consecutiveNightShifts = nurse.consecutiveNightShifts;

		if (consecutiveNightShifts >= 2) {
			int previousShift = getPreviousShift();
			int daysBetween = NurseCalculations.convertShiftToDay(shift)
					- NurseCalculations.convertShiftToDay(previousShift);
			
			if (previousShift == -1)
				return true;

			if (daysBetween > 2)
				return true;
			else{
				int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
				if (rest >= 42)
					return true;
				else
					return false;
				
			}
		}

		return true;
	}

	/*
	 * During any period of 24 consecutive hours, at least 11 hours of rest is
	 * required.
	 */
	public boolean enoughRestIn24Hours() {
		int previousShift = getPreviousShift();
		int daysBetween = NurseCalculations.convertShiftToDay(shift)
				- NurseCalculations.convertShiftToDay(previousShift);

		// its going to be the first shift - no need to rest
		if (previousShift == -1)
			return true;

		if (daysBetween > 1)
			return true;

		else {
			int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
			if (rest >= 11)
				return true;
			else
				return false;
		}
	}

	/*
	 * A night shift has to be followed by at least 14 hours rest. An exception
	 * is that once in a period of 21 days for 24 consecutive hours, the resting
	 * time may be reduced to 8 hours.
	 */
	public boolean enoughRestAfterNightShift() {
		int previousShift = getPreviousShift();
		
		// its going to be the first shift - no need to rest
		if (previousShift == -1)
			return true;
		
		if(NurseCalculations.isNightShift(previousShift)){
			int restTime = NurseCalculations.timeBetweenShifts(previousShift, shift);
			if(restTime >= 14){
				return true;
			}
			else{
				if(nurse.exceptionForRestAfterNightShift == false){
					nurse.exceptionForRestAfterNightShift = true;
					return true;
				}
				return false;
			}
		}
		
		else
			return true;
	}
	
	/*
	 * The	number	of	consecutive	night	shifts	is	at	most	3.	
	 */

	public boolean consecutiveNightShiftsConstraint(){
		if(nurse.consecutiveNightShifts < 3)
			return true;
		else
			return false;
	}
	
	/*
	 * The	number	of	consecutive	shifts	(workdays)	is	at	most	6.	
	 */

	public boolean consecutiveWorkdaysConstraint(){
		if(nurse.consecutiveShifts < 6)
			return true;
		else
			return false;
	}
}
