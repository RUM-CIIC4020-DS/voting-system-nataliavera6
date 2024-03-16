package main;

public class Candidate {
	//Creates a Candidate from the line. The line will have the format
	//ID#,candidate_name .
	
	//candidate name 
	private String Name;
	
	//candidate id
	private int id;
	
	//boolean to set candidate to active by default
	private boolean active=true;
	
	// Creates a Candidate from the line. The line will have the format
	//ID#,candidate_name .
	public Candidate(String line) {
		
		//Array to split ["ID","candidate_name"]
		String[] nameArray=line.split(",");
		this.Name=nameArray[1];
		this.id=Integer.valueOf(nameArray[0]);
	}
	// returns the candidate’s id
	public int getId() {
		return this.id;
	}
	// Whether the candidate is still active in the election
	public boolean isActive() {
		return this.active;
	}
	// return the candidates name
	public String getName() {
		return this.Name;
	}
	
	//setter for boolean active, used when eliminating candidates in ballot class
	public void setActive(boolean activity) {
		this.active=activity;
	}

}
