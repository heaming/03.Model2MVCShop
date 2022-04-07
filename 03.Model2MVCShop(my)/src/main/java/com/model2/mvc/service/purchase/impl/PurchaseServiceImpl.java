package com.model2.mvc.service.purchase.impl;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDao;

public class PurchaseServiceImpl implements PurchaseService{

	// field
	private PurchaseDao purchaseDao;
	//private ProductDao productDao;
	
	// ctor
	public PurchaseServiceImpl() {
		purchaseDao = new PurchaseDao();
		//productDao = new ProductDao();
	}

	
	// method
	@Override
	public Purchase addPurchase(Purchase purchase) throws Exception {
		purchaseDao.insertPurchase(purchase);
		return purchase;
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDao.findPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		return purchaseDao.getPurchaseList(search, userId);
	}
	
	@Override
	public Map<String, Object> getUserSaleList(Search search, String userId) throws Exception {
		return purchaseDao.getUserSaleList(search, userId);
	}

	@Override
	public Map<String, Object> getSaleList(Search search) throws Exception {
		return purchaseDao.getSaleList(search);
	}

	@Override
	public Purchase updatePurchase(Purchase purchase) throws Exception {
		purchaseDao.updatePurchase(purchase);
		return purchase;
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		purchaseDao.updateTranCode(purchase);
		
		//String proTranCode = purchase.getTranCode();
		
		//purchase.getPurchaseProd().setProTranCode(proTranCode);
	}
	
}
