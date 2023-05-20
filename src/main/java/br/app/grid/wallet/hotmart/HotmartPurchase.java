package br.app.grid.wallet.hotmart;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotmartPurchase {
  
  @JsonAlias({"approved_date"})
  private Long approvedDate;
  private Long orderDate;
  
  private String status;
  private String transaction;
  
  @JsonAlias({"buyer_ip"})
  private String buyerIp;
  
  /**
  "purchase": {
    "approved_date": 1231241434453,
    "full_price": {
      "value": 134.0
    },
    "original_offer_price": {
      "currency_value": "EUR",
      "value": 150.6
    },
    "price": {
      "value": 150.6
    },
    "offer": {
      "code": "n82b9jqz"
    },
    "checkout_country": {
      "name": "Brasil",
      "iso": "BR"
    },
    "order_bump": {
      "is_order_bump": true,
      "parent_purchase_transaction": "HP02316330308193"
    },
    "order_date": "123243546",
    "status": "STARTED",
    "transaction": "HP02316330308193",
    "buyer_ip": "127.0.0.1",
    "payment": {
      "billet_barcode": "03399.33335 33823.303087 19802.801027 2 87630000015000",
      "billet_url": "https://billet-link.com/bHP02316330308193",
      "installments_number": 2,
      "pix_code": "00020101021226780014br.gov.bcb.pix2556pix-h.juno.com.br/qr/v2/A0ACBEDA916F322FAB94E7DA5B29D0185204000053039865802BR5910EBANX Ltda6008CURITIBA62070503***6304E794",
      "pix_expiration_date": 1645271012000,
      "pix_qrcode": "https://sandbox-local-latam.ebanx.com/pix/checkout?hash=620e34e301fcbdead10d9187a699c4de9e50db35b92da0cd",
      "refusal_reason": "fail",
      "type": "PICPAY"
    }
  },
  */

}
