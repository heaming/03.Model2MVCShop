package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String menu = request.getParameter("menu");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

		Product product = new Product();

		product.setProdNo(prodNo);
		product.setProdCode(Integer.parseInt(request.getParameter("prodeCode")));
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setDueDate(request.getParameter("dueDate").replace("-",""));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setCost(Integer.parseInt(request.getParameter("cost")));
		product.setFileName(request.getParameter("fileName"));
		
		ProductService service = new ProductServiceImpl();				
		service.updateProduct(product);
	
		return "redirect:/getProduct.do?prodNo="+prodNo+"&menu="+menu;
	}

}
