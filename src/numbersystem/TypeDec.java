package numbersystem;

public class TypeDec implements Convertor {

	@Override
	public String getBin(String raw) {
		return decBin.toBase2(raw);
	}

	@Override
	public String getDec(String raw) {
		return raw;
	}

	@Override
	public String getHex(String raw) {
		return decHex.toBase2(raw);
	}

	@Override
	public String getOct(String raw) {
		return decOct.toBase2(raw);
	}
}
