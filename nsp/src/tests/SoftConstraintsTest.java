package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import nsp.Constraints;
import nsp.NurseCalculations;
import nsp.NurseManager;
import nsp.Schedule;

public class SoftConstraintsTest {

	
	Schedule schedule;
	Constraints constraint;

	@Before
	public void setUp() {
		schedule = new Schedule();
		constraint = new Constraints();
		NurseManager.initializeNurses();
	}
	
	@Test
	public void clearNurseDataFromImportedWeekTest(){
		schedule.setSchedule(1, 1);
		schedule.setSchedule(1, 4 );
		schedule.setSchedule(1, 9);
		schedule.setSchedule(1, 14);
		schedule.setSchedule(1, 20);
		schedule.setSchedule(1, 24);
		
		schedule.clearNurseDataFromImportedWeek();
		
		assertEquals(NurseManager.getNurse(1).totalWorkedTime, 0);
	}
	
	
	
	/*
	 * For the period of Friday 22:00 to Monday 0:00 a nurse should have either
	 * no shifts or at least 2 shifts (‘Complete Weekend’).
	 */
	@Test
	public void noShiftsOrAtLeastTwoShiftsOnWeekendsTest() {
		
		//1 zmiana w 'stan weekendowy'
		schedule.setSchedule(13, 19);
		//2
		schedule.setSchedule(13, 23);
		//3
		schedule.setSchedule(13, 27);
		
		constraint.checkSchedule(13, 28, schedule.getAllSchedule());
		assertEquals(0, constraint.noShiftsOrAtLeastTwoShiftsOnWeekends());
	
	}
	
	@Test
	public void noShiftsOrAtLeastTwoShiftsOnWeekendsTest2() {
		
		//1 zmiana w 'stan weekendowy'
		schedule.setSchedule(13, 77);
		//2
		schedule.setSchedule(13, 81);

		constraint.checkSchedule(13, 84, schedule.getAllSchedule());
		assertEquals(0, constraint.noShiftsOrAtLeastTwoShiftsOnWeekends());
	}
	
	@Test
	public void noShiftsOrAtLeastTwoShiftsOnWeekendsTest3() {
		
		//1 zmiana w 'stan weekendowy'
		schedule.setSchedule(13, 77);

		constraint.checkSchedule(13, 84, schedule.getAllSchedule());
		assertEquals(1000, constraint.noShiftsOrAtLeastTwoShiftsOnWeekends());
	}
	
	
	
	/*
	 * For	employees	with	availability	of	30-48	hours	per	
		week,	the	length	of	a	series	of	night	shifts	should	be	
		within	the	range	2-3.	It	could	be	before	another	series.
	 */
	@Test
	public void consecutiveNightShiftTest(){
		
		//1 zmiana nocna
		schedule.setSchedule(5, 3);
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(4));

		//2 zmiana nocna
		schedule.setSchedule(5, 7);
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(8));
		
		
		constraint.checkSchedule(5, 8, schedule.getAllSchedule());
		assertEquals(0, constraint.consecutiveNightShift());
	}
	
	@Test
	public void consecutiveNightShiftTest2(){
		
		//1 zmiana nocna
		schedule.setSchedule(5, 3);
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(4));

		//zmiana  
		schedule.setSchedule(5, 8);
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(9));

		constraint.checkSchedule(5, 8, schedule.getAllSchedule());
		assertEquals(1000, constraint.consecutiveNightShift());
	}
	
	
	@Test
	public void consecutiveNightShiftTest3(){
		
		//1 zmiana nocna
		schedule.setSchedule(5, 3);
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(4));

		//pielêgniarka nic nie robi w tym dniu
		schedule.clearNurseDataDaily(NurseCalculations.convertShiftToDay(8));

		constraint.checkSchedule(5, 8, schedule.getAllSchedule());
		assertEquals(1000, constraint.consecutiveNightShift());
	}
	
}
