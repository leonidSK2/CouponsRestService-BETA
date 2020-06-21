package coupons.management.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.beans.Customer;
import coupons.management.db.ConnectionPool;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.CustomerAllredyExistException;
import coupons.management.exceptions.CustomerNotFoundException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * CustomerDBDAO implementation
 * 
 * @author Leonid S
 *
 */

public class CustomerDBDAO implements CustomerDAO {
	private ConnectionPool pool = ConnectionPool.getInstance();

	/**
	 * create new Customer in DB
	 */
	@Override
	public void createCustomer(Customer customer) throws CustomerAllredyExistException, CouponsDBException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			String sql = String.format("insert into Customer values(%s, '%s','%s')", customer.getId(),
					customer.getCustName(), customer.getPassword());

			int result = st.executeUpdate(sql);
			if (result == 0) {
				throw new CustomerAllredyExistException("Customer already exists" + customer.getCustName(),
						"CustomerDBDAO:createCustomer");
			}
			System.out.println("Customer created successfully");

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO :createCustomer ");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * remove customer and customer coupons from DB
	 */
	@Override
	public void removeCustomer(Customer customer) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {

			// begin transaction
			con.setAutoCommit(false);

			// PreparedStatement if safer (no sql injection) and better when using
			// transactions
			PreparedStatement st = con.prepareStatement("delete from Customer where id = ?");
			st.setLong(1, customer.getId());

			PreparedStatement st2 = con.prepareStatement("delete from Cust_Coupon where customerID = ?");
			st2.setLong(1, customer.getId());

			// try to execute the PreparedStatements
			st.executeUpdate();
			st2.executeUpdate();

			// if all the PreparedStatements where successful => commit changes (save)
			con.commit();
			con.setAutoCommit(true);
			System.out.println("Customer delete successfully");
		} catch (SQLException e) {
			// if en exception was found => rollback!
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new CouponsDBException(e1.getMessage(), "CustomerDBDAO :removeCustomer ");

			}
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO :removeCustomer ");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * update the Customer in DB
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			// String sql = String.format("update Company set email = %s, password = %s
			// where id = %d",
			// company.getEmail(), company.getPassword(), company.getId());

			String sql = String.format(" update Customer set CUSTOMER_NAME = '%s' ,PASSWORD ='%s'  where id = %d ",
					customer.getCustName(), customer.getPassword(), customer.getId());
			int res = st.executeUpdate(sql);
			if (res == 0) {
				throw new CouponsDBException("Error updating  customer", "update Customer()");
			}
			System.out.println("Customer updated successfully");
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO :updateCustomer ");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * get Customer from DB with customer id
	 */
	@Override
	public Customer getCustomer(long id) throws CustomerNotFoundException, CouponsDBException {
		Customer c = null;

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from Customer where id = " + id);
			if (rs.next()) {
				String name = rs.getString(2);
				String password = rs.getString(3);

				c = new Customer(id, name, password);
			} else {
				throw new CustomerNotFoundException("the customer not found" + id, "CustomerDBDAO:getCustomer");
			}
			System.out.println("Customer get successfully");

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO:getCustomer");
		} finally {
			pool.returnConnection(con);
		}

		return c;

	}

	/**
	 * get all customer from DB with array list
	 */
	@Override
	public ArrayList<Customer> getAllCustomers() throws CouponsDBException {
		ArrayList<Customer> customers = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from Customer");
			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				customers.add(new Customer(id, name, password));
			}
			System.out.println(" successfully get all customeres");
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO:getAllCustomers");
		} finally {
			pool.returnConnection(con);
		}

		return customers;
	}

	/**
	 * get Customer Coupons with customer id from DB
	 */
	@Override
	public ArrayList<Coupon> getCustomerCoupons(long customerID) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from coupon join Cust_coupon "
					+ "on coupon.id =  Cust_coupon.couponid " + "where  Cust_coupon.customerid = " + customerID);
			while (rs.next()) {
				long coupondId = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				CouponType type = CouponType.valueOf(rs.getString(6));
				coupons.add(new Coupon(coupondId, title, startDate, endDate, 0, 0, type, "", ""));
			}
			System.out.println(" get customer coupons successfully");
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO: getCustomerCoupons");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * customer login with customer name and customer password
	 */
	@Override
	public long login(String customerName, String password) throws UserOrPasswNotCorrectException, CouponsDBException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select ID , PASSWORD, CUSTOMER_NAME from Customer  where CUSTOMER_NAME like  '"
							+ customerName + "'");
			while (rs.next()) {
				String pass = rs.getString("PASSWORD");
				if (password.equals(pass)) {
					return rs.getLong("ID"); // name and password match!
				} // else {
					// return -1; // password incorrect
			}
			// }else { // no such companyName
			throw new UserOrPasswNotCorrectException("login faled for customer =" + customerName,
					"CustomerDBDAO: login");
			// }

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CustomerDBDAO: login");
		}

	}
}
