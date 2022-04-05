package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class AddProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Product product = new Product();
		product.setProdCode("aa");
		product.setSellerId(user.getUserId());
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setDueDate(CommonUtil.toDateStr(request.getParameter("dueDate")));
		product.setCost(Integer.parseInt(request.getParameter("cost")));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		
		System.out.println("AddProductAction ::" +product); // debugging
		
		ProductService service = new ProductServiceImpl();
		service.addProduct(product);

		request.setAttribute("product", product);
		
		return "forward:/product/addProduct.jsp";
	}
}
