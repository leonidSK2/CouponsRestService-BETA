package coupons.management.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import coupons.management.beans.Coupon;
import coupons.management.beans.CouponType;
import coupons.management.db.ConnectionPool;
import coupons.management.exceptions.CouponAllredyExistException;
import coupons.management.exceptions.CouponNotFoundException;
import coupons.management.exceptions.CouponsDBException;

/**
 * CouponDBDAO implementation
 * 
 * @author Leonid S
 *
 */
public class CouponDBDAO implements CouponDAO {
	private ConnectionPool pool = ConnectionPool.getInstance();

	/**
	 * create new Coupon and associate it to company
	 */
	@Override
	public void createCoupon(Coupon coupon, long companyid) throws CouponAllredyExistException, CouponsDBException {
		Connection con = pool.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("insert into coupon values(?, ?, ?, ?, ?, ?, ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			// notice that we added PreparedStatement.RETURN_GENERATED_KEYS//
			st.setLong(1, coupon.getId());
			st.setString(2, coupon.getTitle());

			Instant instant = coupon.getStartDate().toInstant();
			ZoneId zoneId = ZoneId.of("Israel");
			ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
			LocalDate localDate = zdt.toLocalDate();
			java.sql.Date sqlDate1 = java.sql.Date.valueOf(localDate);

			st.setDate(3, sqlDate1);

			Instant instant2 = coupon.getEndDate().toInstant();
			ZoneId zoneId2 = ZoneId.of("Israel");
			ZonedDateTime zdt2 = ZonedDateTime.ofInstant(instant2, zoneId2);
			LocalDate localDate2 = zdt2.toLocalDate();
			java.sql.Date sqlDate2 = java.sql.Date.valueOf(localDate2);

			st.setDate(4, sqlDate2);
			st.setInt(5, coupon.getAmount());
			st.setString(6, coupon.getType().toString());
			st.setString(7, coupon.getMessage());
			st.setDouble(8, coupon.getPrice());
			st.setString(9, coupon.getImage());

			st.executeUpdate();

			PreparedStatement st2 = con.prepareStatement("INSERT INTO Company_Coupon values (? ,? ) ",
					PreparedStatement.RETURN_GENERATED_KEYS);
			st2.setLong(1, companyid);
			st2.setLong(2, coupon.getId());

			int result = st2.executeUpdate();
			if (result == 0) {
				throw new CouponAllredyExistException("Coupon id name already exists", "" + coupon.getId());
			}
			System.out.println("Coupon created successfully");

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "" + coupon.getId());

		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * remove coupon from DB, also company and customer relations
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {

			// begin transaction
			con.setAutoCommit(false);

			// PreparedStatement if safer (no sql injection) and better when using
			// transactions
			PreparedStatement st = con.prepareStatement("delete from Company_Coupon where couponid = ?");
			st.setLong(1, coupon.getId());

			PreparedStatement st1 = con.prepareStatement("delete from Cust_Coupon where couponid = ?");
			st1.setLong(1, coupon.getId());

			PreparedStatement st2 = con.prepareStatement("delete from Coupon where ID = ?");
			st2.setLong(1, coupon.getId());

			// try to execute the PreparedStatements
			st.executeUpdate();
			st1.executeUpdate();
			st2.executeUpdate();

			// if all the PreparedStatements where successful => commit changes (save)
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new CouponsDBException(e1.getMessage(), "CouponDBDAO:removeCoupon");
			}

			throw new CouponsDBException(e.getMessage(), "CouponDBDAO:removeCoupon");

		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * update Coupon in DB
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"update Coupon set TITLE = ? , START_DATE = ? , END_DATE = ? , AMOUNT = ? , TYPE_ENUM = ? ,MESSAGE = ?,"
							+ " PRICE = ?, IMAGE = ? where ID = ? ");
			st.setString(1, coupon.getTitle());
			Instant instant = coupon.getStartDate().toInstant();
			ZoneId zoneId = ZoneId.of("Israel");
			ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
			LocalDate localDate = zdt.toLocalDate();
			java.sql.Date sqlDate1 = java.sql.Date.valueOf(localDate);

			st.setDate(3, sqlDate1);

			//Instant instant2 = coupon.getEndDate().toInstant();
			//ZoneId zoneId2 = ZoneId.of("Israel");
			//ZonedDateTime zdt2 = ZonedDateTime.ofInstant(instant2, zoneId2);
			LocalDate localDate2 = zdt.toLocalDate();
			java.sql.Date sqlDate2 = java.sql.Date.valueOf(localDate2);

