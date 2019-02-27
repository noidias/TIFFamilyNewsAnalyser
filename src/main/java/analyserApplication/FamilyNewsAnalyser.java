package analyserApplication;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FamilyNewsAnalyser {
	//input file
	//static int i = 0;
	//Regex for extracting data
	static String playerNameRegex = "([\\w+\\s*\\w*]*)";
	static String planetRegex = " planet (\\d+) in the (\\d+),(\\d+) system";
	static String lineRegx = "(\\d+) ";
	static String eventTick = "(\\w+)[\\s]+"+lineRegx+"T-(\\d{1,4})[\\s]+";
	
	public static void main(String[] args) throws IOException {
		String famNews = readFileLineByLine("famNews5.txt");
		runFamNewsAnalyser(famNews);
		
		//famNews = addLineNumber(famNews);
		//reportPlanetSections();
		//reportAidSections();
	}
	
	public static String runFamNewsAnalyser(String famNews) {
		
		famNews = addLineNumber(famNews);
		String report = reportPlanetSections(famNews);
		reportAidSections(famNews);
		return report;
	}
	
		
	public static String reportPlanetSections(String famNews) {
		String report = "";
		ArrayList<PlanetNews> captureArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> blownSaArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> blownEaArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> defeatsArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> exploreArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> retakesArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> lostBlownArray = new ArrayList<PlanetNews>();
		ArrayList<PlanetNews> missingArray = new ArrayList<PlanetNews>();
		
		//Pattern explorePattern = Pattern.compile("(?s)"+lineRegx+eventTick+playerNameRegex+" explored"+planetRegex+"()()");		
		Pattern explorePattern = Pattern.compile("(?s)"+eventTick+playerNameRegex+" explored"+planetRegex+"()()");		
		Pattern capturePattern = Pattern.compile("(?s)"+eventTick+"The forces of "+playerNameRegex+" took"+planetRegex+" from "+playerNameRegex+" .(\\d+)+..");	
		Pattern defeatPattern = Pattern.compile("(?s)"+eventTick+"After a brave fight our family member "+playerNameRegex+" had to flee the planet"+planetRegex+" which was attacked by "+playerNameRegex+" of family (\\d+)+.");
		Pattern blownSAPattern = Pattern.compile("(?s)"+eventTick+playerNameRegex+" attacked "+playerNameRegex+" .(\\d+). on"+planetRegex+", and the heavy battle made the planet uninhabitable; an exploration ship will have to be sent there.");
		Pattern blownEAPattern = Pattern.compile("(?s)"+eventTick+"An overwhelming force from "+playerNameRegex+", family (\\d+) attacked "+playerNameRegex+"'s"+planetRegex+". The defenders for "+playerNameRegex+" managed to set off a nuclear blast which made the planet uninhabitable.");
		
		//explored
		exploreArray = ExtractData.extractPlanetData(explorePattern, famNews);
		String exploreReport = Reporting.printSummaryPlanets(exploreArray, "Explored");
		System.out.println(exploreReport);
		report = Reporting.appendString(report,exploreReport);
		
		//Capture
		captureArray = ExtractData.extractPlanetData(capturePattern, famNews);
		Reporting.printSummaryPlanets(captureArray, "Captures");
		
		//blow ups Attacks
		blownSaArray =ExtractData.extractDataBlownSA(blownSAPattern, famNews);
		Reporting.printSummaryPlanets(blownSaArray, "blow ups");
	
		//
		defeatsArray = ExtractData.extractPlanetData(defeatPattern, famNews);
		Reporting.printSummaryPlanets(defeatsArray, "Defeats");
		
		//blow ups defeats
		blownEaArray =ExtractData.extractDataBlownEA(blownEAPattern, famNews);
		lostBlownArray = Reporting.findOutstandingBlowPLanets(captureArray, blownEaArray, exploreArray);
		
		Reporting.printSummaryPlanets(blownEaArray, "blow ups lost");
		Reporting.printArray(lostBlownArray);
		if (lostBlownArray != null)
			missingArray.addAll( lostBlownArray );
		
		if (blownSaArray != null)
			captureArray.addAll( blownSaArray );
		
		Collections.sort(retakesArray);
		retakesArray = Reporting.findOpenRetakes(captureArray, defeatsArray);

		if (retakesArray != null)
			missingArray.addAll( retakesArray );
		
		
		Reporting.printSummaryPlanets(missingArray, "missing");
		Reporting.printOpenRetakes(retakesArray);
		
		String cleanRetakesReport = Reporting.printOpenRetakesClean(retakesArray);
		System.out.println(cleanRetakesReport);
		report = Reporting.appendString(report,cleanRetakesReport);
		
		
		return report;
		}
	

	public static void reportAidSections(String famNews) {
		ArrayList<AidNews> aidArray = new ArrayList<AidNews>();
		
		Pattern aidPattern1 = Pattern.compile("(?s)"+eventTick+"In the name of family cooperation "+playerNameRegex+" has sent a shipment of (\\d+) (\\w+) to "+playerNameRegex+".");
		Pattern aidPattern2 = Pattern.compile("(?s)"+eventTick+"In the name of family cooperation "+playerNameRegex+" has sent a shipment of (\\d+) (\\w+) (\\d+) (\\w+) to "+playerNameRegex+".");
		Pattern aidPattern3 = Pattern.compile("(?s)"+eventTick+"In the name of family cooperation "+playerNameRegex+" has sent a shipment of (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) to "+playerNameRegex+".");
		Pattern aidPattern4 = Pattern.compile("(?s)"+eventTick+"In the name of family cooperation "+playerNameRegex+" has sent a shipment of (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) to "+playerNameRegex+".");
		Pattern aidPattern5 = Pattern.compile("(?s)"+eventTick+"In the name of family cooperation "+playerNameRegex+" has sent a shipment of (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) (\\d+) (\\w+) to "+playerNameRegex+".");
		aidArray   = ExtractData.extractAid1(aidPattern1, famNews);
		aidArray.addAll(ExtractData.extractAid2(aidPattern2, famNews));
		aidArray.addAll(ExtractData.extractAid3(aidPattern3, famNews));
		aidArray.addAll(ExtractData.extractAid4(aidPattern4, famNews));
		aidArray.addAll(ExtractData.extractAid5(aidPattern5, famNews));
		
		Reporting.printSummaryAidSent(aidArray, "aid");
		Reporting.printSummaryAidReceived(aidArray, "aid");
	}


	
	private static String readFileLineByLine(String filePath)
	{
		//AtomicInteger i = new AtomicInteger();
  	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
	    {
	    	//stream.forEach(s -> contentBuilder.append(i.getAndIncrement()).append(" "+s).append("\n"));
	    	stream.forEach(s -> contentBuilder.append(" "+s).append("\n"));
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	public static String addLineNumber(String famNews) {		
		int count = 1;
		String[] lines = famNews.replace("T-","# T-").split("#");
		//String[] lines = famNews.split("T-");
		
		
		int rows = lines.length;
		String t = "";
        for (String line : lines) {
        	t = t + count++ +line;
        	}
		return t;
	}
	
}

   