package utils;

import com.github.wnameless.math.NumberSystem;
import com.github.wnameless.math.NumberSystemConverter;

import numbersystem.Convertor;
import numbersystem.TypeBin;
import numbersystem.TypeDec;
import numbersystem.TypeHex;
import numbersystem.TypeOct;

public class NumberSystemUtils {
	 static Convertor binConvertor=new TypeBin();
	 static Convertor octConvertor=new TypeOct();
	 static Convertor decConvertor=new TypeDec();
	 static Convertor hexConvertor=new TypeHex();
	 
	public static Convertor getBinConvertor() {
		return binConvertor;
	}
	public static Convertor getOctConvertor() {
		return octConvertor;
	}
	public static Convertor getDecConvertor() {
		return decConvertor;
	}
	public static Convertor getHexConvertor() {
		return hexConvertor;
	}

	 

}
