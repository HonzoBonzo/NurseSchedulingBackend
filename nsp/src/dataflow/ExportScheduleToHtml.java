package dataflow;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ExportScheduleToHtml {

	int [][] schedule;
	
	public ExportScheduleToHtml(int [][] schedule){
		this.schedule = schedule;
	}
	
	public void exportScheduleToHtml() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("results.html", "UTF-8");
		
		
		writer.println("<table>");
		
		for(int i=0; i<16; i++){
			writer.println("<tr>");
			for(int j=0; j<35*4; j++){
				writer.println("<th>" + schedule[i][j] + "</th>");				
			}
			writer.println("</tr>");
		}
		
		writer.println("</table>");
		writer.close();
	}
	
}
