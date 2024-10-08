package MainFormFrame;

//<editor-fold defaultstate="collapsed" desc="Imports">
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
//</editor-fold>

public class FileManager {

    //<editor-fold defaultstate="collapsed" desc="Save to TXT File">
    public static void SaveToTxtFile(String filePath, WarehouseData data)
    {
        try
        {
            //Created buffered writer and connects to provided file location.
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            //Writes header dats on first 2 lines of file.
            writer.write(data.WarehouseName + "\n");
            writer.write(data.Date + "\n");
            writer.write(data.Time + "\n");
            //Iterates through the 2D array and places each row of the array in a single line in comma delimited format before moving to next line.
            for (int y = 0; y < data.WarehouseLayout[0].length; y++)
            {
                for (int x = 0; x < data.WarehouseLayout.length; x++)
                {

                    writer.write(data.WarehouseLayout[x][y].getText());
                }
                writer.newLine();
            }
            //Closes writer when done
            writer.close();
        }
        catch (Exception ex)
        {

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Read TXT file">
    public static WarehouseData ReadFromTxtFile(String filePath)
    {
        //Ceate new data model object
        WarehouseData data  = new WarehouseData();
        try
        {
            //Created buffered reader and connects to provided file location.
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            //Reads first 2 lines of file and puts them in the data model fields for the program headers.
            data.WarehouseName = reader.readLine();
            data.Date = reader.readLine();
            data.Time = reader.readLine();
            //Prepares data model to receive grid data.
            data.WarehouseLayout = new JTextField[20][18];

            //creates variables to manage each line read and keep track of line number
            String line;
            int lineNumber = 0;
            //Reads nect line and stores it in line variable before checking whether it is null/empty to determine whether loop runs.
            while ((line = reader.readLine()) != null)
            {
                //Splits line based upon comma locations
                String[] temp = line.split(",");
                //Places each line segment in sequence into the grid of the data model using the lineNUmber variable to determine which row it should be
                //writing to.
                for (int i = 0; i < data.WarehouseLayout.length; i++)
                {
                    data.WarehouseLayout[i][lineNumber]= new JTextField();
                    data.WarehouseLayout[i][lineNumber].setText(temp[i]);
                }
                //Increases line number at end of loop to track how many lines have been processed so far.
                lineNumber++;
            }
            //Closes the reader.
            reader.close();
        }
        catch(Exception ex)
        {
            //Returns the data model at whatever state it is currently at.
            return data;
        }
        //Returns the completed and filled data model
        return data;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save to RAF file">
    public static void SaveToRAFFile(String filePath, WarehouseData data)
    {
        try
        {
            RandomAccessFile raf = new RandomAccessFile(filePath,"rw");

            raf.seek(0);
            raf.writeUTF(data.WarehouseName);
            raf.seek(18);
            raf.writeUTF(data.Date);
            raf.seek(20);
            raf.writeUTF(data.Time);

            int counter = 0;


            for (int x = 0; x < data.WarehouseLayout.length; x++)
            {
                for (int y = 0; y < data.WarehouseLayout[x].length; y++)
                {
                    int start = counter * 50 + 100;
                    raf.seek(start);
                    raf.writeInt(x);
                    raf.seek(start + 18);
                    raf.writeInt(y);
                    raf.seek(start + 20);
                    raf.writeUTF(data.WarehouseLayout[x][y].getText());
                    counter++;
                }
            }
        }
        catch (Exception e)
        {

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Read RAF file">
    public static WarehouseData ReadFromRAFFile(String filepath)
    {
        WarehouseData data = new WarehouseData();
        data.WarehouseLayout = new JTextField[18][20];

        try
        {
            RandomAccessFile raf = new RandomAccessFile(filepath, "r");

            raf.seek(0);
            data.WarehouseName = raf.readUTF();
            raf.seek(2);
            data.Date = raf.readUTF();
            raf.seek(3);
            data.Time = raf.readUTF();

            int counter = 0;

            while(counter * 18l + 20 < raf.length())
            {
                int start = counter * 18 + 20;
                raf.seek(start);
                int xPos = raf.readInt();
                raf.seek(start);
                int yPos = raf.readInt();
                raf.seek(start + 20);
                String value = raf.readUTF();

                data.WarehouseLayout[xPos][yPos] = new JTextField(value);
                counter++;
            }

            return data;
        }
        catch(Exception e)
        {
            return new WarehouseData();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save to RPT file">
    public static void SaveToRPTFile(String filePath, WarehouseData data)  {
        try{

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            int count = 0;
            Color current_color = Color.black;

            for (int row = 0; row < data.WarehouseLayout[0].length; row++){
                for (int col = 0; col < data.WarehouseLayout[row].length; col++){

                    Color color = data.WarehouseLayout[row][col].getBackground();

                    if (current_color == color){
                        count++;

                    }
                    else {
                        if(count > 0){
                            writer.write(TextFieldColorGetter(current_color) + "," + count + ",");
                        }

                        count = 1;
                        current_color = color;
                    }
                }
                writer.write(TextFieldColorGetter(current_color) + "," + count);
                count = 0;
                current_color = Color.black;
                writer.newLine();
            }
            writer.close();
        }
        catch (Exception e){
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save to DAT file">
    public static void SaveToDATFile(String filePath, WarehouseData data){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            StringBuilder builder = new StringBuilder();

            Color current_color = Color.black;
            for (int i = 0; i < data.WarehouseLayout[0].length; i++){

               for (int j = 0; j < data.WarehouseLayout[i].length; j++){

                   Color color = data.WarehouseLayout[j][i].getBackground();
//                   int x = j % data.WarehouseLayout[i].length;
//                   int y = j / data.WarehouseLayout[i].length;

                   if (current_color == color){

                       continue;
                   }
                   else if(i + j > 0 ) {

                           builder.append(j);
                           builder.append(",");
                           builder.append(i);
                           builder.append(TextFieldColorGetter(current_color));
                           builder.append("\r\n");

                       current_color = color;
                   }
               }
               current_color = Color.black;
               writer.write(builder.toString());
               writer.newLine();
            }
            writer.close();
        }
        catch (Exception e){
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Text field color getter">
    private static String TextFieldColorGetter(Color b){
        //Color b = data.getBackground();

        if (b == Color.red){
            return "R";
        }
        if (b == Color.green){
            return "G";
        }
        if (b == Color.yellow){
            return "Y";
        }
        return "W";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Warehouse data">
    public static class WarehouseData {
        public String WarehouseName;
        public String Date;
        public String Time;
        public JTextField[][] WarehouseLayout;
        }
    //</editor-fold>
}
