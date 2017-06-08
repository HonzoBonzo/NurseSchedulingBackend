package program;

import dataflow.ExportScheduleToHtml;
import dataflow.ImportFirstWeek;
import nsp.Constraints;
import nsp.NurseManager;
import nsp.Schedule;

public class Program {
	
	

	public static void main(String[] args) throws Exception{
		

		GetSatisfyingResult getResult = new GetSatisfyingResult();
		int i = 0;
		long lStartTime =System.nanoTime();
		ImportFirstWeek importFirstWeek = new ImportFirstWeek("firstWeek.txt");
		ExportScheduleToHtml exportTest;
		Schedule scheduleTest;
		
		
		
		while(!getResult.generateResult(8000, importFirstWeek.getSchedule() )){
			System.out.println("---------------");
/*			scheduleTest = getResult.getSchedule();
			exportTest = new ExportScheduleToHtml(scheduleTest.getAllSchedule());
			exportTest.exportScheduleToHtml("schedule-" + scheduleTest.getPenalty() );
			exportTest.exportScheduleToTxt("tab");
			exportTest.exportResult(scheduleTest.getPenalty());*/
		}
		long lEndTime = System.nanoTime();
		long output = lEndTime - lStartTime;
		Schedule schedule = getResult.getSchedule();
		ExportScheduleToHtml export = new ExportScheduleToHtml(schedule.getAllSchedule());
		export.exportScheduleToHtml("schedule-" + schedule.getPenalty() );
		export.exportScheduleToTxt("tab");
		export.exportResult(schedule.getPenalty());
		System.out.println("Time: " + output / 1000000);
		System.out.println("Penalty: " + schedule.getPenalty());
		

	}


}
