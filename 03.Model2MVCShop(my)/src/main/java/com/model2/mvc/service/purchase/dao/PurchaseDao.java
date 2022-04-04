package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.user.dao.UserDao;

public class PurchaseDao {
	
	// ctor
	public PurchaseDao() {
	}
	
	// method
	public Purchase findPurchase(int tranNo) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from transaction where tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);
		
		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		while(rs.next()) {			
			purchase = new Purchase();
			
			purchase.setTranNo(tranNo);
			purchase.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
			purchase.setBuyer(new UserDao().findUser(rs.getString("buyer_id")));
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
		}
		
		con.close();
		
		return purchase;
		
	}
	

	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction"+
				" WHERE buyer_id = '"+ userId + "' ORDER BY tran_no";
		
		System.out.println("PurchaseDao:: Original SQL :: " + sql);

		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDao:: totalCount :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = con.prepareStatement(sql, 
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery();
		
		System.out.println(search);
		
		List<Purchase> list = new ArrayList<Purchase>();
		
		while(rs.next()) {
			Purchase purchase = new Purchase();
			
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
			purchase.setBuyer(new UserDao().findUser(userId));
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			
			list.add(purchase);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		stmt.close();
		con.close();

		return map;		
	}
	
	/* TODO saleList 페이지 만들고 구현*/
	public Map<String, Object> getSaleList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction";
		
		if (search.getSearchCondition() != null) {
			if ( search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE buyer_id LIKE '%" + search.getSearchKeyword()+"%'";
			} else if ( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_no LIKE '%" + search.getSearchKeyword()+"%'";
			}
		}
		
		sql += " ORDER BY order_data desc";
		
		System.out.println("PurchaseDao::Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("UserDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
	
		System.out.println(search);
				
		List<Purchase> list = new ArrayList<Purchase>();
		
		while(rs.next()) {
			Purchase purchase = new Purchase();
					
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
			purchase.setBuyer(new UserDao().findUser(rs.getString("buyer_id")));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setDivyDate(rs.getString("dlvy_date") != null ? rs.getString("dlvy_date") : "");
			
			list.add(purchase);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
		
	public void insertPurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction "
				+ "VALUES (seq_transaction_tran_no.nextval, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?)";

		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		stmt.setString(2, purchase.getBuyer().getUserId());
		stmt.setString(3, purchase.getPaymentOption());
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDivyAddr());
		stmt.setString(7, purchase.getDivyRequest());
		stmt.setString(8, purchase.getTranCode());
		stmt.setDate(9, Date.valueOf(purchase.getDivyDate()));
		stmt.executeUpdate();
		
		con.close();		
	}
	
	
	public void updatePurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET payment_option = ?,"
				+ "receiver_name = ?,"
				+ "receiver_phone = ?,"
				+ "demailaddr = ?,"
				+ "dlvy_request = ?,"
				+ "dlvy_date = ?"
				+ "WHERE tran_no = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDivyAddr());
		stmt.setString(5, purchase.getDivyRequest());
		stmt.setString(6, purchase.getDivyDate());
		stmt.setInt(7, purchase.getTranNo());
		stmt.executeUpdate();
		
		con.close();	
	}
	
	
	public void updateTranCode(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET tran_status_code = ?"
				+ "WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getTranNo());
		stmt.executeUpdate();
		
		stmt.close();
		con.close();	
	}
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("UserDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

}
