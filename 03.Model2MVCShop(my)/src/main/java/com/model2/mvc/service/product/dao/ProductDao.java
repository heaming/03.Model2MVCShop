package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Product;

public class ProductDao {

	// field
	
	
	// ctor
	public ProductDao() {
	}
	
	
	// method
	public Product findProduct(int prodNo) throws Exception {
			
			Connection con = DBUtil.getConnection();
			
			String sql = "SELECT * FROM product "
					+ "WHERE prod_no = ?";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, prodNo);
			
			ResultSet rs = stmt.executeQuery();
			
			Product product = null;
			
			while(rs.next()) {
				product = new Product();
				
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				product.setProdDetail(rs.getString("prod_detail"));
				product.setManuDate(rs.getString("manufacture_day"));
				product.setPrice(rs.getInt("price"));
				product.setFileName(rs.getString("image_file"));
				product.setRegDate(rs.getDate("reg_date"));
			}
			
			rs.close();
			stmt.close();
			con.close();
			
			return product;
		}
		
		
		public Map<String, Object> getProductList(Search search) throws Exception {
			
			Map<String , Object>  map = new HashMap<String, Object>();
			
			Connection con = DBUtil.getConnection();
			
			// TODO ���� �ٲٱ� 1�� �����ؼ� makeSQL���������� ���ư���
			String sql = "SELECT * FROM product";		
			if(search.getSearchCondition() != null) {
				if(search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ){
					sql += " WHERE prod_no LIKE '%" + search.getSearchKeyword() + "%'";
				} else if(search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("") ) {
					sql += " WHERE prod_name LIKE '%" + search.getSearchKeyword() + "%'";
				} else if(search.getSearchCondition().equals("2") &&  !search.getSearchKeyword().equals("") ) {
					sql += " WHERE price LIKE '%" + search.getSearchKeyword() + "%'";
				}
			} 
			sql += " ORDER BY prod_no";
			
			System.out.println("ProductDAO::Original SQL :: " + sql);
			
			// TotalCount GET
			int totalCount = this.getTotalCount(sql);
			System.out.println("ProductDAO :: totalCount  :: " + totalCount);
			
			
			// CurrentCount GET
			sql = makeCurrentPageSql(sql, search);
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			System.out.println(search);
			
			List<Product> list = new ArrayList<Product>();
			
			while(rs.next()) {
				Product product = new Product();
				
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				product.setProdDetail(rs.getString("prod_detail"));
				product.setManuDate(rs.getString("manufacture_day"));
				product.setPrice(rs.getInt("price"));
				product.setFileName(rs.getString("image_file"));
				product.setRegDate(rs.getDate("reg_date"));
				
				list.add(product);				
			}
			
			// totalCount ����
			map.put("totalCount", new Integer(totalCount));
			
			// currentPage�� Product�� List ����
			map.put("list", list);
			
			rs.close();
			stmt.close();
			con.close();
			
			return map;
		}
		
		
		public void insertProduct(Product product) throws Exception {
			
			Connection con = DBUtil.getConnection();
			
			String sql = "INSERT INTO product "
					+ "VALUES (seq_product_prod_no.nextval, ?, ? , ?, ?, ?, SYSDATE)";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, product.getProdName());
			stmt.setString(2, product.getProdDetail());
			stmt.setString(3, product.getManuDate().replace("-",""));
			stmt.setInt(4, product.getPrice());
			stmt.setString(5, product.getFileName());
			stmt.executeUpdate();
			
			stmt.close();
			con.close();
		}
		
		
		public void updateProduct(Product product) throws Exception {
			
			Connection con = DBUtil.getConnection();
			
			String sql = "UPDATE product SET prod_name = ?,  prod_detail = ?, \r\n"
					+ "manufacture_day = ?,\r\n"
					+ "price = ?,\r\n"
					+ "image_file=?   \r\n"
					+ "WHERE prod_no=?";
			
			PreparedStatement stmt = con.prepareStatement(sql);
	
			stmt.setString(1, product.getProdName());
			stmt.setString(2, product.getProdDetail());
			stmt.setString(3, product.getManuDate());
			stmt.setInt(4, product.getPrice());
			stmt.setString(5, product.getFileName());
			stmt.setInt(6, product.getProdNo());
			stmt.executeUpdate();
			
			stmt.close();
			con.close();		
		}	
		
		
		// �Խ��� page ó���� ���� ��ü ROW(totalCount)
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
		

		// �Խ��� currentPage Row
		private String makeCurrentPageSql(String sql , Search search){
			sql = 	"SELECT * "+ 
						"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
										" 	FROM (	"+sql+" ) inner_table "+
										"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
						"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
			
			System.out.println("ProductDAO :: make SQL :: "+ sql);	
			
			return sql;
		}
		
}
