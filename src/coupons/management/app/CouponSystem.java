package coupons.management.app;

import coupons.management.clients.AdminFacade;

import coupons.management.clients.ClientType;
import coupons.management.clients.CompanyFacade;
import coupons.management.clients.CouponClientFacade;
import coupons.management.clients.CustomerFacade;
//import coupons.management.dao.CompanyDBDAO;
//import coupons.management.dao.CouponDBDAO;
//import coupons.management.dao.CustomerDBDAO;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * @author Leonid S Coupon System main class of the server.Singletone
 */
public class CouponSystem {
	private static CouponSystem instance = new CouponSystem();
	DailyCouponExpirationTask dailytask = new DailyCouponExpirationTask();

	/**
	 * default constructor of coupon system
	 */
	private CouponSystem() {
		Thread w1 = new Thread(dailytask);

		w1.start();
	}

	/**
	 * get single Instance of the Coupon System
	 * 
	 * @author leonid
	 *
	 */
	public static CouponSystem getInstance() {
		return instance;
	}

	/**
	 * login to the system
	 * 
	 * @param name       - name of the customer , company or admin
	 * @param password   - password of customer , company or admin
	 * @param clienttype : customer , company or admin
	 * @return CouponClientFacade according to client type
	 * @throws UserOrPasswNotCorrectException- if user name of password are not
	 *                                         match to defined in DB
	 * @throws CouponsDBException              if there is any problem working with
	 *                                         DB
	 */
	public CouponClientFacade Login(String name, String password, ClientType clienttype)
			throws UserOrPasswNotCorrectException, CouponsDBException {
		CouponClientFacade facade;
		switch (clienttype) {
		case ADMIN:
			facade = new AdminFacade();
			break;
		case COMPANY:
			facade = new CompanyFacade();
			break;
		case CUSTOMER:
			facade = new CustomerFacade();
			break;
		default:
			return null;
		}

		return facade.login(name, password, clienttype);

	}

}
