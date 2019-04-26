import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import it.unisa.dia.gas.jpbc.*;

public class Trapdoor {
	protected  Element T1,T2,T3;
	protected  int index[];
	
	public Trapdoor(Element prvKey,IndexData idx) {		
		try {
			trapdoor(prvKey,idx);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	protected void trapdoor(Element prvKey,IndexData idx) throws NoSuchAlgorithmException
	//输入检索的m个关键词，num表示各关键词位置
	{
		
		Element t=ParamsGenerator.Zp.newRandomElement().getImmutable();
		T1=ParamsGenerator.g.powZn(t);
		
		
		//随机选取几个索引编号
		//idx=indexQ(KeywordSet.getKeywordSize(),KeywordSet.getIndexLen());
	
		byte[] result1 = MessageDigest.getInstance("SHA-1").digest(idx.keyword[0].getBytes());
		Element hashT2=ParamsGenerator.pairing.getG1().newElement().setFromHash(result1,0,result1.length).getImmutable(); 
		byte[] result2 = MessageDigest.getInstance("SHA-256").digest(idx.keyword[0].getBytes());
		Element hashT3=ParamsGenerator.pairing.getG1().newElement().setFromHash(result2,0,result2.length).getImmutable(); 	
		for(int i=1;i<idx.index.length;i++)
		{	
			result1=MessageDigest.getInstance("SHA-1").digest(idx.keyword[i].getBytes());
			hashT2=hashT2.mul(ParamsGenerator.pairing.getG1().newElement().setFromHash(result1,0,result1.length)).getImmutable();
			result2=MessageDigest.getInstance("SHA-256").digest(idx.keyword[i].getBytes());
			hashT3=hashT3.mul(ParamsGenerator.pairing.getG1().newElement().setFromHash(result2,0,result2.length)).getImmutable();
		}
		T2=hashT2.powZn(t);
		
		
		
		
		T3=hashT3.powZn(t.div(prvKey));	

		index=new int[idx.index.length];
		
		for(int i=0;i<idx.index.length;i++)
		{
			index[i]=idx.index[i];
		}
	
	}
}