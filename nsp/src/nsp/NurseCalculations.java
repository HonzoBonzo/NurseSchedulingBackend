package nsp;

import java.util.ArrayList;
import java.util.Random;

public class NurseCalculations {

	
	public static boolean isFirstWeek(int shift){
		int day = NurseCalculations.convertShiftToDay(shift);
		if(day < 7){
			return true;
		}
		
		
		return false;
	}
	
	public static int randomNurseDraw(){
		Random r = new Random();
		return r.nextInt(16);
	}
	
	public static int randomNurseDraw(ArrayList<Integer> list){
		
		Random r = new Random();
		return list.get(r.nextInt(list.size()));
	}
	
	public static boolean isTheNurseAlreadyAssigned(int nurseId, int[] nursesTab){
		for(int i : nursesTab){
			if(i == nurseId)
				return true;
		}
		return false;
	}
	
	public static int[] addNurseToAssignedNurses(int nurseId, int[] nursesTab){
		
		for(int i = 0; i < 3; i++){
			if(nursesTab[i] == -1){
				nursesTab[i] = nurseId;
				break;
			}
		}
		return nursesTab;
	}
	
	public static void clearTableContent(int [] nursesTab){
		nursesTab[0] = -1;
		nursesTab[1] = -1;
		nursesTab[2] = -1;
		
	}
	
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
	
	public static boolean checkIfItIsSaturday(int shift){
		int value = shift % 28;
		if (value >= 20 && value < 24)
			return true;
		else
			return false;
	}
	
	public static boolean checkIfItIsSunday(int shift){
		int value = shift % 28;
		if (value >= 24 && value < 28)
			return true;
		else
			return false;
	}
	
	public static boolean checkIfItIsTheWeekend(int shift) {
		int value = shift % 28;
		if (value >= 19 && value < 28)
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
			//1
			if (shift1Type != 3 && shift2Type != 3)
				return (fullDaysBetween * 24) + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);

			//2
			if (shift1Type == 3 && shift2Type != 3)
				return (fullDaysBetween - 1) * 24 + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);
			
			//3
			if (shift1Type != 3 && shift2Type == 3)
				return (fullDaysBetween) * 24 + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);

			//4
			if (shift1Type == 3 && shift2Type == 3)
				return (fullDaysBetween - 1) * 24 + (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);
		}

		// == 0
		if (fullDaysBetween == 0) {
			
			//5
			if (shift1Type != 3 && shift2Type != 3)
				return (24 - endTimeForShift(shift1Type)) + startTimeForShift(shift2Type);
			
			//6
			if (shift1Type == 3 && shift2Type != 3)
				return startTimeForShift(shift2Type) - endTimeForShift(shift1Type);

			//7
			if (shift1Type != 3 && shift2Type == 3)
				return startTimeForShift(shift2Type) - endTimeForShift(shift1Type) + 24;
			
			//8
			if (shift1Type == 3 && shift2Type == 3)
				return startTimeForShift(shift2Type) - endTimeForShift(shift1Type);
		}

		return -1;

	}

	

	
}
