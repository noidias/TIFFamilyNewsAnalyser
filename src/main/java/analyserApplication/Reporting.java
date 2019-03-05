package analyserApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Reporting {

	public static String printOpenRetakes(ArrayList<PlanetNews> retakesArray) {
		String outReport = "";
		outReport = appendString(outReport,"--------------------<br>-   OPEN RETAKES -<br>--------------------");
	for (PlanetNews retake : retakesArray) {
		outReport =  appendString(outReport, "<br>"+ retake.getPlanetCoords()+" (#"+retake.getEnemyFam()+", lost Tick "+retake.getTurnOccurred()+")");
		//System.out.println(retake.getPlanetCoords()+" (#"+retake.getEnemyFam()+", lost Tick "+retake.getTurnOccurred()+")");
		//System.out.println(retake.getPlanetCoords()+" (#"+retake.getEnemyFam()+", "+retake.getTurnOccurred()+" week(s) ago)");
		}
	//System.out.println("-------------------");
	//System.out.println(retakesArray.size() + " planet(s) missing in action");		
	return outReport;
	}
	
	public static String printOpenRetakesClean(ArrayList<PlanetNews> retakesArray) {
		String outReport = "";
		outReport = appendString(outReport,"--------------------<br>-   OPEN RETAKES Clean List   -<br>--------------------");
		/*
		System.out.println("--------------------\r\n" + 
			"-   OPEN RETAKES Clean List   -\r\n" + 
			"--------------------");*/
	int i = 0;
	for (PlanetNews retake : retakesArray) {
		outReport =  appendString(outReport, "<br>"+ retake.getPlanetCoords());
		//System.out.println(retake.getPlanetCoords()+" ");
		i++;

	}
	outReport =  appendString(outReport, "<br>-------------------<br> "+retakesArray.size() + " planet(s) missing in action");
	
	//System.out.println("");
	//System.out.println("-------------------");
	//System.out.println(retakesArray.size() + " planet(s) missing in action");	
	
	return outReport;
	}
		
	public static String printSummaryPlanets(ArrayList<PlanetNews> news, String text) {		
		String outReport = "";
		outReport = appendString(outReport,"--------------------<br>-   "+text+"   -<br>--------------------");
		//System.out.println("-------------------\r\n" + "-    "+text+"    -\r\n" + "-------------------");
		outReport = appendString(outReport,(countAndPrintFrequenciesPlanets(news, " planet(s) "+text)));
		outReport =  appendString(outReport, "<br>-------------------<br> "+news.size() + " planet(s) "+text+".<br>");
		//System.out.println("-------------------");
		//System.out.println(news.size() + " planet(s) "+text+".");
		return outReport;
	}
	
	public static void printSummaryAidSent(ArrayList<AidNews> news, String text) {		
		ArrayList<AidSummary> summary = new ArrayList<AidSummary>();
		System.out.println("-------------------\r\n" + "-    Aid Sent     -\r\n" + "-------------------");
		summary = AidNews.sumSentAid(news);
		
		int size = summary.size();
		if (size == 0) {
			
		}
		else {
		
		System.out.print(summary.get(0).getFamMember()+ " sent "+summary.get(0).getResource()+ " "+summary.get(0).getAmount());
		for (int x = 1; x < summary.size(); x++) {
			if (summary.get(x).getFamMember().equals(summary.get(x-1).getFamMember())) {
				System.out.print(" "+summary.get(x).getResource()+ " "+summary.get(x).getAmount());
			}
			else {
				System.out.print("\n"+summary.get(x).getFamMember()+ " sent "+summary.get(x).getResource()+ " "+summary.get(x).getAmount());
			}
		}
		}

	}
		
		public static void printSummaryAidReceived(ArrayList<AidNews> news, String text) {		
			ArrayList<AidSummary> receivedSummary = new ArrayList<AidSummary>();
			System.out.println("\r\n-------------------\r\n" + "-  Aid Received   -\r\n" + "-------------------");
			receivedSummary = AidNews.sumAidReceived(news);
			int size = receivedSummary.size();
			if (size == 0) {
				
			}
			else {
			
			System.out.print(receivedSummary.get(0).getFamMember()+ " received "+receivedSummary.get(0).getResource()+ " "+receivedSummary.get(0).getAmount());
			for (int x = 1; x < receivedSummary.size(); x++) {
				if (receivedSummary.get(x).getFamMember().equals(receivedSummary.get(x-1).getFamMember())) {
					System.out.print(" "+receivedSummary.get(x).getResource()+ " "+receivedSummary.get(x).getAmount());
				}
				else {
					System.out.print("\n"+receivedSummary.get(x).getFamMember()+ " received "+receivedSummary.get(x).getResource()+ " "+receivedSummary.get(x).getAmount());
				}
			}
			}
	}

	public static ArrayList<PlanetNews> findOpenRetakes(ArrayList<PlanetNews> captureArray, ArrayList<PlanetNews> defeatsArray) {
		Boolean match = false;
		ArrayList<PlanetNews> retakesArray = new ArrayList<PlanetNews>();
		for (PlanetNews defeats : defeatsArray) {
				for (PlanetNews captures : captureArray) {
					if (defeats.getPlanetCoords().equals(captures.getPlanetCoords()) && captures.getLineNumber() < defeats.getLineNumber()) {
						match = true;
						break;
						}
				}
				if (!match) {
					retakesArray.add(defeats);
				}		
				match = false;
			}
		return retakesArray;
	}	
	
	public static ArrayList<PlanetNews> findOutstandingBlowPLanets(ArrayList<PlanetNews> captureArray, ArrayList<PlanetNews> blownEaArray, ArrayList<PlanetNews> exploreArray) {
		Boolean match = false;
		ArrayList<PlanetNews> retakesArray = new ArrayList<PlanetNews>();
		for (PlanetNews defeats : blownEaArray) {
				for (PlanetNews captures : captureArray) {
					if (defeats.getPlanetCoords().equals(captures.getPlanetCoords()) && captures.getLineNumber() < defeats.getLineNumber()) {
						match = true;
						break;
						}					
					}
				for (PlanetNews expo : exploreArray) {
					if (defeats.getPlanetCoords().equals(expo.getPlanetCoords()) && expo.getLineNumber() < defeats.getLineNumber()) {
						match = true;
						break;
						}
				}
				if (!match) {
					retakesArray.add(defeats);
				}		
				match = false;
			}
		return retakesArray;
	}	

	public static String countAndPrintFrequenciesPlanets(ArrayList<PlanetNews> newsArray, String text) {
		String outReport = "";
		outReport = appendString(outReport,"--------------------<br>-   "+text+"   -<br>--------------------");
		
		
		Map<String, Integer> hm = new HashMap<String, Integer>();

		for (PlanetNews i : newsArray) {
			Integer j = hm.get(i.getFamMember());
			hm.put(i.getFamMember(), (j == null) ? 1 : j + 1);
		}
		// displaying the occurrence of elements in the arraylist
		for (Map.Entry<String, Integer> val : hm.entrySet()) {
			outReport = appendString(outReport,"<br>"+val.getValue() + " " + text + " " + "#" + val.getKey());
			//System.out.println(val.getValue() + " " + text + " " + "#" + val.getKey());
		}
		return outReport;
	}
	
	public static String printArray(ArrayList<PlanetNews> newsArray) {
		String outReport = "";
		outReport = appendString(outReport,"--------------------<br>- List of destroyed planets, not re-explored or retaken: ");
		//System.out.println("-------------------");
		//System.out.println("List of destroyed planets, not re-explored or retaken:");
		for (PlanetNews planetNews : newsArray) {
			outReport = appendString(outReport,"<br>"+planetNews.getPlanetCoords());
			//System.out.println(planetNews.getPlanetCoords());
		}
		return outReport;
	}
	
	public static String appendString(String text1, String text2) {
		//System.out.println(text2);
		String combinedText = text1 + text2;
		return combinedText;
	}
	

	
	
	
}
