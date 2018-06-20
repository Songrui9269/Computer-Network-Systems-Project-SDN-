import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class Switch 
{

	public static Timer clock1;
	public static Timer clock2;

	static ConcurrentHashMap <Integer, Integer> m1 = new ConcurrentHashMap<Integer, Integer>();
	static ConcurrentHashMap <Integer, String> m2 = new ConcurrentHashMap<Integer, String>();
	static int a1,a2,a3,a4;
	static String inCas1,inCas2;
	static int[] a5 = {1};
	static String[] inCas3 = {""},inCas4 = {""},inCas5 = {""};

	
	Switch(int sID,String incas1,int sP)
	{
	    a1 = sID;
	    a2 = sP;
	    inCas1 = incas1;

	}
	public static void main(String args[]) throws Exception    
	{	       
		int aa1 = 0,aa2 = 0, aa3 = 0;
		String inCass1 = "";
	
		try
		{
			Switch fct1 = new Switch(Integer.parseInt(args[0]),args[1],Integer.parseInt(args[2]));
			
			aa1 = fct1.a1;
			a3 = fct1.a2;
			inCas2 = fct1.inCas1;
		}		
		catch (Exception e)
		{
			System.out.println("Error");
			
		}

		try
		{
			if (args[3].equals("-f"))
			{
				aa2 = Integer.parseInt(args[4]);
				System.out.println("Unsuccessful switch ID is:" + aa2);
			}
			
			
		}
		catch(Exception e)
		{}
		
		try{
			if(args[5].equals("-p"))
			{
				aa3 = 1;
			}
		}
		catch(Exception e)
		{}
		
		a4 = 0;
		int aa0 = 0;
		byte[] rcvDat = new byte[2048];  
		DatagramSocket swcSocket = new DatagramSocket();        
		InetAddress addrIp = InetAddress.getByName(inCas2);  

		byte[] sdDat = new byte[2048];       



		
		String inCass3 = ""; 
		inCass3 = "Result_" + aa1 + ".txt";
		PrintWriter wtr = new PrintWriter(inCass3,"UTF-8");
		wtr.println("Updated information for Switch" + aa1);


		int i;
		String[] inCass4;
		String inCas7 = "";


		try{
			DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, addrIp, a3);  
			String inCas8 = "";
			String inCas9 = String.valueOf(aa1);
			inCas8 = inCas9 + " " + "REGISTER_REQUEST";
			sdDat = inCas8.getBytes(); 

	
			swcSocket.send(sdPkt);    
			
			
			wtr.println("Now sending "+inCas8.toString() + " to controller");
			wtr.flush();
			
			DatagramPacket rcvPkt = new DatagramPacket(rcvDat, rcvDat.length);       
			swcSocket.receive(rcvPkt);   
		
			inCas7 = new String(rcvPkt.getData());    
			System.out.println("From the controller the information is:" + inCas7);  

			inCass4 = inCas7.split(" "); 
			System.out.println(inCass4[0]);

			a4 = inCass4.length;


			if (inCass4[0].equals("REGISTER_RESPONSE"))
			{
				//print the REGISTER_RESPONSE to file
				StringBuilder write_to_file = new StringBuilder();
				i=1;
				
				
				
				a5 = new int[a4-1];
				inCas3 = new String[a4-1];
				inCas4 = new String [a4-1];
				inCas5 = new String[a4-1];

				i = 0;

				while( i < a4-1 )
				{	
					String[] inCass5 = inCass4[i + 1].split("_");  
					a5[i] = Integer.parseInt(inCass5[0]);
					inCas3[i] = inCass5[1];
				
		
					m2.put(i, inCas3[i]);

					i = i + 1; 

				}

				
			}

			aa0 = 0;	
		
		}
		catch (Exception e)
		{
			System.out.println("Error");

		} 

		clock1 = new Timer("Timer");
			
		clock1.schedule(new Switchfct1(clock1,a4, inCas3, inCas4, inCas5, a1, addrIp, a5, a3, swcSocket, m1, m2, wtr),4000); 
		
		clock2 = new Timer("Timer2");
		clock2.schedule(new Switchfct2(m1, a4, a5, clock2, swcSocket, a1, addrIp, a3, m2, wtr), 8000);
		
		
		String inCas11;

		while(true){

			System.out.println("Thread is beginning: __" + Thread.currentThread().getName() + "__");
			if (aa0 == 1)
			{
				for (i = 0; i < a4-1 ; i ++)
				{
					if(a5[i] != aa2)
					{
						if(inCas3[i].equals("Active"))
						{
							inCas11 = "KEEP_ALIVE " + String.valueOf(a1) + " ";
							System.out.println(inCas11);
							sdDat = inCas11.getBytes();

							DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, addrIp, Integer.valueOf(inCas4[i].trim()));

							swcSocket.send(sdPkt);
		
							System.out.println("Now sending KEEP_ALIVE signal to switch ID " + a5[i]);
							
							
							
						}
					}
				}
				
				
			}
			
			aa0 = 0;

			try{
				rcvDat = new byte[1024]; 
				DatagramPacket rcvpkt2 = new DatagramPacket(rcvDat, rcvDat.length);       
				swcSocket.receive(rcvpkt2);      			
				String inCas12 = new String(rcvpkt2.getData());
				System.out.println("FROM:" + inCas12);

				int a11 = 0;
				a11 = rcvpkt2.getPort();
				InetAddress a12 = rcvpkt2.getAddress();

				inCass4 = inCas12.split(" "); 
				

				if (inCass4[0].trim().equals("KEEP_ALIVE_CHECK"))
				{
					String inCas13 = inCass4[1];
					int a13 = Integer.parseInt(inCas13.trim());
					int count ;
					for (count = 0; count < a4-1 ;count++)
					{
						if(a5[count] == a13)
							break;
						
					
					}
					
					inCas3[count] = "Active";
					inCas4[count] = String.valueOf(a11);
					inCas5[count] = a12.toString();
			
					

					try{
						int a14 = 0;
						a14 = m1.get(a5[count]);
					
						if(a14 == 1)
						{
				
							m1.put(a5[count],2);
						}
					}
					catch(Exception e){}
					
				}

				if (inCass4[0].trim().equals("KEEP_ALIVE"))
				{
					String inCas15 = inCass4[1];
					int a15 = Integer.parseInt(inCas15.trim());
					int count ;

					if(aa3 == 1)
					{
						wtr.println("Received " + inCas12 + " from switch ID" + inCas15);
						wtr.flush();
					}
					

					if(a15 != aa2){
						for (count = 0; count < a4-1 ;count++)
						{
							if(a5[count] == a15)
								break;
						
						}
						
						inCas3[count] = "Active";
						inCas4[count] = String.valueOf(a11);
						
						inCas11 = "KEEP_ALIVE_CHECK " + String.valueOf(a1) + " ";
						sdDat = inCas11.getBytes();



						DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, a12, a11);
						swcSocket.send(sdPkt);
						System.out.println("Now sending out the KEEP_ALIVE_CHECK signal. ");
						
						
					}
				}

				if (inCass4[0].trim().equals("ROUTE_UPDATE"))
				{
					
					System.out.println("Now accepting the ROUTE_UPDATE signal from central controller: ");
					System.out.println(inCass4[1]);
					
					if(inCass1.equals(""))
					{
						inCass1 = inCass4[1];
						wtr.println("Now updating the Topology Route Table");
						wtr.println(inCass4[1].toString().trim());
						wtr.flush();
					}
					else{
						if(!inCass1.equals(inCass4[1]))
						{
							inCass1 = inCass4[1];
							wtr.println("Now updating the Topology Route Table");
							wtr.println(inCass4[1].toString().trim());
							wtr.flush();
						}
					}
				
				}


			}
			catch(Exception e){
				System.err.println("Error.");

			}
			
			
		}  
}
}


