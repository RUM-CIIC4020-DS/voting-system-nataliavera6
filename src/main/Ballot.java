package main;
import data_structures.ArrayList;
import interfaces.List;

public class Ballot {
	
	//stores ballot number
	private int BallotNum;

	//list of running candidates 
	private List<Candidate> candidates2=new ArrayList<>();
	
	//list of each rank, used to check ballot type
	private List<Integer> ranks = new ArrayList<>();
	
	/* Creates a ballot based on the line it receives. The format for line is
	id#,candidate_name . It also receives a List of all the candidates in the
	elections.*/
	public Ballot(String line, List<Candidate> candidates) {
		//Array to split ["285", "3:1" , "2:2", "7:3"]
		String[] tempArray = line.split(",");
		
		//first item is the ballot number
		BallotNum=Integer.valueOf(tempArray[0]);
		
		for(int s=1;s<tempArray.length;s++) {
			
			//split again by id and rank 
			String[] CanRank=tempArray[s].split(":");
			if(CanRank.length>=2) {
				//use id to add running candidates to candidates2
				int id = Integer.valueOf(CanRank[0]);
				candidates2.add(candidates.get(id-1));
				
				//use indexing to add their rank to ranks
				ranks.add(Integer.valueOf(CanRank[1]));
			}else {
				ranks.add(Integer.valueOf(CanRank[0]));
			}
			

		}
		
		
	
		
		
	}

	// Returns the ballot number
	public int getBallotNum() {
		return this.BallotNum;
	}
	
	//Returns the rank for that candidate, if no rank is available return -1
	public int getRankByCandidate(int candidateID) {
		for(int i=0;i<candidates2.size();i++) {
			if(candidates2.get(i).getId()==candidateID) {
				return i+1;
			}
			
		}
		return -1;
	}
	
	//Returns the candidate with that rank, if no candidate is available return -1.
	public int getCandidateByRank(int rank) {
		if(rank<candidates2.size()+1 && rank>0) {
			return candidates2.get(rank-1).getId();
		}
		
		return -1;
	}
	
	// Eliminates the candidate with the given id
	public boolean eliminate(int candidateId) {
		int rank=getRankByCandidate(candidateId);
		
		if(rank!=-1) {
			candidates2.get(rank-1).setActive(false);
			candidates2.remove(rank-1);
			ranks.remove(ranks.size()-1);
			return true;
		}
		return false;
	}
	
	/* Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 -
	invalid */
	public int getBallotType() {
		//if size==0, ballot is blanc
		if(candidates2.size()>0) {
			if(ranks.size()!=candidates2.size()) {return 2;}
			for(int i = 0;i<ranks.size();i++) {
				
				////checks that all ranks are not skipped and 
				if((ranks.get(i)!=i+1)) {return 2;} 
				for(int g=0;g<candidates2.size();g++) {
					if(candidates2.get(i).equals(candidates2.get(g))&&i!=g) {return 2;}
				}
			}
			return 0;
		}
			
		else{return 1;}
	
	}

}
