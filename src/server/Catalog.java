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
  public String addSellOffer(Offer offer)
  { 
    if (offerExist(offer)) return "Error(Catalog): Offer already exist!";
    else
    {
      this.addOfferByLowestPrice(offer, this.sellOffers);
      return "Success(Catalog): Offer has been added.";
    }
  }
  
  /**
   * Checks if the passed Offer exist within the Catalog(sellOffer List)
   * @param offer Being searched for in the sellOffers
   * @return true if offer is found, false otherwise
   */
  public boolean offerExist(Offer offer)
  {
    int totalOffers = this.sellOffers.size();
    for (int i=0; i<totalOffers; i++)
    {
      String merchant = this.sellOffers.get(i).merchant;
      int quantity = this.sellOffers.get(i).quantity;
      double unitPrice = this.sellOffers.get(i).unitPrice;
      
      if (offer.merchant.equalsIgnoreCase(merchant))
      { if (offer.quantity == quantity)
        { if (offer.unitPrice == unitPrice)
          { return true;
          }
        }
      }
    }
    return false;
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
  
  /**
   * @return The list of sellOffers.
   */
  public ArrayList<Offer> getOffersList() { return sellOffers; }
  
  /**
   * @param index The index of the offer to be removed.
   */
  public void removeOfferAt(int index) { sellOffers.remove(index); }
  
}
