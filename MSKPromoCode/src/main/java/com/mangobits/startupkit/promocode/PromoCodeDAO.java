package com.mangobits.startupkit.promocode;


import com.mangobits.startupkit.core.dao.AbstractDAO;

public class PromoCodeDAO extends AbstractDAO<PromoCode> {
	
	public PromoCodeDAO(){
		super(PromoCode.class);
	}
	

	@Override
	public Object getId(PromoCode obj) {
		return obj.getId();
	}
}
