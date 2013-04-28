/**
 * Author: Nathan Acosta
 * Date: Apr 27, 2013
 */
package server;

/**
 * @author nacosta
 * Private class to contain the properties of an offer that would have
 * an amount of a product, price for a single unit, and the name of
 * the merchant making the offer. May need a to add a product type.
 */
public class Offer
{
  public int quantity; //Number of units being sold
  public double unitPrice; //Price for a single/individual unit
  public String merchant; //Name of the seller
  
  public Offer(String merchantName, int quantity, double unitPrice)
  {
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.merchant = merchantName;
  }
}