class Switchfct2 extends TimerTask
{
	DatagramSocket swcSocket;
	ConcurrentHashMap<Integer, String> tbActvnebo;
	ConcurrentHashMap <Integer, Integer> sdAlvchknebo;
	Timer clock;
	int a1,a3,a5;
	InetAddress a4;
	int[] a2;
	PrintWriter wtr;
		
	Switchfct2(ConcurrentHashMap<Integer, Integer> sdAlvchknebo, int a1, int[] a2, Timer clk,DatagramSocket swcSocket, int a3, InetAddress a4, int a5, ConcurrentHashMap<Integer, String> tbAvtvnebo, PrintWriter wtr)
	{
		this.sdAlvchknebo = sdAlvchknebo;
		this.a1 = a1;
		this.a2 = a2;
		this.clock = clk;
		this.swcSocket = swcSocket;
		this.a3 = a3;
		this.a4 = a4;
		this.a5 = a5;
		this.tbActvnebo = tbAvtvnebo;
		this.wtr = wtr;
		
	}
	
	public void run(){
		
		System.out.println("The Thread is beginning: __" + Thread.currentThread().getName() + "__");
		System.out.println("Check whether the KEEPALIVE_CHECK signal is received from other active neighbor switches every 8 seconds.");
		int i ;
		for (i = 0; i < a1 - 1; i++){

			if(sdAlvchknebo.get(a2[i]) == 2)
			{
				System.out.println("Received KEEP_ALIVE_CHECK from switch ID " + a2[i]);


				if(tbActvnebo.get(i).equals("Active"))
				{
					tbActvnebo.put(i, "Active");
				}
			
			}
			else 
			{
				System.out.println("No ALIVE_CHECK signal from switch ID" + a2[i]);
				System.out.println("Now changing the status of switch ID" + a2[i] + "to in-active");
				
				if(tbActvnebo.get(i).equals("Active")){
					tbActvnebo.put(i, "Inactive");
					wtr.println("Switch ID" + a2[i] +"'s status changes from active to in-active");
				}
				
				
			}


		}

		String inCas1 = "";
		inCas1 = "Updated Topology: " + a3 + " ";
		for (i = 0; i < a1-1 ; i ++)
		{
			if(tbActvnebo.get(i).equals("Active"))
			{
				inCas1 = inCas1 + a2[i] + "_Active" + " "; 
			}
			
		}
		
		try
		{
			System.out.println("Now sending the updated topology to central controller in timer2.");
			System.out.println(inCas1);
			
			byte[] a1 = new byte[1024];  
			a1 = inCas1.getBytes();
			DatagramPacket sdPkt = new DatagramPacket(a1, a1.length, a4, a5);
			swcSocket.send(sdPkt);
			 
			System.out.println("Now sending the updated topology to central controller");
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}



		clock.cancel();
		clock = new Timer("timer2");
		clock.schedule(new Switchfct2(sdAlvchknebo, a1, a2, clock, swcSocket, a3, a4, a5, tbActvnebo, wtr), 8000);
	}
	
}




