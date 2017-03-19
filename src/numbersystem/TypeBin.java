package numbersystem;

public class TypeBin implements Convertor {

	@Override
	public String getBin(String raw) {
		return raw;
	}

	@Override
	public String getDec(String raw) {
		return binDec.toBase2(raw);
	}

	@Override
	public String getHex(String raw) {
		// TODO Auto-generated method stub
		return binHex.toBase2(raw);
	}

	@Override
	public String getOct(String raw) {
		// TODO Auto-generated method stub
		return binOct.toBase2(raw);
	}


}
