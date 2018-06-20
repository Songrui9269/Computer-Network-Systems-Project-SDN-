import java.util.List;

public class Vetx 
{
	private Vetx a1;
	private int a2;
	private int a3;
	private List<Vetx> a4;
	
	public void setwidth(int label)
	{
		this.a3 = label;
	}
	public int getLabel() 
	{
		return a3;
	}
	public void setId(int id) 
	{
		this.a2 = id;
	}
	public int getId() 
	{
		return a2;
	}
	public void setPrevious(Vetx previous)
	{
		this.a1 = previous;
	}
	public Vetx getPrevious() 
	{
		return a1;
	}
	public void setPrev(List<Vetx> prev) 
	{
		this.a4 = prev;
	}
	public List<Vetx> getPrev() 
	{
		return a4;
	}
 
}
