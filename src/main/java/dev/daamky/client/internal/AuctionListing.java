package dev.daamky.client.internal;

import dev.daamky.client.item.ItemActionCatalog;

public class AuctionListing {
   public ItemActionCatalog itemType;
   public int price;
   public int allPrice;
   public int auctionPrice;
   public int balance;
   public long timestamp = System.currentTimeMillis();

   public AuctionListing(ItemActionCatalog itemType, int price, int allPrice, int auctionPrice, int balance) {
      this.itemType = itemType;
      this.price = price;
      this.allPrice = allPrice;
      this.auctionPrice = auctionPrice;
      this.balance = balance;
      this.timestamp = System.currentTimeMillis();
   }

   public ItemActionCatalog getItemType() {
      return this.itemType;
   }

   public int getPrice() {
      return this.price;
   }

   public int getAllPrice() {
      return this.allPrice;
   }

   public int getAuctionPrice() {
      return this.auctionPrice;
   }

   public int getBalance() {
      return this.balance;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setItemType(ItemActionCatalog itemType) {
      this.itemType = itemType;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setAllPrice(int allPrice) {
      this.allPrice = allPrice;
   }

   public void setAuctionPrice(int auctionPrice) {
      this.auctionPrice = auctionPrice;
   }

   public void setBalance(int balance) {
      this.balance = balance;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   public boolean computePrice(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AuctionListing var2)) {
         return false;
      } else if (!var2.computeFlag(this)) {
         return false;
      } else if (this.getPrice() != var2.getPrice()) {
         return false;
      } else if (this.getAllPrice() != var2.getAllPrice()) {
         return false;
      } else if (this.getAuctionPrice() != var2.getAuctionPrice()) {
         return false;
      } else if (this.getBalance() != var2.getBalance()) {
         return false;
      } else if (this.getTimestamp() != var2.getTimestamp()) {
         return false;
      } else {
         ItemActionCatalog var3 = this.getItemType();
         ItemActionCatalog var4 = var2.getItemType();
         return var3 != null ? var3.equals(var4) : var4 == null;
      }
   }

   public boolean computeFlag(Object other) {
      return other instanceof AuctionListing;
   }

   public int getPrice2() {
      byte var1 = 59;
      int var2 = 1;
      var2 = var2 * 59 + this.getPrice();
      var2 = var2 * 59 + this.getAllPrice();
      var2 = var2 * 59 + this.getAuctionPrice();
      var2 = var2 * 59 + this.getBalance();
      long var3 = this.getTimestamp();
      var2 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      ItemActionCatalog var5 = this.getItemType();
      return var2 * 59 + (var5 == null ? 43 : var5.hashCode());
   }

   public AuctionListing(ItemActionCatalog itemType, int price, int allPrice, int auctionPrice, int balance, long timestamp) {
      this.itemType = itemType;
      this.price = price;
      this.allPrice = allPrice;
      this.auctionPrice = auctionPrice;
      this.balance = balance;
      this.timestamp = timestamp;
   }

   public AuctionListing() {
   }
}
