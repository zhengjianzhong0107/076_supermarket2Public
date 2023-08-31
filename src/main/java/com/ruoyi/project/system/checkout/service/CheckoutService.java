package com.ruoyi.project.system.checkout.service;


import com.ruoyi.project.system.checkout.domain.AddTempBillItemDto;
import com.ruoyi.project.system.checkout.domain.TempBillItem;
import java.math.BigDecimal;
import java.util.List;

public interface CheckoutService {

  List<String[]> matchProductSuggestByProductId(String id);

  boolean saveTempBillItem(Long userId, AddTempBillItemDto addTempBillItemDto);

  List<TempBillItem> getTempBillItems(Long userId);

  BigDecimal countTempBillItemsTotalShouldPay(Long userId);

  int removeTempBillItem(Long userId, int index);

  void closeTempBillItem(Long userId);

  int submitTempBillItem(Long userId);
}
