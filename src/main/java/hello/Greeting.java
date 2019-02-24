package hello;

import analyserApplication.FamilyNewsAnalyser;

public class Greeting {

    private String id;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
    	FamilyNewsAnalyser.runFamNewsAnalyser(id);
    	
    	
        this.id = id;
    }

    public String getContent() {    		
        return content;
    }

    public void setContent(String content) {
        
    	//content = " XXXYYY ";
    	FamilyNewsAnalyser.runFamNewsAnalyser(content);
    	this.content = content;
    }

}


