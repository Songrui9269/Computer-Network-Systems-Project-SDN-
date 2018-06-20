import java.io.BufferedWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class Timerfunction extends TimerTask
{
	ConcurrentHashMap <String,String> tbchkActive;
	int nbofTotalswc;
	BufferedWriter fileLog;
	Timerfunction(ConcurrentHashMap <String,String> tbchkActive,int nbofTotalswc,BufferedWriter fileLog) 
	{
        this.tbchkActive = tbchkActive;
        this.nbofTotalswc = nbofTotalswc;
        this.fileLog = fileLog;
    }
	
    public void run() 
    {
      System.out.println("The thread " + Thread.currentThread().getName() + " is to deal with the table for checking active switches.");
      try { 
          int i;
          for(i=0;i<=nbofTotalswc;i++)
          {

        	  if(tbchkActive.get(String.valueOf(i).trim()).equals("Active"))
        	  {
        		  tbchkActive.put(String.valueOf(i).trim(), "Checking Active");
        	  }
        	  
     
          }
          
          int count1 = 0;
          int count2 = 0;
          for(count2=1;count2<=nbofTotalswc;count2++)
          {
          	
          	if(tbchkActive.get(String.valueOf(count2).trim()).equals("Inactive"))
          	{
      			count1++;
          	}
          	if(count1 == nbofTotalswc)
          	{
          		System.out.println("All of the switches are inactived. The logging file is updating those informations.");
          		fileLog.close();
          		System.exit(0);
          	}
          }
          
     		this.cancel();
     		Timer clock;
    		clock = new Timer("Timer");
    		clock.schedule(new Timerfunction(tbchkActive,nbofTotalswc,fileLog), 4000);
      } catch (Exception e) 
      {
          System.err.println("Errors.");
      }
    }
}
