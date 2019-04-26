//import java.util.*;
import java.math.BigInteger;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory; 
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

public class ParamsGenerator {
	protected static Pairing pairing;
	protected static Field<Element> G1,G2,Zp;
	public static Element g;
	public static final int GeneratorNum=8;//强制设定每个交易的参与者数量
	public static final int keywordNumber=4; //一个关键词集合中的关键词数量是固定的
	public static final int systemTxNumber=20;//系统的交易总数量
	//如何产生生成元g
	//产生n个交易参与方的公私钥对
	public ParamsGenerator()
	{
		init();
	}
		
	@SuppressWarnings("unchecked")
	public void init()
	{
		TypeACurveGenerator pg = new TypeACurveGenerator(170, 510);
		PairingParameters typeAParams = pg.generate();
		pairing = PairingFactory.getPairing(typeAParams);
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		checkSymmetric(pairing);
		
		G1 = pairing.getG1();
		G2 = pairing.getG2();
		Zp = pairing.getZr();
		//G_1 = pairing.getG1().newRandomElement().getImmutable();
		//Z_p = pairing.getZr().newRandomElement().getImmutable();
		g=pairing.getG1().newRandomElement().getImmutable();////乘法循环群的生成元怎么形成；	
		//由于椭圆曲线是加法群，所以G群中任意一个元素都可以作为生成元
	}
		
	private void checkSymmetric(Pairing pairing)
	{
		if (!pairing.isSymmetric()) {
			throw new RuntimeException("Key asymmetry!");
		}
	}
	public static byte[] returnG(String str) {	
		String[] str1=str.split(",");
		BigInteger n1=new BigInteger(str1[0]);
		BigInteger n2=new BigInteger(str1[1]);
		
		byte[] b1=n1.toByteArray();
		byte[] b2=n2.toByteArray();
		if(b1.length==63)
		{
			byte[] tmp=new byte[64];
			tmp[0]=0;
			for(int i=0;i<b1.length;i++)
			{
				tmp[i+1]=b1[i];
			}
			b1=tmp;
		}
		if(b2.length==63)
		{
			byte[] tmp=new byte[64];
			tmp[0]=0;
			for(int i=0;i<b2.length;i++)
			{
				tmp[i+1]=b2[i];
			}
			b2=tmp;
		}
		if(b1.length+b2.length==129)
		{
			if(b1.length>b2.length)
			{
				
				if(b1[0]==0)
				{
					byte[] tmp=new byte[64];
					for(int i=0;i<tmp.length;i++)
					{
						tmp[i]=b1[i+1];
					}
					b1=tmp;
				}
				else
				{
					byte[] tmp=new byte[65];
					tmp[0]=0;
					for(int i=0;i<b2.length;i++)
					{
						tmp[i+1]=b2[i];
					}
					b2=tmp;
				}
			}
			else {
				if(b2[0]==0)
				{
					byte[] tmp=new byte[64];
					for(int i=0;i<tmp.length;i++)
					{
						tmp[i]=b2[i+1];
					}
					b2=tmp;
				}
				else
				{
					byte[] tmp=new byte[65];
					tmp[0]=0;
					for(int i=0;i<b1.length;i++)
					{
						tmp[i+1]=b1[i];
					}
					b1=tmp;
				}	
			}
		}
		if(b1.length+b2.length==130)
		{
			if(b1[0]==0 && b2[0]==0)
			{
				byte[] tmp1=new byte[64];
				byte[] tmp2=new byte[64];
				for(int i=0;i<64;i++)
				{
					tmp1[i]=b1[i+1];
					tmp2[i]=b2[i+1];
				}
				b1=tmp1;
				b2=tmp2;
			}
				
		}
	
		byte[] b3 = new byte[b1.length+b2.length]; 
		int i=0;
	    for(byte bt: b1){
	     b3[i]=bt;
	     i++;
	    }
	    for(byte bt: b2){
	    	b3[i]=bt;
	    	i++;
	    }
	    return b3; 
	}
}
