package tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


import nsp.Constraints;
import nsp.NurseManager;
import nsp.Schedule;

public class nurseDayScheduleTest {

	public void compareNurseDays(int []d1, int[] d2){
		assertEquals(d1.length, d2.length);
		for(int i=0; i<4; i++){
			assertEquals(d1[i], d2[i] );
		}
		
	}
	
	@Test
	public void checkNurseDay(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(0, 0);
		constraint.checkSchedule(0, 7, schedule.getAllSchedule());
		int nurseDay[] = constraint.getNurseDaySchedule(0, 1);
		int expectedNurseDay [] = {1,0,0,0};
		
		compareNurseDays(nurseDay, expectedNurseDay);
	}
	
	@Test
	public void checkNurseDay2(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(5, 5);
		constraint.checkSchedule(5, 7, schedule.getAllSchedule());
		int nurseDay[] = constraint.getNurseDaySchedule(5, 7);
		int expectedNurseDay [] = {0,1,0,0};
		
		compareNurseDays(nurseDay, expectedNurseDay);
	}
	
}
