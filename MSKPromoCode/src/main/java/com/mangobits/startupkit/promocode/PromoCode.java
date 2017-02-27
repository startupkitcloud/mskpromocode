package com.mangobits.startupkit.promocode;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;



@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="promoCode")
@Indexed
public class PromoCode {
	
	
	
	
	@Id
	@DocumentId
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
	
	
	@Field
	private String code;
	
	
	
	private Date creationDate;
	
	
	
	private Date expirationDate;
	
	
	
	private Date consumeDate;
	
	
	
	private String idUserConsumer;
	
	
	
	private String idUserInvited;
	
	
	
	@Enumerated(EnumType.STRING)
	private PromoCodeTypeEnum type;
	
	
	
	@Enumerated(EnumType.STRING)
	private PromoCodeStatusEnum status;
	
	
	
	private Integer discountPercent;
	
	
	
	private Double discountValue;
	
	
	
	public PromoCode(){
		
	}
	
	
	
	public PromoCode(String id){
		this.id = id;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public Date getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}



	public Date getExpirationDate() {
		return expirationDate;
	}



	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}



	public String getIdUserConsumer() {
		return idUserConsumer;
	}



	public void setIdUserConsumer(String idUserConsumer) {
		this.idUserConsumer = idUserConsumer;
	}



	public PromoCodeTypeEnum getType() {
		return type;
	}



	public void setType(PromoCodeTypeEnum type) {
		this.type = type;
	}



	public PromoCodeStatusEnum getStatus() {
		return status;
	}



	public void setStatus(PromoCodeStatusEnum status) {
		this.status = status;
	}



	public Integer getDiscountPercent() {
		return discountPercent;
	}



	public void setDiscountPercent(Integer discountPercent) {
		this.discountPercent = discountPercent;
	}



	public Double getDiscountValue() {
		return discountValue;
	}



	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}



	public Date getConsumeDate() {
		return consumeDate;
	}



	public void setConsumeDate(Date consumeDate) {
		this.consumeDate = consumeDate;
	}



	public String getIdUserInvited() {
		return idUserInvited;
	}



	public void setIdUserInvited(String idUserInvited) {
		this.idUserInvited = idUserInvited;
	}
}
