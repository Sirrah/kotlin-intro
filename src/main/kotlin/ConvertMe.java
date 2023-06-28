import java.util.*;

public class ConvertMe
{
	private String name;

	private int age;

	public ConvertMe( String name, int age )
	{
		this.name = name;
		this.age = age;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge( int age )
	{
		this.age = age;
	}

	private static final List<String> SUGGESTED_NAMES = Arrays.asList( "Jan", "Piet");

	public static List<String> getSuggestedNames()
	{
		return SUGGESTED_NAMES;
	}
}
