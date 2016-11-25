package numbersystem;

public class TypeOct implements Convertor {

	@Override
	public String getBin(String raw) {
		return octBin.toBase2(raw);
	}

	@Override
	public String getDec(String raw) {
		return octDec.toBase2(raw);
	}

	@Override
	public String getHex(String raw) {
		return octHex.toBase2(raw);
	}

	@Override
	public String getOct(String raw) {
		return raw;
	}

}
