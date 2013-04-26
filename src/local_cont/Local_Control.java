package local_cont;

import topo.Topography;
import XML_Handler.XML_Handler;
import gui.WaterProjectGUI;
import cell.Crops;
import cell.Farm;
import cell.Plant;

public class Local_Control {
	Farm f;
	public double getMoney(){

		return (f==null)?0:f.getMoney();
	}
	public Farm newFarm(double latitude, double longitude, String path){
	  f = Topography.createFarm(latitude, longitude);
	  XML_Handler.initGround(f, path);
	  return f;
	}
	public void saveFarm(String path){
		try {
			XML_Handler.save(f, path);
		} catch (Exception e) {
		}
	}
	public Farm loadFarm(String path){
		f = XML_Handler.restore(path);
		return f;
	}
	public void plantCrops(String[] crops){
		int len = crops.length;
		for (int i=0;i<len;i++){
			if (!crops[i].equalsIgnoreCase("None")){
				try {
					f.setCrop(i, new Crops(Plant.getPlantType(crops[i]), f, i));
				} catch (Exception e) {
				}
			}
		}
	}
 
	public static void main(String args[]){
		Local_Control me = new Local_Control();
		new WaterProjectGUI(me);
	}
}
