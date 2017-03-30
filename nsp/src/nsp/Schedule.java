package nsp;

import java.awt.font.NumericShaper;

import dataflow.ExportScheduleToHtml;

public class Schedule {
	
	int [][]schedule;
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
	
	
	public boolean checkIfNurseWorkedNightShift(int[] nurseDaySchedule){
		return nurseDaySchedule[3] == 1;
	}
	
	public boolean checkIfNurseWorkedYesterday(int[] nurseDaySchedule){
		for(int i=0; i<4; i++)
			if(nurseDaySchedule[i] == 1)
				return true;
		
		return false;
	}
	
	public void setSchedule(int nurseId, int shift){
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
		for(int i=0; i<16; i++){
			nurse = NurseManager.getNurse(i);
			int []nurseDay = getNurseDaySchedule(i, day -1);
			
			//je�li nie pracowa�a wczoraj
			if(!checkIfNurseWorkedNightShift(nurseDay))
				nurse.consecutiveNightShifts = 0;
			
			if(checkIfNurseWorkedYesterday(nurseDay))
				nurse.workedYesterday = true;
			else
				nurse.workedYesterday = false;
				
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
			
			//is new day?
			if(shift % 4 == 0)
				clearNurseDataDaily(NurseCalculations.convertShiftToDay(shift));
			
			for(int nurseId=0; nurseId < 16; nurseId++){
				if(constraint.checkSchedule(nurseId, shift, schedule)){
					setSchedule(shift, nurseId);
					nursesScheduledForTheDay++;
					lastScheduledNurse = nurseId;
					
					if(nursesScheduledForTheDay == NurseCalculations.getRequirementsForTheShift(shift)){
						break;						
					}
				}

				else{
					nurseId = lastScheduledNurse + 1;
					failedAttemptsToSetNurse++;
					if(failedAttemptsToSetNurse > 100){
						ExportScheduleToHtml export = new ExportScheduleToHtml(getAllSchedule());
						export.exportScheduleToHtml();
						throw new Exception("Failed to generate. For nurse: " + nurseId + " shift: " + shift);
						
					}
						

				}
			}

		}
				
	}
	
	
	
	
	
}
