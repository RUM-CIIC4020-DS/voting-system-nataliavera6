package main;

public class Candidate {
	//Creates a Candidate from the line. The line will have the format
	//ID#,candidate_name .
	private String Name;
	private int id;
	private boolean active=true;
	
	// Creates a Candidate from the line. The line will have the format
	//ID#,candidate_name .
	public Candidate(String line) {
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
	public void setActive(boolean activity) {
		this.active=activity;
	}

}
