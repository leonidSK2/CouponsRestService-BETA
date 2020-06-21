package coupons.management.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import coupons.management.app.CouponSystem;
import coupons.management.beans.Company;
import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.beans.Customer;
import coupons.management.clients.AdminFacade;
import coupons.management.clients.ClientType;
import coupons.management.clients.CompanyFacade;
import coupons.management.clients.CustomerFacade;
import coupons.management.exceptions.CompanyAllredyExistException;
import coupons.management.exceptions.CompanyNotFoundException;
import coupons.management.exceptions.CouponAllredyExistException;
import coupons.management.exceptions.CouponExpiredException;
import coupons.management.exceptions.CouponNotFoundException;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.CustomerAllredyExistException;
import coupons.management.exceptions.CustomerNotFoundException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * Test Project of a project
 * @author Owner
 *
 */
public class TestProject1 {

	public static void main(String[] args)   {
		
		AdminFacade adminFacade = null;
		CustomerFacade customerfacade = null;
		CustomerFacade customerfacade2 = null;
		CompanyFacade companyfacade = null;
		CompanyFacade companyfacade2 = null;

		String companyName = "Watches";
		long companyId = 555;
		String companyPassword = "12345";
		String companyName2 = "mania";
		long companyId2 = 444;
		String companyPassword2 = "123456";

		String customerName = "Leonid S";
		String customerPassword = "simba";
		long customerId = 12345678;
		
		String customerName2 = "avital S";
		String customerPassword2 = "kisa";
		long customerId2 = 87654321;
		

		long couponId1 = 123;
		long couponId2 = 321;
		long couponIdExp = 124;
		long couponId4 = 2;
		Date couponStartDate = new GregorianCalendar(2019, 0, 1).getTime();
		Date couponEndDate = new GregorianCalendar(2019, 11, 31).getTime();

		CouponSystem cs = CouponSystem.getInstance();

		// login into admin facade
		try {
			adminFacade = (AdminFacade) cs.Login("admin", "1234", ClientType.ADMIN);
		} catch (UserOrPasswNotCorrectException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		Company company = new Company(companyId, companyName, companyPassword, "leonidSK1@watches.com");
		Customer customer = new Customer(customerId, customerName, customerPassword);
		Customer customer2 = new Customer(customerId2, customerName2, customerPassword2);
		Company company2 = new Company(companyId2, companyName2, companyPassword2, "leonidSK1@mania.com");

		// try to remove company, if it was created in previous run. it removes also all
		// company coupons
		try {
			adminFacade.removeCompany(company);
			adminFacade.removeCompany(company2);
			System.out.println("company removed successfully: " + company);

		} catch (CompanyNotFoundException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		// try to remove customer
		try {
			adminFacade.removeCustomer(customer);
			System.out.println("customer removed successfully: " + customer);
			adminFacade.removeCustomer(customer2);
			System.out.println("customer removed successfully: " + customer2);
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		// here data base should be empty

		// create company
		try {

			adminFacade.createCompany(company);
			System.out.println("Company created succefully: " + company);
			adminFacade.createCompany(company2);
			System.out.println("Company created succefully: " + company2);
		} catch (CompanyAllredyExistException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		// get company facade
		try {
			companyfacade = (CompanyFacade) cs.Login(companyName, companyPassword, ClientType.COMPANY);
			companyfacade2 = (CompanyFacade) cs.Login(companyName2, companyPassword2, ClientType.COMPANY);
		} catch (UserOrPasswNotCorrectException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		// create customer
		try {
			adminFacade.createCustomer(customer);
			adminFacade.createCustomer(customer2);
			Customer customer3 = adminFacade.getCustomer(customerId);
			System.out.println(customer3);
			Customer customer4 = adminFacade.getCustomer(customerId2);
			System.out.println(customer4);

		} catch (CustomerAllredyExistException | CouponsDBException e) {
			System.out.println(e.getMessage());
		} catch (CustomerNotFoundException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}

		Coupon coupon1 = new Coupon(couponId1, "rolex", couponStartDate, couponEndDate, 55000.5, 5, CouponType.JEWERLY,
				"rolex watch coupon", "rolex.jpg");
		Coupon coupon2 = new Coupon(couponId2, "patek", couponStartDate, couponEndDate, 555000.5, 5, CouponType.JEWERLY,
				"patek watch coupon", "patek.jpg");
		Coupon couponExp = new Coupon(couponIdExp, "AP", couponStartDate, couponStartDate, 55.5, 5, CouponType.JEWERLY,
				"AP watch coupon", "solut.jpg");
		Coupon coupon4 = new Coupon(couponId4, "Apple", couponStartDate, couponEndDate, 1.5, 5, CouponType.FOOD,
				"Apple coupon", "Apple.jpg");
		// create coupons in company Facade
		try {
			companyfacade.createCoupon(coupon1, companyId);
			companyfacade.createCoupon(coupon2, companyId);
			companyfacade.createCoupon(couponExp, companyId);
			companyfacade2.createCoupon(coupon4, companyId2);
		} catch (CouponAllredyExistException | CouponsDBException e) {
			System.out.println(e.getMessage());
		}

		// get customer facade with wrong password, get UserOrPasswNotCorrectException
		try {
			customerfacade = (CustomerFacade) cs.Login(customerName, "sdsd", ClientType.CUSTOMER);
		} catch (UserOrPasswNotCorrectException | CouponsDBException e) {
			System.out.println("Exception because not correct password was provided. " + e.getMessage());
		}

		// get customer facade with correct password, get CustomerFacade
		try {
			customerfacade = (CustomerFacade) cs.Login(customerName, customerPassword, ClientType.CUSTOMER);
			System.out.println("Successfully get Customer facade");
			customerfacade2 = (CustomerFacade) cs.Login(customerName2, customerPassword2, ClientType.CUSTOMER);
			System.out.println("Successfully get Customer facade 2");
		} catch (UserOrPasswNotCorrectException | CouponsDBException e) {
			System.out.println(e.getMessage());
		}

		// get coupons and purchase them
		try {
			coupon1 = companyfacade.getCoupon(couponId1);
			coupon2 = companyfacade.getCoupon(couponId2);
			coupon4 = companyfacade2.getCoupon(couponId4);

			
			customerfacade.purchaseCoupon(coupon1);
			System.out.println("Coupon purshased succefully: " + coupon1);
			customerfacade2.purchaseCoupon(coupon2);
			System.out.println("Coupon purshased succefully: " + coupon2);
			customerfacade.purchaseCoupon(coupon4);
			System.out.println("Coupon purshased succefully: " + coupon4);
		} catch (CouponNotFoundException | CouponsDBException e) {
			System.out.println(e.getMessage());
		} catch (CouponExpiredException e) {
			System.out.println(e.getMessage());
		}

		// get expired coupon and try to purchase it
		try {
			couponExp = companyfacade.getCoupon(couponIdExp);
			customerfacade.purchaseCoupon(couponExp);
			System.out.println("Coupon purshased succefully: " + coupon1);

		} catch (CouponNotFoundException | CouponsDBException e) {
			System.out.println(e.getMessage());
		} catch (CouponExpiredException e) {
			System.out.println("Failed to purshase expired coupon : " + couponExp);
		}

		try {
			ArrayList<Coupon> couponsByType = customerfacade.getAllPurchasedCouponByType(CouponType.FOOD);
			System.out.println("get all Purchased Coupon By Type = FOOD for customer 1 ");
			for (Coupon coupon : couponsByType) {
				System.out.println(coupon);
			}
		} catch (CouponsDBException e) {

			System.out.println(e.getMessage() + " " + e.getSource());

		}
		try {
			ArrayList<Coupon> couponsByPrice = customerfacade.getAllPurchasedCouponByPrice(2);
			System.out.println("get all Purchased Coupon By Price <= 2 for customer 1");
			for (Coupon coupon : couponsByPrice) {
				System.out.println(coupon);
			}
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}
		
		try {
			ArrayList<Coupon> couponsByType = customerfacade.getAllPurchasedCouponByType(CouponType.JEWERLY);
			System.out.println("get all Purchased Coupon By Type = JEWERLY for customer 1");

			for (Coupon coupon : couponsByType) {
				System.out.println(coupon);
			}
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}
		
		try {
			ArrayList<Coupon> couponsByType = customerfacade2.getAllPurchasedCouponByType(CouponType.JEWERLY);
			System.out.println("get all Purchased Coupon By Type = JEWERLY for customer 2");

			for (Coupon coupon : couponsByType) {
				System.out.println(coupon);
			}
		} catch (CouponsDBException e) {
			System.out.println(e.getMessage() + " " + e.getSource());
		}
		
		// Exit
		System.exit(0);
	}

}
