package com.wang.miaosha_1.domain;

import java.util.Date;

/**
 * 秒杀商品
 */

public class MiaoshaGoods {
	private Long id;
	private Long goodsId;
	private Integer stockCount;
	private Double miaoshaPrice;
	private Date startDate;
	private Date endDate;

	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}

	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
