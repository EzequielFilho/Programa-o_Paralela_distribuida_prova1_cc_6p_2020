package iftm.edu.br.semaforo;

import java.awt.Color;
import java.util.Random;

public class Carro implements Runnable {

	public static final int TICKS = 60;
	public static final int SLEEP = 1000 / TICKS;
	
	private static final int CENTRO_X = 195;
	private static final int CENTRO_Y = 195;
	private static final int DISTANCIA_CARROS = 30;
	private static final int DISTANCIA_SEMAFORO = 70;
	
	private static int[][] DadosRua = {
			// PosicaoX, PosicaoY, VelocidadeX, VelocidadeY 
			{ -10, 223,  1,  0 },
			{ 178, -10,  0,  1 },
			{ 410, 178, -1,  0 },
			{ 223, 410,  0, -1 }
	};
	private static Carro[] ProximoRua = {
			null, null, null, null
	};
	private static Color[] CoresCarros = {
			Color.red, Color.blue, Color.green, Color.yellow, Color.cyan, Color.yellow
	};
	
	private int rua;
	private Semaforo semaforo;
	private Carro proximo;
	
	private Color cor;

	private int posicaoX;
	private int posicaoY;
	
	private int velocidadeX;
	private int velocidadeY;
	
	private boolean jaPassou = false;
	private Thread thread;
	
	public Carro(int rua, Semaforo semaforo) {
		this.rua = rua;
		this.semaforo = semaforo;

		this.proximo = ProximoRua[rua];
		ProximoRua[rua] = this;
		
		thread = new Thread(this);
	}
	
	public static Carro Gerar(Semaforo semaforo) {
		Random random = new Random();
		int rua = random.nextInt(Math.min(DadosRua.length, semaforo.getRuas()));
		
		Carro carro = new Carro(rua, semaforo);
		carro.cor = CoresCarros[random.nextInt(CoresCarros.length)];
		carro.posicaoX = DadosRua[rua][0];
		carro.posicaoY = DadosRua[rua][1];
		carro.velocidadeX = DadosRua[rua][2];
		carro.velocidadeY = DadosRua[rua][3];
		return carro;
	}
	
	public int getRua() {
		return rua;
	}

	public Semaforo getSemaforo() {
		return semaforo;
	}

	public Color getCor() {
		return cor;
	}

	public int getPosicaoX() {
		return posicaoX;
	}

	public int getPosicaoY() {
		return posicaoY;
	}

	public int getVelocidadeX() {
		return velocidadeX;
	}

	public int getVelocidadeY() {
		return velocidadeY;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public boolean estaNoSemaforo() {
		return !jaPassou && semaforo.getEstadoSinal(rua) == Semaforo.SINAL_FECHADO && distancia(posicaoX, posicaoY, CENTRO_X, CENTRO_Y) < DISTANCIA_SEMAFORO;
	}
	public boolean estaAtrasDeUmCarro() {
		return proximo != null && distancia(posicaoX, posicaoY, proximo.posicaoX, proximo.posicaoY) < DISTANCIA_CARROS;
	}
	public boolean estaMovendo() {
		return !estaAtrasDeUmCarro() && !estaNoSemaforo();
	}
	
	public boolean simular() throws InterruptedException {
		if(estaMovendo()) {			
			// Mover
			posicaoX += velocidadeX;
			posicaoY += velocidadeY;
			
			// Veficar se já saiu do mapa
			if(posicaoX < -20 || posicaoY < -20 || posicaoX > 420 || posicaoY > 420) {
				proximo = null;
				posicaoX = 0;
				posicaoY = 0;
				
				// Remover da lista
				semaforo.removerCarro(this);
				return false;
			}
			
			// Gravar se já passou do semaforo
			if(distancia(posicaoX, posicaoY, CENTRO_X, CENTRO_Y) < DISTANCIA_SEMAFORO * 0.9) {
				jaPassou = true;
			}
		}
		else if(estaNoSemaforo()) {
			// Aguardar o sinal
			synchronized(this) {
				this.wait();
			}
		}		
		return true;
	}

	@Override
	public void run() {
		try {
			while(Principal.Executando) {
				// Simular até finalizar
				if(!simular())
					return;
				
				// Aguardar um tick
				Thread.sleep(SLEEP);
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Calcula a distancia entre dois pontos
	private static double distancia(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
}
