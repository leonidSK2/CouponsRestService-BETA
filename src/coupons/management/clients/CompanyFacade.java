package coupons.management.clients;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import coupons.management.beans.Company;
import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.dao.CompanyDBDAO;
import coupons.management.dao.CouponDBDAO;
import coupons.management.exceptions.CompanyNotFoundException;
import coupons.management.exceptions.CouponAllredyExistException;
import coupons.management.exceptions.CouponNotFoundException;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * Implementation of CompanyFacade
 * @author Owner
 *
 */
public class CompanyFacade implements CouponClientFacade {

	CouponDBDAO coupDBdao;
	CompanyDBDAO compDBdao;
	long companyId;

	/**
	 * Compare name and password and type in DB login to the system
	 * return : Coupon Client Facade 
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clienttype)
			throws UserOrPasswNotCorrectException, CouponsDBException {
		long rc = compDBdao.login(name, password);

		companyId = rc;
		return this;
	}

	/**
	 * Default constructor of CompanyFacade
	 */
	public CompanyFacade() {
		compDBdao = new CompanyDBDAO();
		coupDBdao = new CouponDBDAO();
	}

	/**
	 * create new Coupon for the company
	 * 
	 * @param coupon
	 * @param companyid
	 * @throws CouponAllredyExistException - the coupon not found in the DB
	 * @throws CouponsDBException          - if there is any problem working with DB
	 */
	public void createCoupon(Coupon coupon, long companyid) throws CouponAllredyExistException, CouponsDBException {

		coupDBdao.createCoupon(coupon, companyid);

	}

	/**
	 * remove the Coupon
	 * 
	 * @param coupon
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	public void removeCoupon(Coupon coupon) throws CouponsDBException {
		coupDBdao.removeCoupon(coupon);
	}

	/**
	 * update the Coupon in DB
	 * 
	 * @param coupon
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	public void updateCoupon(Coupon coupon) throws CouponsDBException {
		coupDBdao.updateCoupon(coupon);

	}

	/**
	 * get the Company from DB
	 * 
	 * @param companyid
	 * @return company
	 * @throws CompanyNotFoundException -the company not found in the DB
	 * @throws CouponsDBException-      if there is any problem working with DB
	 */
	public Company getCompany(long companyid) throws CompanyNotFoundException, CouponsDBException {

		Company company = this.compDBdao.getCompany(companyid);

		if (company == null)
			throw new CompanyNotFoundException(" The Company Not Found = " + companyid, "CompanyFacade:getCompany");

		return company;

	}

	/**
	 * get Coupon by ID
	 * 
	 * @param couponid
	 * @return coupon id
	 * @throws CouponNotFoundException- the coupon not found in the DB
	 * @throws CouponsDBException-      if there is any problem working with DB
	 */
	public Coupon getCoupon(long couponid) throws CouponNotFoundException, CouponsDBException {

		Coupon c = coupDBdao.getCoupon(couponid);
		if (c == null) {
			throw new CouponNotFoundException(" The Coupon Not Found = " + couponid, "CompanyFacade:getCoupon");
		}
		return c;
	}

	/**
	 * Get llection of all the company coupons
	 * 
	 * @param companyid
	 * @return all coupons
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public Collection<Coupon> getAllCoupons(long companyid) throws CouponsDBException {
		return coupDBdao.getAllCoupons();

	}

	/**
	 * Collection of all coupons
	 * 
	 * @return all coupons
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public Collection<Coupon> getAllCoupons() throws CouponsDBException {
		return coupDBdao.getAllCoupons();

	}

	/**
	 * Get Collection of coupon by given type
	 * 
	 * @param type
	 * @return Coupon ByType
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public Collection<Coupon> getCouponByType(CouponType type) throws CouponsDBException {
		return coupDBdao.getCouponByType(type);

	}

	/**
	 * Collection of all company coupons by type
	 * 
	 * @param type
	 * @param companyid
	 * @return company coupon by type
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	public Collection<Coupon> getCouponOfCompanyByType(CouponType type, long companyid) throws CouponsDBException {
		return coupDBdao.getAllCouponsByCompIdAndType(companyid, type);

	}

	/**
	 * Collection of all company coupons by type
	 * 
	 * @param companyid
	 * @param price
	 * @return company coupon by type
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	public Collection<Coupon> getCouponsByCompIdPrice(long companyid, float price) throws CouponsDBException {
		return coupDBdao.getCouponsByCompIdPrice(companyid, price);

	}

	/**
	 * get Array list of Coupons By Company Id and EndDate
	 * 
	 * @param companyid
	 * @param date
	 * @return list of Coupons By Company Id EndDate
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	public ArrayList<Coupon> getCouponsByCompIdEndDate(long companyid, Date date) throws CouponsDBException {
		return coupDBdao.getCouponsByCompIdEndDate(companyid, date);
	}
}
