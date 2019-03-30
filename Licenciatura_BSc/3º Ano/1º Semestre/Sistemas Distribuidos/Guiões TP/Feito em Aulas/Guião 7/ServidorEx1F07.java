import java.io.*;
import java.net.*;

class Somas
{
	private int soma;

	public Somas()
	{
		soma = 0;
	}

	public synchronized int getSoma(int num)
	{
		soma += num;
		return soma;
	}
}


public class ServidorEx1F07 implements Runnable
{
	private Socket cs;
	private Somas soma;

	public ServidorEx1F07(Socket cs, Somas soma)
	{
		this.cs = cs;
		this.soma = soma;
	}


	public void run()
	{

		try{
			PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

			String current;
			int num;
			int total = 0;
			int i = 0;
			while((current = in.readLine()) != null)
			{
				//assert current != null;
				num = Integer.parseInt(current);
				total += num;
				i++;
				out.println(soma.getSoma(num));
			}

			out.println("average: " + (total/i));

			in.close();
			out.close();
			cs.close();
		} 
		catch (IOException e) { System.out.println(e.getMessage()); }
	}


	public static void main(String args[]) throws IOException
	{
		
		ServerSocket ss = new ServerSocket(9999);

		Socket cs;

		Thread t;

		Somas soma = new Somas();

		ServidorEx1F07 s;
		
		while((cs = ss.accept()) != null) 
		{
			s = new ServidorEx1F07(cs,soma);
			t = new Thread(s);
			t.start();
		}
		ss.close();		
	}
}