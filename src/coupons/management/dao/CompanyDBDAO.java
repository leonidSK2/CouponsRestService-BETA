package coupons.management.dao;

import java.sql.*;
import java.util.ArrayList;

import coupons.management.beans.Company;
import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.db.ConnectionPool;
import coupons.management.exceptions.CompanyAllredyExistException;
import coupons.management.exceptions.CompanyNotFoundException;
import coupons.management.exceptions.CouponsDBException;
import coupons.management.exceptions.UserOrPasswNotCorrectException;

/**
 * CompanyDBDAO implementation
 * 
 * @author Leonid S
 *
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool = ConnectionPool.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see coupon.management.dao.CompanyDAO#createCompany(coupons.management.beans.
	 * Company)
	 */
	@Override
	public void createCompany(Company company) throws CompanyAllredyExistException, CouponsDBException {

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			String sql = String.format("insert into company values(%s, '%s', '%s','%s')", company.getId(),
					company.getCompanyName(), company.getPassword(), company.getEmail());

			int result = st.executeUpdate(sql);
			if (result == 0) {
				throw new CompanyAllredyExistException("Company name already exists" + company.getCompanyName(),
						"CompanyDBDAO:createCompany");
			}
			System.out.println("Company created successfully");
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CompanyDBDAO:createCompany");
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * remove company and company coupons from DB
	 */
	@Override
	public void removeCompany(Company company) throws CompanyNotFoundException, CouponsDBException {
		Connection con = pool.getConnection();
		try {

			// begin transaction
			con.setAutoCommit(false);

			// PreparedStatement if safer (no sql injection) and better when using
			// transactions
			PreparedStatement st = con.prepareStatement("delete from company where id = ?");
			st.setLong(1, company.getId());

			PreparedStatement st2 = con.prepareStatement("delete from company_coupon where compid = ?");
			st2.setLong(1, company.getId());

			// try to execute the PreparedStatements
			st.executeUpdate();
			st2.executeUpdate();

			// if all the PreparedStatements where successful => commit changes (save)
			con.commit();
			con.setAutoCommit(true);

			System.out.println("Company removed successfully");

		} catch (SQLException e) {
			// if en exception was found => rollback!
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new CouponsDBException(e1.getMessage(), "CompanyDBDAO:removeCompany");
			}
			throw new CouponsDBException(e.getMessage(), "CompanyDBDAO:removeCompany");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * update Company in DB (email , password , id )
	 */
	@Override
	public void updateCompany(Company company) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			String sql = String.format("update company set email ='%s', password = '%s' where id = %d",
					company.getEmail(), company.getPassword(), company.getId());
			st.executeUpdate(sql);

			System.out.println("Company updated successfully");
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * get company by id from DB
	 */
	@Override
	public Company getCompany(long id) throws CompanyNotFoundException, CouponsDBException {
		Company c = null;

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from company where id = " + id);

			if (false == rs.next())
				throw new CompanyNotFoundException(null, null);

			String name = rs.getString(2);
			String password = rs.getString(3);
			String email = rs.getString(4);
			c = new Company(id, name, password, email);

			System.out.println("successfully get company");
		} catch (SQLException e) {
			throw new CouponsDBException("Faled To Get Company From Db , id =  " + id, "CompanyDBDAO:getCompany");
		} finally {
			pool.returnConnection(con);
		}

		return c;
	}

	/**
	 * get all companies with array list from DB
	 */
	@Override
	public ArrayList<Company> getAllCompanies() throws CouponsDBException {

		ArrayList<Company> companies = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from company");
			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);
				companies.add(new Company(id, name, password, email));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "ConpanyDBDAO:getAllCompanies");
		} finally {
			pool.returnConnection(con);
		}

		return companies;
	}

	/**
	 * get Company Coupons by company id from DB
	 */
	@Override
	public ArrayList<Coupon> getCompanyCoupons(long companyid) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from coupon join company_coupon on coupon.ID = company_coupon.couponid "
							+ "where company_coupon.compid = " + companyid);
			while (rs.next()) {
				long coupondId = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				CouponType type = CouponType.valueOf(rs.getString(6));
				coupons.add(new Coupon(coupondId, title, startDate, endDate, 0, 0, type, "", ""));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CompanyDBDAO:getCompanyCoupons");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * company login with company name and company password
	 */
	@Override
	public long login(String companyName, String password) throws UserOrPasswNotCorrectException, CouponsDBException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select id, password, company_Name from company "
					+ "where company_Name like '" + companyName + "'");

			while (rs.next()) {
				String pass = rs.getString("password");
				if (password.equals(pass)) {

					return rs.getLong("id"); // name and password match!
				}

			}

			// no such companyName
			throw new UserOrPasswNotCorrectException("login problem for company " + companyName, " CompanyDBDAO:login");

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CompanyDBDAO:login");

		}

	}

}
