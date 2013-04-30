/**
 * Author: Nathan Acosta
 * Date: Apr 24, 2013
 */
package server;

import java.util.ArrayList;

/**
 * @author nacosta
 * A Catalog contains a list of offers that are available within the
 * market with one list for sellers. The lists make use of the private 
 * class Offer to contain the properties of an offer within the list.
 */
public class Catalog
{
  
  private ArrayList<Offer> sellOffers; //List of all current offers
  
  public Catalog()
  {
    this.sellOffers = new ArrayList<Offer>(10);
  }
  
  /**
   * Creates a new Offer (offer) to add to the list of 
   * selling offers (sellOffers) sorted from lowest unit price to the highest.
   * @param merchantName Name of the seller
   * @param quantity Amount of items to be sold
   * @param unitPrice Price of a single item
   */
  public void addSellOffer(Offer offer)
  { 
    this.addOfferByLowestPrice(offer, this.sellOffers);
  }
  
  /**
   * Adds the offer to the list of offers with the first element being the
   * offer with the lowest unit price.
   * @param offer Contains the information about the offer being added
   * @param list List of offers sorted from lowest to highest unit price
   */
  private void addOfferByLowestPrice(Offer offer, ArrayList<Offer> list)
  {
    int len = list.size();
    for(int i=0; i<len; i++)
    {
      if(offer.unitPrice < list.get(i).unitPrice)
      { list.add(i, offer); //Only add offer if its less than the offer in list
        return;
      }
    }
    list.add(offer); //Add to end of list, highest unit price.
  }
  public ArrayList<Offer> getOffersList()
  {
    return sellOffers;
  }
  public void removeOfferAt(int index)
  {
    sellOffers.remove(index);  
  }
  
}
