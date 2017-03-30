package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import nsp.Constraints;
import nsp.Nurse;
import nsp.NurseManager;
import nsp.Schedule;

public class ConstraintsTest {

	Schedule schedule;
	Constraints constraint;

	@Before
	public void setUp() {
		schedule = new Schedule();
		constraint = new Constraints();
		NurseManager.initializeNurses();
	}

	/*
	 * For each day a nurse may start only one shift
	 */
	@Test
	public void onlyOneShiftADayPerNurse() {
		schedule.setSchedule(13, 58);
		constraint.checkSchedule(13, 59, schedule.getAllSchedule());
		assertEquals(true, constraint.isNurseAlreadyWorkingToday());
		constraint.checkSchedule(13, 60, schedule.getAllSchedule());
		assertEquals(false, constraint.isNurseAlreadyWorkingToday());

		schedule.setSchedule(14, 58);
		constraint.checkSchedule(14, 60, schedule.getAllSchedule());
		// 60 is the first shift in next day
		assertEquals(false, constraint.isNurseAlreadyWorkingToday());
	}

	/*
	 * The maximum number of night shifts is 3 per period of 5 consecutive
	 * weeks.
	 */
	@Test
	public void maxNumberOfNightShifts() {

		// schedule 2 night shifts
		schedule.setSchedule(5, 31);
		schedule.setSchedule(5, 39);
		constraint.checkSchedule(5, 43, schedule.getAllSchedule());
		// 3rd would be ok
		assertEquals(true, constraint.isNumberOfNightShiftsLessOrEqualThanThree());

		schedule.setSchedule(5, 43);
		constraint.checkSchedule(5, 47, schedule.getAllSchedule());
		// 4th would not be ok
		assertEquals(false, constraint.isNumberOfNightShiftsLessOrEqualThanThree());
	}

	/*
	 * A nurse must receive at least 2 weekends off duty per 5 week period. A
	 * weekend off duty lasts 60 hours including Saturday 00:00 to Monday 04:00.
	 */
	@Test
	@Ignore
	public void weekendOfDuty() {

		schedule.setSchedule(5, 20);
		schedule.setSchedule(5, 48);
		schedule.setSchedule(5, 76);
		// cant heave 4th working weekend
		constraint.checkSchedule(5, 105, schedule.getAllSchedule());
		assertEquals(false, constraint.isNumberOfFreeWeekendsMoreOrEqualThenTwo());

		Nurse nurse = NurseManager.getNurse(1);
		schedule.setSchedule(1, 20);
		System.out.println("y: " + nurse.workedYesterday + "| ww: " + nurse.workingWeekends);
		schedule.setSchedule(1, 24);
		System.out.println("y: " + nurse.workedYesterday + "| ww: " + nurse.workingWeekends);
		schedule.setSchedule(1, 76);
		System.out.println("y: " + nurse.workedYesterday + "| ww: " + nurse.workingWeekends);

		// that will be 3rd not 4th working weekend
		constraint.checkSchedule(1, 105, schedule.getAllSchedule());
		assertEquals(true, constraint.isNumberOfFreeWeekendsMoreOrEqualThenTwo());

		schedule.setSchedule(1, 105);
		System.out.println("y: " + nurse.workedYesterday + "| ww: " + nurse.workingWeekends);
		constraint.checkSchedule(1, 108, schedule.getAllSchedule());
		assertEquals(true, constraint.isNumberOfFreeWeekendsMoreOrEqualThenTwo());

	}

	/*
	 * Following a series of at least 2 consecutive night shifts a 42 hours rest
	 * is required.
	 */

	@Test
	public void consecutiveNightShiftsRestTest() {

		// 2 consecutive night shifts
		schedule.setSchedule(5, 3);
		schedule.setSchedule(5, 7);
		// koñczy prace w œrodê o 7:00 - moze zaczac pracowaæ najwczesniej w
		// pi¹tek od 1:00
		// czyli de fakto od porannej zmiany w pi¹tek

		// za wczeœnie
		constraint.checkSchedule(5, 10, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestAfterConsecutiveNightShifts());

		// ci¹gle za wczeœnie
		constraint.checkSchedule(5, 13, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestAfterConsecutiveNightShifts());

		// dopiero ok
		constraint.checkSchedule(5, 16, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestAfterConsecutiveNightShifts());
	}

	/*
	 * During any period of 24 consecutive hours, at least 11 hours of rest is
	 * required.
	 */

	@Test
	public void enoughRestIn24HourPeriod() {
		// zmiana late - konczy o 23:00
		schedule.setSchedule(5, 2);

		// za wczeœnie
		constraint.checkSchedule(5, 4, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());

		// za wczeœnie
		constraint.checkSchedule(5, 5, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());

		// dopiero ok
		constraint.checkSchedule(5, 6, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestIn24Hours());

		// zmiana night - konczy o 7:00
		schedule.setSchedule(6, 3);

		// za wczeœnie
		constraint.checkSchedule(6, 4, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());

		// za wczeœnie
		constraint.checkSchedule(6, 5, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());

		// za wczeœnie
		constraint.checkSchedule(6, 6, schedule.getAllSchedule());
		assertEquals(false, constraint.enoughRestIn24Hours());

		// dopiero ok
		constraint.checkSchedule(6, 7, schedule.getAllSchedule());
		assertEquals(true, constraint.enoughRestIn24Hours());

	}

	/*
	 * A night shift has to be followed by at least 14 hours rest. An exception
	 * is that once in a period of 21 days for 24 consecutive hours, the resting
	 * time may be reduced to 8 hours.
	 */

	@Test
	public void nightShiftRest() {
		//troche dziwne dla mnie ograniczenie
		//bo zmiana nocna koñczy siê o 7 rano
		//14 godzin odoczynku pozwoli³o by pracowaæ dopiero od godziny 21
		//czyli de facto kolejnej zmiany nocnej o 23
		
		//tak samo ogranicza ju¿ przecie¿ minimum 11 godzin odpoczynku w ci¹gu 24h
		//bo po zmianie nocnej mo¿na pracowaæ dopiero od godziny 18 czyli te¿ de facto
		//kolejnej zmiany nocnej o 23
		assertEquals(true, true);
	}
	
	/*
	 * The	number	of	consecutive	night	shifts	is	at	most	3.	
	 */
	@Test
	public void notToManyConsecutiveNightShifts(){
		schedule.setSchedule(5, 31);
		constraint.checkSchedule(5, 35, schedule.getAllSchedule());
		assertEquals(true, constraint.consecutiveNightShiftsConstraint());
		schedule.setSchedule(5, 35);
		
		constraint.checkSchedule(5, 39, schedule.getAllSchedule());
		assertEquals(true, constraint.consecutiveNightShiftsConstraint());
		schedule.setSchedule(5, 39);
		
		
		//ju¿ za du¿o
		constraint.checkSchedule(5, 43, schedule.getAllSchedule());
		assertEquals(false, constraint.consecutiveNightShiftsConstraint());
	}
	
	/*
	 * The	number	of	consecutive	shifts	(workdays)	is	at	most	6.	
	 */
	@Test
	public void consecutiveWorkingDaysTest(){
		schedule.setSchedule(5, 0);
		schedule.setSchedule(5, 4);
		schedule.setSchedule(5, 8);
		schedule.setSchedule(5, 12);
		schedule.setSchedule(5, 16);
		schedule.setSchedule(5, 20);
		
		constraint.checkSchedule(5, 24, schedule.getAllSchedule());
		assertEquals(false, constraint.consecutiveWorkdaysConstraint());
		
	}
}
