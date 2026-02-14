package io.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OCRInvoice {

    private String title; // 标题区域，例如：浙江增值税电子普通发票
    private String invoiceType; // 发票类型，例如：电子普通发票

    // 右上角区域
    private String invoiceCode; // 发票代码
    private String invoiceNumber; // 发票号码
    private String invoiceDate; // 开票日期

    // 表格内合计信息
    private String totalAmount; // 金额
    private String invoiceTax; // 税额
    private String invoiceAmountPreTax; // 不含税金额
    private String totalAmountInWords; // 大写金额
    private String remarks; // 备注

    // 购买方信息
    private String purchaserName; // 购买方：名称
    private String purchaserTaxNumber; // 购买方：纳税人识别号
    private String purchaserContactInfo; // 购买方：地址、电话
    private String purchaserBankAccountInfo; // 购买方：开户行及账号

    // 销售方信息
    private String sellerName; // 销售方：名称息
    private String sellerTaxNumber; // 销售方：纳税人识别号
    private String sellerContactInfo; // 销售方：地址、电话
    private String sellerBankAccountInfo; // 销售方：开户行及账号

    private String recipient; // 收款人
    private String reviewer; // 复核人
    private String drawer; // 开票人

    private List<InvoiceDetails> invoiceDetails;

    @Data
    @Accessors(chain = true)
    static class InvoiceDetails {
        private String itemName;
        private String specification;
        private String unit;
        private String quantity;
        private String unitPrice;
        private String amount;
        private String taxRate;
        private String tax;
    }

}
