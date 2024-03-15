package main;
import data_structures.ArrayList;
import interfaces.List;
//import main.Candidate;
public class Ballot {
	//private String[][]lineArray;
	//private List<String>lineArray=new ArrayList<>();
	private int BallotNum;
	private int size;
	private List<Candidate> candidates2=new ArrayList<>();
	private List<Integer> ranks = new ArrayList<>();
	/* Creates a ballot based on the line it receives. The format for line is
	id#,candidate_name . It also receives a List of all the candidates in the
	elections.*/
	public Ballot(String line, List<Candidate> candidates) {//USATE CANDIDATES LIST EN VEZ DE LINEARRAY
		if(line.contains(",")) {this.BallotNum = Integer.valueOf(line.substring(0,line.indexOf(",")));}
		else {this.BallotNum = Integer.valueOf(line);}
		while(line.contains(",")) {

			int idStart = line.indexOf(",");
			int idEnd = line.indexOf(":");
			int id = Integer.valueOf(line.substring(idStart+1,idEnd));
			
			
			candidates2.add(candidates.get(id-1));
			
			line=line.replaceFirst(",", "!");
			
			if(line.contains(",")) {
				ranks.add(Integer.valueOf(line.substring(idEnd+1,line.indexOf(","))));
			}
			else {ranks.add(Integer.valueOf(line.substring(idEnd+1)));}
			
			line=line.replaceFirst(":", "?");
		}
		
		
		/*String[] tempArray = line.split(",");
		this.size=tempArray.length;
		//this.lineArray=new String[size][2];
		for(int i=0;i<tempArray.length;i++) {
			this.candidates.add(candidates.get(tempArray[i].split(":")[0]));
			//this.lineArray[i]=tempArray[i].split(":");
			this.lineArray.add(tempArray[i]);
		}*/
		
		
	}
	// Returns the ballot number
	public int getBallotNum() {
		
		//return Integer.valueOf(this.lineArray[0][0]);
		//return Integer.valueOf(this.lineArray.get(0));
		return this.BallotNum;
	}
	//Returns the rank for that candidate, if no rank is available return -1
	public int getRankByCandidate(int candidateID) {
		
		for(int i=0;i<candidates2.size();i++) {
			
			if(candidates2.get(i).getId()==candidateID) {
				
				return i+1;
				
			}
			
			/*if(Integer.valueOf(this.lineArray[i][0])==candidateID) {
				return Integer.valueOf(this.lineArray[i][1]);
			}*/
		}
		return -1;
	}
	//Returns the candidate with that rank, if no candidate is available return -1.
	public int getCandidateByRank(int rank) {
		//if(rank<this.size) {
			//return Integer.valueOf(this.lineArray[rank][0]);}
		if(rank<candidates2.size()+1 && rank>0) {
			return candidates2.get(rank-1).getId();
		}
		
		return -1;
	}
	// Eliminates the candidate with the given id
	public boolean eliminate(int candidateId) {
		int rank=getRankByCandidate(candidateId);
		if(rank!=-1) {
			/*for(int i=rank;i<this.size-1;i++) {
				this.lineArray[i][0]=this.lineArray[i+1][0];
			}
			this.lineArray[this.size-1]=null;*/
			//lineArray.
			candidates2.get(rank-1).setActive(false);
			candidates2.remove(rank-1);
			ranks.remove(ranks.size()-1);
			size--;
			return true;
		}
		return false;
	}
	/* Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 -
	invalid */
	public int getBallotType() {
		if(candidates2.size()>0) {
			for(int i = 0;i<ranks.size();i++) {
				if((ranks.get(i)!=i+1)) {return 2;} 
				for(int g=0;g<candidates2.size();g++) {
					if(candidates2.get(i).equals(candidates2.get(g))&&i!=g) {return 2;}
				}
			}
			return 0;
		}
			
		else{return 1;}
		/*if(this.lineArray.length>1) {
		for (int i=1;i<this.size;i++) {
			if(Integer.valueOf(lineArray[i][1])!=i) {return 2;}
			for(int g =1;g<lineArray.length;g++) {
				if(lineArray[i][0].equals(lineArray[g][0])&& g!=i){return 2;}
			}
		}*/

	}
	//sets rank-lo hize yo
	//public void setRank(int rank,)
}
