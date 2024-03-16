package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;

import data_structures.ArrayList;
import interfaces.List;

public class Election {
	/* Constructor that implements the election logic using the files candidates.csv
	and ballots.csv as input. (Default constructor) */
	private List<Candidate>candidates=new ArrayList<>();

	//arraylist of all ballots
	private List<Ballot>ballots = new ArrayList<>();
	
	//arraylist of candidates that have been eliminated
	private List<String>eliminatedCandidates = new ArrayList<>();
	
	//list of amount of 1's candidate had when eliminated
	private List<Integer>eliminatedAmount = new ArrayList<>();
	
	//total amount of ballots
	private int ballotAmount=0;
	
	//amount of ballots that are valid
	private int validBallotAmount=0;
	
	//amount of ballots that are invalid
	private int invalidBallotAmount=0;
	
	//amount of ballots that are blanc
	private int blancBallotAmount=0;
	
	//name of winner for getWinner()
	String Winner=null;
	
	//maximum amount of 1's for getWinner()
	private int max=0;
	
	//minimum amount of 1's for getWinner()
	private int min=ballots.size();
	
	public Election() {
		//files to be used
		String candidatesFilePath ="inputFiles/candidates.csv";
		String ballotsFilePath ="inputFiles/ballots.csv";
		
		//try opening files 
		try (BufferedReader candidateRead = new BufferedReader(new FileReader(candidatesFilePath));
				BufferedReader ballotReader = new BufferedReader (new FileReader(ballotsFilePath))) {
			
			//read file line 
			String candidateLine=candidateRead.readLine();
			
			//add candidate 
			while(candidateLine!=null) {
				Candidate candidate = new Candidate(candidateLine);
				candidates.add(candidate);
				candidateLine=candidateRead.readLine();
			}
			
			/*make new ballot, count amount of the tree types (valid,invalid,blanc) 
			and add valid ballots to arraylist ballots*/
			String ballotLine=ballotReader.readLine();
			while(ballotLine!=null) {
				Ballot ballot = new Ballot(ballotLine,candidates);
				this.ballotAmount++;
				if(ballot.getBallotType()==0) {ballots.add(ballot);this.validBallotAmount++;}
				else if(ballot.getBallotType()==1) {this.blancBallotAmount++;}
				else if(ballot.getBallotType()==2) {this.invalidBallotAmount++;}
				ballotLine=ballotReader.readLine();
				
			}
		}catch(IOException error) {System.err.println(error.getMessage());}
		

	}
	/* Constructor that receives the name of the candidate and ballot files and applies
	the election logic. Note: The files should be found in the input folder. */
	
	public Election(String candidates_filename, String ballot_filename) {
		//files to be used 
		String candidatesFilePath ="inputFiles/"+candidates_filename;
		String ballotsFilePath ="inputFiles/"+ballot_filename;
		
		//try opening files 
		try (BufferedReader candidateRead = new BufferedReader(new FileReader( candidatesFilePath));
				BufferedReader ballotReader = new BufferedReader (new FileReader(ballotsFilePath))) {
			
			//read file line 
			String candidateLine=candidateRead.readLine();
			
			//add candidate 
			while(candidateLine!=null) {
				Candidate candidate = new Candidate(candidateLine);
				candidates.add(candidate);
				candidateLine=candidateRead.readLine();
			}
			
			/*make new ballot, count amount of the tree types (valid,invalid,blanc)
			and add valid ballots to arraylist ballots*/
			String ballotLine=ballotReader.readLine();
			while(ballotLine!=null) {
				Ballot ballot = new Ballot(ballotLine,candidates);
				ballotLine=ballotReader.readLine();
				this.ballotAmount++;
				if(ballot.getBallotType()==0) {ballots.add(ballot);this.validBallotAmount++;}
				else if(ballot.getBallotType()==1) {this.blancBallotAmount++;}
				else if(ballot.getBallotType()==2) {this.invalidBallotAmount++;}
			}
		}catch(IOException error) {System.err.println(error.getMessage());}
	}
	

