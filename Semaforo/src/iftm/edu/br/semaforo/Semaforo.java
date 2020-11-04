package iftm.edu.br.semaforo;

import java.util.ArrayList;

public class Semaforo implements Runnable {

	public final static int SINAL_ABERTO = 0;
	public final static int SINAL_AMARELO = 1;
	public final static int SINAL_FECHADO = 2;
	public final static int TEMPO_ABERTO = 3000;
	public final static int TEMPO_AMARELO = 1000;
	
	private int passo = -1;
	private int ruas = 4;
	
	private ArrayList<Carro> carros = new ArrayList<Carro>();
	
	public int getSinalAberto() {
		return passo / 2;
	}
	public int getEstadoSinal() {
		return passo % 2;
	}
	public int getEstadoSinal(int rua) {
		if(getSinalAberto() == rua)
			return getEstadoSinal();
		else
			return SINAL_FECHADO;
	}
	
	public int getRuas() {
		return ruas;
	}
	public ArrayList<Carro> getCarros() {
		return carros;
	}
	
	public void addCarro() {
		synchronized(this) {
			Carro novoCarro = Carro.Gerar(this);
			carros.add(novoCarro);
			new Thread(novoCarro).start();
		}
	}
	public void removerCarro(Carro carro) {
		synchronized(this) {
			carros.remove(carro);
		}
	}
	
	public void proximoPasso() {
		passo = (passo + 1) % (ruas * 2);
	}

	public void simular() throws InterruptedException {		
		// Criar novos carros
		for(int i = 0; i < 2; i++)
			addCarro();
		
		// Simular		
		proximoPasso();
		
		int sinalNovo = getSinalAberto();
		int estado = getEstadoSinal();
		
		// Notificar carros sobre a alteração
		if(estado == SINAL_ABERTO) {
			for(Carro carro : carros) {
				if(carro.getRua() == sinalNovo) {
					synchronized(carro) {
						carro.notify();
					} 
				}
			}
		}
				
		// Aguardar alteração
		if(estado == SINAL_ABERTO)
			Thread.sleep(TEMPO_ABERTO);
		else if(estado == SINAL_AMARELO)
			Thread.sleep(TEMPO_AMARELO);
	}
	
	@Override
	public void run() {
		try {
			while(Principal.Executando) {
				simular();
			}			
		} 
		catch (InterruptedException e) {			
			e.printStackTrace();			
		}
	}
	
}
