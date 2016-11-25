package numbersystem;

public class TypeHex implements Convertor {

	@Override
	public String getBin(String raw) {
		return hexBin.toBase2(raw);
	}

	@Override
	public String getDec(String raw) {
		return hexDec.toBase2(raw);
	}

	@Override
	public String getHex(String raw) {
		return raw;
	}

	@Override
	public String getOct(String raw) {
		return hexOct.toBase2(raw);
	}


}
