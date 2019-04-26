import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

//需要检索的关键词集合
//索引和keywordset不能放一起，因为索引可能每次不一定一样，可能需要多次实例化；
public class IndexData {
	protected  int index[];
	protected  String keyword[];
	
	void setIndex(int... a)
	{	
		if(a.length>ParamsGenerator.keywordNumber) {
			System.out.print("IndexData:Input keyword of Trapdoor is larger than keyword size!");
			System.exit(0);
		}
		if(!checkIsRepeat(a))
			System.exit(1);
		index=new int[a.length];
		keyword=new String[a.length];
		for(int i=0;i<a.length;i++)
		{
			if(a[i]>ParamsGenerator.keywordNumber||a[i]<0) {
				System.out.println("IndexData:input invalid index");
				System.exit(2);
			}
			index[i]=a[i];
		}
	}
	void setKeyword(String... str) {
		if(str.length!=index.length) {
			System.out.println("IndexData:input invalid string length!");
			System.exit(3);
		}
		for(int i=0;i<index.length;i++)
		{
			keyword[i]=str[i];
		}
	}	
	
	//TxNum表示交易编号，BjNum表示一个交易中的第几个公钥
	
	public String[] getKeyword(int TxNum) throws Exception
	{
		ArrayList<String> keywordArray=KeywordSet.keywordInput(TxNum);
		String[] str=new String[index.length];
		for(int i=0;i<index.length;i++)
		{
			str[i]=keywordArray.get(index[i]);
		}
		return str;
	}
	
	//这是其中一种方法
	public String[] getKeyword1(int TxNum) throws FileNotFoundException
	{
		if(TxNum>ParamsGenerator.systemTxNumber-1)
			System.out.println("IndexData: invalid TxNum!");
			System.exit(0);
	
		File file=new File(KeywordSet.keywordPathName);
		if(!file.exists()) {
			System.out.println("IndexData: keyword FIle not exists!");
			System.exit(1);
		}
		Scanner input=new Scanner(file);
		for(int i=0;i<TxNum;i++)
		{
			input.nextLine();
		}
		String[] tmp=new String[index.length];
		for(int i=0;i<index[0];i++)
		{
			input.next();
		}
		tmp[0]=input.next();	
		for(int i=0;i<index.length-1;i++)
		{
			for(int j=1;j<(index[i+1]-index[i]);j++)
			{
				input.next();
			}
			tmp[i+1]=input.next();			
		}
		input.close();
		return tmp;
	}
	
 	public static boolean checkIsRepeat(int[] array) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            hashSet.add(array[i]);
        }
        if (hashSet.size() == array.length) {
            return true;
        } else {
            return false;
        }
    }
	
}
