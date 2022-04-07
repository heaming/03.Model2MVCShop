package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.dao.UserDao;

public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		
		purchase.setPurchaseProd(new ProductDao().findProduct(Integer.parseInt(request.getParameter("prodNo"))));
		purchase.setBuyer(new UserDao().findUser(request.getParameter("buyerId")));
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyMessage(request.getParameter("receiverMsg"));
		//purchase.setDivyDate(System.currentTimeMillis());
		purchase.setTranCode("001");

		System.out.println(purchase.getPurchaseProd().getProdNo());
		System.out.println(purchase);
			
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(purchase);
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
}
