
import it.unisa.dia.gas.jpbc.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;

public class MPECK{
	public static String eKeywordPathName; 
	public static String keyPairPathName;
	public MPECK(String filePath,String keyPath) throws NoSuchAlgorithmException
	{
		try {
			init(filePath,keyPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//filePath是关键词密文的文件，keyPath是公私钥对的文件
	void init(String filePath,String keyPath) throws Exception {	
		File file=new File(filePath+"\\"+"KeywordCiphertext.txt");	
		if(file.exists())
		{
			System.out.println("MPECK: filePath already exists!");
			System.exit(0);
		}
		eKeywordPathName=filePath+"\\"+"KeywordCiphertext.txt";
		PrintWriter output=new PrintWriter(file);
		
		File file1=new File(keyPath+"\\"+"KeyPairSet.txt");
		if(file1.exists())
		{
			System.out.println("MPECK: keyPath already exists!");
			System.exit(1);
		}
		keyPairPathName=keyPath+"\\"+"KeyPairSet.txt";
		PrintWriter out=new PrintWriter(file1);
		
		
		for(int i=0;i<ParamsGenerator.systemTxNumber;i++) {
			KeywordSet tmp=new KeywordSet();//默认参与交易方数目
			Element r1=ParamsGenerator.Zp.newRandomElement();
			Element r2=ParamsGenerator.Zp.newRandomElement();
			for(int u=0;u<tmp.pub_key.length;u++)
			{
				out.print(tmp.pub_key[u]+" ");
			}
			out.println();
			for(int u=0;u<tmp.pub_key.length;u++)
			{
				out.print(tmp.pri_key[u]+" ");
			}
			out.println();
			out.print(r1+" "+r2);
			out.println();
			
			Element A=ParamsGenerator.g.powZn(r1);
			
			output.print(A+" ");

			Element[] hashH=new Element[ParamsGenerator.keywordNumber];
			Element[] hashF=new Element[ParamsGenerator.keywordNumber];
			for(int j=0;j<ParamsGenerator.keywordNumber;j++)
			{
				ArrayList<String> keywordSet=KeywordSet.keywordInput(i);
				byte[] result1=MessageDigest.getInstance("SHA-1").digest(keywordSet.get(j).getBytes());  //SHA1
				hashH[j]=ParamsGenerator.pairing.getG1().newElement().setFromHash(result1,0,result1.length);
				byte[] result2=MessageDigest.getInstance("SHA-256").digest(keywordSet.get(j).getBytes());  //SHA256
				hashF[j]=ParamsGenerator.pairing.getG1().newElement().setFromHash(result2,0,result2.length);
			}
			Element[] C=new Element[ParamsGenerator.keywordNumber];
			for(int j=0;j<ParamsGenerator.keywordNumber;j++)
			{
				Element tmp1=hashH[j].powZn(r1);
				Element tmp2=hashF[j].powZn(r2);
				C[j]=tmp1.mul(tmp2);
				output.print(C[j]+" ");
			}	
			
			Element[] B=new Element[tmp.pub_key.length];
			for(int j=0;j<tmp.pub_key.length;j++)
			{
				B[j]=tmp.pub_key[j].powZn(r2);
				output.print(B[j]+" ");
			}
		
			output.println();		                                                                                                                                                                                                                                                                                                                                                                                            
		}
			

		output.close();
		out.close();	
	}

}
