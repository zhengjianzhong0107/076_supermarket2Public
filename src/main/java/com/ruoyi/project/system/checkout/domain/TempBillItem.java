package com.ruoyi.project.system.checkout.domain;

import com.ruoyi.project.system.bill.domain.Bill;
import java.math.BigDecimal;

public class TempBillItem extends Bill {

  private Integer index;
  private BigDecimal shouldPay; // 应付金额

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public BigDecimal getShouldPay() {
    return shouldPay;
  }

  public void setShouldPay(BigDecimal shouldPay) {
    this.shouldPay = shouldPay;
  }
}
