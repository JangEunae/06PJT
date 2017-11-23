package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDAO;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserDao;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDAO productDAO;
	
	public void setProductDao(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}
	
	public ProductServiceImpl() {
		System.out.println(this.getClass());
	}
	
	public void addProduct(Product product) throws Exception {
		productDAO.addProduct(product);
	}
	
	public Product getProduct(int prodNo) throws Exception {
		return productDAO.getProduct(prodNo);
	}
	
	public Map<String, Object> getProductList(Search search) throws Exception {
		
		
		List<Product> list= productDAO.getProductList(search);
		int totalCount = productDAO.getTotalCount(search);
		System.out.println("���� "+search);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list );
		map.put("totalCount", new Integer(totalCount));
		return map;
	}
	
	
	public void updateProduct(Product product) throws Exception {
		productDAO.updateProduct(product);
	}




}