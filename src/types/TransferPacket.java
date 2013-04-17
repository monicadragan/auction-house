package types;

public class TransferPacket {
	
	public long transferSize;
	public int transferOffset;
	public Object product;
	public String from;
	public String to;
	public int fromRow;
	public int toRow;
	public byte buffer[];
	public int sizeBuffer;

	public TransferPacket( Object product, String from, String to, int fromRow, int toRow) {

		this.product = product;
		this.from = from;
		this.to = to;
		this.fromRow = fromRow;
		this.toRow = toRow;

	}

}