	//function for resetting max amount of 1's and min amount of 1's after elimination
	public void MaxandMin(List<List<Ballot>> counter,int i) {
		this.max=0;
		this.min=ballots.size();
		for(int g=0;g<counter.size();g++) {
			if(counter.get(g).size()<this.min && candidates.get(g).isActive() && counter.get(g).size()>0) {
				this.min=counter.get(g).size();
			}
			else if(counter.get(g).size()>this.max && candidates.get(g).isActive()) {
				this.max=counter.get(g).size();
				
				if(this.max>ballots.size()/2) {
					this.Winner=candidates.get(g).getName();
				}
				
			}
		}
	}
	public String getWinner() {
		//list of lists, every candidate gets a list of ballots, that goes in the position of they're id-1
		List<List<Ballot>> counter=new ArrayList<>(candidates.size());
		
		//list with candidates that share the same amount of 1's
		List<Candidate> loseTie=new ArrayList<>();
		
		//list intented to place the amount of 2's and 3's and so on that the tied candidates have
		List<Integer> tieAmount=new ArrayList<>();
		

		//adding empty lists to counter
		for (int i=0;i<candidates.size();i++) {
			counter.add(i,new ArrayList<Ballot>());
		}
		
		/*filling empty counter lists with respective ballots:
				counter:
				0|->b1,b5,b6,b3
				1|->b4
				2|->b2*/
		for (int i=0;i<ballots.size();i++) {
			int c=ballots.get(i).getCandidateByRank(1);                                                                                                  
			counter.get(c-1).add(ballots.get(i));
		}
		
		this.max=0;
		this.min=ballots.size();
		
		//loops through every ballot and eliminates the losers when they are rank 1 
		for (int i=0;i<ballots.size();i++) {
			
			//loops through counter and determines the smallest/biggest 1's
			for(int g=0;g<counter.size();g++) {
				
				if(counter.get(g).size()<this.min && candidates.get(g).isActive() && counter.get(g).size()>0) {
					this.min=counter.get(g).size();
					loseTie.clear();
					loseTie.add(candidates.get(g));
					tieAmount.clear();
					tieAmount.add(0);
				}
				
				//if two candidates have the same amount of 1's, it adds them to lose tie and increases the size of tie amount
				else if(counter.get(g).size()==this.min && candidates.get(g).isActive()&&g>0) {
					loseTie.add(candidates.get(g));
					tieAmount.add(0);
				}
				else if(counter.get(g).size()>this.max && candidates.get(g).isActive()) {
					this.max=counter.get(g).size();
					
					//automatic win if one candidate has more than half the votes
					if(this.max>ballots.size()/2) {
						Winner=candidates.get(g).getName();
						return Winner;
						
					}
					
				}
			}
			
			/*loops through counter and 
					b1->Pepe=4>1 NO ELIMINATED
					b2->lola=1=1 SI ELIMINATED
					break*/
			while(counter.size()>eliminatedCandidates.size() && i<ballots.size()) {
				int c=ballots.get(i).getCandidateByRank(1);
				
				
				if(counter.get(c-1).size()<=this.min && candidates.get(c-1).isActive()) {
					//deals with tie between candidates with least amount of 1's
					int y=2;
					while(loseTie.size()==2 && loseTie.contains(candidates.get(c-1))&&y<=candidates.size()) {
						//for every ballot, adds amount of 2's to each loser candidate
						for(Ballot b: ballots) {
							if(b.getCandidateByRank(y)==loseTie.get(0).getId()) {
								tieAmount.add(0,tieAmount.get(0)+1);
							}
							if(b.getCandidateByRank(y)==loseTie.get(1).getId()) {
								tieAmount.add(1,tieAmount.get(1)+1);
							}
						}
						//if they still tie, repeats previous for but adds 3's and then 4's and so on
						if(tieAmount.get(0)==tieAmount.get(1)) {
							y++;
							continue;
						/*if they no longer tie
						it removes whoever has the least amount of the tie-breaking ranking
						from loseTie arrayList*/
						}else {
							int small=tieAmount.get(0);
							if(tieAmount.get(1)<small) {loseTie.remove(0);}
							else {loseTie.remove(1);}
							break;
						}
					}
					
					/*if tie-breaking loser isn't the current first rank, they get eliminated
					else, they would get eliminated anyway, so nothing happens*/
					if(loseTie.size()==1 && !loseTie.contains(candidates.get(c-1))&&y>candidates.size()) {
						int a=counter.get(loseTie.get(0).getId()-1).size();
						this.eliminatedCandidates.add(loseTie.get(0).getName()+"-"+Integer.toString(a));
					}

					// add eliminated candidate to elimination list
					int a=counter.get(c-1).size();
					if(!this.eliminatedCandidates.contains(candidates.get(c-1).getName())) {
						this.eliminatedCandidates.add(candidates.get(c-1).getName()+"-"+Integer.toString(a));
					}
					
					//move all eliminated candidate's ballots to second place rankings
					while(!counter.get(c-1).isEmpty()) {
						
						//find rank2 in each ballot of eliminated
						int secondPlace=counter.get(c-1).get(0).getCandidateByRank(2);
						this.eliminatedAmount.add(counter.get(c-1).size());
						
						//if rank2 is inactive, it finds candidate with rank3, and so on
						int n=3;
						while(!candidates.get(secondPlace-1).isActive()) {
							secondPlace=counter.get(c-1).get(0).getCandidateByRank(n);
							n=n+1;
						}
						
						//adding the ballot to rank to active candidate with next biggest rank
						counter.get(secondPlace-1).add(counter.get(c-1).get(0));
						
						//remove the ballot from eliminated candidate's list 
						counter.get(c-1).remove(counter.get(c-1).get(0));
						
						
					}
				
					

				
					
					
					//eliminate candidate from this ballot
					ballots.get(i).eliminate(c);
					
					//finds the new values of maximum 1's and minimum 1's
					MaxandMin(counter,i);
					
					//returns winner if they are found in MaxandMin()
					if(this.Winner!=null) {return Winner;}
					
					// breaks from loop and repeats process from beggining 
					break;
					
				}
				
				//if no one is eliminated, simply move on to the next ballot
				else if(counter.get(c-1).size()>this.min) {
					i++;
					
				}
			}
			
		}


//if no one ever gets more that 50% of votes 
		return "no winner";
	}

	

