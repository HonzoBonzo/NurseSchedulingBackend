package program;

import dataflow.ExportScheduleToHtml;
import nsp.NurseManager;
import nsp.Schedule;

public class GetSatisfyingResult {

	Schedule schedule;
	
	public boolean generateResult(int maximumPenalty, int[][] scheduleWithFirstWeek) throws Exception{
		this.schedule = new Schedule(scheduleWithFirstWeek);
		NurseManager.initializeNurses();
		if(schedule.generateIndividual() == 0)
			return false;
		
		if(schedule.getPenalty() <= maximumPenalty)
			return true;
		
		return false;
	}

	public int generateResult() throws Exception{
		schedule = new Schedule();
		NurseManager.initializeNurses();
		if(schedule.generateIndividual() == 0)
			return -1;
		
		return schedule.getPenalty();
				
	}
	
	public Schedule getSchedule(){
		return schedule;
	}
	
}
