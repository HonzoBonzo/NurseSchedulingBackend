package nsp;

public class NurseCalculations {

	
	
	public static int getRequirementsForTheShift(int shift){
		boolean isWeekend = checkIfItIsTheWeekend(shift);
		
		if(isWeekend){
			if(isNightShift(shift))
				return 1;
			else
				return 2;
		}
		
		else{
			if(isNightShift(shift))
				return 1;
			else
				return 3;
		}
	}
	
	public static int dayOfTheWeek(int day){
		return day % 7;
	}
	
	public static boolean checkIfItIsTheWeekend(int shift) {
		int weekNr = shift / 28;
		int value = shift % 28;
		if (value >= 20 && value < 28)
			return true;
		else
			return false;
	}

	public static int convertShiftToDay(int shift) {
		return shift / 4;
	}

	public static int getFirstShiftFromTheDay(int day) {
		return day * 4;
	}

	/*
	 * 0 - DAY (08:00 - 17:00) 1 - EARLY (07:00 - 16:00) 2 - LATE (14:00 -
	 * 23:00) 3 - NIGHT (23:00 - 07:00)
	 */
	public static int getShiftType(int shift) {
		return shift % 4;
	}
	
	public static boolean isNightShift(int shift){
		return (getShiftType(shift) == 3);
	}

	public static int startTimeForShift(int shiftType) {

		switch (shiftType) {
		case 0:
			return 8;
		case 1:
			return 7;
		case 2:
			return 14;
		default:
			return 23;
		}
	}

	public static int endTimeForShift(int shiftType) {

		switch (shiftType) {
		case 0:
			return 17;
		case 1:
			return 16;
		case 2:
			return 23;
		default:
			return 7;
		}
	}

	public static int timeBetweenShifts(int sh1, int sh2) {
		int shift1Type = getShiftType(sh1);
		int shift2Type = getShiftType(sh2);
		int fullDaysBetween = convertShiftToDay(sh2) - (convertShiftToDay(sh1) + 1);

		if (fullDaysBetween > 0) {
			if (shift1Type != 3 && shift2Type != 3)
				return (fullDaysBetween * 24) + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);

			if (shift1Type == 3 && shift2Type != 3)
				return (fullDaysBetween - 1) * 24 + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);

			if (shift1Type == 3 && shift2Type == 3)
				return (fullDaysBetween - 1) * 24 + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);
		}

		// == 0
		if (fullDaysBetween == 0) {
			if (shift1Type != 3 && shift2Type != 3)
				return (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);

			if (shift1Type == 3 && shift2Type != 3)
				return startTimeForShift(shift2Type) - endTimeForShift(shift1Type);

			if (shift1Type == 3 && shift2Type == 3)
				return startTimeForShift(shift2Type) - endTimeForShift(shift1Type);
		}



		return -1;

	}

}
