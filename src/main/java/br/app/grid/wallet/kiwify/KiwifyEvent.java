package br.app.grid.wallet.kiwify;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KiwifyEvent {

	@JsonAlias({"order_id"})
	private String orderId; // "da292c35-c6fc-44e7-ad19-ff7865bc2d89",
	@JsonAlias({"order_ref"})
	private String orderRef; // "Quzqwus",
	@JsonAlias({"order_status"})
	private String orderStatus; // "paid", //Status da venda
	@JsonAlias({"payment_method"})
	private String paymentMethod; // "credit_card",
	@JsonAlias({"store_id"})
	private String storeId; // "JKzixndUxOr68LJ",
	@JsonAlias({"payment_merchant_id"})
	private String paymentMerchantId; // "10869585",
	@JsonAlias({"created_at"})
	private String createdAt; // ": "2020-12-21 10:46",
	@JsonAlias({"updated_at"})
	private String updatedAt; // : "2020-12-21 10:46",
	
	@JsonAlias({"Commissions"})
	private KiwifyCommissions commissions;
	
	@JsonAlias({"Customer"})
	private KiwifyCustomer customer;
	
	@JsonAlias({"Product"})
	private KiwifyProduct product;

}
