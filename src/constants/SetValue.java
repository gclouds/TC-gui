package constants;

import numbersystem.Convertor;
import utils.NumberSystemUtils;

public enum SetValue {
	HEX{
		@Override
		public Convertor get() {
			return NumberSystemUtils.getHexConvertor();
		}
	},DEC{
		@Override
		public Convertor get() {
			return NumberSystemUtils.getDecConvertor();
		}
	},OCT{
		@Override
		public Convertor get() {
			return NumberSystemUtils.getOctConvertor();
		}
	},BIN{
		@Override
		public Convertor get() {
			return NumberSystemUtils.getBinConvertor();
		}
	};

	public abstract Convertor get();
}
