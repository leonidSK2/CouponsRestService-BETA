package coupons.management.dao;

import java.util.ArrayList;

import coupons.management.beans.Coupon;
import coupons.management.beans.Customer;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.CustomerAllredyExistException;
import coupons.management.exceptions.CustomerNotFoundException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;
/**
 * CustomerDAO implementation
 * @author Leonid S
 *
 */
public interface CustomerDAO {
	/**
	 * create new Customer in DB
	 * @param customer
	 * @throws CustomerAllredyExistException -  the customer allredy exist in DB
	 * @throws CouponsDBException -   if there is any problem working with DB
	 */
	void createCustomer(Customer customer) throws  CustomerAllredyExistException, CouponsDBException;
	/**
	 * remove  Customer from DB 
	 * @param customer
	 * @throws CouponsDBException -  if there is any problem working with DB
	 */
	void removeCustomer(Customer customer) throws  CouponsDBException;
	/**
	 * update Customer in DB
	 * @param customer
	 * @throws CouponsDBException -  if there is any problem working with DB
	 */
	void updateCustomer(Customer customer) throws CouponsDBException;
	/**
	 * get Customer from DB 
	 * @param id
	 * @return customer
	 * @throws CustomerNotFoundException - the customer not found in DB
	 * @throws CouponsDBException -  if there is any problem working with DB
	 */
	Customer getCustomer(long id) throws CustomerNotFoundException, CouponsDBException;
	/**
	 * get array list of All Customers
	 * @return customer 
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	ArrayList<Customer> getAllCustomers() throws CouponsDBException;
	/**
	 * get array list of all customer coupons
	 * @param id
	 * @return customer 
	 * @throws CouponsDBException- if there is any problem working with DB
	 */
	ArrayList<Coupon> getCustomerCoupons(long id) throws CouponsDBException;
	/**
	 * customer login - compare customer name and password
	 * @param customerName
	 * @param password
	 * @return login(name , password)
	 * @throws UserOrPasswNotCorrectException - the user name or the password not correct 
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	long login(String customerName, String password) throws UserOrPasswNotCorrectException, CouponsDBException;
}
