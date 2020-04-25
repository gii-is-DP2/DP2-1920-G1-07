package org.springframework.samples.petclinic.model.paypal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "status", "amount", "invoice_id", "seller_protection", "expiration_time", "create_time",
		"update_time", "links" })
public class Payment {

	@JsonProperty("id")
	private String id;
	@JsonProperty("status")
	private String status;
	@JsonProperty("amount")
	private Amount amount;
	@JsonProperty("invoice_id")
	private String invoiceId;
	@JsonProperty("seller_protection")
	private SellerProtection sellerProtection;
	@JsonProperty("expiration_time")
	private String expirationTime;
	@JsonProperty("create_time")
	private String createTime;
	@JsonProperty("update_time")
	private String updateTime;
	@JsonProperty("links")
	private List<Link> links = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("amount")
	public Amount getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	@JsonProperty("invoice_id")
	public String getInvoiceId() {
		return invoiceId;
	}

	@JsonProperty("invoice_id")
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	@JsonProperty("seller_protection")
	public SellerProtection getSellerProtection() {
		return sellerProtection;
	}

	@JsonProperty("seller_protection")
	public void setSellerProtection(SellerProtection sellerProtection) {
		this.sellerProtection = sellerProtection;
	}

	@JsonProperty("expiration_time")
	public String getExpirationTime() {
		return expirationTime;
	}

	@JsonProperty("expiration_time")
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	@JsonProperty("create_time")
	public String getCreateTime() {
		return createTime;
	}

	@JsonProperty("create_time")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@JsonProperty("update_time")
	public String getUpdateTime() {
		return updateTime;
	}

	@JsonProperty("update_time")
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@JsonProperty("links")
	public List<Link> getLinks() {
		return links;
	}

	@JsonProperty("links")
	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
