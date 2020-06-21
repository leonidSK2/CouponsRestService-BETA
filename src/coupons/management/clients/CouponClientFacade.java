package coupons.management.clients;

import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * interface CouponClientFacade
 * login to the system
 * @author leonid S 
 * 
 */
public interface CouponClientFacade {
	public CouponClientFacade login(String name, String password, ClientType clienttype)
			throws UserOrPasswNotCorrectException, CouponsDBException;

}
