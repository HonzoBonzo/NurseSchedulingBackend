package dataflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class ExportScheduleToHtml {

	int [][] schedule;
	
	public ExportScheduleToHtml(int [][] schedule){
		this.schedule = schedule;
	}
	
	public int importBestResult() throws FileNotFoundException{
		Scanner scanner = new Scanner(new File("bestResult.txt"));
		return scanner.nextInt();
	}
	
	public void exportBestResult(int bestResult) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("bestResult.txt", "UTF-8");
		writer.print(bestResult);
		writer.close();
	}
	
	public void exportScheduleToHtml(String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename + ".html", "UTF-8");
		
		
		writer.println("<table>");
		
		for(int i=0; i<16; i++){
			writer.println("<tr><th>"+ i +".</th>" );
			for(int j=0; j<35*4; j++){
				if(j%4 == 3){
					if (schedule[i][j] == 1)
						writer.println("<th bgcolor=\"#FF0000\">" + schedule[i][j] + "</th>");
					else
						writer.println("<th bgcolor=\"#e5e1e0\">" + schedule[i][j] + "</th>");	
				}
					
				else{
					if (schedule[i][j] == 1)
						writer.println("<th bgcolor=\"#FF0000\">" + schedule[i][j] + "</th>");
					else
						writer.println("<th>" + schedule[i][j] + "</th>");	
				}
					
				
			
			}
			writer.println("</tr>");
		}
		
		writer.println("<tr><th>  </th>" );
		for(int j=0; j<35*4; j++){
			if(j%4 == 3)
				writer.println("<th bgcolor=\"#e5e1e0\">" + j + "</th>");	
			else
				writer.println("<th>" + j + "</th>");	
			
		
		}
		writer.println("</tr>");
		writer.println("</table>");
		writer.close();
	}
	
}
