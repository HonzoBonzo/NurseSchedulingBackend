package nsp;

import java.awt.font.NumericShaper;

import dataflow.ExportScheduleToHtml;

public class Schedule {
	
	int [][]schedule;
	int [][]scheduleBackup;
	double finess;
	
	Constraints constraint;
	
	//konstruuje pusty harmonogram
	public Schedule(){
		schedule = new int[16][35*4];
		constraint = new Constraints();
		for(int i=0; i<16; i++)
			for(int j=0; j<35*4; j++)
				schedule[i][j] = 0;
	}
	
	
	public void backupSchedule(){
		scheduleBackup = new int[16][35*4];
		constraint = new Constraints();
		for(int i=0; i<16; i++)
			for(int j=0; j<35*4; j++)
				scheduleBackup[i][j] = this.schedule[i][j];
	}
	
	public void backupNurses(){
		NurseManager.backupNurses();
	}
	
	public Schedule(int [][] schedule){
		this.schedule = schedule;
	}
	
	public int getScheduleInfo(int row, int col){
		return this.schedule[row][col];
	}
	
	public int[][] getAllSchedule(){
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
	
	public boolean checkIfNurseWorkedNightShift(int[] nurseDaySchedule){
		return nurseDaySchedule[3] == 1;
	}
	
	public boolean checkIfNurseWorked(int[] nurseDaySchedule){
		for(int i=0; i<4; i++)
			if(nurseDaySchedule[i] == 1)
				return true;
		
		return false;
	}
	
	public void setSchedule(int nurseId, int shift){
		System.out.println("\n+++ Nurse:" + nurseId + " shift: " + shift);
		
		Nurse nurse = NurseManager.getNurse(nurseId);
		boolean isWeekend = NurseCalculations.checkIfItIsTheWeekend(shift);
		boolean isNightShift = NurseCalculations.isNightShift(shift);
		
		int yesterday = NurseCalculations.convertShiftToDay(shift) - 1;
		NurseCalculations.dayOfTheWeek(yesterday);
		
		nurse.consecutiveShifts++;
		nurse.totalWorkedTime += 8;
		
		
		if(isWeekend){
			if(yesterday != 5)
				nurse.workingWeekends++;
			//jesli jest rowny 5, to wczoraj byl weekend i juz ta zmianna
			//nie powinna byc zwiekszana
		}
		
		if(isNightShift){
			nurse.consecutiveNightShifts++;		 
			nurse.nightShiftsThisPeriod++;
			nurse.nightShiftThisWeekend++;
			System.out.println("Inkrementuje dla nursa: " + nurseId + " shift: " +shift + " nightSHiftsThisPeriod: " +nurse.nightShiftsThisPeriod );
		}
		
		nurse.workedYesterday = true;
		this.schedule[nurseId][shift] = 1;
	}
	
	public void clearNurseDataWeekly(){
		Nurse nurse;
		for(int i=0; i<16; i++){
			nurse = NurseManager.getNurse(i);
			nurse.nightShiftThisWeekend = 0;
		}
	}
	
	public void clearNurseDataDaily(int day){
		Nurse nurse;
		if(day==0)
			return;//nothing to check from yesterday
		for(int i=0; i<16; i++){
			nurse = NurseManager.getNurse(i);
			//TODO czy dobrze?
			
			int []nurseDay = getNurseDayScheduleFromDay(i, day -1);
			
			//jeœli nie pracowa³a wczoraj
			if(!checkIfNurseWorkedNightShift(nurseDay))
				nurse.consecutiveNightShifts = 0;
			
			if(checkIfNurseWorked(nurseDay))
				nurse.workedYesterday = true;
			else{
				nurse.workedYesterday = false;
				nurse.consecutiveShifts = 0;
			}

				
		}
	}
	

	
	//tworzy randomowego reprezentanta (randomowy harmonogram)
	public void generateIndividual() throws Exception{
		int failedAttemptsToSetNurse = 0;
		int nursesScheduledForTheDay = 0;
		int lastScheduledNurse = 0;
		
		for(int shift=0; shift<35*4; shift++){
			
			//is new week?
			if(shift % 28 == 0)
				clearNurseDataWeekly();
			
			if(shift==28){
				System.out.println("wdwd");
			}
			
			//is new day?
			if(shift % 4 == 0)
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));

			if(shift==29){
				System.out.println("wdwd");
			}
			
			if(shift==11){
				System.out.println("wdwd");
			}
			lastScheduledNurse = 0;
			nursesScheduledForTheDay = 0;
			for(int nurseId=0; nurseId < 16; nurseId++){
				if(constraint.checkSchedule(nurseId, shift, schedule)){
					setSchedule(nurseId, shift);

					nursesScheduledForTheDay++;
					lastScheduledNurse = nurseId;
					ExportScheduleToHtml export = new ExportScheduleToHtml(getAllSchedule());
					export.exportScheduleToHtml();
					
					if(nursesScheduledForTheDay == NurseCalculations.getRequirementsForTheShift(shift)){
						backupSchedule();
						backupNurses();
						break;						
					}
					
					else{
						if(nurseId >= 15){
							nurseId = 0;
							System.out.println("Robie backup1 Nurse:" +nurseId + " shift: " + shift);
							NurseManager.allNurses = NurseManager.allNursesBackup;
							NurseManager.allNursesBackup = null;
						}
					}
				}

				else{

					if(nurseId >= 15){
						
						failedAttemptsToSetNurse++;
						nurseId = 0;
						//tutaj sie w koncu wywala
						System.out.println("Robie backup2 Nurse:" +nurseId + " shift: " + shift);
						NurseManager.allNurses = NurseManager.allNursesBackup;
						NurseManager.allNursesBackup = null;
					}
					if(failedAttemptsToSetNurse > 100){

						throw new Exception("Failed to generate. For nurse: " + nurseId + " shift: " + shift);
						
					}
						

				}
			}

		}
				
	}
	
	
	
	
	
}
