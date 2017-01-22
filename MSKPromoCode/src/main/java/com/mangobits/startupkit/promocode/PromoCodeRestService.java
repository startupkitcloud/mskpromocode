package com.mangobits.startupkit.promocode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.ws.JsonContainer;

@Stateless
@Path("/promoCode")
public class PromoCodeRestService {

	
	
	@EJB
	private PromoCodeService promoCodeService;
	
	
	
	
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
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);
		
		return resultStr;
	}
}
