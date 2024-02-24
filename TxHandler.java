import java.util.ArrayList;

public class TxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	public TxHandler(UTXOPool utxoPool) 
	{
		//Create a (defensive deep) copy of the utxoPool object.
		UTXOPool MyPool = new UTXOPool(utxoPool);

	}
	UTXOPool internalUtxoPool = new UTXOPool(); //creates an internal UTXO pool for handleTxs function.

	/* Returns true if 
	 * (1) all outputs claimed by tx are in the current UTXO pool, 
	 * (2) the signatures on each input of tx are valid, 
	 * (3) no UTXO is claimed multiple times by tx, 
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	*/
	public boolean isValidTx(Transaction tx) {
		// IMPLEMENT THIS
		return false;
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {

		ArrayList<Transaction> ValidTxList = new ArrayList<>();
		for (int i=0; i<possibleTxs.length; i++)
		{
			Transaction myTx = possibleTxs[i];
			UTXO MyUTXO = new UTXO(myTx.getHash(), i);
			if (isValidTx(myTx))
			{
				//if the transaction is valid, add it to the ArrayList.
				ValidTxList.add(myTx);
				internalUtxoPool.addUTXO(MyUTXO, myTx.getOutput(i));

			}
			else if (!isValidTx(myTx))
			{
				//Transaction is invalid, remove it from the UTXO pool.
				internalUtxoPool.removeUTXO(MyUTXO);
			}
		}
		//Add all the transactions in the dynamic ArrayList to a static Array
		Transaction[] ValidTransactionArray = ValidTxList.toArray(new Transaction[0]);
		
		return ValidTransactionArray[] ;
	}
} 
