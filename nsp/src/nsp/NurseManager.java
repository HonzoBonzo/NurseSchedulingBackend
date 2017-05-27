package nsp;

public class NurseManager {

	public static Nurse[] allNurses = new Nurse[16];
	
	public static void initializeNurses(){
		for(int i=0; i<12; i++)
			allNurses[i] = new Nurse(i, 36);
		
		allNurses[12] = new Nurse(12, 32);		
		allNurses[13] = new Nurse(13, 20);
		allNurses[14] = new Nurse(14, 20);
		allNurses[15] = new Nurse(15, 20);

	}
	
	public static Nurse getNurse(int index){
			return allNurses[index];

	}
}