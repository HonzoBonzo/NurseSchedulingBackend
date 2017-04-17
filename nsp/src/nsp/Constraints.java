package nsp;

public class Constraints {
	Nurse nurse;

	int nurseId;
	int shift;
	int[][] schedule;

	int penalty = 0;

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

		if (nurse.totalWorkedTime + 8 > nurse.hoursPerWeek * 5 + 4) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: godziny");
			return false;
		}

		if (isNurseAlreadyWorkingToday()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h1");
			return false;
		}

		if (!isNumberOfNightShiftsLessOrEqualThanThree()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h2");
			return false;
		}
		if (!isNumberOfFreeWeekendsMoreOrEqualThenTwo()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h3");
			return false;
		}
		if (!enoughRestAfterConsecutiveNightShifts()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h4");
			return false;
		}
		if (!enoughRestIn24Hours()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h5");
			return false;
		}
		if (!enoughRestAfterNightShift()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h6");
			return false;
		}
		if (!consecutiveNightShiftsConstraint()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h7");
			return false;
		}
		if (!consecutiveWorkdaysConstraint()) {
			// System.out.println("\nNurse:" + nurseId + " shift: " + shift +
			// "constraint: h8");
			return false;
		}

		/*
		 * boolean h1 = !isNurseAlreadyWorkingToday(); boolean h2 =
		 * isNumberOfNightShiftsLessOrEqualThanThree(); boolean h3 =
		 * isNumberOfFreeWeekendsMoreOrEqualThenTwo(); boolean h4 =
		 * enoughRestAfterConsecutiveNightShifts(); boolean h5 =
		 * enoughRestIn24Hours(); boolean h6 = enoughRestAfterNightShift();
		 * boolean h7 = consecutiveNightShiftsConstraint(); boolean h8 =
		 * consecutiveWorkdaysConstraint();
		 * 
		 * if (h1 && h2 && h3 && h4 && h5 && h6 && h7 && h8) return true;
		 */

		

		return true;
	}
	
	public void checkSoftConstraints(){
		// SOFT CONSTRAINT CHECK
		for(int i = 0; i< 16; i++){
			this.nurseId = i;
			this.nurse = NurseManager.getNurse(i);
			this.penalty += noShiftsOrAtLeastTwoShiftsOnWeekends();
			this.penalty += consecutiveNightShift();
		}
		
	}

	/*
	 * For each day a nurse may start only one shift
	 */
	public boolean isNurseAlreadyWorkingToday() {

		// if (nurseId == 9 && shift == 19)
		// System.out.println("");
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
	// TODO
	public boolean isNumberOfNightShiftsLessOrEqualThanThree() {
		if (NurseCalculations.isNightShift(shift)) {
			if (nurse.nightShiftsThisPeriod < 3)
				return true;

			return false;
		}

		return true;

	}

	/*
	 * A nurse must receive at least 2 weekends off duty per 5 week period. A
	 * weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00.
	 */
	public boolean isNumberOfFreeWeekendsMoreOrEqualThenTwo() {

		int previousShift = getPreviousShift();
		if (NurseCalculations.checkIfItIsTheWeekend(previousShift)) {
			if (shift - previousShift < 8) {
				// then it was the same weekend
				return true;
			}
		}

		if (NurseCalculations.checkIfItIsTheWeekend(shift)) {
			int freeWeekends = 4 - nurse.workingWeekends;

			if (freeWeekends >= 2)
				return true;

			return false;
		}

		return true;

	}

	/*
	 * Following a series of at least 2 consecutive night shifts a 42 hours rest
	 * is required.
	 */
	public boolean enoughRestAfterConsecutiveNightShifts() {
		int consecutiveNightShifts = nurse.consecutiveNightShifts;

		if (consecutiveNightShifts >= 2 || nurse.notRestedAfterConsecutiveNights == true) {
			int previousShift = getPreviousShift();
			int daysBetween = NurseCalculations.convertShiftToDay(shift)
					- NurseCalculations.convertShiftToDay(previousShift);

			if (previousShift == -1)
				return true;

			if (daysBetween > 2) {
				nurse.notRestedAfterConsecutiveNights = false;
				return true;
			}

			else {
				int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
				if (rest >= 42) {
					nurse.notRestedAfterConsecutiveNights = false;
					return true;
				}

				else
					return false;

			}
		}

		nurse.notRestedAfterConsecutiveNights = false;
		return true;
	}

	/*
	 * During any period of 24 consecutive hours, at least 11 hours of rest is
	 * required.
	 */
	/*
	 * public boolean enoughRestIn24Hours() { int previousShift =
	 * getPreviousShift(); int daysBetween =
	 * NurseCalculations.convertShiftToDay(shift) -
	 * NurseCalculations.convertShiftToDay(previousShift);
	 * 
	 * // its going to be the first shift - no need to rest if (previousShift ==
	 * -1) return true;
	 * 
	 * if (daysBetween > 1) return true;
	 * 
	 * else { int rest = NurseCalculations.timeBetweenShifts(previousShift,
	 * shift); if (rest >= 11) return true; else return false; } }
	 */

	public boolean enoughRestIn24Hours() {
		int previousShift = getPreviousShift();
		int rest = NurseCalculations.timeBetweenShifts(previousShift, shift);
		if (previousShift == -1)
			return true;

		if (rest == -1)
			try {
				// System.out.println("blaaaaaaaad");
				throw new Exception("blaaad");
			} catch (Exception e) {
				System.exit(0);

			}
		if (rest >= 11)
			return true;
		else
			return false;

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

		if (NurseCalculations.isNightShift(previousShift)) {
			int restTime = NurseCalculations.timeBetweenShifts(previousShift, shift);
			if (restTime >= 14) {
				return true;
			} else {
				if (nurse.exceptionForRestAfterNightShift == false) {
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
	 * The number of consecutive night shifts is at most 3.
	 */

	public boolean consecutiveNightShiftsConstraint() {
		if (nurse.consecutiveNightShifts < 3)
			return true;
		else
			return false;
	}

	/*
	 * The number of consecutive shifts (workdays) is at most 6.
	 */

	public boolean consecutiveWorkdaysConstraint() {
		if (nurse.consecutiveShifts < 6)
			return true;
		else
			return false;
	}

	/*
	 * 
	 * SOFT CONSTRAINTS
	 * 
	 * 
	 */

	/*
	 * For the period of Friday 22:00 to Monday 0:00 a nurse should have either
	 * no shifts or at least 2 shifts (‘Complete Weekend’).
	 */

	public int noShiftsOrAtLeastTwoShiftsOnWeekends() {

		int shiftsThisWeekTime = 0;
		// sprawdzane na poprzedni tydzieñ w pierwsz¹ zmianê nastêpnego tygodnia
		if (shift % 28 == 0 && shift != 0) {
			// pobierz ostatnie 9 zmian, czyli do zmiany nocnej w pi¹tek
			// w³¹cznie
			for (int i = 1; i < 10; i++) {
				if (schedule[nurseId][shift - i] == 1)
					shiftsThisWeekTime++;
			}
		}

		if (shiftsThisWeekTime == 1) {
			System.out.println("SC: 1. Nurse: " + nurseId + " shift: " + shift);
			return 1000;
		}

		return 0;

	}

	/*
	 * For employees with availability of 30-48 hours per week, the length of a
	 * series of night shifts should be within the range 2-3. It could be before
	 * another series.
	 */

	public int consecutiveNightShift() {	
		if(nurse.hoursPerWeek >= 30){
			if (nurse.lastConsecutiveNighShiftsSeries == 1) {
				System.out.println("SC: 3. Nurse: " + nurseId + "shift: " + shift);
				return 1000;
			}
		}

		return 0;
	}
	
	
	/*
	 * For	employees	with	availability	of	30-48	hours	per	
		week,	within	one	week	the	number	of	shifts	is	within	
		the	range	4- 5.
	 */

	
	
}