class Switchfct1 extends TimerTask 
{
	ConcurrentHashMap<Integer, String> tbAtvnebo;
	ConcurrentHashMap<Integer, Integer> sdAlvchknebo;
	DatagramSocket swcSocket;
	Timer clock;
	int a1,a2,a5;
	InetAddress a3;
	int[] a4;
	String[] inCasa1,inCasa2,inCasa3;
	PrintWriter wtr;

	
	Switchfct1(Timer clk, int a1, String[] inCasa1, String[] inCasa2, String[] inCasa3, int a2, InetAddress a3, int[] a4, int a5,DatagramSocket swcSocket, ConcurrentHashMap<Integer, Integer> sdAlvchknebo, ConcurrentHashMap<Integer, String> a6, PrintWriter wtr)
	{
		this.clock = clk;
		this.a1 = a1;
		this.inCasa1 = inCasa1;
		this.inCasa2 = inCasa2;
		this.inCasa3 = inCasa3;
		this.a2 = a2;
		this.a3 = a3;
		this.a4 = a4;
		this.a5 = a5;
		this.swcSocket = swcSocket;
		this.sdAlvchknebo = sdAlvchknebo;
		this.tbAtvnebo = a6;
		this.wtr = wtr;
		
	}


	
	public void run(){
		System.out.println("The thread is beginning: __" + Thread.currentThread().getName() + "__");

		System.out.println("Check whether the KEEPALIVE_CHECK signal is received from other active neighbor switches every 4 seconds.");
		int i = 0;
		String inCas1 = "";
		byte[] sdDat = new byte[1024];       
		
			try{
				for (i = 0; i < a1-1 ; i ++)
				{
					if(inCasa1[i].equals("Active"))
					{
						inCas1 = "KEEP_ALIVE " + String.valueOf(a2) + " ";
	
						sdDat = inCas1.getBytes();

						DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, a3, Integer.valueOf(inCasa2[i].trim()));
  
						swcSocket.send(sdPkt);

	
						System.out.println("Now sending the KEEP_ALIVE signal to switchID " + a4[i] + "---" + Thread.currentThread().getName().toString());
				
						sdAlvchknebo.put(a4[i],1);
							
					}
			
					


				inCas1 = "The updated Topology is: " + a2 + " ";
				for (i = 0; i < a1-1 ; i ++)
				{
					if(tbAtvnebo.get(i).equals("Active"))
					{
						inCas1 = inCas1 + a4[i] + "_Active" + " "; 
					}
					
					

				}
				
				try
				{


					sdDat = inCas1.getBytes();
					DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, a3, a5);
					swcSocket.send(sdPkt);
					 	
				
					System.out.println("Now sending the updated Topology to the central controller");
				}
				catch(Exception e)
				{
					System.out.println("Error.");
				}

			

				}
			}
			catch(Exception e)
			{
				System.err.println(e);
				System.out.println("Exiting. ");
				
			}

			
			clock.cancel();
			clock = new Timer("Timer");
			clock.schedule(new Switchfct1(clock,a1, inCasa1, inCasa2, inCasa3, a2, a3, a4, a5, swcSocket, sdAlvchknebo, tbAtvnebo, wtr),4000); 
		


			}
}



	





