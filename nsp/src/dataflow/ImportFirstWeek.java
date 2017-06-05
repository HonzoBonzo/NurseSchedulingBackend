package dataflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImportFirstWeek{

	int [][] schedule = new int[16][42 * 4];;
	
	public ImportFirstWeek(String filename) throws FileNotFoundException{
		Scanner scanner = new Scanner(new File(filename));
		
		for(int i=0; i<16; i++){
			for(int j =0; j<168; j++){
				if(j<28)
					schedule[i][j] = scanner.nextInt();
				else
					schedule[i][j] = 0;
			}
				
		}
	}
	
	
	public int[][] getSchedule(){
		return this.schedule;
	}
	
	
}
