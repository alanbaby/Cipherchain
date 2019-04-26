import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import it.unisa.dia.gas.jpbc.*;
import java.math.BigInteger;

//包括关键词集合文件的生成
//每创建一个关键词集合，需要相应的交易检索者
public class KeywordSet {
	protected Element pub_key[];
	protected Element pri_key[];
	protected static String keywordPathName;  //关键词集合路径
	public static final int keywordMinSize=4;
	public static final int keywordMaxSize=20;
	//protected String keywordSet[];
	//创建关键词集合的构造函数
	public KeywordSet(String filePath) throws IOException
	{
		createKeywordSet(filePath);
	}
	public KeywordSet() {
		keyGenerator();
	}
	protected void keyGenerator()  {
		pri_key=new Element[ParamsGenerator.GeneratorNum];
		pub_key=new Element[ParamsGenerator.GeneratorNum];
		for(int i=0;i<ParamsGenerator.GeneratorNum;i++) {
			Element x=ParamsGenerator.Zp.newRandomElement();
			pri_key[i]=x;
			pub_key[i]=ParamsGenerator.g.powZn(x);			
		}
	}

	
	private void createKeywordSet(String filePath) 
			throws IOException
	{
		File file=new File(filePath+"\\"+"Keyword.txt");
		if(file.exists()) {
			System.out.println("keyGenerator:File already exists.");
			System.exit(1);
		}
		PrintWriter output=new PrintWriter(file);
		keywordPathName=filePath+"\\"+"Keyword.txt";	
		for(int i=0;i<ParamsGenerator.systemTxNumber;i++)
		{
			for(int j=0;j<ParamsGenerator.keywordNumber;j++)
			{
				output.print(getRandomString(keywordMinSize,keywordMaxSize)+" ");
			}
			output.println();
		}	
		output.close();
	}
	//输入交易编号
	public static ArrayList<String> keywordInput(int num)throws Exception
	{
		if(num>ParamsGenerator.systemTxNumber||num<0)
		{
			System.out.println("KeywordSet:keywordInput input num as invalid");
			System.exit(2);
		}
		
		File fin=new File(keywordPathName);
		Scanner input=new Scanner(fin);
		if(!fin.exists())
		{
			System.out.println("KeywordSet:keywordInput file does not exists");
			System.exit(3);
		}
		ArrayList<String> lst=new ArrayList<String>();
		for(int i=0;i<num;i++)
			input.nextLine();
		
		for(int i=0;i<ParamsGenerator.keywordNumber;i++)
			lst.add(input.next());	
			
		input.close();
		return lst;
	}
	
	protected  static Element getPrvKey(int TxNum,int numJ) throws FileNotFoundException
	{
		Element num=ParamsGenerator.Zp.newElement();
		
		File file=new File(MPECK.keyPairPathName);
		if(!file.exists())
		{
			System.out.println("IndexData: keyPairPathName not exists!");
		}
		Scanner input=new Scanner(file);
		for(int i=0;i<3*(TxNum+1)-2;i++)
			input.nextLine();	
		for(int i=0;i<numJ;i++)
		{
			input.next();
		}
		String str=input.next();
		num.set(new BigInteger(str));
		input.close();
		return num;
	}
	
	public static String getRandomString(int min,int max) {
		int tmp=new Random().nextInt(max-min)+min;
		return getRandomString(tmp);	
	}
	
	
	public static String getRandomString(int length) {
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i++) {
	        int number = random.nextInt(3);
	        long result = 0;
	        switch (number) {
	            case 0:
	                result = Math.round(Math.random() * 25 + 65);
	                sb.append(String.valueOf((char) result));
	                break;
	            case 1:
	                result = Math.round(Math.random() * 25 + 97);
	                sb.append(String.valueOf((char) result));
	                break;
	            case 2:
	                sb.append(String.valueOf(new Random().nextInt(10)));
	                break;
	        }
	    }
	    return sb.toString();
	}
	
	
//	public static void createKeywordSet(String filePath) 
//			throws IOException
//	{
//		
//		for(int i=0;i<ParamsGenerator.systemTxNumber;i++)
//		{
//			File file=new File(filePath+"\\"+Integer.toString(i+1)+".txt");
//			if(file.exists()) {
//				System.out.println("keyGenerator:File already exists.");
//				System.exit(1);
//			}
//			keywordPath=filePath;	
//			PrintWriter output=new PrintWriter(file);
//			for(int j=0;j<ParamsGenerator.keywordNumber;j++)
//			{
//				output.println(getRandomString(keywordMinSize,keywordMaxSize));
//			}
//	
//			output.close();
//		}	
//	}
	
	//测试的文本关键词输入到keyword[];
//	public void keywordInput(String pathName)throws Exception
//	{
//		File fin=new File(pathName);
//		Scanner input=new Scanner(fin);
//		if(!fin.exists())
//		{
//			System.out.println("KeywordSet file does not exists");
//			System.exit(0);
//		}
//		ArrayList<String> lst=new ArrayList<String>();
//		while(input.hasNext())
//		{		
//			lst.add(input.next());	
//		}
//		for(int  i=0;i<lst.size();i++)
//		{
//			keywordSet[i]=lst.get(i);
//		}
//		input.close();
//		  
//	}
	
	//获取指定范围大小字节内的随机字符串；	 	
	

}
