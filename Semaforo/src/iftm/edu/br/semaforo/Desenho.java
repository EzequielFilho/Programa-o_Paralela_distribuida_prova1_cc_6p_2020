package iftm.edu.br.semaforo;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Desenho extends JPanel implements Runnable {
	private static final long serialVersionUID = 609660882296787876L;

	private Semaforo semaforo;
	
	private static Color[] CoresSemaforo = {
			Color.green, Color.yellow, Color.red
	};
	
	public Desenho(Semaforo semaforo) { 
		super();
		
		this.semaforo = semaforo;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Grama
		g.setColor(new Color(0x00DD00));
		g.fillRect(0, 0, 400, 400);
		
		// Calcadas
		g.setColor(new Color(0xDDDDDD));
		g.fillRect(150, 0, 100, 400);
		g.fillRect(0, 150, 400, 100);
		
		// Ruas
		g.setColor(new Color(0x333333));
		g.fillRect(160, 0, 80, 400);
		g.fillRect(0, 160, 400, 80);
		
		// Faixas
		g.setColor(Color.white);
		for(int i = 0; i < 8; i++) {
			g.fillRect(198, i * 20, 4, 15);
			g.fillRect(i * 20, 198, 15, 4);
		}
		for(int i = 12; i < 20; i++) {
			g.fillRect(198, 5 + i * 20, 4, 15);
			g.fillRect(5 + i * 20, 198, 15, 4);
		}
		
		// Carros
		synchronized(semaforo) {
			for(Carro carro : semaforo.getCarros()) {
				g.setColor(carro.getCor());
				g.fillRect(carro.getPosicaoX() - 5, carro.getPosicaoY() - 5, 
						10 * (1 + Math.abs(carro.getVelocidadeX())), 
						10 * (1 + Math.abs(carro.getVelocidadeY())));
			}
		}
		
		// Semaforos
		g.setColor(new Color(0xB0B0B0));
		g.fillRect(155, 155, 5, 40);
		g.fillRect(205, 155, 40, 5);
		g.fillRect(240, 205, 5, 40);
		g.fillRect(155, 240, 40, 5);
		
		g.setColor(CoresSemaforo[semaforo.getEstadoSinal(0)]);
		g.fillRect(235, 215, 5, 15);
		g.setColor(CoresSemaforo[semaforo.getEstadoSinal(1)]);
		g.fillRect(170, 235, 15, 5);
		g.setColor(CoresSemaforo[semaforo.getEstadoSinal(2)]);
		g.fillRect(160, 170, 5, 15);
		g.setColor(CoresSemaforo[semaforo.getEstadoSinal(3)]);
		g.fillRect(215, 160, 15, 5);
	}

	@Override
	public void run() {
		try {
			while(Principal.Executando) {
				this.repaint();
				Thread.sleep(Carro.SLEEP);
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
