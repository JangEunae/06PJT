package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseDAO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


//==> ȸ������ Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	//setter Method ���� ����
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@RequestParam("prodNo") String prodNo, @RequestParam("userId") String userId, Model model) throws Exception {

		System.out.println("/addPurchaseView.do");
		
		Product productVO = productService.getProduct(Integer.parseInt(prodNo));
		User userVO = userService.getUser(userId);
		
		model.addAttribute("userVO",userVO);
		model.addAttribute("productVO", productVO);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addProduct( @ModelAttribute("purchase") Purchase purchaseVO , @RequestParam("userId") String userId ,@RequestParam("prodNo") String prodNo , Model model ) throws Exception {

		System.out.println("/addPurchase.do");
		System.out.println(purchaseVO);
		//Business Logic
		purchaseVO.setBuyer(userService.getUser(userId));
		purchaseVO.setPurchaseProd(productService.getProduct(Integer.parseInt(prodNo)));
		purchaseService.addPurchase(purchaseVO);

		model.addAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") String tranNo , Model model ) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		// Model �� View ����
		model.addAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @RequestParam("tranNo") String tranNo , Model model ) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		model.addAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase, Model model ) throws Exception{

		System.out.println("/updatePurchase.do");
		System.out.println(purchase);
		//Business Logic
		purchaseService.updatePurcahse(purchase);
		
		model.addAttribute("purchaseVO", purchase);

		return "forward:/getPurchase.do?tranNo="+purchase.getTranNo();
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search,  Model model , HttpSession session) throws Exception{
		
		System.out.println("/listPurchase.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		// Business logic ����
		
		String buyerId=((User)session.getAttribute("user")).getUserId();
		Map<String , Object> map=purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/deletePurchase.do")
	public String deletePurchase( @RequestParam("tranNo") String tranNo, Model model ) throws Exception{

		System.out.println("/deletePurchase.do");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		purchaseService.deleteTranCode(purchaseVO);
		
		model.addAttribute("purchaseVO", purchaseVO);

		return "forward:/listPurchase.do";
	}
	
	@RequestMapping("/updateTranCodeAction.do")
	public String updateTranCodeAction( @RequestParam("tranNo") String tranNo, Model model ) throws Exception{

		System.out.println("/updateTranCodeAction.do");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase(Integer.parseInt(tranNo));
		purchaseService.updateTranCode(purchaseVO);
		
		model.addAttribute("purchaseVO", purchaseVO);

		return "forward:/listPurchase.do";
	}
	
	@RequestMapping("/updateTranCodeByProdAction.do")
	public String updateTranCodeByProdAction( @RequestParam("prodNo") String prodNo ) throws Exception{

		System.out.println("/updateTranCodeByProdAction.do");
		
		//Business Logic
		Purchase purchaseVO = purchaseService.getPurchase2(Integer.parseInt(prodNo));
		purchaseService.updateTranCode(purchaseVO);
		
		return "forward:/listProduct.do?menu=manage";
	}
}