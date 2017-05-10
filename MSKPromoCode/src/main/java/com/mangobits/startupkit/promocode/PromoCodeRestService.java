package com.mangobits.startupkit.promocode;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.notification.email.EmailService;
import com.mangobits.startupkit.service.admin.util.Secured;
import com.mangobits.startupkit.ws.JsonContainer;

@Stateless
@Path("/promoCode")
public class PromoCodeRestService {

	
	
	@EJB
	private PromoCodeService promoCodeService;
	
	
	
	@EJB
	private EmailService emailService;
	
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/redeemCode")
	public String redeemCode(RedeemCode redeemCode)  throws Exception{ 
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try { 
			
			PromoCode promoCode = promoCodeService.redeemCode(redeemCode); 
			cont.setData(promoCode);

		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
	
	
	
	
	@Secured
	@GET
	@Path("/listAll")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String listAll() throws Exception {
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try {
			
			List<PromoCode> list = promoCodeService.listAll();
			
			cont.setData(list);
			
		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
	
	
	
	@Secured
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/createPromoCode")
	public String createPromoCode(PromoCode promoCode)  throws Exception{ 
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try { 
			
			promoCodeService.createPromoCode(promoCode); 
			cont.setData(promoCode);

		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
	
	
	
	
	@Secured
	@GET
	@Path("/loadCode/{idCode}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String loadCode(@PathParam("idCode") String idCode) throws Exception {
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try {
			
			PromoCode promoCode = promoCodeService.loadCode(idCode);
			cont.setData(promoCode);
			
		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
	
	
	
	
	@Secured
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/sendToUser")
	public String sendToUser(PromoCode promoCode)  throws Exception{ 
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try { 
			
			promoCodeService.sendToUser(promoCode.getId(), promoCode.getIdUserInvited()); 
			cont.setData("OK");

		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
	
	
	
	@Secured
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/cancelActivatePromoCode")
	public String cancelActivatePromoCode(PromoCode promoCode)  throws Exception{ 
		
		String resultStr = null;
		JsonContainer cont = new JsonContainer();
		
		try { 
			
			promoCodeService.cancelActivatePromoCode(promoCode.getId()); 
			cont.setData("OK");

		} catch (Exception e) {
			
			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}
			
			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
			
			emailService.sendEmailError(e);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
}
