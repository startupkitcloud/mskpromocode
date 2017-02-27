package com.mangobits.startupkit.promocode;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.utils.MessageUtils;
import com.mangobits.startupkit.notification.NotificationBuilder;
import com.mangobits.startupkit.notification.NotificationService;
import com.mangobits.startupkit.notification.TypeSendingNotificationEnum;
import com.mangobits.startupkit.notification.email.data.EmailDataTemplate;
import com.mangobits.startupkit.user.LanguageEnum;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PromoCodeServiceImpl implements PromoCodeService {

	
	
	@EJB
	private UserService userService;
	
	
	
	@EJB
	private NotificationService notificationService;
	
	
	
	@EJB
	private ConfigurationService configurationService;
	
	
	
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
			
			if(promoCode.getExpirationDate().before(new Date())){
				promoCode.setStatus(PromoCodeStatusEnum.EXPIRED);
				promoCodeDAO.update(promoCode);
				
				throw new BusinessException("promocode_expired");
			}
			
			if(promoCode.getStatus().equals(PromoCodeStatusEnum.USED)){
				throw new BusinessException("promocode_used");
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



	@Override
	public PromoCode loadCode(String id) throws ApplicationException, BusinessException {
		
		PromoCode promoCode = null;
		
		try {
			
			promoCode = promoCodeDAO.retrieve(new PromoCode(id));
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error trying to redeem a promo code", e);
		}
		
		return promoCode;
	}



	@Override
	public List<PromoCode> listAll() throws ApplicationException, BusinessException {
		
		List<PromoCode> list = null;
		
		try {
			
			list = promoCodeDAO.listAll();
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error listing all promocodes", e);
		}
		
		return list;
	}



	@Override
	public void sendToUser(String idPromoCode, String idUser) throws ApplicationException, BusinessException {
		
		try {
		
			PromoCode promoCode = promoCodeDAO.retrieve(new PromoCode(idPromoCode));
			promoCode.setIdUserInvited(idUser);
			promoCodeDAO.update(promoCode);
			
			String discountData = null;
			if(promoCode.getType().equals(PromoCodeTypeEnum.FIXED_VALUE)){
				NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
				discountData = formatter.format(promoCode.getDiscountValue());
			}
			else{
				discountData = promoCode.getDiscountPercent() + "%";
			}
			
			User user = userService.load(idUser);
			String title = MessageUtils.message(LanguageEnum.localeByLanguage(user.getLanguage()), "promo.message.title");
			String message = MessageUtils.message(LanguageEnum.localeByLanguage(user.getLanguage()), "promo.message.message", discountData, promoCode.getCode());
			String configKeyLang = user.getLanguage() == null ? "" : "_" + user.getLanguage().toUpperCase();
			final int emailTemplateId = configurationService.loadByCode("PROMO_CODE_ID" + configKeyLang).getValueAsInt();
			final String discount = discountData;
			
			notificationService.sendNotification(new NotificationBuilder()
					.setTo(user)
					.setTypeSending(TypeSendingNotificationEnum.EMAIL)
					.setTitle(title)
					.setMessage(message)
					.setEmailDataTemplate(new EmailDataTemplate() {
						
						@Override
						public Integer getTemplateId() {
							return emailTemplateId;
						}
						
						@Override
						public Map<String, String> getData() {
							
							Map<String, String> params = new HashMap<>();
							params.put("user_name", user.getName());
							params.put("discount", discount);
							params.put("code", promoCode.getCode());
							
							return params;
						}
					})
					.build());
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error sending a promocode to an user", e);
		}
	}



	@Override
	public void cancelActivatePromoCode(String idPromoCode) throws ApplicationException, BusinessException {
		
		try {
			
			PromoCode promoCode = promoCodeDAO.retrieve(new PromoCode(idPromoCode));
			
			if(promoCode.getStatus().equals(PromoCodeStatusEnum.ACTIVE)){
				promoCode.setStatus(PromoCodeStatusEnum.CANCELED);
			}
			else{
				promoCode.setStatus(PromoCodeStatusEnum.ACTIVE);
			}
			
			promoCodeDAO.update(promoCode);
			
		} catch (Exception e) {
			throw new ApplicationException("Got an error cancel or activating a promocode", e);
		}
	}
}
