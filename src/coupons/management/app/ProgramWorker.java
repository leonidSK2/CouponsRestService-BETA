package coupons.management.app;

public class ProgramWorker {
	/**
	 * Test Class of server part of the project
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DailyCouponExpirationTask dailytask = new DailyCouponExpirationTask();

		Thread w1 = new Thread(dailytask);

		w1.start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(e.toString());			
		}

		dailytask.stopTask();
	}

}
