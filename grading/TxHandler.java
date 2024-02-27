import java.util.ArrayList;

public class TxHandler {

    private UTXOPool utxoPool;

    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    public boolean isValidTx(Transaction tx) {
        double inputSum = 0;
        double outputSum = 0;
        ArrayList<UTXO> usedUTXOs = new ArrayList<>();

        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

            if (!utxoPool.contains(utxo) || usedUTXOs.contains(utxo)) {
                return false;
            }

            Transaction.Output output = utxoPool.getTxOutput(utxo);
            RSAKey publicKey = output.address;

            if (!publicKey.verifySignature(tx.getRawDataToSign(i), input.signature)) {
                return false;
            }

            usedUTXOs.add(utxo);
            inputSum += output.value;
        }

        for (Transaction.Output output : tx.getOutputs()) {
            if (output.value < 0) {
                return false;
            }
            outputSum += output.value;
        }

        return inputSum >= outputSum;
    }

    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> validTransactions = new ArrayList<>();

        for (Transaction tx : possibleTxs) {
            if (isValidTx(tx)) {
                validTransactions.add(tx);

                // Update the UTXO pool
                for (Transaction.Input input : tx.getInputs()) {
                    UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                    utxoPool.removeUTXO(utxo);
                }

                byte[] txHash = tx.getHash();
                int index = 0;
                for (Transaction.Output output : tx.getOutputs()) {
                    UTXO utxo = new UTXO(txHash, index++);
                    utxoPool.addUTXO(utxo, output);
                }
            }
        }

        return validTransactions.toArray(new Transaction[0]);
    }
}