package coupons.management.app;

import java.util.ArrayList;
import java.util.Date;

import coupons.management.beans.Coupon;
import coupons.management.dao.CouponDBDAO;
import coupons.management.exceptions.CouponsDBException;

/**
 * Daily Coupon Expiration Task
 * 
 * @author leonid S
 *
 */
public class DailyCouponExpirationTask implements Runnable {
	CouponDBDAO couponDbdao = new CouponDBDAO();

	boolean toRun = true;

	public DailyCouponExpirationTask() {

		super();

	}

	/**
	 * @run
	 * Daily check of the coupons and remove expired coupons 
	 */
	public void run() {

		while (toRun) {

			ArrayList<Coupon> coupons = null;
			try {
				coupons = couponDbdao.getAllCoupons();
			} catch (CouponsDBException e1) {
				System.out.println(e1.getMessage());
			}

			for (int i = 0; i < coupons.size(); i++) {
				Coupon c = coupons.get(i);

				Date now = new Date();
				Date couponEndTime = c.getEndDate();
				if (now.after(couponEndTime)) {
					try {
						couponDbdao.removeCoupon(c);
					} catch (CouponsDBException e) {
						System.out.println(e.getMessage());
					}
				}
			}

			try {
				Thread.sleep(86400000); // sleep 24 hour
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		{

		}
	}

	/**
	 * stop task - stop running .
	 */
	void stopTask() {
		toRun = false;
	}
}
