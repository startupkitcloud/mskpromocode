package com.mangobits.startupkit.promocode;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



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
	

	@Field
	private String name;



	private String desc;


	
	private Date creationDate;
	
	
	
	private Date expirationDate;
	
	
	
	private Date consumeDate;
	
	
	
	private String idUserConsumer;
	
	
	
	private String idUserInvited;
	
	
	
	@Enumerated(EnumType.STRING)
	private PromoCodeDurationTypeEnum durationType;



	private Integer ocurrences;


	private Integer maxRedeem;


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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
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



	public PromoCodeDurationTypeEnum getDurationType() {
		return durationType;
	}



	public void setDurationType(PromoCodeDurationTypeEnum durationType) {
		this.durationType = durationType;
	}



	public Integer getOcurrences() {
		return ocurrences;
	}



	public void setOcurrences(Integer ocurrences) {
		this.ocurrences = ocurrences;
	}



	public Integer getMaxRedeem() {
		return maxRedeem;
	}



	public void setMaxRedeem(Integer maxRedeem) {
		this.maxRedeem = maxRedeem;
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
