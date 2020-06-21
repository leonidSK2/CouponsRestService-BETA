



package coupons.management.dao;

import java.util.ArrayList;

import coupons.management.beans.Company;
import coupons.management.beans.Coupon;
import coupons.management.exceptions.CompanyAllredyExistException;
import coupons.management.exceptions.CompanyNotFoundException;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * interface CompanyDAO
 * @author Leonid S
 *
 */
public interface CompanyDAO {
	/**
	 * create new Company in DB
	 * @param company
	 * @throws CouponsDBException - if there is any problem working with DB
	 * @throws CompanyAllredyExistException - the Company All redyExist in DB
	 */
	void createCompany(Company company) throws CouponsDBException, CompanyAllredyExistException;
	/**
	 * remove Company from  DB
	 * @param company
	 * @throws CompanyNotFoundException - the company not found in DB
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	void removeCompany(Company company) throws CompanyNotFoundException, CouponsDBException;
	/**
	 * update Company in DB
	 * 
	 * @param company
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	void updateCompany(Company company) throws CouponsDBException;
	/**
	 * get company from DB 
	 * @param id
	 * @return company 
	 * @throws CompanyNotFoundException -  the company not found from DB
	 * @throws CouponsDBException -  if there is any problem working with DB
	 */
	Company getCompany(long id) throws CompanyNotFoundException, CouponsDBException;
	/**
	 * Get array list  of all companies 
	 * @return company 
	 * @throws CouponsDBException -  if there is any problem working with DB
	 */
	ArrayList<Company> getAllCompanies() throws CouponsDBException;
	/**
	 * Get  array list of all company coupons
	 * @param id
	 * @return company 
	 * @throws CouponsDBException-  if there is any problem working with DB
	 */
	ArrayList<Coupon> getCompanyCoupons(long id) throws CouponsDBException;
	/**
	 * company login , compare company name and password in DB
	 * @param companyName
	 * @param password
	 * @return
	 * @throws UserOrPasswNotCorrectException - the user name or the password not correct 
	 * @throws CouponsDBException - if there is any problem working with DB
	 */
	long login(String companyName, String password) throws UserOrPasswNotCorrectException, CouponsDBException;
	
}
