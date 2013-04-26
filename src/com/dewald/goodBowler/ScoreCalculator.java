package com.dewald.goodBowler;

public class ScoreCalculator {
	
	public Boolean[] pins = {false, false, false, false, false, false, false, false, false, false};
	public static Integer[] intScores;
	
	public Integer getTotalScore(String[] pins){
		int totalScore;
		intScores = new Integer[21];
		intScores = createArrayScores(pins);
		totalScore = calculateScore(intScores);
		return totalScore;
	}
	
	public Integer processScore(String s) {
		Integer score = 0;
			if (s == null) {
				score = 0;
			}
			else if (s.equals("-")){
				score = 0;
			}
			else {
				score = s.length();
			}
		return score;
	}
	
	public Integer[] createArrayScores(String[] s) {
		Integer[] scoresArray = new Integer[21];
		
		for (int i = 0; i < s.length; i++) {
			scoresArray[i] = processScore(s[i]);
		}
		
		return scoresArray;
	}
	
	public Integer calculateScore(Integer[] scores) {
		Integer totalScore = 0;
		
		//1st through 8th frame scoring
		for (int i = 0; i <= 14; i+=2) {
			if (scores[i] == 10) {
				if (scores[i+2] == 10) {
					totalScore = totalScore + scores[i] + scores[i+2] + scores[i+4]; 
				}//end inner if
				else {
					totalScore = totalScore + scores[i] + scores[i+2] + scores[i+3];
				}//inner else
			}//end first if
			else if (scores[i] + scores[i+1] == 10) {
				totalScore = totalScore + scores[i] + scores[i+1] + scores[i+2];
			}//end first else if
			else {
				totalScore = totalScore + scores[i] + scores[i+1];
			}//end else
		}//end for
		
		//9th frame scoring
		if (scores[16] == 10) {
			totalScore = totalScore + scores[16] + scores[18] + scores[19];
		}//end if
		else if (scores[16] + scores[17] == 10) {
			totalScore = totalScore + scores[16] + scores[17] + scores[18];
		}//end else if
		else {
			totalScore = totalScore + scores[16] + scores[17];
		}
		
		//10th frame scoring
			totalScore = totalScore + scores[18] + scores[19] + scores[20];
		
		return totalScore;
	}

	public Boolean[] processString(String s) {
		System.out.println(s);
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch (c) {
			case '1': pins[0] = true; break;
			case '2': pins[1] = true; break;
			case '3': pins[2] = true; break;
			case '4': pins[3] = true; break;
			case '5': pins[4] = true; break;
			case '6': pins[5] = true; break;
			case '7': pins[6] = true; break;
			case '8': pins[7] = true; break;
			case '9': pins[8] = true; break;
			case '0': pins[9] = true; break;
			}
		}
		
