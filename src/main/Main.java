package main;
import java.io.*;

public class Main {	
	public static void main(String[] args) throws IOException {
		Solver solver = new Solver();

		for(int i = 0; i < solver.getTracks(); i++) {
			String track = i >= 0 && i <= 27 ? String.valueOf((char)(i + 65)) : null;
			solver.setTask("TRACK " + track, 540);
			
			solver.dp(0, 180);
			solver.track(0, 180);
			solver.reset(solver.getAux());
			solver.setTask("12:00 Almoço", 780);
			
			solver.dp(0, 240);
			solver.track(0, 240);
			solver.reset(solver.getAux());
			solver.setTask("17:00 Evento de Networking", 540); //Verificar, evento de network pode ocorrer antesx'
			
			solver.setTask("", 0);
		}
		
		for (String task : solver.getAnswer()) {
			System.out.println(task);
		}
	}

}
