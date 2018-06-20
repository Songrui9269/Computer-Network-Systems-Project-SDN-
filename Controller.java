//Computer Network System project 
//Songrui Li
//2/10/2017
//centralized controller part

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.net.*;
import java.io.*;
import java.util.Timer;



public class Controller 
{
	public static void main(String args[]) throws Exception
	{
		//Assign a port to the SDN controller 
		DatagramSocket ctrlerSocket = new DatagramSocket(3421);
		System.out.println("The port of the centralized controller is 3421.");
		
        ConcurrentHashMap <String,String> tbLog = new ConcurrentHashMap <String,String>();
	    Hashtable <String,String> tbPort = new Hashtable <String,String> ();
        Hashtable <String,String> tbIPadd = new Hashtable <String,String> ();
        Hashtable <String,String> tbActiveswc = new Hashtable <String,String> ();
        Hashtable <String,String> tbNeighborswc = new Hashtable <String,String> ();
		
		int count = 1;
		int portNb = 1;
	    int nbofTotalswc = 0;
		int pathWidth[][] = null;
		int pathDelay[][] = null;
		byte[] rcvDat = new byte[2048];
        byte[] sdDat = new byte[2048];
		Timer clock;
		StringBuilder fOutcome = new StringBuilder();
			
		
		pathWidth = readFile(fOutcome,args[0],pathWidth);
		pathDelay = readFile1(args[0],pathDelay,tbNeighborswc);
		
		
	
		//logging
		BufferedWriter fileLog = null;
		File fileResult = new File(args[1]);
		try
		{
	
			fileLog = new BufferedWriter(new FileWriter(fileResult));
		    fileLog.write("The logging file is created successfully \n");
		}catch (IOException e)
		{
			
		}

		
        while (count != nbofTotalswc)
        {
        	
        	DatagramPacket rcvPacket = new DatagramPacket(rcvDat, rcvDat.length);
        	ctrlerSocket.receive(rcvPacket);
        	StringBuilder nebr = new StringBuilder();
        	InetAddress addrIp = rcvPacket.getAddress();
        	portNb = rcvPacket.getPort();
        	String words = new String( rcvPacket.getData());
        	rcvDat = new byte[2048];
        	sdDat = new byte[2048];
       


        	String sID = "";
        	if(words.contains("REGISTER_REQUEST"))
        	{

        		sID = words.split(" ")[0];

        		if(tbActiveswc.contains(sID)) 
        			continue;
        		
        		fileLog.write("The controller received REGISTER_REQUEST signal from ID " + sID + "switch\n");
        		tbIPadd.put(sID,addrIp.toString());
        		tbActiveswc.put(sID, "Active");
        		tbPort.put(sID, Integer.toString(portNb));
        		nebr.append("");
        		
        		
        		nbofTotalswc = nebrtbl(nebr,sID,fOutcome,nbofTotalswc,tbActiveswc,tbPort,tbNeighborswc,tbIPadd);
        		nebr.insert(0, "REGISTER_RESPONSE");
        	
        		System.out.println("The signal sent to ID "+sID+" switch is " + nebr.toString());
        		
        		//logging
        		fileLog.write("The Register response sent to "+sID+" switch is " + nebr.toString()+"\n");
        		sdDat = nebr.toString().getBytes();
        		DatagramPacket sdPacket = new DatagramPacket(sdDat, sdDat.length, addrIp, portNb);
        		ctrlerSocket.send(sdPacket);
        		count = count +1;
        		fileLog.flush();
        	}
        }
        
        System.out.println("Register of switches are completed.");
        

        
        int pathWidth1[][] = new int [nbofTotalswc][nbofTotalswc];
		int pathDelay1[][] = new int [nbofTotalswc][nbofTotalswc];
		int a1,a2;
		for(a1 = 1;a1 < nbofTotalswc; a1++)
		{
			for(a2 = 1;a2 < nbofTotalswc; a2++)
			{
				pathWidth1[a1][a2] = pathWidth[a1][a2];
				pathDelay1[a1][a2] = pathDelay[a1][a2];
			}
		}
        Hashtable <String,String> tbNeighborswc1 = new Hashtable <String,String>(); 
    
        int xxx1 = 2;
        while(xxx1<=nbofTotalswc)
        {
            String xxx2 = tbNeighborswc.get(String.valueOf(xxx1).trim());
            String xxxStr = String.valueOf(xxx1).trim();

            tbNeighborswc1.put(xxxStr,xxx2.trim());
            xxx1++;
        }

        
        
        ConcurrentHashMap <String,String> tbActivechk = new ConcurrentHashMap <String,String>();

        int varChk1 = 2;
        for(varChk1=1;varChk1<=nbofTotalswc;varChk1++)
        {
        	tbActivechk.put(String.valueOf(varChk1).trim(), "Active");
        }
 
        
        int varLog11 = 0;
        for(varLog11=1;varLog11<=nbofTotalswc;varLog11++)
        {
        	tbLog.put(String.valueOf(varLog11).trim(), " ");
        }
        
        
		//check every 4 seconds
    	clock = new Timer("Timer");
        clock.schedule(new Timerfunction(tbActivechk,nbofTotalswc,fileLog), 4000); 

        
        int xxx2 = 1;
        while (true)
        {
        	if(xxx2 == 2) 
        		break;
        
        	int varChk2;
            for(varChk2 = 1; varChk2 <= nbofTotalswc; varChk2++)
            {
            	if(tbActivechk.get(String.valueOf(varChk2).trim()).equals("Inactive"))
            	{
        			tbActiveswc.put(String.valueOf(varChk2).trim(), "Inactive");
            	}
            }

    		

        	
        	rcvDat = new byte[2048];
            sdDat = new byte[2048];
        	
            
        	DatagramPacket packetRcv = new DatagramPacket(rcvDat, rcvDat.length);
        	ctrlerSocket.receive(packetRcv);
        	String words = new String( packetRcv.getData());
        	System.out.println("Received: " + words);
        	InetAddress addrIp = packetRcv.getAddress();
        	portNb = packetRcv.getPort();

        	String [] neboRealive = words.split(" ");
        	
        	

            if(words.contains("REGISTER_REQUEST"))
            {
            	
            	StringBuilder nebo = new StringBuilder();
                String sID = words.split(" ")[0];

                tbActiveswc.put(sID.trim(), "Active");
                tbIPadd.put(sID.trim(),addrIp.toString());
                tbPort.put(sID.trim(), Integer.toString(portNb));
        		nebo.append("");
        		
        		//logging
        		fileLog.write("ID " +sID+"switch returned REGISTER_REQUEST signal"+"\n" );
        		
        
        		nbofTotalswc = nebrtbl(nebo,sID,fOutcome,nbofTotalswc,tbActiveswc,tbPort,tbNeighborswc,tbIPadd);
        		nebo.insert(0, "REGISTER_RESPONSE");
        	

        		System.out.println("The signal sent to ID "+sID+" switch is " + nebo.toString());
        		fileLog.write("The Register response signal sent to ID "+sID+" switch is " + nebo.toString()+"\n");
        		sdDat = nebo.toString().getBytes();
                DatagramPacket packetSd = new DatagramPacket(sdDat, sdDat.length, addrIp, portNb);
                ctrlerSocket.send(packetSd);
            }

            
        	if(neboRealive[0].trim().equals("TOPOLOGY_UPDATE"))
        	{
        		String sID = "";
        		sID = neboRealive[2];
        		int count1 = 1;
        		
        		
        		tbActivechk.put(sID.trim(), "Active");

        		

                if(tbActiveswc.get(sID.trim()).equals("Inactive"))
                {
                    System.out.println("The previous inactive switch ID  \n"+sID+"inactive switch is re-active again. Now updating");

                    tbActiveswc.put(sID.trim(),"Active");

                    tbNeighborswc1.put(sID.trim(),tbNeighborswc.get(sID.trim()));
     
                    int count2;
                    int count3;
                    for(count2= 1; count2 <nbofTotalswc;count2++)
                    {
                        for(count3= 1; count3 <nbofTotalswc;count3++)
                        {
                            if((count2 == (Integer.parseInt(sID.trim()))-1) && (count3 == (Integer.parseInt(sID.trim())-1)))
                            {                             
                                pathWidth1[count2][count3] = pathWidth[count2][count3];
                                pathDelay1[count2][count3] = pathDelay[count2][count3];
                            }
                        } 
                    } 
                                             
                }

        		while(count1 < neboRealive.length-1)
        		{
  
        			String [] inCas = neboRealive[count1].split("_");
   
        			
        			if((inCas[1].trim()).equals("Inactive"))
        			{
        				
        				System.out.println("Upate " + inCas[0] + "with inactive switch.");

        				if(tbNeighborswc1.get(inCas[0].trim()).equals(""))
        				{
        					System.out.println("Update the graph of inactive switch, ID is " + inCas[0]);
        					tbActiveswc.put(inCas[0].trim(), "Inactive");
        					
        					int count2;
        					int count3;
        					
     
        					for(count2= 0; count2 <nbofTotalswc;count2++)
        					{
        						for(count3= 0; count3 <nbofTotalswc;count3++)
        						{
        							if((count2 == (Integer.parseInt(inCas[0].trim()))-1) || (count3 == (Integer.parseInt(inCas[0].trim())-1)))
        							{
     
        								pathWidth1[count2][count3] = 0;
        								pathDelay1[count2][count3] = 0;
        							}
        						}
        					}
        				}
        				
        				
        				

        				String [] inCas1 = null;
        				inCas1 = tbNeighborswc1.get(sID).split(" ");
        				int varNebo = 0;
        				String inCas2 = "";

        		
 
        				if(inCas2!="") 
        					inCas2 = inCas2.trim();

        				tbNeighborswc1.put(sID, inCas2);
        				
        				
        				
        				System.out.println("Updating the dead links of neighbor switches, the ID is " + inCas[0]);

        				inCas1 = null;
        				inCas1 = tbNeighborswc1.get(inCas[0].trim()).split(" ");
        				varNebo = 0;
        				inCas2 = "";
        				while(varNebo<inCas1.length)
        				{
      
        					if(!(inCas1[varNebo].trim()).equals((sID.trim())))
        					{
   
        						inCas2 += inCas1[varNebo].trim()+" ";
        					}
        					varNebo++;
        				}
   
        				
        				if(inCas2!="") inCas2 = inCas2.trim();

        				tbNeighborswc1.put(inCas[0].trim(), inCas2);
        				
        				
        				
        				
        				System.out.println("Now updating the graph.");

        				int count2;
    					int count3;
    			
    					for(count2= 0; count2 <nbofTotalswc;count2++)
    					{
    						for(count3= 0; count3 <nbofTotalswc;count3++)
    						{
   
    							if((count2 == (Integer.parseInt(sID.trim()))-1) && (count3 == (Integer.parseInt(inCas[0].trim())-1)))
    							{
    			
    								pathWidth1[count2][count3] = 0;
    								pathDelay1[count2][count3] = 0;
    							}
    	
    							
    						}
    					}
    				
        				
        				
        			}
        			
        			
      
        			else if ((inCas[1].trim()).equals("Active"))
        			{
        			
        				if(tbActiveswc.get(inCas[0].trim()).equals("Inactive"))
        				{
        					System.out.println("Update re-active switches.");
        					tbActiveswc.put(inCas[0].trim(), "Active");
        					System.out.println(inCas[0]+" switch is re-actived again, the graph has updated as well.");
  
        					int count2;
        					int count3;
        		
        					for(count2= 1; count2 <nbofTotalswc;count2++)
        					{
        						for(count3= 1; count3 <nbofTotalswc;count3++)
        						{
        							if((count2 == (Integer.parseInt(inCas[0].trim()))-1) || (count3 == (Integer.parseInt(inCas[0].trim())-1)))
        							{
        
        								pathWidth1[count2][count3] = pathWidth[count2][count3];
        								pathDelay1[count2][count3] = pathDelay[count2][count3];
        							}
        						}
        					}
        				}
        		
        				else 
        				{
 
        					String xxx3 [] =tbNeighborswc1.get(sID).split(" ");
        					int i = 0;
        					int sflag = 0;
        					
        					if(sflag == 0)
        					{
        						
        					System.out.println("Update dead link if link is dead, hold if no dead link. Switch ID is "+inCas[0]);
        
        					String inCas3 = "";
        					inCas3 = tbNeighborswc1.get((sID.trim())).trim() + " " + inCas[0].trim();
        					tbNeighborswc1.put(sID.trim(), inCas3.trim());
        
        					inCas3 = "";
        					inCas3 = tbNeighborswc1.get((inCas[0].trim())).trim() + " " + sID.trim();
        					tbNeighborswc1.put(inCas[0].trim(), inCas3.trim());
        		
        					int count2;
        					int count3;
        	
        					for(count2= 1; count2 <nbofTotalswc;count2++)
        					{
        						for(count3= 1; count3 <nbofTotalswc;count3++)
        						{
        							if((count2 == (Integer.parseInt(sID.trim()))-1) && (count3 == (Integer.parseInt(inCas[0].trim())-1)))
        							{
        		
        								pathWidth1[count2][count3] = pathWidth[count2][count3];
        								pathDelay1[count2][count3] = pathDelay[count2][count3];
        							}
        							if((count2 == (Integer.parseInt(inCas[0].trim()))-1) && (count3 == (Integer.parseInt(sID.trim())-1)))
        							{
        	
        								pathWidth1[count2][count3] = pathWidth[count2][count3];
        								pathDelay1[count2][count3] = pathDelay[count2][count3];
        							}
        						}
        					}
        					}
        				}
        				
        			}
        			
        			count1++;
        		}
        		
        		
        		System.out.println("The progress of failure handle is completed. Now update the route table of this switch ID "+sID);
        
        		
   
        		System.out.println("The route table for switch ID " + sID + " is updating");
        		fileLog.flush();
        	}
        }
        
       System.exit(0);
        
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static int nebrtbl(StringBuilder nebo,String sID,StringBuilder rstFile, int nbofTotalswc, Hashtable <String,String> tbActice,Hashtable <String,String> tbPort,Hashtable <String,String> tbNebo,Hashtable <String,String> tbIpadd)
	{
		StringBuilder inCas1 = new StringBuilder(rstFile);
		String inCas2 = inCas1.toString();
		String [] inCas3 = inCas2.split("\n");
		nbofTotalswc = Integer.parseInt(inCas3[0]);
		int nbofLns = inCas3.length;
		int i = 1;
		
		while(i<nbofLns)
		{
			String [] inCas4 = inCas3[i].toString().split(" ");
			if(inCas4[0].equals(sID))
			{
			
				if(tbActice.containsKey(inCas4[1]))
				{
					nebo.append(" "+inCas4[1]+"_"+tbActice.get(inCas4[1])+"_"+tbIpadd.get(inCas4[1]));
				}
				else
				{
					nebo.append(" "+inCas4[1]+" "+"_Inactive"+"_Unknown IP");
				}

				
				
			}
			else if (inCas4[1].equals(sID))
			{
		
				if(tbActice.containsKey(inCas4[0]))
				{
					nebo.append(" "+inCas4[0]+"_"+tbActice.get(inCas4[0])+"_"+tbIpadd.get(inCas4[0]));
				}
				else
				{
					nebo.append(" "+inCas4[0]+" "+"_Inactive"+"_Unknown IP");
				}
		
				if(tbPort.containsKey(inCas4[0]))
				{
					nebo.append("_"+tbPort.get(inCas4[0]));
				}
				else
				{
					nebo.append("_"+"Unknown Port");
				}
			}
			i = i+2;
		}
	
		return nbofTotalswc;
	}
	
	

	private static int[][] readFile(StringBuilder rstFile,String iptFile,int [][] pathWidth)
	{
		String inCas1 = "";
		int sflag = 0;
		
		try
		{
			
	
			inCas1 = iptFile;
			FileReader rdrFile = new FileReader(inCas1);
			BufferedReader brdReader = new BufferedReader(rdrFile);
			
		
		
		
	

   
            String word="";
            while((word = brdReader.readLine()) != null)
            {
         
            	if(sflag == 1)
            	{
            		
            		String [] inCas2;
            		inCas2 = word.split(" ");
            		
            		pathWidth[Integer.parseInt(inCas2[0].trim())-1][Integer.parseInt(inCas2[1].trim())-1]=Integer.parseInt(inCas2[2].trim());
            		pathWidth[Integer.parseInt(inCas2[1].trim())-1][Integer.parseInt(inCas2[0].trim())-1]=Integer.parseInt(inCas2[2].trim());
            		
            	}

            	
            	rstFile.append(word+'\n');
            }
                brdReader.close();
		}catch(FileNotFoundException ex) 
		{
            System.out.println("Error.");                
        }catch(IOException ex) 
		{
                System.out.println("Error.");                  
          
        }
		
        		return pathWidth;
            	
            }   

             
   

	
	private static int[][] readFile1(String iptFile,int [][] pathDelay, Hashtable <String,String> tbNebo)
	{
		String inCas1 = "";
		int sflag = 0;
		
		try
		{
			inCas1 = iptFile;
			FileReader rdrFile = new FileReader(inCas1);
			BufferedReader brdReader = new BufferedReader(rdrFile);
			
		
   
            String word="";
            while((word = brdReader.readLine()) != null)
            {
  
            	if(sflag == 0)
            	{
            		
            		String [] inCas2;
            		inCas2 = word.split(" ");
            		pathDelay[Integer.parseInt(inCas2[0].trim())-1][Integer.parseInt(inCas2[1].trim())-1]=Integer.parseInt(inCas2[3].trim());
            		pathDelay[Integer.parseInt(inCas2[1].trim())-1][Integer.parseInt(inCas2[0].trim())-1]=Integer.parseInt(inCas2[3].trim());
            
            		if(tbNebo.containsKey(inCas2[0]))
            		{
         
            			String inCas3 = tbNebo.get(inCas2[0]) + " "+inCas2[1];
            			tbNebo.put(inCas2[0], inCas3);
            			if(tbNebo.containsKey(inCas2[1]))
            			{
            				String inCas4 = tbNebo.get(inCas2[1]) + " "+inCas2[0];
            				tbNebo.put(inCas2[1],inCas4);
            			}
            			
            		}
            		
            		
            	}
            	if(sflag == 1)
            	{
                    pathDelay= new int [Integer.parseInt(word.trim())][Integer.parseInt(word.trim())];
            
                    sflag = 2;
            	}
            }
            brdReader.close();
		}catch(FileNotFoundException ex) 
		{
            System.out.println("Error.");                
        }
            catch(IOException ex) {
                System.out.println("Error.");                  
                
            }
            	
            	
          

		return pathDelay;
	}
	
	
	
	
	
}
