import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
public class Widest_path_computation implements Runnable
{
	int [][]pathWidth;
	int [][]pathDelay;
	int nbofTotalswc;
	int nbofPort;
	String sID;
	ConcurrentHashMap <String,String> tbLog;
	Hashtable <String,String> tbActive; 
	DatagramSocket ctrlerSocket;
	InetAddress addrIp;


	BufferedWriter fileLog;

	
	public Widest_path_computation(String sID, InetAddress addrIp,int nbofPort, Hashtable <String,String> tbActive, int[][]pathWidth,int[][]pathDelay,DatagramSocket ctrlerSocket,int nbofTotalswc,BufferedWriter fileLog,ConcurrentHashMap <String,String> tbLog)
	{
		this.sID = sID;
		this.ctrlerSocket = ctrlerSocket;
		this.nbofTotalswc = nbofTotalswc;
		this.pathDelay = pathDelay;
		this.tbLog = tbLog;
		this.addrIp = addrIp;
		
		this.fileLog = fileLog;
		this.tbActive = tbActive;
		this.pathWidth = pathWidth;
		this.nbofPort = nbofPort;
	}
	public void run()
	{
		try
		{
			System.out.println("The thread"+Thread.currentThread().getName().toString()+" is updating the topology of switch" + sID);
			int nbActvswc = nbofTotalswc;
			
			String inCas1 = "";
			inCas1 = diAlg(pathWidth,pathDelay,Integer.parseInt(sID.trim())-1,nbActvswc);
			System.out.println("The topology is " +inCas1);
			
			String rsltDat = "Topo_UPDATE "+inCas1;
			byte[] sdDat = new byte[1024];
			sdDat = rsltDat.getBytes();
			DatagramPacket sdPkt = new DatagramPacket(sdDat, sdDat.length, addrIp, nbofPort);
        	ctrlerSocket.send(sdPkt);

        	if(!tbLog.get(sID.trim()).equals(inCas1.trim()))
        	{
 
        		tbLog.put(sID.trim(),inCas1.trim());
        		fileLog.write("The updated table for switch ID "+sID+" is \n"+inCas1+"\n");
        	}
			fileLog.flush();
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	

	
	
	String shstCom(StringBuilder ptWist,int [][] pathDelay)
	{
		int i = 0;
		int xxx2 = -1;
		int xxx3 = Integer.MAX_VALUE;
		int xxx4 = 0;
		Hashtable <Integer,Integer> xxx1 = new Hashtable <Integer,Integer>();
		String inCas1 = "";
		StringBuilder rstinCas1 =new StringBuilder();
		String ptll = ptWist.toString();
		String ptinCas1[] = ptll.split("\n");
		
		while(i<ptinCas1.length)
		{
			xxx4 = 0;
			xxx3 = 0;
			if(ptinCas1[i]!=null && !ptinCas1[i].equals(""))
			{
			String inCas2 [] = ptinCas1[i].split(",");
			xxx2 = Integer.parseInt(inCas2[0].trim());
			
			if(xxx1.containsKey(xxx2))
			{
				int xxx5 = rstinCas1.indexOf(inCas1);
				int count1 = 0;
				while(count1<inCas2.length-1)
				{
					xxx3+=pathDelay[Integer.parseInt((inCas2[count1]).trim())-1][Integer.parseInt(inCas2[count1+1].trim())-1];
					count1++;
				}
			
				
			}
	

			else
			{
				
				int count2 = 0;
				while(count2<inCas2.length-1)
				{
					xxx4+=pathDelay[Integer.parseInt((inCas2[count2]).trim())-1][Integer.parseInt(inCas2[count2+1].trim())-1];
					count2++;
				}
				xxx1.put(xxx2, xxx4);
				inCas1 = ptinCas1[i].toString();
				rstinCas1.append(ptinCas1[i]+"\n");
			}
			}
			i++;
		}
		return rstinCas1.reverse().toString();
	}
	
	
	StringBuilder fctPit(List<Vetx> aa1,Vetx [] aa2,int aa3,int aa4)
	{
	
		int i;
		StringBuilder inCas1 = new StringBuilder();
		StringBuilder inCas2 = new StringBuilder();
		for(i=0;i<aa4;i++)
		{
			if(i != aa3)
			{
				if(aa2[i].getPrev()!=null)
				{
					Iterator <Vetx> aa5 = aa2[i].getPrev().iterator();

			while(aa5.hasNext())
			{
				inCas2 = new StringBuilder();
				Vetx aa6 =aa5.next();
				inCas2.append((i+1)+","+(aa6.getId()+1));
				
				
			}
		}
			
			}
			
		}
		return inCas1;
	}
	
	
	
	
	String diAlg(int ary1[][], int pathDelay[][],int aa1,int aa2)
    {
		List<Vetx> a1 = null;
        Vetx a2 [] = new Vetx [aa2];
        Queue<Integer> sQ = new LinkedList<Integer>();
        int wita[] = new int[aa2];
        int i;
        
        for(i=0;i<aa2;i++)
        {
        	
        	Vetx a3 = new Vetx();
        	a3.setId(i);
        	a2[i] = a3;
        	a2[i].setwidth(-Integer.MAX_VALUE);
        	wita[i] = -Integer.MAX_VALUE;
        	sQ.add(i);
        }
		wita[aa1]=Integer.MAX_VALUE;
		a2[aa1].setwidth(Integer.MAX_VALUE);
		
		while(!sQ.isEmpty())
		{
			i = 0;
 
        	Queue<Integer> sQ1 = new LinkedList<Integer>(sQ);
        	int a5 = -1;
        	int a6 = -1;
        	int a7 = 0;

        	while(!sQ1.isEmpty())
        	{
        		a7 = sQ1.poll();
        		if(wita[a7]>a5)
        		{
        			a5 = wita[a7];
        			a6 = a7;
        		}
        	}
        	
        	
        	Queue<Integer> sQ2 = new LinkedList<Integer>(sQ);
        	int ary2 [] = new int [aa2];
         	while(!sQ2.isEmpty())
        	{
        		int a8 = sQ2.poll();
        		ary2[a8] = 1;
        	}
        
        	if(wita[a6] == -Integer.MAX_VALUE) 
        		break;
		
		int a9 = 0;
		for(a9=0;a9<aa2;a9++)
		{

			if(ary1[a6][a9] != 0 && ary2[a9]==1)
			{
				
				a1 = a2[a9].getPrev();
				int a10 = Math.max(wita[a2[a9].getId()],Math.min(wita[a6],ary1[a6][a2[a9].getId()]));
				if(a10>wita[a2[a9].getId()])
				{
					sQ.remove(a9);
					
					wita[a2[a9].getId()] = a10;
					a2[a9].setPrevious(a2[a6]);
					a1 = new ArrayList<Vetx>();
					a1.add(a2[a6]);
					a2[a9].setPrev(a1);
					sQ.add(a9);
				}
				
			}
		}
		}
		StringBuilder wistPt = new StringBuilder();
			

		wistPt=fctPit(a1,a2,aa1,aa2);
		return shstCom(wistPt,pathDelay);
    }
}
