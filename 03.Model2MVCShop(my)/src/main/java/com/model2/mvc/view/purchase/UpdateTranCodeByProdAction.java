package com.model2.mvc.view.purchase;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeByProdAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String menu = request.getParameter("menu");
		String tranCode = request.getParameter("tranCode");	
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

		ProductService prodService = new ProductServiceImpl();	
		Product product = prodService.getProduct(prodNo);
		
		PurchaseService tranService = new PurchaseServiceImpl();
		List<Purchase> saleList = tranService.getSales(prodNo);
		int tranNo = 0;
		
		for(Purchase purchase : saleList) {
			if(purchase.getTranCode().equals("001")) {
				purchase.setTranCode("002");
			} else if(purchase.getTranCode().equals("002")) {
				purchase.setTranCode("003");
			} else if(purchase.getTranCode().equals("003")) {
				purchase.setTranCode("004");
			}
			
			tranNo = purchase.getTranNo();
			product.setProTranCode(purchase.getTranCode());
			tranService.updateTranCode(purchase);
			System.out.println("[UpdateTranCodeByProdAction] :: "+ purchase);
		}
		
		return "redirect:/updateTranCode.do?prodNo="+prodNo+"&tranNo="+tranNo+"&menu="+menu;
	}

}
