package iftm.edu.br.semaforo;

import javax.swing.JFrame;

public class Janela extends JFrame {
	private static final long serialVersionUID = 6561168618281327479L;

	public Janela(Semaforo semaforo) {
		super();
		
		this.setSize(406, 430);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// Desenho
		Desenho desenho = new Desenho(semaforo);
		this.setContentPane(desenho);
		
		this.setVisible(true);
		
		// Thread para atualizar o desenho
		new Thread(desenho).start();
	}
	
}
