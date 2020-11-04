package iftm.edu.br.semaforo;

public class Principal {

	public static boolean Executando = true;

	public static void main(String[] args) {
		Semaforo semaforo = new Semaforo();
		
		Thread semaforoThread = new Thread(semaforo);
		semaforoThread.start();
		
		new Janela(semaforo);
	}

}
