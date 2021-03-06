/**
 * 
 */
package lc1.dp.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import lc1.util.Constants;

import org.freehep.graphics2d.VectorGraphics;

public class LocPanel extends JPanel{
      double y_0 = 10;
      double y_1 =  20;
      double y_2 = 30;
      Dimension dim5;
      static String formatStr= "%9.6g";
     final boolean changewidth;
    static double[] d =null;// new double[] {25.46,25.48,25.50,25.52};

      List<Integer> location;
     
      public static  Font font10 = new Font("SansSerif", Font.PLAIN, 15);
      Map<String, int[]> exons;
      int reg_length;
      int noSnps;
      LocPanel(List<Integer> location, int width, int height, double x_offset, String str){
         this.location = location;
         this.width = width;
         this.x_offset = x_offset;
         this.changewidth = !str.equals("haplotype");
         Dimension dim5 = new Dimension(width, height*2);
         this.setMinimumSize(dim5);
         this.setPreferredSize(dim5);
         this.setSize(dim5);
         reg_length = location.size()==0 ? 0 : location.get(location.size()-1) - location.get(0);
         noSnps = location.size();
      }
      double prev_loc =0;
      final double width;
      public double getX(int i, boolean useLoc){
    	  double width =!changewidth ?this.width :  getWidth();
          if(useLoc){
              double  wid = (width-2*x_offset) / reg_length;
              return x_offset+wid * (location.get(i) - location.get(0));
          }
          double  wid = (width-2*x_offset) / ((double) noSnps);
          return x_offset+((double)i)*wid;
      }
      public double getXLoc(double d, boolean useLoc){
    	  double width = getWidth();
    	  if(useLoc){
              double  wid = (width-2*x_offset) / reg_length;
              return x_offset+wid * (d - location.get(0));
          }
          double  wid = (width-2*x_offset) / ((double) noSnps);
          return x_offset+((double)geti(d))*wid;
      }
      
      private double geti(double d2) {
    	  for(int i=0; i<location.size(); i++){
    		  if(d2<location.get(i)){
    			  if(i==0) {
    				  return 0;
    			  }
    			  else{
    				  int diff = location.get(i) - location.get(i-1);
    				  return (i-1) +  ((d2 - (double)location.get(i-1))/(double)diff);
    			  }
    	  }
    	  }
    	  return location.size();
		
	}
	double x_offset =0;
      public void paint(Graphics g){
          double h = getHeight();
          this.y_0 = (4.0/6.0)*h;
          this.y_1 = (5.0/6.0)*h;
          this.y_2 =h;
          g.setColor(Color.white);
          g.fillRect(0,0, getWidth(), getHeight());
          
          VectorGraphics vg = VectorGraphics.create(g);
          vg.setFont(this.font10);
          vg.setColor(Color.BLACK);
          vg.setLineWidth(0.5);
         /* {
              double x_0 = getX(0, true);
              double x_1 = getX(noSnps-1, true);
            
              
              g.setColor(background_color);
              g.fillRect(0,0, getWidth(), getHeight());
               vg = VectorGraphics.create(g);
               vg.drawRect(x_0, y_0, x_1 - x_0, y_1-y_0);//(arg0, arg1, arg2, arg3)
          }*/
          vg.setColor(Color.BLACK);
          if(d!=null){
        	  
            for( int i1=0; i1<d.length; i1++){
            	double di = d[i1]*1000*1000;
            	double i = this.geti(di);
            	double x_start = getXLoc(di, Constants.useLocInHap());
                
                double x_loc = getXLoc(di, true);
             	   vg.drawLine(x_loc, y_0, x_loc, y_1);
                    vg.drawLine(x_loc, y_1, x_start, y_2);
                    String str =String.format(formatStr, d[i1]).trim()+"mb";
                   
                    vg.drawString(str, x_loc, y_0);
                    prev_loc = x_loc;
                   
            }  
          }
          else{
           for( int i=0; i<noSnps; i++){
               double x_start = getX(i, Constants.useLocInHap());
             
               double x_loc = getX(i, true);
             
               
            	   vg.drawLine(x_loc, y_0, x_loc, y_1);
                   vg.drawLine(x_loc, y_1, x_start, y_2);
                   if(i==0 || x_loc> prev_loc+80){
                   String str =String.format(formatStr, ((double)location.get(i))/(1000.0*1000.0)).trim()+"mb";
                	//   Format.sprintf("%7i",new Number[] {location.get(i)});
                  
                   vg.drawString(str, x_loc, y_0);
                   prev_loc = x_loc;
                  
               }
           }
          }
      }
  }