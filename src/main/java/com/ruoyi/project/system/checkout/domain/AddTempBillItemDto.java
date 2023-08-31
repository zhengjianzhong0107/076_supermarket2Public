package com.ruoyi.project.system.checkout.domain;

/**
 * 服务台扫码，记录商品信息
 */
public class AddTempBillItemDto {

  private String productId;
  private Long number;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }
}
