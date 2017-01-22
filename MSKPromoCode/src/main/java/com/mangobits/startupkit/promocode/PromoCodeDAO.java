package com.mangobits.startupkit.promocode;



import com.mangobits.startupkit.core.utils.AbstractDAO;

public class PromoCodeDAO extends AbstractDAO<PromoCode> {
	
	public PromoCodeDAO(){
		super(PromoCode.class);
	}
	

	@Override
	protected Object getId(PromoCode not) {
		return not.getId();
	}
}
