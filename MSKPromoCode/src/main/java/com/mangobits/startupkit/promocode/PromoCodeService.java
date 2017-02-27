package com.mangobits.startupkit.promocode;

import java.util.List;

import javax.ejb.Local;

import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;

@Local
public interface PromoCodeService {
	
	void createPromoCode(PromoCode promoCode) throws ApplicationException, BusinessException;
	
	
	PromoCode redeemCode(RedeemCode redeemCode) throws ApplicationException, BusinessException;
	
	
	PromoCode loadCode(String id) throws ApplicationException, BusinessException;
	
	
	List<PromoCode> listAll() throws ApplicationException, BusinessException;
	
	
	void sendToUser(String idPromoCode, String idUser) throws ApplicationException, BusinessException;
	
	
	void cancelActivatePromoCode(String idPromoCode) throws ApplicationException, BusinessException;
}
