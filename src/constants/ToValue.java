package constants;

import com.github.wnameless.math.NumberSystemConverter;

import numbersystem.Convertor;
import utils.ReadLogger;

public enum ToValue {

	TO_HEX("'h") {
		@Override
		public String getConvertedValue(Convertor convertor, String value) throws Exception {
			try {
				return convertor.getHex(value);
			} catch (Exception e) {
				log.error(convertor.getClass()+">"+this.name()+" :: error for value: "+value, e);
				//throw e;
				return value;
			}
		}
	},TO_DEC("'d") {
		@Override
		public String getConvertedValue(Convertor convertor, String value) throws Exception {
			try {
				return convertor.getDec(value);
			} catch (Exception e) {
				log.error(convertor.getClass()+">"+this.name()+" :: error for value: "+value, e);
				//throw e;
				return value;
			}
		}
	},TO_OCT("'o") {
		@Override
		public String getConvertedValue(Convertor convertor, String value) throws Exception {
			try {
				return convertor.getOct(value);
			} catch (Exception e) {
				log.error(convertor.getClass()+">"+this.name()+" :: error for value: "+value, e);
				//throw e;
				return value;
			}
		}
	},TO_BIN("'b") {
		@Override
		public String getConvertedValue(Convertor convertor, String value) throws Exception {
			try {
				return convertor.getBin(value);
			} catch (Exception e) {
				log.error(convertor.getClass()+">"+this.name()+" :: error for value: "+value, e);
				//throw e;
				return value;
			}
		}
	};
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ToValue.class);
	public abstract String getConvertedValue(Convertor convertor,String value) throws Exception;
	private String prefix;
	private ToValue(String preFix) {
		this.prefix=preFix;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
