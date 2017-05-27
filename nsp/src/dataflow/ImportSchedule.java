package dataflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImportSchedule {

	int [][] schedule = new int[16][42 * 4];;
	
	public ImportSchedule(String filename) throws FileNotFoundException{
		Scanner scanner = new Scanner(new File(filename));
		
		for(int i=0; i<16; i++){
			for(int j =0; j<168; j++)
				schedule[i][j] = scanner.nextInt();
		}
	}
	
	
	public int[][] getSchedule(){
		return this.schedule;
	}
	
	
}
