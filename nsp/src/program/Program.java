package program;

import dataflow.ExportScheduleToHtml;
import nsp.Constraints;
import nsp.NurseManager;
import nsp.Schedule;

public class Program {
	
	

	public static void main(String[] args) throws Exception{
		

		GetSatisfyingResult getResult = new GetSatisfyingResult();
		int i = 0;
		long lStartTime =System.nanoTime();

		
		while(!getResult.generateResult(9000)){
			//System.out.println("---------------");
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
		
/*		while(getResult.generateResult(10000) == false){
			i++;
			if(i % 1000000 == 0){
				System.out.println("Sleeping");
				Thread.sleep(20000);
			}
		}*/
		
		/*int flag = 0;
		while(flag < 200){
			
			if(getResult.generateResult() != -1){
				flag++;
				lEndTime = System.nanoTime();
				schedule = getResult.getSchedule();
				ExportScheduleToHtml export = new ExportScheduleToHtml(schedule.getAllSchedule());
				export.exportScheduleToHtml("allHardConstrainedChecked");
				//System.out.println("Drukuje harmonogram");
				
				export.exportScheduleToTxt("tab");
				//System.out.println("Zapisuje do pliku");
				output = lEndTime - lStartTime;
				lStartTime= System.nanoTime();
				System.out.println("Penalty: " + schedule.getPenalty());
				System.out.println("Iterations: " +i);
				System.out.println("Elapsed time in milliseconds: " + output / 1000000);
			}
				
			
		}*/
		
		
		

		
		
		
	}


}
