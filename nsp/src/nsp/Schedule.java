package nsp;

import java.awt.font.NumericShaper;
import java.io.File;
import java.util.ArrayList;

import dataflow.ExportScheduleToHtml;
import program.Constants;
import program.Program;

public class Schedule {

	static int bestResult = 0;

	public int[][] schedule;
	int[][] scheduleBackupLvl1, scheduleBackupLvl2;
	double finess;
	int shiftBackupLvl2;

	Constraints constraint;
	
	public int getPenalty(){
		return constraint.penalty;
	}

	// konstruuje pusty harmonogram
	public Schedule() {
		schedule = new int[16][35 * 4];
		constraint = new Constraints();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 35 * 4; j++)
				schedule[i][j] = 0;
	}

	public void backupScheduleLvl1() {
		scheduleBackupLvl1 = new int[16][35 * 4];
		constraint = new Constraints();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 35 * 4; j++)
				scheduleBackupLvl1[i][j] = this.schedule[i][j];
	}

	public void backupNursesLvl1() {
		NurseManager.backupNursesLvl1();
	}

	public void backupScheduleLvl2() {
		scheduleBackupLvl2 = new int[16][35 * 4];
		constraint = new Constraints();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 35 * 4; j++)
				scheduleBackupLvl2[i][j] = this.schedule[i][j];
	}

	public void backupNursesLvl2() {
		NurseManager.backupNursesLvl2();
	}

	public Schedule(int[][] schedule) {
		this.schedule = schedule;
	}

	public int getScheduleInfo(int row, int col) {
		return this.schedule[row][col];
	}

	public int[][] getAllSchedule() {
		return this.schedule;
	}

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

	public int[] getNurseDayScheduleFromDay(int nurseId, int day) {
		int[] nurseDayShedule = new int[4];
		int firstShiftThatDay = NurseCalculations.getFirstShiftFromTheDay(day);

		nurseDayShedule[0] = schedule[nurseId][firstShiftThatDay];
		nurseDayShedule[1] = schedule[nurseId][firstShiftThatDay + 1];
		nurseDayShedule[2] = schedule[nurseId][firstShiftThatDay + 2];
		nurseDayShedule[3] = schedule[nurseId][firstShiftThatDay + 3];

		return nurseDayShedule;
	}

	public boolean checkIfNurseWorkedNightShift(int[] nurseDaySchedule) {
		return nurseDaySchedule[3] == 1;
	}

	public boolean checkIfNurseWorked(int[] nurseDaySchedule) {
		for (int i = 0; i < 4; i++)
			if (nurseDaySchedule[i] == 1)
				return true;

		return false;
	}

	public void setSchedule(int nurseId, int shift) {
		// System.out.println("\n+++ Nurse:" + nurseId + " shift: " + shift);

		Nurse nurse = NurseManager.getNurse(nurseId);
		boolean isWeekend = NurseCalculations.checkIfItIsTheWeekend(shift);
		boolean isNightShift = NurseCalculations.isNightShift(shift);

		int today = NurseCalculations.convertShiftToDay(shift);
		int yesterday = NurseCalculations.convertShiftToDay(shift) - 1;
		NurseCalculations.dayOfTheWeek(yesterday);

		nurse.consecutiveShifts++;

		
		nurse.totalWorkedTime += 8;

		if (isWeekend) {
			if (!nurse.thisWeekend) {
				nurse.workingWeekends++;
				nurse.thisWeekend = true;
			}

		}

		if (isNightShift) {
			nurse.consecutiveNightShifts++;
			if(nurse.consecutiveNightShifts >= 2)
				nurse.notRestedAfterConsecutiveNights = true;
			nurse.nightShiftsThisPeriod++;
			nurse.nightShiftThisWeekend++;
			// System.out.println("Inkrementuje dla nursa: " + nurseId + "
			// shift: " + shift + " nightSHiftsThisPeriod: "
			// + nurse.nightShiftsThisPeriod);
		}
		
		
		if((shift % 28) > 18)
			//bo bedzie to pierwsza zmiana w weekend
			nurse.softConstraint1++;

		nurse.workedYesterday = true;
		this.schedule[nurseId][shift] = 1;
	}

	public void clearNurseDataWeekly() {
		Nurse nurse;
		for (int i = 0; i < 16; i++) {
			nurse = NurseManager.getNurse(i);
			nurse.nightShiftThisWeekend = 0;
			nurse.thisWeekend = false;
			nurse.softConstraint1 = 0;
		}
	}

	public ArrayList<Integer> fillNursesNotCheckedList(ArrayList<Integer> nursesNotChecked) {
		nursesNotChecked = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++)
			nursesNotChecked.add(new Integer(i));

		return nursesNotChecked;
	}
	
	public ArrayList<Integer> clearNursesToCheckFirst(ArrayList<Integer> list){
		return new ArrayList<Integer>();
	}

	public void clearNurseDataDaily(int day) {
		Nurse nurse;
		if (day == 0)
			return;// nothing to check from yesterday
		for (int i = 0; i < 16; i++) {
			nurse = NurseManager.getNurse(i);
			// TODO czy dobrze?

			int[] nurseDay = getNurseDayScheduleFromDay(i, day - 1);

			// jeœli nie pracowa³a wczoraj
			if (!checkIfNurseWorkedNightShift(nurseDay)){
				nurse.lastConsecutiveNighShiftsSeries = nurse.consecutiveNightShifts;
				nurse.consecutiveNightShifts = 0;
			}
				

			if (checkIfNurseWorked(nurseDay))
				nurse.workedYesterday = true;
			else {
				nurse.workedYesterday = false;
				nurse.consecutiveShifts = 0;
			}

		}
	}

	public ArrayList<Integer> copyList(ArrayList<Integer> list) {
		ArrayList<Integer> listToReturn = new ArrayList<Integer>();
		for (Integer i : list) {
			listToReturn.add(i);
		}

		return listToReturn;
	}

	public void testIndividual() throws Exception {
		for (int shift = 0; shift < 35 * 4; shift++) {

			// is new week?
			if (shift % 28 == 0) {
				clearNurseDataWeekly();
			}

			// is new day?
			if (shift % 4 == 0)
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));

			for (int nurseId = 0; nurseId < 16; nurseId++) {
				if (schedule[nurseId][shift] == 1) {
					schedule[nurseId][shift] = 0;
					if(shift == 34 && nurseId == 15)
						System.out.println("");
					if (constraint.checkSchedule(nurseId, shift, schedule))
						setSchedule(nurseId, shift);
					
					else
						throw new Exception("Nurse: " + nurseId + " shift: " + shift);
				}


			}

		}

	}

	// tworzy randomowego reprezentanta (randomowy harmonogram)
	public void generateIndividual() throws Exception {
		int failedAttemptsLvl1 = 0, failedAttemptsLvl2 = 0;
		int nursesScheduledForTheDay = 0;
		int lastScheduledNurse = 0;
		ArrayList<Integer> nursesNotChecked = null;
		ArrayList<Integer> nursesOnSaturday = null;
		ArrayList<Integer> nursesOnSaturdayCopy = new ArrayList<Integer>();
		ArrayList<Integer> nursesToCheckFirst = new ArrayList<Integer>();
		

		int nurseId = 0;

		for (int shift = 0; shift < 35 * 4; shift++) {
			nursesNotChecked = fillNursesNotCheckedList(nursesNotChecked);
			nursesOnSaturday = copyList(nursesOnSaturdayCopy);

			// is new week?
			if (shift % 28 == 0) {
				clearNurseDataWeekly();
				nursesOnSaturday = new ArrayList<Integer>();
				nursesOnSaturdayCopy = new ArrayList<Integer>();
			}

			// is new day?
			if (shift % 4 == 0)
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));

			lastScheduledNurse = 0;
			nursesScheduledForTheDay = 0;

			while (true) {

				if (NurseCalculations.checkIfItIsSunday(shift) && nursesOnSaturday.size() > 0) {
					// jeœli to niedziela to najpierw spróbuj przydzieliæ tê z
					// soboty
					nurseId = NurseCalculations.randomNurseDraw(nursesOnSaturday);
					nursesOnSaturday.remove(new Integer(nurseId));
				}
				else {
					nurseId = NurseCalculations.randomNurseDraw(nursesNotChecked);
				}
				
				//jeœli to zmiana nocna to uwa¿aj na soft constraint 3
				if(NurseCalculations.isNightShift(shift)){
					if(nursesToCheckFirst.size() > 0){
						nurseId = NurseCalculations.randomNurseDraw(nursesToCheckFirst);
						nursesToCheckFirst.remove(new Integer(nurseId));
					}

				}

				nursesNotChecked.remove(new Integer(nurseId));
				if (constraint.checkSchedule(nurseId, shift, schedule)) {
					if (NurseCalculations.checkIfItIsSaturday(shift)) {
						nursesOnSaturday.add(new Integer(nurseId));
						nursesOnSaturdayCopy.add(new Integer(nurseId));
					}
					setSchedule(nurseId, shift);
					//TODO
					Nurse nurse = NurseManager.getNurse(nurseId);
					//jeœli ma aktualnie jedn¹ zmianê nocn¹, to wpisz j¹ do listy pielêgniarek do sprawdzenia
					//¿eby unikn¹æ pojedynczych zmian nocnych (soft constraint 3)
					if(nurse.consecutiveNightShifts == 1){
						nursesToCheckFirst.add(new Integer(nurseId));
					}
					
					nursesScheduledForTheDay++;
					failedAttemptsLvl1 = 0;

					// uda³o siê przydzieliæ do tej zmiany tyle ile potrzeba
					if (nursesScheduledForTheDay == NurseCalculations.getRequirementsForTheShift(shift)) {

						/*
						 * //System.out.println("Drukuje nowy harmonogram");
						 * ExportScheduleToHtml b1 = new
						 * ExportScheduleToHtml(getAllSchedule());
						 * b1.exportScheduleToHtml("in progress");
						 */

						// przechodzimy do nastêpnej zmiany
						break;
					}
				}

				// dla tej pielêgniarki nie mo¿na przydzieliæ tej zmiany
				else {

					if (nursesNotChecked.size() == 0) {
						ExportScheduleToHtml b1 = new ExportScheduleToHtml(getAllSchedule());

						int currentBest = b1.importBestResult();
						if (shift > currentBest) {
							b1.exportScheduleToHtml("results");
							b1.exportBestResult(shift);
						}

						throw new Exception("Program failed to generate individual this time at shift: " + shift +". Try again!");
					}
					failedAttemptsLvl1++;

					if (failedAttemptsLvl1 > 100000)
						System.exit(0);

				}

			}

		}

	}

}
