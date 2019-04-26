
import java.io.File;
import java.io.FileInputStream;


import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
//import java.io.File;


import it.unisa.dia.gas.jpbc.*;
public class MDEC {
	public MDEC(Element prvKey,Message msg,Test test) throws NoSuchAlgorithmException, IOException
	{
		if(isData(prvKey,msg,test))
			System.out.println("Ciphertext decrypts successfully");
		else
			System.out.println("Ciphertext decrypts failed");
			
	}

	
	public byte[] mDec(Element prvKey,Test test) throws NoSuchAlgorithmException
	{
		Element e_A_Bj=ParamsGenerator.pairing.pairing(test.Aj,test.Bj);
		Element eResult1=e_A_Bj.powZn(prvKey.invert());
		byte[] eResult=MessageDigest.getInstance("SHA-256").digest(eResult1.toString().getBytes());	
		return eResult;
	}
	
//	public boolean isData1(Element prvKey,Message msg,Test test) throws NoSuchAlgorithmException {
//		Element r1=ParamsGenerator.Zp.newElement();
//		Element r2=ParamsGenerator.Zp.newElement();			
//		r1=msg.r1;
//		r2=msg.r2;
//
//		System.out.println(test.Bj);
//		Element e_g_g=ParamsGenerator.pairing.pairing(ParamsGenerator.g,ParamsGenerator.g);
//		Element e_r_s=r1.mul(r2);
//		Element eMsg=e_g_g.powZn(e_r_s).getImmutable();
//		Element aj=test.Aj.getImmutable();
//		Element bj=test.Bj.getImmutable();
//		Element e_A_Bj=ParamsGenerator.pairing.pairing(aj,bj).getImmutable();
//		Element q=prvKey.invert();
//		Element eResult=e_A_Bj.powZn(q).getImmutable();
//		//byte[] aa=MessageDigest.getInstance("SHA-256").digest(eResult.toString().getBytes());	
//		//byte[] bb=MessageDigest.getInstance("SHA-256").digest(eMsg.toString().getBytes());	
//		//System.out.println(eResult);
//		//System.out.println(eMsg);
//		
//		//int i;
//		
////		byte[] cc=eResult.toString().getBytes();
////		byte[] dd=eMsg.toString().getBytes();
//
//		
////		for( i=0;i<cc.length;i++)
////		{
////			System.out.print(cc[i]);	
////		}
////		System.out.println("_____________");
////		for( i=0;i<dd.length;i++)
////		{
////			System.out.print(dd[i]);
////		}
//		System.out.println("_____________");
////		for( i=0;i<aa.length;i++)
////		{
////			
////			if(aa[i]!=bb[i]) {
////				System.out.println("MDec:Message TxPathName file not exists");
////				return false;
////			}		
////		}
////		if(i==aa.length) {
////			System.out.println("Success:Get the target result!");
////			return true;
////		}
//			
//		//return true;
//		if(eMsg.equals(eResult)) {
//			System.out.println("Success:Get the target result!");
//			return true;
//		}
//		else
//		{
//			System.out.println("MDec:Message TxPathName file not exists!");
//			return false;
//		}
//		
//	}

	public boolean isData(Element prvKey,Message msg,Test test) throws NoSuchAlgorithmException, IOException
	{
	
	
		byte[] eResult=mDec(prvKey,test);

		//File file=new File(msg.CipherPathName);
//		if(!file.exists())
//		{
//			System.out.println("MDec:Message CipherPathName file not exists!");
//			System.exit(0);
//		}
		
		//Scanner input=new Scanner(file);
		//String eMessage=input.next();
		//int count=0;
		File filePath=new File(msg.CipherPathName);
		FileInputStream in = new FileInputStream(filePath);
		ArrayList<Byte> arr=new ArrayList<Byte>();
		int data;
		while((data=in.read())!=-1)	
		{
			arr.add((byte)data);
		}
		in.close();
	
		byte[] ct=new byte[arr.size()];
		for(int i=0;i<arr.size();i++)
		{
			ct[i]=arr.get(i);
		}
		
		byte[] result=Message.XORoperation(eResult, ct);
	
		File file1=new File(msg.TxPathName);
		if(!file1.exists())
		{
			System.out.println("MDec:Message TxPathName file not exists!");
			System.exit(1);
		}
		FileInputStream input=new FileInputStream(file1);	
		arr.clear();
		while((data=input.read())!=-1)	
		{
			arr.add((byte)data);
		}
		input.close();
	
		byte[]mResult=new byte[arr.size()];
		for(int i=0;i<arr.size();i++)
		{
			mResult[i]=arr.get(i);
		}
		if(mResult.length!=result.length) {
			System.out.println("MDec:The lengths of the mResult and result are different");
			System.exit(2);
		}	
			
		int i;
		for( i=0;i<mResult.length;i++)
		{
			if(mResult[i]!=result[i])
			{
				System.out.println("MDec:The contents of the mResult and result are different");
				in.close();
				return false;
			}
		}
		if(i==mResult.length) {
			System.out.println("Success:Get the target result!");
			in.close();
		
			
			return true;
		}
		else {	
			return false;
		}
	}
	
}