			st.setDate(4, sqlDate2);
			st.setInt(4, coupon.getAmount());
			st.setString(5, coupon.getType().toString());
			st.setString(6, coupon.getMessage());
			st.setDouble(7, coupon.getPrice());
			st.setString(8, coupon.getImage());
			st.setLong(9, coupon.getId());
			int res = st.executeUpdate();
			if (res == 0) {
				throw new CouponsDBException("Error updating coupon", "updateCoupon()");
			}
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "Coupon.updateCoupon()");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * get coupon from DB with id
	 */
	@Override
	public Coupon getCoupon(long id) throws CouponNotFoundException, CouponsDBException {
		Coupon c = null;

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from Coupon where ID = " + id);

			if (rs.next()) {

				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				c = new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image);
			} else {
				throw new CouponNotFoundException("the Coupon not found", "CouponDBDAO:getCoupon");
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO:getCoupon");
		} finally {
			pool.returnConnection(con);
		}

		return c;
	}

	/**
	 * get all coupons from DB with array list
	 */
	@Override
	public ArrayList<Coupon> getAllCoupons() throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from Coupon");
			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO:getAllCoupons");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get all coupons by type from DB with array list
	 */
	@Override
	public ArrayList<Coupon> getCouponByType(CouponType type) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			// select * from Coupon where TYPE_ENUM = 'FOOD'
			ResultSet rs = st.executeQuery("select * from Coupon TYPE_ENUM = " + type.toString() + "'");

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String typeStr = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, typeStr);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);

				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO:getCouponByType");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * Associate Coupon to Customer with customer id and coupon id (purchase coupon)
	 */
	@Override
	public void associateToCustomer(long customerid, long couponid) throws CouponsDBException {
		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement("INSERT INTO Cust_Coupon   VALUES (? ,?  )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setLong(1, customerid);
			st.setLong(2, couponid);

			int res = st.executeUpdate();
			if (res == 0) {
				throw new CouponsDBException("Error updating coupon", "updateCoupon()");
			}
		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "Coupon.updateCoupon()");

		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * get all coupons by company id from DB
	 */
	@Override
	public ArrayList<Coupon> getAllCouponsByCompId(long companyid) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where id in ( select couponid from Company_Coupon where compid = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setLong(1, companyid);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getAllCouponsByCompId");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get All Coupons By Company Id And Type from DB
	 * 
	 * @param companyid
	 * @param type
	 * @return coupon
	 * @throws CouponsDBException
	 */
	public ArrayList<Coupon> getAllCouponsByCompIdAndType(long companyid, CouponType type) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where TYPE_ENUM  like ? and  id in ( select couponid from Company_Coupon where compid = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setString(1, type.toString());
			st.setLong(2, companyid);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type1 = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type1);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getAllCouponsByCompIdAndType");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get Coupons By Company Id Price from DB
	 * 
	 * @param companyid
	 * @param price
	 * @return
	 * @throws CouponsDBException
	 */
	public ArrayList<Coupon> getCouponsByCompIdPrice(long companyid, float price) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where price <= ?  and  id in ( select couponid from Company_Coupon where compid = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setFloat(1, price);
			st.setLong(2, companyid);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type1 = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type1);
				String message = rs.getString(7);
				double price1 = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price1, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getCouponsByCompIdPrice");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get Coupons By Company Id with End Date from DB
	 * 
	 * @param companyid
	 * @param date
	 * @return coupon
	 * @throws CouponsDBException
	 */
	public ArrayList<Coupon> getCouponsByCompIdEndDate(long companyid, Date date) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where END_DATE <= ?  and  id in ( select couponid from Company_Coupon where compid = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setDate(1, date);
			st.setLong(2, companyid);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type1 = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type1);
				String message = rs.getString(7);
				double price1 = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price1, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getCouponsByCompIdEndDate");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get All Coupons By Customer Id from DB
	 */
	@Override
	public ArrayList<Coupon> getAllCouponsByCustomerId(long customerid) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where id in ( select couponid from Cust_Coupon where customerID = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setLong(1, customerid);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getAllCouponsByCustomerId");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get All Coupons By Customer Id And coupon Type from DB
	 * 
	 * @param customerid
	 * @param type
	 * @return coupon
	 * @throws CouponsDBException
	 */
	public ArrayList<Coupon> getAllCouponsByCustomerIdAndType(long customerid, CouponType type)
			throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where TYPE_ENUM  like ? and  id in ( select couponid from Cust_Coupon where customerID = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setString(1, type.toString());
			st.setLong(2, customerid);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type1 = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type1);
				String message = rs.getString(7);
				double price = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getAllCouponsByCustomerIdAndType");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * get All Coupons By Customer Id And Price from DB
	 * 
	 * @param customerid
	 * @param price
	 * @return coupon
	 * @throws CouponsDBException
	 */
	public ArrayList<Coupon> getAllCouponsByCustomerIdAndPrice(long customerid, float price) throws CouponsDBException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {

			PreparedStatement st = con.prepareStatement(
					"SELECT * FROM Coupon where price <= ?  and  id in ( select couponid from Cust_Coupon where customerID  = ? )",
					PreparedStatement.RETURN_GENERATED_KEYS);

			st.setFloat(1, price);
			st.setLong(2, customerid);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date startDate = rs.getDate(3);
				Date endDate = rs.getDate(4);
				int amount = rs.getInt(5);
				String type1 = rs.getString(6);
				CouponType couponType = CouponType.valueOf(CouponType.class, type1);
				String message = rs.getString(7);
				double price1 = rs.getDouble(8);
				String image = rs.getString(9);
				coupons.add(new Coupon(id, title, startDate, endDate, price1, amount, couponType, message, image));
			}

		} catch (SQLException e) {
			throw new CouponsDBException(e.getMessage(), "CouponDBDAO : getAllCouponsByCustomerIdAndPrice");
		} finally {
			pool.returnConnection(con);
		}

		return coupons;
	}

}
