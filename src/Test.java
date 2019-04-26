import it.unisa.dia.gas.jpbc.*;


import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Test {
	int maxCount=0;
	Element Aj,Bj;
	public boolean state=false;
	public int lineNumber;  //如果检测成功，表示交易的编号
	public static final int processors=Runtime.getRuntime().availableProcessors();
	public Test(Trapdoor tp,int num) throws InterruptedException, ExecutionException {
		try {
			test(tp,num);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//num表示在一个交易中公钥排在第num的人
	public void test(Trapdoor tp,int numJ) throws FileNotFoundException, InterruptedException, ExecutionException {
		File file=new File(MPECK.eKeywordPathName);
		if(!file.exists())
		{
			System.out.println("Test: eKeywordPathName not exists!");
			System.exit(0);
		}
		Scanner input=new Scanner(file);
		ExecutorService executor=Executors.newFixedThreadPool(processors);	
		ArrayList<Future<Boolean>> results = new ArrayList<>();
		String str;
		int count=0;
		Future<Boolean> done = null;
		while(input.hasNextLine())
		{	
			for(int i=0;i<processors;i++) {	
				str=input.nextLine();
				results.add(executor.submit(new ReadLine(tp,str,numJ,count++)));
				//System.out.println("Thread"+count+" "+System.currentTimeMillis());
				if(!input.hasNextLine())
					break;	
			}
			while (null==done) {
/////////////////////////////////////////////////////////////////////////////////
				Thread.sleep(25);
///////////////////////////////////////////////////////////////////////////////////
				//System.out.println("Future "+count+" "+System.currentTimeMillis());
				int i;
				Future<Boolean> f;
				for (i=0;i<results.size();i++)
				{
					f=results.get(i);
					if(f.get())
					{
						done=f;				 
						System.out.println("Test: Test success! Index= "+lineNumber);
						break;
					}	
				} 
				if((null==done)&&(maxCount==ParamsGenerator.systemTxNumber)) {	
					System.out.println("Test: Test failed");
					break;
				}
				if(i==results.size())
					break;	
			}	
			if(done!=null)
				break;
		}
		executor.shutdownNow();	
		input.close();		 
	}
	
	class ReadLine implements Callable<Boolean>{
		Trapdoor tp;
		String str;
		int numJ;
		int count;
		public ReadLine(Trapdoor tp,String str,int numJ,int count)
		{
			this.tp=tp;
			this.str=str;
			this.numJ=numJ;
			this.count=count;
		}
		
		@Override
		public Boolean call() throws Exception {
			
			if(count>maxCount)
				maxCount=count;
			String[] data=str.split(" ");
			Element A=ParamsGenerator.G1.newElement();		
			A.setFromBytes(ParamsGenerator.returnG(data[0]));
			Element[] C=new Element[ParamsGenerator.keywordNumber];
			for(int i=0;i<C.length;i++)
			{
				C[i]=ParamsGenerator.G1.newElement();
				C[i].setFromBytes(ParamsGenerator.returnG(data[i+1]));
			}
			Element[] B=new Element[ParamsGenerator.GeneratorNum];
			for(int k=0;k<ParamsGenerator.GeneratorNum;k++)
			{
				B[k]=ParamsGenerator.G1.newElement();
				B[k].setFromBytes(ParamsGenerator.returnG(data[k+1+ParamsGenerator.keywordNumber]));
			}	
			Element D=C[tp.index[0]];
			for(int i=1 ;i<tp.index.length; i++) {
				D=D.mul(C[tp.index[i]]);
			}
			
			Element E1=ParamsGenerator.pairing.pairing(tp.T1,D );		
			Element G3=ParamsGenerator.pairing.pairing(A,tp.T2 );
			Element G2=ParamsGenerator.pairing.pairing(B[numJ],tp.T3);
			Element E2=G3.mul(G2);
			if(E1.isEqual(E2)) {	
				lineNumber=count;
				Aj=A;
				Bj=B[numJ];				
				return true;		
			}
			else {
				return false;			
			}
		}	
	}
	
	
	
	
//	class ReadLineThread extends Thread{
//		
//		public ReadLineThread(Trapdoor tp,String str,int numJ,int count)
//		{
//			
//			String[] data=str.split(" ");
//			Element A=ParamsGenerator.G1.newElement();
//			A.setFromBytes(MainTest.returnG(data[0]));
//			Element[] C=new Element[ParamsGenerator.keywordNumber];
//			for(int i=0;i<C.length;i++)
//			{
//				C[i]=ParamsGenerator.G1.newElement();
//				C[i].setFromBytes(MainTest.returnG(data[i+1]));
//			}
//			Element[] B=new Element[ParamsGenerator.GeneratorNum];
//			for(int k=0;k<ParamsGenerator.GeneratorNum;k++)
//			{
//				B[k]=ParamsGenerator.G1.newElement();
//				B[k].setFromBytes(MainTest.returnG(data[k+1+ParamsGenerator.keywordNumber]));
//			}	
//			Element D=C[tp.index[0]];
//			for(int i=1 ;i<tp.index.length; i++) {
//				D=D.mul(C[tp.index[i]]);
//			}
//			Element E1=ParamsGenerator.pairing.pairing(tp.T1,D );	
//			Element G3=ParamsGenerator.pairing.pairing(A,tp.T2 );
//			Element G2=ParamsGenerator.pairing.pairing(B[numJ],tp.T3);
//			Element E2=G3.mul(G2);
//			if(E1.isEqual(E2)) {	
//				lineNumber=count;
//				Aj=A;
//				Bj=B[numJ];
//				state=true;
//			}
//		}
//	}
	

	
	/*
	public boolean testLine(Trapdoor tp,String str) 	//str表示关键词密文某一行数据
	{
		String[] data=str.split("\\s+");
		Element A=ParamsGenerator.G1.newElement();
		A.setFromBytes(data[0].getBytes());
		Element[] C=new Element[ParamsGenerator.keywordNumber];
		
		for(int j=0;j<ParamsGenerator.keywordNumber;j++)
		{
			C[j].setFromBytes(data[j+1].getBytes());
		}
		Element[] B=new Element[ParamsGenerator.GeneratorNum];
		for(int k=0;k<ParamsGenerator.GeneratorNum;k++)
		{
			B[k].setFromBytes(data[k+1+ParamsGenerator.keywordNumber].getBytes());		
		}	
		Element D=C[tp.index[0]].duplicate();
		for(int i=1 ;i<tp.index.length; i++) {
			D=D.mul(C[tp.index[i]]);
		}
		Element F1=ParamsGenerator.pairing.pairing(tp.T1,D );	
		Element F2=ParamsGenerator.pairing.pairing(A,tp.T2 );
		if(F1.isEqual(F2)) {
			E1=F1;
			E2=F2;
			return true;
		}
		else
			return false;
	}
	*/	
		
}

		
	
	
	


