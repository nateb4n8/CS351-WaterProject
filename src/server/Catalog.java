/**
 * Author: Nathan Acosta
 * Date: Apr 24, 2013
 */
package server;

import java.util.ArrayList;

/**
 * @author nacosta
 * A Catalog contains two lists of offers that are available within the
 * market with one list for sellers and the other for buyers. The lists
 * make use of the private class Offer to contain the properties of
 * an offer within the lists.
 */
public class Catalog
{
  
  private ArrayList<Offer> buyOffers;
  private ArrayList<Offer> sellOffers;
  
  public Catalog()
  {
//    this.buyOffers = new
//    this.sellOffers = new
  }
  
  /**
   * Creates a new Offer (offer) to add to the list of 
   * buying offers (buyOffers) sorted from lowest unit price to the highest.
   * @param merchantName Name of the buyer
   * @param quantity Amount of items to be purchased
   * @param unitPrice Price of a single item
   */
  public void addBuyOffer(String merchantName, int quantity, double unitPrice)
  {     
    this.buyOffers.add( new Offer(merchantName, quantity, unitPrice) );
  }
  
  /**
   * Creates a new Offer (offer) to add to the list of 
   * selling offers (sellOffers) sorted from lowest unit price to the highest.
   * @param merchantName Name of the seller
   * @param quantity Amount of items to be sold
   * @param unitPrice Price of a single item
   */
  public void addSellOffer(String merchantName, int quantity, double unitPrice)
  { 
    this.sellOffers.add( new Offer(merchantName, quantity, unitPrice) );
  }
  
  /**
   * Adds the offer to the list of offers with the first element being the
   * offer with the lowest unit price.
   * @param offer Added to the list of offers
   * @param list List of offers sorted from lowest to highest unit price
   */
  public void addOfferByLowestPrice(Offer offer, ArrayList<Offer> list)
  {
    int len = list.size();
    for(int i=0; i<len; i++)
    {
      if(offer.unitPrice < list.get(i).unitPrice)
      { list.add(i, offer);
        return;
      }
    }
    list.add(offer); //Add to end of list, highest unit price.
  }
  
  /**
   * @author nacosta
   * Private class to contain the properties of an offer that would have
   * an amount of a product, price for a single unit, and the name of
   * the merchant making the offer. May need a to add a product type.
   */
  private class Offer
  {
    public int quantity; //Number of units being bought/sold
    public double unitPrice; //Price for a single/individual unit
    public String merchant; //Name of the buyer/seller
//    public Item itemType; //Name of the product
    
    public Offer(String merchantName, int quantity, double unitPrice)
    {
      this.quantity = quantity;
      this.unitPrice = unitPrice;
      this.merchant = merchantName;
    }
  }
  
}
