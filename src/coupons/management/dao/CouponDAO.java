package coupons.management.dao;

import java.util.ArrayList;

import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.exceptions.CouponAllredyExistException;
import coupons.management.exceptions.CouponNotFoundException;
import coupons.management.exceptions.CouponsDBException;

/**
 * interface CouponDAO
 * @author Leonid S
 *
 */
public interface CouponDAO {
	/**
	 * create new Coupon in DB
	 * @param coupon
	 * @param Companyid
	 * @throws CouponAllredyExistException - the coupon allredy exist in DB
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	void createCoupon(Coupon coupon , long Companyid) throws  CouponAllredyExistException, CouponsDBException;
	/**
	 * remove coupon from DB
	 * @param coupon
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	void removeCoupon(Coupon coupon) throws CouponsDBException;
	/**
	 * update Coupon in DB
	 * @param coupon
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	void updateCoupon(Coupon coupon) throws CouponsDBException;
	/**
	 * get Coupon from DB
	 * @param id
	 * @return coupon
	 * @throws CouponNotFoundException - coupon not found in DB
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	Coupon getCoupon(long id) throws CouponNotFoundException, CouponsDBException;
	/**
	 * Get Array List of all coupons
	 * @return coupon 
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	ArrayList<Coupon> getAllCoupons() throws CouponsDBException;
	/**
	 * Get array list of coupons by type
	 * @param type
	 * @return coupon by type
	 * @throws CouponsDBException  - if there is any problem working with DB
	 */
	ArrayList<Coupon> getCouponByType(CouponType type) throws CouponsDBException;
	/**
	 * associate coupon To Customer , by customer id and coupon id 
	 * @param customerid
	 * @param couponid
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	void associateToCustomer(long customerid , long couponid) throws CouponsDBException;
	/**
	 * get array list of coupons by id
	 * @param companyid
	 * @return coupon by id
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public ArrayList<Coupon> getAllCouponsByCompId(long companyid) throws CouponsDBException ;
	/**
	 * get array list of all coupons by customer id
	 * @param customerid
	 * @return
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	ArrayList<Coupon> getAllCouponsByCustomerId(long customerid) throws CouponsDBException;
}
