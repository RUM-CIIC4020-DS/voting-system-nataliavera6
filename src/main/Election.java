package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import interfaces.List;

public class Election {
	/* Constructor that implements the election logic using the files candidates.csv
	and ballots.csv as input. (Default constructor) */
	private List<Candidate>candidates=new ArrayList<>();
	private List<Candidate>winning=new ArrayList<>();
	private List<Ballot>ballots = new ArrayList<>();
	private List<String>eliminatedCandidates = new ArrayList<>();
	private List<String>ranked=new ArrayList<>();
	//private List<List<Ballot>> n= new ArrayList<>();
	private int ballotAmount=0;
	private int validBallotAmount=0;
	private int invalidBallotAmount=0;
	private int blancBallotAmount=0;
	String Winner=null;
	private int max=0;
	private int min=ballots.size();
	//PREGUNTA SI PUEDES CAMBIAR EL ORDEN DE LAS FUNCIONES
	
	public Election() {
		String candidatesFilePath ="inputFiles/candidates.csv";
		String ballotsFilePath ="inputFiles/ballots.csv";
		
		//check este try vs el otro
		
		try (BufferedReader candidateRead = new BufferedReader(new FileReader(candidatesFilePath));
				BufferedReader ballotReader = new BufferedReader (new FileReader(ballotsFilePath))) {
			String candidateLine=candidateRead.readLine();
			while(candidateLine!=null) {
				Candidate candidate = new Candidate(candidateLine);
				candidates.add(candidate);
				winning.add(candidate);
				candidateLine=candidateRead.readLine();
			}
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
		String candidatesFilePath ="inputFiles/"+candidates_filename;
		String ballotsFilePath ="inputFiles/"+ballot_filename;
		try {
			BufferedReader candidateRead = new BufferedReader(new FileReader( candidatesFilePath));
			BufferedReader ballotReader = new BufferedReader (new FileReader(ballotsFilePath));
			String candidateLine=candidateRead.readLine();
			while(candidateLine!=null) {
				Candidate candidate = new Candidate(candidateLine);
				candidates.add(candidate);
				candidateLine=candidateRead.readLine();
			}
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
	/*public int getMax(List<Integer> votes){
		int max=0;
		for(int i=0;i<votes.size();i++) {
			if(votes.get(i)>max) {max=votes.get(i);}
		}
		return max;
	}
	public int getMin(List<Integer> votes) {
		int min=candidates.size()+1;
		for(int i=0;i<votes.size();i++) {
			if(votes.get(i)<min) {min=votes.get(i);}
		}
		return min;
	}*/
/*	public void MaxMin(List<Integer>votes) {
		max=0;
		min=candidates.size()+1;
		for(int i=0;i<votes.size();i++) {
			if(votes.get(i)<this.min) {this.min=votes.get(i);}
			if(votes.get(i)>this.max) {this.max=votes.get(i);winning.clear();winning.add(candidates.get(i));}
			else if(votes.get(i)==this.max) {winning.add(candidates.get(i));}
		}
	}*/
	
	// returns the name of the winner of the election
	// FALTA: que hacer si dos tienen la misma cantidad de 1
	//que hacer con empate full	public void MaxMin(List<Integer>votes)
	public boolean MaxandMin(List<List<Ballot>> counter,int i) {
		this.max=0;
		this.min=ballots.size();
		for(int g=0;g<counter.size();g++) {
			if(counter.get(g).size()<this.min && candidates.get(g).isActive()) {
				this.min=counter.get(g).size();
			}
			
			else if(counter.get(g).size()>this.max) {
				this.max=counter.get(g).size();
				if(this.max>ballots.size()/2) {
					this.Winner=candidates.get(g).getName();
					return true;
				}
				
			}
			//System.out.println(eliminatedCandidates.size());
			//else if(counter.get(i).size()==min)
		}
		return false;
	}
	public String getWinner() {

		List<List<Ballot>> counter=new ArrayList<>(candidates.size());
		//List<Ballot> StandIn=new ArrayList<>();
//		counter:
//		0|->b1,b5,b6,b3
//		1|->b4
//		2|->b2
		for (int i=0;i<candidates.size();i++) {
			counter.add(i,new ArrayList<Ballot>());
		}
		for (int i=0;i<ballots.size();i++) {
			int c=ballots.get(i).getCandidateByRank(1)-eliminatedCandidates.size();
			counter.get(c-1).add(ballots.get(i));
			//System.out.println("b"+c+" "+counter.get(c-1).size());
			/*StandIn=counter.get(c-1);
			StandIn.add(ballots.get(i));
			System.out.println("b"+StandIn.size());
			counter.set(c-1,StandIn);*/
		}
		
//		max=4
//		min=1
		this.max=0;
		this.min=ballots.size();
		
		for (int i=0;i<ballots.size();i++) {

			for(int g=0;g<counter.size();g++) {
				
				if(counter.get(g).size()<this.min && candidates.get(g).isActive()) {
					this.min=counter.get(g).size();
				}
				
				else if(counter.get(g).size()>this.max) {
					this.max=counter.get(g).size();
					
					if(this.max>ballots.size()/2) {
						Winner=candidates.get(g).getName();
					//	System.out.println("max"+max+" winner"+Winner);
						return Winner;
						
					}
					
				}
				//System.out.println(eliminatedCandidates.size());
				//else if(counter.get(i).size()==min)
			}
//			b1->Pepe=4>1
//			b2->lola=1=1
//			break
			
			while(counter.size()>eliminatedCandidates.size() && i<ballots.size()) {
				int c=ballots.get(i).getCandidateByRank(1);
				int d=ballots.get(i).getCandidateByRank(1);
//				System.out.println("d"+d+"="+ ballots.get(i).getCandidateByRank(1)+"-"+eliminatedCandidates.size());
//				System.out.println("c"+c);
//				System.out.println("can"+candidates.get(c-1).getName()+" "+this.min+" "+counter.get(c-1).size());
//				System.out.println("b"+c+" "+counter.size());
				//falta verificar si el candidato eliminado tiene empate
				
				if(counter.get(d-1).size()<=this.min && candidates.get(c-1).isActive()) {
				//	System.out.println("eliminated"+candidates.get(c-1).getName());
					int a=counter.get(d-1).size();
					while(!counter.get(d-1).isEmpty()) {
						//rank2 in each ballot of eliminated
						//System.out.println(" size "+counter.get(d-1).size());
						//System.out.println("1");
						int n=3;
						/*List<Ballot> m=counter.get(d-1);
						Ballot t=m.get(0);
						System.out.println(a+" size= "+m.size());
						int secondPlace=t.getCandidateByRank(2);*/
						int secondPlace=counter.get(d-1).get(0).getCandidateByRank(2);
						while(!candidates.get(secondPlace-1).isActive()) {
							//System.out.println(candidates.get(counter.get(d-1).get(0).getCandidateByRank(n)-1).getName()+candidates.get(counter.get(d-1).get(0).getCandidateByRank(n)-1).isActive());
							secondPlace=counter.get(d-1).get(0).getCandidateByRank(n);
							//System.out.println("n"+n);
							n=n+1;
							
						}
						
						//adding the ballot to rank two
						//System.out.println("2");
						counter.get(secondPlace-1).add(counter.get(d-1).get(0));
						//remove the ballot from eliminated
						//System.out.println("a ");
						counter.get(d-1).remove(counter.get(d-1).get(0));
						//a++;
						
					}
					//remove Person from counter
					//counter.remove(counter.get(d-1));
					//add to eliminated list
					//System.out.println("3");
					this.eliminatedCandidates.add(candidates.get(c-1).getName()+"-"+Integer.toString(a));
					//System.out.println(" c"+eliminatedCandidates.get(eliminatedCandidates.size()-1));
					
					//eliminate candidate
					//for(Candidate j:candidates) {System.out.println()
					ballots.get(i).eliminate(c);
					MaxandMin(counter,i);
					if(this.Winner!=null) {/*System.out.println("winner" + this.Winner);*/return Winner;}
					break;
					
				}
				else if(counter.get(d-1).size()>this.min) {
					i++;
					
				}
			}
			
		}
/*		min=counter.get(0).size();
		for (int i =0;i<counter.size();i++) {
			if(counter.get(i).size()>validBallotAmount/2) {
				return candidates.get(i).getName();
			}
			if(counter.get(i).size()<min) {
				min=counter.get(i).size();
			}
			
		}*/
		//int i=0;
//		for(int i=0;i<ballots.size();i++) {
//		//while(Winner==null) {
//			int c=ballots.get(i).getCandidateByRank(1);
//			
//			min=counter.get(0).size();
//			for (int b =0;b<counter.size();b++) {
//				if(counter.get(b).size()>validBallotAmount/2) {
//					//return candidates.get(b).getName();
//					Winner=candidates.get(b).getName();
//					break;
//				}
//				if(counter.get(b).size()<min) {
//					min=counter.get(i).size();
//				}
//					
//			}
//			if(candidates.get(c-1).isActive()) {
//				if(counter.get(c-1).size()<=min ) {
//					eliminatedCandidates.add(candidates.get(c-1).getName());
//					ballots.get(i).eliminate(candidates.get(c-1).getId());
//					while(!counter.get(c-1).isEmpty()) {
//						counter.get(ballots.get(i).getCandidateByRank(1)).add(ballots.get(i));
//						counter.get(c-1).remove(ballots.get(i));
//					}
//					counter.remove(c-1);
//					
//					
//				}
//
//			}
//			//i++;
//		}
//		return Winner;
		//System.out.println(" win" +Winner);
		return "no Winner";
	}

	//List<Integer> votes=new ArrayList<Integer>();
	//List<Candidate> tempEliminating=new ArrayList<Candidate>();
		
		//votes=[6,1,1,2,2]
		/*for(Ballot b:ballots) {
			int rank=1;
			int first=b.getCandidateByRank(1);
			while(first!=-1&&candidates.get(first-1).isActive()){
				votes.set(first-1, votes.get(first-1)+1);
				rank++;
				first=b.getCandidateByRank(rank);
			}
		}
		System.out.println(min);
		

	/*	for (int i=0;i<votes.size();i++) {
			if(votes.get(i)>votes.size()/2) {
				votes.remove(i);
				max = getMax(votes);
				min = getMin(votes);
			}
			
		}*/
		


		/*for(int i=0;i<votes.size();i++) {
			if(votes.get(i)>max) {max=votes.get(i);}
			if(votes.get(i)<min) {min=votes.get(i);}
		}*/
		/*for (Ballot b:ballots) {
				System.out.println(min);
				if(votes.get(b.getCandidateByRank(1)-1)<=min && candidates.get(b.getCandidateByRank(1)-1).isActive()) {
					tempEliminating.add(candidates.get(b.getCandidateByRank(1)-1));
					this.eliminatedCandidates.add(candidates.get(b.getCandidateByRank(1)-1).getName());
					votes.remove(b.getCandidateByRank(1));
					b.eliminate(b.getCandidateByRank(1));
					MaxMin(votes);
				}
		}
		//this.eliminatedCandidates=tempEliminating

		return candidates.get(0).getName();
	}
		
		
//		List<Integer> votes=new ArrayList<Integer>();
//		List<Candidate> tempEliminating=new ArrayList<Candidate>();
//		/*int lowestValue=0;
//		int first;
//		for(Ballot b:ballots) {
//			first = b.getCandidateByRank(1);
//			numAmount.add(first,numAmount.get(first)+1);
//			lowestValue++;
//		}*/
//		
//		//votes=[6,1,1,2,2]
//		for(Ballot b:ballots) {
//			int rank=1;
//			int first=b.getCandidateByRank(1);
//			while(first!=-1&&candidates.get(first-1).isActive()){
//				votes.set(first-1, votes.get(first-1)+1);
//				rank++;
//				first=b.getCandidateByRank(rank);
//			}
//		}
//		
//		//MAYBE TRATA SOLO ELIMINA CUANDO LLEGA AL BALLOT CON RANK 1 DEL MIN
//		
//		//Eliminated maybe {lola,juan}
//		//max=6
//		int max=0;
//		int min=candidates.size()+1;
//		Candidate maxCandidate;
//		for(int i=0;i<votes.size();i++) {
//			if(votes.get(i)>max) {max=votes.get(i);}
//			if(votes.get(i)<min) {min=votes.get(i);/*tempEliminating.clear();*/}
//			/*if(votes.get(i)==min){
//				if(!eliminatedCandidates.contains(candidates.get(i).getName())&&candidates.get(i).isActive()) {
//					tempEliminating.add(candidates.get(i));
//					}
//				}*/
//		}
//		for (Ballot b:ballots) {
//			//for (Candidate c:candidates) {
//				if(votes.get(b.getCandidateByRank(1))<=min && candidates.get(b.getCandidateByRank(1)).isActive()) {
//					tempEliminating.add(candidates.get(b.getCandidateByRank(1)));
//					//votes.remove(votes.get(b.getCandidateByRank(1)));
//					//candidates.remove(maxCandidate)
//					votes.remove(b.getCandidateByRank(1));
//					b.eliminate(b.getCandidateByRank(1));
//					
//					//winning.remove(candidates.get(b.getCandidateByRank(1)));
//				}
//			//}
//		}
//		/*if (max>candidates.size()/2) {eliminatedCandidates.add(getWinner())
//		
//		//boolean eliminatedFound=false;
//		int ranks=2;
//		//int maxSize=1;
//		List<Integer> secondCount=new ArrayList<Integer>();
//		while(ranks>this.ballotAmount && tempEliminating.size()>1) {
//			
//			for(int i=0;i<tempEliminating.size();i++) {
//				for(Ballot b:ballots) {
//					if(b.getRankByCandidate(tempEliminating.get(i).getId())==ranks) {
//						secondCount.set(i, secondCount.get(i)+1);
//					}
//				}
//			}
//			
//			int minimum=secondCount.get(0);
//			for(int s=1;s<secondCount.size();s++) {
//				if(secondCount.get(s)<minimum) {
//					
//					candidates.remove(tempEliminating.get(s));
//					tempEliminating.remove(s);
//					
//					secondCount.remove(s);
//				}
//			}
//			
//			secondCount.clear();
//			ranks++;
//		}
//		for(Candidate c:tempEliminating) {
//			eliminatedCandidates.add(c.getName());
//			c.setActive(false);
//			
//		}
//		tempEliminating.clear();*/
//	
//		return null;
//	}
	

	// returns the total amount of ballots submitted
	public int getTotalBallots() {
		return this.ballotAmount;
		//return 0;
	}
	// returns the total amount of invalid ballots
	public int getTotalInvalidBallots() {
		return this.invalidBallotAmount;
		//return 0;
	}
	// returns the total amount of blank ballots
	public int getTotalBlankBallots() {
		return this.blancBallotAmount;
		//return 0;
	}
	// returns the total amount of valid ballots
	public int getTotalValidBallots() {
		return this.validBallotAmount;
		//return 0;
	}
	/* List of names for the eliminated candidates with the numbers of 1s they had,
	must be in order of elimination. Format should be <candidate name>-<number of 1s
	when eliminated>*/
	public List<String> getEliminatedCandidates() {
		//System.out.println(" size"+eliminatedCandidates.size());
		//for (String e:eliminatedCandidates) {System.out.println("a"+e+"s"+eliminatedCandidates.size());}
		//return this.eliminatedCandidates;
		/*this.eliminatedCandidates.add("Lola Mento-1");
		this.eliminatedCandidates.add("Juan Lopez-1");
		this.eliminatedCandidates.add("Pucho Avellanet-3");*/
		return  this.eliminatedCandidates;
		//return null;
	}
}