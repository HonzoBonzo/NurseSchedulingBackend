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
	
	public int importResult() throws FileNotFoundException{
		Scanner scanner = new Scanner(new File("result.txt"));
		return scanner.nextInt();
	}
	
	public void exportResult(int result) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
		writer.print(result);
		writer.close();
	}
	
	public void exportScheduleToHtml(String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename + ".html", "UTF-8");
		
		
		writer.println("<table>");
		
		for(int i=0; i<16; i++){
			writer.println("<tr><th>"+ i +".</th>" );
			for(int j=0; j<42*4; j++){
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
		for(int j=0; j<42*4; j++){
			if(j%4 == 3)
				writer.println("<th bgcolor=\"#e5e1e0\">" + j + "</th>");	
			else
				writer.println("<th>" + j + "</th>");	
			
		
		}
		writer.println("</tr>");
		writer.println("</table>");
		writer.close();
	}
	
	
	public void exportScheduleToTxt(String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename + ".txt", "UTF-8");
		
		for(int i=0; i<16; i++){
			if(i != 0)
				writer.println("");
			for(int j =0; j<168; j++)
				writer.print(schedule[i][j] + " ");
		}
		
		writer.close();
	}
}
