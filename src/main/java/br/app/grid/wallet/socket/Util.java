package br.app.grid.wallet.socket;

public class Util {

	public static void sleep(int timeInMilliSeconds) {
		try {
			Thread.sleep(timeInMilliSeconds);
		} catch (InterruptedException e) {
		}
	}

}
