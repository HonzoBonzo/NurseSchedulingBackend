package tests;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import dataflow.ExportScheduleToHtml;
import nsp.Constraints;
import nsp.NurseManager;
import nsp.Schedule;

public class ExportTest {

	@Test
	public void ScheduleExport() throws FileNotFoundException, UnsupportedEncodingException{
		Schedule schedule = new Schedule();
		ExportScheduleToHtml export = new ExportScheduleToHtml(schedule.getAllSchedule());
		export.exportScheduleToHtml();
	}
}
