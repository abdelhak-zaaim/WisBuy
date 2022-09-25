package com.strontech.imgautam.handycaft.model;

public class HandiCraft {

  private String product_id;
  private String product_image;
  private String product_name;
  private String product_reseller_name;
  private String product_sp;
  private String product_mrp;
  private String product_discount;
  private String product_quantity;
  private String product_highlight;
  private String product_desc;
private  String category;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public HandiCraft() {

  }

  public HandiCraft(String product_id, String product_image, String product_name, String product_reseller_name, String product_sp,
      String product_mrp, String product_discount, String product_quantity, String product_highlight, String product_desc) {
    this.product_id=product_id;
    this.product_image = product_image;
    this.product_name = product_name;
    this.product_reseller_name=product_reseller_name;
    this.product_sp = product_sp;
    this.product_mrp = product_mrp;
    this.product_discount = product_discount;
    this.product_quantity=product_quantity;
    this.product_highlight=product_highlight;
    this.product_desc = product_desc;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getProduct_image() {
    return product_image;
  }

  public void setProduct_image(String product_image) {
    this.product_image = product_image;
  }

  public String getProduct_name() {
    return product_name;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public String getProduct_reseller_name() {
    return product_reseller_name;
  }

  public void setProduct_reseller_name(String product_reseller_name) {
    this.product_reseller_name = product_reseller_name;
  }

  public String getProduct_sp() {
    return product_sp;
  }

  public void setProduct_sp(String product_sp) {
    this.product_sp = product_sp;
  }

  public String getProduct_mrp() {
    return product_mrp;
  }

  public void setProduct_mrp(String product_mrp) {
    this.product_mrp = product_mrp;
  }

  public String getProduct_discount() {
    return product_discount;
  }

  public void setProduct_discount(String product_discount) {
    this.product_discount = product_discount;
  }

  public String getProduct_quantity() {
    return product_quantity;
  }

  public void setProduct_quantity(String product_quantity) {
    this.product_quantity = product_quantity;
  }

  public String getProduct_highlight() {
    return product_highlight;
  }

  public void setProduct_highlight(String product_highlight) {
    this.product_highlight = product_highlight;
  }

  public String getProduct_desc() {
    return product_desc;
  }

  public void setProduct_desc(String product_desc) {
    this.product_desc = product_desc;
  }
}
