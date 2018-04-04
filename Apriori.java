import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.BitSet;



public class Apriori{
	
	//String variables used to store input output, buffered writer for output
	public static String input;//String to store input path
	public static String output;//STring to store output path
	public static BufferedWriter bw_out;//Buffered output
	public static String candiset;//String to store the generated candidate sets path
	
	int itemCount = 1;
	int lineCount = 1;
	
	//Converted integer item and the list of transactions of item occurrence
	Map<Integer,ArrayList<Integer>> itemtranList=new HashMap<Integer, ArrayList<Integer>>();
	 
	
	//Hash map to store the review id s in string format to integer as key value pairs
	HashMap<String,Integer> stoi = new HashMap<String, Integer>();
	
	//Hash map to look up the items
	HashMap<Integer,String> itos = new HashMap<Integer,String>();
	
	//Map to store the 
	Map<Integer,ArrayList<Integer>> tranItemList=new HashMap<Integer, ArrayList<Integer>>();
	
	//Map to store the support of each item
	Map<Integer,Integer> sup_count = new HashMap<Integer, Integer>();
	
	//Map to store frequent patterns after the minimum support
	Map<Integer,ArrayList<ArrayList<Integer>>> FreqPatterns=new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	
	public static boolean flag;
	
	//method to convert the item sets having review id s to integers i.e A->1
	public void convert_reviewid() {
		
		//Read the input file using BufferedReader from path input
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			
		
             //System.out.println("Inside Convert review method");
			//Loop to check for EOF
             //current_pos: String to track the current line in the file
             for(String current_pos; (current_pos = br.readLine()) != null; ) {
				
				//String to store the current line items spitting with space
				String [] split_items = current_pos.split(" ");//Review IDs in input are split by space 
				//for(String event: split_items) {
				//System.out.print(event);
				//}
				ArrayList<Integer> itemList = new ArrayList<Integer>();				
				for(int i=0;i<split_items.length;i++)
				{
				if(stoi.containsKey(split_items[i]))
				{
					
					ArrayList<Integer> tranList2 = new ArrayList<Integer>();
					tranList2 = itemtranList.get(stoi.get(split_items[i]));
					tranList2.add(lineCount);
					itemtranList.put(stoi.get(split_items[i]), tranList2);
				    itemList.add(stoi.get(split_items[i]));
				}
				else{
					ArrayList<Integer> tranList = new ArrayList<Integer>();
					stoi.put(split_items[i], itemCount);
					itemCount++;
				    //System.out.println(itemCount);
					//Array list of items present in each transaction
					itemList.add(stoi.get(split_items[i]));
					tranList.add(lineCount);
					//converting string review id's to integers
					itemtranList.put(stoi.get(split_items[i]),tranList );
				}
				}
				Collections.sort(itemList);
				tranItemList.put(lineCount, itemList);
				lineCount+=1;
				//bw.newLine();
			}
			
			for (Map.Entry<Integer, ArrayList<Integer>> entry : itemtranList.entrySet()) {
			    int key = entry.getKey();
			    sup_count.put(key, entry.getValue().size());
			    	//System.out.println(entry.getValue().size());
			   //System.out.println(sup.get(key));
			   //System.out.println("A BIG GAP");
			     //System.out.println("key, " + key +"values"+entry.getValue());
			}
			for (Map.Entry<String,Integer> entry : stoi.entrySet()){
				itos.put(entry.getValue(), entry.getKey());
			}	
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Frequent One Item Sets
	public void freqOneItems(int minsup,int item_order)
	{
		try{
		if(FreqPatterns.isEmpty())
		{
		ArrayList<ArrayList<Integer>> tem1=new ArrayList<ArrayList<Integer>>();
		for (Map.Entry<Integer, Integer> entry : sup_count.entrySet()) {
			if(entry.getValue()>=minsup)
			{
				ArrayList<Integer> tem=new ArrayList<Integer>();
				tem.add(entry.getKey());
				tem1.add(tem);
				
				if(item_order==1){
					String d=itos.get(entry.getKey());
						//System.out.println(d);
						bw_out.write(d+' ');
						bw_out.write('('+Integer.toString(entry.getValue())+')');
						bw_out.newLine();

				}
			}
		}
		FreqPatterns.put(1, tem1);
		//System.out.println(tem1);
		//bw_out.close();
		}
		}
		catch(Exception e){

		}
	}
	
//generating frequent items of item-set order 2
    public void freqTwoItems(int minsup){
	  
  try{
					int size = 1;
					BufferedWriter candi = null;
					ArrayList<ArrayList<Integer>> itemset2=new ArrayList<ArrayList<Integer>>();
					ArrayList<Integer> itemset2_fin=new ArrayList<Integer>();
					itemset2=FreqPatterns.get(1);
					//System.out.println(itemset2);
					for(ArrayList<Integer> a:itemset2){
						itemset2_fin.addAll(a);
					}
					//System.out.println(itemset2_fin);
					
					candi = new BufferedWriter(new FileWriter(candiset));
					come_here:
						for(int is = 0; is < itemset2_fin.size()-1; is++)
						{
						
						if(size == itemset2_fin.size()){
						//	System.out.println("break");
							break come_here;
						}
						else{
							
						for(int bs=0;bs<itemset2_fin.size()-size;bs++){
								
					    BitSet bset[]=new BitSet[2];
					    bset[0]=new BitSet(tranItemList.size());
					    bset[1]=new BitSet(tranItemList.size());
					    for(int it:itemtranList.get(itemset2_fin.get(is))){
					    	
					    	bset[0].set(it-1);
					    
					    }
					   
					    for(int it1: itemtranList.get(itemset2_fin.get(is+bs+1))){
					    	bset[1].set(it1-1);
					    
					    }
					    
					    bset[0].and(bset[1]);
					    
					    if(bset[0].cardinality()>=minsup){
					    	candi.write(itemset2_fin.get(is)+" "+itemset2_fin.get(is+1+bs));
					    	candi.newLine();
					    }
					}
							size++;
							
					}
					}
					candi.close();
					}
					catch(Exception e){
						
					}
				}	
  
  //generating candidate sets and frequent sets from those candidate sets
  public void support(int itemorder,int msup)
	{


		if(!FreqPatterns.isEmpty())
		{
			int fpsize = FreqPatterns.size();
			ArrayList<ArrayList<Integer>> underFreq=new ArrayList<ArrayList<Integer>>();
			//ArrayList<ArrayList<Integer>> candidate=new ArrayList<ArrayList<Integer>>();
			
			//Array list of frequent patterns generated from candidate sets
			ArrayList<ArrayList<Integer>> fcs=new ArrayList<ArrayList<Integer>>();
			underFreq = FreqPatterns.get(fpsize);
			
			int l=underFreq.size();
			try{
				if(fpsize!=1){
				BufferedWriter candi = null;
				candi = new BufferedWriter(new FileWriter(candiset));
			
			for(int i=0;i<l-1;i++)
			{

				for(int j=i+1;j<l;j++)
				{
					ArrayList<Integer> prevFreqList = new ArrayList<Integer>();
					
					prevFreqList.addAll(underFreq.get(i));
				ArrayList<Integer> prevFreqList1=new ArrayList<Integer>();
				ArrayList<Integer> temCandidate=new ArrayList<Integer>();
				prevFreqList1.addAll(underFreq.get(j));
				int found1 = prevFreqList.get(fpsize-1);
				int found2 = prevFreqList1.get(fpsize-1);
				prevFreqList.remove(fpsize-1);
				prevFreqList1.remove(fpsize-1);
				
				if(prevFreqList.equals(prevFreqList1))
				{
					if(found1>found2){
						temCandidate=prevFreqList;
						temCandidate.add(found2);
						temCandidate.add(found1);
						
					}
					else
					{
						temCandidate=prevFreqList;
						temCandidate.add(found1);
						temCandidate.add(found2);
						
					}
					
					//Pruning Step of Apriori Algorithm
					loop:
					for(int m=0;m<fpsize;m++)
					{
						
						int rt=temCandidate.get(m);
						temCandidate.remove(m);
						
						if(!underFreq.contains(temCandidate))
						{
							temCandidate=null;
							
							break loop;
						}
						temCandidate.add(rt);
						Collections.sort(temCandidate);
						//System.out.println(temCandidate);

					}

					if(temCandidate!=null)
					{

						
					for(int c:temCandidate){

						candi.write(c+" ");
					}

					candi.newLine();

					}

				}
				}

			}
			candi.close();
				}
			}
			catch(Exception e){

			}

			ArrayList<Integer> a=new ArrayList<>();

			try{

				BufferedReader br = new BufferedReader(new FileReader(candiset));
				for(String current_pos; (current_pos = br.readLine()) != null; ) {
				
				String final_length[]=current_pos.split(" ");
				ArrayList<Integer> e= new ArrayList<Integer>();
				for(int fl=0;fl<final_length.length;fl++)
				{
					e.add(Integer.parseInt(final_length[fl]));
				}
				
				//final transaction list for calculating support
					ArrayList<Integer> ftl = new ArrayList<Integer>();
					
				for(int p=0;p<e.size();p++){
					ArrayList<Integer> tc1=new ArrayList<Integer>();
					if(p==0)
					{
						ftl.addAll(itemtranList.get(e.get(p)));
						
					}
					else{
						tc1.addAll(itemtranList.get(e.get(p)));
						ftl.retainAll(tc1);
					}
				}
				

				if(ftl.size()>=msup)
				{

					fcs.add(e);
				
					a.add(ftl.size());
					if(itemorder<=fpsize+1){
						
					for(int c:e){
						String d=itos.get(c);
						
						bw_out.write(d+' ');
					}
					bw_out.write('('+Integer.toString(ftl.size())+')');
					bw_out.newLine();
					
					}
				}
				

			}
			br.close();

			
			FreqPatterns.put(fpsize+1, fcs);

			if(fcs.isEmpty()){
				flag = true;
			}
			}
			catch(Exception e){

			}

		}
		

	}

public static void main(String args[]) {
    long startTime = System.currentTimeMillis();
	
	try {
		Apriori ap = new Apriori();
		Scanner reader = new Scanner(System.in);  
		//Read the input from user and later change to arguments
//		System.out.println("Enter minimum support ");
//		int min_sup = reader.nextInt();
//		System.out.println("Enter order of itemset ");
//		int k = reader.nextInt();	
//		System.out.println("Enter input path ");
//		input = reader.next();
//		System.out.println("Enter path to store candidate set ");
//		candiset = reader.next();
//		System.out.println("Enter path to store final frequent patterns ");
//		output = reader.next();
		
		int min_sup = Integer.parseInt(args[0]);
		int k = Integer.parseInt(args[1]);
		input = args[2];
		candiset = args[3];
		output = args[4];
		
		
		bw_out = new BufferedWriter(new FileWriter(output));
		ap.convert_reviewid();
		ap.freqOneItems(min_sup, k);
		ap.freqTwoItems(min_sup);
		while(!flag)
		{

		ap.support(k,min_sup);
		
		}
		bw_out.close();
	}
	catch(Exception e) {
		
	}
    long stopTime = System.currentTimeMillis();
    long elapsedTime = (stopTime - startTime)/60000;
    System.out.println(elapsedTime);
 }

}
