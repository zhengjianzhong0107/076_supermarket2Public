package com.ruoyi.project.system.checkout.controller;

import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.checkout.domain.AddTempBillItemDto;
import com.ruoyi.project.system.checkout.service.CheckoutService;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/system/checkout")
public class CheckOutController extends BaseController {

  private String prefix = "system/checkout";

  @Autowired
  private CheckoutService checkoutService;

//  @RequiresPermissions("system:checkout:view")
  @GetMapping()
  public String product() {
    return prefix + "/checkout";
  }

  /**
   * 新增保存商品分类
   */
  @PostMapping("/tmp-bill-item/add")
  @ResponseBody
  public AjaxResult saveTempBillItem(AddTempBillItemDto addTempBillItemDto)
  {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    boolean success = checkoutService.saveTempBillItem(userId, addTempBillItemDto);
    if (success) {
      return AjaxResult.success("添加成功");
    } else {
      return AjaxResult.error("找不到商品，请检查商品编号是否正确");
    }
  }

  /**
   * 获取数据集合
   */
  @PostMapping("/tmp-bill-item")
  @ResponseBody
  public TableDataInfo getTempBillItem() {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    return getDataTable(checkoutService.getTempBillItems(userId));
  }

  /**
   * 获取数据集合
   */
  @GetMapping("/search-product")
  @ResponseBody
  public AjaxResult collection(@RequestParam("id") String id)
  {
    AjaxResult ajax = new AjaxResult();
    ajax.put("result", checkoutService.matchProductSuggestByProductId(id));
    return ajax;
  }

  /**
   * 获取数据集合
   */
  @GetMapping("/total-should-pay")
  @ResponseBody
  public AjaxResult countTempBillItemsTotalShouldPay() {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    AjaxResult ajax = new AjaxResult();
    ajax.put("result", checkoutService.countTempBillItemsTotalShouldPay(userId).toString());
    return ajax;
  }


  @GetMapping("/tmp-bill-item/remove")
  @ResponseBody
  public AjaxResult removeTempBillItem(@RequestParam("index") int index)
  {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    checkoutService.removeTempBillItem(userId, index);
    return AjaxResult.success("删除成功");
  }

  @GetMapping("/tmp-bill-item/close")
  @ResponseBody
  public AjaxResult closeTempBillItem()
  {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    checkoutService.closeTempBillItem(userId);
    return AjaxResult.success("删除成功");
  }

  /*
    确认结算
   */
  @GetMapping("/tmp-bill-item/submit")
  @ResponseBody
  public AjaxResult submitTempBillItem()
  {
    User currentUser = ShiroUtils.getSysUser();
    Long userId = currentUser.getUserId();
    checkoutService.submitTempBillItem(userId);
    return AjaxResult.success("结算成功");
  }
}