		return pins;
	}
	
	public Integer scorePerBall(String s) {
		Integer totPins = 0;
		totPins = s.length();
		
		return totPins;
	}
	
	public void display() {
		for (int i = 0; i < pins.length; i++) {
			System.out.println(pins[i]);
		}
	}
	
	public Integer scorePerFrame(String[] s, Integer frame){
		Integer score = 0;
		Integer[] scoreArray = createArrayScores(s);
		switch(frame){
		case 1:
			if(scoreArray[0] == 10 && scoreArray[2] == 10){
				score = scoreArray[0] + scoreArray[2] + scoreArray[4];
			}else if(scoreArray[0] == 10){
				score = scoreArray[0] + scoreArray[2] + scoreArray[3];
			}else if(scoreArray[0] + scoreArray[1] == 10){
				score = scoreArray[0] + scoreArray[1] + scoreArray[2];
			}else score = scoreArray[0] + scoreArray[1];
			break;
		case 2:
			if(scoreArray[2] == 10 && scoreArray[4] == 10){
				score = scoreArray[2] + scoreArray[4] + scoreArray[6];
			}else if(scoreArray[2] == 10){
				score = scoreArray[2] + scoreArray[4] + scoreArray[5];
			}else if(scoreArray[2] + scoreArray[3] == 10){
				score = scoreArray[2] + scoreArray[3] + scoreArray[4];
			}else score = scoreArray[2] + scoreArray[3];
			break;
		case 3:
			if(scoreArray[4] == 10 && scoreArray[6] == 10){
				score = scoreArray[4] + scoreArray[6] + scoreArray[8];
			}else if(scoreArray[4] == 10){
				score = scoreArray[4] + scoreArray[6] + scoreArray[7];
			}else if(scoreArray[4] + scoreArray[5] == 10){
				score = scoreArray[4] + scoreArray[5] + scoreArray[6];
			}else score = scoreArray[4] + scoreArray[5];
			break;
		case 4:
			if(scoreArray[6] == 10 && scoreArray[8] == 10){
				score = scoreArray[6] + scoreArray[8] + scoreArray[10];
			}else if(scoreArray[6] == 10){
				score = scoreArray[6] + scoreArray[8] + scoreArray[9];
			}else if(scoreArray[6] + scoreArray[7] == 10){
				score = scoreArray[6] + scoreArray[7] + scoreArray[8];
			}else score = scoreArray[6] + scoreArray[7];
			break;
		case 5:
			if(scoreArray[8] == 10 && scoreArray[10] == 10){
				score = scoreArray[8] + scoreArray[10] + scoreArray[12];
			}else if(scoreArray[8] == 10){
				score = scoreArray[8] + scoreArray[10] + scoreArray[11];
			}else if(scoreArray[8] + scoreArray[9] == 10){
				score = scoreArray[8] + scoreArray[9] + scoreArray[10];
			}else score = scoreArray[8] + scoreArray[9];
			break;
		case 6:
			if(scoreArray[10] == 10 && scoreArray[12] == 10){
				score = scoreArray[10] + scoreArray[12] + scoreArray[14];
			}else if(scoreArray[10] == 10){
				score = scoreArray[10] + scoreArray[12] + scoreArray[13];
			}else if(scoreArray[10] + scoreArray[11] == 10){
				score = scoreArray[10] + scoreArray[11] + scoreArray[12];
			}else score = scoreArray[10] + scoreArray[11];
			break;
		case 7:
			if(scoreArray[12] == 10 && scoreArray[14] == 10){
				score = scoreArray[12] + scoreArray[14] + scoreArray[16];
			}else if(scoreArray[12] == 10){
				score = scoreArray[12] + scoreArray[14] + scoreArray[15];
			}else if(scoreArray[12] + scoreArray[13] == 10){
				score = scoreArray[12] + scoreArray[13] + scoreArray[14];
			}else score = scoreArray[12] + scoreArray[13];
			break;
		case 8:
			if(scoreArray[14] == 10 && scoreArray[16] == 10){
				score = scoreArray[14] + scoreArray[16] + scoreArray[18];
			}else if(scoreArray[14] == 10){
				score = scoreArray[14] + scoreArray[16] + scoreArray[17];
			}else if(scoreArray[14] + scoreArray[15] == 10){
				score = scoreArray[14] + scoreArray[15] + scoreArray[16];
			}else score = scoreArray[14] + scoreArray[15];
			break;
		case 9:
			if(scoreArray[16] == 10){
				score = scoreArray[16] + scoreArray[18] + scoreArray[19];
			}else if(scoreArray[16] + scoreArray[17] == 10){
				score = scoreArray[16] + scoreArray[17] + scoreArray[18];
			}else score = scoreArray[16] + scoreArray[17];
			break;
		case 10:
			score = scoreArray[18] + scoreArray[19] + scoreArray[20];
			break;
		default:
			score = -1;
		}
		return score;
	}
	
	public Double strikesPerGame(String[] scores){
		Double strikes = 0.0;
		Integer[] intscores = createArrayScores(scores);
		for(int i = 0; i < 17; i+=2){
			if(intscores[i] == 10){strikes = strikes + 1;}
		}
		for(int k = 18; k < 21; k++){
			if(intscores[k] == 10){strikes = strikes + 1;}
		}
		return strikes;
	}
	
	public Double sparesPerGame(String[] scores){
		Double spares = 0.0;
		Integer[] intscores = createArrayScores(scores);
		for(int i = 0; i < 17; i+=2){
			if(intscores[i] != 10){
				if(intscores[i] + intscores[i+1] == 10){
					spares = spares + 1;
				}
			}
		}
		if(intscores[18] != 10){
			if(intscores[18] + intscores[19] == 10){
				spares = spares + 1;
			}
		}else if(intscores[19] != 10){
			if(intscores[19] + intscores[20] == 10){
				spares = spares + 1;
			}
		}
		return spares;
	}
	
	public Double opensPerGame(String[] scores){
		Double opens = 0.0;
		Integer[] intscores = createArrayScores(scores);
		for(int i = 0; i < 19; i+=2){
			if(intscores[i] + intscores[i+1] < 10){
				opens = opens + 1;
			}
		}
		return opens;
	}
	
	public Double singleSparesByPin(String[] scores, int pin){
		Double count = 0.0;
		switch(pin){
		case 1:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("1")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 2:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("2")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 3:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("3")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 4:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("4")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 5:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("5")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 6:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("6")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 7:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("7")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 8:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("8")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 9:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("9")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		case 10:
			for(int i = 0; i < 20; i+=2){
				if(scores[i+1].equals("0")){
					if(scores[i].length() + scores[i+1].length() == 10){
						count = count + 1;
					}
				}
			}
			break;
		}
		return count;
	}
	
	public Double singlePinOpen(String[] scores, int pin){
		Double count = 0.0;
		switch(pin){
		case 1:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("234567890")){
					if(!scores[i+1].equals("1")){
						count = count + 1;
					}
				}
			}
			break;
		case 2:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("134567890")){
					if(!scores[i+1].equals("2")){
						count = count + 1;
					}
				}
			}
			break;
		case 3:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("124567890")){
					if(!scores[i+1].equals("3")){
						count = count + 1;
					}
				}
			}
			break;
		case 4:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123567890")){
					if(!scores[i+1].equals("4")){
						count = count + 1;
					}
				}
			}
			break;
		case 5:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123467890")){
					if(!scores[i+1].equals("5")){
						count = count + 1;
					}
				}
			}
			break;
		case 6:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123457890")){
					if(!scores[i+1].equals("6")){
						count = count + 1;
					}
				}
			}
			break;
		case 7:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123456890")){
					if(!scores[i+1].equals("7")){
						count = count + 1;
					}
				}
			}
			break;
		case 8:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123456790")){
					if(!scores[i+1].equals("8")){
						count = count + 1;
					}
				}
			}
			break;
		case 9:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123456780")){
					if(!scores[i+1].equals("9")){
						count = count + 1;
					}
				}
			}
			break;
		case 10:
			for(int i = 0; i < 20; i+=2){
				if(scores[i].equals("123456789")){
					if(!scores[i+1].equals("0")){
						count = count + 1;
					}
				}
			}
			break;
		}
		return count;
	}
	
	//calculates the average of the scores for the game table
	public String calculateAverage(Integer[] scores){
		if(scores.length == 0){
			return "0";
		}else{
		Integer average = 0;
		Integer totalScore = 0;
		for(int i = 0; i < scores.length; i++){
			totalScore = totalScore + scores[i];
		}
		average = totalScore / scores.length;
		return average.toString();
		}
	}
	
	public String highScore(Integer[] scores){
		if(scores.length == 0){
			return "0";
		}else{
			Integer highScore = 0;
			for(int i = 0; i < scores.length; i++){
				if(scores[i] > highScore){
					highScore = scores[i];
				}
			}
			return highScore.toString();
		}
	}
	
	//checks for how many strikes where made in a series of games
	public Integer numStrikes(String[][] scores){
		Integer strikeCount = 0;
			for(int r = 0; r < scores.length; r++){
				for(int c = 0; c < 12; c++){
					if(scores[r][c].length() == 10){
						strikeCount = strikeCount + 1;
					}
				}			
			}
		return strikeCount;
	}
	
	//checks for how many spares where made
		public Integer numSpares(String[][] scores){
			Integer spareCount = 0;
			String[] s = new String[21];
				for(int r = 0; r < scores.length; r++){
					for(int c = 0; c < 21; c++){
						s[c] = scores[r][c];
					}
					spareCount = (int) (spareCount + sparesPerGame(s));
					/*for(int c = 0; c < 17; c+=2){
						if(scores[r][c].length() != 10){
							if(scores[r][c].length() + scores[r][c+1].length()  == 10){
								spareCount = spareCount + 1;
							}
						}
					}
					if(scores[r][18].length() != 10){
						if(scores[r][18].length() + scores[r][19].length() == 10){
							spareCount = spareCount + 1;
						}
					}else if(scores[r][19].length() != 10){
						if(scores[r][19].length() + scores[r][20].length()  == 10){
						spareCount = spareCount + 1;
						}
					}*/
				}
			return (Integer)spareCount;
		}
	
	//checks to see how many chances you had to shoot a spare
	public Integer spareChances(String[][] scores){
		Integer spare = 0;
			for(int r = 0; r < scores.length; r++){
				for(int c = 0; c < 19; c+=2){
					if(scores[r][c].length() != 10){
						spare = spare + 1;
					}
				}
				if(scores[r][18].length() == 10 && scores[r][19].length()  != 10){
					spare = spare + 1;
				}
			}
		return spare;
	}
	
	//counts the most strikes in a row
	public Integer strikesRow(String[][] scores){
		Integer count = 0;
		Integer strikes = 0;
		Integer mostStrikes = 0;
		for(int r = 0; r < scores.length; r++){
			for(int c = 0; c < 12; c++){
				if(scores[r][c].length() == 10){
					count = count + 1;
					if(count > strikes){
						strikes = count;
					}
				}else
					count = 0;
			}
			if(strikes > mostStrikes){
				mostStrikes = strikes;
			}
			strikes = 0;
			count = 0;
		}
		return mostStrikes;
	}
}
