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
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.exception.DAOException;
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
	public void createPromoCode(PromoCode promoCode) throws Exception {

		promoCode.setCode(generatePromoCodeNumber());
		promoCode.setCreationDate(new Date());
		promoCode.setStatus(PromoCodeStatusEnum.ACTIVE);

		promoCodeDAO.insert(promoCode);
	}
	
	

	@Override
	public PromoCode redeemCode(RedeemCode redeemCode) throws Exception {

		PromoCode promoCode = null;

		User user = userService.retrieve(redeemCode.getIdUser());

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

		if(promoCode.getMaxRedeem() != null && promoCode.getMaxRedeem()>0){
			promoCode.setMaxRedeem(promoCode.getMaxRedeem() -1);
		} else{
			throw new BusinessException("promocode_expired");
		}


		if(promoCode.getDurationType() !=null){

			if(promoCode.getDurationType() == PromoCodeDurationTypeEnum.ONCE){
				promoCode.setConsumeDate(new Date());
				promoCode.setStatus(PromoCodeStatusEnum.USED);
				promoCode.setIdUserConsumer(user.getId());

				promoCodeDAO.update(promoCode);
			}

			if(promoCode.getDurationType() == PromoCodeDurationTypeEnum.REPEATING){
				if(promoCode.getOcurrences()>0){
					promoCode.setOcurrences( promoCode.getOcurrences() -1);
					promoCode.setConsumeDate(new Date());
					promoCode.setIdUserConsumer(user.getId());
					promoCodeDAO.update(promoCode);

					if(promoCode.getOcurrences() == 0){
						promoCode.setConsumeDate(new Date());
						promoCode.setStatus(PromoCodeStatusEnum.USED);
						promoCode.setIdUserConsumer(user.getId());

						promoCodeDAO.update(promoCode);
					}
				}else{
					throw new BusinessException("promocode_expired");
				}
			}
		} else {
			promoCode.setConsumeDate(new Date());
			promoCode.setStatus(PromoCodeStatusEnum.USED);
			promoCode.setIdUserConsumer(user.getId());

			promoCodeDAO.update(promoCode);
		}

		return promoCode;
	}
	
	
	
	private String generatePromoCodeNumber() throws Exception {
		
		String numberStr = null;

		boolean numberFound = false;

		while(!numberFound){

			Integer number = (int)(Math.random() * 1000000);
			numberStr = String.valueOf(number);

			Map<String, Object> params = new HashMap<>();
			params.put("code", numberStr);

			PromoCode key = promoCodeDAO.retrieve(params);
			numberFound = number > 99999 && key == null;
		}

		return numberStr;
	}



	@Override
	public PromoCode loadCode(String id) throws Exception {
		
		PromoCode promoCode = null;

		promoCode = promoCodeDAO.retrieve(new PromoCode(id));
		
		return promoCode;
	}



	@Override
	public List<PromoCode> listAll() throws Exception {
		
		List<PromoCode> list = null;

		list = promoCodeDAO.listAll();
		
		return list;
	}



	@Override
	public void sendToUser(String idPromoCode, String idUser) throws Exception {

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

		User user = userService.retrieve(idUser);
		String title = MessageUtils.message(LanguageEnum.localeByLanguage(user.getLanguage()), "promo.message.title");
		String msg = MessageUtils.message(LanguageEnum.localeByLanguage(user.getLanguage()), "promo.message.message");
		String configKeyLang = user.getLanguage() == null ? "" : "_" + user.getLanguage().toUpperCase();
		final int emailTemplateId = configurationService.loadByCode("PROMO_CODE_ID" + configKeyLang).getValueAsInt();
		final String discount = discountData;

		notificationService.sendNotification(new NotificationBuilder()
				.setTo(user)
				.setTypeSending(TypeSendingNotificationEnum.EMAIL)
				.setTitle(title)
				.setMessage("")
				.setEmailDataTemplate(new EmailDataTemplate() {

					@Override
					public Integer getTemplateId() {
						return emailTemplateId;
					}

					@Override
					public Map<String, Object> getData() {

						Map<String, Object> params = new HashMap<>();
						params.put("user_name", user.getName());
						params.put("msg", msg);
						if(promoCode.getType().equals(PromoCodeTypeEnum.FIXED_VALUE)){
							params.put("discount", "R$" + promoCode.getDiscountValue().toString());
						}else{
							params.put("discount", "%" + promoCode.getDiscountPercent().toString());
						}

						params.put("code", promoCode.getCode());

						return params;
					}
				})
				.build());
	}



	@Override
	public void cancelActivatePromoCode(String idPromoCode) throws Exception {

		PromoCode promoCode = promoCodeDAO.retrieve(new PromoCode(idPromoCode));

		if(promoCode.getStatus().equals(PromoCodeStatusEnum.ACTIVE)){
			promoCode.setStatus(PromoCodeStatusEnum.CANCELED);
		}
		else{
			promoCode.setStatus(PromoCodeStatusEnum.ACTIVE);
		}

		promoCodeDAO.update(promoCode);
	}


	@Override
	public PromoCode loadByCode (String code) throws DAOException {
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);

		PromoCode promoCode = promoCodeDAO.retrieve(params);

		return promoCode;
	}
}
