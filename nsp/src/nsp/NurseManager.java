package nsp;

public class NurseManager {

	public static Nurse[] allNurses = new Nurse[16];
	public static Nurse[] allNursesBackupLvl1, allNursesBackupLvl2;
	
	public static void initializeNurses(){
		for(int i=0; i<12; i++)
			allNurses[i] = new Nurse(i, 36);
		
		allNurses[12] = new Nurse(12, 32);		
		allNurses[13] = new Nurse(13, 30);
		allNurses[14] = new Nurse(14, 30);
		allNurses[15] = new Nurse(15, 30);

	}
	
	public static Nurse getNurse(int index){
			return allNurses[index];

	}
	
	public static void backupNursesLvl1(){
		allNursesBackupLvl1 = new Nurse[16];
		for(int i=0; i<12; i++)
			allNursesBackupLvl1[i] = allNurses[i].copyNurse();
		
		allNursesBackupLvl1[12] = allNurses[12].copyNurse();	
		allNursesBackupLvl1[13] =  allNurses[13].copyNurse();
		allNursesBackupLvl1[14] = allNurses[14].copyNurse();
		allNursesBackupLvl1[15] =  allNurses[15].copyNurse();
	}
	
	
	public static void backupNursesLvl2(){
		allNursesBackupLvl2 = new Nurse[16];
		for(int i=0; i<12; i++)
			allNursesBackupLvl2[i] = allNurses[i].copyNurse();
		
		allNursesBackupLvl2[12] = allNurses[12].copyNurse();	
		allNursesBackupLvl2[13] =  allNurses[13].copyNurse();
		allNursesBackupLvl2[14] = allNurses[14].copyNurse();
		allNursesBackupLvl2[15] =  allNurses[15].copyNurse();
	}
	
	
}
