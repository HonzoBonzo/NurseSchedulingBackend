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

	public int getPenalty() {
		return constraint.penalty;
	}

	// konstruuje pusty harmonogram
	public Schedule() {
		schedule = new int[16][42 * 4];
		constraint = new Constraints();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 42 * 4; j++)
				schedule[i][j] = 0;
	}

	public Schedule(int[][] scheduleToCopy) {
		this.schedule = new int[16][42 * 4];
		constraint = new Constraints();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 42 * 4; j++){
				if(j<28)
					schedule[i][j] = scheduleToCopy[i][j];
				else
					schedule[i][j] = 0;
			}
					
		
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
		if (NurseCalculations.isFirstWeek(shift)) {
			nurse.hoursToSubtract += 8;
		}

		if (isWeekend) {
			if (!nurse.thisWeekend) {
				if (NurseCalculations.isFirstWeek(shift)) {
					nurse.workingWeekendsToSubtract++;
				}
				nurse.workingWeekends++;
				nurse.thisWeekend = true;
			}

		}

		if (isNightShift) {
			nurse.consecutiveNightShifts++;
			if (nurse.consecutiveNightShifts >= 2)
				nurse.notRestedAfterConsecutiveNights = true;
			if (NurseCalculations.isFirstWeek(shift)) {
				nurse.nightShiftsThisPeriodToSubtract++;
				nurse.nightShiftThisWeekendToSubtract++;
			}
			nurse.nightShiftsThisPeriod++;
			nurse.nightShiftThisWeekend++;
			// System.out.println("Inkrementuje dla nursa: " + nurseId + "
			// shift: " + shift + " nightSHiftsThisPeriod: "
			// + nurse.nightShiftsThisPeriod);
		}

		if ((shift % 28) > 18)
			// bo bedzie to pierwsza zmiana w weekend
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

	public ArrayList<Integer> clearNursesToCheckFirst(ArrayList<Integer> list) {
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

			// je�li nie pracowa�a wczoraj
			if (!checkIfNurseWorkedNightShift(nurseDay)) {
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

	public void clearNurseDataFromImportedWeek() {
		Nurse nurse;
		for (int i = 0; i < 16; i++) {
			nurse = NurseManager.getNurse(i);
			// TODO
			// test this
			// wyczyscic night shifts z tygodnia -1
			// wyczyscic working weekends z tygodnia -1
			nurse.nightShiftsThisPeriod -= nurse.nightShiftsThisPeriodToSubtract;
			nurse.nightShiftThisWeekend -= nurse.nightShiftThisWeekendToSubtract;
			nurse.workingWeekends -= nurse.workingWeekendsToSubtract;
			nurse.totalWorkedTime -= nurse.hoursToSubtract;
		}
	}

	public ArrayList<Integer> copyList(ArrayList<Integer> list) {
		ArrayList<Integer> listToReturn = new ArrayList<Integer>();
		for (Integer i : list) {
			listToReturn.add(i);
		}

		return listToReturn;
	}

	public void setFirstWeekSchedule() throws Exception {
		int nursesScheduledForTheDay = 0;

		for (int shift = 0; shift < 28; shift++) {
			// is new week?
			if (shift % 28 == 0) {

				clearNurseDataWeekly();
			}
			// is new day?
			if (shift % 4 == 0) {
				nursesScheduledForTheDay = 0;
				constraint.checkSoftConstraints();
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));
			}

			for (int nurseId = 0; nurseId < 16; nurseId++) {
					if (schedule[nurseId][shift] == 1) {
						setSchedule(nurseId, shift);
					}
				
			}
		}

	}

	public void testIndividual() throws Exception {
		int nursesScheduledForTheDay = 0;

		for (int shift = 0; shift < 42 * 4; shift++) {
			// is new week?
			if (shift % 28 == 0) {

				clearNurseDataWeekly();
			}
			// is new day?
			if (shift % 4 == 0) {
				nursesScheduledForTheDay = 0;
				constraint.checkSoftConstraints();
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));
			}

			// clear specific data from week -1
			if (shift == 140) {
				clearNurseDataFromImportedWeek();
			}

			for (int nurseId = 0; nurseId < 16; nurseId++) {

				if (schedule[nurseId][shift] == 1) {
					schedule[nurseId][shift] = 0;
					if (shift == 34 && nurseId == 15)
						System.out.println("");
					if (constraint.checkSchedule(nurseId, shift, schedule)) {
						setSchedule(nurseId, shift);
						nursesScheduledForTheDay++;
						// uda�o si� przydzieli� do tej zmiany tyle ile
						// potrzeba
						if (nursesScheduledForTheDay == NurseCalculations.getRequirementsForTheShift(shift)) {

							if (shift % 28 == 0) {
								constraint.checkSoftConstraints();
							}

							// przechodzimy do nast�pnej zmiany
							break;
						}
					}

					else
						throw new Exception("Nurse: " + nurseId + " shift: " + shift);
				}

			}

		}

	}

	// tworzy randomowego reprezentanta (randomowy harmonogram)
	public int generateIndividual() throws Exception {
		
		setFirstWeekSchedule();
		
		int failedAttemptsLvl1 = 0, failedAttemptsLvl2 = 0;
		int nursesScheduledForTheDay = 0;
		int lastScheduledNurse = 0;
		ArrayList<Integer> nursesNotChecked = null;
		ArrayList<Integer> nursesOnSaturday = null;
		ArrayList<Integer> nursesOnSaturdayCopy = new ArrayList<Integer>();
		ArrayList<Integer> nursesToCheckFirst = new ArrayList<Integer>();
		nursesToCheckFirst.add(new Integer(13));
		nursesToCheckFirst.add(new Integer(14));
		nursesToCheckFirst.add(new Integer(15));

		int nurseId = 0;

		for (int shift = 28; shift < 42 * 4; shift++) {
			nursesNotChecked = fillNursesNotCheckedList(nursesNotChecked);
			nursesOnSaturday = copyList(nursesOnSaturdayCopy);

			// is new week?
			if (shift % 28 == 0) {
				clearNurseDataWeekly();
				nursesOnSaturday = new ArrayList<Integer>();
				nursesOnSaturdayCopy = new ArrayList<Integer>();
			}

			// is new day?
			if (shift % 4 == 0) {
				constraint.checkSoftConstraints();
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));
			}

			// is a new scheduling period?
			if (shift % 140 == 0) {
				clearNurseDataFromImportedWeek();
			}

			lastScheduledNurse = 0;
			nursesScheduledForTheDay = 0;

			while (true) {

				if (NurseCalculations.checkIfItIsSunday(shift) && nursesOnSaturday.size() > 0) {
					// je�li to niedziela to najpierw spr�buj przydzieli�
					// t� z
					// soboty
					nurseId = NurseCalculations.randomNurseDraw(nursesOnSaturday);
					nursesOnSaturday.remove(new Integer(nurseId));
				}

				// tutaj wpadaj� zmiany dzienne z dni powszednich
				// nale�y unika� piel�gniarek part-time-job
				else {
					if (nursesNotChecked.size() > 0) {
						nurseId = NurseCalculations.randomNurseDraw(nursesNotChecked);
					} else
						return 0;

				}

				// je�li to zmiana nocna to uwa�aj na soft constraint 3
				if (NurseCalculations.isNightShift(shift)) {
					if (nursesToCheckFirst.size() > 0) {
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
					// TODO
					Nurse nurse = NurseManager.getNurse(nurseId);
					// je�li ma aktualnie jedn� zmian� nocn�, to wpisz
					// j� do listy piel�gniarek do sprawdzenia
					// �eby unikn�� pojedynczych zmian nocnych (soft
					// constraint 3)
					if (nurse.consecutiveNightShifts == 1) {
						if (nurse.hoursPerWeek > 30) {
							nursesToCheckFirst.add(new Integer(nurseId));
						}
					}

					nursesScheduledForTheDay++;
					failedAttemptsLvl1 = 0;

					// uda�o si� przydzieli� do tej zmiany tyle ile
					// potrzeba
					if (nursesScheduledForTheDay == NurseCalculations.getRequirementsForTheShift(shift)) {

						if (shift % 28 == 0) {
							constraint.checkSoftConstraints();
						}
						break;
					}
				}

				// dla tej piel�gniarki nie mo�na przydzieli� tej zmiany
				else {

					if (nursesNotChecked.size() == 0) {
						return 0;
					}

				}

			}

		}

		return 1;
	}

}
