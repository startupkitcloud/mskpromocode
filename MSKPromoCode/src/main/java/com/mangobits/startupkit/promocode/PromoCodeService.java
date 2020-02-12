package com.mangobits.startupkit.promocode;

import com.mangobits.startupkit.core.exception.DAOException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PromoCodeService {
	
	void createPromoCode(PromoCode promoCode) throws Exception;
	
	
	PromoCode redeemCode(RedeemCode redeemCode) throws Exception;
	
	
	PromoCode loadCode(String id) throws Exception;
	
	
	List<PromoCode> listAll() throws Exception;
	
	
	void sendToUser(String idPromoCode, String idUser) throws Exception;
	
	
	void cancelActivatePromoCode(String idPromoCode) throws Exception;


	PromoCode loadByCode (String code) throws DAOException;
}
