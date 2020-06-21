package coupons.management.clients;

import java.util.ArrayList;
import java.util.Date;

import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.dao.CouponDBDAO;
import coupons.management.dao.CustomerDBDAO;
import coupons.management.exceptions.CouponExpiredException;
import coupons.management.exceptions.CouponNotFoundException;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

public class CustomerFacade implements CouponClientFacade {
	CustomerDBDAO custDBdao;
	CouponDBDAO couponDBdao;

	private long customerid;

	/**
	 * CustomerFacade login, compare name and password in DB
	 * 
	 * @throws CouponsDBException
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clienttype)
			throws UserOrPasswNotCorrectException, CouponsDBException {
		long rc = custDBdao.login(name, password);

		customerid = rc;
		return this;
	}

	/**
	 * default constructor of CustomerFacade
	 **/
	public CustomerFacade() {
		super();
		couponDBdao = new CouponDBDAO();
		custDBdao = new CustomerDBDAO();

	}

	/**
	 * purchase Coupon - associate coupon to Customer
	 * 
	 * @param coupon
	 * @throws CouponsDBException          - if there is any problem working with DB
	 * @throws CouponNotFoundException-the coupon not found in the DB
	 * @throws CouponExpiredException      - the coupon expire and remove from DB
	 */
	public void purchaseCoupon(Coupon coupon)
			throws CouponsDBException, CouponNotFoundException, CouponExpiredException {

		Coupon c = couponDBdao.getCoupon(coupon.getId());
		if (c == null) {
			throw new CouponNotFoundException("coupon not found" + coupon.getId(), "CustomerFacade:: purchaseCoupon");
		}

		Date now = new Date();
		Date couponEndTime = c.getEndDate();
		if (now.after(couponEndTime)) {
			throw new CouponExpiredException("coupon expired" + coupon.getId(), "CustomerFacade:: purchaseCoupon");
		}
		couponDBdao.associateToCustomer(customerid, coupon.getId());

	}

	/**
	 * Get array list of all Purchase coupons for this customer
	 * 
	 * @return coupons
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public ArrayList<Coupon> getAllPurchase() throws CouponsDBException {
		ArrayList<Coupon> coupons = couponDBdao.getAllCouponsByCustomerId(customerid);

		return coupons;

	}

	/**
	 * Get array list of All Purchased Coupon By Type
	 * 
	 * @param type
	 * @return coupons
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public ArrayList<Coupon> getAllPurchasedCouponByType(CouponType type) throws CouponsDBException {
		ArrayList<Coupon> coupons = couponDBdao.getAllCouponsByCustomerIdAndType(customerid, type);

		return coupons;

	}

	/**
	 * Get array list of All Purchased Coupon By Price
	 * 
	 * @param price
	 * @return coupons
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public ArrayList<Coupon> getAllPurchasedCouponByPrice(float price) throws CouponsDBException {
		ArrayList<Coupon> coupons = couponDBdao.getAllCouponsByCustomerIdAndPrice(customerid, price);

		return coupons;
	}
}
