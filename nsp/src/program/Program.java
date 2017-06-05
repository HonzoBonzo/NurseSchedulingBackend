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
		
		
		
		
		while(!getResult.generateResult(5000, importFirstWeek.getSchedule() )){
			System.out.println("---------------");
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
