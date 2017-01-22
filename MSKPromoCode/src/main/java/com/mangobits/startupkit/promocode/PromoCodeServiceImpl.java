package com.mangobits.startupkit.promocode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PromoCodeServiceImpl implements PromoCodeService {

	
	
	@EJB
	private UserService userService;
	
	
	
	@Inject
	@New
	private PromoCodeDAO promoCodeDAO;
	
	
	
	
	@Override
	public void createPromoCode(PromoCode promoCode) throws ApplicationException, BusinessException {
		
		try {
			
			promoCode.setCode(generatePromoCodeNumber());
			promoCode.setCreationDate(new Date());
			promoCode.setStatus(PromoCodeStatusEnum.ACTIVE);
			
			promoCodeDAO.insert(promoCode);
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error creating a new promo code", e);
		}
	}
	
	

	@Override
	public PromoCode redeemCode(RedeemCode redeemCode) throws ApplicationException, BusinessException {
		
		PromoCode promoCode = null;
		
		try {
			
			User user = userService.load(redeemCode.getIdUser());
			
			if(user == null){
				throw new BusinessException("user_not_found");
			}
			
			Map<String, Object> params = new HashMap<>();
			params.put("code", redeemCode.getCode());
			
			promoCode = promoCodeDAO.retrieve(params);
			
			if(promoCode == null){
				throw new BusinessException("promocode_not_found");
			}
			
			promoCode.setConsumeDate(new Date());
			promoCode.setStatus(PromoCodeStatusEnum.USED);
			promoCode.setIdUserConsumer(user.getId());
			
			promoCodeDAO.update(promoCode);
			
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("Got an error trying to redeem a promo code", e);
		}
		
		return promoCode;
	}
	
	
	
	private String generatePromoCodeNumber() throws BusinessException, ApplicationException {
		
		String numberStr = null;
		
		try {
			
			boolean numberFound = false;
			
			while(!numberFound){
				
				Integer number = (int)(Math.random() * 1000000);
				numberStr = String.valueOf(number);
				
				Map<String, Object> params = new HashMap<>();
				params.put("code", numberStr);
				
				PromoCode key = promoCodeDAO.retrieve(params);
				numberFound = number > 99999 && key == null;
			}
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error generating promo code number", e);
		}
		
		return numberStr;
	}
}
