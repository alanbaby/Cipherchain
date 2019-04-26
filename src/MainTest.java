


import it.unisa.dia.gas.jpbc.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainTest {
	//设立寻找的目标交易编号
	public static final int TxIndex=19;
	//指明在该目标交易中的公钥次序；
	public static final int TxBj=4;
	//设立搜寻的关键词子集
	public static final int[] SubKeyword= {0,1,2,3};
	//public static String  filePath="E:\\Test";
	public static String  filePath="C:\\Users\\HilinChen\\Desktop\\Test";
//public static byte[][] extractMessage(String msg){
//	
//	List<String> list=new ArrayList<String>();
//	Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
//	Matcher m = p.matcher(msg);
//	while(m.find()){
//		list.add(m.group().substring(1, m.group().length()-1));
//	}
//	int num1=list.size();
//	int num2=list.get(0).split(", ").length;
//	byte[][] bt=new byte[num1][num2];
//	for(int i=0;i<num1;i++)
//	{	
//		String[] str=list.get(i).split(", ");
//		for(int j=0;j<num2;j++)
//		{
//			bt[i][j]=(byte)Integer.parseInt(str[j]);
//		}
//	}
//	return bt;
//}
	

	
	public static void main(String[] agrs) throws Exception
	{	
		//long time2=System.currentTimeMillis();
		//System.out.println(time2-time1);
	
		
		new ParamsGenerator();
		
		
		
		try {	
			
			new KeywordSet(filePath);
			
			new MPECK(filePath,filePath);
			Message msg=new Message(filePath,filePath,TxIndex,4);
			IndexData idx=new IndexData();
			idx.setIndex(SubKeyword);
			idx.setKeyword(idx.getKeyword(TxIndex));
			Element prvKey=KeywordSet.getPrvKey(TxIndex,TxBj);		
			
			Trapdoor tp=new Trapdoor(prvKey,idx);	
			
			Test test=new Test(tp,TxBj);
			
			new MDEC(prvKey,msg,test);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
//字节数组转换为element	
//Element c=ParamsGenerator.G1.newElement().setToRandom();
//byte[] d=c.toBytes();
//Element e=ParamsGenerator.G1.newElement().setToRandom();
//e.setFromBytes(d);
//System.out.println(c);
//System.out.println(e);





/////////////////////////////////////
//Element r=ParamsGenerator.Zp.newRandomElement();
//Element s=ParamsGenerator.Zp.newRandomElement();
//Element g=ParamsGenerator.G1.newRandomElement().getImmutable();
//Element x=ParamsGenerator.Zp.newRandomElement();
//Element y=g.powZn(x);
//System.out.println(g);
//Element A=g.powZn(r);
//
//Element B=y.powZn(s);
//
//Element e_A_B=ParamsGenerator.pairing.pairing(A, B);
//Element e_A_B1=e_A_B.powZn(x.invert());
//byte[] Xj=MessageDigest.getInstance("SHA-256").digest(e_A_B1.toString().getBytes());
//Element e_g_g=ParamsGenerator.pairing.pairing(g, g);
//Element rs=r.mul(s).getImmutable();
//Element e_g_g1=e_g_g.powZn(rs).getImmutable();
//
//byte[] T=MessageDigest.getInstance("SHA-256").digest(e_g_g1.toString().getBytes());
//System.out.println(Xj.length+" "+T.length);
//System.out.println(e_A_B1);
//System.out.println(e_g_g1);
//
//
//int i=0;
//for( i=0;i<Xj.length;i++)
//{
//	if(Xj[i]!=T[i]) {
//		System.out.println("failure");
//		System.exit(1);
//	}
//}
//if(i==Xj.length)
//	System.out.println("good");




//ELEMENT A=PARAMSGENERATOR.G1.NEWELEMENT().SETTORANDOM();
//
//BYTE[] ABT=A.TOBYTES();
//
//STRING ASTR=A.TOSTRING();
//BYTE[] AJIA=RETURNG(ASTR);
//SYSTEM.OUT.PRINTLN("——————————A.TOBYTES————————————");
//FOR(INT I=0;I<ABT.LENGTH;I++)
//{
//	SYSTEM.OUT.PRINT(ABT[I]+" ");
//}
//SYSTEM.OUT.PRINTLN();
//SYSTEM.OUT.PRINTLN(AJIA.LENGTH+"  "+ABT.LENGTH);
//SYSTEM.OUT.PRINTLN();
//SYSTEM.OUT.PRINTLN("——————————RETURNG(A.TOSTRING)———————————");
//FOR(INT I=0;I<AJIA.LENGTH;I++)
//{
//	SYSTEM.OUT.PRINT(AJIA[I]+" ");
//}
//SYSTEM.OUT.PRINTLN();
//SYSTEM.OUT.PRINTLN("——————————AB————————————");
//SYSTEM.OUT.PRINTLN(A);
//ELEMENT B=PARAMSGENERATOR.G1.NEWELEMENT().SETTORANDOM();
//B.SETFROMBYTES(AJIA);	
//SYSTEM.OUT.PRINTLN(B);
//if(!A.equals(B))
//{
//	System.out.println("budeng ");
//	System.exit(1);
//}