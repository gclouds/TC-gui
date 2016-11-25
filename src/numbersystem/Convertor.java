package numbersystem;

import com.github.wnameless.math.NumberSystem;
import com.github.wnameless.math.NumberSystemConverter;

public interface Convertor {
	public static final NumberSystemConverter decHex= new NumberSystemConverter(NumberSystem.DEC, NumberSystem.HEX);
	public static final NumberSystemConverter decOct= new NumberSystemConverter(NumberSystem.DEC, NumberSystem.OCT);
	public static final NumberSystemConverter decBin= new NumberSystemConverter(NumberSystem.DEC, NumberSystem.BIN);
	public static final NumberSystemConverter hexDec= new NumberSystemConverter(NumberSystem.HEX, NumberSystem.DEC);
	public static final NumberSystemConverter hexOct= new NumberSystemConverter(NumberSystem.HEX, NumberSystem.OCT);
	public static final NumberSystemConverter hexBin= new NumberSystemConverter(NumberSystem.HEX, NumberSystem.BIN);
	public static final NumberSystemConverter octHex= new NumberSystemConverter(NumberSystem.OCT, NumberSystem.HEX);
	public static final NumberSystemConverter octDec= new NumberSystemConverter(NumberSystem.OCT, NumberSystem.DEC);
	public static final NumberSystemConverter octBin= new NumberSystemConverter(NumberSystem.OCT, NumberSystem.BIN);
	public static final NumberSystemConverter binHex= new NumberSystemConverter(NumberSystem.BIN, NumberSystem.HEX);
	public static final NumberSystemConverter binDec= new NumberSystemConverter(NumberSystem.BIN, NumberSystem.DEC);
	public static final NumberSystemConverter binOct= new NumberSystemConverter(NumberSystem.BIN, NumberSystem.OCT);
	
	public abstract String getBin(String raw) throws Exception;
	public abstract String getDec(String raw) throws Exception;
	public abstract String getHex(String raw) throws Exception;
	public abstract String getOct(String raw) throws Exception;
}


/*class HideObj
{
public Convertor hide()
{
	return new TypeBin();
	
}*/

/*
class DeriveObj
{
	public static void main(String[] args) {
		HideObj hideObj=new HideObj();
		hideObj.hide();
		
	}
}*/


