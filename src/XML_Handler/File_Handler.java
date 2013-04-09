package XML_Handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//
public class File_Handler {
   private BufferedReader buffer_in;
   private File garbage;
   private FileInputStream garbage_in;
   private FileOutputStream garbage_out;
   private OutputStreamWriter buffer_out;
   private DataInputStream data_in;
   private DataOutputStream data_out;
   private String line;
   
   /**
    * Creates a new instance of this class.  Will try to open the file for either Input or Output.
 * @param in_out - IN or OUT.  These are the only two allowed values, and others will throw an exception.
 * @param fil - The name of the file to open.
 * @throws IllegalArgumentException - If the in/out parameter is improperly coded, this will be thrown.
 * @throws FileNotFoundException - If the file, for any reason, cannot be opened.  Includes trying to open a 
 *      file that is already open, or a file that doesn't exist.
 */
File_Handler(String in_out, String fil) throws IllegalArgumentException, FileNotFoundException{
	   String typ = in_out.toUpperCase();
	   if (typ.contentEquals("IN"))
	   {
		   garbage = new File(fil);
		   garbage_in = new FileInputStream(garbage);
		   buffer_in = new BufferedReader(new InputStreamReader(garbage_in));
		   data_in = new DataInputStream(new BufferedInputStream(garbage_in));
	   } 
	   else if (typ.contentEquals("OUT"))
	   {
		   garbage = new File(fil);
		   garbage_out = new FileOutputStream(garbage);
		   buffer_out = new OutputStreamWriter(garbage_out);
		   data_out = new DataOutputStream(new BufferedOutputStream(garbage_out));
		   
	   } 
	   //else if (typ == "INOUT")
	   //{
		   // possible to-do.  I haven't decided yet whether to do an in/out file reader.
	   //} 
	   else
	   {
		   throw new IllegalArgumentException("Argument must be In or Out");
	   }
   }   
   /**
    * Reads a line from an Input (not Output) file.
 * @return - The line to be read.  A null return means there are no more lines to be read.
 * @throws IOException
 */
public String get_line() throws IOException{
	   if (buffer_in == null) throw new IOException("Cannot read from an output-only or closed file.");
	   line = buffer_in.readLine();
	   return line;
   }
/**
 * Gets a double value from the input binary file
 * @return
 * @throws IOException
 */
public double get_double() throws IOException{
	return data_in.readDouble();
}
/**
 * Gets an integer from the input binary file
 * @return
 * @throws IOException
 */
public int get_int() throws IOException{
	return data_in.readInt();
}
/**
 * Gets a character value from the input binary file.
 * @return
 * @throws IOException
 */
public char get_char() throws IOException {
	return data_in.readChar();
}
/**
 * Gets a boolean value from the input binary file.
 * @return
 * @throws IOException
 */
public boolean get_bool() throws IOException {
	return data_in.readBoolean();
}
/**
 * Put a string into the binary output file.
 * @param in_data
 * @throws IOException
 */
public void put_str_data(String in_data) throws IOException
{
	data_out.writeBytes(in_data);
}
/**
 * Put a boolean value into the binary output file.
 * @param in_data
 * @throws IOException
 */
public void put_boolean(boolean in_data) throws IOException
{
	data_out.writeBoolean(in_data);
}
/**
 * Put an integer into the binary output file.
 * @param in_data
 * @throws IOException
 */
public void put_int_data(int in_data) throws IOException
{
	if (data_out == null) throw new IOException("Cannot read from an output-only or closed file.");
	data_out.writeInt(in_data);
}
/**
 * Put a long value into the binary output file
 * @param x
 * @throws IOException
 */
public void put_long_data(long x) throws IOException {
	data_out.writeLong(x);
}
/**
 * Put a double into the binary output file
 * @param in_data
 * @throws IOException
 */
public void put_double_data(double in_data) throws IOException {
	data_out.writeDouble(in_data);
}
/**
 * Put a character into the binary output file.
 * @param in_data
 * @throws IOException
 */
public void put_char_data(char in_data) throws IOException
{
	data_out.writeByte(in_data);
}
   /**
    *  Writes a line to an output file.  Does not function on an input file.
 * @param input_val - the value to be written to the file.  
 * @throws IOException
 */
public void put_line(String input_val) throws IOException{
	   if (buffer_out == null) throw new IOException("Cannot write to an input-only or closed file.");
	   buffer_out.write(input_val+'\n');
   }
/**
 * Closes the file buffers.  This flushes the buffers and ensures output files have all data written to the file.
 *    This closes buffers on both input and output files.
 * @throws IOException - if there is an error in closing the file.
 */
public void close() throws IOException{
	   if (buffer_out != null)
	   {
		   buffer_out.close();
	       garbage_out.close();
	       data_out.close();
	   }
	   else
	   {
		   buffer_in.close();
	       garbage_in.close();
	       data_in.close();
	   }
   }
/**
 * Write a header for a PPM file into a binary output file.
 * @param ht
 * @param wd
 * @param in_file
 * @param eqn
 */
public static void PPMHeader8(int ht, int wd, File_Handler in_file, String eqn)
{
	try {
		//System.out.println("Printing file magic");
		in_file.put_str_data("P6 \n");
		//System.out.println("Printing ht/width");
		in_file.put_str_data(String.valueOf(wd)+" "+String.valueOf(ht)+" 255 ");
		//System.out.println("Printing equation");
		in_file.put_str_data("# eqn ="+eqn+"\n");
		//System.out.println("Printing newline");
		in_file.put_str_data("\n");
		in_file.put_char_data('\0');
		in_file.put_char_data('\0');
	} catch (IOException e) {
		e.printStackTrace();
	}
	
}
/**
 * Does the file exist?
 * @param fil
 * @return
 */
public static boolean exists(String fil)
{
	File tmp = new File(fil);
	return tmp.exists();
}
/**
 * Have we reached the End of File?
 * @return
 * @throws IOException
 */
public boolean EOF() throws IOException {
	
	return (garbage_in.available() > 0);
}

}
