package XML_Handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import cell.*;

public class XML_Handler{
	/**
	 * 
	 * @param thefarm
	 * @param path
	 */
	// Constants for the XML tags (save and restore)
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String RELIEF = "relief";
	private static final String LAYERS = "layers";
	private static final String CELL = "cell";
	private static final String CELLSIZE = "cellsize";
	private static final String HEIGHT = "height";
	private static final String SURFACE = "surface";
	private static final String DEPTH = "depth";
	private static final String VOLUME = "volume";
	private static final String XCOORD = "xcoord";
	private static final String YCOORD = "ycoord";
	private static final String ZCOORD = "zcoord";
	private static final String SOILTYPE = "soil";
	private static final String PLANTTYPE = "plant";
	// Constants for the XML tags (config)
	private static final String STRATUM = "stratum";
	private static final String GILASAND = "GilaSand";
	private static final String GILAFINESANDYLOAM = "GilaFineSandyLoam";
	private static final String GILALOAM = "GilaLoam";
	private static final String GILACLAYLOAM = "GilaClayLoam";
	private static final String GILACLAY = "GilaClay";
	private static final String RIVERWASH = "RiverWash";

	
	public void save(Farm thefarm, String path) throws Exception
	{
		Cell[][][] fm = thefarm.getGrid();
	    // Create a XMLOutputFactory
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	    // Create XMLEventWriter
	    XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(path));
	    // Create a EventFactory
	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    // Create and write Start Tag
	    StartDocument startDocument = eventFactory.createStartDocument();
	    eventWriter.add(startDocument);

	    // Create farm open tag
	    StartElement configStartElement = eventFactory.createStartElement("", "", "farm");
	    eventWriter.add(configStartElement);
	    eventWriter.add(end);
	    // Write the farm-level nodes
	    createNode(eventWriter, LATITUDE, Double.toString(thefarm.getLatitude()));
	    createNode(eventWriter, LONGITUDE, Double.toString(thefarm.getLongitude()));
	    createNode(eventWriter, RELIEF, Double.toString(thefarm.getRelief()));
	    createNode(eventWriter, LAYERS, Integer.toString(fm.length));

