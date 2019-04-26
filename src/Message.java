import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import it.unisa.dia.gas.jpbc.Element;


public class Message {
	public static int messageNumber=1;//生成的明文数量
	protected String TxPathName;//包括路径和文件名
	protected String CipherPathName;
	protected Element r1,r2;
	
	//fileSize表示xMB
	//注意Linux和Windows在目录路径上斜杠的差别！！！！！！！！！！！！！！！！
	//预先统一表示为.txt文件！！！！！！！！
	//index指明是针对哪个交易的r1和r2，从keypair获取；
	public Message(String targetPath,String sourcePath,int index,int fileSize) throws IOException, NoSuchAlgorithmException {
		if(index>ParamsGenerator.systemTxNumber)
		{
			System.out.println("Message:Message invalid idex!");
			System.exit(5);
		}
			
		File file=new File(MPECK.keyPairPathName);
		if(!file.exists())
		{
			System.out.println("Message:Message keyPairPathName not exists!");
			System.exit(4);
		}
		
		Scanner input=new Scanner(file);
//		int q=0;
//		while(input.hasNextLine())
//		{
//			q++;
//			System.out.println(q);
//		}
//		System.out.println(q);
		
		for(int i=0;i<3*(index+1)-1;i++)
		{
			
			input.nextLine();
		}
		r1=ParamsGenerator.Zp.newElement();
		r2=ParamsGenerator.Zp.newElement();
		String str1=input.next();
		String str2=input.next();
		r1.set(new BigInteger(str1));
		r2.set(new BigInteger(str2));
		
		input.close();
		
		TxCreate(sourcePath,fileSize);
	
		
		CipherCreate(targetPath,TxPathName);
		
	}
	
	void TxCreate(String filePath,int fileSize)throws IOException 
	{
		File file=new File(filePath+"\\"+Integer.toString(messageNumber)+"-plaintext.txt");//////////////////  \\
		if(file.exists())
		{
			System.out.println("Message:TxCreate File already exists!");
			System.exit(1);
		}
		FileOutputStream fos = new FileOutputStream(file);
		int fileSizeByte=fileSize*1024*1024;
		fos.write(KeywordSet.getRandomString(fileSizeByte).getBytes());	
		TxPathName=filePath+"\\"+Integer.toString(messageNumber)+"-plaintext.txt";
		fos.close();
	}
	
	void CipherCreate(String targetPath,String sourcePath) throws IOException, NoSuchAlgorithmException
	{
		File sourceFile=new File(sourcePath);
		if(!sourceFile.exists())
		{
			System.out.println("Message: CipherCreate sourceFile does not exist!");
			System.exit(2);
		}
		FileInputStream input = new FileInputStream(sourceFile);
		CipherPathName=targetPath+"\\"+Integer.toString(messageNumber)+"-ciphertext.txt";
		File targetFile=new File(CipherPathName);
		if(targetFile.exists())
		{
			System.out.println("Message: CipherCreate targetFile already exist!");
			System.exit(3);
		}
		FileOutputStream fos = new FileOutputStream(targetFile);
		//PrintWriter output=new PrintWriter(targetFile);
	
		
		
		Element e_g_g=ParamsGenerator.pairing.pairing(ParamsGenerator.g, ParamsGenerator.g).getImmutable();
		Element r3=r1.duplicate();
		Element e_r_s=r3.mul(r2).getImmutable();
		Element e_g_g1=e_g_g.powZn(e_r_s).getImmutable();	
		byte[] e_g_g2=MessageDigest.getInstance("SHA-256").digest(e_g_g1.toString().getBytes()); 
		int data;
		ArrayList<Byte> arr=new ArrayList<Byte>();
		

		while((data=input.read())!=-1)	
		{
			arr.add((byte)data);
		}

		byte[] cm=new byte[arr.size()];
		for(int i=0;i<arr.size();i++)
		{
			cm[i]=arr.get(i);		
		}
		byte[] temp=XORoperation(cm,e_g_g2);
		
		

		
		fos.write(temp);
		//output.println(new String(temp));
			
		//System.out.println(str.length());/////////////////为什么大小不一致
		
		messageNumber++;	
		input.close();
		//output.close();
		fos.close();
	}

	public static byte[] XORoperation(byte[] origin1,byte[] origin2) {
		byte[] result;
		if(origin1.length>origin2.length)
		{
			result=new byte[origin1.length];
			for(int i=0;i<origin1.length;i++)
			{
				result[i]=(byte)((byte)origin1[i]^(byte)origin2[i%origin2.length]);
				//System.out.println(result[i]+" "+origin1[i]+" "+origin2[i%origin2.length] );
			}	
		}
		else
		{
			result=new byte[origin2.length];
			for(int i=0;i<origin2.length;i++)
			{
				result[i]=(byte)((byte)origin2[i]^(byte)origin1[i%origin1.length]);
			//	System.out.println(result[i]+" "+origin1[i]+" "+origin2[i%origin2.length] );
			}	
		}
		return result;
		
	}
}
