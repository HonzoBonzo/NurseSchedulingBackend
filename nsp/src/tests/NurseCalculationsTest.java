package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import nsp.NurseCalculations;

public class NurseCalculationsTest {

	@Test
	public void randomNurseDraw(){
		
		int nursesRange1 = 0, nursesRange2 = 0, nursesRange3 = 0;
		
		for(int i=0; i<100000; i++){
			int nurseId = NurseCalculations.randomNurseDraw();
			if(nurseId < 5)
				nursesRange1++;
			if(nurseId >= 5 && nurseId < 10 )
				nursesRange2++;
			if(nurseId >= 10 && nurseId < 16)
				nursesRange3++;
			
			if(nurseId < 0 || nurseId > 15)
				fail();	
		}
	
		
		assertEquals(true, nursesRange1 > 20000 && nursesRange1 < 40000);
		assertEquals(true, nursesRange2 > 20000 && nursesRange2 < 40000);
		assertEquals(true, nursesRange3 > 20000 && nursesRange3 < 40000);
	}
	
	@Test
	public void randomNurseFromListTest(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(5);
		int nurseId = NurseCalculations.randomNurseDraw(list);
		assertEquals(5, nurseId);
				
		list.add(3);
		list.add(4);
		list.add(2);
		
		for(int i=0; i<100; i++){
			nurseId = NurseCalculations.randomNurseDraw(list);
			assertEquals(true, nurseId < 6 && nurseId > 1);
		}

	}
	
	@Test
	public void isTheNurseAlreadyAssigned(){
		
		int []nursesTab = {-1, -1, -1};
		assertEquals(false, NurseCalculations.isTheNurseAlreadyAssigned(4,nursesTab));
		assertEquals(false, NurseCalculations.isTheNurseAlreadyAssigned(2, nursesTab));
		
		int []nursesTab2 = {3, -1, -1};
		assertEquals(false, NurseCalculations.isTheNurseAlreadyAssigned(4, nursesTab2));
		assertEquals(true, NurseCalculations.isTheNurseAlreadyAssigned(3, nursesTab2));
		
		
		int []nursesTab3 = {3, 12, -1};
		assertEquals(false, NurseCalculations.isTheNurseAlreadyAssigned(5, nursesTab3));
		assertEquals(true, NurseCalculations.isTheNurseAlreadyAssigned(3, nursesTab3));
		assertEquals(true, NurseCalculations.isTheNurseAlreadyAssigned(12, nursesTab3));
		
	}
	
	@Test
	public void addNurseToAssignedNurses(){
		int []nursesTab = {-1, -1, -1};
		
		NurseCalculations.addNurseToAssignedNurses(13, nursesTab);
		int [] expectedArray = {13, -1, -1}; 
		assertArrayEquals(nursesTab, expectedArray);
		
		NurseCalculations.addNurseToAssignedNurses(12, nursesTab);
		int [] expectedArray2 = {13, 12, -1}; 
		assertArrayEquals(nursesTab, expectedArray2);
		
	}
	
	@Test
	public void clearTableContent(){
		int []nursesTab = {3,4,5};
		int [] expectedArray = {-1, -1, -1}; 
		NurseCalculations.clearTableContent(nursesTab);
		assertArrayEquals(nursesTab, expectedArray);
	}
	
	
	@Test
	public void isFirstWeekTest(){
		
		assertEquals(true, NurseCalculations.isFirstWeek(0));
		assertEquals(true, NurseCalculations.isFirstWeek(1));
		assertEquals(true, NurseCalculations.isFirstWeek(19));
		assertEquals(true, NurseCalculations.isFirstWeek(26));
		assertEquals(true, NurseCalculations.isFirstWeek(27));
		
		assertEquals(false, NurseCalculations.isFirstWeek(28));
		assertEquals(false, NurseCalculations.isFirstWeek(122));
		assertEquals(false, NurseCalculations.isFirstWeek(166));
		assertEquals(false, NurseCalculations.isFirstWeek(167));
		
		
	}

	
}