	    for (Cell[][] xy : fm)
	    {
	    	for (Cell[] x : xy)
	    	{
	    		for (Cell c : x)
	    		{
	    			// create cell open tag
	    		    configStartElement = eventFactory.createStartElement("", "", CELL);
	    			eventWriter.add(configStartElement);
	    			eventWriter.add(end);
	    			// write nodes
	    			//createNode(eventWriter, CELLSIZE, Double.toString(c.getCellSize()));
	    			createNode(eventWriter, HEIGHT, Double.toString(c.getHeight()));
	    			createNode(eventWriter, SURFACE, Boolean.toString(c.isSurface()));
	    			createNode(eventWriter, DEPTH, Double.toString(c.getDepth()));
	    			createNode(eventWriter, VOLUME, Double.toString(c.getWaterVolume()));
	    			createNode(eventWriter, XCOORD, Integer.toString(c.getCoordinate().x));
	    			createNode(eventWriter, YCOORD, Integer.toString(c.getCoordinate().y));
	    			createNode(eventWriter, ZCOORD, Integer.toString(c.getCoordinate().z));
	    			createNode(eventWriter, SOILTYPE, c.getSoil().toString());
	    			createNode(eventWriter, PLANTTYPE, c.getPlant().toString());
	    		    eventWriter.add(eventFactory.createEndElement("", "", CELL));
	    		    eventWriter.add(end);
	    		}
	    	}
	    }
	    eventWriter.add(eventFactory.createEndElement("", "", "farm"));
	    eventWriter.add(end);
	    eventWriter.add(eventFactory.createEndDocument());
	    eventWriter.close();

	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public Farm restore(String path){
		Farm fm = new Farm();
		Cell[][][] grid = null;
		try {
			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(path);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// Read the XML document
			Cell c = null;
			Point3D loc = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (CELL)) {
						loc = new Point3D(-1,-1,-1);
						c = new Cell();
						continue;
					}
					if (startElement.getName().getLocalPart().equals(LATITUDE)) {
						event = eventReader.nextEvent();
						fm.setLatitude(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}

					if (startElement.getName().getLocalPart().equals(LONGITUDE)) {
						event = eventReader.nextEvent();
						fm.setLongitude(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}

					if (startElement.getName().getLocalPart().equals(RELIEF)) {
						event = eventReader.nextEvent();
						fm.setRelief(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}

					if (startElement.getName().getLocalPart().equals(LAYERS)) {
						event = eventReader.nextEvent();
						grid = new Cell[64][64][Integer.decode(event.asCharacters().getData())];
						fm.setGrid(grid);
						continue;
					}
					//if (startElement.getName().getLocalPart().equals(CELLSIZE)) {
					//	event = eventReader.nextEvent();
					//  c.setCellSize(Double.parseDouble(event.asCharacters().getData()));
					//	continue;
					//}
					if (startElement.getName().getLocalPart().equals(HEIGHT)) {
						event = eventReader.nextEvent();
						c.setHeight(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}
					if (startElement.getName().getLocalPart().equals(SURFACE)) {
						event = eventReader.nextEvent();
						c.setSurface((event.asCharacters().getData().equals("true")));
						continue;
					}
					if (startElement.getName().getLocalPart().equals(DEPTH)) {
						event = eventReader.nextEvent();
						c.setDepth(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}
					if (startElement.getName().getLocalPart().equals(VOLUME)) {
						event = eventReader.nextEvent();
						c.setWaterVolume(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}
					if (startElement.getName().getLocalPart().equals(XCOORD)) {
						event = eventReader.nextEvent();
						loc.x = Integer.parseInt(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(YCOORD)) {
						event = eventReader.nextEvent();
						loc.y = Integer.parseInt(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(ZCOORD)) {
						event = eventReader.nextEvent();
						loc.x = Integer.parseInt(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(SOILTYPE)) {
						event = eventReader.nextEvent();
						c.setSoil(Soil.valueOf(event.asCharacters().getData()));
						continue;
					}
					if (startElement.getName().getLocalPart().equals(PLANTTYPE)) {
						event = eventReader.nextEvent();
						c.setPlant(Plant.valueOf(event.asCharacters().getData()));
						continue;
					}
				}
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (CELL)) {
						c.setCoordinate(loc);
						grid[loc.x][loc.y][loc.z] = c;
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return fm;
	}
	/**
	 * 
	 * @param thefarm
	 * @param configFile
	 */
	public void initGround(Farm thefarm, String configFile){
		ArrayList<double[]> soilTbl = ParseConfig(configFile);
		Cell[][][] strata = DetSoil(soilTbl);
		loadGround(thefarm,strata);
	}

	private void loadGround(Farm thefarm, Cell[][][] strata){
		Cell [][][] grid = thefarm.getGrid();
		int h = grid[0][0].length;
		for (int x=0;x<64;x++)
		{
			for (int y=0;y<64;y++)
			{
				double curr_ht = 0.0;
				double strata_ht = 0.0;
				int strata_idx = 0;
				boolean found = false; 
				for (int z=h; z<0; z--)
				{
					if (grid[x][y][z] == null)
					{
					}
					else if (found || grid[x][y][z].isSurface())
					{
						found = true;
						cellCopy(strata[x][y][strata_idx],grid[x][y][z]);
						grid[x][y][z].setDepth(curr_ht);
						curr_ht += grid[x][y][z].getHeight();
						if (curr_ht >= (strata[x][y][strata_idx].getHeight()+strata_ht))
						{
							strata_ht += strata[x][y][strata_idx].getHeight();
							strata_idx++;
							strata_idx = (strata_idx == strata[0][0].length)?strata_idx:--strata_idx;
						}
					}
				}
			}
		}
	}
	private void cellCopy(Cell src, Cell dest)
    {
    	src.setSoil(dest.getSoil());
    }
	private Cell[][][] DetSoil(ArrayList<double[]> soilTbl)
	{
		Soil[] soilArr = {Soil.GILASAND, Soil.GILAFINESANDYLOAM, Soil.GILALOAM, Soil.GILACLAYLOAM, Soil.GILACLAY, Soil.RIVERWASH};
		int nrStratum = soilTbl.size();
		double[] tmptbl = new double[8];
		double[] strataTbl;
		Cell[][][] strata = new Cell[64][64][nrStratum];
		for (int x = 0;x < 64; x++)
                   for (int y=0;y<64;y++)
                      for (int z=0;z<nrStratum;z++)
                         strata[x][y][z] = new Cell();
		Random die = new Random();
		for (double[] t : soilTbl)
		{
			double sum = 0;
			for (int i= 0;i < t.length-1;i++)
			{
				sum += t[i];
			}
			for (int i= 0;i < t.length-1;i++)
			{
				t[i] = (t[i]/sum);
			}
		}
		for (int z = 0; z < nrStratum; z++)
		{
			strataTbl = soilTbl.get(z);
			for (int x = 0; x < 64; x++)
			{
				if (x == 0)
				{
					tmptbl[0] = 0;
				}
				for (int y = 0; y < 64; y++)
				{
					if (x != 0)
					{
						tmptbl[0] = (strataTbl[idxSoil(soilArr,strata[x-1][y][z].getSoil())]);
						if (y != 0)
						{
							tmptbl[0] /= 2;
						}
					}
					if (y == 0)
					{
						tmptbl[1] = 0;
					}
					else
					{
						tmptbl[1] = (strataTbl[idxSoil(soilArr,strata[x][y-1][z].getSoil())]);
						if (x != 0)
						{
							tmptbl[1] /= 2;
						}
					}
					double tmpadj = 1 - (tmptbl[0]+tmptbl[1]);
					for (int i = 0; i < 6; i++)
					{
						tmptbl[i+2] = strataTbl[i]*tmpadj;
					}
					for (int i=1;i<8;i++)
					{
						tmptbl[i] += tmptbl[i-1];
					}
					int idx = idxTbl(tmptbl,die.nextDouble());
					Soil det;
					if (idx == 0)
					{
						det = strata[x-1][y][z].getSoil();
					}
					else if (idx == 1)
					{
						det = strata[x][y-1][z].getSoil();
					}
					else
					{
						det = soilArr[idx-2];
					}
					strata[x][y][z].setSoil(det);
					strata[x][y][z].setHeight(strataTbl[6]);
				}
			}
		}
		return strata;
		}
	
	private int idxSoil(Soil[] sArr, Soil s)
	{
		int i = 0;
		while (sArr[i++] != s);
		return --i;
	}
	private int idxTbl(double[] arr, double d)
	{
		int i = 0;
		while (arr[i++] < d);
		return --i;		
	}
	private ArrayList<double[]> ParseConfig(String configFile){
		ArrayList<double[]> layer = new ArrayList<double[]>();
		double[] tbl = new double[7];
		try {
			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// Read the XML document

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (STRATUM)) {
						tbl = new double[7];
						continue;
					}
					if (startElement.getName().getLocalPart().equals(GILASAND)) {
						event = eventReader.nextEvent();
						tbl[0] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(GILAFINESANDYLOAM)) {
						event = eventReader.nextEvent();
						tbl[1] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(GILALOAM)) {
						event = eventReader.nextEvent();
						tbl[2] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(GILACLAYLOAM)) {
						event = eventReader.nextEvent();
						tbl[3] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(GILACLAY)) {
						event = eventReader.nextEvent();
						tbl[4] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(RIVERWASH)) {
						event = eventReader.nextEvent();
						tbl[5] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
					if (startElement.getName().getLocalPart().equals(HEIGHT)) {
						event = eventReader.nextEvent();
						tbl[6] = Double.parseDouble(event.asCharacters().getData());
						continue;
					}
				}
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (STRATUM)) {
						layer.add(tbl);
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return layer;
	}
	private void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// Create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

}
