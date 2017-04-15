package tests;

import org.junit.Test;

import dataflow.ExportScheduleToHtml;
import dataflow.ImportSchedule;
import nsp.NurseManager;
import nsp.Schedule;


public class ImortFromFileAndCheckTest {

	@Test
	public void testSchedule() throws Exception {
		Schedule schedule = new Schedule();
		NurseManager.initializeNurses();
		
		ImportSchedule importSchedule = new ImportSchedule("tab.txt");
		schedule.schedule = importSchedule.getSchedule();
		
/*		ExportScheduleToHtml export = new ExportScheduleToHtml(schedule.getAllSchedule());
		export.exportScheduleToHtml("allHardConstrainedChecked imported");
		System.out.println("Drukuje harmonogram");*/

		schedule.testIndividual();	
		
		System.out.println("Imported schedule looks fine to me!");
		System.out.println("Penalty: " + schedule.getPenalty());

	}

}
