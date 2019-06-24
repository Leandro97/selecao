package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
	private List<String> talkList; //This list stores lecture names
	private List<Integer> timeList; //This list stores lecture duration
	private List<Integer> aux = new ArrayList<Integer>(); //Auxiliary list for the chosen lectures
	private List<String> answer = new ArrayList<String>(); //Final answer to the problem
	private int currentTime = 540; //Initial time (09:00 AM)
	private int remainingTime = 240; //Maximum time for session (4 hours)	
	private int activities = 0; //Number of available lectures
	private double tracks; //Number of tracks
	private Integer[][] record; //Auxiliary array used in Dynamic Problem

	public Solver() throws IOException {
		double total = 0;
		URL url = Main.class.getClassLoader().getResource("files/proposals.txt");
		
		File file = new File(url.getPath());
		BufferedReader reader = new BufferedReader(new FileReader(file)); 

		String line;
		talkList = new ArrayList<String>();
		timeList = new ArrayList<Integer>();

		//Setting the lecture duration
		while((line = reader.readLine()) != null) {
			activities++;
			String last = line.replaceAll("\\D+","");
			
			int time;
			if(last.equals("")) {
				time =  5;
			} else {
				time = Integer.parseInt(last.split("m")[0]);
			}

			int duration = time;
			total += time;
			
			talkList.add(line);
			timeList.add(duration);
		}
		
		reader.close();
		tracks = Math.ceil(total/420);
		record = new Integer[activities + 1][remainingTime + 1];
	}

	public Integer[][] getRecord() {
		return record;
	}

	//Solution using Dynamic Programming
	public int dp(int act, int remainingTime) {
		if((remainingTime == 0) || (act == activities)) {
			record[act][remainingTime] = 0;
			return 0;
		}

		if(record[act][remainingTime] != null) {
			return record[act][remainingTime];
		}

		if(timeList.get(act) > remainingTime) {
			record[act][remainingTime] = dp(act + 1, remainingTime);
		} else {
			int put = 1 + dp(act + 1, remainingTime - timeList.get(act));
			int notPut = dp(act + 1, remainingTime);

			if(put > notPut) {
				record[act][remainingTime] = put;

			}else {
				record[act][remainingTime] = notPut;
			}
		}

		return record[act][remainingTime];
	}

	//Return the order of the chosen lectures
	public List<Integer> track(int act, int remainingTime) {
		if (act == activities || record[act + 1][remainingTime] == null) {
			return null;
		}

		if (record[act][remainingTime] > record[act + 1][remainingTime]) {
			aux.add(act);
			
			return track(act + 1, remainingTime - timeList.get(act));
		} else {
			return track(act + 1, remainingTime);
		}
	}
	
	//Saves the chosen lectures in the final answer list and delete them from the auxiliary lists
	public void reset(List<Integer> indexes) {
		record = new Integer[activities + 1][remainingTime + 1];
		aux = new ArrayList<Integer>();
		
		for (int index : indexes) {
			String str = formatting(currentTime) + " " + talkList.get(index);
			answer.add(str);
			currentTime += timeList.get(index);
		}
		
		Collections.reverse(indexes);
		for (int index : indexes) {
			activities--;
			talkList.remove(index);
			timeList.remove(index);
		}
		
	}
	
	//Formatting time (HH:mm)
	public String formatting(int time) {
		int hour = time / 60;
		int minute = time % 60;
		String str = String.format("%02d" , hour) + ":" + String.format("%02d" , minute);
		return str;
	}

	public List<String> getAnswer() {
		return answer;
	}
	
	public List<Integer> getAux() {
		return aux;
	}
	
	public double getTracks() {
		return tracks;
	}
	
	public void setTask(String task, int time) {
		answer.add(task);
		currentTime = time;
	}
	
	public void setCurrentTime(int time) {
		currentTime = time;
	}
}
