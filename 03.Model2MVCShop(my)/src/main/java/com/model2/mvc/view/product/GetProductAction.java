package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		String menu = request.getParameter("menu");
		String history = null;

		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(prodNo);

		Cookie[] cookies = request.getCookies();

		if (cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("history")) {
					history = cookie.getValue();
					history.replace("null", "");
				}
			}
		}
		
		history += "&" + new Integer(product.getProdNo()).toString();
		System.out.println("[GetProductAction.java] history :: "+history); // debugging

		Cookie cookie = new Cookie("history", history);

		request.setAttribute("product", product);
		request.setAttribute("menu", menu);
		response.addCookie(cookie);

		if(menu.equals("manage")) {
			return "forward:/updateProductView.do?prodNo="+prodNo+"&menu=manage";
		} else {
			return "forward:/product/getProduct.jsp";
		}				
	}
}