	// returns the total amount of ballots submitted
	public int getTotalBallots() {
		return this.ballotAmount;
	
	}
	// returns the total amount of invalid ballots
	public int getTotalInvalidBallots() {
		return this.invalidBallotAmount;
		
	}
	// returns the total amount of blank ballots
	public int getTotalBlankBallots() {
		return this.blancBallotAmount;
		
	}
	// returns the total amount of valid ballots
	public int getTotalValidBallots() {
		return this.validBallotAmount;
		
	}
	/* List of names for the eliminated candidates with the numbers of 1s they had,
	must be in order of elimination. Format should be <candidate name>-<number of 1s
	when eliminated>*/
	public List<String> getEliminatedCandidates() {
		return  this.eliminatedCandidates;
		
	}
	//BONO1
	public void printCandidates(Function<Candidate, String> func) {
		for(Candidate candidate:candidates) {
			System.out.println(func.apply(candidate));
		}
	}
	//BONO2
	public int countBallots(Function<Ballot, Boolean> func) {
		int n=0;
		for(Ballot b:ballots) {
			if (func.apply(b)) {n++;}
		}
		System.out.println(n);
		return n;
	}
	
	//OUTPUT FILES
	public void WriteFileResults() {
		String winner=getWinner();
		String outputTxt="outputFiles/"+"expected_output_"+winner.toLowerCase().replace(" ", "_")+max+".txt";

		try(BufferedWriter output=new BufferedWriter(new FileWriter(outputTxt))) {
			
			output.write("Number of ballots: "+String.valueOf(this.ballotAmount)+"\n");
			output.write("Number of blanc ballots: "+String.valueOf(this.blancBallotAmount)+"\n");
			output.write("Number of invalid ballots: "+String.valueOf(this.invalidBallotAmount)+"\n");
			for(int i=0;i<eliminatedCandidates.size();i++) {
				String w="Round "+String.valueOf(i+1)+": "+eliminatedCandidates.get(i);
				output.write(w+" was eliminated with "+eliminatedAmount.get(i)+" #1's"+"\n");
			}
			output.write("Winner: "+winner+" wins with "+max+" #1's"+"\n");
			output.close();
		
		
		} catch (IOException e) {
			System.err.println("Error writing output results: "+ e.getMessage());
			e.printStackTrace();
		}
	}

}