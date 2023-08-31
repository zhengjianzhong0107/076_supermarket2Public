package com.ruoyi.project.system.checkout.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.system.bill.service.IBillService;
import com.ruoyi.project.system.checkout.domain.AddTempBillItemDto;
import com.ruoyi.project.system.checkout.domain.TempBillItem;
import com.ruoyi.project.system.checkout.service.CheckoutService;
import com.ruoyi.project.system.product.domain.Product;
import com.ruoyi.project.system.product.service.IProductService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

  @Autowired
  private IProductService productService;

  @Autowired
  private IBillService billService;

  /**
   *  Map<UserId(Long), List<Bill>>
   */
  private Map<Long, List<TempBillItem>> tempBillItemCache = new HashMap();

  @Override
  public List<String[]> matchProductSuggestByProductId(String id) {
    List<Product> productList = productService.matchProductsByProductId(id);
    if (productList.isEmpty()) {
      return Collections.emptyList();
    }

    List<String[]> result = new ArrayList<>();
    for (Product product : productList) {
      result.add(new String[]{product.getId(), product.getName(), product.getStock().toString()});
    }
    return result;
  }

  @Override
  public boolean saveTempBillItem(Long userId, AddTempBillItemDto addTempBillItemDto) {
    Product product = productService.selectProductById(addTempBillItemDto.getProductId());
    if (product == null) {
      return false;
    }

    TempBillItem bill = new TempBillItem();
    BeanUtils.copyBeanProp(bill, product);
    bill.setNumber(addTempBillItemDto.getNumber());
    BigDecimal totalSalePrice = bill.getSalePrice().multiply(new BigDecimal(bill.getNumber()));
    BigDecimal totalPurchasePrice = bill.getPurchasePrice().multiply(new BigDecimal(bill.getNumber()));
    bill.setProfits(totalSalePrice.subtract(totalPurchasePrice));
    bill.setProductName(product.getName());
    bill.setProductId(product.getId());
    bill.setShouldPay(totalSalePrice);
    bill.setCreateTime(DateUtils.getNowDate());

    List<TempBillItem> billItems = tempBillItemCache.get(userId);
    if (billItems == null) {
      billItems = new ArrayList<>();
      tempBillItemCache.put(userId, billItems);
    }
    bill.setIndex(billItems.size() + 1);
    billItems.add(bill);
    return true;
  }

  @Override
  public List<TempBillItem> getTempBillItems(Long userId) {
    List<TempBillItem> bills = tempBillItemCache.get(userId);
    if (bills == null) {
      bills = new ArrayList<>();
    }
    return bills;
  }

  @Override
  public BigDecimal countTempBillItemsTotalShouldPay(Long userId) {
    BigDecimal totalShouldPay = new BigDecimal(0);
    totalShouldPay.setScale(2); // 保留两位小数
    List<TempBillItem> bills = tempBillItemCache.get(userId);
    if (bills == null) {
      return totalShouldPay;
    }

    for (TempBillItem bill : bills) {
      totalShouldPay = totalShouldPay.add(bill.getShouldPay());
    }
    return totalShouldPay;
  }

  @Override
  public int removeTempBillItem(Long userId, int index) {
    List<TempBillItem> bills = tempBillItemCache.get(userId);
    if (bills == null || bills.isEmpty() || (index-1 >= bills.size())) {
      return 1;
    }

    bills.remove(index - 1);

    // 刷新Index
    for (int i = 0; i < bills.size(); i++) {
      TempBillItem tempBillItem = bills.get(i);
      tempBillItem.setIndex(i + 1);
    }
    return 1;
  }

  @Override
  public void closeTempBillItem(Long userId) {
    tempBillItemCache.remove(userId);
  }

  @Override
  @Transactional
  public int submitTempBillItem(Long userId) {
    int result = 0;
    List<TempBillItem> tempBillItems = tempBillItemCache.get(userId);
    if (tempBillItems == null || tempBillItems.isEmpty()) {
      return result;
    }
    for (TempBillItem tempBillItem : tempBillItems) {
      billService.insertBill(tempBillItem);
      productService.deductStock(tempBillItem.getProductId(), tempBillItem.getNumber());
    }
    result = tempBillItems.size();
    closeTempBillItem(userId);
    return result;
  }
}
