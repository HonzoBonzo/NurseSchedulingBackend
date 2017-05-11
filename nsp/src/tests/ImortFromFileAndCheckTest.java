package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dataflow.ExportScheduleToHtml;
import dataflow.ImportSchedule;
import nsp.NurseManager;
import nsp.Schedule;
import program.GetSatisfyingResult;


public class ImortFromFileAndCheckTest {

	int penalty = 0;
		
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
		this.penalty = schedule.getPenalty();
		System.out.println("Penalty: " + this.penalty);
		
		ExportScheduleToHtml export = new ExportScheduleToHtml(null);
		int expected = export.importResult();
		
		assertEquals(expected, this.penalty);
	}

}
