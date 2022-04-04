package com.model2.mvc.service.product.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDao;



public class ProductServiceImpl implements ProductService {
	
	//field
	private ProductDao productDao;
	
	
	//ctor
	public ProductServiceImpl() {
		productDao = new ProductDao();
	}
	
	
	//method
	public Product addProduct(Product product) throws Exception {
		productDao.insertProduct(product);
		return product;
	};
	
	public Product getProduct(int prodNo) throws Exception {
		return productDao.findProduct(prodNo);
	}
	
	public Map<String, Object> getProductList(Search search) throws Exception {	
		return productDao.getProductList(search);
	}
	
	public Product updateProduct(Product product) throws Exception {
		productDao.updateProduct(product); 
		return product;
	}
}
