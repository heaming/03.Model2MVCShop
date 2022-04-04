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
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));	
		String tranCode = request.getParameter("tranCode");
		
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(tranNo);
		purchase.setTranCode(tranCode);
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.updateTranCode(purchase);
	
		return "redirect:/listSale.do?menu=manage";
	}

}
