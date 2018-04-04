# README

Entire code is in the source file Apriori.java

Steps for execution:

1) Compile the program:

javac Apriori.java

2)Execution must be done by passing the command line arguments in the order as follows.

java Apriori min_support itemset_order(k) input_transaction_file_path candidate_gen_file_path output_file_path


3)Output will be saved in the folder where the project is residing.


Eg: 


For min_sup = 10, k = 2


java Apriori 10 2 transactionDB.txt candidate.txt output.txt


Input file is in the same folder as the source file.


