package program;

import dataflow.ExportScheduleToHtml;
import nsp.Constraints;
import nsp.NurseManager;
import nsp.Schedule;

public class Program {

	public static void main(String[] args) throws Exception{
		Schedule schedule = new Schedule();
		NurseManager.initializeNurses();
		
		schedule.generateIndividual();
		ExportScheduleToHtml export = new ExportScheduleToHtml(schedule.getAllSchedule());
		
	}
}
