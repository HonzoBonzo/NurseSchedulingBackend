package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nsp.Constraints;
import nsp.NurseCalculations;
import nsp.NurseManager;
import nsp.Schedule;

public class checkWeekendsTest {

	
	
	@Test
	public void checkEnoughRest2(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(1, 3);
		constraint.checkSchedule(1, 7, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestIn24Hours());
	}
	
	@Test
	public void checkEnoughRest3(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(1, 3);
		constraint.checkSchedule(1, 6, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());
	}
	
	@Test
	public void checkEnoughRest4(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(1, 2);
		constraint.checkSchedule(1, 5, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());
	}
	
	@Test
	public void checkEnoughRest5(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(1, 2);
		constraint.checkSchedule(1, 6, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestIn24Hours());
	}
	
	@Test
	public void checkEnoughRest6(){
		Schedule schedule = new Schedule();
		Constraints constraint = new Constraints();
		NurseManager.initializeNurses();
		
		schedule.setSchedule(1, 0);
		constraint.checkSchedule(1, 122, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestIn24Hours());
	}

	
	@Test
	public void checkTimeBetweenShifts(){
		//more tests required
		
		assertEquals(15, NurseCalculations.timeBetweenShifts(0, 4)); //1
		assertEquals(64, NurseCalculations.timeBetweenShifts(1, 12)); //4
		assertEquals(40, NurseCalculations.timeBetweenShifts(3, 11)); //3
		assertEquals(16, NurseCalculations.timeBetweenShifts(59, 63)); //6
		assertEquals(48, NurseCalculations.timeBetweenShifts(87, 97)); //2
		assertEquals(0, NurseCalculations.timeBetweenShifts(87, 89)); //5
		
	}
	
	
	@Test
	public void checkShiftTypeConversion(){
		assertEquals(0, NurseCalculations.getShiftType(0));
		assertEquals(0, NurseCalculations.getShiftType(4));
		assertEquals(1, NurseCalculations.getShiftType(5));
		assertEquals(1, NurseCalculations.getShiftType(13));
		assertEquals(3, NurseCalculations.getShiftType(27));
		assertEquals(2, NurseCalculations.getShiftType(102));
		assertEquals(3, NurseCalculations.getShiftType(139));
	}
	
	@Test
	public void checkConvertShiftToDay(){
		assertEquals(0, NurseCalculations.convertShiftToDay(0));
		assertEquals(5, NurseCalculations.convertShiftToDay(23));
		assertEquals(6, NurseCalculations.convertShiftToDay(24));
		assertEquals(6, NurseCalculations.convertShiftToDay(27));
		assertEquals(7, NurseCalculations.convertShiftToDay(28));
		assertEquals(34, NurseCalculations.convertShiftToDay(139));
	}
	
	@Test
	public void checkFirstShiftFromDay(){
		assertEquals(0, NurseCalculations.getFirstShiftFromTheDay(0));
		assertEquals(4, NurseCalculations.getFirstShiftFromTheDay(1));
		assertEquals(24, NurseCalculations.getFirstShiftFromTheDay(6));
	}
	
	@Test
	public void checkWeekendDays(){
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(0));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(19));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(28));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(47));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(65));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(98));
		assertEquals(false, NurseCalculations.checkIfItIsTheWeekend(131));
		
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(20));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(27));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(48));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(55));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(106));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(137));
		assertEquals(true, NurseCalculations.checkIfItIsTheWeekend(139));
	}
	
	@Test
	public void checkNightShiftType(){
		assertEquals(false, NurseCalculations.isNightShift(2));
		assertEquals(false, NurseCalculations.isNightShift(4));
		assertEquals(false, NurseCalculations.isNightShift(89));
		assertEquals(false, NurseCalculations.isNightShift(116));
		
		assertEquals(true, NurseCalculations.isNightShift(3));
		assertEquals(true, NurseCalculations.isNightShift(115));
		assertEquals(true, NurseCalculations.isNightShift(127));
		assertEquals(true, NurseCalculations.isNightShift(15));
	}
}
